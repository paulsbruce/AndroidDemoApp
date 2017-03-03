package com.example.perfecto.tipcalculator;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.test.runner.lifecycle.ActivityLifecycleCallback;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import java.util.Map;
import java.util.UUID;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

/**
 * Created by paulb on 2/24/17.
 */

public class CustomRunner extends android.support.test.runner.AndroidJUnitRunner {

    private String _deviceId = null;

    @Override
    public void onCreate(Bundle arguments) {

        // inject or override args passed in here...

        super.onCreate(arguments);

        // obtain device ID to be used in reporting
        _deviceId = Build.SERIAL;
        _print("Device ID: " + _deviceId);

       _wakeLock();
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        return super.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        super.finish(resultCode, results);

        for(String key : results.keySet()) {
            Object o = results.get(key);
            o = o;
        }
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        return super.onException(obj, e);
    }

    private void _wakeLock() {
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback(new ActivityLifecycleCallback() {
            @Override public void onActivityLifecycleChanged(Activity activity, Stage stage) {
                if (stage == Stage.PRE_ON_CREATE) {
                    activity.getWindow().addFlags(FLAG_DISMISS_KEYGUARD | FLAG_TURN_SCREEN_ON | FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    private void _print(Object o) {
        Log.v(CustomRunner.class.getCanonicalName(), (o == null ? "[null]" : o.toString()));
    }
}

