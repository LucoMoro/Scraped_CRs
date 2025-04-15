/*parse instrumentation result bundles

During tests, there are per-instrumentation based results emmitted
as key-value pairs. Examples include performance tests and normal
tests in code coverage mode. Currently most of these are discarded
by InstrumentationResultParser, this change adds parsing function,
stores the key-value pairs in a map, and finally send it to
ITestRunListeners at the end of test run.

Change-Id:If04c5f8b10eeaca494a155ed6c4a25bf0d9d892c*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java
//Synthetic comment -- index b61a698..b8aee4b 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmlib.testrunner;

import java.util.Map;

/**
* Receives event notifications during instrumentation test runs. 
* Patterned after {@link junit.runner.TestRunListener}.
//Synthetic comment -- @@ -44,7 +46,7 @@
* 
* @param elapsedTime device reported elapsed time, in milliseconds
*/
    public void testRunEnded(long elapsedTime, Map<String, String> resultBundle);

/**
* Reports test run stopped before completion.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index ff49c44..b62a16c 100755

//Synthetic comment -- @@ -22,6 +22,8 @@

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
* Parses the 'raw output mode' results of an instrumentation test run from shell and informs a
//Synthetic comment -- @@ -156,6 +158,12 @@
/** The number of tests expected to run  */
private int mNumTestsExpected = 0;

    /** True if the parser is parsing a line beginning with "INSTRUMENTATION_RESULT" */
    private boolean mInInstrumentResultSection = false;

    /** Stores key-value pairs under INSTRUMENTATION_RESULT header, these are per test run, not per test */
    private Map<String, String> mInstrumentationResultBundle = new HashMap<String, String>();

private static final String LOG_TAG = "InstrumentationResultParser";

/** Error message supplied when no parseable test results are received from test run. */
//Synthetic comment -- @@ -216,19 +224,23 @@
if (line.startsWith(Prefixes.STATUS_CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
            mInInstrumentResultSection = false;
parseStatusCode(line);
} else if (line.startsWith(Prefixes.STATUS)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
            mInInstrumentResultSection = false;
parseKey(line, Prefixes.STATUS.length());
} else if (line.startsWith(Prefixes.RESULT)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
            mInInstrumentResultSection = true;
parseKey(line, Prefixes.RESULT.length());
} else if (line.startsWith(Prefixes.STATUS_FAILED) ||
line.startsWith(Prefixes.CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
            mInInstrumentResultSection = false;
// these codes signal the end of the instrumentation run
mTestRunFinished = true;
// just ignore the remaining data on this line
//Synthetic comment -- @@ -250,25 +262,33 @@
*/
private void submitCurrentKeyValue() {
if (mCurrentKey != null && mCurrentValue != null) {
            if (mInInstrumentResultSection) {
                String statusValue = mCurrentValue.toString();
                mInstrumentationResultBundle.put(mCurrentKey, statusValue);
                if (mCurrentKey.equals(StatusKeys.SHORTMSG)) {
                    // test run must have failed
                    handleTestRunFailed(statusValue);
}
            } else {
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
                } else if (mCurrentKey.equals(StatusKeys.ERROR)) {
                    // test run must have failed
                    handleTestRunFailed(statusValue);
                } else if (mCurrentKey.equals(StatusKeys.STACK)) {
                    testInfo.mStackTrace = statusValue;
                }
}

mCurrentKey = null;
//Synthetic comment -- @@ -490,7 +510,7 @@
// no tests
listener.testRunStarted(0);
}
                listener.testRunEnded(mTestTime, mInstrumentationResultBundle);
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index ed3a420..e20c301 100644

//Synthetic comment -- @@ -20,6 +20,10 @@

import junit.framework.TestCase;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.util.Map;


/**
* Tests InstrumentationResultParser.
//Synthetic comment -- @@ -205,6 +209,24 @@
}

/**
     * Test parsing of a test run that produces INSTRUMENTATION_RESULT output. This mimics launch
     * performance test output.
     */
    public void testRunWithInstrumentationResults() {
        StringBuilder output = new StringBuilder();
        addResultKey(output, "other_pss", "2390");
        addResultKey(output, "java_allocated", "2539");
        addResultKey(output, "foo", "bar");
        addLine(output, "INSTRUMENTATION_CODE: -1");

        injectTestString(output.toString());

        assertEquals("2390", mTestResult.mResultBundle.get("other_pss"));
        assertEquals("2539", mTestResult.mResultBundle.get("java_allocated"));
        assertEquals("bar", mTestResult.mResultBundle.get("foo"));
    }

    /**
* Builds a common test result using TEST_NAME and TEST_CLASS.
*/
private StringBuilder buildCommonResult() {
//Synthetic comment -- @@ -249,6 +271,18 @@
}

/**
     * Helper method to add a result key value bundle.
     */
    private void addResultKey(StringBuilder outputBuilder, String key,
          String value) {
      outputBuilder.append("INSTRUMENTATION_RESULT: ");
      outputBuilder.append(key);
      outputBuilder.append('=');
      outputBuilder.append(value);
      addLineBreak(outputBuilder);
    }

    /**
* Append a line to output.
*/
private void addLine(StringBuilder outputBuilder, String lineContent) {
//Synthetic comment -- @@ -313,12 +347,14 @@
boolean mStopped;
/** stores the error message provided to testRunFailed */
String mRunFailedMessage;
        Map<String, String> mResultBundle;

VerifyingTestResult() {
mNumTestsRun = 0;
mTestStatus = null;
mStopped = false;
mRunFailedMessage = null;
            mResultBundle = null;
}

public void testEnded(TestIdentifier test) {
//Synthetic comment -- @@ -335,9 +371,9 @@
assertEquals("Unexpected test ended", mTestName, test.getTestName());
}

        public void testRunEnded(long elapsedTime, Map<String, String> resultBundle) {
mTestTime = elapsedTime;
            mResultBundle = resultBundle;
}

public void testRunStarted(int testCount) {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index d365248..a0eac97 100644

//Synthetic comment -- @@ -266,7 +266,7 @@
// ignore
}

        public void testRunEnded(long elapsedTime, Map<String, String> resultBundle) {
// ignore
}








