/*Allow ListPreference summary to use entry

Currently when ListPreferences are used in a PreferenceActivity, the summary
values are set to the same as the current index in mEntryValue. This patch
adds the ability for a string substitution to be used in the summary
which points to the corresponding entry in mEntries to aid in
localization.

For example a preference may be named "color" with the following attributes
in the locale "de" (German):

mEntryValues = { "red", "green", "blue" }
mEntries = { "rot", "grün", "blau" }
mSummary = "Die Farbe ist %1$s."

getSummary() returns "Die Farbe ist grün."

Change-Id:Iea169ac3d1c9d6290d853fc7c67a7c4c8a11bb90*/
//Synthetic comment -- diff --git a/core/java/android/preference/ListPreference.java b/core/java/android/preference/ListPreference.java
//Synthetic comment -- index f842d75..f44cbe4 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
private CharSequence[] mEntries;
private CharSequence[] mEntryValues;
private String mValue;
private int mClickedDialogEntryIndex;

public ListPreference(Context context, AttributeSet attrs) {
//Synthetic comment -- @@ -49,8 +50,16 @@
mEntries = a.getTextArray(com.android.internal.R.styleable.ListPreference_entries);
mEntryValues = a.getTextArray(com.android.internal.R.styleable.ListPreference_entryValues);
a.recycle();
}
    
public ListPreference(Context context) {
this(context, null);
}
//Synthetic comment -- @@ -127,6 +136,43 @@
}

/**
* Sets the value to the given index from the entry values.
* 
* @param index The index of the value to set.








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/preference/ListPreferenceTest.java b/core/tests/coretests/src/android/preference/ListPreferenceTest.java
new file mode 100644
//Synthetic comment -- index 0000000..41f8e03

//Synthetic comment -- @@ -0,0 +1,45 @@







