/*Handle instrumentation time output that contains a bracket.

Bug 2975380

Change-Id:I51bcb5b3aaaf320b25619b0b8b4679691c4bff7e*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 04c912b..e9b8bb5 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
* Parses the 'raw output mode' results of an instrumentation test run from shell and informs a
//Synthetic comment -- @@ -254,7 +256,7 @@
mTestRunFinished = true;
// just ignore the remaining data on this line
} else if (line.startsWith(Prefixes.TIME_REPORT)) {
            parseTime(line, Prefixes.TIME_REPORT.length());
} else {
if (mCurrentValue != null) {
// this is a value that has wrapped to next line.
//Synthetic comment -- @@ -475,13 +477,20 @@
/**
* Parses out and store the elapsed time.
*/
    private void parseTime(String line, int startPos) {
        String timeString = line.substring(startPos);
        try {
            float timeSeconds = Float.parseFloat(timeString);
            mTestTime = (long) (timeSeconds * 1000);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Unexpected time format " + timeString);
}
}









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index c971a71..357dcca 100644

//Synthetic comment -- @@ -124,6 +124,16 @@
injectTestString(output.toString());
assertEquals(4900, mTestResult.mTestTime);
}

/**
* Test basic parsing of a test run failure.







