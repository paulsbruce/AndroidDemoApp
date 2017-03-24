package com.example.perfecto.tipcalculator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.VisibleForTesting;
import android.support.test.internal.runner.RunnerArgs;
import android.support.test.internal.runner.TestExecutor;
import android.support.test.internal.runner.TestRequest;
import android.support.test.internal.runner.TestRequestBuilder;
import android.support.test.internal.runner.listener.ActivityFinisherRunListener;
import android.support.test.internal.runner.listener.CoverageListener;
import android.support.test.internal.runner.listener.DelayInjector;
import android.support.test.internal.runner.listener.InstrumentationResultPrinter;
import android.support.test.internal.runner.listener.LogRunListener;
import android.support.test.internal.runner.listener.SuiteAssignmentPrinter;
import android.support.test.internal.runner.tracker.AnalyticsBasedUsageTracker;
import android.support.test.runner.MonitoringInstrumentation;
import android.support.test.runner.UsageTrackerFacilitator;
import android.support.test.runner.lifecycle.ApplicationLifecycleCallback;
import android.support.test.runner.lifecycle.ApplicationLifecycleMonitorRegistry;
import android.util.Log;

import org.junit.runner.notification.RunListener;

import java.util.ArrayList;

/**
 * Created by paulb on 2/24/17.
 */

public class CustomRunner extends android.support.test.runner.MonitoringInstrumentation {

    private ArrayList<RunListener> _listeners = null;

    public CustomRunner() {
        super();
        _listeners = new ArrayList<RunListener>();
    }

    public void addListener(RunListener listener) {
        _listeners.add(listener);
    }





    /* PSB Copied from AndroidJUnitRunner */


    private static final String LOG_TAG = "AndroidJUnitRunner";

    private Bundle mArguments;
    private InstrumentationResultPrinter mInstrumentationResultPrinter = null;
    private RunnerArgs mRunnerArgs;
    private UsageTrackerFacilitator mUsageTrackerFacilitator;

    @Override
    public void onCreate(Bundle arguments) {
        mArguments = arguments;
        parseRunnerArgs(mArguments);

        if (mRunnerArgs.debug) {
            Log.i(LOG_TAG, "Waiting for debugger to connect...");
            Debug.waitForDebugger();
            Log.i(LOG_TAG, "Debugger connected.");
        }

        mUsageTrackerFacilitator = new UsageTrackerFacilitator(mRunnerArgs);

        super.onCreate(arguments);

        for (ApplicationLifecycleCallback listener : mRunnerArgs.appListeners) {
            ApplicationLifecycleMonitorRegistry.getInstance().addLifecycleCallback(listener);
        }

        start();
    }

    /**
     * Build the arguments. Read from manifest first so manifest-provided args can be overridden
     * with command line arguments
     * @param arguments
     */
    private void parseRunnerArgs(Bundle arguments) {
        mRunnerArgs = new RunnerArgs.Builder()
                .fromManifest(this)
                .fromBundle(arguments)
                .build();
    }

    /**
     * Get the Bundle object that contains the arguments passed to the instrumentation
     *
     * @return the Bundle object
     */
    private Bundle getArguments(){
        return mArguments;
    }

    @VisibleForTesting
    InstrumentationResultPrinter getInstrumentationResultPrinter() {
        return mInstrumentationResultPrinter;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mRunnerArgs.idle) {
            // TODO this is just proof of concept. A more detailed implementation will follow
            Log.i(LOG_TAG, "Runner is idle...");
            return;
        }

        Bundle results = new Bundle();
        try {
            TestExecutor.Builder executorBuilder = new TestExecutor.Builder(this);

            addListeners(mRunnerArgs, executorBuilder);

            TestRequest testRequest = buildRequest(mRunnerArgs, getArguments());

            results = executorBuilder.build().execute(testRequest);

        } catch (RuntimeException e) {
            final String msg = "Fatal exception when running tests";
            Log.e(LOG_TAG, msg, e);
            // report the exception to instrumentation out
            results.putString(Instrumentation.REPORT_KEY_STREAMRESULT,
                    msg + "\n" + Log.getStackTraceString(e));
        }
        finish(Activity.RESULT_OK, results);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        try {
            mUsageTrackerFacilitator.trackUsage("AndroidJUnitRunner");
            mUsageTrackerFacilitator.sendUsages();
        } catch (RuntimeException re) {
            Log.w(LOG_TAG, "Failed to send analytics.", re);
        }
        super.finish(resultCode, results);
    }

    private void addListeners(RunnerArgs args, TestExecutor.Builder builder) {
        if (args.suiteAssignment) {
            builder.addRunListener(new SuiteAssignmentPrinter());
        } else {
            builder.addRunListener(new LogRunListener());
            mInstrumentationResultPrinter = new InstrumentationResultPrinter();
            builder.addRunListener(mInstrumentationResultPrinter);
            builder.addRunListener(new ActivityFinisherRunListener(this,
                    new MonitoringInstrumentation.ActivityFinisher()));
            addDelayListener(args, builder);
            addCoverageListener(args, builder);

            for(RunListener lis : _listeners)
                builder.addRunListener(lis);
        }

        addListenersFromArg(args, builder);
    }

    private void addCoverageListener(RunnerArgs args, TestExecutor.Builder builder) {
        if (args.codeCoverage) {
            builder.addRunListener(new CoverageListener(args.codeCoveragePath));
        }
    }

    /**
     * Sets up listener to inject a delay between each test, if specified.
     */
    private void addDelayListener(RunnerArgs args, TestExecutor.Builder builder) {
        if (args.delayInMillis > 0) {
            builder.addRunListener(new DelayInjector(args.delayInMillis));
        } else if (args.logOnly && Build.VERSION.SDK_INT < 16) {
            // On older platforms, collecting tests can fail for large volume of tests.
            // Insert a small delay between each test to prevent this
            builder.addRunListener(new DelayInjector(15 /* msec */));
        }
    }

    private void addListenersFromArg(RunnerArgs args, TestExecutor.Builder builder) {
        for (RunListener listener : args.listeners) {
            builder.addRunListener(listener);
        }
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        InstrumentationResultPrinter instResultPrinter = getInstrumentationResultPrinter();
        if (instResultPrinter != null) {
            // report better error message back to Instrumentation results.
            instResultPrinter.reportProcessCrash(e);
        }
        return super.onException(obj, e);
    }

    /**
     * Builds a {@link TestRequest} based on given input arguments.
     * <p/>
     */
    @VisibleForTesting
    TestRequest buildRequest(RunnerArgs runnerArgs, Bundle bundleArgs) {

        TestRequestBuilder builder = createTestRequestBuilder(this, bundleArgs);

        // only scan for tests for current apk aka testContext
        // Note that this represents a change from InstrumentationTestRunner where
        // getTargetContext().getPackageCodePath() aka app under test was also scanned
        builder.addApkToScan(getContext().getPackageCodePath());

        builder.addFromRunnerArgs(runnerArgs);

        registerUserTracker();

        return builder.build();
    }

    private void registerUserTracker() {
        Context targetContext = getTargetContext();
        if (targetContext != null) {
            mUsageTrackerFacilitator.registerUsageTracker(new AnalyticsBasedUsageTracker.Builder(
                    targetContext).buildIfPossible());
        }
    }

    /**
     * Factory method for {@link TestRequestBuilder}.
     */
    @VisibleForTesting
    TestRequestBuilder createTestRequestBuilder(Instrumentation instr, Bundle arguments) {
        return new TestRequestBuilder(instr, arguments);
    }
}

