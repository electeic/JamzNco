package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Notification implements Serializable{
   protected String mFromUser;
    protected String mAboutPost;
    protected String mToUser;
    protected String mId;

    public Notification(){

    }

    public Notification(String mFromUser, String mAboutPost, String mToUser) {
        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
        this.mToUser = mToUser;
    }

    /*public User getmFromUser() {
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
    }*/

    public String getmFromUser() {
        return mFromUser;
    }

    public void setmFromUser(String mFromUser) {
        this.mFromUser = mFromUser;
    }

    public String getmAboutPost() {
        return mAboutPost;
    }

    public void setmAboutPost(String mAboutPost) {
        this.mAboutPost = mAboutPost;
    }

    public String getmToUser() {
        return mToUser;
    }

    public void setmToUser(String mToUser) {
        this.mToUser = mToUser;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Notification(String mFromUser, String mAboutPost, String mToUser, String mId) {

        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
        this.mToUser = mToUser;
        this.mId = mId;
    }
}
