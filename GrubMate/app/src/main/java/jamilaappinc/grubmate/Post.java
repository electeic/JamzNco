package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Post {
    String mTitle;
    String mDescription;
    String mLocation;
    Date mDate;
    Vector<String> mCategories;
    Vector<String> mTags;
    Vector <Group> mGroups;
    int id;
    Vector<String> photos;
    User mPoster;

    public Post(String mTitle, String mDescription, String mLocation, Date mDate,
                Vector<String> mCategories, Vector<String> mTags,
                Vector<Group> mGroups, Vector<String>  photos, User poster) {
        this.mPoster = poster;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mLocation = mLocation;
        this.mDate = mDate;
        this.mCategories = mCategories;
        this.mTags = mTags;
        this.mGroups = mGroups;
        this.photos = photos;
    }

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

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {this.mDate = mDate;}

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

    public Vector<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Vector<String> photos) {
        this.photos = photos;
    }


}
