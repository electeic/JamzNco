package jamilaappinc.grubmate;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.login.widget.LoginButton;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.SendButton;

/**
 * Created by ericajung on 10/13/17.
 */

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "main_position";
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    private Toolbar mToolbar;
    private View mMessengerButton;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    CallbackManager callbackManager;

// This is the request code that the SDK uses for startActivityForResult.<br />
// See the code below that references it. Messenger currently doesn't<br />
// return any data back to the calling application.<br />
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setTitle(R.string.app_name);

        // If we received Intent.ACTION_PICK from Messenger, we were launched from a composer shortcut
        // or the reply flow.
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

            // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
            // It will contain the metadata associated with the original content, if there was content.
        }

        //mMessengerButton = (View) findViewById(R.id.messenger_send_button);
        final SendButton sendButton = (SendButton)findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }
        });
    }

    private void onMessengerButtonClicked() {

        Uri uri = Uri.parse("android.resource://com.numetriclabz.messengerdemo/" + R.drawable.gmlogo);

        // Create the parameters for what we want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                        .setMetaData("{ \"image\" : \"logo\" }")
                        .build();

        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.
            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
        }
        else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    this,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }

        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

//        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null) {
            //TODO modify for id
            f = ProfileFragment.newInstance(pos);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();




    }
}
