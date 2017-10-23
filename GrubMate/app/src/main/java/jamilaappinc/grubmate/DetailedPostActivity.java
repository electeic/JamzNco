package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DetailedPostActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "firebase.EXTRA_URL";
    public static final String EXTRA_POSITION = "main_position";
    public static final String EXTRA_POST = "DetailedPostActivityPosts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","heyfromdetailedpost");
        setContentView(R.layout.activity_main);

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        String reference = i.getStringExtra(EXTRA_URL);
//        int pos = i.getIntExtra(EXTRA_POSITION, -1);
        Post p = (Post) i.getSerializableExtra(DetailedPostActivity.EXTRA_POST);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            //TODO modify for id
//            f = DetailedPostFragment.newInstance(reference);
            f = DetailedPostFragment.newInstance(p);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
