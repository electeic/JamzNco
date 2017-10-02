package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Subscription {


    public Subscription(User mUser, String mTimeStart, String mTimeEnd, Vector<String> mTags, Vector<String> mCategories, boolean mHomeMade) {
        this.mUser = mUser;
        this.mTimeStart = mTimeStart;
        this.mTimeEnd = mTimeEnd;
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

    public String getmTimeStart() {
        return mTimeStart;
    }

    public void setmTimeStart(String mTimeStart) {
        this.mTimeStart = mTimeStart;
    }

    public String getmTimeEnd() {
        return mTimeEnd;
    }

    public void setmTimeEnd(String mTimeEnd) {
        this.mTimeEnd = mTimeEnd;
    }

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

    User mUser;
    String mTimeStart;
    String mTimeEnd;
    Vector<String> mTags;
    Vector<String> mCategories;
    boolean mHomeMade;
    int mId;
    Post mPost;


}
