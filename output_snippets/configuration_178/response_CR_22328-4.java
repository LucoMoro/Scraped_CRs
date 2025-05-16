//<Beginning of snippet n. 0>

Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));

final String mobilePatternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
            " (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            " Mobile Safari/533\\.1";

final String tabletPatternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
            " (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            " Safari/533\\.1";

Pattern userAgentExpr;
if (actualUserAgentString.contains("Mobile")) {
    Log.i(LOG_TAG, String.format("Trying to match mobile pattern %s", mobilePatternString));
    userAgentExpr = Pattern.compile(mobilePatternString);
} else {
    Log.i(LOG_TAG, String.format("Trying to match tablet pattern %s", tabletPatternString));
    userAgentExpr = Pattern.compile(tabletPatternString);
}

Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);

//<End of snippet n. 0>