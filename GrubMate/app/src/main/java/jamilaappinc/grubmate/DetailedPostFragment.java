package jamilaappinc.grubmate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import android.graphics.BitmapFactory;
import android.view.animation.AnimationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedPostFragment} interface
 * to handle interaction events.
 */
public class DetailedPostFragment extends Fragment implements OnMapReadyCallback {

    TextView fPostName;
    TextView fUsername;
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
    TextView fAddress;
    Button fRequestButton, fReportButton;
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

    String ID, currUserName, currPicture;
    ArrayList<String> userFriends;

    private int currImage = 0;

    private static final String ARG_URL = "itp341.firebase.ARG_URL";
    private static final String ARG_POSTS = "itp341.firebase.ARGPOSTS";

    private String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String mapsApiKey = "AIzaSyBJmhcsJPAAKvCtiCnjgkvWadbf4NNd2wg";
    private GoogleMap mMap;




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
        currPicture = i.getStringExtra("CurrPic");
        //status = i.getStringExtra("Status");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        n = (Post) i.getSerializableExtra(DetailedPostActivity.EXTRA_POST);
        fUsername = (TextView) v.findViewById(R.id.detail_userName);
        fPostName = (TextView) v.findViewById(R.id.postName);
        fCategories = (TextView) v.findViewById(R.id.categories);
        fDate = (TextView) v.findViewById(R.id.date);
        fDescription = (TextView) v.findViewById(R.id.description);
        fDietaryInfo = (TextView) v.findViewById(R.id.dietInfo);
        fEndTime = (TextView) v.findViewById(R.id.endTime);
        fStartTime = (TextView) v.findViewById(R.id.startTime);
        fFoodPicture = (ImageView) v.findViewById(R.id.foodPhoto);
        fProfilePicture = (ImageView) v.findViewById(R.id.profilePicture);
        fRating = (TextView) v.findViewById(R.id.userRatings);
        fAddress = (TextView) v.findViewById(R.id.LocationText);
        //fReportButton = (Button) v.findViewById(R.id.spamButton);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        if(n.getmAllFoodPics() != null)
        {
            setInitialImage(v);
            setImageRotateListener(v);
        }

        Glide.with(DetailedPostFragment.this)
                .load("https://graph.facebook.com/"+n.getmAuthorId()+"/picture?type=large&width=1080")
                .centerCrop()
                .placeholder(R.drawable.gmlogo)
                .crossFade()
                .into(fProfilePicture);
        fProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users", userFriends);
                intent.putExtra("Friend", n.getmAuthorId());
                intent.putExtra("Picture", n.getmAuthorPic());
                intent.putExtra("MyPic", currPicture);
                //  intent.putExtra("Status",status);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        fReportButton = (Button) v.findViewById(R.id.spamButton);
        fReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DetailedPostFragment.SendMail().execute("");
            }
        });

        fTags = (TextView) v.findViewById(R.id.tags);
        fHomeOrRestuarant = (TextView) v.findViewById(R.id.homeOrRestaurant);
        fRequestButton = (Button) v.findViewById(R.id.requestButton);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_detailPost);
        fServings = (TextView) v.findViewById(R.id.num_portions);
        fRating = (TextView) v.findViewById(R.id.userRatings);

        if(!n.getmActive() || (n.getmAuthorId().equals(ID))){
            fRequestButton.setVisibility(View.INVISIBLE);
        }


/*        fReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","abuomarj1786@gmail.com", null));

                intent.putExtra(Intent.EXTRA_SUBJECT, "spamReport");
                intent.putExtra(Intent.EXTRA_TEXT, "the post " + n.getmId() + " was reported as spam.");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });*/


        fRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                intent.putExtra(RequestActivity.POST_FROM_DETAILED, n);
                intent.putExtra("ID", ID);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users", userFriends);
               // intent.putExtra("Status",status);
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
              //  intent.putExtra("Status",status);
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


        fPostName.setText(n.getmTitle());
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(n.getmAuthorId());
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("name")) {
                        fUsername.setText(child.getValue(String.class));
                    }

                    else if(child.getKey().equals("avgRating")) {
                        fRating.setText("Rating: " +   new DecimalFormat("#.##").format(child.getValue(Double.class)));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // fUsername.setText();
        //fUsername.setText();
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
        fAddress.setText(n.getmAddress());

//        Glide.with(DetailedPostFragment.this)
//                .load(n.getmPhotos())
//                .centerCrop()
//                .placeholder(R.drawable.hamburger)
//                .crossFade()
//                .into(fFoodPicture);


        //now update information using the posts information

        //database = FirebaseDatabase.getInstance();
        //dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);


        return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status ="";
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("alreadyLoggedIn")) {
                        status = child.getValue(String.class);
                    }
                }

                if(status.equals("0")) {
                    new ShowcaseView.Builder(getActivity())
                            .setTarget(new ViewTarget(R.id.requestButton, getActivity()))
                            .setContentTitle("Request button")
                            .setContentText("You can request for food.")
                            .hideOnTouchOutside()
                            .build();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = new LatLng(n.getmLatitude(), n.getmLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(n.getmAddress().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

    }



    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private void setImageRotateListener(View v) {
        final View fv = v;
        final ImageView rotatebutton = (ImageView) v.findViewById(R.id.foodPhoto);
        rotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(n.getmAllFoodPics() != null)
                {
                    System.out.println("IVAN CLICKED A BUNCH OF SHIT WITH CURR IMAGE" + currImage);
                    currImage++;
                    if (currImage == n.getmAllFoodPics().size()) {
                        currImage = 0;
                    }
//                    setCurrentImage(fv);
                        Glide.with(DetailedPostFragment.this)
                                .load(n.getmAllFoodPics().get(currImage))
                                .centerCrop()
                                .placeholder(R.drawable.hamburger)
                                .crossFade()
                                .into(fFoodPicture);
                }

            }
        });
    }

    private void setInitialImage(View v) {
        if(n.getmAllFoodPics() != null)
        {
            Glide.with(DetailedPostFragment.this)
                    .load(n.getmAllFoodPics().get(currImage))
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(fFoodPicture);
        }
        else
        {
            Glide.with(DetailedPostFragment.this)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(fFoodPicture);
        }
    }

//    private void setCurrentImage(View v) {
//        final ImageView imageView = (ImageView) v.findViewById(R.id.foodPhoto);
//        ImageDownloader imageDownLoader = new ImageDownloader(imageView);
//        if(n.getmAllFoodPics() != null)
//        {
//            imageDownLoader.execute(n.getmAllFoodPics().get(currImage));
//        }
//    }
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


    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show( getContext(), "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail("abuomarj1786@gmail.com", "froggy19");

            String[] toArr = {"abuomarj1786@gmail.com", "abuomarj1786@gmail.com"};
            m.setTo(toArr);
            m.setFrom("abuomarj1786@gmail.com");
            m.setSubject("Reported As Spam");
            m.setBody("a post was marked as being spam, the ID is " + n.getmId());

            try {
                if(m.send()) {
                    Toast.makeText(getContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

//noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        // return true;
        // }

        return super.onOptionsItemSelected(item);
    }
}
