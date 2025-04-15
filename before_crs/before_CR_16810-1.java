/*Write tests for the new ITestRunListener API method

This change also includes some minor fixes to make the tests compile.
Previously, the makefiles were broken, the tests did not compile
and were not runnable, so some breakage went unnoticed.*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index 7a70518..fa738d3 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
}

/**
     * Tests that a single successful test execution.
*/
public void testTestSuccess() {
StringBuilder output = createSuccessTest();
//Synthetic comment -- @@ -78,12 +78,23 @@
}

/**
* Create instrumentation output for a successful single test case execution.
*/
private StringBuilder createSuccessTest() {
StringBuilder output = buildCommonResult();
        addStartCode(output);
        addCommonStatus(output);
addSuccessCode(output);
return output;
}
//Synthetic comment -- @@ -231,7 +242,7 @@
StringBuilder output = new StringBuilder();
// add test start bundle
addCommonStatus(output);
        addStatusCode(output, "1");
// add end test bundle, without status
addCommonStatus(output);
return output;
//Synthetic comment -- @@ -345,7 +356,8 @@
boolean mStopped;
/** stores the error message provided to testRunFailed */
String mRunFailedMessage;
        Map<String, String> mResultBundle;

VerifyingTestResult() {
mNumTestsRun = 0;
//Synthetic comment -- @@ -355,8 +367,9 @@
mResultBundle = null;
}

        public void testEnded(TestIdentifier test) {
mNumTestsRun++;
assertEquals("Unexpected class name", mSuiteName, test.getClassName());
assertEquals("Unexpected test ended", mTestName, test.getTestName());









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 343efdc..77d7300 100644

//Synthetic comment -- @@ -289,7 +289,7 @@
*/
private static class EmptyListener implements ITestRunListener {

        public void testEnded(TestIdentifier test) {
// ignore
}








