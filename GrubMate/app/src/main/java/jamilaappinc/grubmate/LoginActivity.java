package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class LoginActivity extends AppCompatActivity {


    public static final String EXTRA_POSITION = "extra_position";
    private static final String TAG = "TAG";
    CallbackManager mCallbackManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.setApplicationId("140229246720277");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_login);

        //THIS IS TO TEST STUFF, IF YOU NEED TO WORK ON DIFFERENT SCREEN
//        startSplashTimer();
        ////////////////////////////////////////////////////////////////

        //get intent data
        Intent i = getIntent();

        //TODO modify for id
        int pos = i.getIntExtra(EXTRA_POSITION, -1);

        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess");
                AccessToken token = AccessToken.getCurrentAccessToken();
                final ArrayList<String> currUsers = new ArrayList<String>();

                //intent that links curr screen to mainactivity.
                final Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                System.out.println("BEFORE FIRST TASK");

                final GraphRequest graphRequestAsyncTask = new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");

                                    for(int i = 0; i < rawName.length(); i++){
                                        currUsers.add(Integer.toString(rawName.getJSONObject(i).getInt("id")));
                                    }
                                    intent.putExtra("Users",currUsers);//places users vector in intent and passes to main screen
                                  startActivity(intent);
                                  finish();
                                    Log.d(TAG,rawName.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );


                //gets the current user's ID & name and puts it into intent.
                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        intent.putExtra("Name",user.optString("name"));
                        intent.putExtra("ID",user.optString("id"));
                        writeNewUser(user.optString("id"),user.optString("name"),"fuckPic",currUsers);
                        graphRequestAsyncTask.executeAsync();

                    }
                }).executeAsync();



            }

            @Override
            public void onCancel() {
                Context context = getApplicationContext();
                CharSequence text = "Incorrect Login.  Pls try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Context context = getApplicationContext();
                CharSequence text = "Error with Login.  Pls try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }


    /////////////////////////////////////////////////////////////////////
    // THIS IS TEMPORARY, AND ALLOWS FOR TESTING FOR THE MAIN ACTIVITY //
    // TO USE THIS, CHANGE 2nd PARAM OF INTENT TO THE ACTIVITY YOU WANT //
    ////////////////////////////////////////////////////////////////////
    private void startSplashTimer() {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void writeNewUser(String userId, String name, String picture, ArrayList<String> friends) {
        DatabaseReference databaseRef = database.getReference().child("Users").child(userId);
        User u = new User(name,picture);
        u.setmId(userId);
        u.setFriends(friends);

        databaseRef.setValue(u);

    }

}
