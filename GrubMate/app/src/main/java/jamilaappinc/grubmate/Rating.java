package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Rating implements Serializable{

    String mReview;
    double mRating;
    String mId;
    String personRatingName;
    String rateeID; // the id of the person being rated
    String raterID; // id of the person rating the individual

    public Rating(){

    }

    public Rating(String review, double rating, String name, String rateeID, String raterID) {
        this.mReview = review;
        this.mRating = rating;
        personRatingName = name;
        this.raterID = raterID;
    }
    public void setRaterID(String id){
        raterID = id;
    }
    public String getRaterID(){
        return raterID;
    }
    public void setRateeID(String id){
        rateeID = id;
    }
    public String getRateeID(){
        return rateeID;
    }

    public String getPersonRatingName() {
        return personRatingName;
    }

    public void setUser(String user) {
        personRatingName = user;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        this.mReview = review;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }


}
