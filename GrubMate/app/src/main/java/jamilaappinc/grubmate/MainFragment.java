package jamilaappinc.grubmate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    MovieAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    String currUserId;
    String currUserName;
    ArrayList<String> userFriends;
    List<Post> myPost = new ArrayList<>();

    ArrayList<Integer> postCount = new ArrayList<>();
    ArrayList<Integer> postsReadCounter = new ArrayList<>();


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
        // Attach a listener to read the data at our posts reference




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        Intent i = getActivity().getIntent();
        //
        //            //reads in userid, username, and friends from loginactivity
        currUserId = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        if(currUserId.equals("") || currUserName.equals("") ||  userFriends == null){
            System.out.println("I DIDNT RECEIVE INFO FOR POPULATING MAIN FRAGMENT");
        }
        System.out.println("IN MAIN FRAGMENT CONST, USER ID IS" + currUserId + currUserName);


        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        //find views

        postsReadCounter.add(0);

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Post")) {
                        postCount.add((int)snap.getChildrenCount());
                        System.out.println("ADDED # FRIENDS, count is " + snap.getChildrenCount());
                    }
                }
                dbRefPosts.addChildEventListener(new ChildEventListener(){
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int postsRead = postsReadCounter.get(0);
                        postsRead++;
                        System.out.println("POSTS READ COUNT " + postsRead);
                        postsReadCounter.clear();
                        postsReadCounter.add(postsRead);


                        Post post = dataSnapshot.getValue(Post.class);
                        if(userFriends != null)
                        {
                            for (int i = 0; i < userFriends.size(); i++) {
                                System.out.println("I IS " + i);
                                System.out.println("POST ID IS " + post.getmId());
                                System.out.println("USER FRIENDS ID IS " + userFriends.get(i));

                                if (post.getmAuthorId().equals(userFriends.get(i))) {
                                    myPost.add(post);
                                    System.out.println("I GOT A POST!!" + post);
                                }
                            }
                        }

                        System.out.println("POSTS READ COUNTER" + postsReadCounter.get(0));
                        System.out.println("POSTS COUNTER" + postCount.get(0));

                        if (postsReadCounter.get(0) == postCount.get(0)) {
                            System.out.println("IN IF, SETTING ADAPTER NOW");
                            mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, myPost);
                            mListView = (ListView)v.findViewById(R.id.active_post_list);
                            //        postList = PostSingleton.get(getActivity()).getMovies();
                            //        postAdapter = new PostListAdapter(getActivity(), Post.class,
                            //                R.layout.list_active_posts_item,
                            //                dbRefPosts);
                            mListView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
//                        // we know a row was clicked but we need to know WHERE specifically
//                        // is that data stored in the database
//
//                        DatabaseReference dbRefClicked = mAdapter.getRef(position);
//                        Intent i = new Intent(getActivity(), DetailedPostActivity.class);
//                        // toString instead of sending over the whole DatabaseReference because it's easier
//                        i.putExtra("ID", currUserId);
//                        i.putExtra(DetailedPostActivity.EXTRA_URL, dbRefClicked.toString());
//                        startActivity(i);
//                    }
//                });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                System.out.println("IN MAIN FRAGMENT, USER ID IS" + currUserId);
                intent.putExtra("ID", currUserId);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);

                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.menu_from_main, getActivity()))
                .setContentTitle("Menu button")
                .setContentText("Click to see menu options")
                .hideOnTouchOutside()
                .build();
    }

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    //    //todo onDetach
    //    public void onDetach() {
    //        super.onDetach();
    //        mAdapter.cleanup();
    //    }

    //todo create custom FirebaseListAdapter


    //    private class PostListAdapter extends FirebaseListAdapter<Post> {
    //
    //        public PostListAdapter(Activity activity, Class<Post> modelClass, int modelLayout, DatabaseReference ref) {
    //            super(activity, modelClass, modelLayout, ref);
    //        }
    //
    ////        private PostFilter postFilter;
    //
    //        @Override
    //        protected void populateView(View v, Post model, int position) {
    //            // get references to row widgets
    //            // copy data from model to widgets
    //            boolean isFriendPost = false;
    //            //
    //
    //
    //
    //            //            for (int j = 0; j < userFriends.size(); j++) {
    //            //                String x = model.getmAuthorId();
    //            //
    //            //                if (x.equals(userFriends.get(j))) {
    //            //
    //            //                    System.out.println("this_is_true");
    //            isFriendPost = true;
    //            //                }
    //            //            }
    //
    //            //System.out.println("USFRIENDPOST" + isFriendPost);
    //            if (isFriendPost) {
    //                ImageView mImage = (ImageView)v.findViewById(R.id.imagePic);
    //
    //                Glide.with(MainFragment.this)
    //                        .load(model.getmPhotos())
    //                        .centerCrop()
    //                        .placeholder(R.drawable.hamburger)
    //                        .crossFade()
    //                        .into(mImage);
    //
    //                TextView pPostContent = (TextView)v.findViewById(R.id.listNoteContent);
    //                TextView pPostTitle = (TextView)v.findViewById(R.id.listNoteTitle);
    //
    //                pPostContent.setText(model.getmDescription());
    //                pPostTitle.setText(model.getmTitle());
    //            }
    //        }

    //        @Override
    //        public Filter getFilter() {
    //            if (postFilter == null) {
    //                postFilter = new PostFilter();
    //            }
    //            return postFilter;
    //        }
    //
    //        private class PostFilter extends Filter {
    //
    //            @Override
    //            protected FilterResults performFiltering(CharSequence constraint) {
    //                FilterResults filterResults = new FilterResults();
    //                Query query;
    //                if (constraint != null && constraint.length() > 0) {
    //                    query = FirebaseHelper.getCustomerRef().orderByChild("name").equalTo(constraint.toString());
    //                } else {
    //                    query = FirebaseHelper.getCustomerRef().orderByChild("name");
    //                }
    //                filterResults.values = query;
    //                return filterResults;
    //            }
    //
    //            @Override
    //            protected void publishResults(CharSequence constraint, FilterResults results) {
    //                query = (Query) results.values;
    //            }
    //        }

    public class MovieAdapter extends ArrayAdapter<Post> {
        List<Post> Posts;
        Context context;

        public MovieAdapter(Context context, int resource, List<Post> objects) {
            super(context, resource, objects);
            this.context = context;
            this.Posts = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_active_posts_item, null);
            }

            ImageView image = (ImageView)convertView.findViewById(R.id.imagePic);
            ImageView imagePerson = (ImageView)convertView.findViewById(R.id.active_post_person_image);
            TextView textTitle = (TextView)convertView.findViewById(R.id.listNoteTitle);
            TextView textDescription = (TextView)convertView.findViewById(R.id.listNoteContent);

            //            findOutMore.setTag(position);
            //            findOutMore.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    Intent i = new Intent(getActivity(),
            //                            DetailActivity.class);
            //
            //                    //TODO change position to id
            ////                i.putExtra(DetailActivity.EXTRA_POSITION, position);
            //                    i.putExtra(DetailActivity.ARGS_ID, (int) v.getTag());
            //                    startActivityForResult(i, 0);
            //                }
            //            });

            final Post mv = Posts.get(position);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
                    // we know a row was clicked but we need to know WHERE specifically
                    // is that data stored in the database

                    Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                    // toString instead of sending over the whole DatabaseReference because it's easier
                    i.putExtra("ID", currUserId);
                    i.putExtra(DetailedPostActivity.EXTRA_POST, mv);
                    startActivity(i);
                }
            });



            Glide.with(MainFragment.this)
                    .load( mv.getmPhotos())
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(image);

            textTitle.setText(mv.getmTitle());
            textDescription.setText(mv.getmDescription());
            return convertView;
        }
    }

}