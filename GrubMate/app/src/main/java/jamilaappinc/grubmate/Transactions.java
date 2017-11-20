package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 11/19/17.
 */

public class Transactions implements Serializable {
    protected String mFromUser;
    protected String mAboutPost;
    protected String mToUser;
    protected String mId;
    protected String mAddress;
    protected String transactionID;

//    protected double destinationLongitude, originLongitude; //origin is the original post's information
//    protected double destinationLatitude, originLatitude;
    double mLatitude, mLongitude;
//    protected String destinationAddress, originAddress;

    public Transactions(){

    }

    /**
     *
     * @param mFromUser the id of the user creating the notification
     * @param mToUser the id of the user receiving the notification
     */


    public Transactions(String mFromUser, String mToUser, String mId, double mLatitude, double mLongitude, String mAddress, String transactionID) {

        this.mFromUser = mFromUser;
        this.mToUser = mToUser;
        this.mId = mId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mAddress = mAddress;
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getmFromUser() {
        return mFromUser;
    }

    public void setmFromUser(String mFromUser) {
        this.mFromUser = mFromUser;
    }

    public String getmToUser() {
        return mToUser;
    }

    public void setmToUser(String mToUser) {
        this.mToUser = mToUser;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmAddress() {
        return mAddress;
    }

    public double getmLatitude(){return mLatitude;}

    public void setmLatitude(double mLatitude){this.mLatitude = mLatitude;}

    public double getmLongitude(){return mLongitude;}

    public void setmLongitude(double mLongitude){this.mLongitude = mLongitude;}

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }


}
