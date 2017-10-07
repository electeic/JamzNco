package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Post {
    String mTitle;

    public Post(String mTitle, String mDescription, String mLocation, String mDate,
                String startTime, String endTime, Vector<String> mCategories, Vector<String> mTags,
                Vector<Group> mGroups, Vector<String>  photos) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mLocation = mLocation;
        this.mDate = mDate;
        StartTime = startTime;
        EndTime = endTime;
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

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

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

    String mDescription;
    String mLocation;
    String mDate;
    String StartTime; // TODO: make a date class instead?
    String EndTime;
    Vector<String> mCategories;
    Vector<String> mTags;
    Vector <Group> mGroups;
    int id;
    Vector<String> photos;

}
