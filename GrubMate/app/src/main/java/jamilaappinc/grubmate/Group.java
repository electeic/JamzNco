package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Group implements Serializable{
    String mName;
    ArrayList<User> mGroupMembers = new ArrayList<>();
    HashMap<String, User> mGroupMembersList = new HashMap<>();
    String mId;
    String mUserAuthorId;
    String mKey;
    String mUsers;

    public Group(String mName, HashMap<String, User> mGroupMembersList) {
        this.mName = mName;
        this.mGroupMembersList = mGroupMembersList;

    }

    public Group() {

    }

   /* public Group(String mName, HashMap<String, User> mGroupMembersList){
        this.mName = mName;
        this.mGroupMembersList = mGroupMembersList;
    }
*/
    public HashMap<String, User> getmGroupMembersList(){return mGroupMembersList;}

    public void addmGroupMembersList(String id, User user){mGroupMembersList.put(id,user);}

    public void setmGroupMembersList(HashMap<String,User> map) {mGroupMembersList.putAll(map);}

    public void removemGroupMemberList(String id){mGroupMembersList.remove(id);}

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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setmUserAuthorId(String id) {this.mUserAuthorId = id;}

    public String getmUserAuthorId() {return mUserAuthorId;}

    public void setKey(String key) {this.mKey = key;}

    public String getKey() {return mKey;}

    public boolean userExists(String userId){
        if(mUsers.contains(userId)){
            return true;
        }
        else return false;
    }

    public void addFriend(String userId){
        if(mUsers != null){
            mUsers += ",";
        }
        else{
            mUsers = "";
        }
        mUsers+=userId;
    }

    public String getmUsers(){
        return mUsers;
    }
}
