/*Try to use the user's default language by default

The configuration chooser creates an alphabetical list of languages
and regions. When you open a layout the first time, it will just pick
the first item in the drop box - the earliest language in the
alphabet. This is often (usually?) not the native language of the
user, who probably wants to see either their own locale's language, or
the fallback/"Other" language.

This changeset tries to initially pick the user's language when you
open a new layout. It does this by looking up the current locale. I at
first attempted to select the right combobox index, but ran into
problems because the configuration matcher would later reset it to the
first alphabetically matching language.

Instead, this changeset looks up the current locale, then finds the
best match among the available languages and regions, and then adds
this language item at the TOP of the language combobox list. This
means it gets picked by the existing default code, and it's also a
reasonable thing to show first.

Change-Id:Ieb2615fe88520b2901722911b40576f193b58cdb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 5fd953d..7103e3a 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

//Synthetic comment -- @@ -1145,11 +1146,62 @@
// get the languages from the project.
ProjectResources project = mListener.getProjectResources();

            // Format used to show languages/regions in the combo box: language / region
            String localeFormat = "%1$s / %2$s";
            String otherLabel = "Other";
            String anyLabel = "Any";

// in cases where the opened file is not linked to a project, this could be null.
if (project != null) {
// now get the languages from the project.
languages = project.getLanguages();

                Locale locale = Locale.getDefault();
                if (locale != null && languages.size() > 1) {
                    String currentLanguage = locale.getLanguage();
                    String currentRegion = locale.getCountry();
                    boolean found = false;

                    for (String language : languages) {
                        if (language.equals(currentLanguage)) {
                            LanguageQualifier langQual = new LanguageQualifier(language);
                            SortedSet<String> regions = project.getRegions(language);
                            for (String region : regions) {
                                if (region.equals(currentRegion)) {
                                    mLocaleCombo.add(String.format(localeFormat, language, region));
                                    RegionQualifier regionQual = new RegionQualifier(region);
                                    mLocaleList.add(new ResourceQualifier[] {
                                            langQual, regionQual
                                    });
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                // Pick ANY region as long as the language matches
                                String regionName = regions.size() > 0 ? otherLabel : anyLabel;
                                mLocaleCombo.add(String.format(localeFormat, language, regionName));
                                mLocaleList.add(new ResourceQualifier[] {
                                        langQual,
                                        new RegionQualifier(RegionQualifier.FAKE_REGION_VALUE)
                                });
                                found = true;
                            }
                            break;
                        }
                    }

                    if (!found) {
                        // Use the "Other" language as a default
                        mLocaleCombo.add(otherLabel);
                        mLocaleList.add(new ResourceQualifier[] {
                                new LanguageQualifier(LanguageQualifier.FAKE_LANG_VALUE),
                                new RegionQualifier(RegionQualifier.FAKE_REGION_VALUE)
                        });
                    }
                }

for (String language : languages) {
hasLocale = true;

//Synthetic comment -- @@ -1158,18 +1210,14 @@
// find the matching regions and add them
SortedSet<String> regions = project.getRegions(language);
for (String region : regions) {
                        mLocaleCombo.add(String.format(localeFormat, language, region));
RegionQualifier regionQual = new RegionQualifier(region);
mLocaleList.add(new ResourceQualifier[] { langQual, regionQual });
}

// now the entry for the other regions the language alone
                    String regionName = regions.size() > 0 ? otherLabel : anyLabel;
                    mLocaleCombo.add(String.format(localeFormat, language, regionName));
// create a region qualifier that will never be matched by qualified resources.
mLocaleList.add(new ResourceQualifier[] {
langQual,
//Synthetic comment -- @@ -1181,7 +1229,7 @@
// add a locale not present in the project resources. This will let the dev
// tests his/her default values.
if (hasLocale) {
                mLocaleCombo.add(otherLabel);
} else {
mLocaleCombo.add("Any locale");
}







