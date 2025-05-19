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
        String timeString = line.substring(startPos).replaceAll("\\[(.*?)\\]", "$1").trim();
        if (timeString.isEmpty()) {
            Log.e(LOG_TAG, "Time string is empty after processing: " + line);
            return;
        }

        String[] parts = timeString.split("\\s+");
        boolean validNumber = false;

        for (String part : parts) {
            try {
                float timeSeconds = Float.parseFloat(part);
                mTestTime = (long) (timeSeconds * 1000);
                validNumber = true;
                break; // Take the first valid number found
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, "Invalid numeric format from part: " + part + " in time string: " + timeString);
            }
        }

        if (!validNumber) {
            Log.e(LOG_TAG, "No valid time values found in: " + timeString);
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







