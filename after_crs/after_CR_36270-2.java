/*logcat: Validate regex patterns before creating filters.

Currently, when users create regex based filters, any error in the
regex pattern is logged. When the error is logged, context switches
to the Eclipse console view. As a result, users have to switch back
to the logcat view and fix the error.

This patch validates the text on entry and sets the text color to
be red if the pattern is incorrect. There are no context switches
as a result.

Change-Id:I5b571e34c1517b0a78046b56ad83c2aa8632abdb*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java
//Synthetic comment -- index 509449d..7bdd98a 100644

//Synthetic comment -- @@ -15,7 +15,6 @@
*/
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.ArrayList;
//Synthetic comment -- @@ -96,8 +95,6 @@
mAppNamePattern = Pattern.compile(mAppName, getPatternCompileFlags(mAppName));
mCheckAppName = true;
} catch (PatternSyntaxException e) {
mCheckAppName = false;
}
}
//Synthetic comment -- @@ -107,8 +104,6 @@
mTagPattern = Pattern.compile(mTag, getPatternCompileFlags(mTag));
mCheckTag = true;
} catch (PatternSyntaxException e) {
mCheckTag = false;
}
}
//Synthetic comment -- @@ -118,8 +113,6 @@
mTextPattern = Pattern.compile(mText, getPatternCompileFlags(mText));
mCheckText = true;
} catch (PatternSyntaxException e) {
mCheckText = false;
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 54381fb..d4da8e4 100644

//Synthetic comment -- @@ -78,6 +78,8 @@
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
* LogCatPanel displays a table listing the logcat messages.
//Synthetic comment -- @@ -129,6 +131,12 @@
/** Index of the default filter in the saved filters column. */
private static final int DEFAULT_FILTER_INDEX = 0;

    /* Text colors for the filter box */
    private static final Color VALID_FILTER_REGEX_COLOR =
            Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
    private static final Color INVALID_FILTER_REGEX_COLOR =
            Display.getDefault().getSystemColor(SWT.COLOR_RED);

private LogCatReceiver mReceiver;
private IPreferenceStore mPrefStore;

//Synthetic comment -- @@ -542,6 +550,7 @@
mLiveFilterText.addModifyListener(new ModifyListener() {
@Override
public void modifyText(ModifyEvent arg0) {
                updateFilterTextColor();
updateAppliedFilters();
}
});
//Synthetic comment -- @@ -617,6 +626,19 @@
});
}

    /** Sets the foreground color of filter text based on whether the regex is valid. */
    private void updateFilterTextColor() {
        String text = mLiveFilterText.getText();
        Color c;
        try {
            Pattern.compile(text.trim());
            c = VALID_FILTER_REGEX_COLOR;
        } catch (PatternSyntaxException e) {
            c = INVALID_FILTER_REGEX_COLOR;
        }
        mLiveFilterText.setForeground(c);
    }

private void updateFiltersColumn(boolean showFilters) {
if (showFilters) {
mSash.setWeights(WEIGHTS_SHOW_FILTERS);







