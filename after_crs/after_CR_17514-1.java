/*Fix case where testRunEnded was called twice.

Change-Id:I14be6641c020baf0ad90f2f036e35fb854aed2ab*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 4819fbc..a986380 100644

//Synthetic comment -- @@ -565,10 +565,19 @@
@Override
public void done() {
super.done();
        if (!mTestRunFailReported) {
            handleOutputDone();
        }
    }

    /**
     * Handles the end of the adb session when a test run failure has not been reported yet
     */
    private void handleOutputDone() {
        if (!mTestStartReported && !mTestRunFinished) {
// no results
handleTestRunFailed(NO_TEST_RESULTS_MSG);
        } else if (mNumTestsExpected > mNumTestsRun) {
final String message =
String.format("%s. Expected %d tests, received %d",
INCOMPLETE_RUN_ERR_MSG_PREFIX, mNumTestsExpected, mNumTestsRun);







