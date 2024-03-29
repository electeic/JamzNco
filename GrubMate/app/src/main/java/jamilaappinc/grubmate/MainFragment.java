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
import java.util.Comparator;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Adapter;
import java.util.Collections;
import java.util.Random;

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
    SwipeRefreshLayout mSwipeRefreshLayout;
    MovieAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    DatabaseReference dbRefUsers;

    DatabaseReference databaseRef;
    String currUserId;
    String currUserName;
    String currPicture;
    String friendPic;
    ArrayList<String> userFriends;
    List<Post> myPost = new ArrayList<>();
    List<User> myUser = new ArrayList<>();

    ArrayList<Integer> postCount = new ArrayList<>();
    ArrayList<Integer> postsReadCounter = new ArrayList<>();
    ArrayList<Post> receivedPosts = new ArrayList<>();
    ArrayList<Integer> userCount = new ArrayList<>();
    ArrayList<Integer> userReadCounter = new ArrayList<>();

    Button sortByTitleButton, sortByEndTimeButton, sortByStartTimeButton, sortByRatingButton, sortByPopularityButton, mapButton;


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
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);

        databaseRef = database.getReference();
        // dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
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
        currPicture = i.getStringExtra("MyProfilePicture");
        receivedPosts = (ArrayList<Post>) i.getSerializableExtra("ReceivedPosts");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        //status = i.getStringExtra("Status");
        //System.out.println("cvghdjksnl;, " + status);
//        if(currUserId.equals("") || currUserName.equals("") ||  userFriends == null){
//            System.out.println("I DIDNT RECEIVE INFO FOR POPULATING MAIN FRAGMENT");
//        }
//        System.out.println("IN MAIN FRAGMENT CONST, USER ID IS" + currUserId + currUserName);


        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        sortByTitleButton = (Button) v.findViewById(R.id.orderByTitle);
        sortByEndTimeButton = (Button) v.findViewById(R.id.orderByEndTime);
        sortByStartTimeButton = (Button) v.findViewById(R.id.orderByStartTime);
       // mListView = (ListView) v.findViewById(R.id.active_post_list);
        sortByRatingButton = (Button) v.findViewById(R.id.orderByRating);
        sortByPopularityButton = (Button) v.findViewById(R.id.orderByPopularity);
        mapButton = (Button) v.findViewById(R.id.mapButton);

        //find views

        postsReadCounter.add(0);
        userReadCounter.add(0);

        databaseCall(v);

        mapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapViewActivity.class);
                intent.putExtra("ID", currUserId);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                ArrayList<Post> sendPosts = new ArrayList<Post>();
                for(int i=0; i < myPost.size(); i++){
                    sendPosts.add(myPost.get(i));
                }
                System.out.println("meldoy received post size is "+ myPost.size()+" "+sendPosts.size());

                intent.putExtra("Posts", sendPosts);
                startActivityForResult(intent, 0);

            }
        });


        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                System.out.println("IN MAIN FRAGMENT, USER ID IS" + currUserId);
                intent.putExtra("ID", currUserId);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                System.out.println("meldoy tge currusername on main is "+ currUserName);
                intent.putExtra("MyProfilePicture", currPicture);
//                intent.putExtra("Status",status);

                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });

        sortByTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmTitle().compareToIgnoreCase(post2.getmTitle());
                    }
                });
                refresh();
            }
        });

        sortByStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmStartDate().compareTo(post2.getmStartDate());
                    }
                });
                refresh();
            }
        });

        sortByEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmEndDate().compareTo(post2.getmEndDate());
                    }
                });
                refresh();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, myPost);
