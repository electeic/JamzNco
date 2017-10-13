package jamilaappinc.grubmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailedPostActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "firebase.EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_post);
    }
}
