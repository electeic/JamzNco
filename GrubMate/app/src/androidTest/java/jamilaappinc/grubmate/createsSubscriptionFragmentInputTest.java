package jamilaappinc.grubmate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by ivanchen on 10/30/17.
 */

@RunWith(AndroidJUnit4.class)
public class createsSubscriptionFragmentInputTest {

    String ID;
    String title, location = "2", servings, tags, descriptions, startTimeString, endTimeString,startDateString, endDateString;
    Date startDateTime, endDateTime;
    String userProfilePic;
    String currUserName;

    @Rule
    public ActivityTestRule<createsSubscriptionActivity> mainFragmentActivityTestRule =
            new ActivityTestRule<>(createsSubscriptionActivity.class);

    @Before
    public void init()
    {
        ID = "12345";
        title = "1";
        tags= "none,";
        descriptions= "none";
        startTimeString= "1";
        endTimeString= "2";
        startDateString= "4";
        endDateString= "5";
        startDateTime = null;
        endDateTime = null;
        userProfilePic="idk";
        currUserName="idk";
    }

    @Test
    public void clickActivty_MainActivity()
    {

        onView(withId(R.id.menu_from_main)).perform(click()).check(matches(isDisplayed()));
//        onView(withId(R.id.menu_from_main)).perform(click()).check(matches(is));
        onView(withId(R.id.post_submit)).check(matches(notNullValue() ));
        onView(withId(R.id.post_submit)).check(matches(withText("Submit")));
        onView(withId(R.id.subscription_titleText)).perform(typeText(title));
        closeSoftKeyboard();
        onView(withId(R.id.dietaryText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.tagsText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.subscription_description)).perform(typeText(descriptions));
        closeSoftKeyboard();

    }
}
