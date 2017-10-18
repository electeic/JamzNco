package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ViewNotificationsActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "main_position";
    public static final String GET_ALL_NOTIFICATIONS = "all notifications";
    private ArrayList<Notification> notification = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");
        setContentView(R.layout.fragment_view_notifications);

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        /*
            THE LINE FOR SENDING IN ALL OF THE USER'S NOTIFICATIONS VIA INTENT

            notification = getIntent().getSerializableExtra(GET_ALL_NOTIFICATIONS);

         */

        if (f == null ) {
            //TODO modify for id
            f = ViewNotificationsFragment.newInstance(pos);
            /*
                LINE FOR SENDING ARRAYLIST TO FRAGMENT:

                f. ViewNotificationsFragment.newInstance(notification);

             */
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
