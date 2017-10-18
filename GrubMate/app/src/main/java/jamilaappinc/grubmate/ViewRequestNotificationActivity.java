package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ViewRequestNotificationActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "main_position";
    public static final String GET_REQUEST = "request";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        Notification request = (Notification) getIntent().getSerializableExtra(GET_REQUEST);


        if (f == null ) {
            //TODO modify for id
            f = ViewRequestNotificationFragment.newInstance(request);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
