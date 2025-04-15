/*icu4c 4.9 can localize "fil".

Bug: 8023288
Change-Id:I40d26622e135c5ab06c90b1744a87bcf8cb320de*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Locale.java b/luni/src/main/java/java/util/Locale.java
//Synthetic comment -- index 3f1874d..d513bd7 100644

//Synthetic comment -- @@ -400,19 +400,17 @@
return "";
}

        // http://b/8049507 --- frameworks/base should use fil_PH instead of tl_PH.
        // Until then, we're stuck covering their tracks, making it look like they're
        // using "fil" when they're not.
        String localeString = toString();
if (languageCode.equals("tl")) {
            localeString = toNewString("fil", countryCode, variantCode);
}

        String result = ICU.getDisplayLanguageNative(localeString, locale.toString());
if (result == null) { // TODO: do we need to do this, or does ICU do it for us?
            result = ICU.getDisplayLanguageNative(localeString, Locale.getDefault().toString());
}
return result;
}
//Synthetic comment -- @@ -585,10 +583,13 @@
@Override
public final String toString() {
String result = cachedToStringResult;
        if (result == null) {
            result = cachedToStringResult = toNewString(languageCode, countryCode, variantCode);
        }
        return result;
}

    private static String toNewString(String languageCode, String countryCode, String variantCode) {
// The string form of a locale that only has a variant is the empty string.
if (languageCode.length() == 0 && countryCode.length() == 0) {
return "";








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/LocaleTest.java b/luni/src/test/java/libcore/java/util/LocaleTest.java
//Synthetic comment -- index 993c0ec..b0522a3 100644

//Synthetic comment -- @@ -64,15 +64,22 @@
assertEquals("Deutsch", Locale.GERMAN.getDisplayLanguage(Locale.GERMAN));
}

public void test_tl() throws Exception {
        // In jb-mr1, we had a last-minute hack to always return "Filipino" because
        // icu4c 4.8 didn't have any localizations for fil. (http://b/7291355)
Locale tl = new Locale("tl");
Locale tl_PH = new Locale("tl", "PH");
assertEquals("Filipino", tl.getDisplayLanguage(Locale.ENGLISH));
assertEquals("Filipino", tl_PH.getDisplayLanguage(Locale.ENGLISH));
assertEquals("Filipino", tl.getDisplayLanguage(tl));
assertEquals("Filipino", tl_PH.getDisplayLanguage(tl_PH));

        // After the icu4c 4.9 upgrade, we could localize "fil" correctly, though we
        // needed another hack to supply "fil" instead of "tl" to icu4c. (http://b/8023288)
        Locale es_MX = new Locale("es", "MX");
        assertEquals("filipino", tl.getDisplayLanguage(es_MX));
        assertEquals("filipino", tl_PH.getDisplayLanguage(es_MX));
      }

// http://b/3452611; Locale.getDisplayLanguage fails for the obsolete language codes.
public void test_getDisplayName_obsolete() throws Exception {







