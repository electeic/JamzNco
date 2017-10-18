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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
    Button fRequestButton;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    android.support.design.widget.FloatingActionButton floatButton;
    private static final String ARG_PARAM1 = "param1";

    //todo database references
    private DatabaseReference dbRefNotes;
    private DatabaseReference dbNoteToEdit;
    DatabaseReference dbRefCount;

    private static final String ARG_URL = "itp341.firebase.ARG_URL";


//    private OnFragmentInteractionListener mListener;

    public DetailedPostFragment() {
        // Required empty public constructor
    }

    public static DetailedPostFragment newInstance(String reference) {
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, pos);
        args.putString(ARG_URL, reference);
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

        fRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                startActivityForResult(intent, 0);
                //getActivity().finish();
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivityForResult(intent, 0);
               // getActivity().finish();
            }
        });

        //todo read selected note
        if(dbNoteToEdit != null) {  // null if urlToEdit is null
            // read from the note to update
            dbNoteToEdit.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // convert this "data snapshot" to a model class
                    Post n = dataSnapshot.getValue(Post.class);
                    fPostName.setText(n.getmTitle());
//                    fCategories.setText(n.getmCategories());
                    fDescription.setText(n.getmDescription());

//                    fProfilePicture
//                    fFoodPicture;
//                    fTags.setText(); //passes in an array so o.o
//                    fDietaryInfo.setText(n);
//                    fCategories.setText(n.getmCategories());
//                    if(n.get)
//                    fHomeOrRestuarant.setText();
//                    TextView fDate;

                    fStartTime.setText(n.getmStartDate().toString());
                    fEndTime.setText(n.getmEndDate().toString());

                    Glide.with(DetailedPostFragment.this)
                            .load(n.getmPhotos())
                            .centerCrop()
                            .placeholder(R.drawable.hamburger)
                            .crossFade()
                            .into(fFoodPicture);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //now update information using the posts information

        //database = FirebaseDatabase.getInstance();
        //dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);


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
