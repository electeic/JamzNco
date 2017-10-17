package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Request implements Serializable{

    String mLocation;
    Post mRequestedPost;
    int mServings;
    int mId;
    User mRequestingUser;
    boolean mResponded;
    boolean mAccepted;
    boolean mActive;

    public Request(String mLocation, Post mRequestedPost, int mServings, User muser) {
        this.mLocation = mLocation;
        this.mRequestedPost = mRequestedPost;
        this.mServings = mServings;
        mRequestingUser = muser;
        mAccepted = Boolean.FALSE;
        mResponded = Boolean.FALSE;
        mActive = Boolean.TRUE;
    }

    public boolean getmActive(){return mActive;}

    public void setmActive(boolean isActive){mActive = isActive;}

    public User getmRequestingUser(){ return mRequestingUser; }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Post getmRequestedPost() {
        return mRequestedPost;
    }

    public void setmRequestedPost(Post mRequestedPost) {
        this.mRequestedPost = mRequestedPost;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public boolean ismResponded() {
        return mResponded;
    }

    public void setmResponded(boolean mResponded) {
        this.mResponded = mResponded;
    }

    public boolean ismAccepted() {
        return mAccepted;
    }

    public void setmAccepted(boolean mAccepted) {
        this.mAccepted = mAccepted;
    }

}
