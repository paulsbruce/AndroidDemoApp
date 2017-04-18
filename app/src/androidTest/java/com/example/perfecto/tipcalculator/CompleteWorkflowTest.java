package com.example.perfecto.tipcalculator;

import android.support.test.filters.SdkSuppress;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.perfecto.annotations.KeySmokeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class CompleteWorkflowTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class); //

    @KeySmokeTest
    @SdkSuppress(minSdkVersion=17)
    @SmallTest
    @Test
    public void clickIntoCalcAndRunBasicTest() {

        // set input values
        onView(withId(R.id.btn_home_calc))
                .perform(click());

        BasicTest test = new BasicTest();
        test.enterStaticData();
        test = null;
    }
}