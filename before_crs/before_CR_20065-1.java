/*add arLocale into requiredLocales

Change-Id:I2e889df241407ba8d13ca30fb4abcb3c09720d67*/
//Synthetic comment -- diff --git a/libcore/text/src/test/java/org/apache/harmony/text/tests/java/text/NumberFormatTest.java b/libcore/text/src/test/java/org/apache/harmony/text/tests/java/text/NumberFormatTest.java
//Synthetic comment -- index e949f1d..48c986c 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
Locale chLocale = new Locale("de", "CH");
// END android-added

        Locale[] requiredLocales = {usLocale, chLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269
return;







