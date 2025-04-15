/*junit: Rework support for parallel junit launch.

Change-Id:Iffedcb38cdf21e349fbe38ecf9a654060b469b04*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 2a65699..71f329a 100644

//Synthetic comment -- @@ -146,9 +146,6 @@
}
}

/** the name to provide to {@link ITestRunListener#testRunStarted(String, int)} */
private final String mTestRunName;

//Synthetic comment -- @@ -218,24 +215,10 @@
* @param runName the test run name to provide to
*            {@link ITestRunListener#testRunStarted(String, int)}
* @param listeners informed of test results as the tests are executing
*/
public InstrumentationResultParser(String runName, Collection<ITestRunListener> listeners) {
        mTestRunName = runName;
        mTestListeners = new ArrayList<ITestRunListener>(listeners);
}

/**
//Synthetic comment -- @@ -246,7 +229,7 @@
* @param listener informed of test results as the tests are executing
*/
public InstrumentationResultParser(String runName, ITestRunListener listener) {
        this(runName, Collections.singletonList(listener));
}

/**
//Synthetic comment -- @@ -459,8 +442,7 @@
return;
}
reportTestRunStarted(testInfo);
        TestIdentifier testId = new TestIdentifier(testInfo.mTestClass, testInfo.mTestName);
Map<String, String> metrics;

switch (testInfo.mCode) {
//Synthetic comment -- @@ -570,7 +552,7 @@
// received test start msg, but not test complete
// assume test caused this, report as test failure
TestIdentifier testId = new TestIdentifier(mLastTestResult.mTestClass,
                    mLastTestResult.mTestName);
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
String.format("%1$s. Reason: '%2$s'. %3$s", INCOMPLETE_TEST_ERR_MSG_PREFIX,








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 65ed3c0..124df7d 100644

//Synthetic comment -- @@ -256,7 +256,7 @@
Log.i(LOG_TAG, String.format("Running %1$s on %2$s", runCaseCommandStr,
mRemoteDevice.getSerialNumber()));
String runName = mRunName == null ? mPackageName : mRunName;
        mParser = new InstrumentationResultParser(runName, listeners);

try {
mRemoteDevice.executeShellCommand(runCaseCommandStr, mParser, mMaxTimeToOutputResponse);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/TestIdentifier.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/TestIdentifier.java
//Synthetic comment -- index 00295e7..7de5736 100644

//Synthetic comment -- @@ -23,24 +23,6 @@

private final String mClassName;
private final String mTestName;

/**
* Creates a test identifier.
//Synthetic comment -- @@ -49,7 +31,12 @@
* @param testName name of the test. Cannot be null.
*/
public TestIdentifier(String className, String testName) {
        if (className == null || testName == null) {
            throw new IllegalArgumentException("className and testName must " +
                    "be non-null");
        }
        mClassName = className;
        mTestName = testName;
}

/**
//Synthetic comment -- @@ -66,19 +53,11 @@
return mTestName;
}

@Override
public int hashCode() {
final int prime = 31;
int result = 1;
result = prime * result + ((mClassName == null) ? 0 : mClassName.hashCode());
result = prime * result + ((mTestName == null) ? 0 : mTestName.hashCode());
return result;
}
//Synthetic comment -- @@ -97,11 +76,6 @@
return false;
} else if (!mClassName.equals(other.mClassName))
return false;
if (mTestName == null) {
if (other.mTestName != null)
return false;
//Synthetic comment -- @@ -112,12 +86,6 @@

@Override
public String toString() {
        return String.format("%s#%s", getClassName(), getTestName());
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index 148f329..478e09e 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
private static final String CLASS_NAME = "com.test.FooTest";
private static final String TEST_NAME = "testFoo";
private static final String STACK_TRACE = "java.lang.AssertionFailedException";
    private static final TestIdentifier TEST_ID = new TestIdentifier(CLASS_NAME, TEST_NAME);

/**
* @param name - test name








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index 23b31f0..c579456 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestReference;
import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;
import org.eclipse.jdt.internal.junit.runner.TestExecution;
//Synthetic comment -- @@ -38,6 +40,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -135,14 +138,17 @@
runners.add(runner);
}

        Map<String, TestSuiteReference> testTree = new HashMap<String, TestSuiteReference>();

// Launch all test info collector jobs
        List<TestTreeCollectorJob> collectorJobs = new ArrayList<TestTreeCollectorJob>(devices.size());
for (int i = 0; i < devices.size(); i++) {
RemoteAndroidTestRunner runner = runners.get(i);
            String deviceName = devices.get(i).getName();
            TestCollector collector = new TestCollector(deviceName, testTree);

TestTreeCollectorJob job = new TestTreeCollectorJob(
                    "Test Tree Collector for " + deviceName,
runner, mLaunchInfo.isDebugMode(), collector);
job.setPriority(Job.INTERACTIVE);
job.schedule();
//Synthetic comment -- @@ -151,7 +157,8 @@
}

// wait for all test info collector jobs to complete
        int totalTests = 0;
        for (TestTreeCollectorJob job : collectorJobs) {
try {
job.join();
} catch (InterruptedException e) {
//Synthetic comment -- @@ -163,27 +170,35 @@
endTestRunWithError(job.getResult().getMessage());
return;
}

            TestCollector collector = job.getCollector();
            String err = collector.getErrorMessage();
            if (err != null) {
                endTestRunWithError(err);
                return;
            }

            totalTests += collector.getTestCaseCount();
}

AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Sending test information to Eclipse");
        notifyTestRunStarted(totalTests);

        sendTrees(testTree);

List<TestRunnerJob> instrumentationRunnerJobs =
new ArrayList<TestRunnerJob>(devices.size());

        TestResultsNotifier notifier = new TestResultsNotifier(mExecution.getListener(),
                devices.size());

// Spawn all instrumentation runner jobs
for (int i = 0; i < devices.size(); i++) {
RemoteAndroidTestRunner runner = runners.get(i);
            String deviceName = devices.get(i).getName();
            TestRunListener testRunListener = new TestRunListener(deviceName, notifier);
InstrumentationRunJob job = new InstrumentationRunJob(
                    "Test Tree Collector for " + deviceName,
runner, mLaunchInfo.isDebugMode(), testRunListener);
job.setPriority(Job.INTERACTIVE);
job.schedule();
//Synthetic comment -- @@ -207,6 +222,13 @@
}
}

    /** Sends info about the test tree to be executed (ie the suites and their enclosed tests) */
    private void sendTrees(Map<String, TestSuiteReference> testTree) {
        for (ITestReference ref : testTree.values()) {
            ref.sendTree(this);
        }
    }

private static abstract class TestRunnerJob extends Job {
private ITestRunListener mListener;
private RemoteAndroidTestRunner mRunner;
//Synthetic comment -- @@ -258,12 +280,16 @@
return mIsDebug;
}

        public ITestRunListener getListener() {
            return mListener;
        }

protected abstract void setupRunner();
}

