package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Group implements Serializable{
    String mName;
    ArrayList<User> mGroupMembers;
    int mId;

    public Group(String mName, ArrayList<User> mGroupMembers) {
        this.mName = mName;
        this.mGroupMembers = mGroupMembers;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<User> getGroupMembers() {
        return mGroupMembers;
    }

    public void setGroupMembers(ArrayList<User> groupMembers) {
        this.mGroupMembers = groupMembers;
    }

    public void setGroupMember(User member) {this.mGroupMembers.add(member);}

    public Boolean isInGroup(User member) {
        for(User temp: mGroupMembers){
            if(temp.equals(member)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

}
