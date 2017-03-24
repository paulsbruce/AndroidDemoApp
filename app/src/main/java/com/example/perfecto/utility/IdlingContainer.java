package com.example.perfecto.utility;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.base.Supplier;

import java.lang.reflect.ParameterizedType;

/**
 * Created by paulb on 11/28/16.
 */

public class IdlingContainer<TActivity> implements IdlingResource {

    private TActivity activity;
    private Supplier<Boolean> isIdleSup;
    private ResourceCallback callback;

    public IdlingContainer(TActivity activity, Supplier<Boolean> isIdle) {
        this.activity = activity;
        this.isIdleSup = isIdle;
    }

    @Override
    public String getName() {
        return ((Class<TActivity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).toString();
    }
    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && callback != null && this.isIdleSup.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}