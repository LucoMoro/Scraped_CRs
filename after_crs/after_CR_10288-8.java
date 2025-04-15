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
    private String mSummary;
private int mClickedDialogEntryIndex;

public ListPreference(Context context, AttributeSet attrs) {
//Synthetic comment -- @@ -49,8 +50,16 @@
mEntries = a.getTextArray(com.android.internal.R.styleable.ListPreference_entries);
mEntryValues = a.getTextArray(com.android.internal.R.styleable.ListPreference_entryValues);
a.recycle();

        /* Retrieve the Preference summary attribute since it's private
         * in the Preference class.
         */
        a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.Preference, 0, 0);
        mSummary = a.getString(com.android.internal.R.styleable.Preference_summary);
        a.recycle();
}

public ListPreference(Context context) {
this(context, null);
}
//Synthetic comment -- @@ -127,6 +136,43 @@
}

/**
     * Returns the summary of this ListPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @Override
    public CharSequence getSummary() {
        final CharSequence entry = getEntry();
        if (mSummary == null || entry == null) {
            return super.getSummary();
        } else {
            return String.format(mSummary, entry);
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     * If the summary has a
     * {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place when it's retrieved.
     *
     * @param summary The summary for the preference.
     */
    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (summary == null && mSummary != null) {
            mSummary = null;
        } else if (summary != null && !summary.equals(mSummary)) {
            mSummary = summary.toString();
        }
    }

    /**
* Sets the value to the given index from the entry values.
* 
* @param index The index of the value to set.








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/preference/ListPreferenceTest.java b/core/tests/coretests/src/android/preference/ListPreferenceTest.java
new file mode 100644
//Synthetic comment -- index 0000000..41f8e03

//Synthetic comment -- @@ -0,0 +1,45 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.preference;

import android.preference.ListPreference;
import android.test.AndroidTestCase;

public class ListPreferenceTest extends AndroidTestCase {
    public void testListPreferenceSummaryFromEntries() {
        String[] entries = { "one", "two", "three" };
        String[] entryValues = { "1" , "2", "3" };
        ListPreference lp = new ListPreference(getContext());
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);

        lp.setValue(entryValues[1]);
        assertTrue(lp.getSummary() == null);

        lp.setSummary("%1$s");
        assertEquals(entries[1], lp.getSummary());

        lp.setValue(entryValues[2]);
        assertEquals(entries[2], lp.getSummary());

        lp.setSummary(null);
        assertTrue(lp.getSummary() == null);

        lp.setSummary("The color is %1$s");
        assertEquals("The color is " + entries[2], lp.getSummary());
    }
}







