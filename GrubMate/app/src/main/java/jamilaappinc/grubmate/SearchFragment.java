package jamilaappinc.grubmate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by ericajung on 10/16/17.
 */



/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchActivity.DataFromActivityToFragment{

    public static final String ARGS_POSITION = "args_position";

    //    private List<Post> postList;
//    private PostListAdapter postAdapter;
    android.support.design.widget.FloatingActionButton floatButton;
    public static final String IDString = "fuck";


    FirebaseListAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    String currUserId;
    String currUserName;
    String startTimeString, endTimeString ,startDateString, endDateString;
    ArrayList<String> userFriends;
    ArrayList<String> categories = new ArrayList<String>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> tagsVec = new ArrayList<>();
    ArrayList<Integer> postCount = new ArrayList<>();
    ArrayList<Integer> postsReadCounter = new ArrayList<>();
    ArrayList<Post> allMatchingPosts = new ArrayList<>();

    EditText title, tags;
    Button categoryButton,startDateButton, startTimeButton, endDateButton, endTimeButton,searchButton,cancelButton, groupButton;
    CheckBox homeMade;

    SimpleDateFormat sdf;

    private String ID;
//    private String status;

    private Date startDateTime = null;
    private Date endDateTime = null;

    public SearchFragment() {
        // Required empty public constructor
    }

    //TODO modify for id
    public static SearchFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, pos);
        SearchFragment f = new SearchFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        //todo get database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);
        //todo get database reference paths
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_filtered_search, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");

        //status = i.getStringExtra("Status");
        postsReadCounter.add(0);
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Post")) {
                        postCount.add((int)snap.getChildrenCount());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        initComponents(v);
        //find views
        addListeners();

        return v;
    }

