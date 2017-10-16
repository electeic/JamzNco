package jamilaappinc.grubmate;

import java.util.ArrayList;
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
    Vector<Subscription> mSubscriptions;
    ArrayList<String> mFriends;

    double mAvgRating;
    String mName;
    String mProfilePhoto;
    String mId;

    public User(String mName, String mPic) {
        this.mName = mName;
        this.mProfilePhoto = mPic;
    }

    public Vector<Post> getUserPosts() {
        return mUserPosts;
    }

    public void addUserPost(Post mUserPost) {
        this.mUserPosts.add(mUserPost);
    }

    public Vector<Request> getUserRequests() {
        return mUserRequests;
    }

    public void addUserRequest(Request mUserRequest) {
        this.mUserRequests.add(mUserRequest);
    }

    public Vector<Group> getUserGroups() {
        return mUserGroups;
    }

    public void addUserGroup(Group group) {
        this.mUserGroups.add(group);
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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Vector<Subscription> getmSubscription() {
        return mSubscriptions;
    }

    public void addmSubscription(Subscription mSubscription) {
        mSubscriptions.add(mSubscription);
    }

    public ArrayList<String> getFriends(){
        return mFriends;
    }

    public void setFriends(ArrayList<String> newFriends){
        mFriends = newFriends;
    }


}
