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
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by ericajung on 10/13/17.
 */

public class PostActivity extends AppCompatActivity implements TimeStartPickerFragment.TimeStartDialogListener, TimeEndPickerFragment.TimeEndDialogListener, DatePickerFragment.DateDialogListener, EndDatePickerFragment.EndDateDialogListener{

    public static final String EXTRA_POSITION = "main_position";
    public static final String EDIT_POSITION = "edit_position";
    String[] listCategories,listGroups;

    boolean [] checkedItems, groupCheckedItems;
    ArrayList<String> selectedCategories = new ArrayList<String>();
    ArrayList<String> selectedGroups = new ArrayList<>();

    private Button startDate, endDate;
    String startDateString;

    DataFromActivityToFragment dataFromActivityToFragment;

    ArrayList<Integer> groupsCount = new ArrayList<>();
    ArrayList<Integer> groupsReadCounter = new ArrayList<>();
    ArrayList<Integer> groupsMatchCounter = new ArrayList<>();

    ArrayList<String> allGroups = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference dbRefGroups;

    String ID;
    String currUserName;

    //interface to send data from activity to fragment
    public interface DataFromActivityToFragment {
        void sendStartDate(String data);
        void sendEndDate(String data);
        void sendStartTime(String time);
        void sendEndTime(String time);
        void sendCategories(ArrayList<String>cat);
        void sendGroups(ArrayList<String> _group);
    }

    /**
     * NOTE: NEED TO CHANGE THE LINE WHERE WE GET THE LIST OF CATEGORIES & GROUPS
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("id","hey");

        setContentView(R.layout.activity_main);
        dbRefGroups = database.getInstance().getReference().child(FirebaseReferences.GROUPS);
        listCategories = getResources().getStringArray(R.array.categories);
        groupsReadCounter.add(0);
        groupsMatchCounter.add(0);
        database = FirebaseDatabase.getInstance();
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Group")) {
                        groupsCount.add((int)snap.getChildrenCount());
                        listGroups = new String[(int)snap.getChildrenCount()];
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //listGroups = getResources().getStringArray(R.array.tempGroup);
        checkedItems = new boolean[listCategories.length];
        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);
        String s = i.getStringExtra(EDIT_POSITION);
        System.out.println("POSTACTIVITY Post firebase is " + s);
//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);


        if (f == null ) {
            //TODO modify for id
            f = PostFragment.newInstance(pos, s);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();

        //this is how you initialize the connection bt fragment and activity
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostActivity.this);
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
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostActivity.this);
        mBuilder.setTitle("Select Group(s)");
        Intent j = getIntent();
        ID = j.getStringExtra("ID");
        currUserName = j.getStringExtra("Name");
        final TaskCompletionSource<List<Objects>> tcs = new TaskCompletionSource<>();
        groupsReadCounter = new ArrayList<>();
        groupsMatchCounter = new ArrayList<>();
        groupsReadCounter.add(0);
        groupsMatchCounter.add(0);

        dbRefGroups.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int groupsRead = groupsReadCounter.get(0);
                groupsRead++;
                System.out.println("POSTS READ COUNT " + groupsRead);
                groupsReadCounter.clear();
                groupsReadCounter.add(groupsRead);


                Group group = dataSnapshot.getValue(Group.class);
                System.out.println("GROUP RECEIVED" + group.getName());
                System.out.println("USER ID IS" + ID);



                System.out.println("Group reading with users" + group.getmUsers());

                if(group.getmUsers().contains(ID)){
                    System.out.println("GROUP MATCHED" + group.getName() + " " + ID);

                    listGroups[groupsMatchCounter.get(0)] = group.getName();
                        int groupsMatched = groupsMatchCounter.get(0);
                        groupsMatched++;
                        groupsMatchCounter.clear();
                        groupsMatchCounter.add(groupsRead);
                }


                groupCheckedItems = new boolean[listGroups.length];


                if (groupsReadCounter.get(0) == groupsCount.get(0)) {//all posts read.
                    mBuilder.setMultiChoiceItems(listGroups,groupCheckedItems, new DialogInterface.OnMultiChoiceClickListener(){
                        public void onClick(DialogInterface di, int pos, boolean isChecked){
                            if(isChecked){
                                //if current item isn't already part of list, add it to list

                                if(!selectedGroups.contains(listGroups[pos])){
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
                            sendGroups();
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
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






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
