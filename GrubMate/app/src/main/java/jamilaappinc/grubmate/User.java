package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ivanchen on 10/2/17.
 */

public class User implements Serializable {
    ArrayList<String> userPosts;
    ArrayList<String> userRequests;
    ArrayList<String> userGroups;
    ArrayList<String> ratings;
    ArrayList<String> notifications;
    ArrayList<String> subscriptions;
    ArrayList<String> friends;

    double avgRating;
    String name;
    String profilePhoto;
    String id;



    public User(){

    }
    public User(String mName, String mPic) {
        this.name = mName;
        this.profilePhoto = mPic;
    }

    public User(ArrayList<String> userPosts, ArrayList<String> userRequests, ArrayList<String> userGroups, ArrayList<String> ratings, ArrayList<String> notifications, ArrayList<String> subscriptions, ArrayList<String> friends, double avgRating, String name, String profilePhoto, String id) {
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

    public ArrayList<String> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(ArrayList<String> userPosts) {
        this.userPosts = userPosts;
    }

    public ArrayList<String> getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(ArrayList<String> userRequests) {
        this.userRequests = userRequests;
    }

    public ArrayList<String> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(ArrayList<String> userGroups) {
        this.userGroups = userGroups;
    }

    public ArrayList<String> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<String> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<String> subscriptions) {
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
}
