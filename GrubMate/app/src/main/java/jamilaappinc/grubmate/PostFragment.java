package jamilaappinc.grubmate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.bumptech.glide.Glide;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment implements PostActivity.DataFromActivityToFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int PICK_IMAGE_REQUEST = 123;
    private static final int PICK_IMAGE_REQUEST2 = 1234;
    private static final int PICK_IMAGE_REQUEST3 = 1235;
    private static final int PICK_IMAGE_REQUEST4 = 1236;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseDatabase database;
    private DatabaseReference dbRefNotes;
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
    private ArrayList<Group> groups = new ArrayList<>(); //everyone who can see the post
    private ArrayList<String> tagsVec = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView pImage;
    ImageView pImage2;
    ImageView pImage3;
    ImageView pImage4;

    Button pSubmitpostbutton;
    Button pAddPictureButton;

    private Uri filePath;
    private Uri filePath2;
    private Uri filePath3;
    private Uri filePath4;
    Bitmap mBitmap;
    private String ID;

    String currUserName;
    ArrayList<String> userFriends;
    String userProfilePic;
    String FirebaseKey;
   // private String status;
    boolean edit = false;

    ArrayList<String> foodPhotosInPosts = new ArrayList<>();

    ArrayList<Integer> subsCount = new ArrayList<>();
    ArrayList<Integer> subsReadCount = new ArrayList<>();

    private ArrayList<String> allMatchingSubs = new ArrayList<>();

    String foodPics = "photos";

    private PlaceAutocompleteFragment autocompleteFragment;
    private Place myPlace;
    private String locationText;
    private Post n;

    android.support.design.widget.FloatingActionButton floatButton;

    private DatabaseReference dbNoteToEdit;

    ArrayList<User> selectedFriends;

//    private OnFragmentInteractionListener mListener;

    public PostFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(int pos, String edit2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pos);
