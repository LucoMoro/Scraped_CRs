/*junit: Launch tests on multiple devices concurrently.

Rather than launch tests sequentially on each device, launch
them concurrently on all devices. An Eclipse Job is created per
device from which the test tree information is collected and then
tests are run.

Change-Id:I564f04616e2f1dc26571ad7eec77df1d77c0afa9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index df47e27..02f89c7 100755

//Synthetic comment -- @@ -27,6 +27,10 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.LaunchMessages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;
import org.eclipse.jdt.internal.junit.runner.TestExecution;
//Synthetic comment -- @@ -84,6 +88,15 @@
* executing the tests,  and send it back to JDT JUnit. The second is the actual test execution,
* whose results will be communicated back in real-time to JDT JUnit.
*
     * The tests are run concurrently on all devices. The overall structure is as follows:
     * <ol>
     *   <li> First, a separate job per device is run to collect test tree data. </li>
     *   <li> Once all the devices have reported their test tree data, the tree info is
     *        passed to the Junit UI </li>
     *   <li> A job per device is again launched to do the actual test run. </li>
     *   <li> As tests complete, the test run listener updates the Junit UI </li>
     * </ol>
     *
* @param testClassNames ignored - the AndroidJUnitLaunchInfo will be used to determine which
*     tests to run.
* @param testName ignored
//Synthetic comment -- @@ -122,66 +135,165 @@
runners.add(runner);
}

        // Launch all test info collector jobs
TestCollector collector = new TestCollector();
        List<TestRunnerJob> collectorJobs = new ArrayList<TestRunnerJob>(devices.size());
        for (int i = 0; i < devices.size(); i++) {
            RemoteAndroidTestRunner runner = runners.get(i);

            TestTreeCollectorJob job = new TestTreeCollectorJob(
                    "Test Tree Collector for " + devices.get(i).getName(),
                    runner, mLaunchInfo.isDebugMode(), collector);
            job.setPriority(Job.INTERACTIVE);
            job.schedule();

            collectorJobs.add(job);
        }

        // wait for all test info collector jobs to complete
        for (TestRunnerJob job : collectorJobs) {
try {
                job.join();
            } catch (InterruptedException e) {
                endTestRunWithError(e.getMessage());
                return;
            }

            if (!job.getResult().isOK()) {
                endTestRunWithError(job.getResult().getMessage());
                return;
}
}

        if (collector.getErrorMessage() != null) {
            endTestRunWithError(collector.getErrorMessage());
            return;
        }

        AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Sending test information to Eclipse");
notifyTestRunStarted(collector.getTestCaseCount() * devices.size());
collector.sendTrees(this);

TestRunListener testRunListener = new TestRunListener(devices.size());
        List<TestRunnerJob> instrumentationRunnerJobs =
                new ArrayList<TestRunnerJob>(devices.size());

        // Spawn all instrumentation runner jobs
        for (int i = 0; i < devices.size(); i++) {
            RemoteAndroidTestRunner runner = runners.get(i);

            InstrumentationRunJob job = new InstrumentationRunJob(
                    "Test Tree Collector for " + devices.get(i).getName(),
                    runner, mLaunchInfo.isDebugMode(), testRunListener);
            job.setPriority(Job.INTERACTIVE);
            job.schedule();

            instrumentationRunnerJobs.add(job);
        }

        // Wait for all jobs to complete
        for (TestRunnerJob job : instrumentationRunnerJobs) {
try {
                job.join();
            } catch (InterruptedException e) {
                endTestRunWithError(e.getMessage());
                return;
            }

            if (!job.getResult().isOK()) {
                endTestRunWithError(job.getResult().getMessage());
                return;
            }
        }
    }

    private static abstract class TestRunnerJob extends Job {
        private ITestRunListener mListener;
        private RemoteAndroidTestRunner mRunner;
        private boolean mIsDebug;

        public TestRunnerJob(String name, RemoteAndroidTestRunner runner,
                boolean isDebug, ITestRunListener listener) {
            super(name);

            mRunner = runner;
            mIsDebug = isDebug;
            mListener = listener;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            try {
                setupRunner();
                mRunner.run(mListener);
} catch (TimeoutException e) {
                return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID,
                        LaunchMessages.RemoteAdtTestRunner_RunTimeoutException,
                        e);
} catch (IOException e) {
                return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format(LaunchMessages.RemoteAdtTestRunner_RunIOException_s,
                                e.getMessage()),
                        e);
} catch (AdbCommandRejectedException e) {
                return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format(
                                LaunchMessages.RemoteAdtTestRunner_RunAdbCommandRejectedException_s,
                                e.getMessage()),
                        e);
} catch (ShellCommandUnresponsiveException e) {
                return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID,
                        LaunchMessages.RemoteAdtTestRunner_RunTimeoutException,
                        e);
            }

            return Status.OK_STATUS;
        }

        public RemoteAndroidTestRunner getRunner() {
            return mRunner;
        }

        public boolean isDebug() {
            return mIsDebug;
        }

        protected abstract void setupRunner();
    }

    private static class TestTreeCollectorJob extends TestRunnerJob {
        public TestTreeCollectorJob(String name, RemoteAndroidTestRunner runner, boolean isDebug,
                ITestRunListener listener) {
            super(name, runner, isDebug, listener);
        }

        @Override
        protected void setupRunner() {
            RemoteAndroidTestRunner runner = getRunner();

            // set log only to just collect test case info,
            // so Eclipse has correct test case count/tree info
            runner.setLogOnly(true);

            // add a small delay between each test. Otherwise for large test suites framework may
            // report Binder transaction failures
            runner.addInstrumentationArg(DELAY_MSEC_KEY, COLLECT_TEST_DELAY_MS);
        }
    }

    private static class InstrumentationRunJob extends TestRunnerJob {
        public InstrumentationRunJob(String name, RemoteAndroidTestRunner runner, boolean isDebug,
                ITestRunListener listener) {
            super(name, runner, isDebug, listener);
        }

        @Override
        protected void setupRunner() {
            RemoteAndroidTestRunner runner = getRunner();
            runner.setLogOnly(false);
            runner.removeInstrumentationArg(DELAY_MSEC_KEY);
            if (isDebug()) {
                runner.setDebug(true);
}
}
}
//Synthetic comment -- @@ -228,6 +340,11 @@
//notifyTestRunStopped(-1);
}

    private void endTestRunWithError(String message) {
        reportError(message);
        notifyTestRunEnded(0);
    }

/**
* TestRunListener that communicates results in real-time back to JDT JUnit
*/
//Synthetic comment -- @@ -243,7 +360,7 @@
}

