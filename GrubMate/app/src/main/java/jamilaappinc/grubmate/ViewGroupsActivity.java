package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class ViewGroupsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "main_position";
    private ArrayList<Group> groups = new ArrayList<Group>();

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
            f = ViewGroupsFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();

//        populateList();


    }

//    private void populateList(){
//        Vector<User> group = new Vector<User>();
//        for(int i = 0; i < 4; i++){
//            group.addElement(new User(i+"", "lol"));
//        }
//        groups.add(new Group("CSCI310", group));
//        groups.add(new Group("Group 2", group));
//        groups.add(new Group("yAsKwEEN", group));
//
//        populateListView();
//    }

    private void populateListView() {
        GroupsAdapter adapter= new GroupsAdapter(this, groups);
//        ListView list = (ListView) findViewById(R.id.GroupsListView);
//        Log.d("POPULATE", "BOO");
//        list.setAdapter(adapter);

    }

    /*private class GroupAdapter extends ArrayAdapter<Group>{
        public GroupAdapter(){
            super(ViewGroupsActivity.this,0, groups );
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){
//                itemView = LayoutInflater.from(getContext()).inflate(R.layout.groups_view, parent, false);
                itemView = getLayoutInflater().inflate(R.layout.groups_view, parent, false);
            }
            //find the group to work with
            Group currentGroup = groups.get(position);
            String members = "";
            for(int i = 0; i < currentGroup.getGroupMembers().size(); i++){
                if(i < (currentGroup.getGroupMembers().size() -1)){
                    members = members+" " + currentGroup.getGroupMembers().get(i).getName()+", ";
                }else{
                    members = members+" " + currentGroup.getGroupMembers().get(i).getName();

                }

            }

            if(members.length() > 100){
                members = members.substring(0, 100);
                members+=" ...";
            }


            // fill the view
            TextView groupName = (TextView)itemView.findViewById(R.id.groups_name);
            TextView groupMembers = (TextView)itemView.findViewById(R.id.groups_members);
            groupName.setText(currentGroup.getName());
            groupMembers.setText(members);

            return itemView;
// return  super.getView(position convertView, parent)
        }

    }*/

}
