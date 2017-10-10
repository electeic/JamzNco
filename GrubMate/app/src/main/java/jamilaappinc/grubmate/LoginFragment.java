package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String ARGS_POSITION = "args_position";
    private static final String TAG = "TAG";
    CallbackManager mCallbackManager;


    public LoginFragment() {
        // Required empty public constructor
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
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CallbackManager getCallbackManager()
    {
        return mCallbackManager;
    }

    /////////////////////////////////////////////////////////////////////

    //TODO modify for id
    public static LoginFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, pos);
        LoginFragment f = new LoginFragment();
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        startSplashTimer();
        FacebookSdk.setApplicationId("140229246720277");
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        return v;
    }


}
