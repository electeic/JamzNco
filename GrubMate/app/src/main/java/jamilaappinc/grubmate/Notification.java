package jamilaappinc.grubmate;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Notification {
    User mFromUser;
    Post mAboutPost;
    int mId;

    public Notification(User mFromUser, Post mAboutPost) {
        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
    }

    public User getmFromUser() {
        return mFromUser;
    }

    public void setmFromUser(User mFromUser) {
        this.mFromUser = mFromUser;
    }

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
