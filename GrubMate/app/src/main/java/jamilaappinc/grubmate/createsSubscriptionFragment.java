package jamilaappinc.grubmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class createsSubscriptionFragment extends Fragment implements createsSubscriptionActivity.DataFromActivityToFragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseDatabase database;
    private DatabaseReference dbRefNotes;
    private DatabaseReference dbNoteToEdit;
    DatabaseReference FirebaseRef;
    private StorageReference mStorageRef;


    private Button cancelButton, startDateButton,endDateButton, startTimeButton, endTimeButton;
    private EditText _title, _dietary, _tags, _descriptions;
    private CheckBox _homemade;
    private String title, dietary, tags, descriptions, startTimeString, endTimeString, startDateString, endDateString;
    private SimpleDateFormat sdf;
    private Date startDateTime, endDateTime;

    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> tagsVec = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText sSubscriptionTitle;
    Button sSubmitSubscriptionButton;


    android.support.design.widget.FloatingActionButton floatButton;

//    private OnFragmentInteractionListener mListener;

    public createsSubscriptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static createsSubscriptionFragment newInstance(int pos) {
        createsSubscriptionFragment fragment = new createsSubscriptionFragment();
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

        //todo get database
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_subscription, container, false);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        sSubscriptionTitle = (EditText) v.findViewById(R.id.subscription_titleText);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        initGUIComp(v);
        addListeners();


        return v;
    }

    private void initGUIComp(View v){
        cancelButton = (Button) v.findViewById(R.id.subscription_cancel);
        sSubmitSubscriptionButton = (Button) v.findViewById(R.id.subscription_submit);
        startDateButton =(Button) v.findViewById(R.id.subscription_startDateButton);
        endDateButton = (Button)v.findViewById(R.id.subscription_endDateButton);
        startTimeButton =(Button)v.findViewById(R.id.subscription_startTimeButton);
        endTimeButton = (Button)v.findViewById(R.id.subscription_endTimeButton);


        _title = (EditText) v.findViewById(R.id.subscription_titleText);
        _dietary = (EditText) v.findViewById(R.id.dietaryText);
        _tags = (EditText) v.findViewById(R.id.tagsText);
        _descriptions = (EditText) v.findViewById(R.id.subscription_description);


        _homemade = (CheckBox) v.findViewById(R.id.subscription_homemadeCheck);
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
//        date = _date.getText().toString().trim();
        tags = _tags.getText().toString().trim();
        descriptions = _descriptions.getText().toString().trim();
        boolean dateTime = checkDateTime();
        Log.d("error check", ""+dateTime+(title.length()>0)+(tags.length()>0)+(descriptions.length()>0)+(categories.size() > 0));
        filled = (groups.size() >0 && dateTime && (title.length()>0)&& (tags.length()>0) && (descriptions.length()>0) && (categories.size() > 0));

        return filled;

    }

    private boolean checkDateTime() {
        boolean check = false;
        try {
            startDateTime = sdf.parse(startDateString + " " + startTimeString);
            endDateTime = sdf.parse(endDateString + " " + endTimeString);

            check = startDateTime.before(endDateTime);
        } catch (ParseException e) {
            Log.d("PARSE FAIL", "failed");
            return false;
        }

        return check;
    }

    /**
     *
     * @return Changes the String of tags into a Vector<String> so that can be used in new Post
     */
    private ArrayList<String> getTags(){
        String[] temp = tags.split(",");
        for(String s : temp){
            tagsVec.add(s);
        }
        return tagsVec;

    }

    private void addListeners(){

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Cancel?");
                adb.setMessage("Are you sure you want to cancel? ");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivityForResult(intent,0);
                            getActivity().finish();*/
                        Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO GO BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME" , Toast.LENGTH_SHORT).show();

                    }});
                adb.show();


            }
        });

        sSubmitSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAllFilled()){
                    //all forms filled out correctly
                    Intent i = getActivity().getIntent();
                    String ID = i.getStringExtra("ID");
                    DatabaseReference databaseRef = database.getReference().child("Subscription").child(ID);
                    Subscription subscription = new Subscription(title,descriptions,startDateTime,endDateTime,categories,getTags(), null, _homemade.isChecked(),Integer.parseInt(ID));
                    databaseRef.setValue(subscription);


                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivityForResult(intent,0);
                    getActivity().finish();

                    Toast.makeText(getContext(), "Subscription Set" , Toast.LENGTH_SHORT).show();


//                    uploadFile();
//                    getActivity().finish();
                    //send this post to the DB

                }else{
                    //something is wrong so send a toast
                    Toast.makeText(getContext(), "Please make sure everything is filled out properly" , Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



//    public void uploadFile()
//    {
//        if(filePath != null) {
//
//            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Uploading...");
//            progressDialog.show();
//
//    public void uploadFile()
//    {
//        if(filePath != null) {
//
//            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Uploading...");
//            progressDialog.show();
//
//
//            StorageReference riversRef = mStorageRef.child("images/food.jpg");
//
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
////                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                            progressDialog.dismiss();
//                            Uri downloadUri = taskSnapshot.getDownloadUrl();
//                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
//                            uploadMeta(downloadUri.toString());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            // ...
//                            Toast.makeText(getActivity().getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                    progressDialog.setMessage(((int)progress) + "% uploaded");
//                }
//            });
//        }
//        else
//        {
//
//        }
//    }
//            StorageReference riversRef = mStorageRef.child("images/food.jpg");
//
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
////                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                            progressDialog.dismiss();
//                            Uri downloadUri = taskSnapshot.getDownloadUrl();
//                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
//                            uploadMeta(downloadUri.toString());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            // ...
//                            Toast.makeText(getActivity().getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                    progressDialog.setMessage(((int)progress) + "% uploaded");
//                }
//            });
//        }
//        else
//        {
//
//        }
//    }


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

    public void sendCategories(ArrayList<String> cat){
        if(cat!=null){
            categories = (ArrayList<String>)cat.clone();
        }
    }

    public void sendGroups(ArrayList<String> _group){
        if(_group!=null){
            groups = (ArrayList<String>)_group.clone();
        }
    }
    //

//    /*
    //    // TODO: Rename method, update argument and hook method into UI event
    //    public void onButtonPressed(Uri uri) {
    //        if (mListener != null) {
    //            mListener.onFragmentInteraction(uri);
    //        }
    //    }
    //
    //    public void onAttach(Activity activity) {
    //        super.onAttach(activity);
    //
    //        if (activity instanceof OnFragmentInteractionListener) {
    //            mListener = (OnFragmentInteractionListener) activity;
    //             } else {
    //            throw new RuntimeException(context.toString()
    //            throw new RuntimeException(activity.toString()
    //                    + " must implement OnFragmentInteractionListener");
    //        }
    //    }
    //
    //    @Override
    //    public void onDetach() {
    //        super.onDetach();
    //        mListener = null;
    //    }
    //
    //    *//**
    //     * This interface must be implemented by activities that contain this
    //     * fragment to allow an interaction in this fragment to be communicated
    //     * to the activity and potentially other fragments contained in that
    //     * activity.
    //     * <p>
    //     * See the Android Training lesson <a href=
    //     * "http://developer.android.com/training/basics/fragments/communicating.html"
    //     * >Communicating with Other Fragments</a> for more information.
    //     *//*
    //    public interface OnFragmentInteractionListener {
    //        // TODO: Update argument type and name
    //        void onFragmentInteraction(Uri uri);
//    }*/
}