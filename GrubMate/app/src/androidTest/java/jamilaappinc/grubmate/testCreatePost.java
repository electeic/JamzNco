package jamilaappinc.grubmate;

import android.support.test.espresso.action.TypeTextAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by ivanchen on 10/30/17.
 */

@RunWith(AndroidJUnit4.class)
public class testCreatePost {

    String ID;
    String title, location = "2", servings, tags, descriptions, startTimeString, endTimeString,startDateString, endDateString;
    Date startDateTime, endDateTime;
    String userProfilePic;
    String currUserName;

    @Rule
    public ActivityTestRule<PostActivity> mainFragmentActivityTestRule =
            new ActivityTestRule<>(PostActivity.class);

    @Before
    public void init()
    {
        ID = "12345";
        title = "1";
        location= "2";
        servings= "3";
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
    public void triggerIntentTest() {
        // check that the button is there

        onView(withId(R.id.post_submit)).check(matches(notNullValue() ));
        onView(withId(R.id.post_submit)).check(matches(withText("Submit")));
        onView(withId(R.id.post_titleText)).perform(typeText(title));
        closeSoftKeyboard();
        onView(withId(R.id.dietaryText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.post_locationText)).perform(typeText("abc"));
        closeSoftKeyboard();
        onView(withId(R.id.ServingsText)).perform(typeText(servings));
        closeSoftKeyboard();
        onView(withId(R.id.tagsText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.post_description)).perform(typeText(descriptions));
        closeSoftKeyboard();

        onView(withId(R.id.post_submit)).perform(click());
//        intended(toPackage("jamilaappinc.grubmate"));
//        intended(hasExtra("ID", ID));
    }

    @Test
    public void EnterInputTest() {
        // check that the button is there

        onView(withId(R.id.post_submit)).check(matches(notNullValue() ));
        onView(withId(R.id.post_submit)).check(matches(withText("Submit")));
        onView(withId(R.id.post_titleText)).perform(typeText(title));
        closeSoftKeyboard();
        onView(withId(R.id.dietaryText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.post_locationText)).perform(typeText("abc"));
        closeSoftKeyboard();
        onView(withId(R.id.ServingsText)).perform(typeText(servings));
        closeSoftKeyboard();
        onView(withId(R.id.tagsText)).perform(typeText(tags));
        closeSoftKeyboard();
        onView(withId(R.id.post_description)).perform(typeText(descriptions));
        closeSoftKeyboard();
    }

}
