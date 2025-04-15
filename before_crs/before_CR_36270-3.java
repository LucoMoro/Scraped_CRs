/*logcat: Validate regex patterns before creating filters.

Currently, when users create regex based filters, any error in the
regex pattern is logged. When the error is logged, context switches
to the Eclipse console view. As a result, users have to switch back
to the logcat view and fix the error.

This patch validates the text on entry and sets the text color to
be red if the pattern is incorrect. There are no context switches
as a result.

This fixes issue:http://code.google.com/p/android/issues/detail?id=22019Change-Id:I5b571e34c1517b0a78046b56ad83c2aa8632abdb*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java
//Synthetic comment -- index 509449d..7bdd98a 100644

//Synthetic comment -- @@ -15,7 +15,6 @@
*/
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;

import java.util.ArrayList;
//Synthetic comment -- @@ -96,8 +95,6 @@
mAppNamePattern = Pattern.compile(mAppName, getPatternCompileFlags(mAppName));
mCheckAppName = true;
} catch (PatternSyntaxException e) {
                Log.e("LogCatFilter", "Ignoring invalid app name regex.");
                Log.e("LogCatFilter", e.getMessage());
mCheckAppName = false;
}
}
//Synthetic comment -- @@ -107,8 +104,6 @@
mTagPattern = Pattern.compile(mTag, getPatternCompileFlags(mTag));
mCheckTag = true;
} catch (PatternSyntaxException e) {
                Log.e("LogCatFilter", "Ignoring invalid tag regex.");
                Log.e("LogCatFilter", e.getMessage());
mCheckTag = false;
}
}
//Synthetic comment -- @@ -118,8 +113,6 @@
mTextPattern = Pattern.compile(mText, getPatternCompileFlags(mText));
mCheckText = true;
} catch (PatternSyntaxException e) {
                Log.e("LogCatFilter", "Ignoring invalid text regex.");
                Log.e("LogCatFilter", e.getMessage());
mCheckText = false;
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 54381fb..d4da8e4 100644

//Synthetic comment -- @@ -78,6 +78,8 @@
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* LogCatPanel displays a table listing the logcat messages.
//Synthetic comment -- @@ -129,6 +131,12 @@
/** Index of the default filter in the saved filters column. */
private static final int DEFAULT_FILTER_INDEX = 0;

private LogCatReceiver mReceiver;
private IPreferenceStore mPrefStore;

//Synthetic comment -- @@ -542,6 +550,7 @@
mLiveFilterText.addModifyListener(new ModifyListener() {
@Override
public void modifyText(ModifyEvent arg0) {
updateAppliedFilters();
}
});
//Synthetic comment -- @@ -617,6 +626,19 @@
});
}

private void updateFiltersColumn(boolean showFilters) {
if (showFilters) {
mSash.setWeights(WEIGHTS_SHOW_FILTERS);







