package io.paulsbruce.dev.android.espresso;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.perfecto.tipcalculator.CalcTipActivity;
import com.example.perfecto.tipcalculator.R;

import io.perfecto.espresso.annotations.ActiveDebugTest;
import io.perfecto.espresso.annotations.KeySmokeTest;
import io.perfecto.espresso.utility.MoreActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class SeekBarExampleTest {

    @Rule
    public ActivityTestRule<CalcTipActivity> mActivityRule = new ActivityTestRule<>(CalcTipActivity.class);

    @ActiveDebugTest
    @Test
    public void treatControlsLikeUnits() {

        onView(ViewMatchers.withId(R.id.bill_value))
                .perform(click(), replaceText("28.73"));

        onView(withId(R.id.tip_percent_seekBar))
                .perform(MoreActions.setProgress(20));

        onView(withId(R.id.tip_percent)).check(matches(withText("Tip Percent - 20")));

        // fails because... SeekBar.OnSeekBarChangeListener is not fired by using SeekBar.setProgress
    }

    @ActiveDebugTest
    @Test
    public void treatControlsLikeUnitsWithSleeps() throws java.lang.InterruptedException {
        onView(withId(R.id.bill_value))
                .perform(click(), replaceText("28.73"));

        onView(withId(R.id.tip_percent_seekBar))
                .perform(MoreActions.setProgress(20));

        Thread.sleep(5000);

        onView(withId(R.id.tip_percent)).check(matches(withText("Tip Percent - 20")));
    }

    @ActiveDebugTest
    @KeySmokeTest
    @Test
    public void treatControlsLikeTheEventBasedAnimalsTheyAre() {
        onView(withId(R.id.bill_value))
                .perform(click(), replaceText("28.73"));

        onView(withId(R.id.tip_percent_seekBar))
                .perform(MoreActions.clickSeekBar(20)); // <== this

        // uses the swipe

        onView(withId(R.id.tip_percent)).check(matches(withText("Tip Percent - 20")));

        // succeeds!
    }
}