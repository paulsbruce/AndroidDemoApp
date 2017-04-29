package com.example.perfecto.tipcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.SdkSuppress;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.perfecto.annotations.KeySmokeTest;
import com.example.perfecto.tipcalculator.api.service.TipsServiceManager;
import com.example.perfecto.tipcalculator.util.RecyclerViewItemCountAssertion;
//import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.example.perfecto.tipcalculator.util.AssetReaderUtil.asset;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(AndroidJUnit4.class)
public class ListViewTest {

    @Rule
    public ActivityTestRule<FlexViewActivity> mActivityRule = new ActivityTestRule<FlexViewActivity>(FlexViewActivity.class);

    @Test
    @KeySmokeTest
    public void clickAndVerify() {

        onView(withId(R.id.tipcalc_list))
                .check(new RecyclerViewItemCountAssertion(greaterThan(0)));

        //onView(withId(R.id.tipcalc_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

    }

    /*
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(BuildConfig.MOCK_PORT);

    @Before
    public void setup() {

        FlexViewActivity activity = (FlexViewActivity) mActivityRule.getActivity();

        String jsonBody = asset(activity, "tips.json");
        stubFor(get(urlMatching("/tips"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(jsonBody)));

        activity.setTipsServiceManager(new TipsServiceManager(
                "http://"+BuildConfig.MOCK_IP+":" + BuildConfig.MOCK_PORT
        ));
    }
    */

}