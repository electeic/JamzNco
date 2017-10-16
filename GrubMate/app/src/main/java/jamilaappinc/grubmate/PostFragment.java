package jamilaappinc.grubmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * TO DO: in method submitButton.setOnClickListener(new View.OnClickListener(){ the Post object created
 * doesn't have a Vector<Groups> or a User
 *
 * Send USER ID instead of sending the actual user object itself?
 *
 * Figure out how to set up the Group stuff in dialog/ activity
 *
 *
 */
public class PostFragment extends Fragment implements PostActivity.DataFromActivityToFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    android.support.design.widget.FloatingActionButton floatButton;
    private Button cancelButton, submitButton, startDateButton,endDateButton, startTimeButton, endTimeButton;
    private EditText _title, _dietary, _location, _servings, _tags, _descriptions;
    private Spinner _startH, _startM, _startTP, _endH, _endM, _endTP;
    private CheckBox _homemade;
    private String title, dietary, location, servings, tags, date, descriptions, startTimeString, endTimeString,startDateString, endDateString;
    private SimpleDateFormat sdf;
    private Date startDateTime, endDateTime;
    private CheckBox homemade;

    private Vector<String> categories = new Vector<>();
    private Vector<String> groups = new Vector<>();
    private Vector<String> tagsVec = new Vector<>();


//    private OnFragmentInteractionListener mListener;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(int pos) {
        PostFragment fragment = new PostFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_post, container, false);
            initGUIComp(v);
            addListeners();

            return v;
    }

    private void initGUIComp(View v){
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        cancelButton = (Button) v.findViewById(R.id.post_cancel);
        submitButton = (Button) v.findViewById(R.id.post_submit);
        startDateButton =(Button) v.findViewById(R.id.post_startDateButton);
        endDateButton = (Button)v.findViewById(R.id.post_endDateButton);
        startTimeButton =(Button)v.findViewById(R.id.post_startTimeButton);
        endTimeButton = (Button)v.findViewById(R.id.post_endTimeButton);


        _title = (EditText) v.findViewById(R.id.post_titleText);
        _dietary = (EditText) v.findViewById(R.id.dietaryText);
        _location = (EditText) v.findViewById(R.id.locationText);
        _servings = (EditText) v.findViewById(R.id.ServingsText);
        _tags = (EditText) v.findViewById(R.id.tagsText);
        _descriptions = (EditText) v.findViewById(R.id.post_description);


        _homemade = (CheckBox) v.findViewById(R.id.post_homemadeCheck);
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        sdf.setLenient(false);

    }

    /**
     *
     * @return If the form is filled out correectly then return TRUE
     */
    private Boolean checkAllFilled(){
        boolean filled = false;
        title = _title.getText().toString().trim();
        dietary = _dietary.getText().toString().trim();
        location = _location.getText().toString().trim();
        servings = _servings.getText().toString().trim();
//        date = _date.getText().toString().trim();
        tags = _tags.getText().toString().trim();
        descriptions = _descriptions.getText().toString().trim();
        boolean dateTime = checkDateTime();
        Log.d("error check", ""+dateTime+(title.length()>0)+(location.length()>0)+(servings.length()>0)+(tags.length()>0)+(descriptions.length()>0)+(categories.size() > 0));
        filled = (groups.size() >0 && dateTime && (title.length()>0) && (dietary.length()>0) && (location.length()>0) && (servings.length()>0) && (tags.length()>0) && (descriptions.length()>0) && (categories.size() > 0));

        return filled;

    }

    /**
     *
     * @return if the date and time for start/ end is a valid time period then return TRUE
     */
    private boolean checkDateTime(){
        boolean check = false;
        try {
            startDateTime = sdf.parse(startDateString+ " " + startTimeString);
            endDateTime = sdf.parse(endDateString + " " + endTimeString);

            check = startDateTime.before(endDateTime);
        }catch(ParseException e){
            Log.d("PARSE FAIL", "failed");
            return false;
        }

        return check;

    }

    /**
     *
     * @return Changes the String of tags into a Vector<String> so that can be used in new Post
     */
    private Vector<String> getTags(){
        String[] temp = tags.split(",");
        for(String s : temp){
            tagsVec.add(s);
        }
        return tagsVec;

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

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivityForResult(intent,0);
                getActivity().finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkAllFilled()){
                    //all forms filled out correctly
                    Post post = new Post(title,descriptions,location,startDateTime,endDateTime,categories,getTags(), null, "photos", Integer.parseInt(servings),null);

                    //send this post to the DB

                }else{
                    //something is wrong so send a toast
                    Toast.makeText(getContext(), "Please make sure everything is filled out properly" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     *
     * @param data activity sends the start date that the user has chosen
     *             sets the button text to the chosen time
     */
    public void sendStartDate(String data) {
        if(data != null){
            startDateButton.setText("Start Date: " + data);
            startDateString = data;
        }
    }


    public void sendEndDate(String data) {
        if(data != null){
            endDateButton.setText("End Date: " + data);
            endDateString = data;
        }
    }

    public void sendStartTime(String time){
        if(time!=null){
            startTimeButton.setText("Start Time: "+ time);
            startTimeString = time;
        }
    }

    public void sendEndTime(String time){
        if(time!=null){
            endTimeButton.setText("End Time: " + time);
            endTimeString = time;
        }
    }

    /**
     *
     * @param cat Activity sends the categories/groups chosen to the fragment
     */

    public void sendCategories(Vector<String> cat){
        if(cat!=null){
            categories = (Vector<String>)cat.clone();
        }
    }

    public void sendGroups(Vector<String> _group){
        if(_group!=null){
            groups = (Vector<String>)_group.clone();
        }
    }

















/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
/*
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
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void getStartDate();
    }
    */
}
