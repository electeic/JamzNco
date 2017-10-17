package jamilaappinc.grubmate;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class RequestActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "main_position";
    String[] listCategories;
    boolean [] checkedItems;


    PostActivity.DataFromActivityToFragment dataFromActivityToFragment;

    ArrayList<Integer> selectedCategories = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");
        setContentView(R.layout.activity_main);
        listCategories = getResources().getStringArray(R.array.categories);
        checkedItems = new boolean[listCategories.length];

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            //TODO modify for id
            f = RequestFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
        dataFromActivityToFragment = (PostActivity.DataFromActivityToFragment)f;
    }


}
