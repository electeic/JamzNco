package jamilaappinc.grubmate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment implements PostActivity.DataFromActivityToFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int PICK_IMAGE_REQUEST = 123;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseDatabase database;
    private DatabaseReference dbRefNotes;
    private DatabaseReference dbNoteToEdit;
    DatabaseReference FirebaseRef;
    private StorageReference mStorageRef;
    String mPicURL;


    private Button cancelButton, startDateButton,endDateButton, startTimeButton, endTimeButton;
    private EditText _title, _dietary, _location, _servings, _tags, _descriptions;
    private CheckBox _homemade;
    private String title, dietary, location, servings, tags, date, descriptions, startTimeString, endTimeString,startDateString, endDateString;
    private SimpleDateFormat sdf;
    private Date startDateTime, endDateTime;

    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> tagsVec = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText pPostTitle;
    EditText pDietaryText;
    EditText pLocationText;
    EditText pServingsText;
    EditText pTagsText;
    EditText pPostDescription;
    ImageView pImage;
    Button pSubmitpostbutton;
    Button pAddPictureButton;


    private ArrayList<String> userFriends;
    private String currUserName;

    ArrayList<Integer> subscriptionCount = new ArrayList<>();
    ArrayList<Integer> subscriptionReadCounter = new ArrayList<>();

    private Uri filePath;
    Bitmap mBitmap;
    private String ID;

    android.support.design.widget.FloatingActionButton floatButton;