private static class TestTreeCollectorJob extends TestRunnerJob {
public TestTreeCollectorJob(String name, RemoteAndroidTestRunner runner, boolean isDebug,
                TestCollector listener) {
super(name, runner, isDebug, listener);
}

//Synthetic comment -- @@ -279,6 +305,10 @@
// report Binder transaction failures
runner.addInstrumentationArg(DELAY_MSEC_KEY, COLLECT_TEST_DELAY_MS);
}

        public TestCollector getCollector() {
            return (TestCollector) getListener();
        }
}

private static class InstrumentationRunJob extends TestRunnerJob {
//Synthetic comment -- @@ -345,31 +375,63 @@
notifyTestRunEnded(0);
}

    private class TestResultsNotifier {
        private final IListensToTestExecutions mListener;
        private final int mDeviceCount;

        private int mCompletedRuns;
        private long mMaxElapsedTime;

        public TestResultsNotifier(IListensToTestExecutions listener, int nDevices) {
            mListener = listener;
            mDeviceCount = nDevices;
        }

        public synchronized void testEnded(TestCaseReference ref) {
            mListener.notifyTestEnded(ref);
        }

        public synchronized void testFailed(TestReferenceFailure ref) {
            mListener.notifyTestFailed(ref);
        }

        public synchronized void testRunEnded(String mDeviceName, long elapsedTime) {
            mCompletedRuns++;

            if (elapsedTime > mMaxElapsedTime) {
                mMaxElapsedTime = elapsedTime;
            }

            if (mCompletedRuns == mDeviceCount) {
                notifyTestRunEnded(mMaxElapsedTime);
            }
        }

        public synchronized void testStarted(TestCaseReference testId) {
            mListener.notifyTestStarted(testId);
        }
    }

/**
* TestRunListener that communicates results in real-time back to JDT JUnit
*/
private class TestRunListener implements ITestRunListener {
        private final String mDeviceName;
        private TestResultsNotifier mNotifier;