//                mAdapter.notifyDataSetChanged();
//                mListView.setAdapter(mAdapter);
                databaseCall(v);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    public void databaseCall(final View v) {
        if (receivedPosts == null) {
            database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (snap.getKey().equals("Post")) {
                            postCount.add((int) snap.getChildrenCount());
                        }
                        else if(snap.getKey().equals("Users")){
                            userCount.add((int) snap.getChildrenCount());
                        }
                    }
                    dbRefPosts.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            int postsRead = postsReadCounter.get(0);
                            postsRead++;
                            postsReadCounter.clear();
                            postsReadCounter.add(postsRead);

                            Post post = dataSnapshot.getValue(Post.class);
                            if (userFriends != null) {
                                for (int i = 0; i < userFriends.size(); i++) {
                                    if (post.getmAuthorId().equals(userFriends.get(i))) {
                                        if (post.getTargetUsersString() != null && post.getTargetUsersString() != "") {//if have a group, then find the groups and see if user is a part of them
                                            if (post.getTargetUsersString().contains(currUserId)) {
                                                myPost.add(post);
                                            }
                                        } else {
                                            myPost.add(post);
                                        }
                                    }
                                }

                                //receivedPosts = myPost;
                            }

//                        System.out.println("POSTS READ COUNTER" + postsReadCounter.get(0));
//                        System.out.println("POSTS COUNTER" + postCount.get(0));

                            if (postsReadCounter.get(0) == postCount.get(0)) {
//                                mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, myPost);
//                                mListView = (ListView) v.findViewById(R.id.active_post_list);
                                //        postList = PostSingleton.get(getActivity()).getMovies();
                                //        postAdapter = new PostListAdapter(getActivity(), Post.class,
                                //                R.layout.list_active_posts_item,
                                //                dbRefPosts);
//                                mListView.setAdapter(mAdapter);
//
//                                else {
                                mListView = (ListView) v.findViewById(R.id.active_post_list);
                                mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, myPost);
                                mAdapter.notifyDataSetChanged();
                                mListView.setAdapter(mAdapter);
//                                }
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

        } else {
            mListView = (ListView) v.findViewById(R.id.active_post_list);
            mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, receivedPosts);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
        }

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                System.out.println("IN MAIN FRAGMENT, USER ID IS" + currUserId);
                intent.putExtra("ID", currUserId);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                System.out.println("meldoy tge currusername on main is "+ currUserName);
                intent.putExtra("MyProfilePicture", currPicture);
