/*Move UI data in LogCatFilter to a separate class

There were a couple of pieces of UI data (unread count & flag
indicating whether a filter is transient) associated with a filter.
This CL moves both of them out into a separate class. The panel
maintains a mapping from a filter to its associated UI data.

The core filter class has been moved out into ddmlib.

Change-Id:I325176c33094d583c0ef9abe890e5462aeeb4945*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java
deleted file mode 100644
//Synthetic comment -- index 3024978..0000000

//Synthetic comment -- @@ -1,280 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A Filter for logcat messages. A filter can be constructed to match
 * different fields of a logcat message. It can then be queried to see if
 * a message matches the filter's settings.
 */
public final class LogCatFilter {
    private static final String PID_KEYWORD = "pid:";   //$NON-NLS-1$
    private static final String APP_KEYWORD = "app:";   //$NON-NLS-1$
    private static final String TAG_KEYWORD = "tag:";   //$NON-NLS-1$
    private static final String TEXT_KEYWORD = "text:"; //$NON-NLS-1$

    private final String mName;
    private final String mTag;
    private final String mText;
    private final String mPid;
    private final String mAppName;
    private final LogLevel mLogLevel;

    /** Indicates the number of messages that match this filter, but have not
     * yet been read by the user. This is really metadata about this filter
     * necessary for the UI. If we ever end up needing to store more metadata,
     * then it is probably better to move it out into a separate class. */
    private int mUnreadCount;

    /** Indicates that this filter is transient, and should not be persisted
     * across Eclipse sessions. */
    private boolean mTransient;

    private boolean mCheckPid;
    private boolean mCheckAppName;
    private boolean mCheckTag;
    private boolean mCheckText;

    private Pattern mAppNamePattern;
    private Pattern mTagPattern;
    private Pattern mTextPattern;

    /**
     * Construct a filter with the provided restrictions for the logcat message. All the text
     * fields accept Java regexes as input, but ignore invalid regexes. Filters are saved and
     * restored across Eclipse sessions unless explicitly marked transient using
     * {@link LogCatFilter#setTransient}.
     * @param name name for the filter
     * @param tag value for the logcat message's tag field.
     * @param text value for the logcat message's text field.
     * @param pid value for the logcat message's pid field.
     * @param appName value for the logcat message's app name field.
     * @param logLevel value for the logcat message's log level. Only messages of
     * higher priority will be accepted by the filter.
     */
    public LogCatFilter(String name, String tag, String text, String pid, String appName,
            LogLevel logLevel) {
        mName = name.trim();
        mTag = tag.trim();
        mText = text.trim();
        mPid = pid.trim();
        mAppName = appName.trim();
        mLogLevel = logLevel;

        mUnreadCount = 0;

        // By default, all filters are persistent. Transient filters should explicitly
        // mark it so by calling setTransient.
        mTransient = false;

        mCheckPid = mPid.length() != 0;

        if (mAppName.length() != 0) {
            try {
                mAppNamePattern = Pattern.compile(mAppName, getPatternCompileFlags(mAppName));
                mCheckAppName = true;
            } catch (PatternSyntaxException e) {
                mCheckAppName = false;
            }
        }

        if (mTag.length() != 0) {
            try {
                mTagPattern = Pattern.compile(mTag, getPatternCompileFlags(mTag));
                mCheckTag = true;
            } catch (PatternSyntaxException e) {
                mCheckTag = false;
            }
        }

        if (mText.length() != 0) {
            try {
                mTextPattern = Pattern.compile(mText, getPatternCompileFlags(mText));
                mCheckText = true;
            } catch (PatternSyntaxException e) {
                mCheckText = false;
            }
        }
    }

    /**
     * Obtain the flags to pass to {@link Pattern#compile(String, int)}. This method
     * tries to figure out whether case sensitive matching should be used. It is based on
     * the following heuristic: if the regex has an upper case character, then the match
     * will be case sensitive. Otherwise it will be case insensitive.
     */
    private int getPatternCompileFlags(String regex) {
        for (char c : regex.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return 0;
            }
        }

