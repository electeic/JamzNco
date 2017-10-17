package jamilaappinc.grubmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 *  FROM MY UNDERSTANDING, this class is useless but i'm too afraid to delete it rn = melody
 */
public class ViewGroupsAction extends AppCompatActivity {
    private ArrayList<Group> groups = new ArrayList<Group>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        populateList();
        populateListView();
    }

    private void populateList(){
        Vector<User> group = new Vector<User>();
        for(int i = 0; i < 4; i++){
            group.addElement(new User(i+"", "lol"));
        }
        groups.add(new Group("CSCI310", group));
        groups.add(new Group("Group 2", group));
        groups.add(new Group("yAsKwEEN", group));
    }

    private void populateListView() {
        ArrayAdapter<Group> adapter= new GroupAdapter();
        ListView list = (ListView) findViewById(R.id.groups_ListView);
        list.setAdapter(adapter);

    }

    private class GroupAdapter extends ArrayAdapter<Group>{
        public GroupAdapter(){
            super(ViewGroupsAction.this, R.layout.groups_view,groups );
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){
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
            groupName.setText(currentGroup.getName());

            TextView groupMembers = (TextView)itemView.findViewById(R.id.groups_members);
            groupMembers.setText(members);

            return itemView;
// return  super.getView(position convertView, parent)
        }

    }

}
