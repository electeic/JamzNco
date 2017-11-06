package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedPostFragment} interface
 * to handle interaction events.
 */
public class DetailedPostFragment extends Fragment {

    TextView fPostName;
    ImageView fProfilePicture;
    ImageView fFoodPicture;
    TextView fTags;
    TextView fDietaryInfo;
    TextView fCategories;
    TextView fDescription;
    TextView fHomeOrRestuarant;
    TextView fDate;
    TextView fStartTime;
    TextView fEndTime;
    TextView fRating;
    Button fRequestButton;
    TextView fServings;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    android.support.design.widget.FloatingActionButton floatButton;
    private static final String ARG_PARAM1 = "param1";

    //todo database references
    private DatabaseReference dbRefNotes;
    private DatabaseReference dbNoteToEdit;
    DatabaseReference dbRefCount;
    Post n;

    String ID, currUserName, status;
    ArrayList<String> userFriends;


    private static final String ARG_URL = "itp341.firebase.ARG_URL";
    private static final String ARG_POSTS = "itp341.firebase.ARGPOSTS";



//    private OnFragmentInteractionListener mListener;

    public DetailedPostFragment() {
        // Required empty public constructor
    }

//    public static DetailedPostFragment newInstance(String reference) {
    public static DetailedPostFragment newInstance(Post reference){
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, pos);
        args.putSerializable(ARG_POSTS, reference);
        DetailedPostFragment fragment = new DetailedPostFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo get database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //todo get database reference paths
        dbRefNotes = database.getReference(FirebaseReferences.POSTS);
        dbRefCount = database.getReference(FirebaseReferences.POST_COUNT);

        Bundle args = getArguments();
        n = (Post) args.get(ARG_POSTS);
        //todo get reference to note to be edited (if it exists)
        String urlToEdit = args.getString(ARG_URL);
        if(urlToEdit != null) { // NULL if we are adding a new record
            dbNoteToEdit = database.getReferenceFromUrl(urlToEdit);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_post, container, false);

        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");
        status = i.getStringExtra("Status");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        n = (Post) i.getSerializableExtra(DetailedPostActivity.EXTRA_POST);
        fPostName = (TextView) v.findViewById(R.id.postName);
        fCategories = (TextView) v.findViewById(R.id.categories);
        fDate = (TextView) v.findViewById(R.id.date);
        fDescription = (TextView) v.findViewById(R.id.description);
        fDietaryInfo = (TextView) v.findViewById(R.id.dietInfo);
        fEndTime = (TextView) v.findViewById(R.id.endTime);
        fStartTime = (TextView) v.findViewById(R.id.startTime);
        fFoodPicture = (ImageView) v.findViewById(R.id.foodPhoto);
        fProfilePicture = (ImageView) v.findViewById(R.id.profilePicture);
        fTags = (TextView) v.findViewById(R.id.tags);
        fHomeOrRestuarant = (TextView) v.findViewById(R.id.homeOrRestaurant);
        fRequestButton = (Button) v.findViewById(R.id.requestButton);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_detailPost);
        fServings = (TextView) v.findViewById(R.id.num_portions);
        fRating = (TextView) v.findViewById(R.id.userRatings);

        fRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                System.out.println("meldoy the post is " + n.getmTitle());
                intent.putExtra(RequestActivity.POST_FROM_DETAILED, n);
                intent.putExtra("ID", ID);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Status",status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Status",status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        //todo read selected note
//        if(dbNoteToEdit != null) {  // null if urlToEdit is null
//            // read from the note to update
//            dbNoteToEdit.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // convert this "data snapshot" to a model class
//                    n = dataSnapshot.getValue(Post.class);
//                    System.out.println("FUCK THIS: " + n);
//                    fPostName.setText(n.getmTitle());
////                    fCategories.setText(n.getmCategories());
//                    String desc = "Description: " + n.getmDescription();
//                    fDescription.setText(desc);
//
//                    String s = Integer.toString(n.getmServings()) + " Shares Left";
//                    fServings.setText(s);
////                    String s2 = "Rating " + Float.toString();
////                    fProfilePicture
//
//                    String Tags = "Tags: ";
//                    for(String sw: n.getmTags())
//                    {
//                        Tags += sw + ", ";
//                    }
//                    fTags.setText(Tags); //passes in an array so o.o
////                    fDietaryInfo.setText(n);
//
//                    String Cates = "Categories: ";
//                    for(String sw: n.getmCategories())
//                    {
//                        Cates += sw + ", ";
//                    }
//                    fCategories.setText(Cates);
//
//                    if(n.getHomemade())
//                    {
//                        fHomeOrRestuarant.setText("Homemade");
//                    }
//                    else
//                    {
//                        fHomeOrRestuarant.setText("Resturaunt");
//                    }
//
//                    fStartTime.setText(n.getmStartDate().toString());
//                    fEndTime.setText(n.getmEndDate().toString());
//
//                    Glide.with(DetailedPostFragment.this)
//                            .load(n.getmPhotos())
//                            .centerCrop()
//                            .placeholder(R.drawable.hamburger)
//                            .crossFade()
//                            .into(fFoodPicture);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }


        System.out.println("FUCK THIS: " + n);
        fPostName.setText(n.getmTitle());
//                    fCategories.setText(n.getmCategories());
        String desc = "Description: " + n.getmDescription();
        fDescription.setText(desc);

        String s = Integer.toString(n.getmServings()) + " Shares Left";
        fServings.setText(s);
//                    String s2 = "Rating " + Float.toString();
//                    fProfilePicture

        String Tags = "Tags: ";
        for(String sw: n.getmTags())
        {
            Tags += sw + ", ";
        }
        fTags.setText(Tags); //passes in an array so o.o
//                    fDietaryInfo.setText(n);

        String Cates = "Categories: ";
        for(String sw: n.getmCategories())
        {
            Cates += sw + ", ";
        }
        fCategories.setText(Cates);

        if(n.getHomemade())
        {
            fHomeOrRestuarant.setText("Homemade");
        }
        else
        {
            fHomeOrRestuarant.setText("Resturaunt");
        }

        fStartTime.setText(n.getmStartDate().toString());
        fEndTime.setText(n.getmEndDate().toString());

        Glide.with(DetailedPostFragment.this)
                .load(n.getmPhotos())
                .centerCrop()
                .placeholder(R.drawable.hamburger)
                .crossFade()
                .into(fFoodPicture);


        //now update information using the posts information

        //database = FirebaseDatabase.getInstance();
        //dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);


        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(status == "" || status == "0") {
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(R.id.requestButton, getActivity()))
                    .setContentTitle("Request button")
                    .setContentText("You can request for food.")
                    .hideOnTouchOutside()
                    .build();
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
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
