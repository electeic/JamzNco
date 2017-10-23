package jamilaappinc.grubmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment implements CreateGroupActivity.DataFromActivityToFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<User> selectedFriends = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<Group> myGroups = new ArrayList<>();
    private String ID;


    private EditText groupName;
    private Button add,submit,cancel;
    private TextView list;


//    private OnFragmentInteractionListener mListener;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(ArrayList<String> friends) {
        Bundle args = new Bundle();
        args.putSerializable(CreateGroupActivity.GET_ALL_FRIENDS, friends);
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        View v = inflater.inflate(R.layout.fragment_create_group, container, false);
        initComponent(v);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();


        return v;

    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.createGroup_addbutton, getActivity()))
                .setContentTitle("Add button")
                .setContentText("Add more members to your group.")
                .hideOnTouchOutside()
                .build();
    }

    private void initComponent(View v){
        groupName = (EditText)v.findViewById(R.id.createGroup_groupName);
        add = (Button)v.findViewById(R.id.createGroup_addbutton);
        submit = (Button)v.findViewById(R.id.createGroup_createButton);
        cancel = (Button) v.findViewById(R.id.createGroup_cancelButton);
        list = (TextView) v.findViewById(R.id.createGroup_listFriends);
    }

    private void addListeners(){
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                for(int i =0 ; i <selectedFriends.size(); i++){
                    if(i < 1) list.setText(selectedFriends.get(i).getName() + "\n");
                    else list.append(selectedFriends.get(i).getName() + "\n");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewGroupsActivity.class);
                intent.putExtra("ID", ID);
            }
        });

    }

    @Override
    public void sendFriends(ArrayList<User> finalList) {
        selectedFriends = (ArrayList<User>)finalList.clone();
        updateTextView();
    }
    private void updateTextView(){
        for(int i= 0; i < selectedFriends.size(); i++){
            if(i == 0){
                list.setText(selectedFriends.get(i).getName()+ "\n");
            }else{
                list.append(selectedFriends.get(i).getName()+"\n");
            }
        }
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/
/*
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
    } */
}
