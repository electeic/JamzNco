package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Post {
    String mTitle;
    String mDescription;
    String mLocation;
    DateAndTime mDate;
    Vector<String> mCategories;
    Vector<String> mTags;
    Vector <Group> mGroups;
    int id;
//    Vector<String> mPhotos;
    String mPhotos;
    String mPoster;
    int mServings;
    boolean mActive;

    public Post(String title){
        mTitle = title;
    }


//    public Post(String mTitle, String mDescription, String mLocation, DateAndTime mDate,
//                Vector<String> mCategories, Vector<String> mTags,
//                Vector<Group> mGroups, String photos, int servings,  String poster) {
    public Post(String mTitle, String mDescription){
//        this.mPoster = poster;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
//        this.mLocation = mLocation;
//        this.mDate = mDate;
//        this.mCategories = mCategories;
//        this.mTags = mTags;
//        this.mGroups = mGroups;
//        this.mPhotos = photos;
//        mServings = servings;
        mActive = Boolean.TRUE;
    }

    public Post(){

    }

    public boolean isActive(){ return mActive;}

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

    public DateAndTime getmDate() {
        return mDate;
    }

    public void setmDate(DateAndTime mDate) {this.mDate = mDate;}

    public Vector<String> getmCategories() {
        return mCategories;
    }

    public void setmCategories(Vector<String> mCategories) {
        this.mCategories = mCategories;
    }

    public Vector<String> getmTags() {
        return mTags;
    }

    public void setmTags(Vector<String> mTags) {
        this.mTags = mTags;
    }

    public Vector<Group> getmGroups() {
        return mGroups;
    }

    public void setmGroups(Vector<Group> mGroups) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(String photos) {
        this.mPhotos = photos;
    }


}
