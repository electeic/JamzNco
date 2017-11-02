package jamilaappinc.grubmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *TextView groupName (R.id.editGroup_name)
 * ListView listMember (R.id.editGroup_list)
 *
 */
public class EditGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Group group;
    TextView groupName;
    ListView listMember;
    GroupAdapter adapter;
    android.support.design.widget.FloatingActionButton floatButton;
    Button submitButton, deleteAllButton;

    private String ID;
    ArrayList<String> userFriends; //passing around screens

    int _position;

    DatabaseReference dbRefUsers;
    private ArrayList<User> selectedFriends = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference dbRefGroups, getDbRefUsers,databaseRef;

    ArrayList<User> groupMembers;
    ArrayList<String> friends;



    public EditGroupFragment() {
        // Required empty public constructor
    }

    public static EditGroupFragment newInstance(Group group) {
        EditGroupFragment fragment = new EditGroupFragment();
        Bundle args = new Bundle();
        args.putSerializable(EditGroupActivity.GET_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_group, container, false);
        initComp(v);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        friends = (ArrayList<String>)i.getSerializableExtra(ViewGroupsActivity.GET_ALL_FRIENDS);
        group = (Group) i.getSerializableExtra(EditGroupActivity.GET_GROUP);


        Collection<User> members = group.getmGroupMembersList().values();
        groupMembers = new ArrayList<>(members);

        adapter= new GroupAdapter(getActivity(), groupMembers);
        listMember.setAdapter(adapter);
        addListeners();
        return v;
    }

    private void initComp(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
        dbRefGroups = database.getInstance().getReference().child(FirebaseReferences.GROUPS);

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        group = (Group)getArguments().getSerializable(EditGroupActivity.GET_GROUP);
        groupName = (TextView) v.findViewById(R.id.editGroup_name);
        listMember = (ListView) v.findViewById(R.id.editGroup_list);
        submitButton =(Button)v.findViewById(R.id.editGroup_submitButton);
        deleteAllButton = (Button)v.findViewById(R.id.editGroup_deleteButton);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.editGroup_deleteButton, getActivity()))
                .setContentTitle("Remove members")
                .setContentText("Choose members you want to delete.")
                .hideOnTouchOutside()
                .build();
    }

    private void addListeners(){
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //group to delete
//                //set values in database
//                dbRefUsers.child(ID).child("userGroups").child(group.getId()).removeValue();
                if(groupMembers.size() == 0) {
                    databaseRef.child("Group").child(group.getId()).setValue(null);
//                    databaseRef.setValue(null);
                }

                goToViewGroupsFragment();

//                else {
//                    Group groupI = new Group(groupName.getText().toString(), selectedFriends);
//                    groupI.setmUserAuthorId(ID);
//                    databaseRef.setValue(groupI);
//                }


            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                System.out.println("deleting everyone: " + group.getName());
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete");
                adb.setMessage("Are you sure you want to delete the group?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseRef.child("Group").child(group.getId()).setValue(null);
                        Intent intent = new Intent(getActivity(), ViewGroupsActivity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Users", userFriends);
                        intent.putExtra(ViewGroupsActivity.GET_ALL_FRIENDS, friends);
                        startActivityForResult(intent, 0);
                        getActivity().finish();

                    }});
                adb.show();
            }
        });


        listMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _position = position;
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete");
                adb.setMessage("Are you sure you want to remove " + groupMembers.get(position).getName() + " from the group?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRefGroups.child(group.getId()).child("mGroupMembersList").child(groupMembers.get(_position).getId()).setValue(null);

                        group.removemGroupMemberList(group.getGroupMembers().get(_position).getId());
                        groupMembers.remove(_position);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });


    }
    private class GroupAdapter extends ArrayAdapter<User> {
        List<User> groupMembers;
        public GroupAdapter(Context context, ArrayList<User> groupMembers) {
            super(context, 0, groupMembers);
            this.groupMembers = groupMembers;
        }

        public View getView(int position, View itemView, ViewGroup parent) {
            if (itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_group_member_info, parent, false);
            }
            //find the group members
            User user = groupMembers.get(position);
            String name = user.getName();

            TextView nameText = (TextView) itemView.findViewById(R.id.editGroupMember_name);
            nameText.setText(name);
            groupName.setText(group.getName());


            return itemView;
// return  super.getView(position convertView, parent)
        }
        // TODO: Rename method, update argument and hook method into UI event

    }

    private void goToViewGroupsFragment(){
        Intent intent = new Intent(getActivity(), ViewGroupsActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("Users", userFriends);
        intent.putExtra(ViewGroupsActivity.GET_ALL_FRIENDS, friends);
        startActivityForResult(intent, 0);
//        getActivity().finish();
    }
}
