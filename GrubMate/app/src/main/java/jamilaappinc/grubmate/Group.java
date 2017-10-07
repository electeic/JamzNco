package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Group {
    String mName;
    Vector<User> mGroupMembers;
    int mId;

    public Group(String mName, Vector<User> mGroupMembers) {
        this.mName = mName;
        this.mGroupMembers = mGroupMembers;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Vector<User> getGroupMembers() {
        return mGroupMembers;
    }

    public void setGroupMembers(Vector<User> groupMembers) {
        this.mGroupMembers = groupMembers;
    }

    public void setGroupMember(User member) {this.mGroupMembers.add(member);}

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

}
