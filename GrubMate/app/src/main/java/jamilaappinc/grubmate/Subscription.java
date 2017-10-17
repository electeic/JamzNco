package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Subscription implements Serializable{

    User mUser;
    DateAndTime mDate;
    Vector<String> mTags;
    Vector<String> mCategories;
    boolean mHomeMade;
    boolean mActive;
    int mId;
    Post mPost;

    public Subscription(Vector<String> mTags, Vector<String> mCategories){
        this.mTags = mTags;
        this.mCategories = mCategories;
    }

    public Subscription(User mUser, DateAndTime mDate, Vector<String> mTags,
                        Vector<String> mCategories, boolean mHomeMade) {
        this.mUser = mUser;
        this.mDate = mDate;
        this.mTags = mTags;
        this.mCategories = mCategories;
        this.mHomeMade = mHomeMade;
        mActive = Boolean.TRUE;
    }

    public boolean getmActive(){return mActive;}

    public void setmActive(boolean isActive){mActive = isActive;}
    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public DateAndTime getmDate() { return mDate; }

    public void setmDate (DateAndTime date) { mDate = date;}

    public Vector<String> getmTags() {
        return mTags;
    }

    public void setmTags(Vector<String> mTags) {
        this.mTags = mTags;
    }

    public Vector<String> getmCategories() {
        return mCategories;
    }

    public void setmCategories(Vector<String> mCategories) {
        this.mCategories = mCategories;
    }

    public boolean ismHomeMade() {
        return mHomeMade;
    }

    public void setmHomeMade(boolean mHomeMade) {
        this.mHomeMade = mHomeMade;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Post getmPost() {
        return mPost;
    }

    public void setmPost(Post mPost) {
        this.mPost = mPost;
    }

}
