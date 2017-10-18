package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Post implements Serializable{
    String mTitle;
    String mDescription;
    String mLocation;
    Date mStartDate, mEndDate;
    ArrayList<String> mCategories;
    ArrayList<String> mTags;
    ArrayList <Group> mGroups;
    String id;
    String mPhotos;
    String mAuthorId;
    int mServings;
    boolean mActive;
    boolean homemade;

    public Post(String title, int servings){

        mTitle = title;
        mServings = servings;
    }



    public Post(String mTitle, String mDescription, String mLocation, Date mStart, Date mEnd,
                ArrayList<String> mCategories, ArrayList<String> mTags,
                ArrayList<Group> mGroups, String photos, int servings, boolean homemade, String mAuthorId) {
        this.mAuthorId = mAuthorId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mLocation = mLocation;
        this.mStartDate = mStart;
        this.mEndDate = mEnd;
        this.mCategories = mCategories;
        this.mTags = mTags;
        this.mGroups = mGroups;
        this.mPhotos = photos;
        this.mServings = servings;
        this.mActive = Boolean.TRUE;
        this.homemade = homemade;
    }

    public Post(String mTitle, String mDescription){
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        mActive = Boolean.TRUE;
    }

    public Post(){

    }

    public boolean isActive(){
        mActive = Boolean.FALSE;
        if(this.mEndDate.before(new Date())){
            if(!outOfServings()){
                mActive = Boolean.TRUE;
            }
        }
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

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

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

}
