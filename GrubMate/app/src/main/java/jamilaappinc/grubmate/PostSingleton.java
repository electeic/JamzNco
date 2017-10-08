package jamilaappinc.grubmate;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchen on 10/7/17.
 */

public class PostSingleton {
    private List<Post> mPosts;

    private static PostSingleton singletonPosts;
    private Context mAppContext;

    //TODO do not instantiate list or load data
    private PostSingleton(Context appContext) {
        mAppContext = appContext;
        mPosts = new ArrayList<Post>();
    }

    public static PostSingleton get(Context c) {
        if (singletonPosts == null) {
            singletonPosts = new PostSingleton(c.getApplicationContext());
        }
        return singletonPosts;
    }

    //TODO Read all items --change to return List
    public List<Post> getMovies() {
        return mPosts;
    }

    //TODO Read
    public Post getMovie(int id) {
        return mPosts.get(id);
    }

    //TODO Create
    public void addMovie(Post mv) {
        mPosts.add(mv);
    }
}
