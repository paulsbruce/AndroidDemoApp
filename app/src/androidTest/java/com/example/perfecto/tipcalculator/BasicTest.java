package com.example.perfecto.tipcalculator;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.RequiresDevice;
import android.support.test.filters.SdkSuppress;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import io.perfecto.espresso.annotations.KeySmokeTest;

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
public class BasicTest {

    @Rule
    public ActivityTestRule<CalcTipActivity> mActivityRule = new ActivityTestRule<>(CalcTipActivity.class); //

    @KeySmokeTest
    @RequiresDevice
    @SdkSuppress(minSdkVersion=24)
    @SmallTest
    @Test
    public void enterStaticData() {

        // set input values

        onView(withId(R.id.bill_value))
                .perform(replaceText("28.73"));

        onView(withId(R.id.tip_percent_input))
                .perform(typeText("25"));

        onView(withId(R.id.split_number_input))
                .perform(typeText("2"));

        // perform the calculation

        onView(withId(R.id.calculate_tips))
                .perform(closeSoftKeyboard(),click());


        // verify individual calculations

        onView(withId(R.id.total_to_pay_result))
                .check(matches(withText("35.91"))); // 28.73 + (28.73 * .25 == 7.18) // base 10, human

        onView(withId(R.id.total_tip_result))
                .check(matches(withText("7.18")));

        onView(withId(R.id.tip_per_person_result))
                .check(matches(withText("3.59")));

        // validate final tip value

        onView(withId(R.id.tip_percent)).check(matches(withText("Tip Percent - 25")));

    }
}