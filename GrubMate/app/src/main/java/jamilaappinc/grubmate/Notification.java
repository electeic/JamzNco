package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Notification {
   protected User mFromUser;
    protected Post mAboutPost;
    protected User mToUser;
    protected int mId;

    public Notification(){

    }

    public Notification(User mFromUser, Post mAboutPost, User mToUser) {
        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
        this.mToUser = mToUser;
    }

    public User getmFromUser() {
        return mFromUser;
    }

    public User getmToUser() {
        return mToUser;
    }

    public void setmFromUser(User mFromUser) {
        this.mFromUser = mFromUser;
    }

    public void setmToUser(User mToUser) { this.mToUser = mToUser; }

    public Post getmAboutPost() {
        return mAboutPost;
    }

    public void setmAboutPost(Post mAboutPost) {
        this.mAboutPost = mAboutPost;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
