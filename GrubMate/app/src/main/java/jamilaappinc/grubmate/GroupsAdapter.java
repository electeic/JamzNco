package jamilaappinc.grubmate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static jamilaappinc.grubmate.R.id.groups;

/**
 * Created by melod on 10/14/2017.
 */

public class GroupsAdapter extends ArrayAdapter<Group> {
    ArrayList<Group> groups;
    private Context context;
    public GroupsAdapter(Context context, ArrayList<Group> groups){
        super(context, 0, groups);
        this.groups = groups;
//        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = convertView;
        if(itemView == null){
//                Log.d("checking", "idk");
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.groups_view, parent, false);
//            itemView = getLayoutInflater().inflate(R.layout.groups_view, parent, false);
        }
        //find the group to work with
        Group currentGroup = getItem(position);
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

        Log.d("THE MEMBERS ARE: ", members);
        // fill the view
        TextView groupName = (TextView)itemView.findViewById(R.id.groups_name);
        TextView groupMembers = (TextView)itemView.findViewById(R.id.groups_members);
        groupName.setText(currentGroup.getName());
        groupMembers.setText(members);

        return itemView;
    }
}
