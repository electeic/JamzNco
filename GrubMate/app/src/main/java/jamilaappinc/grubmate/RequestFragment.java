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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    FirebaseDatabase database;
    private DatabaseReference dbRefNotes;
    private DatabaseReference dbNoteToEdit;
    private StorageReference mStorageRef;

    private String location;
    private Integer numOfServings;
    Button cancelButton, submitButton;


    TextView rNumOfServingsLabel;
    SeekBar rServingsChosen;
    EditText rLocation;




//    private OnFragmentInteractionListener mListener;
    android.support.design.widget.FloatingActionButton floatButton;

    public RequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(int pos) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pos);
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

        database = FirebaseDatabase.getInstance();

        //todo get database reference paths
        dbRefNotes = database.getReference(FirebaseReferences.POSTS);

        Bundle args = getArguments();
        //todo get reference to note to be edited (if it exists)
        String urlToEdit = args.getString(mParam1);
        if(urlToEdit != null) { // NULL if we are adding a new record
            dbNoteToEdit = database.getReferenceFromUrl(urlToEdit);
        }
    }

    private Boolean checkAllFilled(){
        boolean filled = false;
        location = rLocation.getText().toString().trim();
        numOfServings = Integer.parseInt(rNumOfServingsLabel.getText().toString().trim());

        Log.d("error check",""+(location.length()>0)+(numOfServings>0));
        filled = ((location.length()>0) && (numOfServings>0));

        return filled;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request, container, false);

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);

        rLocation = (EditText) v.findViewById(R.id.locationText);
        cancelButton = (Button) v.findViewById(R.id.request_cancelButton);
        submitButton = (Button) v.findViewById(R.id.request_submitButton);
        rServingsChosen = (SeekBar) v.findViewById(R.id.possibleServings);
        rNumOfServingsLabel = (TextView) v.findViewById(R.id.servingsWanted);
        rNumOfServingsLabel.setText("1");


        rServingsChosen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        }

@Override
public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        }

@Override
public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        // TODO Auto-generated method stub
        rNumOfServingsLabel.setText(Integer.toString(rServingsChosen.getProgress()));

        }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivityForResult(intent, 0);
        getActivity().finish();
        }
        });


        cancelButton.setOnClickListener(new View.OnClickListener(){
public void onClick(View view){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivityForResult(intent,0);
        getActivity().finish();
        }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

        if(checkAllFilled()){
        //all forms filled out correctly



            Request request = new Request(location,null, numOfServings, null);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String key = database.getReference("jobs").push().getKey();
            request.setmId(key);

            DatabaseReference databaseRef = database.getReference().child("Request").child(key);
            databaseRef.setValue(request);


            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivityForResult(intent,0);
            getActivity().finish();

            Toast.makeText(getContext(), "Request Sent" , Toast.LENGTH_SHORT).show();

        //send this post to the DB

        }else{
        //something is wrong so send a toast
        Toast.makeText(getContext(), "Please make sure everything is filled out properly" , Toast.LENGTH_SHORT).show();
        }
               /* DatabaseReference databaseRef = database.getReference().child("Request").child("DELTETHIS2");
//                 uploadFile();
                getActivity().finish();*/
        }
        });

        return v;
        }
/*
    // TODO: Rename method, update argument and hook method into UI event
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
