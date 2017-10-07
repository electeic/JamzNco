package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Request {

    String mLocation;
    Date mDate;
    Post mRequestedPost;
    int mServings;
    int mId;
    User mRequestingUser;
    boolean mResponded;
    boolean mAccepted;

    public Request(String mLocation, Date date, Post mRequestedPost, int mServings, User muser) {
        this.mLocation = mLocation;
        this.mDate = date;
        this.mRequestedPost = mRequestedPost;
        this.mServings = mServings;
        mRequestingUser = muser;
    }

    public User getmRequestingUser(){ return mRequestingUser; }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Date getmDate() { return mDate; }

    public void setmDate(Date mTime) {
        this.mDate = mTime;
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
