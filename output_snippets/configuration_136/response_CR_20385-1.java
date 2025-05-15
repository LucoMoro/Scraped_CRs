//<Beginning of snippet n. 0>
Locale chLocale = new Locale("de", "CH");
Locale arLocale = new Locale("ar", "AR"); // Added ar_AR locale
// END android-added

Locale[] requiredLocales = {usLocale, chLocale, arLocale}; // Updated to include ar_AR
if (!Support_Locale.areLocalesAvailable(requiredLocales)) {
// locale dependent test, bug 1943269
return;
//<End of snippet n. 0>