//        System.out.println("IVANS passing from newInstance" + edit);
        args.putString(ARG_PARAM2, edit2);
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

        System.out.println("IVANS mParam2 " + mParam2);

        //todo get database
        database = FirebaseDatabase.getInstance();

        //for pictures
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //todo get database reference paths
        dbRefNotes = database.getReference(FirebaseReferences.POSTS);

        Bundle args = getArguments();
        //todo get reference to note to be edited (if it exists)
        System.out.println("IVANS urlToEdit " + mParam2);
        if (mParam2 != null) { // NULL if we are adding a new record
//            dbNoteToEdit = database.getReferenceFromUrl(FirebaseKey);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        Intent i = getActivity().getIntent();
        initGUIComp(v);
        ID = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");

       // status = i.getStringExtra("Status");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        userProfilePic = (String) i.getSerializableExtra("MyProfilePicture");
        edit = (boolean)i.getSerializableExtra("Edit");

        subsReadCount.add(0);
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Subscription")) {
                        subsCount.add((int)snap.getChildrenCount());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                myPlace = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("meldoy", "An error occurred: " + status);
            }
        });

        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        autocompleteFragment.setText("");
                        myPlace = null;
                        locationText = null;
                    }
                });


        //todo read selected note
        if(mParam2 != null) {  // null if urlToEdit is null
            // read from the note to update
//            dbNoteToEdit.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
                    // convert this "data snapshot" to a model class
//                    Post n = dataSnapshot.getValue(Post.class);
            DatabaseReference ref = database.getReference().child("Post").child(mParam2);


            // Attach a listener to read the data at our posts reference
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    n = dataSnapshot.getValue(Post.class);
                    _title.setText(n.getmTitle());
                    autocompleteFragment.setText(n.getmAddress().toString());
                    locationText = n.getmAddress().toString();
                    if(n.getmAllFoodPics() != null)
                    foodPhotosInPosts = n.getmAllFoodPics();

//                    _location.setText(n.getmLocation());
                    _servings.setText(Integer.toString(n.getmServings()));
                    _tags.setText(n.getmTags().toString());
                    _descriptions.setText(n.getmDescription());
                    _homemade.setChecked(n.getHomemade());
                    foodPics = n.getmPhotos();
                    Glide.with(PostFragment.this)
                            .load(n.getmPhotos())
                            .centerCrop()
                            .placeholder(R.drawable.hamburger)
                            .crossFade()
                            .into(pImage);

                    if(n.getmAllFoodPics() != null) {
                        int liimtSizeforPics = n.getmAllFoodPics().size();
                        if (n.getmAllFoodPics().get(0) != null) {
                            Glide.with(PostFragment.this)
                                    .load(n.getmAllFoodPics().get(0))
                                    .centerCrop()
                                    .placeholder(R.drawable.gmlogo)
                                    .crossFade()
                                    .into(pImage);

                        }
                        if (liimtSizeforPics >= 2) {
                            Glide.with(PostFragment.this)
                                    .load(n.getmAllFoodPics().get(1))
                                    .centerCrop()
                                    .placeholder(R.drawable.gmlogo)
                                    .crossFade()
                                    .into(pImage2);

                        }
                        if (liimtSizeforPics >= 3) {
                            Glide.with(PostFragment.this)
                                    .load(n.getmAllFoodPics().get(2))
                                    .centerCrop()
                                    .placeholder(R.drawable.gmlogo)
                                    .crossFade()
                                    .into(pImage3);

                        }
                        if (liimtSizeforPics == 4) {
                            Glide.with(PostFragment.this)
                                    .load(n.getmAllFoodPics().get(3))
                                    .centerCrop()
                                    .placeholder(R.drawable.gmlogo)
                                    .crossFade()
                                    .into(pImage4);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }
        addListeners();


        return v;
    }

    private void initGUIComp(View v) {
        pImage = (ImageView)v.findViewById(R.id.post_food_pic);
        pImage2 = (ImageView)v.findViewById(R.id.post_food_pic_2);
        pImage3 = (ImageView)v.findViewById(R.id.post_food_pic_3);
        pImage4 = (ImageView)v.findViewById(R.id.post_food_pic_4);

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        cancelButton = (Button)v.findViewById(R.id.post_cancel);
        pSubmitpostbutton = (Button)v.findViewById(R.id.post_submit);
        startDateButton = (Button)v.findViewById(R.id.post_startDateButton);
        endDateButton = (Button)v.findViewById(R.id.post_endDateButton);
        startTimeButton = (Button)v.findViewById(R.id.post_startTimeButton);
        endTimeButton = (Button)v.findViewById(R.id.post_endTimeButton);
        _title = (EditText)v.findViewById(R.id.post_titleText);
//        _dietary = (EditText)v.findViewById(R.id.dietaryText);
        _servings = (EditText)v.findViewById(R.id.ServingsText);
        _tags = (EditText)v.findViewById(R.id.tagsText);
        _descriptions = (EditText)v.findViewById(R.id.post_description);


        _homemade = (CheckBox)v.findViewById(R.id.post_homemadeCheck);
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        sdf.setLenient(false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference temp = database.getReference().child("Users").child(ID);//child("alreadyLoggedIn");
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status ="";// dataSnapshot.getValue(String.class);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("alreadyLoggedIn")) {
                        status = child.getValue(String.class);
                    }
                }

                System.out.println("POST FRAGMENT STATUS: " + status);
                if(status.equals("0")) {
                    new ShowcaseView.Builder(getActivity())
                            .setTarget(new ViewTarget(R.id.post_food_pic, getActivity()))
                            .setContentTitle("Add images")
                            .setContentText("Add images to show your post.")
                            .hideOnTouchOutside()
                            .build();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @return If the form is filled out correectly then return TRUE
     */
    private Boolean checkAllFilled() {
        boolean filled = false;
        title = _title.getText().toString().trim();
//        dietary = _dietary.getText().toString().trim();
        servings = _servings.getText().toString().trim();
        tags = _tags.getText().toString().trim();
        descriptions = _descriptions.getText().toString().trim();
        boolean dateTime = checkDateTime();
       if(myPlace==null) { //if myplace is empty then check if you're editing
           if(edit){//if locationText is null then there's no place set in
               filled = (groups.size() >0 && dateTime && (locationText!=null) && (title.length()>0)  && (servings.length()>0) && (tags.length()>0) && (descriptions.length()>0) && (categories.size() > 0));
               Log.d("meldoy edit 1", "" + (locationText!=null)+dateTime + (title.length() > 0) + (servings.length() > 0) + (tags.length() > 0) + (descriptions.length() > 0) + (categories.size() > 0));
               return filled;
           }
           Log.d("meldoy edit 2", "" + (locationText!=null)+dateTime + (title.length() > 0) + (servings.length() > 0) + (tags.length() > 0) + (descriptions.length() > 0) + (categories.size() > 0));

           return false;
       }else{
           Log.d("meldoy edit 3", "" + (locationText!=null)+(myPlace != null)+dateTime + (title.length() > 0) + (servings.length() > 0) + (tags.length() > 0) + (descriptions.length() > 0) + (categories.size() > 0));

           filled = (groups.size() >0 && dateTime && (myPlace != null) && (title.length()>0)  && (servings.length()>0) && (tags.length()>0) && (descriptions.length()>0) && (categories.size() > 0));
           return filled;

       }



    }

    public static boolean checkAllFilledAndWriteDBTest(String title, String dietary, String location, String servings, String tags, Date startTime, Date endTime){
        for(int i = 0; i < 1612; i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(title.equals("") || location.equals("") || servings.equals("") || tags.equals("") || startTime == null || endTime == null){
            return false;
        }
        else{
            for(int i = 0; i < 3259; i++){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    private boolean checkDateTime() {
        boolean check = false;
        try {
            startDateTime = sdf.parse(startDateString + " " + startTimeString);
            endDateTime = sdf.parse(endDateString + " " + endTimeString);

            check = startDateTime.before(endDateTime);
        }
        catch (ParseException e) {
            Log.d("PARSE FAIL", "failed");
            return false;
        }

        return check;
    }

    /**
     *
     * @return Changes the String of tags into a Vector<String> so that can be used in new Post
     */
    private ArrayList<String> getTags() {
        String[] temp = tags.split(",");
        for (String s : temp) {
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
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name", currUserName);
                //intent.putExtra("Status", status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Cancel?");
                adb.setMessage("Are you sure you want to cancel? ");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Users", userFriends);
                        intent.putExtra("Name", currUserName);
                        //intent.putExtra("Status", status);
                        startActivityForResult(intent,0);
                        getActivity().finish();
                    }
                });
                adb.show();

            }
        });

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        pImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser2();
            }
        });

        pImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser3();
            }
        });

        pImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_homemade.isChecked())
                {
                    showFileChooser4();
                }
            }
        });

        pSubmitpostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAllFilled()) {
                    //all forms filled out correctly
                    String tempString;
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    if(mParam2 == null) {
                        DatabaseReference database3 = database2.getReference("Post").push();
                        tempString = database3.getKey();
                    }
                    else
                    {
                        tempString = mParam2;
                    }

                    final String key = tempString;
                    System.out.println("Post Frag Key = " + key);

                    DatabaseReference databaseRef = database.getReference().child("Post").child(key); //reference to the key of the post inside Posts

                    Intent intent = new Intent(getActivity(), MenuActivity.class);

                    if (filePath != null || filePath2 != null || filePath3 != null || filePath4 != null )
                        //uploadFile(key);
                        new MyAsyncTask().execute(key);
                    else
                    {
//                        System.out.println("post fragment: " + endDateTime);
//                        System.out.println("meldoy the place is " + myPlace.getName());
                        final Post post;
                        if(!edit){
                            post = new Post(title, descriptions, myPlace.getLatLng().latitude, myPlace.getLatLng().longitude,myPlace.getAddress().toString(), startDateTime, endDateTime, categories, getTags(), groups, foodPics, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, null, selectedFriends);

                        }else{
                            if(myPlace!=null){
                                post = new Post(title, descriptions, myPlace.getLatLng().latitude, myPlace.getLatLng().longitude,myPlace.getAddress().toString(), startDateTime, endDateTime, categories, getTags(), groups, foodPics, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, null, selectedFriends);

                            }
                            else{
                                post = new Post(title, descriptions, n.getmLatitude(), n.getmLongitude(),n.getmAddress(), startDateTime, endDateTime, categories, getTags(), groups, foodPics, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, null, selectedFriends);

                            }

                        }
                        post.setmId(key);
                        post.addmAcceptedUsers("initial");
                        for(int i = 0; i < groups.size(); i++){
                            post.userTargetedOrAdd(groups.get(i).getmUsers());
                        }

                        for(int i =0; i<selectedFriends.size(); i++) {
                            post.userTargetedOrAdd(selectedFriends.get(i).getId());
                        }

                        databaseRef.setValue(post); //adds the value (the post) to the key post


                        final DatabaseReference dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS); // gets all of the users to update the user's posts information
                        dbRefUsers.child(ID).child("userPosts").child(key).setValue(post.getmId()); //this line is what adds the post id to the user's userPosts


                        final DatabaseReference dbRefSubs = database.getInstance().getReference().child("Subscription");

                        System.out.println("meldoy 1");
                        dbRefSubs.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()){
                                    System.out.println("meldoy 2");

                                Subscription sub = (Subscription) snap.getValue(Subscription.class);
                                System.out.println("meldoy 3");

                                boolean matchingSub = false;

                                System.out.println("meldoy the author of sub is " + sub.getmUserAuthorId());

                                if (sub.getmUserAuthorId().equals(ID)) {
                                    for (int i = 0; i < categories.size(); i++) {
                                        if (!matchingSub && sub.getmCategories() != null) {
                                            for (int j = 0; j < sub.getmCategories().size(); j++) {

                                                if (categories.get(i).equals(sub.getmCategories().get(j))) {
                                                    System.out.println("MATCH");
                                                    if (sub.isActive() == true) {
                                                        System.out.println(" meldoy MATCH + ACTIVE");
                                                        ArrayList<String> tempPostList = dataSnapshot.child("Subscriptions").child(sub.getmId()).child("mPosts").getValue(ArrayList.class);
                                                        if (tempPostList == null) {
                                                            tempPostList = new ArrayList<String>();
                                                        }
                                                        int listSize = tempPostList.size() + 1;
                                                        System.out.println("CURRENT LISTSIZE1 " + listSize);
                                                        System.out.println("CURRENT SUB IS1" + sub.getmId());
                                                        tempPostList.add(Integer.toString(listSize));

                                                        dbRefSubs.child(sub.getmId()).child("mPosts").child(Integer.toString(listSize)).setValue(post.getmId());
                                                        String key = database.getReference("Notification").push().getKey();
                                                        Notification notification = new Notification(ID, sub.getmId(), ID, key, NotificationReference.SUBSCRIPTION);
                                                        DatabaseReference databaseRef = database.getReference().child("Notification").child(key);
                                                        notification.setMatchingPostTitle(title);
                                                        notification.setmId(key);
                                                        notification.setmFromUserName(currUserName);
                                                        databaseRef.setValue(notification);
                                                        dbRefUsers.child(ID).child("notifications").child(notification.getmId()).setValue(notification.getmId());
                                                        matchingSub = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                       /* dbRefSubs.addChildEventListener(new ChildEventListener(){
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                Subscription sub = dataSnapshot.getValue(Subscription.class);
                                int counter = subsReadCount.get(0);
                                counter++;
                                subsReadCount.clear();
                                subsReadCount.add(counter);


                                boolean matchingSub = false;

                                for (int i = 0; i < categories.size(); i++) {
                                    if(!matchingSub) {
                                        for (int j = 0; j < sub.getmCategories().size(); j++) {

                                            if (categories.get(i).equals(sub.getmCategories().get(j))) {
                                                System.out.println("MATCH");
                                                if (sub.isActive() == true) {
                                                    System.out.println("MATCH + ACTIVE");
                                                    ArrayList<String> tempPostList = dataSnapshot.child("Subscriptions").child(sub.getmId()).child("mPosts").getValue(ArrayList.class);
                                                    if (tempPostList == null) {
                                                        tempPostList = new ArrayList<String>();
                                                    }
                                                    int listSize = tempPostList.size() + 1;
                                                    System.out.println("CURRENT LISTSIZE1 " + listSize);
                                                    System.out.println("CURRENT SUB IS1" + sub.getmId());
                                                    tempPostList.add(Integer.toString(listSize));

                                                    dbRefSubs.child(sub.getmId()).child("mPosts").child(Integer.toString(listSize)).setValue(post.getmId());
                                                    String key = database.getReference("Notification").push().getKey();
                                                    Notification notification = new Notification(ID, sub.getmId(), ID, key, NotificationReference.SUBSCRIPTION);
                                                    DatabaseReference databaseRef = database.getReference().child("Notification").child(key);
                                                    notification.setMatchingPostTitle(title);
                                                    notification.setmId(key);
                                                    notification.setmFromUserName(currUserName);
                                                    databaseRef.setValue(notification);
                                                    dbRefUsers.child(ID).child("notifications").child(notification.getmId()).setValue(notification.getmId());
                                                    matchingSub = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
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
                        });*/

                    }
                    intent.putExtra("ID", ID);
                    intent.putExtra("Users", userFriends);
                    intent.putExtra("Name", currUserName);
                    //intent.putExtra("Status", status);
                    startActivityForResult(intent, 0);
                    getActivity().finish();
                }
                else {
                    //something is wrong so send a toast
                    Toast.makeText(getContext(), "Please make sure everything is filled out properly" , Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params) {
            uploadFile(params[0]);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("FOOD PIC START");
            for(String s: foodPhotosInPosts)
            {
                System.out.println("FOOD PIC" + s);
            }
            try {
                Thread.sleep(3000);
//                Thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            uploadMeta(result, foodPhotosInPosts);
        }
    }


    private void uploadFile(String key)
    {
        if (filePath != null) {

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
//                            uploadMeta(downloadUri.toString(), key2);
                            System.out.println("Filepathdi Ivan downloading pics");
                            addToFoodPics(downloadUri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
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
                    @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //                    progressDialog.setMessage(((int)progress) + "% uploaded");
                }
            });
        }
        if (filePath2 != null) {

            //            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //            progressDialog.setMessage("Uploading...");
            //            progressDialog.show();
            final String key2 = key;

            StorageReference riversRef2 = mStorageRef.child("images/" + key + 2 + ".jpg");

            riversRef2.putFile(filePath2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //                            progressDialog.dismiss();
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                            //                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
//                            uploadMeta(downloadUri.toString(), key2);
                            System.out.println("Filepath2 Ivan downloading pics");
                            addToFoodPics(downloadUri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
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
                    @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //                    progressDialog.setMessage(((int)progress) + "% uploaded");
                }
            });
        }
        if (filePath3 != null) {

            //            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //            progressDialog.setMessage("Uploading...");
            //            progressDialog.show();
            final String key2 = key;

            StorageReference riversRef3 = mStorageRef.child("images/" + key + 3 + ".jpg");

            riversRef3.putFile(filePath3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //                            progressDialog.dismiss();
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                            //                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
//                            uploadMeta(downloadUri.toString(), key2);
                            addToFoodPics(downloadUri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
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
                    @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //                    progressDialog.setMessage(((int)progress) + "% uploaded");
                }
            });
        }
        if (filePath4 != null) {

            //            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //            progressDialog.setMessage("Uploading...");
            //            progressDialog.show();
            final String key2 = key;

            StorageReference riversRef4 = mStorageRef.child("images/" + key + 4 + ".jpg");

            riversRef4.putFile(filePath4)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //                            progressDialog.dismiss();
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                            //                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded Success", Toast.LENGTH_SHORT).show();
//                            uploadMeta(downloadUri.toString(), key2);
                            addToFoodPics(downloadUri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
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
                    @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //                    progressDialog.setMessage(((int)progress) + "% uploaded");
                }
            });
        }

//        uploadMeta(key, foodPhotosInPosts);
    }


    public void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    public void showFileChooser2()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST2);
    }

    public void showFileChooser3()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST3);
    }

    public void showFileChooser4()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                pImage.setImageBitmap(mBitmap);
                //                mBitmap = BitmapFactory.decodeFile(filePath.getPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath2 = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath2);
                pImage2.setImageBitmap(mBitmap);
                //                mBitmap = BitmapFactory.decodeFile(filePath.getPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath3 = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath3);
                pImage3.setImageBitmap(mBitmap);
                //                mBitmap = BitmapFactory.decodeFile(filePath.getPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST4 && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath4 = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath4);
                pImage4.setImageBitmap(mBitmap);
                //                mBitmap = BitmapFactory.decodeFile(filePath.getPath());
            }
            catch (IOException e) {
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
        if (data != null) {
            startDateButton.setText("Start Date: " + data);
            startDateString = data;
        }
    }

    public void sendEndDate(String data) {
        if (data != null) {
            endDateButton.setText("End Date: " + data);
            endDateString = data;
        }
    }

    public void sendStartTime(String time) {
        if (time != null) {
            startTimeButton.setText("Start Time: " + time);
            startTimeString = time;
        }
    }

    public void sendEndTime(String time) {
        if (time != null) {
            endTimeButton.setText("End Time: " + time);
            endTimeString = time;
        }
    }

    public void sendCategories(ArrayList<String> cat) {
        if (cat != null) {
            categories = (ArrayList<String>)cat.clone();
        }
    }

    public void sendGroups(ArrayList<Group> _group) {
        if (_group != null) {
            groups = (ArrayList<Group>)_group.clone();
        }
    }

    @Override
    public void sendFriends(ArrayList<User> _finalList) {
        if(_finalList != null) {
            selectedFriends = (ArrayList<User>) _finalList.clone();
        }
    }

    void uploadMeta(String key, ArrayList<String> allPicsTho)
    {
        //        if()
        //        String id = FirebaseRef.push().getKey();
        for(String s: allPicsTho)
        {
            System.out.println("FOOD PIC2" + s);
        }
        Post post;
        if(myPlace!=null){
            post = new Post(title, descriptions, myPlace.getLatLng().latitude, myPlace.getLatLng().longitude,myPlace.getAddress().toString(), startDateTime, endDateTime, categories, getTags(), groups, foodPics, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, allPicsTho, selectedFriends);
//            post = new Post(title, descriptions, myPlace.getLatLng().latitude, myPlace.getLatLng().longitude,myPlace.getAddress().toString(), startDateTime, endDateTime, categories, getTags(), null, null, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, allPicsTho);

        }
        else{
            post = new Post(title, descriptions, n.getmLatitude(), n.getmLongitude(),n.getmAddress(), startDateTime, endDateTime, categories, getTags(), groups, foodPics, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key, null, selectedFriends);

        }
        post.addmAcceptedUsers("initial");
        post.setmAllFoodPics(allPicsTho);
        //        PictureSingleton.get(getActivity()).addMovie(picUri);
        post.setmId(key);
        DatabaseReference dbChild = database.getReference().child("Post").child(key);
        dbChild.setValue(post);

        //        FirebaseRef.child(id).setValue(picUri); //part of og code

        //        Toast.makeText(getActivity().getApplicationContext(), "Added Picture to Real Time Database", Toast.LENGTH_SHORT).show();
    }

//    void uploadMeta(String uri, String key) //old  uploads
//    {
//        //        if()
//        //        String id = FirebaseRef.push().getKey();
//        Post post = new Post(title, descriptions, location, startDateTime, endDateTime, categories, getTags(), null, uri, Integer.parseInt(servings), _homemade.isChecked(), ID, userProfilePic, key);
//        post.addmAcceptedUsers("initial");
//
//        //        PictureSingleton.get(getActivity()).addMovie(picUri);
//        post.setmId(key);
//        DatabaseReference dbChild = database.getReference().child("Post").child(key);
//        dbChild.setValue(post);
//
//        //        FirebaseRef.child(id).setValue(picUri); //part of og code
//
//        //        Toast.makeText(getActivity().getApplicationContext(), "Added Picture to Real Time Database", Toast.LENGTH_SHORT).show();
//    }

    void addToFoodPics(String uri)
    {
        foodPhotosInPosts.add(uri);
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
