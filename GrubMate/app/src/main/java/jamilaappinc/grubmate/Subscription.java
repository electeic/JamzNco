package jamilaappinc.grubmate;


import java.io.Serializable;
import java.util.Vector;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Subscription implements Serializable {

    String mUserAuthorId;
    Date mStartDate, mEndDate;
    ArrayList<String> mTags;
    ArrayList<String> mCategories;
    ArrayList<String> mPostsIds;
    boolean mHomeMade;
    boolean mActive;
    String mId;
    String mTitle;
    String mDescription;
    ArrayList<Group> mGroups;

    public Subscription(ArrayList<String> mTags, ArrayList<String> mCategories) {
        this.mTags = mTags;
        this.mCategories = mCategories;
    }

    public Subscription( String title,String description, Date mStartDate, Date mEndDate,  ArrayList<String> mCategories,
                         ArrayList<String> mTags, ArrayList<Group> groups, String mUser, boolean mHomeMade, String mId, ArrayList<String> mPostsIds) {
        this.mDescription = description;
        this.mTitle = title;
        this.mUserAuthorId = mUser;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mTags = mTags;
        this.mCategories = mCategories;
        mGroups = groups;
        this.mHomeMade = mHomeMade;
        mActive = Boolean.TRUE;
        this.mId = mId;
        this.mPostsIds = mPostsIds;
    }

    public boolean getmActive() { return mActive; }

    public void setmActive(boolean isActive) { mActive = isActive; }
    public String getmUserAuthorId() {
        return mUserAuthorId;
    }

    public void setmUser(String mUser) {
        this.mUserAuthorId = mUser;
    }

    public Date getmEndDate() { return mEndDate; }

    public Date getmStartDate() { return mStartDate; }

    public void setmStartDate(Date date) { mStartDate = date; }

    public void setmEndDate(Date date) { mEndDate = date; }

    public ArrayList<String> getmTags() {
        return mTags;
    }

    public void setmTags(ArrayList<String> mTags) {
        this.mTags = mTags;
    }

    public ArrayList<String> getmCategories() {
        return mCategories;
    }

    public void setmCategories(ArrayList<String> mCategories) {
        this.mCategories = mCategories;
    }

    public boolean ismHomeMade() {
        return mHomeMade;
    }

    public void setmHomeMade(boolean mHomeMade) {
        this.mHomeMade = mHomeMade;
    }

    public void setmId(String key) {
        mId = key;
    }

    public String getmId() { return mId; }

    public void setmUserAuthorId(String mUserAuthorId) {
        this.mUserAuthorId = mUserAuthorId;
    }

    public ArrayList<String> getmPosts() {
        return mPostsIds;
    }

    public void setmPosts(ArrayList<String> mPosts) {
        this.mPostsIds = mPosts;
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
}
