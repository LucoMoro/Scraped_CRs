/*Add more console logs during ADT test run.

This is intended to give the user more visibility the exact progress of the
test run. And also better notification of failures.

Also tweak the ADT launch action so it reports its terminated when the test
runner thread is no longer active.

Related bug 7976

Change-Id:Idb258dfa1c15d6937d505bd86add208b4d09d526*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchAction.java
//Synthetic comment -- index fcb859a..2bde545 100644

//Synthetic comment -- @@ -239,7 +239,7 @@
* @see org.eclipse.debug.core.model.ITerminate#isTerminated()
*/
public boolean isTerminated() {
            return mIsTerminated || isInterrupted();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index 0a045bc..b3dfb35 100755

//Synthetic comment -- @@ -33,7 +33,7 @@
* Supports Eclipse JUnit execution of Android tests.
* <p/>
* Communicates back to a Eclipse JDT JUnit client via a socket connection.
 *
* @see org.eclipse.jdt.internal.junit.runner.RemoteTestRunner for more details on the protocol
*/
@SuppressWarnings("restriction")
//Synthetic comment -- @@ -41,17 +41,17 @@

private AndroidJUnitLaunchInfo mLaunchInfo;
private TestExecution mExecution;

/**
* Initialize the JDT JUnit test runner parameters from the {@code args}.
     *
     * @param args name-value pair of arguments to pass to parent JUnit runner.
* @param launchInfo the Android specific test launch info
*/
protected void init(String[] args, AndroidJUnitLaunchInfo launchInfo) {
defaultInit(args);
mLaunchInfo = launchInfo;
    }

/**
* Runs a set of tests, and reports back results using parent class.
//Synthetic comment -- @@ -65,13 +65,13 @@
*   <li>The test execution result for each test method. Expects individual notifications of
*   the test execution start, any failures, and the end of the test execution.</li>
*   <li>The end of the test run, with its elapsed time.</li>
     * </ol>
* <p/>
* In order to satisfy this, this method performs two actual Android instrumentation runs.
* The first is a 'log only' run that will collect the test tree data, without actually
* executing the tests,  and send it back to JDT JUnit. The second is the actual test execution,
* whose results will be communicated back in real-time to JDT JUnit.
     *
* @param testClassNames ignored - the AndroidJUnitLaunchInfo will be used to determine which
*     tests to run.
* @param testName ignored
//Synthetic comment -- @@ -81,16 +81,16 @@
public void runTests(String[] testClassNames, String testName, TestExecution execution) {
// hold onto this execution reference so it can be used to report test progress
mExecution = execution;

        RemoteAndroidTestRunner runner = new RemoteAndroidTestRunner(mLaunchInfo.getAppPackage(),
                mLaunchInfo.getRunner(), mLaunchInfo.getDevice());

if (mLaunchInfo.getTestClass() != null) {
if (mLaunchInfo.getTestMethod() != null) {
runner.setMethodName(mLaunchInfo.getTestClass(), mLaunchInfo.getTestMethod());
            } else {
runner.setClassName(mLaunchInfo.getTestClass());
            }
}

if (mLaunchInfo.getTestPackage() != null) {
//Synthetic comment -- @@ -102,6 +102,8 @@
runner.setLogOnly(true);
TestCollector collector = new TestCollector();
try {
            AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Collecting test information");

runner.run(collector);
if (collector.getErrorMessage() != null) {
// error occurred during test collection.
//Synthetic comment -- @@ -111,6 +113,8 @@
return;
}
notifyTestRunStarted(collector.getTestCaseCount());
            AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Sending test information to Eclipse");

collector.sendTrees(this);

// now do real execution
//Synthetic comment -- @@ -118,23 +122,24 @@
if (mLaunchInfo.isDebugMode()) {
runner.setDebug(true);
}
            AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Running tests...");
runner.run(new TestRunListener());
} catch (IOException e) {
reportError(String.format(LaunchMessages.RemoteAdtTestRunner_RunIOException_s,
e.getMessage()));
}
}

/**
* Main entry method to run tests
     *
* @param programArgs JDT JUnit program arguments to be processed by parent
* @param junitInfo the {@link AndroidJUnitLaunchInfo} containing info about this test ru
*/
public void runTests(String[] programArgs, AndroidJUnitLaunchInfo junitInfo) {
init(programArgs, junitInfo);
run();
    }

/**
* Stop the current test run.
//Synthetic comment -- @@ -147,7 +152,7 @@
protected void stop() {
if (mExecution != null) {
mExecution.stop();
        }
}

private void notifyTestRunEnded(long elapsedTime) {
//Synthetic comment -- @@ -161,14 +166,14 @@
* @param errorMessage
*/
private void reportError(String errorMessage) {
        AdtPlugin.printErrorToConsole(mLaunchInfo.getProject(),
String.format(LaunchMessages.RemoteAdtTestRunner_RunFailedMsg_s, errorMessage));
// is this needed?
//notifyTestRunStopped(-1);
}

/**
     * TestRunListener that communicates results in real-time back to JDT JUnit
*/
private class TestRunListener implements ITestRunListener {

//Synthetic comment -- @@ -189,8 +194,8 @@
} else {
statusString = MessageIds.TEST_FAILED;
}
            TestReferenceFailure failure =
                new TestReferenceFailure(new TestCaseReference(test),
statusString, trace, null);
mExecution.getListener().notifyTestFailed(failure);
}
//Synthetic comment -- @@ -234,5 +239,33 @@
TestCaseReference testId = new TestCaseReference(test);
mExecution.getListener().notifyTestStarted(testId);
}

    }

    /**
     * Override parent to get extra logs.
     */
    @Override
    protected boolean connect() {
        boolean result = super.connect();
        if (!result) {
            AdtPlugin.printErrorToConsole(mLaunchInfo.getProject(),
                    "Connect to Eclipse test result listener failed");
        }
        return result;
    }

    /**
     * Override parent to dump error message to console.
     */
    @Override
    public void runFailed(String message, Exception exception) {
        if (exception != null) {
            AdtPlugin.logAndPrintError(exception, mLaunchInfo.getProject().getName(),
                    "Test launch failed: %s", message);
        } else {
            AdtPlugin.printErrorToConsole(mLaunchInfo.getProject(), "Test launch failed: %s",
                    message);
        }
}
}
\ No newline at end of file







