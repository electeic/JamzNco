package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    TextView fHome;
    TextView fProfile;
    TextView fNotifications;
    TextView fPost;
    TextView fGroups;
    TextView fSubscriptions;
    TextView fSearch;
    TextView fLogout;
    TextView fCreateSubscription;
    TextView fMyPosts;

    User myUser;

    FirebaseDatabase database;
    DatabaseReference dbRefUsers;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Intent i = getActivity().getIntent();
        final String ID = i.getStringExtra("ID");
        final String currUserName = i.getStringExtra("Name");
//        System.out.println("meldoy the username in View is " + currUserName);
        final ArrayList<String> userFriends = (ArrayList<String>) i.getSerializableExtra("Users");

        fHome = (TextView)v.findViewById(R.id.home);
        fProfile = (TextView)v.findViewById(R.id.profile);
        fNotifications = (TextView)v.findViewById(R.id.notifications);
        fPost = (TextView)v.findViewById(R.id.post);
        fGroups = (TextView)v.findViewById(R.id.groups);
        fSubscriptions = (TextView)v.findViewById(R.id.subscriptions);
        fSearch = (TextView)v.findViewById(R.id.search);
        fLogout = (TextView)v.findViewById(R.id.logout);
        fCreateSubscription = (TextView)v.findViewById(R.id.createSubscription);
        fMyPosts = (TextView)v.findViewById(R.id.myPosts);

        // DATABASE REFERENCING STUFF
        database = FirebaseDatabase.getInstance();
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);


        dbRefUsers.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);

                System.out.println(user.getFriends() + user.getId() + user.getName());
                System.out.println("ID SENT OVER IS " + ID);
                System.out.println("USER's ID IS" + user.getId());
                if (user.getId().equals(ID)) {
                    //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  " + ID , Toast.LENGTH_SHORT).show();
                    myUser = user;
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


        fHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                startActivityForResult(intent, 0);
//                getActivity().finish();
            }
        });
        fCreateSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), createsSubscriptionActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                startActivityForResult(intent, 0);
            }
        });

        fProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                startActivityForResult(intent, 0);
            }
        });

        fNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewNotificationsActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                System.out.println("meldoy the sername is " + myUser.getName());
                intent.putExtra("Name", currUserName);
//                intent.putExtra(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS, myUser.getNotifications());
                startActivityForResult(intent, 0);
            }
        });
        fMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPostsActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                //                intent.putExtra(MyPostsActivity.GET_POSTS,  myUser.getUserPosts());
                startActivityForResult(intent, 0);
            }
        });

        fPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);

                startActivityForResult(intent, 0);
            }
        });

        fGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewGroupsActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                System.out.println("BLAH BLAH BLAH " + myUser.getName());
//                                System.out.println(" JESUS LORD HELP ME I NEED YOU : " + myUser.getFriends().size());
                                intent.putExtra(ViewGroupsActivity.GET_ALL_FRIENDS, myUser.getFriends());
//                                intent.putExtra(ViewGroupsActivity.GET_ALL_GROUPS,  myUser.getUserGroups());
                startActivityForResult(intent, 0);
            }
        });

        fSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewSubscriptionsActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
//                intent.putExtra(ViewSubscriptionsActivity.GET_ALL_SUBSCRIPTIONS, myUser.getSubscriptions());
                startActivityForResult(intent, 0);
            }
        });

        fSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                startActivityForResult(intent, 0);
            }
        });
        fLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        });





        return v;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.textViewMenu, getActivity()))
                .setContentTitle("Menu options")
                .setContentText("Choose an option to be redirected to.")
                .hideOnTouchOutside()
                .build();
    }

}
