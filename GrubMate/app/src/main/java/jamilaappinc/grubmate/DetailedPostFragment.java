package jamilaappinc.grubmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedPostFragment.OnFragmentInteractionListener} interface
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

    private OnFragmentInteractionListener mListener;

    public DetailedPostFragment() {
        // Required empty public constructor
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




        return v;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
