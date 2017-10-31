package jamilaappinc.grubmate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import android.support.test.espresso.Espresso.*;

import static org.junit.Assert.*;

/**
 * Created by ericajung on 10/30/17.
 */

@RunWith(AndroidJUnit4.class)
public class CreateGroupUpdate {

    @Rule
    public ActivityTestRule<CreateGroupActivity> createGroupActivityActivityTestRule =
            new ActivityTestRule<>(CreateGroupActivity.class);


    @Before
    public void init() {
        createGroupActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void CreateNewGroupUpdate() throws Exception {

    }
}
