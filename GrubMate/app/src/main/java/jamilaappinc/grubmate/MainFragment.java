package jamilaappinc.grubmate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final String ARGS_POSITION = "args_position";

    private List<Post> postList;
    private PostListAdapter postAdapter;
    ListView mListView;

    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    DatabaseReference dbRefCount;
    FirebaseListAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    //TODO modify for id
    public static MainFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, pos);
        MainFragment f = new MainFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo get database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference(FirebaseReferences.POSTS);
        //todo get database reference paths
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //find views
        mListView = (ListView)v.findViewById(R.id.active_post_list);

        postList = PostSingleton.get(getActivity()).getMovies();
        postAdapter = new PostListAdapter(getActivity(), Post.class,
                R.layout.list_active_posts_item,
                dbRefPosts);
        mListView.setAdapter(postAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // we know a row was clicked but we need to know WHERE specifically
                // is that data stored in the database
                DatabaseReference dbRefClicked = mAdapter.getRef(position);
                Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                // toString instead of sending over the whole DatabaseReference because it's easier
                i.putExtra(DetailedPostActivity.EXTRA_URL, dbRefClicked.toString());
                startActivity(i);
            }
        });



        return v;
    }

    public void refresh(){
        postAdapter.notifyDataSetChanged();
    }

    //todo create custom FirebaseListAdapter


    public class PostListAdapter extends FirebaseListAdapter<Post>{

        public PostListAdapter(Activity activity, Class<Post> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, Post model, int position) {
            // get references to row widgets
            // copy data from model to widgets
//            ImageView pFoodPic = (ImageView) v.findViewById(R.id.imagePic);
//            ImageView pPersonPic = (ImageView) v.findViewById(R.id.active_post_person_image);
            TextView pPostContent = (TextView) v.findViewById(R.id.listNoteContent);
            TextView pPostTitle = (TextView) v.findViewById(R.id.listNoteTitle);

//            pFoodPic.setImageBitmap(model.getmPhotos());
//            pPersonPic.setImageBitmap(model.getmPhotos());
            pPostContent.setText(model.getmTitle());
            pPostTitle.setText(model.getmDescription());
        }

    }

//    public class PostListAdapter extends ArrayAdapter<Post>{
//        List<Post> Posts;
//        Context context;
//
//        public PostListAdapter(Context context, int resource, List<Post> objects) {
//            super(context, resource, objects);
//            this.context = context;
//            this.Posts = objects;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater().inflate(
//                        R.layout.list_active_posts_item, null);
//            }
//
//            ImageView image = (ImageView) convertView.findViewById(R.id.imagePic);
//            ImageView imagePerson = (ImageView) convertView.findViewById(R.id.active_post_person_image);
//            TextView textTitle = (TextView) convertView.findViewById(R.id.listNoteTitle);
//            TextView textDescription = (TextView) convertView.findViewById(R.id.listNoteContent);
//
////            findOutMore.setTag(position);
////            findOutMore.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent i = new Intent(getActivity(),
////                            DetailActivity.class);
////
////                    //TODO change position to id
//////                i.putExtra(DetailActivity.EXTRA_POSITION, position);
////                    i.putExtra(DetailActivity.ARGS_ID, (int) v.getTag());
////                    startActivityForResult(i, 0);
////                }
////            });
//
////            Movies = MovieSingleton.get(context).getMovies();
//            Post mv = Posts.get(position);
//
//            image.setImageDrawable(getResources().getDrawable(R.drawable.gmlogo));
//
//
//
//            textTitle.setText(mv.getmTitle());
//            textDescription.setText(mv.getmDescription());
//            return convertView;
//        }
//    }
}