        return Pattern.CASE_INSENSITIVE;
    }

    /**
     * Construct a list of {@link LogCatFilter} objects by decoding the query.
     * @param query encoded search string. The query is simply a list of words (can be regexes)
     * a user would type in a search bar. These words are searched for in the text field of
     * each collected logcat message. To search in a different field, the word could be prefixed
     * with a keyword corresponding to the field name. Currently, the following keywords are
     * supported: "pid:", "tag:" and "text:". Invalid regexes are ignored.
     * @param minLevel minimum log level to match
     * @return list of filter settings that fully match the given query
     */
    public static List<LogCatFilter> fromString(String query, LogLevel minLevel) {
        List<LogCatFilter> filterSettings = new ArrayList<LogCatFilter>();

        for (String s : query.trim().split(" ")) {
            String tag = "";
            String text = "";
            String pid = "";
            String app = "";

            if (s.startsWith(PID_KEYWORD)) {
                pid = s.substring(PID_KEYWORD.length());
            } else if (s.startsWith(APP_KEYWORD)) {
                app = s.substring(APP_KEYWORD.length());
            } else if (s.startsWith(TAG_KEYWORD)) {
                tag = s.substring(TAG_KEYWORD.length());
            } else {
                if (s.startsWith(TEXT_KEYWORD)) {
                    text = s.substring(TEXT_KEYWORD.length());
                } else {
                    text = s;
                }
            }
            filterSettings.add(new LogCatFilter("livefilter-" + s,
                    tag, text, pid, app, minLevel));
        }

        return filterSettings;
    }

    public String getName() {
        return mName;
    }

    public String getTag() {
        return mTag;
    }

    public String getText() {
        return mText;
    }

    public String getPid() {
        return mPid;
    }

    public String getAppName() {
        return mAppName;
    }

    public LogLevel getLogLevel() {
        return mLogLevel;
    }

    /**
     * Check whether a given message will make it through this filter.
     * @param m message to check
     * @return true if the message matches the filter's conditions.
     */
    public boolean matches(LogCatMessage m) {
        /* filter out messages of a lower priority */
        if (m.getLogLevel().getPriority() < mLogLevel.getPriority()) {
            return false;
        }

        /* if pid filter is enabled, filter out messages whose pid does not match
         * the filter's pid */
        if (mCheckPid && !m.getPid().equals(mPid)) {
            return false;
        }

        /* if app name filter is enabled, filter out messages not matching the app name */
        if (mCheckAppName) {
            Matcher matcher = mAppNamePattern.matcher(m.getAppName());
            if (!matcher.find()) {
                return false;
            }
        }

        /* if tag filter is enabled, filter out messages not matching the tag */
        if (mCheckTag) {
            Matcher matcher = mTagPattern.matcher(m.getTag());
            if (!matcher.find()) {
                return false;
            }
        }

        if (mCheckText) {
            Matcher matcher = mTextPattern.matcher(m.getMessage());
            if (!matcher.find()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Update the unread count based on new messages received. The unread count
     * is incremented by the count of messages in the received list that will be
     * accepted by this filter.
     * @param newMessages list of new messages.
     */
    public void updateUnreadCount(List<LogCatMessage> newMessages) {
        for (LogCatMessage m : newMessages) {
            if (matches(m)) {
                mUnreadCount++;
            }
        }
    }

    /**
     * Reset count of unread messages.
     */
    public void resetUnreadCount() {
        mUnreadCount = 0;
    }

    /**
     * Get current value for the unread message counter.
     */
    public int getUnreadCount() {
        return mUnreadCount;
    }

    /** Make this filter transient: It will not be persisted across sessions. */
    public void setTransient() {
        mTransient = true;
    }

    public boolean isTransient() {
        return mTransient;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java
//Synthetic comment -- index 68c08d4..629b0e0 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ddmuilib.logcat;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterData.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterData.java
new file mode 100644
//Synthetic comment -- index 0000000..dbc34d8

//Synthetic comment -- @@ -0,0 +1,81 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterLabelProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterLabelProvider.java
//Synthetic comment -- index 59e236c..fe24ddd 100644

//Synthetic comment -- @@ -15,15 +15,25 @@
*/
package com.android.ddmuilib.logcat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
* A JFace label provider for the LogCat filters. It expects elements of type
* {@link LogCatFilter}.
*/
public final class LogCatFilterLabelProvider extends LabelProvider implements ITableLabelProvider {
@Override
public Image getColumnImage(Object arg0, int arg1) {
return null;
//Synthetic comment -- @@ -42,11 +52,12 @@
}

LogCatFilter f = (LogCatFilter) element;

        if (f.getUnreadCount() == 0) {
            return f.getName();
} else {
            return String.format("%s (%d)", f.getName(), f.getUnreadCount());
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializer.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializer.java
//Synthetic comment -- index 12fbdfa..de35162 100644

//Synthetic comment -- @@ -16,9 +16,11 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
* Class to help save/restore user created filters.
//Synthetic comment -- @@ -49,14 +51,17 @@
* {@link LogCatFilterSettingsSerializer#decodeFromPreferenceString(String)} for the
* reverse operation.
* @param filters list of filters to save.
* @return an encoded string that can be saved in Eclipse preference store. The encoded string
* is of a list of key:'value' pairs.
*/
    public String encodeToPreferenceString(List<LogCatFilter> filters) {
StringBuffer sb = new StringBuffer();

for (LogCatFilter f : filters) {
            if (f.isTransient()) {
// do not persist transient filters
continue;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index c978de7..80bd004 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
//Synthetic comment -- @@ -86,6 +87,8 @@
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

//Synthetic comment -- @@ -161,6 +164,7 @@
private IPreferenceStore mPrefStore;

private List<LogCatFilter> mLogCatFilters;
private int mCurrentSelectedFilterIndex;

private ToolItem mNewFilterToolItem;
//Synthetic comment -- @@ -237,18 +241,25 @@

private void initializeFilters() {
mLogCatFilters = new ArrayList<LogCatFilter>();

/* add default filter matching all messages */
String tag = "";
String text = "";
String pid = "";
String app = "";
        mLogCatFilters.add(new LogCatFilter("All messages (no filters)",
                tag, text, pid, app, LogLevel.VERBOSE));

/* restore saved filters from prefStore */
List<LogCatFilter> savedFilters = getSavedFilters();
        mLogCatFilters.addAll(savedFilters);
}

private void setupDefaultPreferences() {
//Synthetic comment -- @@ -324,7 +335,7 @@

/* save all filter settings except the first one which is the default */
String e = serializer.encodeToPreferenceString(
                mLogCatFilters.subList(1, mLogCatFilters.size()));
mPrefStore.setValue(LOGCAT_FILTERS_LIST, e);
}

//Synthetic comment -- @@ -349,7 +360,8 @@

// When switching between devices, existing filter match count should be reset.
for (LogCatFilter f : mLogCatFilters) {
                f.resetUnreadCount();
}
}

//Synthetic comment -- @@ -479,6 +491,7 @@
LogLevel.getByString(d.getLogLevel()));

mLogCatFilters.add(f);
mFiltersTableViewer.refresh();

/* select the newly added entry */
//Synthetic comment -- @@ -490,8 +503,7 @@
}

private void addNewFilter() {
        addNewFilter("", "", "",
                "", LogLevel.VERBOSE);
}

private void deleteSelectedFilter() {
//Synthetic comment -- @@ -501,7 +513,10 @@
return;
}

mLogCatFilters.remove(selectedIndex);
mFiltersTableViewer.refresh();
mFiltersTableViewer.getTable().setSelection(selectedIndex - 1);

//Synthetic comment -- @@ -552,6 +567,10 @@
if (f == null) {
f = createTransientAppFilter(appName);
mLogCatFilters.add(f);
}

selectFilterAt(mLogCatFilters.indexOf(f));
//Synthetic comment -- @@ -559,7 +578,8 @@

private LogCatFilter findTransientAppFilter(String appName) {
for (LogCatFilter f : mLogCatFilters) {
            if (f.isTransient() && f.getAppName().equals(appName)) {
return f;
}
}
//Synthetic comment -- @@ -573,7 +593,6 @@
"",
appName,
LogLevel.VERBOSE);
        f.setTransient();
return f;
}

//Synthetic comment -- @@ -595,7 +614,7 @@

mFiltersTableViewer = new TableViewer(table);
mFiltersTableViewer.setContentProvider(new LogCatFilterContentProvider());
        mFiltersTableViewer.setLabelProvider(new LogCatFilterLabelProvider());
mFiltersTableViewer.setInput(mLogCatFilters);

mFiltersTableViewer.getTable().addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -1092,13 +1111,15 @@

mCurrentSelectedFilterIndex = idx;

        resetUnreadCountForSelectedFilter();
updateFiltersToolBar();
updateAppliedFilters();
}

    private void resetUnreadCountForSelectedFilter() {
        mLogCatFilters.get(mCurrentSelectedFilterIndex).resetUnreadCount();
refreshFiltersTable();
}

//Synthetic comment -- @@ -1143,6 +1164,8 @@
@Override
public void bufferChanged(List<LogCatMessage> addedMessages,
List<LogCatMessage> deletedMessages) {

synchronized (mLogBuffer) {
addedMessages = applyCurrentFilters(addedMessages);
//Synthetic comment -- @@ -1153,8 +1176,6 @@
}

refreshLogCatTable();
        updateUnreadCount(addedMessages);
        refreshFiltersTable();
}

private void reloadLogBuffer() {
//Synthetic comment -- @@ -1185,7 +1206,9 @@
/* no need to update unread count for currently selected filter */
continue;
}
            mLogCatFilters.get(i).updateUnreadCount(receivedMessages);
}
}

//Synthetic comment -- @@ -1329,7 +1352,9 @@
Display.getDefault().asyncExec(new Runnable() {
@Override
public void run() {
                    startScrollBarMonitor(mTable.getVerticalBar());
}
});
}
//Synthetic comment -- @@ -1373,7 +1398,9 @@

/** Scroll to the last line. */
private void scrollToLatestLog() {
        mTable.setTopIndex(mTable.getItemCount() - 1);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializerTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializerTest.java
//Synthetic comment -- index 0fc0c76..e6c0e76 100644

//Synthetic comment -- @@ -16,8 +16,10 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
//Synthetic comment -- @@ -34,7 +36,8 @@
LogLevel.ERROR);

LogCatFilterSettingsSerializer serializer = new LogCatFilterSettingsSerializer();
        String s = serializer.encodeToPreferenceString(Arrays.asList(fs));
List<LogCatFilter> decodedFiltersList = serializer.decodeFromPreferenceString(s);

assertEquals(1, decodedFiltersList.size());
//Synthetic comment -- @@ -57,10 +60,14 @@
"123",                      //$NON-NLS-1$
"TestAppName.*",            //$NON-NLS-1$
LogLevel.ERROR);
        fs.setTransient();

LogCatFilterSettingsSerializer serializer = new LogCatFilterSettingsSerializer();
        String s = serializer.encodeToPreferenceString(Arrays.asList(fs));
List<LogCatFilter> decodedFiltersList = serializer.decodeFromPreferenceString(s);

assertEquals(0, decodedFiltersList.size());








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java
deleted file mode 100644
//Synthetic comment -- index 98b186e..0000000

//Synthetic comment -- @@ -1,164 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;

import java.util.List;

import junit.framework.TestCase;

public class LogCatFilterTest extends TestCase {
    public void testFilterByLogLevel() {
        LogCatFilter filter = new LogCatFilter("",
                "", "", "", "", LogLevel.DEBUG);

        /* filter message below filter's log level */
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "", "", "");
        assertEquals(false, filter.matches(msg));

        /* do not filter message above filter's log level */
        msg = new LogCatMessage(LogLevel.ERROR,
                "", "", "", "", "", "");
        assertEquals(true, filter.matches(msg));
    }

    public void testFilterByPid() {
        LogCatFilter filter = new LogCatFilter("",
                "", "", "123", "", LogLevel.VERBOSE);

        /* show message with pid matching filter */
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "123", "", "", "", "", "");
        assertEquals(true, filter.matches(msg));

        /* don't show message with pid not matching filter */
        msg = new LogCatMessage(LogLevel.VERBOSE,
                "12", "", "", "", "", "");
        assertEquals(false, filter.matches(msg));
    }

    public void testFilterByAppNameRegex() {
        LogCatFilter filter = new LogCatFilter("",
                "", "", "", "dalvik.*", LogLevel.VERBOSE);

        /* show message with pid matching filter */
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "dalvikvm1", "", "", "");
        assertEquals(true, filter.matches(msg));

        /* don't show message with pid not matching filter */
        msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "system", "", "", "");
        assertEquals(false, filter.matches(msg));
    }

    public void testFilterByTagRegex() {
        LogCatFilter filter = new LogCatFilter("",
                "tag.*", "", "", "", LogLevel.VERBOSE);

        /* show message with tag matching filter */
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "tag123", "", "");
        assertEquals(true, filter.matches(msg));

        msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "ta123", "", "");
        assertEquals(false, filter.matches(msg));
    }

    public void testFilterByTextRegex() {
        LogCatFilter filter = new LogCatFilter("",
                "", "text.*", "", "", LogLevel.VERBOSE);

        /* show message with text matching filter */
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "", "", "text123");
        assertEquals(true, filter.matches(msg));

        msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "", "", "te123");
        assertEquals(false, filter.matches(msg));
    }

    public void testMatchingText() {
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "", "",                        //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "message with word1 and word2");       //$NON-NLS-1$
        assertEquals(true, search("word1 with", msg)); //$NON-NLS-1$
        assertEquals(true, search("text:w.* ", msg));  //$NON-NLS-1$
        assertEquals(false, search("absent", msg));    //$NON-NLS-1$
    }

    public void testTagKeyword() {
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "tag", "",                     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "sample message");                     //$NON-NLS-1$
        assertEquals(false, search("t.*", msg));       //$NON-NLS-1$
        assertEquals(true, search("tag:t.*", msg));    //$NON-NLS-1$
    }

    public void testPidKeyword() {
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "123", "", "", "", "",                     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "sample message");                     //$NON-NLS-1$
        assertEquals(false, search("123", msg));       //$NON-NLS-1$
        assertEquals(true, search("pid:123", msg));    //$NON-NLS-1$
    }

    public void testAppNameKeyword() {
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "dalvik", "", "",                  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "sample message");                     //$NON-NLS-1$
        assertEquals(false, search("dalv.*", msg));    //$NON-NLS-1$
        assertEquals(true, search("app:dal.*k", msg)); //$NON-NLS-1$
    }

    public void testCaseSensitivity() {
        LogCatMessage msg = new LogCatMessage(LogLevel.VERBOSE,
                "", "", "", "", "",
                "Sample message");

        // if regex has an upper case character, it should be
        // treated as a case sensitive search
        assertEquals(false, search("Message", msg));

        // if regex is all lower case, then it should be a
        // case insensitive search
        assertEquals(true, search("sample", msg));
    }

    /**
     * Helper method: search if the query string matches the message.
     * @param query words to search for
     * @param message text to search in
     * @return true if the encoded query is present in message
     */
    private boolean search(String query, LogCatMessage message) {
        List<LogCatFilter> filters = LogCatFilter.fromString(query,
                LogLevel.VERBOSE);

        /* all filters have to match for the query to match */
        for (LogCatFilter f : filters) {
            if (!f.matches(message)) {
                return false;
            }
        }
        return true;
    }
}







