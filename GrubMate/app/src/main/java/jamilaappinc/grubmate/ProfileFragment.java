package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import org.w3c.dom.Text;

import java.util.ArrayList;

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
    FirebaseListAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefUsers;

    private TextView nameText;
    private TextView ratingText;
    private ListView myPosts;

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private View mMessengerButton;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    ArrayList<String> userFriends;


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
        //        if (getArguments() != null) {
        //            mParam1 = getArguments().getString(ARG_PARAM1);
        //            mParam2 = getArguments().getString(ARG_PARAM2);
        //        }

        database = FirebaseDatabase.getInstance();
        dbRefUsers = database.getInstance().getReference().child("Users");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        //get intent data
        Intent i = getActivity().getIntent();
        final String id = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");

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

        nameText = (TextView)v.findViewById(R.id.nameText);
        ratingText = (TextView)v.findViewById(R.id.profile_actualRating);
        myPosts = (ListView)v.findViewById(R.id.profile_postList);

        System.out.println("READING DB NOW...");


        dbRefUsers.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println(user.getFriends() + user.getId() + user.getName());
                System.out.println("ID SENT OVER IS " + id);
                System.out.println("USER's ID IS" + user.getId());
                if (user.getId().equals(id)) {
                    System.out.println("trueee");
                    nameText.setText(user.getName());
                    ratingText.setText(String.valueOf(user.getAvgRating()));
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
                intent.putExtra("Users", userFriends);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

//        mMessengerButton = v.findViewById(R.id.messenger_send_button);
//
//        mMessengerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMessengerButtonClicked();
//            }
//
//            private void onMessengerButtonClicked() {
//                // The URI can reference a file://, content://, or android.resource. Here we use
//                // android.resource for sample purposes.
//                Uri uri =
//                        Uri.parse("android.resource://com.facebook.samples.messenger.send/" + R.drawable.gmlogo);
//
//                // Create the parameters for what we want to send to Messenger.
//                ShareToMessengerParams shareToMessengerParams =
//                        ShareToMessengerParams.newBuilder(uri, "image/jpeg")
//                                .setMetaData("{ \"image\" : \"logo\" }")
//                                .build();
//
//                if (mPicking) {
//                    // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
//                    // the content to Messenger.
//                    MessengerUtils.finishShareToMessenger(getActivity(), shareToMessengerParams);
//                } else {
//                    // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
//                    // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
//                    // to be upgraded, this will direct the user to the play store.
//                    MessengerUtils.shareToMessenger(getActivity(), REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
//                }
//            }
//        });

        return v;
    }
}
