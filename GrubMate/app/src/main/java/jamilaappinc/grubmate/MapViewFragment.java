package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    ArrayList<String> userFriends;
    ArrayList<Post> posts;
    String ID, currUserName;
    private GoogleMap mMap;
    android.support.design.widget.FloatingActionButton floatButton;



    public static MapViewFragment newInstance(){
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, pos);
        MapViewFragment fragment = new MapViewFragment();
        fragment.setArguments(args);
        return fragment;

    }


    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        currUserName = i.getStringExtra("Name");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        posts = (ArrayList<Post> )i.getSerializableExtra("Posts");

        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_detailPost);
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
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("meldoy posts size is " + posts.size());
        for(int i = 0; i < posts.size(); i++){
            LatLng location = new LatLng(posts.get(i).getmLatitude(),posts.get(i).getmLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title( posts.get(i).getmTitle())
                    .snippet(posts.get(i).getmAddress().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0224,-118.2851), 15));


    }
}
