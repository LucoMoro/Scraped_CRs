/*Use unicode escapes in LocaleManager. DO NOT MERGE

This ensures that when the plugin is built outside of
Eclipse (where the source file is marked as being in UTF-8
encoding), the special characters are handled correctly.

Change-Id:I917d281db96fdd57fe867bd0a2d8da13cf02cb28*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java
//Synthetic comment -- index 0d30011..43c90d9 100644

//Synthetic comment -- @@ -487,7 +487,7 @@

// "gn": Guaraní -> Paraguay
sLanguageToCountry.put("gn", "PY"); //$NON-NLS-1$ //$NON-NLS-2$
         sLanguageNames.put("gn", "Guaraní"); //$NON-NLS-1$

// "gu": Gujarati -> India
sLanguageToCountry.put("gu", "IN"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -708,7 +708,7 @@

// "nb": Norwegian -> Norway
sLanguageToCountry.put("nb", "NO"); //$NON-NLS-1$ //$NON-NLS-2$
         sLanguageNames.put("nb", "Norwegian Bokmål"); //$NON-NLS-1$

// "nd": North Ndebele -> Zimbabwe
sLanguageToCountry.put("nd", "ZW"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -976,7 +976,7 @@

// "yo": Yorùbá -> Nigeria, Togo, Benin
sLanguageToCountry.put("yo", "NG"); //$NON-NLS-1$ //$NON-NLS-2$
         sLanguageNames.put("yo", "Yorùbá"); //$NON-NLS-1$

// "za": Zhuang -> China
sLanguageToCountry.put("za", "CN"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -1005,7 +1005,7 @@
sRegionNames.put("AT", "Austria"); //$NON-NLS-1$
sRegionNames.put("AU", "Australia"); //$NON-NLS-1$
sRegionNames.put("AW", "Aruba"); //$NON-NLS-1$
         sRegionNames.put("AX", "Åland Islands"); //$NON-NLS-1$
sRegionNames.put("AZ", "Azerbaijan"); //$NON-NLS-1$
sRegionNames.put("BA", "Bosnia and Herzegovina"); //$NON-NLS-1$
sRegionNames.put("BB", "Barbados"); //$NON-NLS-1$
//Synthetic comment -- @@ -1016,7 +1016,7 @@
sRegionNames.put("BH", "Bahrain"); //$NON-NLS-1$
sRegionNames.put("BI", "Burundi"); //$NON-NLS-1$
sRegionNames.put("BJ", "Benin"); //$NON-NLS-1$
         sRegionNames.put("BL", "Saint Barthélemy"); //$NON-NLS-1$
sRegionNames.put("BM", "Bermuda"); //$NON-NLS-1$
sRegionNames.put("BN", "Brunei Darussalam"); //$NON-NLS-1$
sRegionNames.put("BO", "Bolivia, Plurinational State of"); //$NON-NLS-1$
//Synthetic comment -- @@ -1034,7 +1034,7 @@
sRegionNames.put("CF", "Central African Republic"); //$NON-NLS-1$
sRegionNames.put("CG", "Congo"); //$NON-NLS-1$
sRegionNames.put("CH", "Switzerland"); //$NON-NLS-1$
         sRegionNames.put("CI", "Côte d'Ivoire"); //$NON-NLS-1$
sRegionNames.put("CK", "Cook Islands"); //$NON-NLS-1$
sRegionNames.put("CL", "Chile"); //$NON-NLS-1$
sRegionNames.put("CM", "Cameroon"); //$NON-NLS-1$
//Synthetic comment -- @@ -1043,7 +1043,7 @@
sRegionNames.put("CR", "Costa Rica"); //$NON-NLS-1$
sRegionNames.put("CU", "Cuba"); //$NON-NLS-1$
sRegionNames.put("CV", "Cape Verde"); //$NON-NLS-1$
         sRegionNames.put("CW", "Curaçao"); //$NON-NLS-1$
sRegionNames.put("CX", "Christmas Island"); //$NON-NLS-1$
sRegionNames.put("CY", "Cyprus"); //$NON-NLS-1$
sRegionNames.put("CZ", "Czech Republic"); //$NON-NLS-1$
//Synthetic comment -- @@ -1178,7 +1178,7 @@
sRegionNames.put("PW", "Palau"); //$NON-NLS-1$
sRegionNames.put("PY", "Paraguay"); //$NON-NLS-1$
sRegionNames.put("QA", "Qatar"); //$NON-NLS-1$
         sRegionNames.put("RE", "Réunion"); //$NON-NLS-1$
sRegionNames.put("RO", "Romania"); //$NON-NLS-1$
sRegionNames.put("RS", "Serbia"); //$NON-NLS-1$
sRegionNames.put("RU", "Russian Federation"); //$NON-NLS-1$
//Synthetic comment -- @@ -1257,7 +1257,7 @@
// in sLanguageToCountry, since they are either extinct or constructed or
// only in literary use:
sLanguageNames.put("pi", "Pali"); //$NON-NLS-1$
        sLanguageNames.put("vo", "Volapük"); //$NON-NLS-1$
sLanguageNames.put("eo", "Esperanto"); //$NON-NLS-1$
sLanguageNames.put("la", "Latin"); //$NON-NLS-1$
sLanguageNames.put("ia", "Interlingua"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManagerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManagerTest.java
//Synthetic comment -- index bee3ec7..4ab6a87 100644

//Synthetic comment -- @@ -201,4 +201,46 @@
}
}
}
}







