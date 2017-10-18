package jamilaappinc.grubmate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MyPostsActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "main_position";
    public static final String GET_POSTS = "all posts";

    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();

        int pos = i.getIntExtra(EXTRA_POSITION, -1);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        posts = (ArrayList<Post>)getIntent().getSerializableExtra(GET_POSTS);

        if (f == null ) {
            //TODO modify for id
            f = MyPostsFragment.newInstance(posts);

        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();


    }
}
