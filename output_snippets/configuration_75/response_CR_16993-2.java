//<Beginning of snippet n. 0>


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
* Parses out and store the elapsed time.
*/
private void parseTime(String line, int startPos) {
    if (line.length() <= startPos) {
        Log.e(LOG_TAG, "Input string too short for parsing: " + line);
        return;
    }

    String timeString = line.substring(startPos).trim();
    if (timeString.isEmpty()) {
        Log.e(LOG_TAG, "No time data found after parsing: " + line);
        return;
    }
    
    // Enhanced bracket handling
    String numericString = timeString.replaceAll("[^\\d.]", "").trim();
    if (numericString.isEmpty()) {
        Log.e(LOG_TAG, "No valid numeric data found after parsing: " + line);
        return;
    }
    
    try {
        float timeSeconds = Float.parseFloat(numericString);
        mTestTime = (long) (timeSeconds * 1000);
    } catch (NumberFormatException e) {
        Log.e(LOG_TAG, "Unexpected time format: " + numericString + ". Please check the input format.");
    } catch (Exception e) {
        Log.e(LOG_TAG, "An error occurred while parsing time: " + e.getMessage());
    }
}


//<End of snippet n. 0>