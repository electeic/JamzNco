package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequestNotificationFragment extends Fragment implements OnMapReadyCallback {

    android.support.design.widget.FloatingActionButton floatButton;
    private Notification notification;
    private TextView name, title, size, location;
    private Button accept, deny;
    private String ID, currUserName;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<String> userFriends = new ArrayList<>();
    private Request request;
    private DatabaseReference dbRefUsers, dbRefRequests;
    FirebaseDatabase database;
    private GoogleMap myMap;

    //private String status;

    //google maps stuff
    private String baseURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
    private String mapsApiKey = "AIzaSyBJmhcsJPAAKvCtiCnjgkvWadbf4NNd2wg";


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
        location.setText("LOOK AT LINE 90 of ViewReqNotifFrag");
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

        // Google Maps API - Create instance of map fragment to be embedded in current fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
      /*  LatLng destLatLng = request.getmLocation();
        LatLng originLatLng = request.getmPost().getmLocation();
        double destLat = request.getmLocation().latitude;
        double destLong = request.getmLocation().longitude;
        double originLat = request.getmPost().getmLocation().latitude;
        double originLong = request.getmPost().getmLocation().longitude;

        String url = baseURL+originLat+","+originLong+"&destinations="+destLat+","+destLong+"&key="+mapsApiKey;

        LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
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

                                    if(newServing == 0) databasePostActiveRef.setValue(false);

                                    databasePostServingRef.setValue(newServing);

                                    key = database.getReference("Notification").push().getKey();
                                    Notification notification = new Notification(ID, request.getmPost().getmId() ,request.mRequestUserId,key, NotificationReference.ACCEPT);
                                    databaseRef = database.getReference().child("Notification").child(key);
                                    notification.setMatchingPostTitle(request.getmPost().getmTitle());
                                    notification.setmId(key);
                                    notification.setmFromUserName(currUserName);
                                    databaseRef.setValue(notification);
                                    dbRefRequests.child(request.getmId()).setValue(null);
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


}
