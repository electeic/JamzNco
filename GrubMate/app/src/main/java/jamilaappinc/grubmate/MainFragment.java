package jamilaappinc.grubmate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
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

    //    private List<Post> postList;
    //    private PostListAdapter postAdapter;
    android.support.design.widget.FloatingActionButton floatButton;
    ListView mListView;
    public static final String IDString = "fuck";


    FirebaseListAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    String currUserId;
    String currUserName;
    ArrayList<String> userFriends;

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

        //        //todo get database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);
        //todo get database reference paths

        Intent i = getActivity().getIntent();
        //
        //            //reads in userid, username, and friends from loginactivity
        currUserId = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        System.out.println("IN MAIN FRAGMENT CONST, USER ID IS" + currUserId + currUserName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Toast.makeText(getActivity().getApplicationContext(), "Click on a post for more information!.", Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(), "Click on the logo on the right for more options!", Toast.LENGTH_LONG).show();

        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);


        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        //find views
        mAdapter = new PostListAdapter(getActivity(), Post.class, R.layout.list_active_posts_item, dbRefPosts);
        mListView = (ListView)v.findViewById(R.id.active_post_list);
        //        postList = PostSingleton.get(getActivity()).getMovies();
        //        postAdapter = new PostListAdapter(getActivity(), Post.class,
        //                R.layout.list_active_posts_item,
        //                dbRefPosts);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
                // we know a row was clicked but we need to know WHERE specifically
                // is that data stored in the database

                DatabaseReference dbRefClicked = mAdapter.getRef(position);
                Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                // toString instead of sending over the whole DatabaseReference because it's easier
                i.putExtra(DetailedPostActivity.EXTRA_URL, dbRefClicked.toString());
                startActivity(i);
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                System.out.println("IN MAIN FRAGMENT, USER ID IS" + currUserId);
                intent.putExtra("ID", currUserId);
                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });

        // Attach a listener to read the data at our posts reference
        dbRefPosts.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return v;
    }

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    //todo onDetach
    public void onDetach() {
        super.onDetach();
        mAdapter.cleanup();
    }

    //todo create custom FirebaseListAdapter


    private class PostListAdapter extends FirebaseListAdapter<Post> {

        public PostListAdapter(Activity activity, Class<Post> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, Post model, int position) {
            // get references to row widgets
            // copy data from model to widgets
            boolean isFriendPost = false;
            //



            //            for (int j = 0; j < userFriends.size(); j++) {
            //                String x = model.getmAuthorId();
            //
            //                if (x.equals(userFriends.get(j))) {
            //
            //                    System.out.println("this_is_true");
            isFriendPost = true;
            //                }
            //            }

            //System.out.println("USFRIENDPOST" + isFriendPost);
            if (isFriendPost) {
                ImageView mImage = (ImageView)v.findViewById(R.id.imagePic);

                Glide.with(MainFragment.this)
                        .load(model.getmPhotos())
                        .centerCrop()
                        .placeholder(R.drawable.hamburger)
                        .crossFade()
                        .into(mImage);

                TextView pPostContent = (TextView)v.findViewById(R.id.listNoteContent);
                TextView pPostTitle = (TextView)v.findViewById(R.id.listNoteTitle);


                pPostContent.setText(model.getmTitle());
                pPostTitle.setText(model.getmDescription());
            }
        }
    }

    //    public class PostListAdapter extends ArrayAdapter<Post> {
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
    //            //            findOutMore.setTag(position);
    //            //            findOutMore.setOnClickListener(new View.OnClickListener() {
    //            //                @Override
    //            //                public void onClick(View v) {
    //            //                    Intent i = new Intent(getActivity(),
    //            //                            DetailActivity.class);
    //            //
    //            //                    //TODO change position to id
    //            ////                i.putExtra(DetailActivity.EXTRA_POSITION, position);
    //            //                    i.putExtra(DetailActivity.ARGS_ID, (int) v.getTag());
    //            //                    startActivityForResult(i, 0);
    //            //                }
    //            //            });
    //
    //            //            Movies = MovieSingleton.get(context).getMovies();
    //            Post mv = Posts.get(position);
    //
    //            image.setImageDrawable(getResources().getDrawable(R.drawable.gmlogo));
    //
    //
    //            textTitle.setText(mv.getmTitle());
    //            textDescription.setText(mv.getmDescription());
    //            return convertView;
    //        }
    //    }

}
