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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ericajung on 10/16/17.
 */

public class SearchActivity extends AppCompatActivity implements TimeStartPickerFragment.TimeStartDialogListener, TimeEndPickerFragment.TimeEndDialogListener, DatePickerFragment.DateDialogListener, EndDatePickerFragment.EndDateDialogListener{
    public static final String EXTRA_POSITION = "main_position";
        String[] listCategories,listGroups;

        boolean [] checkedItems, groupCheckedItems;
        ArrayList<String> selectedCategories = new ArrayList<String>();
        ArrayList<String> selectedGroups = new ArrayList<>();

        private Button startDate, endDate;
        String startDateString;

        DataFromActivityToFragment dataFromActivityToFragment;


        //interface to send data from activity to fragment
        public interface DataFromActivityToFragment {
            void sendStartDate(String data);
            void sendEndDate(String data);
            void sendStartTime(String time);
            void sendEndTime(String time);
            void sendCategories(ArrayList<String>cat);
            void sendGroups(ArrayList<String> _group);
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");
        setContentView(R.layout.activity_main);
        listCategories = getResources().getStringArray(R.array.categories);
        listGroups = getResources().getStringArray(R.array.tempGroup);
        checkedItems = new boolean[listCategories.length];
        groupCheckedItems = new boolean[listGroups.length];
        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            //TODO modify for id
            f = SearchFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
        dataFromActivityToFragment = (DataFromActivityToFragment)f;
    }

    /**
     *  Called by the click="" in the XML files
     *  Used to create the separate dialog fragments
     *
     */
    public void selectStartDate(View v){
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.show(getSupportFragmentManager(), "Date");
    }

    public void selectStartTime(View v){
        TimeStartPickerFragment dialog = new TimeStartPickerFragment();
        dialog.show(getSupportFragmentManager(), "Start time");
    }

    public void selectEndTime(View v){
        TimeEndPickerFragment dialog = new TimeEndPickerFragment();
        dialog.show(getSupportFragmentManager(), "End time");
    }
    public void selectEndDate(View v){
        EndDatePickerFragment dialog = new EndDatePickerFragment();
        dialog.show(getSupportFragmentManager(),"Date");
    }
    /**
     *
     * @param date makes the date format more universal
     * @return
     */
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    /*
        Called when the Choose Category button is clicked, it pops up a dialog of options
     */
    public void clickCategories(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this);
        mBuilder.setTitle("Select Category(s)");
        mBuilder.setMultiChoiceItems(listCategories,checkedItems, new DialogInterface.OnMultiChoiceClickListener(){
            public void onClick(DialogInterface di, int pos, boolean isChecked){
                if(isChecked){
                    //if current item isn't already part of list, add it to list

                    if(! selectedCategories.contains(listCategories[pos])){
                        Log.d("NOT PART", pos+"");
                        selectedCategories.add(listCategories[pos]);
                    }
                }else if(selectedCategories.contains(listCategories[pos])){
                    selectedCategories.remove(listCategories[pos]); //user unchecked the item
                    Log.d("PART", pos+"");

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
                sendCategories();
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


    /**
     *
     * @param v Connects the button with the activity
     *          Used when the Add Groups button is chosen
     */
    public void clickGroups(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this);
        mBuilder.setTitle("Select Group(s)");
        mBuilder.setMultiChoiceItems(listGroups,groupCheckedItems, new DialogInterface.OnMultiChoiceClickListener(){
            public void onClick(DialogInterface di, int pos, boolean isChecked){
                if(isChecked){
                    //if current item isn't already part of list, add it to list

                    if(! selectedGroups.contains(listGroups[pos])){
                        Log.d("NOT PART", pos+"");
                        selectedGroups.add(listGroups[pos]);
                    }
                }else if(selectedGroups.contains(listGroups[pos])){
                    selectedGroups.remove(listGroups[pos]); //user unchecked the item
                    Log.d("PART", pos+"");

                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di, int which){
                sendGroups();
            }
        });

        mBuilder.setNeutralButton("Select All", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di, int which){
                selectedGroups.clear();
                for(int i = 0; i < groupCheckedItems.length; i++){
                    groupCheckedItems[i] = true;
                    selectedGroups.add(listGroups[i]);
                }
            }
        });
        mBuilder.setNegativeButton("Clear All", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di, int which){
                for(int i = 0; i < groupCheckedItems.length; i++){
                    groupCheckedItems[i] = false;
                    selectedGroups.clear();

                }
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    /**
     * Sends information to the Fragment, aka interface method calls
     *
     */
    public void onFinishDialog(Date date) {
        dataFromActivityToFragment.sendStartDate(formatDate(date));
    }

    public void onFinishEndDialog(Date date) {
        dataFromActivityToFragment.sendEndDate(formatDate(date));
    }

    @Override
    public void onFinishStartDialog(String time) {
        dataFromActivityToFragment.sendStartTime(time);
    }

    @Override
    public void onFinishEndDialog(String time) {
        dataFromActivityToFragment.sendEndTime(time);
    }

    public void sendCategories(){
        dataFromActivityToFragment.sendCategories(selectedCategories);
    }

    private void sendGroups(){
        dataFromActivityToFragment.sendGroups(selectedGroups);
    }



}
