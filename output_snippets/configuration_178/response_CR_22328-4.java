//<Beginning of snippet n. 0>
Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));
final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
        " (.+) Build/(.+)\\) AppleWebKit/\\d+\\.\\d+ \\(KHTML, like Gecko\\) Version/\\d+\\.\\d+" +
        " Safari/\\d+\\.\\d+(?<!\\bMobile\\b)(?<!\\bPhone\\b)(?!.*\\bTablet\\b)";
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);
if (patternMatcher.find()) {
    Log.i(LOG_TAG, "Matched tablet user agent: " + actualUserAgentString);
} else {
    Log.i(LOG_TAG, "Did not match tablet user agent: " + actualUserAgentString);
    if (actualUserAgentString.contains("Mobile")) {
        Log.i(LOG_TAG, "Reason for mismatch: contains 'Mobile'");
    } else if (actualUserAgentString.contains("Phone")) {
        Log.i(LOG_TAG, "Reason for mismatch: contains 'Phone'");
    } else if (actualUserAgentString.contains("Tablet")) {
        Log.i(LOG_TAG, "Reason for mismatch: contains 'Tablet'");
    } else {
        Log.i(LOG_TAG, "Reason for mismatch: user agent does not meet tablet criteria");
    }
}
//<End of snippet n. 0>