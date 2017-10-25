package jamilaappinc.grubmate;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ivanchen on 10/2/17.
 */

public class Notification implements Serializable{
   protected String mFromUser;
    protected String mAboutPost;
    protected String mToUser;
    protected String mId;
    protected String mType;
    protected String subscriptionMatchingPostsTitles;

    public Notification(){

    }

    public Notification(String mFromUser, String mAboutPost, String mToUser) {
        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
        this.mToUser = mToUser;
    }

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

    public void setmType(String mType){this.mType = mType;}

    public String getmType(){return mType;}

    public void setSubscriptionMatchingPostTitle(String titles){subscriptionMatchingPostsTitles = titles;}

    public String getTitle(){return subscriptionMatchingPostsTitles;}

    public Notification(String mFromUser, String mAboutPost, String mToUser, String mId, String mType) {

        this.mFromUser = mFromUser;
        this.mAboutPost = mAboutPost;
        this.mToUser = mToUser;
        this.mId = mId;
        this.mType = mType;
    }
}
