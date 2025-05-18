//<Beginning of snippet n. 0>
Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));
final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
            " (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            " Safari/533\\.1(?!.*Mobile.*)";
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);

// Improved logging for matching process
if (patternMatcher.find()) {
    Log.i(LOG_TAG, "User agent string is recognized as a tablet device.");
} else {
    if (actualUserAgentString.contains("Mobile")) {
        Log.w(LOG_TAG, String.format("User agent string does not match tablet pattern (Mobile detected). Actual user agent: %s", actualUserAgentString));
    } else {
        Log.w(LOG_TAG, String.format("User agent string does not match tablet pattern (other factors). Actual user agent: %s", actualUserAgentString));
    }
}
// Additional unit test cases should be implemented separately to cover variations of "Mobile" and other user agent patterns.
//<End of snippet n. 0>