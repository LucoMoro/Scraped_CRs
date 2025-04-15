/*Make test runner parser detect runs with no tests.

Add logic to differeniate between an incomplete test run, and a test run which
completed successfully but has no tests.

Fixes bughttp://b.android.com/7830Change-Id:I54068de73e068faae6e34779b91665f6d1dc1a47*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 2bc5710..98d98b8 100755

//Synthetic comment -- @@ -137,6 +137,9 @@

/** True if start of test has already been reported to listener. */
private boolean mTestStartReported = false;

/** True if test run failure has already been reported to listener. */
private boolean mTestRunFailReported = false;
//Synthetic comment -- @@ -226,6 +229,8 @@
line.startsWith(Prefixes.CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
// just ignore the remaining data on this line
} else if (line.startsWith(Prefixes.TIME_REPORT)) {
parseTime(line, Prefixes.TIME_REPORT.length());
//Synthetic comment -- @@ -470,7 +475,7 @@
@Override
public void done() {
super.done();
        if (!mTestRunFailReported && !mTestStartReported) {
// no results
handleTestRunFailed(NO_TEST_RESULTS_MSG);
} else if (!mTestRunFailReported && mNumTestsExpected > mNumTestsRun) {
//Synthetic comment -- @@ -480,6 +485,11 @@
handleTestRunFailed(message);
} else {
for (ITestRunListener listener : mTestListeners) {
listener.testRunEnded(mTestTime);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index ee227f0..cc5ca97 100644

//Synthetic comment -- @@ -170,23 +170,39 @@
*/
public void testRunAmFailed() {
StringBuilder output = new StringBuilder();
        output.append("usage: am [subcommand] [options]");
        addLineBreak(output);
        output.append("start an Activity: am start [-D] [-W] <INTENT>");
        addLineBreak(output);
        output.append("-D: enable debugging");
        addLineBreak(output);
        output.append("-W: wait for launch to complete");
        addLineBreak(output);
        output.append("start a Service: am startservice <INTENT>");
        addLineBreak(output);
        output.append("Error: Bad component name: wfsdafddfasasdf");

injectTestString(output.toString());

assertEquals(InstrumentationResultParser.NO_TEST_RESULTS_MSG,
mTestResult.mRunFailedMessage);
}

/**
* Builds a common test result using TEST_NAME and TEST_CLASS.
//Synthetic comment -- @@ -218,7 +234,6 @@
*/
private void addStackTrace(StringBuilder output) {
addStatusKey(output, "stack", STACK_TRACE);

}

/**
//Synthetic comment -- @@ -232,6 +247,14 @@
outputBuilder.append(value);
addLineBreak(outputBuilder);
}

/**
* Append line break characters to output







