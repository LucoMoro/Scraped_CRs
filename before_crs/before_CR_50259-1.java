/*icu4c 4.9 can localize "fil".

Bug: 8023288
Change-Id:I40d26622e135c5ab06c90b1744a87bcf8cb320de*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Locale.java b/luni/src/main/java/java/util/Locale.java
//Synthetic comment -- index 3f1874d..d513bd7 100644

//Synthetic comment -- @@ -400,19 +400,17 @@
return "";
}

        // Last-minute workaround for http://b/7291355 in jb-mr1.
        // This isn't right for all languages, but it's right for en and tl.
        // We should have more CLDR data in a future release, but we'll still
        // probably want to have frameworks/base translate the obsolete tl and
        // tl-rPH locales to fil and fil-rPH at runtime, at which point
        // libcore and icu4c will just do the right thing.
if (languageCode.equals("tl")) {
            return "Filipino";
}

        String result = ICU.getDisplayLanguageNative(toString(), locale.toString());
if (result == null) { // TODO: do we need to do this, or does ICU do it for us?
            result = ICU.getDisplayLanguageNative(toString(), Locale.getDefault().toString());
}
return result;
}
//Synthetic comment -- @@ -585,10 +583,13 @@
@Override
public final String toString() {
String result = cachedToStringResult;
        return (result == null) ? (cachedToStringResult = toNewString()) : result;
}

    private String toNewString() {
// The string form of a locale that only has a variant is the empty string.
if (languageCode.length() == 0 && countryCode.length() == 0) {
return "";








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/LocaleTest.java b/luni/src/test/java/libcore/java/util/LocaleTest.java
//Synthetic comment -- index 993c0ec..b0522a3 100644

//Synthetic comment -- @@ -64,15 +64,22 @@
assertEquals("Deutsch", Locale.GERMAN.getDisplayLanguage(Locale.GERMAN));
}

    // http://b/7291355; Locale.getDisplayLanguage fails for tl in tl in ICU 4.9.
public void test_tl() throws Exception {
Locale tl = new Locale("tl");
Locale tl_PH = new Locale("tl", "PH");
assertEquals("Filipino", tl.getDisplayLanguage(Locale.ENGLISH));
assertEquals("Filipino", tl_PH.getDisplayLanguage(Locale.ENGLISH));
assertEquals("Filipino", tl.getDisplayLanguage(tl));
assertEquals("Filipino", tl_PH.getDisplayLanguage(tl_PH));
    }

// http://b/3452611; Locale.getDisplayLanguage fails for the obsolete language codes.
public void test_getDisplayName_obsolete() throws Exception {







