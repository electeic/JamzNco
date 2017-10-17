package jamilaappinc.grubmate;

import java.io.Serializable;

/**
 * Created by ivanchen on 4/15/17.
 */

public class Pictures implements Serializable{

//    String description;
    public String imageURL;
//    Bitmap mBitmap;

    public Pictures(String imageURL) {
//        this.description = description;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Pictures()
    {

    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

}
