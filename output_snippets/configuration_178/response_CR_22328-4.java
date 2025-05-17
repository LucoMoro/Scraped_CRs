//<Beginning of snippet n. 0>

Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));

boolean isTablet = actualUserAgentString.matches(".*Tablet.*");
if (isTablet) {
    actualUserAgentString = actualUserAgentString.replace("Mobile", "");
}

final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
" (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
" Safari/533\\.1";
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);

//<End of snippet n. 0>