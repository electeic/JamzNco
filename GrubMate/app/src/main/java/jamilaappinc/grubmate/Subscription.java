package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Subscription {

    User mUser;
    Date mDate;
    Vector<String> mTags;
    Vector<String> mCategories;
    boolean mHomeMade;
    int mId;
    Post mPost;


    public Subscription(User mUser, Date mDate, Vector<String> mTags,
                        Vector<String> mCategories, boolean mHomeMade) {
        this.mUser = mUser;
        this.mDate = mDate;
        this.mTags = mTags;
        this.mCategories = mCategories;
        this.mHomeMade = mHomeMade;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public Date getmDate() { return mDate; }

    public void setmDate (Date date) { mDate = date;}

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