/*
EditText title, tags;
Button categoryButton,startDate, startTime, endDate, endTime,search,cancel;
CheckBox homeMade;
*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference temp = database.getReference().child("Users").child(ID);
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status ="";
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("alreadyLoggedIn")) {
                        status = child.getValue(String.class);
                        if(status.equals("0")) {
                            new ShowcaseView.Builder(getActivity())
                                    .setTarget(new ViewTarget(R.id.search_title, getActivity()))
                                    .setContentTitle("Advanced Search")
                                    .setContentText("Make filtered searches to see certain posts.")
                                    .hideOnTouchOutside()
                                    .build();
                        }
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<String> getTags() {
        String tmp = tags.getText().toString();
        System.out.println("TAGS STRING IS " + tmp);
        if(!tmp.equals("")){
            String[] temp = tmp.split(",");
            System.out.println("TAGS ARRAY IS " + temp);

            for (String s : temp) {
                System.out.println("CURRENT STRING S IS " + s);

                tagsVec.add(s);
            }
            return tagsVec;
        }
        else return null;


    }


    private void initComponents(View v) {
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        title = (EditText)v.findViewById(R.id.search_titleText);
        tags = (EditText)v.findViewById(R.id.search_tagsText);
        categoryButton = (Button)v.findViewById(R.id.search_cat);
        startDateButton = (Button)v.findViewById(R.id.search_startDateButton);
        startTimeButton = (Button)v.findViewById(R.id.search_startTimeButton);
        endDateButton = (Button)v.findViewById(R.id.search_endDateButton);
        endTimeButton = (Button)v.findViewById(R.id.search_endTimeButton);
        searchButton = (Button)v.findViewById(R.id.search_submit);
        cancelButton = (Button)v.findViewById(R.id.search_cancel);
        homeMade = (CheckBox)v.findViewById(R.id.post_homemadeCheck);

        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        sdf.setLenient(false);
    }

    private void addListeners() {

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name",currUserName);
                //intent.putExtra("Status", status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        // Attach a listener to read the data at our posts reference
        dbRefPosts.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check what is filled out
                String pTitle;
                String startDateString;
                String endDateString;
                getTags();
//                System.out.println("MY CATEGOEIES SIZE IS " + categories.size());
//                System.out.println("MY START DATE IS " + startDateTime.toString());
//                System.out.println("MY END DATE IS " + endDateTime.toString());


                dbRefPosts.addChildEventListener(new ChildEventListener(){
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int postsRead = postsReadCounter.get(0);
                        postsRead++;
                        System.out.println("POSTS READ COUNT " + postsRead);
                        postsReadCounter.clear();
                        postsReadCounter.add(postsRead);


                        Post post = dataSnapshot.getValue(Post.class);
                        if(!post.getmAuthorId().equals(ID)){
                            allMatchingPosts.add(post);

                        }

                        System.out.println("POSTS READ COUNTER" + postsReadCounter.get(0));
                        System.out.println("POSTS COUNTER" + postCount.get(0));


                        if (postsReadCounter.get(0) == postCount.get(0)) {//all posts read.
                            System.out.println("ALL POSTS READ");
                            System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());
                            if (!title.getText().toString().trim().equals("")) {//user entered post title
                                for (int i = 0; i < allMatchingPosts.size(); i++) {
                                    if (allMatchingPosts.get(i).getmTitle().equals(title.getText().toString()) == false) {
                                        System.out.println("REMOVED POST FROM ALL MATCHING POSTS " + allMatchingPosts.get(i).getmId());
                                        allMatchingPosts.remove(i);//removes if doesn't match given title
                                        i--;
                                    }
                                }
                            }
                            System.out.println("TITLE MATCH DONE");
                            System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());


                            if (startDateTime != null) {//user entered start date
                                for (int i = 0; i < allMatchingPosts.size(); i++) {
                                    if (startDateTime.after(allMatchingPosts.get(i).getmStartDate())) {//current post being read's start date is before search parameters
                                        allMatchingPosts.remove(i);//removes if doesn't match given title
                                        i--;
                                    }
                                }
                            }
                            System.out.println("S DATE MATCH DONE");
                            System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());


                            if (endDateTime != null) {
                                for (int i = 0; i < allMatchingPosts.size(); i++) {
                                    if (endDateTime.before(allMatchingPosts.get(i).getmEndDate())) {//current post being read's end date is after search parameters
                                        allMatchingPosts.remove(i);//removes if doesn't match given title
                                        i--;
                                    }
                                }
                            }
                            System.out.println("E DATE MATCH DONE");
                            System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());


                            if (tagsVec.size() != 0) {
                                for (int i = 0; i < allMatchingPosts.size(); i++) {
                                    boolean allTagsMatch = true;
                                    for (int j = 0; j < tagsVec.size(); j++) {
                                        if(!allMatchingPosts.get(i).findTag(tagsVec.get(j))){
                                            allTagsMatch = false;
                                        }
                                    }
                                    if(allTagsMatch == false){//not all tags match the given search parameter tags.
                                        allMatchingPosts.remove(i);
                                        i--;
                                    }
                                }
                            }
                            System.out.println("TAGS MATCH DONE");
                            System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());


                            if (categories.size() >0) {
                                for (int i = 0; i < allMatchingPosts.size(); i++) {
                                    boolean allCatsMatch = true;
                                    for (int j = 0; j < categories.size(); j++) {
                                        if(!allMatchingPosts.get(i).findCategory(categories.get(j))){
                                            allCatsMatch = false;
                                        }
                                    }
                                    if(allCatsMatch == false){//not all the categories match the given search parameter categories.
                                        allMatchingPosts.remove(i);
                                        i--;
                                    }
                                }
                            }
                            System.out.println("CATEGORIES MATCH DONE");

                            System.out.println("SIZE OF ALL MATCHING POSTS AFTER REMOVING IS " + allMatchingPosts.size());


                            for(int i = 0; i < allMatchingPosts.size(); i++){
                                System.out.println("PRINTING ALL POSTS THAT MATCH NOW " + allMatchingPosts.get(i).getmId());
                            }
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("ID", ID);
                            intent.putExtra("Users", userFriends);
                            intent.putExtra("Name",currUserName);
                            intent.putExtra("ReceivedPosts",allMatchingPosts);
                          //  intent.putExtra("Status", status);
                            startActivityForResult(intent, 0);
                            getActivity().finish();

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
//
//                // compare to posts and repopulate main with posts that match
//                if (!title.getText().toString().trim().equals("")) {
//
//                    boolean isTitleMatched = false;
//                    //
//                    Intent i = getActivity().getIntent();
//
//                    //System.out.println("USFRIENDPOST" + isFriendPost);
//                    //                    if (isFriendPost) {
//                    //                        ImageView mImage = (ImageView) v.findViewById(R.id.imagePic);
//                    //
//                    //                        Glide.with(SearchFragment.this)
//                    //                                .load(model.getmPhotos())
//                    //                                .centerCrop()
//                    //                                .placeholder(R.drawable.hamburger)
//                    //                                .crossFade()
//                    //                                .into(mImage);
//                    //
//                    //                        TextView pPostContent = (TextView) v.findViewById(R.id.listNoteContent);
//                    //                        TextView pPostTitle = (TextView) v.findViewById(R.id.listNoteTitle);
//                    //
//                    //
//                    //                        pPostContent.setText(model.getmTitle());
//                    //                        pPostTitle.setText(model.getmDescription());
//                    //                    }
//                    //                }
//
//                }
//                if (startDateTime != null) {
//
//                }
//                if (endDateTime != null) {
//
//                }
//                if (getTags().size() > 0) {
//
//                }
//                if (categories.size() >0) {
//
//                }
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

                        intent.putExtra("Name",currUserName);
                       // intent.putExtra("Status", status);
                        startActivityForResult(intent,0);
                        getActivity().finish();
                        //Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO GO BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME" , Toast.LENGTH_SHORT).show();

                    }
                });
                adb.show();

            }
        });
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
    public void sendStartDate(String data) {
        System.out.println("meldoy : wtf bro 1" + data);

        if (data != null) {
            startDateButton.setText("Start Date: " + data);
            startDateString = data;
        }
    }

    public void sendEndDate(String data) {
        System.out.println("meldoy : wtf bro 2" + data);
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
    public void sendGroups(ArrayList<String> _group) {
        if (_group != null) {
            groups = (ArrayList<String>)_group.clone();
        }
    }

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    public static  ArrayList<Post> filterPosts(ArrayList<Post> allPosts, String filterTitle, Date startTime, Date endTime,
    ArrayList<String> inputTags){
        if (!filterTitle.equals("")) {//user entered post title
            for (int i = 0; i < allPosts.size(); i++) {
                if (allPosts.get(i).getmTitle().equals(filterTitle) == false) {
                    allPosts.remove(i);//removes if doesn't match given title
                    i--;
                }
            }
        }
        if (startTime != null) {//user entered start date
            for (int i = 0; i < allPosts.size(); i++) {
                if (startTime.after(allPosts.get(i).getmStartDate())) {//current post being read's start date is before search parameters
                    allPosts.remove(i);//removes if doesn't match given title
                    i--;
                }
            }
        }


        if (endTime != null) {
            for (int i = 0; i < allPosts.size(); i++) {
                if (endTime.before(allPosts.get(i).getmEndDate())) {//current post being read's end date is after search parameters
                    allPosts.remove(i);//removes if doesn't match given title
                    i--;
                }
            }
        }

//
        if (inputTags.size() != 0) {
            for (int i = 0; i < allPosts.size(); i++) {
                boolean allTagsMatch = true;
                for (int j = 0; j < inputTags.size(); j++) {
                    if(!allPosts.get(i).findTag(inputTags.get(j))){
                        allTagsMatch = false;
                    }
                }
                if(allTagsMatch == false){//not all tags match the given search parameter tags.
                    allPosts.remove(i);
                    i--;
                }
            }
        }
//        System.out.println("TAGS MATCH DONE");
//        System.out.println("SIZE OF ALL MATCHING POSTS IS " + allMatchingPosts.size());
//
//
//        if (categories.size() >0) {
//            for (int i = 0; i < allMatchingPosts.size(); i++) {
//                boolean allCatsMatch = true;
//                for (int j = 0; j < categories.size(); j++) {
//                    if(!allMatchingPosts.get(i).findCategory(categories.get(j))){
//                        allCatsMatch = false;
//                    }
//                }
//                if(allCatsMatch == false){//not all the categories match the given search parameter categories.
//                    allMatchingPosts.remove(i);
//                    i--;
//                }
//            }
//        }
        return allPosts;
    }

//todo onDetach
/*  public void onDetach() {
super.onDetach();
mAdapter.cleanup();
}
*/
//todo create custom FirebaseListAdapter

/*
1. The default search result will be all the posts that are visible to the user.
2. can search by the name/tags/description of the post.
3. can search by the category of the post(e.g.,Asian,Thai).
4. can search by a specific time period that the food will be available.
5. the search result can be sorted by at least one of the following:
(1) when the food will be available from the current time
(2) the rating of the provider(owner of the post)
(3) most popular providers(i.e.,the number of successful transactions for each provider)
(4) distance from the current location.
*/

}
