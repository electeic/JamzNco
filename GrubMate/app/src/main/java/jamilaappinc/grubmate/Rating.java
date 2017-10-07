package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Rating {

    String mReview;
    double mRating;
    int mId;
    User mUser;

    public Rating(String review, double rating, User user) {
        this.mReview = review;
        this.mRating = rating;
        this.mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }


}
