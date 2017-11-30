package jamilaappinc.grubmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private final int STR_SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSplashTimer();
    }

    private void startSplashTimer() {
        try {
            Timer timer = new Timer();

            SharedPreferences sharedPrefs = getSharedPreferences("INTERNALSTORAGE", MODE_PRIVATE);
            if(sharedPrefs.getBoolean("LOGGEDIN", false))
            {
                Gson gson = new Gson();
                String json = sharedPrefs.getString("INTERNALFRIENDS", null);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> arrayList = gson.fromJson(json, type);
                String personalName = sharedPrefs.getString("INTERNALNAME",null);
                String personalID = sharedPrefs.getString("INTERNALID",null);
                String personalProfilePic = sharedPrefs.getString("INTERNALPROFILEPIC",null);

                final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                System.out.println("INTERNAL LOGGING BACK IN");
                intent.putExtra("Name", personalName);
                intent.putExtra("ID", personalID);
                intent.putExtra("MyProfilePicture", personalProfilePic);
                intent.putExtra("Users", arrayList);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, STR_SPLASH_TIME);
            }
            else
            {
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        System.out.println("INTERNAL SAVING NOT WORKING");
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, STR_SPLASH_TIME);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
