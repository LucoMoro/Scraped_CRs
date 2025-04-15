/*Add support for 'suiteAssignment' arg.

Change-Id:I9e5de0a77d3712e228861294a2236e4875cd8013*/




//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 90a3752..a932523 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;
import com.android.test.runner.listener.InstrumentationRunListener;
import com.android.test.runner.listener.SuiteAssignmentPrinter;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
//Synthetic comment -- @@ -121,8 +122,8 @@
*/
public class AndroidJUnitRunner extends Instrumentation {

    // constants for supported instrumentation arguments
public static final String ARGUMENT_TEST_CLASS = "class";
private static final String ARGUMENT_TEST_SIZE = "size";
private static final String ARGUMENT_LOG_ONLY = "log";
private static final String ARGUMENT_ANNOTATION = "annotation";
//Synthetic comment -- @@ -130,6 +131,9 @@
private static final String ARGUMENT_DELAY_MSEC = "delay_msec";
private static final String ARGUMENT_COVERAGE = "coverage";
private static final String ARGUMENT_COVERAGE_PATH = "coverageFile";
    private static final String ARGUMENT_SUITE_ASSIGNMENT = "suiteAssignment";
    private static final String ARGUMENT_DEBUG = "debug";
    // TODO: consider supporting 'count' from InstrumentationTestRunner

private static final String LOG_TAG = "AndroidJUnitRunner";

//Synthetic comment -- @@ -180,7 +184,7 @@
public void onStart() {
prepareLooper();

        if (getBooleanArgument(ARGUMENT_DEBUG)) {
Debug.waitForDebugger();
}

//Synthetic comment -- @@ -190,11 +194,7 @@

try {
JUnitCore testRunner = new JUnitCore();
            addListeners(listeners, testRunner, writer);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -220,6 +220,18 @@

}

    private void addListeners(List<RunListener> listeners, JUnitCore testRunner,
            PrintStream writer) {
        if (getBooleanArgument(ARGUMENT_SUITE_ASSIGNMENT)) {
            addListener(listeners, testRunner, new SuiteAssignmentPrinter(writer));
        } else {
            addListener(listeners, testRunner, new TextListener(writer));
            addListener(listeners, testRunner, new InstrumentationResultPrinter(this));
            addDelayListener(listeners, testRunner);
            addCoverageListener(listeners, testRunner);
        }
    }

private void addListener(List<RunListener> list, JUnitCore testRunner, RunListener listener) {
list.add(listener);
testRunner.addListener(listener);








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/TestRequestBuilder.java b/androidtestlib/src/com/android/test/runner/TestRequestBuilder.java
//Synthetic comment -- index 250a9fc..a15b70e 100644

//Synthetic comment -- @@ -48,6 +48,10 @@

private static final String LOG_TAG = "TestRequestBuilder";

    public static final String LARGE_SIZE = "large";
    public static final String MEDIUM_SIZE = "medium";
    public static final String SMALL_SIZE = "small";

private String[] mApkPaths;
private TestLoader mTestLoader;
private Filter mFilter = new AnnotationExclusionFilter(Suppress.class);
//Synthetic comment -- @@ -154,11 +158,11 @@
* @param testSize
*/
public void addTestSizeFilter(String testSize) {
        if (SMALL_SIZE.equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(SmallTest.class));
        } else if (MEDIUM_SIZE.equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(MediumTest.class));
        } else if (LARGE_SIZE.equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(LargeTest.class));
} else {
Log.e(LOG_TAG, String.format("Unrecognized test size '%s'", testSize));








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/SuiteAssignmentPrinter.java b/androidtestlib/src/com/android/test/runner/listener/SuiteAssignmentPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..ac4aa2c

//Synthetic comment -- @@ -0,0 +1,101 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.test.runner.listener;

import com.android.test.runner.TestRequestBuilder;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.PrintStream;

/**
 * This class measures the elapsed run time of each test, and used it to report back to the user
 * which suite ({@link SmallSuite}, {@link MediumSuite}, {@link LargeSuite}) the test should belong
 * to.
 */
public class SuiteAssignmentPrinter extends RunListener {
    /**
     * This constant defines the maximum allowed runtime (in ms) for a test included in the "small"
     * suite. It is used to make an educated guess at what suite an unlabeled test belongs to.
     */
    private static final float SMALL_SUITE_MAX_RUNTIME = 100;

    /**
     * This constant defines the maximum allowed runtime (in ms) for a test included in the "medium"
     * suite. It is used to make an educated guess at what suite an unlabeled test belongs to.
     */
    private static final float MEDIUM_SUITE_MAX_RUNTIME = 1000;

    private final PrintStream mWriter;

    public SuiteAssignmentPrinter(PrintStream writer) {
        mWriter = writer;
    }

    private long mStartTime;
    private boolean mTimingValid;

    @Override
    public void testStarted(Description description) throws Exception {
        mTimingValid = true;
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void testFinished(Description description) throws Exception {
        long runTime;
        String assignmentSuite;
        long endTime = System.currentTimeMillis();

        if (!mTimingValid || mStartTime < 0) {
            assignmentSuite = "NA";
            runTime = -1;
        } else {
            runTime = endTime - mStartTime;
            if (runTime < SMALL_SUITE_MAX_RUNTIME) {
                assignmentSuite = TestRequestBuilder.SMALL_SIZE;
            } else if (runTime < MEDIUM_SUITE_MAX_RUNTIME) {
                assignmentSuite = TestRequestBuilder.MEDIUM_SIZE;
            } else {
                assignmentSuite = TestRequestBuilder.LARGE_SIZE;
            }
        }
        // Clear mStartTime so that we can verify that it gets set next time.
        mStartTime = -1;

        mWriter.printf("%s#%s\n" + "in %s suite\n" + "runTime: %d ms\n",
                        description.getClassName(), description.getMethodName(), assignmentSuite,
                        runTime);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        mTimingValid = false;
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        mTimingValid = false;
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        mTimingValid = false;
    }
}








//Synthetic comment -- diff --git a/androidtestlib/tests/src/com/android/test/MyAndroidTestCase.java b/androidtestlib/tests/src/com/android/test/MyAndroidTestCase.java
//Synthetic comment -- index 1e328ac..f48337e 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

/**
* Placeholder test to verify {@link Context} gets injected to {@link AndroidTestCase}.
//Synthetic comment -- @@ -24,4 +25,8 @@
public class MyAndroidTestCase extends AndroidTestCase {

// rely on testCaseSetupProperly to test for context

    public MyAndroidTestCase() {
        Log.i("MyAndroidTestCase", "I'm created");
    }
}







