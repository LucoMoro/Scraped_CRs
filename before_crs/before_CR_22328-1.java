/*Fix CtsWebkitTestCases:WebSettingsTest--testUserAgentString_default fail due to
tablet device remove
"Mobile" from UA string

Change-Id:Ib1c1ee6d275ad13b64fb6f1cb667d7560a940b10*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index de80713..e00daf7 100644

//Synthetic comment -- @@ -92,7 +92,7 @@
Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));
final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
" (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            " Mobile Safari/533\\.1";
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);







