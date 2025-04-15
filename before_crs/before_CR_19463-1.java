/*Bugfix: Don't run getAndResetTestMetrics in a loop

Change-Id:I40a26966f3c2e6500172017765937fd0090a0c55*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index a986380..6413076 100644

//Synthetic comment -- @@ -445,32 +445,36 @@
}
break;
case StatusCodes.FAILURE:
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.FAILURE, testId,
getTrace(testInfo));

                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
case StatusCodes.ERROR:
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
getTrace(testInfo));
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
case StatusCodes.OK:
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
default:
Log.e(LOG_TAG, "Unknown status code received: " + testInfo.mCode);
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;







