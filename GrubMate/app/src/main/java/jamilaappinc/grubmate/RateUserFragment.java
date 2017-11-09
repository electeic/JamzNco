package jamilaappinc.grubmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * TextView rateUser_name : Where we put in the user getting rated's name
 * TextView ratingAmt : changes when the rater clicks on the rating bar
 * RatingBar rateUser_ratingBar : the amt user rates
 * EditText rateUser_review : the written portion of review
 * Button rateUser_submitButton : submit button
 */
public class RateUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView userName, ratingAmt;
    private RatingBar ratingBar;
    private EditText review;
    private Button submit;
    private Notification notification;
    private User rater;
    private String ID, currUserName, pic;
    private ArrayList<String> userFriends;
    private Notification notif;
    FirebaseDatabase database;
    DatabaseReference dbRef, dbRefUsers;
    private User myUser;
    ImageView myImage;


//    private OnFragmentInteractionListener mListener;

    public RateUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment RateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RateUserFragment newInstance(Notification notification, User raterUser) {
        Bundle args = new Bundle();
        RateUserFragment fragment = new RateUserFragment();
        args.putSerializable(RateUserActivity.GET_RATE_REQUEST, notification);
        args.putSerializable(RateUserActivity.GET_RATER_USER, raterUser);

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
        View v =inflater.inflate(R.layout.fragment_rate_user, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
//        currUserName = i.getStringExtra("Name");
        System.out.println("meldoy the username ASKDF is " + currUserName);
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        notif = (Notification)i.getSerializableExtra("Notification");
        initGUIComponents(v);
        addListeners();
        fillPage(notif.getmFromUserName());

        database = FirebaseDatabase.getInstance().getInstance();
        dbRef = database.getReference("Rating");
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
        dbRefUsers.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);

                System.out.println(user.getFriends() + user.getId() + user.getName());
                System.out.println("ID SENT OVER IS " + ID);
                System.out.println("USER's ID IS" + user.getId());
                if (user.getId().equals(ID)) {
                    //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  " + ID , Toast.LENGTH_SHORT).show();
                    myUser = user;
                    currUserName = myUser.getName();

                }
                if(user.getId().equals(notif.getmFromUser())){
                    pic = user.getProfilePhoto();
                    Glide.with(RateUserFragment.this)
                            .load(pic)
                            .centerCrop()
                            .placeholder(R.drawable.hamburger)
                            .crossFade()
                            .into(myImage);
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
        });
        return v;
    }

    private void initGUIComponents(View v){
//        notification = (Notification)getArguments().getSerializable(RateUserActivity.GET_RATE_REQUEST);
//        rater = (User)getArguments().getSerializable(RateUserActivity.GET_RATER_USER);
        userName = (TextView) v.findViewById(R.id.rateUser_name);
        ratingBar = (RatingBar) v.findViewById(R.id.rateUser_ratingBar);
        ratingAmt = (TextView) v.findViewById(R.id.rateUser_ratingText);
        review = (EditText)v.findViewById(R.id.rateUser_review);
        submit = (Button) v.findViewById(R.id.rateUser_submitButton);
        myImage = (ImageView) v.findViewById(R.id.rateUser_photo);

    }

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
                                    .setTarget(new ViewTarget(R.id.rateUser_ratingBar, getActivity()))
                                    .setContentTitle("Rating/Review")
                                    .setContentText("Give users a rating and review.")
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

    private void fillPage(String _userName){
        userName.setText(_userName);
    }

    private void addListeners(){
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String key = dbRef.push().getKey();
                if(getRating()){
                    //if the rating isn't 0 then you can submit the rating
//                    Toast.makeText(getContext(), "@JAMILAAPPCORP:(rateUserFragment) NEED TO GO BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME & SUBMIT THE RATING TO DB," +
//                            " also need to figure out what needs to be passed into the main screen" , Toast.LENGTH_SHORT).show();
                    System.out.println("meldoy curr user nmae is lksdjf 2 " + currUserName);
                    writeRatingToDB(review.getText().toString().trim(),ratingBar.getRating(), currUserName, key);

                    /*Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("Name",currUserName);
                    intent.putExtra("Users",userFriends);
                    startActivityForResult(intent,0);
                    getActivity().finish();*/

                }else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("Zero Rating?");
                    adb.setMessage("Are you sure you want to give 0/5 stars? ");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

//                            Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME & SUBMIT THE RATING TO DB," +
//                                    " also need to figure out what needs to be passed into the main screen" , Toast.LENGTH_SHORT).show();

                        writeRatingToDB(review.getText().toString().trim(),ratingBar.getRating(), currUserName,key);

                        }});
                    adb.show();
                }



            }
        });


/*        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
		public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {

			ratingAmt.setText(String.valueOf(rating));

		}
	});*/
}
    private void writeRatingToDB(String review, double rating,String name, String key){
        final String _review = review;
        final double _rating = rating;
        final String _name = name;
        final String _key = key;


        dbRefUsers.child(notif.getmFromUser()).child("avgRating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final double userRating = dataSnapshot.getValue(Double.class);

                dbRefUsers.child(notif.getmFromUser()).child("numRatings").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null){
                            dbRefUsers.child(notif.getmFromUser()).child("numRatings").setValue(1);
                            Rating rating = new Rating(_review, _rating, _name, notif.getmFromUser(), notif.getmToUser());
                            System.out.println("meldoy the name is 111 " + _name + " notif user is " + notif.getmFromUser());
                            rating.setRateeID(notif.getmFromUser());
                            rating.setId(_key);
                            dbRef.child(_key).setValue(rating);
                            dbRefUsers.child(notif.getmFromUser()).child("avgRating").setValue(_rating);



                        }
                        else {

                            double endRating = (double)  ((dataSnapshot.getValue(Integer.class) * userRating)+_rating)/(dataSnapshot.getValue(Integer.class) + 1);
                            Rating rating = new Rating(_review, _rating, _name, notif.getmFromUser(),notif.getmToUser() );
                            System.out.println("meldoy the name is " + _name + " notif user is " + notif.getmFromUser());
                            rating.setRateeID(notif.getmFromUser());
                            rating.setId(_key);
                            dbRef.child(_key).setValue(rating);
                            dbRefUsers.child(notif.getmFromUser()).child("avgRating").setValue(endRating);
                            dbRefUsers.child(notif.getmFromUser()).child("numRatings").setValue(dataSnapshot.getValue(Integer.class) + 1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(getActivity(), ViewNotificationsActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Name",currUserName);
                startActivityForResult(intent,0);
                getActivity().finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

/*    public void changeRatingText(View v){
        ratingAmt.setText((float)ratingBar.getNumStars() +"/5");
    }*/

    private boolean getRating(){
        System.out.println("Rating is "+ ratingBar.getRating() );
        if(ratingBar.getRating() == 0.0){
            return false;
        }else{
            return true;
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
    }
*/

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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
