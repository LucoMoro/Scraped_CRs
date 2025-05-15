//<Beginning of snippet n. 0>

Locale chLocale = new Locale("de", "CH");
Locale arLocale = new Locale("ar", "AR");
// END android-added

Locale[] requiredLocales = {usLocale, chLocale, arLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269 for ar_AR
return;

//<End of snippet n. 0>