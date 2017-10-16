package jamilaappinc.grubmate;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchen on 4/15/17.
 */

public class PictureSingleton {

    private List<Pictures> mPictures;

    private static PictureSingleton singletonPic;
    private Context mAppContext;

    //TODO do not instantiate list or load data
    private PictureSingleton(Context appContext) {
        mAppContext = appContext;
        mPictures = new ArrayList<Pictures>();
    }

    public static PictureSingleton get(Context c) {
        if (singletonPic == null) {
            singletonPic = new PictureSingleton(c.getApplicationContext());
        }
        return singletonPic;
    }

    //TODO Read all items --change to return List
    public List<Pictures> getMovies() {
        return mPictures;
    }

    //TODO Read
    public Pictures getMovie(int id) {
        return mPictures.get(id);
    }

    //TODO Create
    public void addMovie(Pictures mv) {
        mPictures.add(mv);
    }
}
