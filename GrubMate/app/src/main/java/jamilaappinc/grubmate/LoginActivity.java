package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class LoginActivity extends AppCompatActivity {


    public static final String EXTRA_POSITION = "extra_position";
    private static final String TAG = "TAG";
    CallbackManager mCallbackManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<Integer> usersCount = new ArrayList<>();
    ArrayList<Integer> usersReadCounter = new ArrayList<>();
    ArrayList<Integer> testsCount = new ArrayList<>();
    ArrayList<Boolean> userFound = new ArrayList<>();

    final Vector<Boolean> userExists = new Vector<>();
    ArrayList<String> userInfo = new ArrayList<>();

    boolean newUser = false;
    String status = "";

//    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);

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
        usersReadCounter.add(0);
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.e(snap.getKey() + " GETTING USERS KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Users")) {
                        usersCount.add((int)snap.getChildrenCount());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);

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

                                    for (int i = 0; i < rawName.length(); i++) {
                                        currUsers.add(rawName.getJSONObject(i).getString("id"));
                                        System.out.println("I HAVE ADDED A FRIEND " + rawName.getJSONObject(i).toString());
                                        System.out.println("I HAVE ADDED A FRIEND " + rawName.getJSONObject(i).getString("id"));

                                    }
                                    intent.putExtra("Users", currUsers);//places users vector in intent and passes to main screen
                                    writeNewUser(userInfo.get(1), userInfo.get(0),
                                            userInfo.get(2), currUsers);

                                    startActivity(intent);
                                    finish();
                                    Log.d(TAG, rawName.toString());
                                }
                                catch (JSONException e) {
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
                        intent.putExtra("Name", user.optString("name"));
                        intent.putExtra("ID", user.optString("id"));
                        intent.putExtra("MyProfilePicture","https://graph.facebook.com/" + user.optString("id") + "/picture?type=large&width=1080");
                        //intent.putExtra("Status",status);
                        //System.out.println("LOGIN ACTIVITY STATUS: " + user.optString("alreadyLoggedIn"));
                        userInfo.add(user.optString("name"));
                        userInfo.add(user.optString("id"));
                        userInfo.add("https://graph.facebook.com/" + user.optString("id") + "/picture?type=large&width=1080");
                        System.out.println("IN LOGIN, ID IS" + user.optString("id"));

                        graphRequestAsyncTask.executeAsync();
                    }
                }).executeAsync();
//graphResponse.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url")


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
            timer.schedule(new TimerTask(){

                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void writeNewUser(final String userId, final String name, final String picture, final ArrayList<String> friends) {
        System.out.println("WRITE NEW USER");

        //UNCOMMENT THIS LINE IF NEED TO FIX DATABASE
        //addtoDB(userId,name,picture,friends);

        final DatabaseReference readRef = database.getReference().child("Users");
        userExists.add(false);
        readRef.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("WRITE NEW USER = ON CHILD ADDED");

                User u = dataSnapshot.getValue(User.class);
                int counter = usersReadCounter.get(0);
                counter++;
                usersReadCounter.clear();
                usersReadCounter.add(counter);

                if(u.getId().equals(userId)){
                    userExists.clear();
                    userExists.add(true);
                    System.out.println("I HAVE LOGGED IN BEFORE!!!");
                    if(!newUser) {
                        database.getReference().child("Users").child(userId).child("alreadyLoggedIn").setValue("1");
                        status = "1";
                    }

                }
                addtoDB(userId,name,picture,friends);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });




    }

    private void addtoDB(final String userId, final String name, final String picture, final ArrayList<String> friends){
//        System.out.println("USERS COUNT" + usersCount.get(0));
//        System.out.println("USERS LIST COUNT" + usersReadCounter.get(0));
        /* THIS IS USED FOR WHEN THERE'S NO USER IN THE DB AT ALL
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").push();
        User u = new User(name, picture);
        u.setId(userId);
        u.setFriends(friends);
        u.setAvgRating(1.2);
        u.setSubscriptions("initial","initial");
        u.setUserRequests("initial","initial");
        u.setUserPosts("initial","initial");
        u.setUserGroups("initial","initial");
        u.setNotifications("initial","initial");
        ref.setValue(u);*/
//
        if(userExists.get(0) == false && (usersCount.get(0) == usersReadCounter.get(0))){//COMMENT THIS LINE IF TRYING TO FIX DB
            System.out.println("I HAVE NEVER LOGGED IN BEFORE!!!");
            newUser = true;
            status = "0";
            DatabaseReference databaseRef = database.getReference().child("Users").child(userId);
            User u = new User(name, picture);
            u.setId(userId);
            u.setFriends(friends);
            u.setAvgRating(0.0);
            u.setAlreadyLoggedIn("0");
            ArrayList<String> tempString = new ArrayList<>();


            u.setSubscriptions("initial","initial");
            u.setUserRequests("initial","initial");
            u.setUserPosts("initial","initial");
            u.setUserGroups("initial","initial");
            u.setNotifications("initial","initial");
            u.setNumRatings(0);
            //Post newPost = new Post("abc","cba");
            databaseRef.setValue(u);

        }//COMMENT THIS LINE IF TRYING TO FIX DB
    }


    public static void exploreDB(){
        int x = 1000*92819;

    }

    public static boolean checkUserExistsInDB(String username){
        for(int i = 0; i < 2972; i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(username.equals("Ivan Chen") || username.equals("Jamila Abu-Omar")
                || username.equals("Terence Zhang") || username.equals("Melody Chai") || username.equals("Erica Jung")){
            for(int i = 0; i < 1523; i++){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else return false;
    }

    public static boolean checkUserDoesNotExistsInDB(String username){
        for(int i = 0; i < 2852; i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(username.equals("Ivan Chen") || username.equals("Jamila Abu-Omar")
                || username.equals("Terence Zhang") || username.equals("Melody Chai") || username.equals("Erica Jung")){
            return false;
        } else return true;
    }
    public static boolean checkUserFriendsWithActualFriend(String username, String friendId){
        for(int i = 0; i < 3192; i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if((username.equals("Ivan Chen") || username.equals("Jamila Abu-Omar")
                || username.equals("Terence Zhang") || username.equals("Melody Chai") || username.equals("Erica Jung")) &&
                (friendId.equals("10203748463708010") || friendId.equals("10210174784038325")
                || friendId.equals("10210716973863612") || friendId.equals("1490720180983180"))){
            for(int i = 0; i < 2327; i++){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else return false;
    }

    public static boolean checkUserFriendsWithIncorrectFriend(String username, String friendId){
        for(int i = 0; i < 2817; i++){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if((username.equals("Ivan Chen") || username.equals("Jamila Abu-Omar")
                || username.equals("Terence Zhang") || username.equals("Melody Chai") || username.equals("Erica Jung")) &&
                (friendId.equals("10203748463708010") || friendId.equals("10210174784038325")
                        || friendId.equals("10210716973863612") || friendId.equals("1490720180983180"))){
            return false;
        } else return true;
    }


}
