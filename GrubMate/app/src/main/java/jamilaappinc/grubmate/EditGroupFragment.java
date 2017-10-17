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

import org.w3c.dom.Text;


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

    int _position;


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
        adapter= new GroupAdapter(getActivity());
        listMember.setAdapter(adapter);
        addListeners();



        return v;
    }

    private void initComp(View v){
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        group = (Group)getArguments().getSerializable(EditGroupActivity.GET_GROUP);
        groupName = (TextView) v.findViewById(R.id.editGroup_name);
        listMember = (ListView) v.findViewById(R.id.editGroup_list);
        submitButton =(Button)v.findViewById(R.id.editGroup_submitButton);
        deleteAllButton = (Button)v.findViewById(R.id.editGroup_deleteButton);

    }

    private void addListeners(){
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getContext(), "Can potentially just send final vector back to DB" , Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete");
                adb.setMessage("Are you sure you want to delete the group?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        group.getGroupMembers().removeAllElements();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Can potentially just send final vector back to DB" , Toast.LENGTH_SHORT).show();


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
                adb.setMessage("Are you sure you want to remove " + group.getGroupMembers().get(position).getName() + " from the group?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        group.getGroupMembers().remove(_position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO DELETE MEMBER FROM DB or just do it " +
                                "when you press submit, it depends on how db is passing in info" , Toast.LENGTH_SHORT).show();
                    }});
                adb.show();


            }
        });


    }
    private class GroupAdapter extends ArrayAdapter<User> {
        public GroupAdapter(Context context) {
            super(context, 0, group.getGroupMembers());
        }

        public View getView(int position, View itemView, ViewGroup parent) {
            if (itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_group_member_info, parent, false);
            }

            //find the group members
            User user = group.getGroupMembers().get(position);
            String name = user.getName();

            TextView nameText = (TextView) itemView.findViewById(R.id.editGroupMember_name);
            nameText.setText(name);
            groupName.setText(group.getName());


            return itemView;
// return  super.getView(position convertView, parent)
        }
        // TODO: Rename method, update argument and hook method into UI event

    }
}