@Override
        public synchronized void testEnded(TestIdentifier test, Map<String, String> ignoredTestMetrics) {
mExecution.getListener().notifyTestEnded(new TestCaseReference(test));
}

//Synthetic comment -- @@ -251,7 +368,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testFailed(com.android.ddmlib.testrunner.ITestRunListener.TestFailure, com.android.ddmlib.testrunner.TestIdentifier, java.lang.String)
*/
@Override
        public synchronized void testFailed(TestFailure status, TestIdentifier test, String trace) {
String statusString;
if (status == TestFailure.ERROR) {
statusString = MessageIds.TEST_ERROR;
//Synthetic comment -- @@ -268,7 +385,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long, Map<String, String>)
*/
@Override
        public synchronized void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
mRunCount--;

if (mRunCount > 0) {
//Synthetic comment -- @@ -285,7 +402,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunFailed(java.lang.String)
*/
@Override
        public synchronized void testRunFailed(String errorMessage) {
reportError(errorMessage);
}

//Synthetic comment -- @@ -293,7 +410,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
@Override
        public synchronized void testRunStarted(String runName, int testCount) {
// ignore
}

//Synthetic comment -- @@ -301,7 +418,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStopped(long)
*/
@Override
        public synchronized void testRunStopped(long elapsedTime) {
notifyTestRunStopped(elapsedTime);
AdtPlugin.printToConsole(mLaunchInfo.getProject(),
LaunchMessages.RemoteAdtTestRunner_RunStoppedMsg);
//Synthetic comment -- @@ -311,7 +428,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testStarted(com.android.ddmlib.testrunner.TestIdentifier)
*/
@Override
        public synchronized void testStarted(TestIdentifier test) {
TestCaseReference testId = new TestCaseReference(test);
mExecution.getListener().notifyTestStarted(testId);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java
//Synthetic comment -- index e0a639a..91954b4 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
}

@Override
    public synchronized void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
// ignore
}

//Synthetic comment -- @@ -50,7 +50,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testFailed(com.android.ddmlib.testrunner.ITestRunListener.TestFailure, com.android.ddmlib.testrunner.TestIdentifier, java.lang.String)
*/
@Override
    public synchronized void testFailed(TestFailure status, TestIdentifier test, String trace) {
// ignore - should be impossible since this is only collecting test information
}

//Synthetic comment -- @@ -58,7 +58,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long, Map<String, String>)
*/
@Override
    public synchronized void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
// ignore
}

//Synthetic comment -- @@ -66,7 +66,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunFailed(java.lang.String)
*/
@Override
    public synchronized void testRunFailed(String errorMessage) {
mErrorMessage = errorMessage;
}

//Synthetic comment -- @@ -74,7 +74,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
@Override
    public synchronized void testRunStarted(String ignoredRunName, int testCount) {
mTotalTestCount = testCount;
}

//Synthetic comment -- @@ -82,7 +82,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStopped(long)
*/
@Override
    public synchronized void testRunStopped(long elapsedTime) {
// ignore
}

//Synthetic comment -- @@ -90,7 +90,7 @@
* @see com.android.ddmlib.testrunner.ITestRunListener#testStarted(com.android.ddmlib.testrunner.TestIdentifier)
*/
@Override
    public synchronized void testStarted(TestIdentifier test) {
TestSuiteReference suiteRef;
TestSuiteReference deviceSuiteRef;

//Synthetic comment -- @@ -123,7 +123,7 @@
/**
* Returns the total test count in the test run.
*/
    public synchronized int getTestCaseCount() {
return mTotalTestCount;
}

//Synthetic comment -- @@ -132,7 +132,7 @@
*
* @param notified the {@link IVisitsTestTrees} to send test data to
*/
    public synchronized void sendTrees(IVisitsTestTrees notified) {
for (ITestReference ref : mTestTree.values()) {
ref.sendTree(notified);
}
//Synthetic comment -- @@ -142,7 +142,7 @@
* Returns the error message that was reported when collecting test info.
* Returns <code>null</code> if no error occurred.
*/
    public synchronized String getErrorMessage() {
return mErrorMessage;
}
}







