package jamilaappinc.grubmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


/**
 * Created by ericajung on 10/13/17.
 */

public class PostActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "main_position";
    Button category;
    String[] listCategories;
    boolean [] checkedItems;
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
            f = PostFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();



    }

    public void clickCategories(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostActivity.this);
                mBuilder.setTitle("Select Category(s)");
                mBuilder.setMultiChoiceItems(listCategories,checkedItems, new DialogInterface.OnMultiChoiceClickListener(){
                    public void onClick(DialogInterface di, int pos, boolean isChecked){
                        if(isChecked){
                            //if current item isn't already part of list, add it to list
                            if(! selectedCategories.contains(pos)){
                                selectedCategories.add(pos);
                            }
                        }else if(selectedCategories.contains(pos)){
                            selectedCategories.remove(pos); //user unchecked the item
                        }
                    }
                });

                mBuilder.setCancelable(false);
                /* TODO
                ** After user is done selecting the categories, then we want to send
                ** store the selected items somewhere
                 */
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface di, int which){

                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface di, int which){
                        di.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Clear All", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface di, int which){
                        for(int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                            selectedCategories.clear();

                        }
                    }
                });


                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
    }
}
