/*fix failing test to locale specific issue

add ar_AR to locale dependent bug exception list

Change-Id:I40b1a2b4268e8d2db44eff807d9dc8b4aa4bf990*/




//Synthetic comment -- diff --git a/libcore/text/src/test/java/org/apache/harmony/text/tests/java/text/NumberFormatTest.java b/libcore/text/src/test/java/org/apache/harmony/text/tests/java/text/NumberFormatTest.java
//Synthetic comment -- index e949f1d..48c986c 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
Locale chLocale = new Locale("de", "CH");
// END android-added

        Locale[] requiredLocales = {usLocale, arLocale, chLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269
return;







