/*Add a runName parameter to ITestRunListener#testRunStarted.

Change-Id:Ibaab85879f5432a24f8d44dc8d22aa6b0965a0c2*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java
//Synthetic comment -- index 40f89fe..b235597 100644

//Synthetic comment -- @@ -36,11 +36,12 @@

/** 
* Reports the start of a test run.
     * 
* @param testCount total number of tests in test run
*/
    public void testRunStarted(int testCount);
    
/**
* Reports end of test run.
* 








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index e9b8bb5..61236d8 100644

//Synthetic comment -- @@ -127,6 +127,9 @@
}
}

/** Stores the status values for the test result currently being parsed */
private TestResult mCurrentTestResult = null;

//Synthetic comment -- @@ -183,18 +186,24 @@
/**
* Creates the InstrumentationResultParser.
*
* @param listeners informed of test results as the tests are executing
*/
    public InstrumentationResultParser(Collection<ITestRunListener> listeners) {
mTestListeners = new ArrayList<ITestRunListener>(listeners);
}

/**
* Creates the InstrumentationResultParser for a single listener.
*
* @param listener informed of test results as the tests are executing
*/
    public InstrumentationResultParser(ITestRunListener listener) {
mTestListeners = new ArrayList<ITestRunListener>(1);
mTestListeners.add(listener);
}
//Synthetic comment -- @@ -455,7 +464,7 @@
// if start test run not reported yet
if (!mTestStartReported && testInfo.mNumTests != null) {
for (ITestRunListener listener : mTestListeners) {
                listener.testRunStarted(testInfo.mNumTests);
}
mNumTestsExpected = testInfo.mNumTests;
mTestStartReported = true;
//Synthetic comment -- @@ -539,7 +548,7 @@
if (!mTestStartReported) {
// test run wasn't started, but it finished successfully. Must be a run with
// no tests
                    listener.testRunStarted(0);
}
listener.testRunEnded(mTestTime, mInstrumentationResultBundle);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 681c214..bc7b012 100644

//Synthetic comment -- @@ -230,7 +230,8 @@
getArgsCommand(), getRunnerPath());
Log.i(LOG_TAG, String.format("Running %s on %s", runCaseCommandStr,
mRemoteDevice.getSerialNumber()));
        mParser = new InstrumentationResultParser(listeners);

mRemoteDevice.executeShellCommand(runCaseCommandStr, mParser, mMaxTimeToOutputResponse);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index fe80427..946e614 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
private VerifyingTestResult mTestResult;

// static dummy test names to use for validation
private static final String CLASS_NAME = "com.test.FooTest";
private static final String TEST_NAME = "testFoo";
private static final String STACK_TRACE = "java.lang.AssertionFailedException";
//Synthetic comment -- @@ -50,7 +51,7 @@
protected void setUp() throws Exception {
super.setUp();
mTestResult = new VerifyingTestResult();
        mParser = new InstrumentationResultParser(mTestResult);
}

/**
//Synthetic comment -- @@ -344,6 +345,7 @@
}

private void assertCommonAttributes() {
assertEquals(CLASS_NAME, mTestResult.mSuiteName);
assertEquals(1, mTestResult.mTestCount);
assertEquals(TEST_NAME, mTestResult.mTestName);
//Synthetic comment -- @@ -354,6 +356,7 @@
*/
private class VerifyingTestResult implements ITestRunListener {

String mSuiteName;
int mTestCount;
int mNumTestsRun;
//Synthetic comment -- @@ -395,7 +398,8 @@
mResultBundle = resultBundle;
}

        public void testRunStarted(int testCount) {
mTestCount = testCount;
}









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 77d7300..4ee2f4d 100644

//Synthetic comment -- @@ -305,7 +305,7 @@
// ignore
}

        public void testRunStarted(int testCount) {
// ignore
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index 7ba1b2f..de3ea38 100755

//Synthetic comment -- @@ -190,14 +190,7 @@
*/
private class TestRunListener implements ITestRunListener {

        /* (non-Javadoc)
         * @see com.android.ddmlib.testrunner.ITestRunListener#testEnded(com.android.ddmlib.testrunner.TestIdentifier)
         */
        public void testEnded(TestIdentifier test) {
            mExecution.getListener().notifyTestEnded(new TestCaseReference(test));
        }

        public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
mExecution.getListener().notifyTestEnded(new TestCaseReference(test));
}

//Synthetic comment -- @@ -236,7 +229,7 @@
/* (non-Javadoc)
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
        public void testRunStarted(int testCount) {
// ignore
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java
//Synthetic comment -- index c5f52c8..3f099a2 100644

//Synthetic comment -- @@ -41,13 +41,6 @@
mTestTree = new HashMap<String, TestSuiteReference>();
}

    /* (non-Javadoc)
     * @see com.android.ddmlib.testrunner.ITestRunListener#testEnded(com.android.ddmlib.testrunner.TestIdentifier)
     */
    public void testEnded(TestIdentifier test) {
        // ignore
    }

public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
// ignore
}
//Synthetic comment -- @@ -76,7 +69,7 @@
/* (non-Javadoc)
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunStarted(int)
*/
    public void testRunStarted(int testCount) {
mTotalTestCount = testCount;
}








