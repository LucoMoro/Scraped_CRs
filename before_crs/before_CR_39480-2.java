/*junit: Launch tests on multiple devices concurrently.

Rather than launch tests sequentially on each device, launch
them concurrently on all devices. An Eclipse Job is created per
device from which the test tree information is collected and then
tests are run.

Change-Id:I564f04616e2f1dc26571ad7eec77df1d77c0afa9*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index df47e27..23b31f0 100755

//Synthetic comment -- @@ -27,6 +27,10 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.LaunchMessages;

import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;
import org.eclipse.jdt.internal.junit.runner.TestExecution;
//Synthetic comment -- @@ -84,6 +88,15 @@
* executing the tests,  and send it back to JDT JUnit. The second is the actual test execution,
* whose results will be communicated back in real-time to JDT JUnit.
*
* @param testClassNames ignored - the AndroidJUnitLaunchInfo will be used to determine which
*     tests to run.
* @param testName ignored
//Synthetic comment -- @@ -122,66 +135,165 @@
runners.add(runner);
}

TestCollector collector = new TestCollector();
        for (RemoteAndroidTestRunner runner : runners) {
            // set log only to first collect test case info, so Eclipse has correct test case count/
            // tree info
            runner.setLogOnly(true);
            // add a small delay between each test. Otherwise for large test suites framework may
            // report Binder transaction failures
            runner.addInstrumentationArg(DELAY_MSEC_KEY, COLLECT_TEST_DELAY_MS);

try {
                AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Collecting test information");

                runner.run(collector);
                if (collector.getErrorMessage() != null) {
                    // error occurred during test collection.
                    reportError(collector.getErrorMessage());
                    // abort here
                    notifyTestRunEnded(0);
                    return;
                }
                AdtPlugin.printToConsole(mLaunchInfo.getProject(),
                        "Sending test information to Eclipse");
            } catch (TimeoutException e) {
                reportError(LaunchMessages.RemoteAdtTestRunner_RunTimeoutException);
            } catch (IOException e) {
                reportError(String.format(LaunchMessages.RemoteAdtTestRunner_RunIOException_s,
                        e.getMessage()));
            } catch (AdbCommandRejectedException e) {
                reportError(String.format(
                        LaunchMessages.RemoteAdtTestRunner_RunAdbCommandRejectedException_s,
                        e.getMessage()));
            } catch (ShellCommandUnresponsiveException e) {
                reportError(LaunchMessages.RemoteAdtTestRunner_RunTimeoutException);
}
}
notifyTestRunStarted(collector.getTestCaseCount() * devices.size());
collector.sendTrees(this);

TestRunListener testRunListener = new TestRunListener(devices.size());
        for (RemoteAndroidTestRunner runner : runners) {
try {
                // now do real execution
                runner.setLogOnly(false);
                runner.removeInstrumentationArg(DELAY_MSEC_KEY);
                if (mLaunchInfo.isDebugMode()) {
                    runner.setDebug(true);
                }
                AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Running tests...");
                runner.run(testRunListener);
} catch (TimeoutException e) {
                reportError(LaunchMessages.RemoteAdtTestRunner_RunTimeoutException);
} catch (IOException e) {
                reportError(String.format(LaunchMessages.RemoteAdtTestRunner_RunIOException_s,
                        e.getMessage()));
} catch (AdbCommandRejectedException e) {
                reportError(String.format(
                        LaunchMessages.RemoteAdtTestRunner_RunAdbCommandRejectedException_s,
                        e.getMessage()));
} catch (ShellCommandUnresponsiveException e) {
                reportError(LaunchMessages.RemoteAdtTestRunner_RunTimeoutException);
}
}
}
//Synthetic comment -- @@ -228,6 +340,11 @@
//notifyTestRunStopped(-1);
}

/**
* TestRunListener that communicates results in real-time back to JDT JUnit
*/
//Synthetic comment -- @@ -243,7 +360,8 @@
}