//                intent.putExtra("Status",status);

                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });

        sortByTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmTitle().compareToIgnoreCase(post2.getmTitle());
                    }
                });
                refresh();
            }
        });

        sortByStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmStartDate().compareTo(post2.getmStartDate());
                    }
                });
                refresh();
            }
        });

        sortByEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmEndDate().compareTo(post2.getmEndDate());
                    }
                });
                refresh();
            }
        });

        sortByRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in sort by rating button");
                dbRefUsers.addChildEventListener(new ChildEventListener(){
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (userReadCounter.get(0) != userCount.get(0)) {
                            int usersRead = userReadCounter.get(0);
                            usersRead++;
                            userReadCounter.clear();
                            userReadCounter.add(usersRead);
                            System.out.println("users read is " + usersRead);
                            User user = dataSnapshot.getValue(User.class);
                            myUser.add(user);
                        }
                        if (userReadCounter.get(0) == userCount.get(0)) {//all users read
                            Collections.sort(myUser, new Comparator<User>() {//sorts all user by order of rating
                                @Override
                                public int compare(User user1, User user2)
                                {
                                    return Double.compare(user1.getAvgRating(),user2.getAvgRating());
                                }
                            });
                            for(int i = 0; i < myPost.size(); i++){
                                for(int j = 0; j < myUser.size(); j++){//goes through all users (in order of rating)
                                    if(myPost.get(i).getmAuthorId().equals(myUser.get(j).getId())){
                                        myPost.get(i).setPriority(j);//sets post's priority according to the index of author (sorted by rating)
                                    }
                                }
                            }

                            Collections.sort(myPost, new Comparator<Post>() {//resort posts by priority
                                @Override
                                public int compare(Post post2, Post post1)
                                {
                                    return Integer.compare(post1.getPriority(), post2.getPriority());
                                }
                            });
                            refresh();

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
        });

        sortByEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2)
                    {
                        return  post1.getmEndDate().compareTo(post2.getmEndDate());
                    }
                });
                refresh();
            }
        });

        sortByPopularityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in sort by popularity button");
                dbRefUsers.addChildEventListener(new ChildEventListener(){
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (userReadCounter.get(0) != userCount.get(0)) {
                            int usersRead = userReadCounter.get(0);
                            usersRead++;
                            userReadCounter.clear();
                            userReadCounter.add(usersRead);
                            System.out.println("users read is " + usersRead);
                            User user = dataSnapshot.getValue(User.class);
                            myUser.add(user);
                        }

                        if (userReadCounter.get(0) == userCount.get(0)) {//all users read
                            Collections.sort(myUser, new Comparator<User>() {//sorts all user by order of rating
                                @Override
                                public int compare(User user1, User user2)
                                {
                                    return Double.compare(user1.getNumRatings(),user2.getNumRatings());
                                }
                            });
                            for(int i = 0; i < myPost.size(); i++){
                                for(int j = 0; j < myUser.size(); j++){//goes through all users (in order of rating)
                                    if(myPost.get(i).getmAuthorId().equals(myUser.get(j).getId())){
                                        myPost.get(i).setPriority(j);//sets post's priority according to the index of author (sorted by rating)
                                    }
                                }
                            }

                            Collections.sort(myPost, new Comparator<Post>() {//resort posts by priority
                                @Override
                                public int compare(Post post2, Post post1)
                                {
                                    return Integer.compare(post1.getPriority(), post2.getPriority());
                                }
                            });
                            refresh();


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
        });

        //return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference temp = databaseRef.child("Users").child(currUserId);
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("alreadyLoggedIn")) {
                        String update = child.getValue(String.class);
                        if(child.getValue(String.class).equals("0")){//update == "0") {
                            new ShowcaseView.Builder(getActivity())
                                    .setTarget(new ViewTarget(R.id.menu_from_main, getActivity()))
                                    .setContentTitle("Menu button")
                                    .setContentText("Click to see menu options")
                                    .hideOnTouchOutside()
                                    .build();
                        }
                    }
                }

                if(dataSnapshot.getChildrenCount() == 0) {
                    new ShowcaseView.Builder(getActivity())
                            .setTarget(new ViewTarget(R.id.menu_from_main, getActivity()))
                            .setContentTitle("Menu button")
                            .setContentText("Click to see menu options")
                            .hideOnTouchOutside()
                            .build();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void refresh() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public class MovieAdapter extends ArrayAdapter<Post> {
        List<Post> Posts;
        Context context;

        public MovieAdapter(Context context, int resource, List<Post> objects) {
            super(context, resource, objects);
            this.context = context;
            this.Posts = objects;
           // this.Posts.clear();
            //this.Posts.addAll(Posts);
            //notifyDataSetChanged();
        }

        @Override
        public View getView(int positions, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_active_posts_item, null);
            }

            ImageView image = (ImageView)convertView.findViewById(R.id.imagePic);
            ImageView imagePerson = (ImageView)convertView.findViewById(R.id.active_post_person_image);
//            imagePerson.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                    System.out.println("IN MAIN FRAGMENT, USER ID IS" + currUserId);
//                    intent.putExtra("ID", currUserId);
//                    intent.putExtra("Users", userFriends);
//                    intent.putExtra("Name", currUserName);
//                    intent.putExtra("MyProfilePicture", currPicture);
////                intent.putExtra("Status",status);
//
//                    startActivityForResult(intent, 0);
//                }
//            });
            TextView textTitle = (TextView)convertView.findViewById(R.id.listNoteTitle);
            TextView textDescription = (TextView)convertView.findViewById(R.id.listNoteContent);
            friendPic = imagePerson.toString();
            final Post mv = Posts.get(positions);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
                    // we know a row was clicked but we need to know WHERE specifically
                    // is that data stored in the database

                    Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                    // toString instead of sending over the whole DatabaseReference because it's easier
                    i.putExtra("ID", currUserId);
                    i.putExtra("Name",currUserName);
                    i.putExtra("Users", userFriends);
                    i.putExtra("Friend", friendPic);
                    i.putExtra("CurrPic", currPicture);
                    i.putExtra(DetailedPostActivity.EXTRA_POST, Posts.get(position));
                    startActivity(i);
                }
            });

            String f = mv.getmPhotos();
            if(mv.getmAllFoodPics() != null)
            {
                f= mv.getmAllFoodPics().get(0);
                System.out.println("GETTING PHOTO 0");
            }

            Glide.with(MainFragment.this)
                    .load("https://graph.facebook.com/"+mv.getmAuthorId()+"/picture?type=large&width=1080")
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(imagePerson);

            Glide.with(MainFragment.this)
                    .load(f)
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