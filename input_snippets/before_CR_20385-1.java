
//<Beginning of snippet n. 0>


Locale chLocale = new Locale("de", "CH");
// END android-added

        Locale[] requiredLocales = {usLocale, chLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269
return;

//<End of snippet n. 0>








