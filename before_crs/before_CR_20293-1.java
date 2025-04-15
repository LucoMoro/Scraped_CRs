/*add arLocale into requiredLocales

Change-Id:Ief839deb806b5a1f6c908324cff2998092796b93*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldNumberFormatTest.java b/luni/src/test/java/libcore/java/text/OldNumberFormatTest.java
//Synthetic comment -- index fd383aa..24a7422 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
Locale chLocale = new Locale("de", "CH");
// END android-added

        Locale[] requiredLocales = {usLocale, chLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269
return;







