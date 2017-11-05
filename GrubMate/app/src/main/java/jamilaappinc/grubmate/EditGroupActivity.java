package jamilaappinc.grubmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class EditGroupActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "main_position";
    public static final String GET_GROUP = "group";


    public static final String GET_ALL_FRIENDS = "get friends";
    String[] listFriends;
    boolean[] checkedFriends;
    FirebaseDatabase database;
    Group group;
    DatabaseReference dbRefUsers;
    private ArrayList<String> friends; //Contains the ID's of the friends
    private ArrayList<String> selectedFriends = new ArrayList<>(); // contains the names of the friend's selected
    private ArrayList<User> myFriends = new ArrayList<>(); // contains all of your friend's in user forms
    private ArrayList<User> finalSelectedFriends = new ArrayList<>();
    private ArrayList<String> myFriendsNames = new ArrayList<>(); // contains all of the names of the friends
    String ID;
    CreateGroupActivity.DataFromActivityToFragment dataFromActivityToFragment;

    public interface DataFromActivityToFragment {
        void sendFriends(ArrayList<User> finalList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");
        setContentView(R.layout.activity_main);

        //get intent data
        Intent i = getIntent();
        friends = (ArrayList<String>) i.getSerializableExtra(ViewGroupsActivity.GET_ALL_FRIENDS);
        ID = i.getStringExtra("ID");

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);


        group = (Group)getIntent().getSerializableExtra(GET_GROUP);

        if (f == null ) {
            //TODO modify for id
            f = EditGroupFragment.newInstance(group);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();


        // DATABASE REFERENCING STUFF
        database = FirebaseDatabase.getInstance();
        dbRefUsers = database.getInstance().getReference().child("Users");

        dbRefUsers.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);

                for(String s: friends)
                {
                    if(s.equals(user.getId()))
                    {
                        myFriends.add(user);
                        myFriendsNames.add(user.getName());
                    }
                }

                listFriends = new String[myFriendsNames.size()];
                for(int j =0; j<myFriendsNames.size(); j++){
                    listFriends[j] = myFriendsNames.get(j);
                }
                checkedFriends = new boolean[listFriends.length];


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
    }



    public void clickFriend(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditGroupActivity.this);
        mBuilder.setTitle("Select Group(s)");
        mBuilder.setMultiChoiceItems(listFriends,checkedFriends, new DialogInterface.OnMultiChoiceClickListener(){
            public void onClick(DialogInterface di, int pos, boolean isChecked){
                if(isChecked){
                    //if current item isn't already part of list, add it to list

                    if(! selectedFriends.contains(listFriends[pos])){
                        selectedFriends.add(listFriends[pos]);
                    }
                }else if(selectedFriends.contains(listFriends[pos])){
                    selectedFriends.remove(listFriends[pos]); //user unchecked the item

                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di, int which){
                getAllSelectedUsers();

                for(int i = 0; i < finalSelectedFriends.size(); i++){
                    boolean isInGroup = checkIfInGroup(finalSelectedFriends.get(i).getId());
                    if(!isInGroup){
                        group.addmGroupMembersList(finalSelectedFriends.get(i).getId(), finalSelectedFriends.get(i));
                    }
                }
                database.getReference().child("Group").child(group.getId()).setValue(group);

            }
        });

        mBuilder.setNegativeButton("Clear All", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di, int which){
                for(int i = 0; i < checkedFriends.length; i++){
                    checkedFriends[i] = false;
                    selectedFriends.clear();

                }
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private boolean checkIfInGroup(String id){
        HashMap <String, User> groupMembers = group.getmGroupMembersList();
        if(groupMembers.containsKey(id)) return true;
        return false;
    }
    private void getAllSelectedUsers(){
        for(String name:selectedFriends){
            for(User friend:myFriends){
                if(friend.getName().equals(name)){
                    finalSelectedFriends.add(friend);
                }
            }
        }
    }
}
