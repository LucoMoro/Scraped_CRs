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
import java.util.Map;
import java.util.SortedSet;

//Synthetic comment -- @@ -1145,11 +1146,62 @@
// get the languages from the project.
ProjectResources project = mListener.getProjectResources();

// in cases where the opened file is not linked to a project, this could be null.
if (project != null) {
// now get the languages from the project.
languages = project.getLanguages();

for (String language : languages) {
hasLocale = true;

//Synthetic comment -- @@ -1158,18 +1210,14 @@
// find the matching regions and add them
SortedSet<String> regions = project.getRegions(language);
for (String region : regions) {
                        mLocaleCombo.add(
                                String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
RegionQualifier regionQual = new RegionQualifier(region);
mLocaleList.add(new ResourceQualifier[] { langQual, regionQual });
}

// now the entry for the other regions the language alone
                    if (regions.size() > 0) {
                        mLocaleCombo.add(String.format("%1$s / Other", language)); //$NON-NLS-1$
                    } else {
                        mLocaleCombo.add(String.format("%1$s / Any", language)); //$NON-NLS-1$
                    }
// create a region qualifier that will never be matched by qualified resources.
mLocaleList.add(new ResourceQualifier[] {
langQual,
//Synthetic comment -- @@ -1181,7 +1229,7 @@
// add a locale not present in the project resources. This will let the dev
// tests his/her default values.
if (hasLocale) {
                mLocaleCombo.add("Other");
} else {
mLocaleCombo.add("Any locale");
}







