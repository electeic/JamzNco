package jamilaappinc.grubmate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static jamilaappinc.grubmate.R.id.view;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostsFragment extends Fragment {
    private ListView list;
//    private PostListAdapter adapter;
    private ArrayList<Post> mPosts = new ArrayList<>();
    android.support.design.widget.FloatingActionButton floatButton;
//    HashSet<String>
    int _position;

    FirebaseDatabase database;
    DatabaseReference dbRefUsers;
    DatabaseReference dbRefPosts;
    private String ID;
    private String currUserName;
    ArrayList<Integer> postsReadCounter = new ArrayList<>();
    ArrayList<Integer> postCount = new ArrayList<>();
    ArrayList<String> userFriends;
    MovieAdapter mAdapter;
    private String status;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    public static MyPostsFragment newInstance(ArrayList<Post> posts){
        MyPostsFragment fragment = new MyPostsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MyPostsActivity.GET_POSTS, posts);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_my_posts, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        currUserName = i.getStringExtra("Name");
        status = i.getStringExtra("Status");

        // Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);

        postsReadCounter.add(0);

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
//                // we know a row was clicked but we need to know WHERE specifically
//                // is that data stored in the database
//
//                DatabaseReference dbRefClicked = mAdapter.getRef(position);
//                Intent i = new Intent(getActivity(), DetailedPostActivity.class);
//                // toString instead of sending over the whole DatabaseReference because it's easier
//                i.putExtra("ID", currUserId);
//                i.putExtra(DetailedPostActivity.EXTRA_URL, dbRefClicked.toString());
//                startActivity(i);
//            }
//        });

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
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

                        if(post.getmAuthorId().equals(ID)){
                            if(post.getmActive()){
                                mPosts.add(post);
                            }
                        }


                        if(postsReadCounter.get(0) == postCount.get(0)){
                            System.out.println("IN IF, SETTING ADAPTER NOW");
                            mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, mPosts);
                            //        postList = PostSingleton.get(getActivity()).getMovies();
                            //        postAdapter = new PostListAdapter(getActivity(), Post.class,
                            //                R.layout.list_active_posts_item,
                            //                dbRefPosts);
                            list.setAdapter(mAdapter);
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

//        database.getInstance().getReference().child("users")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Post myPost = snapshot.getValue(Post.class);
//                            posts.add(myPost);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//        // Attach a listener to read the data at our posts reference
//        dbRefMyPosts.addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String post = dataSnapshot.getValue(String.class);
//                    System.out.println(post);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });



        initComponents(v);
//        if(posts!=null){
//            list.setAdapter(adapter);
//        }
        addListeners();
        return v;
    }

    private void initComponents(View v){
//        mPosts = (ArrayList<Post>)getArguments().getSerializable(MyPostsActivity.GET_POSTS);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        list = (ListView) v.findViewById(R.id.mypost_list);
//        adapter= new PostListAdapter(getActivity(), Post.class, R.layout.list_active_posts_item, dbRefMyPosts);

    }

    private void addListeners() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              Intent i = getActivity().getIntent();
                ID = i.getStringExtra("ID");*/
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Status", status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Post post = (Post) list.getItemAtPosition(position);
                final ArrayList<Post> myPost = new ArrayList<Post>();
                myPost.add(post);
                System.out.println("meldoy the post is " + post.getmId() + " " + post.getmTitle());
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("");
                adb.setMessage("What would you like to do? ");