@Override
        public void testEnded(TestIdentifier test, Map<String, String> ignoredTestMetrics) {
mExecution.getListener().notifyTestEnded(new TestCaseReference(test));
}

//Synthetic comment -- @@ -251,7 +369,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testFailed(com.android.ddmlib.testrunner.ITestRunListener.TestFailure, com.android.ddmlib.testrunner.TestIdentifier, java.lang.String)
*/
@Override
        public void testFailed(TestFailure status, TestIdentifier test, String trace) {
String statusString;
if (status == TestFailure.ERROR) {
statusString = MessageIds.TEST_ERROR;
//Synthetic comment -- @@ -268,7 +386,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long, Map<String, String>)
*/
@Override
        public void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
mRunCount--;

if (mRunCount > 0) {
//Synthetic comment -- @@ -285,7 +403,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunFailed(java.lang.String)
*/
@Override
        public void testRunFailed(String errorMessage) {
reportError(errorMessage);
}

//Synthetic comment -- @@ -293,7 +411,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
@Override
        public void testRunStarted(String runName, int testCount) {
// ignore
}

//Synthetic comment -- @@ -301,7 +419,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStopped(long)
*/
@Override
        public void testRunStopped(long elapsedTime) {
notifyTestRunStopped(elapsedTime);
AdtPlugin.printToConsole(mLaunchInfo.getProject(),
LaunchMessages.RemoteAdtTestRunner_RunStoppedMsg);
//Synthetic comment -- @@ -311,7 +429,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testStarted(com.android.ddmlib.testrunner.TestIdentifier)
*/
@Override
        public void testStarted(TestIdentifier test) {
TestCaseReference testId = new TestCaseReference(test);
mExecution.getListener().notifyTestStarted(testId);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java
//Synthetic comment -- index e0a639a..91954b4 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
}

@Override
    public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
// ignore
}

//Synthetic comment -- @@ -50,7 +50,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testFailed(com.android.ddmlib.testrunner.ITestRunListener.TestFailure, com.android.ddmlib.testrunner.TestIdentifier, java.lang.String)
*/
@Override
    public void testFailed(TestFailure status, TestIdentifier test, String trace) {
// ignore - should be impossible since this is only collecting test information
}

//Synthetic comment -- @@ -58,7 +58,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long, Map<String, String>)
*/
@Override
    public void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
// ignore
}

//Synthetic comment -- @@ -66,7 +66,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunFailed(java.lang.String)
*/
@Override
    public void testRunFailed(String errorMessage) {
mErrorMessage = errorMessage;
}

//Synthetic comment -- @@ -74,7 +74,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
@Override
    public void testRunStarted(String ignoredRunName, int testCount) {
mTotalTestCount = testCount;
}

//Synthetic comment -- @@ -82,7 +82,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStopped(long)
*/
@Override
    public void testRunStopped(long elapsedTime) {
// ignore
}

//Synthetic comment -- @@ -90,7 +90,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testStarted(com.android.ddmlib.testrunner.TestIdentifier)
*/
@Override
    public void testStarted(TestIdentifier test) {
TestSuiteReference suiteRef;
TestSuiteReference deviceSuiteRef;

//Synthetic comment -- @@ -123,7 +123,7 @@
/**
* Returns the total test count in the test run.
*/
    public int getTestCaseCount() {
return mTotalTestCount;
}

//Synthetic comment -- @@ -132,7 +132,7 @@
*
* @param notified the {@link IVisitsTestTrees} to send test data to
*/
    public void sendTrees(IVisitsTestTrees notified) {
for (ITestReference ref : mTestTree.values()) {
ref.sendTree(notified);
}
//Synthetic comment -- @@ -142,7 +142,7 @@
* Returns the error message that was reported when collecting test info.
* Returns <code>null</code> if no error occurred.
*/
    public String getErrorMessage() {
return mErrorMessage;
}
}







