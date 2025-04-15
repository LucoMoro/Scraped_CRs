/*Write tests for the new ITestRunListener API method

Change-Id:Ieea11742ae23cc30296fcc1cfe477d9814fa4a4e*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index 7a70518..c971a71 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
}

/**
     * Tests basic parsing of a single successful test execution.
*/
public void testTestSuccess() {
StringBuilder output = createSuccessTest();
//Synthetic comment -- @@ -78,12 +78,21 @@
}

/**
     * Tests basic parsing of a successful test execution with metrics.
     */
    public void testTestSuccessMetrics() {
        StringBuilder output = buildCommonResult();

        addStatusKey(output, "randomKey", "randomValue");
        assertNotNull(mTestResult.mTestMetrics);
        assertEquals("randomValue", mTestResult.mTestMetrics.get("randomKey"));
    }

    /**
* Create instrumentation output for a successful single test case execution.
*/
private StringBuilder createSuccessTest() {
StringBuilder output = buildCommonResult();
addSuccessCode(output);
return output;
}
//Synthetic comment -- @@ -231,7 +240,7 @@
StringBuilder output = new StringBuilder();
// add test start bundle
addCommonStatus(output);
        addStartCode(output);
// add end test bundle, without status
addCommonStatus(output);
return output;
//Synthetic comment -- @@ -345,7 +354,8 @@
boolean mStopped;
/** stores the error message provided to testRunFailed */
String mRunFailedMessage;
        Map<String, String> mResultBundle = null;
        Map<String, String> mTestMetrics = null;

VerifyingTestResult() {
mNumTestsRun = 0;
//Synthetic comment -- @@ -355,8 +365,9 @@
mResultBundle = null;
}

        public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
mNumTestsRun++;
            mTestMetrics = testMetrics;
assertEquals("Unexpected class name", mSuiteName, test.getClassName());
assertEquals("Unexpected test ended", mTestName, test.getTestName());









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 343efdc..77d7300 100644

//Synthetic comment -- @@ -289,7 +289,7 @@
*/
private static class EmptyListener implements ITestRunListener {

        public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
// ignore
}








