package jamilaappinc.grubmate;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastTransactionsFragment extends Fragment {

    private ListView list;
    //    private PostListAdapter adapter;
    private ArrayList<Transactions> mPosts = new ArrayList<>();
    android.support.design.widget.FloatingActionButton floatButton;
    int _position;

    FirebaseDatabase database;
    DatabaseReference dbRefTransactions;
    private String ID;
    private String currUserName;
    ArrayList<Integer> transactionsReadCounter = new ArrayList<>();
    ArrayList<Integer> transactionsCount = new ArrayList<>();
    ArrayList<String> userFriends;
    PastTransactionsFragment.PastTransactionsAdapter mAdapter;
    private String status;

    public PastTransactionsFragment() {
        // Required empty public constructor
    }

    public static PastTransactionsFragment newInstance(){
        PastTransactionsFragment fragment = new PastTransactionsFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(MyPostsActivity.GET_POSTS, posts);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_past_transactions, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        currUserName = i.getStringExtra("Name");
        status = i.getStringExtra("Status");

        // Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        dbRefTransactions = database.getInstance().getReference().child(FirebaseReferences.PASTTRANSACTIONS);

        transactionsReadCounter.add(0);

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
                    if (snap.getKey().equals(FirebaseReferences.PASTTRANSACTIONS)) { //if it
                        transactionsCount.add((int)snap.getChildrenCount());
                    }
                }
                dbRefTransactions.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int transactionsRead = transactionsReadCounter.get(0);
                        transactionsRead++;
                        transactionsReadCounter.clear();
                        transactionsReadCounter.add(transactionsRead);


                        Transactions post = dataSnapshot.getValue(Transactions.class);

                        if(post.getmId().equals(ID)){
                            mPosts.add(post);
                        }


                        if(transactionsReadCounter.get(0) == transactionsCount.get(0)){
                            System.out.println("IN IF, SETTING ADAPTER NOW");
                            mAdapter = new PastTransactionsFragment.PastTransactionsAdapter(getActivity(), R.layout.transaction_view, mPosts);
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
        initComponents(v);
        addListeners();
        return v;
    }

    private void initComponents(View v){
//        mPosts = (ArrayList<Post>)getArguments().getSerializable(MyPostsActivity.GET_POSTS);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        list = (ListView) v.findViewById(R.id.mytransactions_list);
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

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final Post post = (Post) list.getItemAtPosition(position);
//                final ArrayList<Post> myPost = new ArrayList<Post>();
//                myPost.add(post);
//                System.out.println("meldoy the post is " + post.getmId() + " " + post.getmTitle());
//                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//                adb.setTitle("");
//                adb.setMessage("What would you like to do? ");
////                adb.setNegativeButton("Cancel", null);
//
//                final int posi = position;
//                /**
//                 * !!!!this will be changed to whatever the condition it is when you can delete the post
//                 */
//
//
//                adb.setNegativeButton("Delete Post", new AlertDialog.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (post.getmAuthorId().equals(ID) && post.getmAcceptedUsers().size() == 1) {
//                            dbRefPosts.child(post.getmFirebaseKey()).removeValue();
//                        }
//                    }
//                });
//
//                adb.setNeutralButton("edit post", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                adb.setPositiveButton("Delivery Complete", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Map<String, String> acceptedUsers = myPost.get(0).getmAcceptedUsers();
//                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        String key;
//                        String myKey;
//                        DatabaseReference dbAcceptedRef;
//                        final DatabaseReference dbUsers = database.getReference().child("Users");
//                        final DatabaseReference dbNotificationsRef = database.getReference().child("Notification");
//                        dbRefPosts.child(myPost.get(0).getmId()).child("mActive").setValue(false);
//                        mPosts.get(0).setmActive(false);
//                        mPosts.remove(myPost.get(0));
//                        mAdapter.notifyDataSetChanged();
//                        dbAcceptedRef = database.getReference().child("Post").child(myPost.get(0).getmId()).child("mAcceptedUsers"); //get the user's post's accepted user
//                        dbAcceptedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot) {
//
//                                final ArrayList<String> acceptedName = new ArrayList<String>();
//                                for (DataSnapshot child : snapshot.getChildren()) {
//                                    final String childID = ""+child.getValue();
//
//                                    if(!childID.equals("initial")) {
//                                        DatabaseReference name = dbUsers.child("" + child.getValue()).child("name");
//                                        name.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            String key, notificationKey, myNotificationKey;
//                                            String myKey; //send a rate notification to me
//
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                acceptedName.add("" + dataSnapshot.getValue());
//                                                myKey = dbUsers.child(ID).child("notifications").push().getKey(); //send to me
//                                                myNotificationKey = dbNotificationsRef.push().getKey(); // store notification for sending me the rate the accepted user
//                                                Notification myNotification = new Notification(childID, myPost.get(0).getmId(), ID, myKey, NotificationReference.RATE);
//                                                myNotification.setMatchingPostTitle(myPost.get(0).getmTitle());
//                                                myNotification.setmId(myNotificationKey);
//                                                myNotification.setmFromUserName(acceptedName.get(0));
//                                                dbUsers.child(ID).child("notifications").child(myKey).setValue(myNotification.getmId());
//                                                dbNotificationsRef.child(myNotificationKey).setValue(myNotification);
//
//
//                                                //send notification to accepted User
//                                                key = dbUsers.child(ID).child("notifications").push().getKey(); //send to accepted user key
//                                                notificationKey = dbNotificationsRef.push().getKey(); // store notification for sending accepted user the rate me
//                                                Notification notification = new Notification(ID, myPost.get(0).getmId(), childID, key, NotificationReference.RATE);
//                                                notification.setMatchingPostTitle(myPost.get(0).getmTitle());
//                                                notification.setmId(notificationKey);
//                                                notification.setmFromUserName(currUserName);
//                                                dbUsers.child(childID).child("notifications").child(key).setValue(notification.getmId());
//                                                dbNotificationsRef.child(notificationKey).setValue(notification);
//                                                acceptedName.clear();
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//
//                        });
//
//                    }
//                });
//                adb.show();
//
//            }
//        });
    }

    public class PastTransactionsAdapter extends ArrayAdapter<Transactions> implements OnMapReadyCallback {
        List<Transactions> Posts;
        Context context;
        private GoogleMap mMap;
        Transactions mv;

        public PastTransactionsAdapter(Context context, int resource, List<Transactions> objects) {
            super(context, resource, objects);
            this.context = context;
            this.Posts = objects;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
//            Thread t = new Thread();
//            try {
//                t.wait(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            mMap = googleMap;
            // Add a marker in Sydney, Australia, and move the camera.
            LatLng location = new LatLng(mv.getmLatitude(), mv.getmLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(mv.getmAddress().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.transaction_view, null);
            }

            TextView textDescription = (TextView) convertView.findViewById(R.id.mytransation_info_title);

            mv = Posts.get(position);

            textDescription.setText("Delivery from " + mv.getmFromUser() + " to " + mv.getmToUser());
            return convertView;
        }
    }
}
