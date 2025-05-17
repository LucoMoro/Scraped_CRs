//<Beginning of snippet n. 0>


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses the 'raw output mode' results of an instrumentation test run from shell and informs a
 */
mTestRunFinished = true;
// just ignore the remaining data on this line
} else if (line.startsWith(Prefixes.TIME_REPORT)) {
    parseTime(line, Prefixes.TIME_REPORT.length());
} else {
    if (mCurrentValue != null) {
        // this is a value that has wrapped to next line.
/**
 * Parses out and store the elapsed time.
 */
private void parseTime(String line, int startPos) {
    String timeString = line.substring(startPos).replaceAll("[^0-9.]", "");
    if (timeString.isEmpty()) {
        Log.e(LOG_TAG, "Empty time value for input: " + line);
        return;
    }
    try {
        float timeSeconds = Float.parseFloat(timeString);
        mTestTime = (long) (timeSeconds * 1000);
    } catch (NumberFormatException e) {
        Log.e(LOG_TAG, "Unexpected time format for input: " + line + ", sanitized value: " + timeString);
    }
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}

/**
 * Test basic parsing of a test run failure.
 */
public void testRunFailed() {

//<End of snippet n. 1>