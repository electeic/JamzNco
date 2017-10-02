package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Rating {

    String review;
    double rating;
    int id;
    User user;

    public Rating(String review, double rating, User user) {
        this.review = review;
        this.rating = rating;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
