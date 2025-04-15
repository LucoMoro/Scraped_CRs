/*Add support for 'suiteAssignment' arg.

Change-Id:I9e5de0a77d3712e228861294a2236e4875cd8013*/
//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 90a3752..a932523 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;
import com.android.test.runner.listener.InstrumentationRunListener;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
//Synthetic comment -- @@ -121,8 +122,8 @@
*/
public class AndroidJUnitRunner extends Instrumentation {

public static final String ARGUMENT_TEST_CLASS = "class";

private static final String ARGUMENT_TEST_SIZE = "size";
private static final String ARGUMENT_LOG_ONLY = "log";
private static final String ARGUMENT_ANNOTATION = "annotation";
//Synthetic comment -- @@ -130,6 +131,9 @@
private static final String ARGUMENT_DELAY_MSEC = "delay_msec";
private static final String ARGUMENT_COVERAGE = "coverage";
private static final String ARGUMENT_COVERAGE_PATH = "coverageFile";

private static final String LOG_TAG = "AndroidJUnitRunner";

//Synthetic comment -- @@ -180,7 +184,7 @@
public void onStart() {
prepareLooper();

        if (getBooleanArgument("debug")) {
Debug.waitForDebugger();
}

//Synthetic comment -- @@ -190,11 +194,7 @@

try {
JUnitCore testRunner = new JUnitCore();

            addListener(listeners, testRunner, new TextListener(writer));
            addListener(listeners, testRunner, new InstrumentationResultPrinter(this));
            addDelayListener(listeners, testRunner);
            addCoverageListener(listeners, testRunner);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -220,6 +220,18 @@

}

private void addListener(List<RunListener> list, JUnitCore testRunner, RunListener listener) {
list.add(listener);
testRunner.addListener(listener);








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/TestRequestBuilder.java b/androidtestlib/src/com/android/test/runner/TestRequestBuilder.java
//Synthetic comment -- index 250a9fc..a15b70e 100644

//Synthetic comment -- @@ -48,6 +48,10 @@

private static final String LOG_TAG = "TestRequestBuilder";

private String[] mApkPaths;
private TestLoader mTestLoader;
private Filter mFilter = new AnnotationExclusionFilter(Suppress.class);
//Synthetic comment -- @@ -154,11 +158,11 @@
* @param testSize
*/
public void addTestSizeFilter(String testSize) {
        if ("small".equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(SmallTest.class));
        } else if ("medium".equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(MediumTest.class));
        } else if ("large".equals(testSize)) {
mFilter = mFilter.intersect(new AnnotationInclusionFilter(LargeTest.class));
} else {
Log.e(LOG_TAG, String.format("Unrecognized test size '%s'", testSize));








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/SuiteAssignmentPrinter.java b/androidtestlib/src/com/android/test/runner/listener/SuiteAssignmentPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..ac4aa2c

//Synthetic comment -- @@ -0,0 +1,101 @@








//Synthetic comment -- diff --git a/androidtestlib/tests/src/com/android/test/MyAndroidTestCase.java b/androidtestlib/tests/src/com/android/test/MyAndroidTestCase.java
//Synthetic comment -- index 1e328ac..f48337e 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import android.content.Context;
import android.test.AndroidTestCase;

/**
* Placeholder test to verify {@link Context} gets injected to {@link AndroidTestCase}.
//Synthetic comment -- @@ -24,4 +25,8 @@
public class MyAndroidTestCase extends AndroidTestCase {

// rely on testCaseSetupProperly to test for context
}







