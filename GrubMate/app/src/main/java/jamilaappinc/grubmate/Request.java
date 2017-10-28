package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Request implements Serializable{

    String mLocation;
    String mRequestedPostId;
    int mServings;
    String mId;
    String mRequestUserId;
    boolean mResponded;
    boolean mAccepted;
    boolean mActive;
    Post mPost;
    String requestedUserName;

    public Request(String mLocation, String mRequestedPost, int mServings,Post post, String muser) {
        this.mLocation = mLocation;
        this.mRequestedPostId = mRequestedPost; //id of the request in the DB
        this.mServings = mServings;
        mRequestUserId = muser; //  the id of the user making the request
        mPost = post; // use the post to get the id of the user who made the post
        mAccepted = Boolean.FALSE;
        mResponded = Boolean.FALSE;
        mActive = Boolean.TRUE;
    }

    public Request(){

    }

    public void setRequestedUserName(String name) {requestedUserName = name;}
    public String getRequestedUserName(){return requestedUserName;}
    public boolean getmActive(){return mActive;}

    public void setmActive(boolean isActive){mActive = isActive;}

    public String getmRequestUserId(){ return mRequestUserId; }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmRequestedPost() {
        return mRequestedPostId;
    }

    public void setmRequestedPost(String mRequestedPost) {
        this.mRequestedPostId = mRequestedPost;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String id) {mId = id; }

    public Post getmPost(){return mPost;}


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
