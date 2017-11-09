package jamilaappinc.grubmate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;
import android.net.Uri;
import com.facebook.FacebookSdk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.facebook.CallbackManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    android.support.design.widget.FloatingActionButton floatButton;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String TAG = "TAG";
//    FirebaseListAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefUsers, dbRefReviews, dbRefPosts;

    private TextView nameText;
    private TextView ratingText;
    private ListView myPosts, myRating;
    ImageView myImage;
    String ID;
    String friendPic;

    String friend;

    private User currUser, profileUser; //profile user is the user whose profile we're lookign at

    CallbackManager callbackManager;




    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private View mMessengerButton;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;

    private ArrayList<Rating> myRatings = new ArrayList<>(); //holds all my reviews
    private ArrayList<Integer> reviewCount = new ArrayList<>(); // holds the num of reviews so that we can create reviewAdapter
    RatingAdapter ratingAdapter;


    ArrayList<Integer> postsReadCounter = new ArrayList<>();
    ArrayList<Integer> postCount = new ArrayList<>();
    MovieAdapter mAdapter;
    private ArrayList<Post> mPosts = new ArrayList<>();




    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(int pos) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        //        if (getArguments() != null) {
        //            mParam1 = getArguments().getString(ARG_PARAM1);
        //            mParam2 = getArguments().getString(ARG_PARAM2);
        //        }

        database = FirebaseDatabase.getInstance();
        dbRefUsers = database.getInstance().getReference().child("Users");
        dbRefReviews = database.getInstance().getReference().child("Rating");
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);


        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

            // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
            // It will contain the metadata associated with the original content, if there was content.
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        //get intent data
        Intent i = getActivity().getIntent();
        final String id = i.getStringExtra("ID");
        final String pic = i.getStringExtra("MyPic");
        final String currUserName = i.getStringExtra("Name");
        friend = i.getStringExtra("Friend");
        final String friendPic = i.getStringExtra("Picture");
        System.out.println("my id: " + id + " pic: " + pic + " currUserName: " + currUserName);
       // status = i.getStringExtra("alreadyLoggedIn");
        final ArrayList<String> userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        
//        final Intent myIntent = new Intent((getActivity()), ProfileActivity.class);
//        getActivity().startActivity(myIntent);

//         If we received Intent.ACTION_PICK from Messenger, we were launched from a composer shortcut
//         or the reply flow.
//        Intent intent = getActivity().getIntent();
//        if (Intent.ACTION_PICK.equals(intent.getAction())) {
//            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
//            mPicking = true;

