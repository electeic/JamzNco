package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Request {

    String mLocation;
    String mTimeStart;
    String mTimeEnd;
    Post mRequestedPost;
    int mServings;
    int mId;
    boolean mResponded;
    boolean mAccepted;

    public Request(String mLocation, String mTimeStart, String mTimeEnd, Post mRequestedPost, int mServings) {
        this.mLocation = mLocation;
        this.mTimeStart = mTimeStart;
        this.mTimeEnd = mTimeEnd;
        this.mRequestedPost = mRequestedPost;
        this.mServings = mServings;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
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
