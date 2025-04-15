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








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java
//Synthetic comment -- index 68c08d4..629b0e0 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ddmuilib.logcat;

import com.android.ddmlib.logcat.LogCatFilter;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterData.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterData.java
new file mode 100644
//Synthetic comment -- index 0000000..dbc34d8

//Synthetic comment -- @@ -0,0 +1,81 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

import com.android.ddmlib.logcat.LogCatFilter;
import com.android.ddmlib.logcat.LogCatMessage;

import java.util.List;

public class LogCatFilterData {
    private final LogCatFilter mFilter;

    /** Indicates the number of messages that match this filter, but have not
     * yet been read by the user. This is really metadata about this filter
     * necessary for the UI. If we ever end up needing to store more metadata,
     * then it is probably better to move it out into a separate class. */
    private int mUnreadCount;

    /** Indicates that this filter is transient, and should not be persisted
     * across Eclipse sessions. */
    private boolean mTransient;

    public LogCatFilterData(LogCatFilter f) {
        mFilter = f;

        // By default, all filters are persistent. Transient filters should explicitly
        // mark it so by calling setTransient.
        mTransient = false;
    }

    /**
     * Update the unread count based on new messages received. The unread count
     * is incremented by the count of messages in the received list that will be
     * accepted by this filter.
     * @param newMessages list of new messages.
     */
    public void updateUnreadCount(List<LogCatMessage> newMessages) {
        for (LogCatMessage m : newMessages) {
            if (mFilter.matches(m)) {
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








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterLabelProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterLabelProvider.java
//Synthetic comment -- index 59e236c..fe24ddd 100644

//Synthetic comment -- @@ -15,15 +15,25 @@
*/
package com.android.ddmuilib.logcat;

import com.android.ddmlib.logcat.LogCatFilter;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.util.Map;

/**
* A JFace label provider for the LogCat filters. It expects elements of type
* {@link LogCatFilter}.
*/
public final class LogCatFilterLabelProvider extends LabelProvider implements ITableLabelProvider {
    private Map<LogCatFilter, LogCatFilterData> mFilterData;

    public LogCatFilterLabelProvider(Map<LogCatFilter, LogCatFilterData> filterData) {
        mFilterData = filterData;
    }

@Override
public Image getColumnImage(Object arg0, int arg1) {
return null;
//Synthetic comment -- @@ -42,11 +52,12 @@
}

LogCatFilter f = (LogCatFilter) element;
        LogCatFilterData fd = mFilterData.get(f);

        if (fd != null && fd.getUnreadCount() > 0) {
            return String.format("%s (%d)", f.getName(), fd.getUnreadCount());
} else {
            return f.getName();
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializer.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializer.java
//Synthetic comment -- index 12fbdfa..de35162 100644

//Synthetic comment -- @@ -16,9 +16,11 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Class to help save/restore user created filters.
//Synthetic comment -- @@ -49,14 +51,17 @@
* {@link LogCatFilterSettingsSerializer#decodeFromPreferenceString(String)} for the
* reverse operation.
* @param filters list of filters to save.
     * @param filterData mapping from filter to per filter UI data
* @return an encoded string that can be saved in Eclipse preference store. The encoded string
* is of a list of key:'value' pairs.
*/
    public String encodeToPreferenceString(List<LogCatFilter> filters,
            Map<LogCatFilter, LogCatFilterData> filterData) {
StringBuffer sb = new StringBuffer();

for (LogCatFilter f : filters) {
            LogCatFilterData fd = filterData.get(f);
            if (fd != null && fd.isTransient()) {
// do not persist transient filters
continue;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index c978de7..bda742c 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatFilter;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
//Synthetic comment -- @@ -86,6 +87,8 @@
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

//Synthetic comment -- @@ -161,6 +164,7 @@
private IPreferenceStore mPrefStore;

private List<LogCatFilter> mLogCatFilters;
    private Map<LogCatFilter, LogCatFilterData> mLogCatFilterData;
private int mCurrentSelectedFilterIndex;

private ToolItem mNewFilterToolItem;
//Synthetic comment -- @@ -237,18 +241,25 @@

private void initializeFilters() {
mLogCatFilters = new ArrayList<LogCatFilter>();
        mLogCatFilterData = new ConcurrentHashMap<LogCatFilter, LogCatFilterData>();

/* add default filter matching all messages */
String tag = "";
String text = "";
String pid = "";
String app = "";
        LogCatFilter defaultFilter = new LogCatFilter("All messages (no filters)",
                tag, text, pid, app, LogLevel.VERBOSE);

        mLogCatFilters.add(defaultFilter);
        mLogCatFilterData.put(defaultFilter, new LogCatFilterData(defaultFilter));

/* restore saved filters from prefStore */
List<LogCatFilter> savedFilters = getSavedFilters();
        for (LogCatFilter f: savedFilters) {
            mLogCatFilters.add(f);
            mLogCatFilterData.put(f, new LogCatFilterData(f));
        }
}

private void setupDefaultPreferences() {
//Synthetic comment -- @@ -324,7 +335,7 @@

/* save all filter settings except the first one which is the default */
String e = serializer.encodeToPreferenceString(
                mLogCatFilters.subList(1, mLogCatFilters.size()), mLogCatFilterData);
mPrefStore.setValue(LOGCAT_FILTERS_LIST, e);
}

//Synthetic comment -- @@ -349,7 +360,8 @@

// When switching between devices, existing filter match count should be reset.
for (LogCatFilter f : mLogCatFilters) {
                LogCatFilterData fd = mLogCatFilterData.get(f);
                fd.resetUnreadCount();
}
}

//Synthetic comment -- @@ -479,6 +491,7 @@
LogLevel.getByString(d.getLogLevel()));

mLogCatFilters.add(f);
        mLogCatFilterData.put(f, new LogCatFilterData(f));
mFiltersTableViewer.refresh();

/* select the newly added entry */
//Synthetic comment -- @@ -490,8 +503,7 @@
}

private void addNewFilter() {
        addNewFilter("", "", "", "", LogLevel.VERBOSE);
}

private void deleteSelectedFilter() {
//Synthetic comment -- @@ -501,7 +513,10 @@
return;
}

        LogCatFilter f = mLogCatFilters.get(selectedIndex);
mLogCatFilters.remove(selectedIndex);
        mLogCatFilterData.remove(f);

mFiltersTableViewer.refresh();
mFiltersTableViewer.getTable().setSelection(selectedIndex - 1);

//Synthetic comment -- @@ -552,6 +567,10 @@
if (f == null) {
f = createTransientAppFilter(appName);
mLogCatFilters.add(f);

            LogCatFilterData fd = new LogCatFilterData(f);
            fd.setTransient();
            mLogCatFilterData.put(f, fd);
}

selectFilterAt(mLogCatFilters.indexOf(f));
//Synthetic comment -- @@ -559,7 +578,8 @@

private LogCatFilter findTransientAppFilter(String appName) {
for (LogCatFilter f : mLogCatFilters) {
            LogCatFilterData fd = mLogCatFilterData.get(f);
            if (fd != null && fd.isTransient() && f.getAppName().equals(appName)) {
return f;
}
}
//Synthetic comment -- @@ -573,7 +593,6 @@
"",
appName,
LogLevel.VERBOSE);
return f;
}

//Synthetic comment -- @@ -595,7 +614,7 @@

mFiltersTableViewer = new TableViewer(table);
mFiltersTableViewer.setContentProvider(new LogCatFilterContentProvider());
        mFiltersTableViewer.setLabelProvider(new LogCatFilterLabelProvider(mLogCatFilterData));
mFiltersTableViewer.setInput(mLogCatFilters);

mFiltersTableViewer.getTable().addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -672,6 +691,7 @@
if (mReceiver != null) {
mReceiver.clearMessages();
refreshLogCatTable();
                    resetUnreadCountForAllFilters();

// the filters view is not cleared unless the filters are re-applied.
updateAppliedFilters();
//Synthetic comment -- @@ -1092,13 +1112,15 @@

mCurrentSelectedFilterIndex = idx;

        resetUnreadCountForAllFilters();
updateFiltersToolBar();
updateAppliedFilters();
}

    private void resetUnreadCountForAllFilters() {
        for (LogCatFilterData fd: mLogCatFilterData.values()) {
            fd.resetUnreadCount();
        }
refreshFiltersTable();
}

//Synthetic comment -- @@ -1143,6 +1165,8 @@
@Override
public void bufferChanged(List<LogCatMessage> addedMessages,
List<LogCatMessage> deletedMessages) {
        updateUnreadCount(addedMessages);
        refreshFiltersTable();

synchronized (mLogBuffer) {
addedMessages = applyCurrentFilters(addedMessages);
//Synthetic comment -- @@ -1153,8 +1177,6 @@
}

refreshLogCatTable();
}

private void reloadLogBuffer() {
//Synthetic comment -- @@ -1185,7 +1207,9 @@
/* no need to update unread count for currently selected filter */
continue;
}
            LogCatFilter f = mLogCatFilters.get(i);
            LogCatFilterData fd = mLogCatFilterData.get(f);
            fd.updateUnreadCount(receivedMessages);
}
}

//Synthetic comment -- @@ -1329,7 +1353,9 @@
Display.getDefault().asyncExec(new Runnable() {
@Override
public void run() {
                    if (!mTable.isDisposed()) {
                        startScrollBarMonitor(mTable.getVerticalBar());
                    }
}
});
}
//Synthetic comment -- @@ -1373,7 +1399,9 @@

/** Scroll to the last line. */
private void scrollToLatestLog() {
        if (!mTable.isDisposed()) {
            mTable.setTopIndex(mTable.getItemCount() - 1);
        }
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializerTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterSettingsSerializerTest.java
//Synthetic comment -- index 0fc0c76..e6c0e76 100644

//Synthetic comment -- @@ -16,8 +16,10 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
//Synthetic comment -- @@ -34,7 +36,8 @@
LogLevel.ERROR);

LogCatFilterSettingsSerializer serializer = new LogCatFilterSettingsSerializer();
        String s = serializer.encodeToPreferenceString(Arrays.asList(fs),
                new HashMap<LogCatFilter, LogCatFilterData>());
List<LogCatFilter> decodedFiltersList = serializer.decodeFromPreferenceString(s);

assertEquals(1, decodedFiltersList.size());
//Synthetic comment -- @@ -57,10 +60,14 @@
"123",                      //$NON-NLS-1$
"TestAppName.*",            //$NON-NLS-1$
LogLevel.ERROR);
        LogCatFilterData fd = new LogCatFilterData(fs);
        fd.setTransient();
        HashMap<LogCatFilter, LogCatFilterData> fdMap =
                new HashMap<LogCatFilter, LogCatFilterData>();
        fdMap.put(fs, fd);

LogCatFilterSettingsSerializer serializer = new LogCatFilterSettingsSerializer();
        String s = serializer.encodeToPreferenceString(Arrays.asList(fs), fdMap);
List<LogCatFilter> decodedFiltersList = serializer.decodeFromPreferenceString(s);

assertEquals(0, decodedFiltersList.size());








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java
deleted file mode 100644
//Synthetic comment -- index 98b186e..0000000

//Synthetic comment -- @@ -1,164 +0,0 @@







