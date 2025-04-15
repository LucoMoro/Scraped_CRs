/*Merge remote branch 'korg/froyo' into manualmerge

Conflicts:
	tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java

Change-Id:Id2073fa347aba8227514afb1c4de602c62cd2bf7*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 18301e7..b8f6059 100644

//Synthetic comment -- @@ -92,7 +92,7 @@
Log.i(LOG_TAG, String.format("Checking user agent string %s", actualUserAgentString));
final String patternString = "Mozilla/5\\.0 \\(Linux; U; Android (.+); (\\w+)-(\\w+);" +
" (.+) Build/(.+)\\) AppleWebKit/533\\.1 \\(KHTML, like Gecko\\) Version/4\\.0" +
            "( Mobile)? Safari/533\\.1";
Log.i(LOG_TAG, String.format("Trying to match pattern %s", patternString));
final Pattern userAgentExpr = Pattern.compile(patternString);
Matcher patternMatcher = userAgentExpr.matcher(actualUserAgentString);








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 57b5804..2131965 100644

//Synthetic comment -- @@ -848,6 +848,12 @@
R.drawable.animated, R.drawable.black,
R.drawable.blue, R.drawable.failed,
R.drawable.pass, R.drawable.red,
                R.drawable.failed, R.drawable.pass,
                R.drawable.animated, R.drawable.black,
                R.drawable.blue, R.drawable.red,
                R.drawable.animated, R.drawable.black,
                R.drawable.blue, R.drawable.failed,
                R.drawable.pass, R.drawable.red,
};

private final DataSetObservable mDataSetObservable = new DataSetObservable();







