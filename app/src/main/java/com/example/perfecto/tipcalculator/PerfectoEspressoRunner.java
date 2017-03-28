package com.example.perfecto.tipcalculator;

import android.app.Activity;
import android.os.Build;
import android.support.test.runner.lifecycle.ActivityLifecycleCallback;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

/*import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
*/
/**
 * Created by paulb on 2/24/17.
 */

public class PerfectoEspressoRunner extends CustomRunner {

    private String _deviceId = null;
    //private ReportiumClient _reporting = null;

    public PerfectoEspressoRunner() {
        super();

        //_reporting = createDriver();

        super.addListener(new RunListener() {
            // use tag consistent with InstrumentationTestRunner
            private static final String TAG = "TestRunner";

            @Override
            public void testRunStarted(Description description) throws Exception {

                Log.i(TAG, String.format("["+_deviceId+"]run started: %d tests", description.testCount()));
            }

            @Override
            public void testRunFinished(Result result) throws Exception {
                Log.i(TAG, String.format("["+_deviceId+"]run finished: %d tests, %d failed, %d ignored",
                        result.getRunCount(), result.getFailureCount(), result.getIgnoreCount()));
            }

            @Override
            public void testStarted(Description description) throws Exception {
                Log.i(TAG, "["+_deviceId+"] started: " + description.getDisplayName());
            }

            @Override
            public void testFinished(Description description) throws Exception {
                Log.i(TAG, "["+_deviceId+"]finished: " + description.getDisplayName());
            }

            @Override
            public void testFailure(Failure failure) throws Exception {
                Log.i(TAG, "["+_deviceId+"]failed: " + failure.getDescription().getDisplayName());
                Log.i(TAG, "----- begin exception -----");
                Log.i(TAG, failure.getTrace());
                Log.i(TAG, "----- end exception -----");
            }

            @Override
            public void testAssumptionFailure(Failure failure) {
                Log.i(TAG, "["+_deviceId+"]assumption failed: " + failure.getDescription().getDisplayName());
                Log.i(TAG, "----- begin exception -----");
                Log.i(TAG, failure.getTrace());
                Log.i(TAG, "----- end exception -----");
            }

            @Override
            public void testIgnored(Description description) throws Exception {
                Log.i(TAG, "["+_deviceId+"]ignored: " + description.getDisplayName());
            }
        });

        // obtain device ID to be used in reporting
        _deviceId = Build.SERIAL;
        _print("Device ID: " + _deviceId);
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

