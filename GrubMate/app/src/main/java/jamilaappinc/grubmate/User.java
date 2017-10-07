package jamilaappinc.grubmate;

import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class User {
    Vector<Post> mUserPosts;
    Vector<Request> mUserRequests;
    Vector<Group> mUserGroups;
    Vector<Rating> mRatings;
    Vector<Notification> mNotifications;

    double mAvgRating;
    String mName;
    String mProfilePhoto;
    int mId;
    Subscription mSubscription;

    public User(String mName, String mPic) {
        this.mName = mName;
        this.mProfilePhoto = mPic;
    }

    public Vector<Post> getUserPosts() {
        return mUserPosts;
    }

    public void setUserPosts(Vector<Post> mUserPosts) {
        this.mUserPosts = mUserPosts;
    }

    public Vector<Request> getUserRequests() {
        return mUserRequests;
    }

    public void setUserRequests(Vector<Request> mUserRequests) {
        this.mUserRequests = mUserRequests;
    }

    public Vector<Group> getUserGroups() {
        return mUserGroups;
    }

    public void setUserGroups(Vector<Group> mUserGroups) {
        this.mUserGroups = mUserGroups;
    }

    public Vector<Rating> getRatings() {
        return mRatings;
    }

    public void setRatings(Vector<Rating> mRatings) {
        this.mRatings = mRatings;
    }

    public Vector<Notification> getNotifications() {
        return mNotifications;
    }

    public void setNotifications(Vector<Notification> notifications) {
        this.mNotifications = notifications;
    }

    public double getmAvgRating() {
        return mAvgRating;
    }

    public void setAvgRating(double mAvgRating) {
        this.mAvgRating = mAvgRating;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getProfilePhoto() {
        return mProfilePhoto;
    }

    public void setProfilePhoto(String mProfilePhoto) {
        this.mProfilePhoto = mProfilePhoto;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Subscription getmSubscription() {
        return mSubscription;
    }

    public void setmSubscription(Subscription mSubscription) {
        this.mSubscription = mSubscription;
    }


}
