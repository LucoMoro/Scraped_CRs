/*parse instrumentation result bundles

During tests, there are per-instrumentation based results emmitted
as key-value pairs. Examples include performance tests and normal
tests in code coverage mode. Currently most of these are discarded
by InstrumentationResultParser, this change adds parsing function,
stores the key-value pairs in a map, and finally send it to
ITestRunListeners at the end of test run.

Change-Id:If04c5f8b10eeaca494a155ed6c4a25bf0d9d892c*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java
//Synthetic comment -- index b61a698..5bcc836 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmlib.testrunner;

/**
* Receives event notifications during instrumentation test runs. 
* Patterned after {@link junit.runner.TestRunListener}.
//Synthetic comment -- @@ -43,8 +45,9 @@
* Reports end of test run.
* 
* @param elapsedTime device reported elapsed time, in milliseconds
*/
    public void testRunEnded(long elapsedTime);

/**
* Reports test run stopped before completion.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index ff49c44..c3399f0 100755

//Synthetic comment -- @@ -22,6 +22,8 @@

import java.util.ArrayList;
import java.util.Collection;

/**
* Parses the 'raw output mode' results of an instrumentation test run from shell and informs a
//Synthetic comment -- @@ -156,6 +158,15 @@
/** The number of tests expected to run  */
private int mNumTestsExpected = 0;

private static final String LOG_TAG = "InstrumentationResultParser";

/** Error message supplied when no parseable test results are received from test run. */
//Synthetic comment -- @@ -216,19 +227,23 @@
if (line.startsWith(Prefixes.STATUS_CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
parseStatusCode(line);
} else if (line.startsWith(Prefixes.STATUS)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
parseKey(line, Prefixes.STATUS.length());
} else if (line.startsWith(Prefixes.RESULT)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
parseKey(line, Prefixes.RESULT.length());
} else if (line.startsWith(Prefixes.STATUS_FAILED) ||
line.startsWith(Prefixes.CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
// these codes signal the end of the instrumentation run
mTestRunFinished = true;
// just ignore the remaining data on this line
//Synthetic comment -- @@ -250,25 +265,34 @@
*/
private void submitCurrentKeyValue() {
if (mCurrentKey != null && mCurrentValue != null) {
            TestResult testInfo = getCurrentTestInfo();
            String statusValue = mCurrentValue.toString();

            if (mCurrentKey.equals(StatusKeys.CLASS)) {
                testInfo.mTestClass = statusValue.trim();
            } else if (mCurrentKey.equals(StatusKeys.TEST)) {
                testInfo.mTestName = statusValue.trim();
            } else if (mCurrentKey.equals(StatusKeys.NUMTESTS)) {
                try {
                    testInfo.mNumTests = Integer.parseInt(statusValue);
                } catch (NumberFormatException e) {
                    Log.e(LOG_TAG, "Unexpected integer number of tests, received " + statusValue);
}
            } else if (mCurrentKey.equals(StatusKeys.ERROR) ||
                    mCurrentKey.equals(StatusKeys.SHORTMSG)) {
                // test run must have failed
                handleTestRunFailed(statusValue);
            } else if (mCurrentKey.equals(StatusKeys.STACK)) {
                testInfo.mStackTrace = statusValue;
}

mCurrentKey = null;
//Synthetic comment -- @@ -490,7 +514,7 @@
// no tests
listener.testRunStarted(0);
}
                listener.testRunEnded(mTestTime);
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index ed3a420..7a70518 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

import junit.framework.TestCase;


/**
* Tests InstrumentationResultParser.
//Synthetic comment -- @@ -205,6 +207,24 @@
}

/**
* Builds a common test result using TEST_NAME and TEST_CLASS.
*/
private StringBuilder buildCommonResult() {
//Synthetic comment -- @@ -249,6 +269,18 @@
}

/**
* Append a line to output.
*/
private void addLine(StringBuilder outputBuilder, String lineContent) {
//Synthetic comment -- @@ -313,12 +345,14 @@
boolean mStopped;
/** stores the error message provided to testRunFailed */
String mRunFailedMessage;

VerifyingTestResult() {
mNumTestsRun = 0;
mTestStatus = null;
mStopped = false;
mRunFailedMessage = null;
}

public void testEnded(TestIdentifier test) {
//Synthetic comment -- @@ -335,9 +369,9 @@
assertEquals("Unexpected test ended", mTestName, test.getTestName());
}

        public void testRunEnded(long elapsedTime) {
mTestTime = elapsedTime;

}

public void testRunStarted(int testCount) {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 85b57aa..9d4e77b 100644

//Synthetic comment -- @@ -274,7 +274,7 @@
// ignore
}

        public void testRunEnded(long elapsedTime) {
// ignore
}