//    private OnFragmentInteractionListener mListener;

    public PostFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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

        //todo get database
        database = FirebaseDatabase.getInstance();

        //for pictures
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        currUserName = i.getStringExtra("Name");



        subscriptionReadCounter.add(0);
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Post")) {
                        subscriptionCount.add((int)snap.getChildrenCount());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        initGUIComp(v);
        addListeners();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Cancel?");
                adb.setMessage("Are you sure you want to cancel? ");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Users",userFriends);
                        intent.putExtra("Name", currUserName);
                        startActivityForResult(intent,0);
                        getActivity().finish();
                    }
                });
                adb.show();
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users",userFriends);
                intent.putExtra("Name", currUserName);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        return v;
    }

    private void initGUIComp(View v){
        pImage = (ImageView) v.findViewById(R.id.post_food_pic);

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        cancelButton = (Button) v.findViewById(R.id.post_cancel);
        pSubmitpostbutton = (Button) v.findViewById(R.id.post_submit);
        startDateButton =(Button) v.findViewById(R.id.post_startDateButton);
        endDateButton = (Button)v.findViewById(R.id.post_endDateButton);
        startTimeButton =(Button)v.findViewById(R.id.post_startTimeButton);
        endTimeButton = (Button)v.findViewById(R.id.post_endTimeButton);
        pAddPictureButton = (Button) v.findViewById(R.id.post_add_picture);


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

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.post_add_picture, getActivity()))
                .setContentTitle("Add images")
                .setContentText("Add images to show your post.")
                .hideOnTouchOutside()
                .build();
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
        filled = (groups.size() >0 && dateTime && (title.length()>0)&& (location.length()>0) && (servings.length()>0) && (tags.length()>0) && (descriptions.length()>0) && (categories.size() > 0));

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

    private void addListeners() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = getActivity().getIntent();
                ID = i.getStringExtra("ID");
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        pAddPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        pSubmitpostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAllFilled()) {

                    final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    final String key = database2.getReference("Post").push().getKey();
                    final DatabaseReference databaseRef = database.getReference().child("Post").child(key);
                    Intent i = getActivity().getIntent();
                    ID = i.getStringExtra("ID");

                    final Intent intent = new Intent(getActivity(), MenuActivity.class);
                    DatabaseReference dbRefSubscriptions = database2.getReference().child("Subscription");

                    if(filePath != null)
                        uploadFile(key);
                   /* else {
                        dbRefSubscriptions.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                Subscription subscription = dataSnapshot.getValue(Subscription.class);
                                int counter = subscriptionReadCounter.get(0);
                                counter++;
                                subscriptionReadCounter.clear();
                                subscriptionReadCounter.add(counter);

                                boolean matchingSubscription = true;


                                for (int i = 0; i < categories.size(); i++) {
                                    for (int j = 0; j < subscription.getmCategories().size(); j++) {
                                        if (!categories.get(i).equals(subscription.getmCategories().get(j))) {
                                            matchingSubscription = false;
                                        }
                                    }
                                }

                                if (subscription.isActive() == false) {
                                    matchingSubscription = false;
                                }

                                if (matchingSubscription == true) {
                                    //post matches subscription.
                                    //so add post to subscription's list of posts
                                    //allMatchingSubscriptions.add(Subscro.getmId());
                                    ArrayList<String> newIdAdded = subscription.getmPosts();
                                    newIdAdded.add(key);
                                    subscription.setmPosts(newIdAdded);

                                    //now rewrite the new posts list to the subscription
                                    DatabaseReference dbRefSubscription = database2.getReference().child("Subscription").child(subscription.getmId());
                                    dbRefSubscription.child("subscription").child("mPosts").setValue(newIdAdded);

                                }

                                final Post post = new Post(title, descriptions, location, startDateTime, endDateTime, categories, getTags(), null, "photos", Integer.parseInt(servings), _homemade.isChecked(), ID);
                                post.setmId(key);
                                final DatabaseReference dbRefUsers = database2.getInstance().getReference().child(FirebaseReferences.USERS);


                                // final DatabaseReference ref = database.getReference();
                                dbRefUsers.child("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList<String> tempPostList = dataSnapshot.child("Users").child(ID).child("userPosts").getValue(ArrayList.class);
                                        if (tempPostList == null) {
                                            tempPostList = new ArrayList<String>();
                                        }
                                        int listSize = tempPostList.size() + 1;
                                        //System.out.println("CURRENT LISTSIZE " + listSize);
                                      //  tempPostList.add(Integer.toString(listSize));
                                        //tempPostList.add(post.getmId());

                                        dbRefUsers.child(ID).child("userPosts").child(Integer.toString(listSize)).setValue(post.getmId());

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                System.out.println("going to create and write post on next line");
                                createAndWritePost(post, ID);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }*/
                }
                else{
                    //something is wrong so send a toast
                    Toast.makeText(getContext(), "Please make sure everything is filled out properly" , Toast.LENGTH_SHORT).show();

                }
            }

        });
    }




    private void uploadFile(String key)
    {
        if(filePath != null) {

//            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Uploading...");
//            progressDialog.show();
            final String key2 = key;

            StorageReference riversRef = mStorageRef.child("images/" + key + ".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                            progressDialog.dismiss();
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
//                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
                            uploadMeta(downloadUri.toString(), key2);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
//                            Toast.makeText(getActivity().getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                    progressDialog.setMessage(((int)progress) + "% uploaded");
                }
            });
        }
        else
        {

        }
    }

    public void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                pImage.setImageBitmap(mBitmap);
//                mBitmap = BitmapFactory.decodeFile(filePath.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    void uploadMeta(String uri, String key)
    {
//        if()
//        String id = FirebaseRef.push().getKey();
        Post post = new Post(title, descriptions, location, startDateTime, endDateTime, categories, getTags(), null, uri, Integer.parseInt(servings), _homemade.isChecked(), ID);

//        PictureSingleton.get(getActivity()).addMovie(picUri);

        DatabaseReference dbChild = database.getReference().child("Post").child(key);
        dbChild.setValue(post);

//        FirebaseRef.child(id).setValue(picUri); //part of og code

//        Toast.makeText(getActivity().getApplicationContext(), "Added Picture to Real Time Database", Toast.LENGTH_SHORT).show();
    }

    public void createAndWritePost(Post post, String ID) {
        System.out.println("ENTERED CREATRE AND WRITE POST");

        if (subscriptionReadCounter.get(0) == subscriptionCount.get(0)) {//only call after reading ALL posts
            System.out.println("can now go to the main screen");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference().child("Post").child(post.getmId());
            databaseRef.setValue(post);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("Users",userFriends);
            intent.putExtra("Name", currUserName);
            intent.putExtra("ID",ID);
            startActivityForResult(intent,0);
            getActivity().finish();

            Toast.makeText(getContext(), "Post Set" , Toast.LENGTH_SHORT).show();
        }

    }

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
