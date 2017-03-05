package com.example.perfecto.tipcalculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.perfecto.espresso.annotations.SearchWorkflowTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public ActivityTestRule<CalcTipActivity> mActivityRule = new ActivityTestRule<>(CalcTipActivity.class);

    @SearchWorkflowTest
    @Test
    public void searchForTips() {

        // click into search
        // ...

    }
}