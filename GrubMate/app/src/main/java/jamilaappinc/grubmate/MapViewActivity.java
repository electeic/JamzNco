package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MapViewActivity extends AppCompatActivity {
    private ArrayList<String> friends; //Contains the ID's of the friends
    private ArrayList<Post> posts;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        friends = (ArrayList<String>) i.getSerializableExtra(ViewGroupsActivity.GET_ALL_FRIENDS);
        ID = i.getStringExtra("ID");
        posts = (ArrayList<Post>) i.getSerializableExtra("Posts");
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            //TODO modify for id
//            f = DetailedPostFragment.newInstance(reference);
            f = MapViewFragment.newInstance();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();

    }
}
