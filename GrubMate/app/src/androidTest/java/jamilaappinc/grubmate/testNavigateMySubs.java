package jamilaappinc.grubmate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ivanchen on 10/30/17.
 */

@RunWith(AndroidJUnit4.class)
public class testNavigateMySubs {

    @Rule
    public ActivityTestRule<MenuActivity> mainFragmentActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void clickActivty_MainActivity()
    {

        onView(withId(R.id.subscriptions)).perform(click()).check(matches(isDisplayed()));

//        onView(withId(R.id.menu_from_main)).perform(click()).check(matches(is));
    }
}
