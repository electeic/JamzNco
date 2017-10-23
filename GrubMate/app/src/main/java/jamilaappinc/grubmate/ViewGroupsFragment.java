package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ViewGroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewGroupsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CREATEGROUP = "createGroup";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    android.support.design.widget.FloatingActionButton floatButton;
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<String> friends = new ArrayList<>();
    ListView list;
    GroupsAdapter adapter;
    Button addGroupButton;
    private String ID;

//    private OnFragmentInteractionListener mListener;

    public ViewGroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewGroupsFragment.
     */
    // TODO: Rename and change types and number of parameters

//            LINE FOR GETTING ALL THE GROUPS :
    public static ViewGroupsFragment newInstance(ArrayList<Group> groups, ArrayList<String> friends){
        ViewGroupsFragment fragment = new ViewGroupsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ViewGroupsActivity.GET_ALL_GROUPS, groups);
        args.putSerializable(ViewGroupsActivity.GET_ALL_FRIENDS, friends);
//        args.putInt(ARG_PARAM1, pos);
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
        View v = inflater.inflate(R.layout.fragment_view_groups, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();

//        populateList();
        initComponents(v);
        list.setAdapter(adapter);
        addListeners();

        return v;

    }

    private void initComponents(View v){
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        addGroupButton = (Button) v.findViewById(R.id.groups_button);
        list = (ListView) v.findViewById(R.id.groups_ListView);
        adapter= new GroupsAdapter(getActivity(), groups);
//            LINE FOR GETTING ALL GROUPS
        groups = (ArrayList<Group>)getArguments().getSerializable(ViewGroupsActivity.GET_ALL_GROUPS);
        friends = (ArrayList<String>)getArguments().getSerializable(ViewGroupsActivity.GET_ALL_FRIENDS);

    }

    private void addListeners(){
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) list.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), EditGroupActivity.class);
                if(group == null)System.out.println("Why");
                i.putExtra("ID", ID);
                i.putExtra(EditGroupActivity.GET_GROUP, group);
                startActivity(i);

            }
        });
        addGroupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getContext(), "@JAMILAAPPCORP: Create fragment or can we do it with just an Alert dialog? " , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
//                intent.putExtra(CREATEGROUP, "create");
//                startActivity(intent);
                intent.putExtra("ID", ID);
                intent.putExtra(CreateGroupActivity.GET_ALL_FRIENDS, friends);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
    }


    private void populateList() {
        ArrayList<User> group = new ArrayList<User>();
        for (int i = 0; i < 4; i++) {
            group.add(new User("Melody Chai " +i, "lol"));
        }
        groups.add(new Group("CSCI310", group));
        groups.add(new Group("Group 2", group));
        groups.add(new Group("yAsKwEEN", group));

    }
/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