//                adb.setNegativeButton("Cancel", null);

                final int posi = position;
                /**
                 * !!!!this will be changed to whatever the condition it is when you can delete the post
                 */


                adb.setNegativeButton("Delete Post", new AlertDialog.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        if (post.getmAuthorId().equals(ID) && post.getmAcceptedUsers().size() == 1) {
                            dbRefPosts.child(post.getmFirebaseKey()).removeValue();
                        }
                    }
                });

                adb.setNeutralButton("edit post", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        DatabaseReference dbRefClicked = dbRefPosts.child(post.getmFirebaseKey());
                        Intent i = new Intent(getActivity(), PostActivity.class);
                        //dbRefClicked.toString()
                        // toString instead of sending over the whole DatabaseReference because it's easier
                        i.putExtra("ID", ID);
                        i.putExtra(PostActivity.EDIT_POSITION, post.getmFirebaseKey());
                        i.putExtra("Status",status);
                        startActivity(i);

                    }
                });

                adb.setPositiveButton("Delivery Complete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String, String> acceptedUsers = myPost.get(0).getmAcceptedUsers();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String key;
                        String myKey;
                        DatabaseReference dbAcceptedRef;
                        final DatabaseReference dbUsers = database.getReference().child("Users");
                        final DatabaseReference dbNotificationsRef = database.getReference().child("Notification");
                        dbRefPosts.child(myPost.get(0).getmId()).child("mActive").setValue(false);
                        mPosts.get(0).setmActive(false);
                        mPosts.remove(myPost.get(0));
                        mAdapter.notifyDataSetChanged();
                        dbAcceptedRef = database.getReference().child("Post").child(myPost.get(0).getmId()).child("mAcceptedUsers"); //get the user's post's accepted user
                        dbAcceptedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                final ArrayList<String> acceptedName = new ArrayList<String>();
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    final String childID = ""+child.getValue();

                                    if(!childID.equals("initial")) {
                                        DatabaseReference name = dbUsers.child("" + child.getValue()).child("name");
                                        name.addListenerForSingleValueEvent(new ValueEventListener() {
                                            String key, notificationKey, myNotificationKey;
                                            String myKey; //send a rate notification to me

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                acceptedName.add("" + dataSnapshot.getValue());
                                                myKey = dbUsers.child(ID).child("notifications").push().getKey(); //send to me
                                                myNotificationKey = dbNotificationsRef.push().getKey(); // store notification for sending me the rate the accepted user
                                                Notification myNotification = new Notification(childID, myPost.get(0).getmId(), ID, myKey, NotificationReference.RATE);
                                                myNotification.setMatchingPostTitle(myPost.get(0).getmTitle());
                                                myNotification.setmId(myNotificationKey);
                                                myNotification.setmFromUserName(acceptedName.get(0));
                                                dbUsers.child(ID).child("notifications").child(myKey).setValue(myNotification.getmId());
                                                dbNotificationsRef.child(myNotificationKey).setValue(myNotification);


                                                //send notification to accepted User
                                                key = dbUsers.child(ID).child("notifications").push().getKey(); //send to accepted user key
                                                notificationKey = dbNotificationsRef.push().getKey(); // store notification for sending accepted user the rate me
                                                Notification notification = new Notification(ID, myPost.get(0).getmId(), childID, key, NotificationReference.RATE);
                                                notification.setMatchingPostTitle(myPost.get(0).getmTitle());
                                                notification.setmId(notificationKey);
                                                notification.setmFromUserName(currUserName);
                                                dbUsers.child(childID).child("notifications").child(key).setValue(notification.getmId());
                                                dbNotificationsRef.child(notificationKey).setValue(notification);
                                                acceptedName.clear();


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }






                                   /* //send rate notification to the accepted users
                                    key = dbUsers.child(""+child.getValue()).child("notifications").push().getKey(); //send to the accepted user
                                    notificationKey = dbNotificationsRef.push().getKey(); //store notification for accepted user rating
*/



                                }




                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                    });
                        /*for (Map.Entry<String, String> entry : acceptedUsers.entrySet())
                        {
                            System.out.println(entry.getKey() + "/" + entry.getValue());
                            key = database.getReference("Notification").push().getKey(); // telling accepted users to rate the user
                            myKey = database.getReference("Notification").push().getKey(); // this is used to ask the user to rate the accepted user
                            //Notifiction from me to the accepted usrs
                            Notification notification = new Notification(ID, post.getmTitle() ,entry.getKey(),key, NotificationReference.RATE);
                            // notification from accepted users to me
                            Notification myNotification = new Notification(entry.getKey(), post.getmTitle() ,ID, myKey, NotificationReference.RATE);
                            databaseRef = database.getReference().child("Notification").child(key);
                            notification.setmId(key);
                            databaseRef.setValue(notification);
                            myNotification.setmId(myKey);
                            databaseRef = database.getReference().child("Notification").child(myKey);
                            databaseRef.setValue(myNotification);
                            //send to accepted users
                            dbRefUsers.child(entry.getValue()).child("notifications").child(notification.getmId()).setValue(notification.getmId());
                            //send to me
                            dbRefUsers.child(ID).child("notifications").child(myNotification.getmId()).setValue(myNotification.getmId());
                        }*/

                    }
                });
                adb.show();

            }
        });
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



            //            Movies = MovieSingleton.get(context).getMovies();
            Post mv = Posts.get(position);

//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView< ? > adapterView, View view, int position, long id) {
//                    // we know a row was clicked but we need to know WHERE specifically
//                    // is that data stored in the database
//
//                    Intent i = new Intent(getActivity(), DetailedPostActivity.class);
//                    // toString instead of sending over the whole DatabaseReference because it's easier
//                    i.putExtra("ID", currUserId);
//                    i.putExtra(DetailedPostActivity.EXTRA_POST, mv);
//                    startActivity(i);
//                }
//            });

            String f = mv.getmPhotos();
            if(mv.getmAllFoodPics() != null)
            {
                f= mv.getmAllFoodPics().get(0);
                System.out.println("GETTING PHOTO 0");
            }


            Glide.with(MyPostsFragment.this)
                    .load(f)
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(image);

            Glide.with(MyPostsFragment.this)
                    .load("https://graph.facebook.com/"+mv.getmAuthorId()+"/picture?type=large&width=1080" )
                    .centerCrop()
                    .placeholder(R.drawable.gmlogo)
                    .crossFade()
                    .into(imagePerson);

//                image.setImageDrawable(getResources().getDrawable(R.drawable.gmlogo));


            textTitle.setText(mv.getmTitle());
            textDescription.setText(mv.getmDescription());
            return convertView;
        }
    }


//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                _position = position;
//                Post post = (Post) list.getItemAtPosition(position);
//                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//                adb.setTitle("Complete Transaction");
//                adb.setMessage("Completed delivery for" + post.getmTitle() + "?");
//                adb.setNegativeButton("Cancel", null);
//                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        posts.remove(_position);
//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO SEND NOTIFICATION TO ACCEPTED USERS" +
//                                " and mark post as inactive" , Toast.LENGTH_SHORT).show();
//                    }});
//                adb.show();
//
//            }
//        });


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
//            ImageView mImage = (ImageView)v.findViewById(R.id.imagePic);
//
//            Glide.with(MyPostsFragment.this)
//                    .load(model.getmPhotos())
//                    .centerCrop()
//                    .placeholder(R.drawable.hamburger)
//                    .crossFade()
//                    .into(mImage);
//
//            TextView pPostContent = (TextView)v.findViewById(R.id.listNoteContent);
//            TextView pPostTitle = (TextView)v.findViewById(R.id.listNoteTitle);
//
//            pPostContent.setText(model.getmDescription());
//            pPostTitle.setText(model.getmTitle());
//        }
//    }

}
