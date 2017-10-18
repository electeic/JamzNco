package jamilaappinc.grubmate;


import java.io.Serializable;
import java.util.Vector;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ivanchen on 10/2/17.
 */

    public class Subscription implements Serializable{

    String mUserAuthorId;
    Date mStartDate, mEndDate;
    ArrayList<String> mTags;
    ArrayList<String> mCategories;
    ArrayList<Post> mPosts;
    boolean mHomeMade;
    boolean mActive;
    String mId;
    String mTitle;
    String mDescription;

    public Subscription(ArrayList<String> mTags, ArrayList<String> mCategories){
        this.mTags = mTags;
        this.mCategories = mCategories;
    }

    public Subscription( String title,String description, Date mStartDate, Date mEndDate,  ArrayList<String> mCategories,
                         ArrayList<String> mTags,String mUser, boolean mHomeMade, String mId, ArrayList<Post> mPosts) {
        this.mDescription = description;
        this.mTitle = title;
        this.mUserAuthorId = mUser;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mTags = mTags;
        this.mCategories = mCategories;
        this.mHomeMade = mHomeMade;
        mActive = Boolean.TRUE;
        this.mId = mId;
        this.mPosts = mPosts;
    }

    public boolean getmActive(){return mActive;}

    public void setmActive(boolean isActive){mActive = isActive;}
    public String getmUserAuthorId() {
        return mUserAuthorId;
    }

    public void setmUser(String mUser) {
        this.mUserAuthorId = mUser;
    }

    public Date getmEndDate() { return mEndDate; }

    public Date getmStartDate() { return mStartDate; }

    public void setmStartDate (Date date) { mStartDate = date;}

    public void setmEndDate (Date date) { mEndDate = date;}

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

    public String getmId(){return mId; }



}
