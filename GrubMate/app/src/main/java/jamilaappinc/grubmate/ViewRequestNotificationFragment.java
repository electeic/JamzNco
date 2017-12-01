package jamilaappinc.grubmate;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequestNotificationFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    android.support.design.widget.FloatingActionButton floatButton;
    private Notification notification;
    private TextView name, title, size, location;
    private Button accept, deny;
    private String ID, currUserName;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<String> userFriends = new ArrayList<>();
    private Request request;
    private DatabaseReference dbRefUsers, dbRefRequests, dbRefTransactions;
    FirebaseDatabase database;
    private GoogleMap mMap;
    private ArrayList<LatLng> markerPoints;

    //private String status;

    //google maps stuff
//    private String baseURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
    private String baseURL = "https://maps.googleapis.com/maps/api/directions/json?&origin=";
    private String mapsApiKey = "AIzaSyC48IsyZN5lkXybRzkJJj20BilB9q_xZsY";


    public ViewRequestNotificationFragment() {
        // Required empty public constructor
    }
    public static ViewRequestNotificationFragment newInstance(Notification request) {
        Bundle args = new Bundle();
        ViewRequestNotificationFragment fragment = new ViewRequestNotificationFragment();
        args.putSerializable(ViewRequestNotificationActivity.GET_REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_request_notification, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        currUserName = i.getStringExtra("Name");
        //status = i.getStringExtra("Status");
        request = (Request) i.getSerializableExtra("Request");

        //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        initComponents(v);
        addListeners();
        fillInScreen();


        return v;
    }

    private void fillInScreen(){
        name.setText(request.getRequestedUserName());
        title.setText(request.getmPost().getmTitle());
        size.setText(""+request.getmServings());
        location.setText(request.getAddress());
    }

    private void initComponents(View v){
        notification = (Notification) getArguments().getSerializable(ViewRequestNotificationActivity.GET_REQUEST);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        name = (TextView) v.findViewById(R.id.requestNotif_userName);
        title = (TextView) v.findViewById(R.id.requestNotif_postTitle);
        size =(TextView)v.findViewById(R.id.requestNotif_servingAmt);
        location = (TextView) v.findViewById(R.id.requestNotif_locationAddr);
        accept = (Button) v.findViewById(R.id.requestNotif_acceptButton);
        deny =(Button) v.findViewById(R.id.requestNotif_denyButton);
        database = FirebaseDatabase.getInstance();

        dbRefRequests = database.getInstance().getReference().child(FirebaseReferences.REQUEST);

        notifications = (ArrayList<Notification>)getArguments().getSerializable(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS);

        markerPoints = new ArrayList<LatLng>();
        // Google Maps API - Create instance of map fragment to be embedded in current fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);

        double destLat = request.getLatitude();
        double destLong = request.getLongitude();
        double originLat = request.mPost.getmLatitude();
        double originLong = request.getmPost().getmLongitude();
        LatLng destLatLng = new LatLng(destLat,destLong);
        LatLng originLatLng = new LatLng(originLat,originLong);


        String url = baseURL+originLat+","+originLong+"&destination="+destLat+","+destLong+"&key="+mapsApiKey;

        System.out.println("meldoy the url is " + url);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        mMap.addMarker(new MarkerOptions().position(originLatLng).title("Start: " +request.getmPost().getmAddress()));
        mMap.addMarker(new MarkerOptions().position(destLatLng).title("End: " +request.getAddress()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 15));



        /*Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add( destLatLng,originLatLng));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15));


        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);*/


        /*LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception dwnld url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();;
            PolylineOptions lineOptions = new PolylineOptions();;
            lineOptions.width(5);
            lineOptions.color(Color.BLUE);
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);

            }
            // Drawing polyline in the Google Map for the i-th route
            if(points.size()!=0)mMap.addPolyline(lineOptions);//to avoid crash
        }
    }





    private void addListeners() {
        floatButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("Users", userFriends);
                       // intent.putExtra("Status", status);
                        startActivityForResult(intent, 0);
                        getActivity().finish();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(getContext(), "@JAMILAAPPCORP:(VIEW REQUEST NOTIF) SEND THE CORRESPONDING USER A NOTIFICATION AND ADD TO POST'S ACCEPTED USERS ARRAYLIST  AND DELETE NOTIFICATION FROM USER" , Toast.LENGTH_SHORT).show();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference databasePostActiveRef, databasePostServingRef;
                        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
                        dbRefTransactions = database.getInstance().getReference().child(FirebaseReferences.PASTTRANSACTIONS);
                        databasePostServingRef = database.getReference().child("Post").child(request.getmPost().getmId()).child("mServings");
                        databasePostActiveRef = database.getReference().child("Post").child(request.getmPost().getmId()).child("mActive");
                        databasePostServingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int newServing = Integer.parseInt(""+dataSnapshot.getValue()) - request.getmServings();
                                System.out.println("meldoy the new serving is " + newServing);
                                if(newServing < 0){
                                    Toast.makeText(getContext(), "There's not enough servings left to accept this request.",Toast.LENGTH_SHORT).show();
                                }else{
                                    String key;
                                    DatabaseReference databaseRef;
                                    DatabaseReference databaseRef2;
                                    DatabaseReference databaseRef3;

                                    if(newServing == 0) databasePostActiveRef.setValue(false);

                                    databasePostServingRef.setValue(newServing);

                                    key = database.getReference("Notification").push().getKey();
                                    String keytransations = database.getReference(FirebaseReferences.PASTTRANSACTIONS).push().getKey();
                                    String keytransations2 = database.getReference(FirebaseReferences.PASTTRANSACTIONS).push().getKey();

                                    Notification notification = new Notification(ID, request.getmPost().getmId() ,request.mRequestUserId,key, NotificationReference.ACCEPT);
                                    databaseRef = database.getReference().child("Notification").child(key);

                                    Transactions myTransaction = new Transactions(currUserName, request.mRequestUserId , ID, request.getmPost().getmLatitude(), request.getmPost().getmLongitude(), request.getmPost().getmAddress(), keytransations);
                                    Transactions myTransaction2 = new Transactions(currUserName + " the deliverer", request.mRequestUserId, request.mRequestUserId, request.getmPost().getmLatitude(), request.getmPost().getmLongitude(), request.getmPost().getmAddress(), keytransations2); //requestor

                                    myTransaction2.setmAddress(request.getmPost().getmAddress());
                                    myTransaction.setmAddress(request.getmPost().getmAddress());
                                    myTransaction2.setTransactionID(keytransations2);
                                    myTransaction.setTransactionID(keytransations);
                                    //String mFromUser, String mToUser, String mId, double mLatitude, double mLongitude, String mAddress, String transactionID)
                                    databaseRef2 = database.getReference().child(FirebaseReferences.PASTTRANSACTIONS).child(keytransations);
                                    databaseRef3 = database.getReference().child(FirebaseReferences.PASTTRANSACTIONS).child(keytransations2);

                                    databaseRef2.setValue(myTransaction);
                                    databaseRef3.setValue(myTransaction2);

                                    notification.setMatchingPostTitle(request.getmPost().getmTitle());
                                    notification.setmId(key);
                                    notification.setmFromUserName(currUserName);
                                    databaseRef.setValue(notification);
//                                    dbRefRequests.child(request.getmId()).setValue(null);
                                    dbRefUsers.child(request.getmRequestUserId()).child("notifications").child(notification.getmId()).setValue(notification.getmId());
                                    DatabaseReference dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);

                                    String key2 = dbRefPosts.child(request.getmPost().getmId()).child("mAcceptedUsers").push().getKey();
                                    dbRefPosts.child(request.getmPost().getmId()).child("mAcceptedUsers").child(key2).setValue(request.getmRequestUserId());
                                    request.getmPost().addmAcceptedUsers(request.getmRequestUserId());
                                    Intent i = new Intent(getActivity(), ViewNotificationsActivity.class);
                                    i.putExtra("ID", ID);
                                    i.putExtra("Users", userFriends);
                                    i.putExtra("Name", currUserName);
                                  //  i.putExtra("Status", status);
                                    startActivity(i);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                }
        );

        deny.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(getContext(), "@JAMILAAPPCORP:(VIEW REQUEST NOTIF)  DELETE NOTIFICATION FROM USER" , Toast.LENGTH_SHORT).show();
                        dbRefRequests.child(request.getmId()).setValue(null);
                        Intent i = new Intent(getActivity(), ViewNotificationsActivity.class);
                        i.putExtra("ID", ID);
                        i.putExtra("ID", ID);
                        i.putExtra("Users", userFriends);
                        i.putExtra("Name", currUserName);
                      //  i.putExtra("Status", status);
                        startActivity(i);
                    }
                }
        );

    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}
