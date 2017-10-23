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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


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
    private String ID;


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
        Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();

        initGUIComponents(v);
        addListeners();
//        fillPage(notification.getmFromUser().getName());
        return v;
    }

    private void initGUIComponents(View v){
        notification = (Notification)getArguments().getSerializable(RateUserActivity.GET_RATE_REQUEST);
        rater = (User)getArguments().getSerializable(RateUserActivity.GET_RATER_USER);
        userName = (TextView) v.findViewById(R.id.rateUser_name);
        ratingBar = (RatingBar) v.findViewById(R.id.rateUser_ratingBar);
        ratingAmt = (TextView) v.findViewById(R.id.rateUser_ratingText);
        review = (EditText)v.findViewById(R.id.rateUser_review);
        submit = (Button) v.findViewById(R.id.rateUser_submitButton);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.rateUser_ratingBar, getActivity()))
                .setContentTitle("Rating/Review")
                .setContentText("Give users a rating and review.")
                .hideOnTouchOutside()
                .build();
    }

    private void fillPage(String _userName){
        userName.setText(_userName);
    }

    private void addListeners(){
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(getRating()){
                    //if the rating isn't 0 then you can submit the rating
//                    Toast.makeText(getContext(), "@JAMILAAPPCORP:(rateUserFragment) NEED TO GO BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME & SUBMIT THE RATING TO DB," +
//                            " also need to figure out what needs to be passed into the main screen" , Toast.LENGTH_SHORT).show();
                    Rating rating = new Rating(review.getText().toString().trim(),ratingBar.getRating(), rater);


                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("ID", ID);
                    startActivityForResult(intent,0);
                    getActivity().finish();

                }else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("Zero Rating?");
                    adb.setMessage("Are you sure you want to give 0/5 stars? ");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

//                            Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED BACK TO HOME SCREEN & PASS IN USER INFO TO POPULATE HOME & SUBMIT THE RATING TO DB," +
//                                    " also need to figure out what needs to be passed into the main screen" , Toast.LENGTH_SHORT).show();
                            Rating rating = new Rating(review.getText().toString().trim(),ratingBar.getRating(), rater);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("ID", ID);
                            startActivityForResult(intent,0);
                            getActivity().finish();

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
