package jamilaappinc.grubmate;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "main_position";

    SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mRefreshAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");
        setContentView(R.layout.activity_main);

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            //TODO modify for id
            f = MainFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        mListView = findViewById(R.id.active_post_list);
//        mListView.setAdapter(new ArrayAdapter<String>(){
//            String[] fakeTweets = getResources().getStringArray(R.array.categories);
//            mRefreshAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fakeTweets);
//            mListview.setAdapter(mRefreshAdapter);
//        });
    }
}