        /** Constructs a {@link ITestRunListener} that listens for test results on given device. */
        public TestRunListener(String deviceName, TestResultsNotifier notifier) {
            mDeviceName = deviceName;
            mNotifier = notifier;
}

@Override
        public void testEnded(TestIdentifier test, Map<String, String> ignoredTestMetrics) {
            mNotifier.testEnded(new TestCaseReference(mDeviceName, test));
}

@Override
        public void testFailed(TestFailure status, TestIdentifier test, String trace) {
String statusString;
if (status == TestFailure.ERROR) {
statusString = MessageIds.TEST_ERROR;
//Synthetic comment -- @@ -377,47 +439,28 @@
statusString = MessageIds.TEST_FAILED;
}
TestReferenceFailure failure =
                new TestReferenceFailure(new TestCaseReference(mDeviceName, test),
statusString, trace, null);
            mNotifier.testFailed(failure);
}

@Override
public synchronized void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
            mNotifier.testRunEnded(mDeviceName, elapsedTime);
AdtPlugin.printToConsole(mLaunchInfo.getProject(),
LaunchMessages.RemoteAdtTestRunner_RunCompleteMsg);
}

@Override
public synchronized void testRunFailed(String errorMessage) {
reportError(errorMessage);
}

@Override
public synchronized void testRunStarted(String runName, int testCount) {
// ignore
}

@Override
public synchronized void testRunStopped(long elapsedTime) {
notifyTestRunStopped(elapsedTime);
//Synthetic comment -- @@ -425,19 +468,14 @@
LaunchMessages.RemoteAdtTestRunner_RunStoppedMsg);
}

@Override
public synchronized void testStarted(TestIdentifier test) {
            TestCaseReference testId = new TestCaseReference(mDeviceName, test);
            mNotifier.testStarted(testId);
}
}

    /** Override parent to get extra logs. */
@Override
protected boolean connect() {
boolean result = super.connect();
//Synthetic comment -- @@ -448,9 +486,7 @@
return result;
}

    /** Override parent to dump error message to console. */
@Override
public void runFailed(String message, Exception exception) {
if (exception != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCaseReference.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCaseReference.java
//Synthetic comment -- index a81e266..e05e9b8 100644

//Synthetic comment -- @@ -25,26 +25,18 @@
*/
@SuppressWarnings("restriction")
class TestCaseReference extends AndroidTestReference {
private final String mClassName;
private final String mTestName;
private final String mDeviceName;

/**
* Creates a TestCaseReference from a {@link TestIdentifier}
* @param test
*/
    TestCaseReference(String deviceName, TestIdentifier test) {
        mDeviceName = deviceName;
        mClassName = test.getClassName();
        mTestName = test.getTestName();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java
//Synthetic comment -- index 91954b4..d62acf9 100644

//Synthetic comment -- @@ -19,26 +19,25 @@
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.TestIdentifier;

import java.util.Map;

/**
* Collects info about tests to be executed by listening to the results of an Android test run.
*/
class TestCollector implements ITestRunListener {
    private final String mDeviceName;
    private final Map<String, TestSuiteReference> mTestTree;

private int mTotalTestCount;
/** test name to test suite reference map. */

private String mErrorMessage = null;

    TestCollector(String deviceName, Map<String, TestSuiteReference> testTree) {
        mDeviceName = deviceName;
        mTestTree = testTree;

mTotalTestCount = 0;
}

@Override
//Synthetic comment -- @@ -94,30 +93,20 @@
TestSuiteReference suiteRef;
TestSuiteReference deviceSuiteRef;

        // if the device name is available, nest the test under a per device test suite
        deviceSuiteRef = mTestTree.get(mDeviceName);
        if (deviceSuiteRef == null) {
            deviceSuiteRef = new TestSuiteReference(mDeviceName);
            mTestTree.put(mDeviceName, deviceSuiteRef);
}

        suiteRef = deviceSuiteRef.getTestSuite(test.getClassName());
        if (suiteRef == null) {
            suiteRef = new TestSuiteReference(test.getClassName());
            deviceSuiteRef.addTest(suiteRef);
        }

        suiteRef.addTest(new TestCaseReference(mDeviceName, test));
}

/**
//Synthetic comment -- @@ -128,17 +117,6 @@
}

/**
* Returns the error message that was reported when collecting test info.
* Returns <code>null</code> if no error occurred.
*/







