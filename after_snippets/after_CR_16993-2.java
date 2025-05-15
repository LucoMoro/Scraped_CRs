
//<Beginning of snippet n. 0>


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Parses the 'raw output mode' results of an instrumentation test run from shell and informs a
mTestRunFinished = true;
// just ignore the remaining data on this line
} else if (line.startsWith(Prefixes.TIME_REPORT)) {
            parseTime(line);
} else {
if (mCurrentValue != null) {
// this is a value that has wrapped to next line.
/**
* Parses out and store the elapsed time.
*/
    private void parseTime(String line) {
        final Pattern timePattern = Pattern.compile(String.format("%s\\s*([\\d\\.]+)",
                Prefixes.TIME_REPORT));
        Matcher timeMatcher = timePattern.matcher(line);
        if (timeMatcher.find()) {
            String timeString = timeMatcher.group(1);
            try {
                float timeSeconds = Float.parseFloat(timeString);
                mTestTime = (long) (timeSeconds * 1000);
            } catch (NumberFormatException e) {
                Log.w(LOG_TAG, String.format("Unexpected time format %s", line));
            }
        } else {
            Log.w(LOG_TAG, String.format("Unexpected time format %s", line));
}
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}

/**
     * Test parsing and conversion of time output that contains extra chars.
     */
    public void testTimeParsing_bracket() {
        StringBuilder output = createSuccessTest();
        output.append("Time: 0.001)");
        injectTestString(output.toString());
        assertEquals(1, mTestResult.mTestTime);
    }

    /**
* Test basic parsing of a test run failure.
*/
public void testRunFailed() {

//<End of snippet n. 1>








