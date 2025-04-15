/*Add summaries by default to ListPreference

Currently when ListPreferences are used in a PreferenceActivity, the summary values are set to the same as the current index in mEntryValue. This patch sets the summary to the corresponding entry in mEntries to aid in localization.

For example a preference may be named "color" with the following attributes in the locale "de":

mEntryValues = { "red", "green", "blue" }
mEntries = { "rot", "grün", "blau" }*/
//Synthetic comment -- diff --git a/core/java/android/preference/ListPreference.java b/core/java/android/preference/ListPreference.java
//Synthetic comment -- index f842d75..f08037b 100644

//Synthetic comment -- @@ -123,6 +123,13 @@
public void setValue(String value) {
mValue = value;

persistString(value);
}








