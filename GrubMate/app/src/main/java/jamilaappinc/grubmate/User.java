package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class User implements Serializable {
    Vector<Post> userPosts;
    Vector<Request> userRequests;
    Vector<Group> userGroups;
    Vector<Rating> ratings;
    Vector<Notification> notifications;
    Vector<Subscription> subscriptions;
    ArrayList<String> friends;

    double avgRating;
    String name;
    String profilePhoto;
    String id;

    public User(String mName, String mPic) {
        this.name = mName;
        this.profilePhoto = mPic;
    }

    public User() {

    }

    public Vector<Post> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(Vector<Post> userPosts) {
        this.userPosts = userPosts;
    }

    public Vector<Request> getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(Vector<Request> userRequests) {
        this.userRequests = userRequests;
    }

    public Vector<Group> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Vector<Group> userGroups) {
        this.userGroups = userGroups;
    }

    public Vector<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Vector<Rating> ratings) {
        this.ratings = ratings;
    }

    public Vector<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Vector<Notification> notifications) {
        this.notifications = notifications;
    }

    public Vector<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Vector<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(Vector<Post> userPosts, Vector<Request> userRequests, Vector<Group> userGroups, Vector<Rating> ratings, Vector<Notification> notifications, Vector<Subscription> subscriptions, ArrayList<String> friends, double avgRating, String name, String profilePhoto, String id) {

        this.userPosts = userPosts;
        this.userRequests = userRequests;
        this.userGroups = userGroups;
        this.ratings = ratings;
        this.notifications = notifications;
        this.subscriptions = subscriptions;
        this.friends = friends;
        this.avgRating = avgRating;
        this.name = name;
        this.profilePhoto = profilePhoto;
        this.id = id;
    }
}
