//<Beginning of snippet n. 0>

Locale chLocale = new Locale("de", "CH");
Locale arLocale = new Locale("ar", "AR");
Locale usLocale = new Locale("en", "US");

Locale[] requiredLocales = {usLocale, chLocale, arLocale};
if (!Support_Locale.areLocalesAvailable(requiredLocales) || !Support_Locale.isLocaleException(arLocale)) {
    return;
}

//<End of snippet n. 0>