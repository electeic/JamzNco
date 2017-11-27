package jamilaappinc.grubmate;

import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Post implements Serializable {
    String mTitle;
    String mDescription;
    String mAddress;
    double mLatitude, mLongitude;
    Date mStartDate, mEndDate;
    ArrayList<String> mCategories;
    ArrayList<String> mTags;
    ArrayList <Group> mGroups;
    ArrayList<User> mSelectedFriends;
    Map<String, String> mAcceptedUsers;
    String id;
    String mPhotos;
    String mAuthorId;
    int mServings;
    boolean mActive;
    boolean homemade;
    String mAuthorPic;
    String mFirebaseKey;
    String mGroupIds;
    String allTargetedUsers = "";
    ArrayList<String> mAllFoodPics;
    int priority;

//    public Post(String title, int servings){
//        mTitle = title;
//        mServings = servings;
//    }

    public ArrayList<String> getmAllFoodPics() {
        return mAllFoodPics;
    }

    public void setmAllFoodPics(ArrayList<String> mAllFoodPics) {
        this.mAllFoodPics = mAllFoodPics;
    }

    public Post(String mTitle, String mDescription, double mLatitude, double mLongitude, String mAddress, Date mStart, Date mEnd,
                ArrayList<String> mCategories, ArrayList<String> mTags,
                ArrayList<Group> mGroups, String photos, int servings, boolean homemade, String mAuthorId,
                String authorPicture, String mFirebaseKey, ArrayList<String> allFoodPics, ArrayList<User> mSelectedFriends) {
        this.mAuthorId = mAuthorId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mAddress = mAddress;
        this.mStartDate = mStart;
        this.mEndDate = mEnd;
        this.mCategories = mCategories;
        this.mTags = mTags;
        this.mGroups = mGroups;
        this.mPhotos = photos;
        this.mServings = servings;
        this.mActive = Boolean.TRUE;
        this.homemade = homemade;
        this.mAuthorPic = authorPicture;
        this.mAcceptedUsers = new HashMap<>();
        this.mFirebaseKey = mFirebaseKey;
        this.mAllFoodPics = allFoodPics;
        this.mSelectedFriends = mSelectedFriends;
    }

//    public Post(String mTitle, String mDescription){
//        this.mTitle = mTitle;
//        this.mDescription = mDescription;
//        mActive = Boolean.TRUE;
//    }

    public Map<String, String> getmAcceptedUsers(){return mAcceptedUsers;}
    public void addmAcceptedUsers(String id){mAcceptedUsers.put(id,id);}
    public void removemAcceptedUser(String id){
        mAcceptedUsers.remove(id);
    }

    public Post(){

    }

    public boolean isActive(){
        //if(!mActive) return mActive;
        //else {
            mActive = Boolean.FALSE;
            Date date = Calendar.getInstance().getTime();
            System.out.println(mEndDate);
            if (this.mEndDate.before(date)) {
                if (!outOfServings()) {
                    mActive = Boolean.TRUE;
                }
            }
            return mActive;
        //}
    }

    public boolean getmActive(){
        return mActive;
    }

    public void setmActive(boolean active){ mActive = active;}

    public boolean outOfServings() {
        if(mServings ==0){
            mActive = Boolean.FALSE;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public int getmServings(){ return mServings; };

    public void removemServings(int numToRemove){ mServings -= numToRemove;}

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getmLatitude(){return mLatitude;}

    public void setmLatitude(double mLatitude){this.mLatitude = mLatitude;}

    public double getmLongitude(){return mLongitude;}

    public void setmLongitude(double mLongitude){this.mLongitude = mLongitude;}

    public Date getmStartDate() {
        return mStartDate;
    }

    public Date getmEndDate() {
        return mEndDate;
    }

    public void setmStartDate(Date mStartDate) {this.mStartDate = mStartDate;}

    public void setmEndDate(Date mEndDate) {this.mEndDate = mEndDate;}

    public ArrayList<String> getmCategories() {
        return mCategories;
    }

    public void setmCategories(ArrayList<String> mCategories) {
        this.mCategories = mCategories;
    }

    public ArrayList<String> getmTags() {
        return mTags;
    }

    public void setmTags(ArrayList<String> mTags) {
        this.mTags = mTags;
    }

    public ArrayList<Group> getmGroups() {
        return mGroups;
    }

    public void setmGroups(ArrayList<Group> mGroups) {
        this.mGroups = mGroups;
    }

    public void addmGroup(Group group){ mGroups.add(group);}

    public void removemGroup(Group group) {
        for(Group temp: mGroups){
            if(temp.equals(group)){
                mGroups.remove(group);
            }
        }
    }

    public String getmId() {
        return id;
    }

    public void setmId(String id) {
        this.id = id;
    }

    public String getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(String photos) {
        this.mPhotos = photos;
    }

    public String getmAuthorId(){
        return mAuthorId;
    }
    public void setmAuthorId(String newID){
        mAuthorId = newID;
    }

    public boolean getHomemade(){ return homemade; }
    public void setHomemade(boolean b) {homemade = b; }

    public String getmAuthorPic() {
        return mAuthorPic;
    }

    public void setmAuthorPic(String mAuthorPic) {
        this.mAuthorPic = mAuthorPic;
    }

    public String getmFirebaseKey() {
        return mFirebaseKey;
    }

    public void setmFirebaseKey(String mFirebaseKey) {
        this.mFirebaseKey = mFirebaseKey;
    }

    public boolean findTag(String tag){
        for(int i = 0; i < mTags.size(); i++){
            if(tag.equals(mTags.get(i))){
                return true;
            }
        }
        return false;
    }

    public boolean findCategory(String category){
        for(int i = 0; i < mCategories.size(); i++){
            if(category.equals(mCategories.get(i))){
                return true;
            }
        }
        return false;
    }

    public String getmGroupString(){
        return mGroupIds;
    }

    public void addGroup(String group){
        if(mGroupIds != null){
            mGroupIds += ",";
        }
        else{
            mGroupIds = "";
        }
        mGroupIds+=group;

    }

    public void userTargetedOrAdd(String userId){
        if(!allTargetedUsers.contains(userId)){
            //if(allTargetedUsers.equals("")){
                allTargetedUsers+=",";
            //}

            allTargetedUsers+=userId;
        }
    }

    public String getTargetUsersString(){
        return allTargetedUsers;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
