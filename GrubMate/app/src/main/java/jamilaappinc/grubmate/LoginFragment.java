package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String ARGS_POSITION = "args_position";

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
        startSplashTimer();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

}
