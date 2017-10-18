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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
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


    EditText title, tags;
    Button categoryButton,startDateButton, startTimeButton, endDateButton, endTimeButton,searchButton,cancelButton, groupButton;
    CheckBox homeMade;

    SimpleDateFormat sdf;
    private Date startDateTime, endDateTime;

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

    private void initComponents(View v){
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        title = (EditText) v.findViewById(R.id.search_titleText);
        tags =(EditText) v.findViewById(R.id.search_tagsText);
        categoryButton = (Button) v.findViewById(R.id.search_cat);
        startDateButton = (Button) v.findViewById(R.id.search_startDateButton);
        startTimeButton = (Button) v.findViewById(R.id.search_startTimeButton);
        endDateButton = (Button) v.findViewById(R.id.search_startDateButton);
        endTimeButton = (Button) v.findViewById(R.id.search_endTimeButton);
        searchButton = (Button) v.findViewById(R.id.search_submit);
        cancelButton = (Button) v.findViewById(R.id.search_cancel);
        homeMade = (CheckBox) v.findViewById(R.id.post_homemadeCheck);

        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        sdf.setLenient(false);
    }

    private void addListeners(){

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID",currUserId);
                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });

        // Attach a listener to read the data at our posts reference
        dbRefPosts.addValueEventListener(new ValueEventListener() {
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

            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Cancel?");
                adb.setMessage("Are you sure you want to cancel? ");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivityForResult(intent,0);
                        getActivity().finish();
                    }});
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
        } catch (ParseException e) {
            Log.d("PARSE FAIL", "failed");
            return false;
        }

        return check;
    }
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

    public void refresh() {
        mAdapter.notifyDataSetChanged();
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

    private class PostListAdapter extends FirebaseListAdapter<Post> {

        public PostListAdapter(Activity activity, Class<Post> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, Post model, int position) {
            // get references to row widgets
            // copy data from model to widgets
            boolean isFriendPost = false;
//
            Intent i = getActivity().getIntent();
//
//            //reads in userid, username, and friends from loginactivity
            currUserId = i.getStringExtra(IDString);
            currUserName = i.getStringExtra("Name");
            userFriends = (ArrayList<String>) i.getSerializableExtra("Users");


//            for (int j = 0; j < userFriends.size(); j++) {
//                String x = model.getmAuthorId();
//
//                if (x.equals(userFriends.get(j))) {
//
//                    System.out.println("this_is_true");
            isFriendPost = true;
//                }
//            }

            //System.out.println("USFRIENDPOST" + isFriendPost);
            if (isFriendPost) {
                ImageView mImage = (ImageView) v.findViewById(R.id.imagePic);

                Glide.with(SearchFragment.this)
                        .load(model.getmPhotos())
                        .centerCrop()
                        .placeholder(R.drawable.hamburger)
                        .crossFade()
                        .into(mImage);

                TextView pPostContent = (TextView) v.findViewById(R.id.listNoteContent);
                TextView pPostTitle = (TextView) v.findViewById(R.id.listNoteTitle);


                pPostContent.setText(model.getmTitle());
                pPostTitle.setText(model.getmDescription());
            }
        }
    }
}