//             Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
//             It will contain the metadata associated with the original content, if there was content.
//        }
        nameText = (TextView) v.findViewById(R.id.nameText);
        ratingText = (TextView) v.findViewById(R.id.profile_actualRating);
        myPosts = (ListView) v.findViewById(R.id.profile_postList);
        myImage = (ImageView) v.findViewById(R.id.profile_pic);
        myRating = (ListView)v.findViewById(R.id.profile_ratingsList);

        System.out.println("READING DB NOW...");
        if(friend == null) {
            ID = id;
            Glide.with(ProfileFragment.this)
                    .load(pic)
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(myImage);
        }

        else {
            ID = friend;
            Glide.with(ProfileFragment.this)
                    .load(friendPic)
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(myImage);
        }

        dbRefUsers.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println(user.getFriends() + user.getId() + user.getName());
                System.out.println("ID SENT OVER IS " + id);
                System.out.println("USER's ID IS" + user.getId());
                if (user.getId().equals(id)) { //current user
                    if(friend == null) {
                        profileUser = user;
                        nameText.setText(user.getName());
                        ratingText.setText(new DecimalFormat("#.##").format(user.getAvgRating()));
                    }
                    currUser = user;

                }
                if(friend != null){ //looking at friend profile
                    if(user.getId().equals(friend)) {
                        profileUser = user;
                        System.out.println("viewing friend's profile");
                        nameText.setText(user.getName());
                        ratingText.setText(new DecimalFormat("#.##").format(user.getAvgRating()));

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users",userFriends);
                intent.putExtra("MyPic", pic);
               // intent.putExtra("Status", status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        mMessengerButton = v.findViewById(R.id.messenger_send_button_blue);

        mMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }

            private void onMessengerButtonClicked() {
                // The URI can reference a file://, content://, or android.resource. Here we use
                // android.resource for sample purposes.
                Uri uri =
                        Uri.parse("android.resource://com.numetriclabz.messengerdemo/" + R.drawable.tree);

                // Create the parameters for what we want to send to Messenger.
                ShareToMessengerParams shareToMessengerParams =
                        ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                                .setMetaData("{\"image\" : \"tree\"}")
                                .build();

                if (mPicking) {
                    // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
                    // the content to Messenger.
                    System.out.println("let's go to messenger");
                    MessengerUtils.finishShareToMessenger(getActivity(), shareToMessengerParams);
                }

                else {
                    // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
                    // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
                    // to be upgraded, this will direct the user to the play store.
                    System.out.println("did not go to if in messenger part");
                    MessengerUtils.shareToMessenger(getActivity(), REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
                }
            }
        });

        reviewCount.add(0);
        dbRefReviews.orderByChild("rateeID").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int numReview = (int)dataSnapshot.getChildrenCount();

                dbRefReviews.orderByChild("rateeID").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            int reviewsRead = reviewCount.get(0);
                            reviewsRead++;
                            reviewCount.clear();
                            reviewCount.add(reviewsRead);
                            Rating rating = child.getValue(Rating.class);
                            myRatings.add(rating);
                            if(reviewCount.get(0) == numReview){
                                ratingAdapter = new RatingAdapter(getActivity());
                                myRating.setAdapter(ratingAdapter);

                            }

                        }
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


        postsReadCounter.add(0);
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Post")) { //if it
                        postCount.add((int)snap.getChildrenCount());
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

                        if(post.getmAuthorId().equals(profileUser.getId())){
                            mPosts.add(post);
                        }


                        if(postsReadCounter.get(0) == postCount.get(0)){
                            mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, mPosts);
                            myPosts.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
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


        return v;
    }

    public class RatingAdapter extends ArrayAdapter<Rating> {
        Context context;

        public RatingAdapter(Context context) {
            super(context, 0, myRatings);
            this.context = context;
        }

        @Override
        public View getView(int positions, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.rating_and_review, null);
            }

            ImageView image = (ImageView)convertView.findViewById(R.id.review_Pic);
            TextView nameText = (TextView) convertView.findViewById(R.id.review_name);
            TextView ratingText = (TextView)convertView.findViewById(R.id.review_rating);
            TextView reviewText = (TextView)convertView.findViewById(R.id.review_review);

            final Rating rating = myRatings.get(positions);
            Glide.with(ProfileFragment.this)
                    .load("https://graph.facebook.com/"+rating.getRaterID()+"/picture?type=large&width=1080")
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(image);
            nameText.setText(rating.getPersonRatingName());
            ratingText.setText(rating.getRating()+"");
            reviewText.setText(rating.getReview());


            return convertView;
        }
    }

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

            ImageView image = (ImageView) convertView.findViewById(R.id.imagePic);
            ImageView imagePerson = (ImageView) convertView.findViewById(R.id.active_post_person_image);
            TextView textTitle = (TextView) convertView.findViewById(R.id.listNoteTitle);
            TextView textDescription = (TextView) convertView.findViewById(R.id.listNoteContent);
            friendPic = imagePerson.toString();

            final Post mv = Posts.get(position);
           myPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
                    // we know a row was clicked but we need to know WHERE specifically
                    // is that data stored in the database

                    Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                    // toString instead of sending over the whole DatabaseReference because it's easier
                    if(friend == null) {
                        i.putExtra("ID", currUser.getId());
                        i.putExtra("Name",currUser.getName());
                        i.putExtra("Users", currUser.getFriends());
                        i.putExtra("Friend", friendPic);
                        i.putExtra(DetailedPostActivity.EXTRA_POST, Posts.get(position));
                        startActivity(i);

                    }

                    else if(friend.equals(ID)) {
                        i.putExtra("ID", ID);
                        i.putExtra("Friend", friendPic);
                        i.putExtra(DetailedPostActivity.EXTRA_POST, Posts.get(position));
                        startActivity(i);
                    }
                }
            });


            Glide.with(ProfileFragment.this)
                    .load( mv.getmPhotos())
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(image);

            Glide.with(ProfileFragment.this)
                    .load( mv.getmAuthorPic())
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(imagePerson);


            textTitle.setText(mv.getmTitle());
            textDescription.setText(mv.getmDescription());
            return convertView;
        }
    }

}
