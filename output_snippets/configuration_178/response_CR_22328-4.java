//<Beginning of snippet n. 0>


Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));
final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
" (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            " (?!Mobile)Safari/533\\.1"; // Exclude 'Mobile' for tablet user agents
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);

// Additional logging for match success
if (patternMatcher.find()) {
    Log.i(LOG_TAG, "User agent matched successfully.");
} else {
    Log.w(LOG_TAG, "User agent did not match.");
}

//<End of snippet n. 0>