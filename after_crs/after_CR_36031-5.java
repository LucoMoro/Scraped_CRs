/*logcat: Remove JFace TableViewer and use SWT Table directly

This patch fixes a bunch of outstanding issues related to
scrolling in the presence of a full buffer.

Currently, the logbuffer is provided as the input model to the
TableViewer, and ViewerFilter's are used to filter the data.

This patch removes the JFace toolkit and directly works on the
SWT Table. When log messages arrive, rather than refreshing the
entire table, we can now just delete the TableItems corresponding
to the logs that were pushed out, and add new TableItems for the
incoming logs.

At steady state, this implementation performs far less work than
the previous implementation. However, during startup, this
implementation will perform more work since it does not use the
SWT.VIRTUAL bit (as all TableItems are created anyway).

Also, zebra striping has been removed to avoid appearance of flicker
when scroll lock is on.

Auto scroll lock behavior has been removed, and scroll lock button
behaves exactly like the scroll lock button in an Eclipse console.

Change-Id:Ic14487f7ad41338a581aed0ba2d85d292a584950*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageEventListener.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java
similarity index 60%
rename from ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageEventListener.java
rename to ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java
//Synthetic comment -- index 2caf50d..96a22cc 100644

//Synthetic comment -- @@ -19,11 +19,13 @@
import java.util.List;

/**
 * Listeners interested in changes in the log cat buffer should implement this interface.
*/
public interface ILogCatBufferChangeListener {
    /**
     * Called when the logcat buffer changes.
     * @param addedMessages list of messages that were added to the logcat buffer
     * @param deletedMessages list of messages that were removed from the logcat buffer
*/
    void bufferChanged(List<LogCatMessage> addedMessages, List<LogCatMessage> deletedMessages);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java
//Synthetic comment -- index 164f484..68c08d4 100644

//Synthetic comment -- @@ -39,10 +39,6 @@
*/
@Override
public Object[] getElements(Object model) {
        return ((List<?>) model).toArray();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsDialog.java
//Synthetic comment -- index f68ee05..39b3fa9 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
* on the dialog is valid or not. If it is not valid, the message
* field stores the reason why it isn't.
*/
    private static final class DialogStatus {
final boolean valid;
final String message;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageContentProvider.java
deleted file mode 100644
//Synthetic comment -- index bd7b520..0000000

//Synthetic comment -- @@ -1,43 +0,0 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageLabelProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageLabelProvider.java
deleted file mode 100644
//Synthetic comment -- index 1d83a9c..0000000

//Synthetic comment -- @@ -1,144 +0,0 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java
//Synthetic comment -- index 0d0e3c2..080dbc1 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//Synthetic comment -- @@ -33,7 +35,6 @@

private int mFifoSize;
private BlockingQueue<LogCatMessage> mQ;

/**
* Construct an empty message list.
//Synthetic comment -- @@ -43,7 +44,6 @@
mFifoSize = maxMessages;

mQ = new ArrayBlockingQueue<LogCatMessage>(mFifoSize);
}

/**
//Synthetic comment -- @@ -64,8 +64,6 @@
mQ.offer(curMessages[i]);
}
}
}

/**
//Synthetic comment -- @@ -73,17 +71,30 @@
* message will be popped off of it.
* @param m log to be inserted
*/
    public synchronized void appendMessages(final List<LogCatMessage> messages) {
        ensureSpace(messages.size());
        for (LogCatMessage m: messages) {
            mQ.offer(m);
}
}

/**
     * Ensure that there is sufficient space for given number of messages.
     * @return list of messages that were deleted to create additional space.
     */
    public synchronized List<LogCatMessage> ensureSpace(int messageCount) {
        List<LogCatMessage> l = new ArrayList<LogCatMessage>(messageCount);

        while (mQ.remainingCapacity() < messageCount) {
            l.add(mQ.poll());
        }

        return l;
    }

    /**
     * Returns the number of additional elements that this queue can
     * ideally (in the absence of memory or resource constraints)
* accept without blocking.
* @return the remaining capacity
*/
//Synthetic comment -- @@ -91,25 +102,13 @@
return mQ.remainingCapacity();
}

    /** Clear all messages in the list. */
public synchronized void clear() {
mQ.clear();
}

    /** Obtain a copy of the message list. */
    public synchronized List<LogCatMessage> getAllMessages() {
        return new ArrayList<LogCatMessage>(mQ);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 7aa0328..e17faf5 100644

//Synthetic comment -- @@ -30,12 +30,7 @@
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -50,6 +45,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
//Synthetic comment -- @@ -63,7 +59,6 @@
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
//Synthetic comment -- @@ -83,7 +78,7 @@
* LogCatPanel displays a table listing the logcat messages.
*/
public final class LogCatPanel extends SelectionDependentPanel
                        implements ILogCatBufferChangeListener {
/** Preference key to use for storing list of logcat filters. */
public static final String LOGCAT_FILTERS_LIST = "logcat.view.filters.list";

//Synthetic comment -- @@ -121,20 +116,20 @@
private static final String IMAGE_SAVE_LOG_TO_FILE = "save.png"; //$NON-NLS-1$
private static final String IMAGE_CLEAR_LOG = "clear.png"; //$NON-NLS-1$
private static final String IMAGE_DISPLAY_FILTERS = "displayfilters.png"; //$NON-NLS-1$
    private static final String IMAGE_SCROLL_LOCK = "pause_logcat.png"; //$NON-NLS-1$

private static final int[] WEIGHTS_SHOW_FILTERS = new int[] {15, 85};
private static final int[] WEIGHTS_LOGCAT_ONLY = new int[] {0, 100};

    /** Index of the default filter in the saved filters column. */
    private static final int DEFAULT_FILTER_INDEX = 0;

private LogCatReceiver mReceiver;
private IPreferenceStore mPrefStore;

private List<LogCatFilter> mLogCatFilters;
private int mCurrentSelectedFilterIndex;

private ToolItem mNewFilterToolItem;
private ToolItem mDeleteFilterToolItem;
private ToolItem mEditFilterToolItem;
//Synthetic comment -- @@ -143,28 +138,37 @@
private Combo mLiveFilterLevelCombo;
private Text mLiveFilterText;

    private List<LogCatFilter> mCurrentFilters = Collections.emptyList();

    private Table mTable;

private boolean mShouldScrollToLatestLog = true;
    private ToolItem mScrollLockCheckBox;

private String mLogFileExportFolder;

    private Font mFont;
    private int mWrapWidthInChars;

private SashForm mSash;

    private List<LogCatMessage> mLogBuffer; // messages added since last refresh, synchronized on mLogBuffer
    private int mDeletedLogCount;           // # of messages deleted since last refresh, synchronized on mLogBuffer

/**
* Construct a logcat panel.
* @param prefStore preference store where UI preferences will be saved
*/
public LogCatPanel(IPreferenceStore prefStore) {
mPrefStore = prefStore;
        mLogBuffer = new ArrayList<LogCatMessage>(LogCatMessageList.MAX_MESSAGES_DEFAULT);

initializeFilters();

setupDefaultPreferences();
initializePreferenceUpdateListeners();

        mFont = getFontFromPrefStore();
}

private void initializeFilters() {
//Synthetic comment -- @@ -196,15 +200,24 @@
@Override
public void propertyChange(PropertyChangeEvent event) {
String changedProperty = event.getProperty();
if (changedProperty.equals(LogCatPanel.LOGCAT_VIEW_FONT_PREFKEY)) {
                    if (mFont != null) {
                        mFont.dispose();
                    }
                    mFont = getFontFromPrefStore();
                    recomputeWrapWidth();
                    Display.getDefault().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            for (TableItem it: mTable.getItems()) {
                                it.setFont(mFont);
                            }
                        }
                    });
                } else if (changedProperty.equals(LogCatMessageList.MAX_MESSAGES_PREFKEY)) {
mReceiver.resizeFifo(mPrefStore.getInt(
LogCatMessageList.MAX_MESSAGES_PREFKEY));
                    reloadLogBuffer();
}
}
});
//Synthetic comment -- @@ -246,7 +259,7 @@

mReceiver = LogCatReceiverFactory.INSTANCE.newReceiver(device, mPrefStore);
mReceiver.addMessageReceivedEventListener(this);
        reloadLogBuffer();

// Always scroll to last line whenever the selected device changes.
// Run this in a separate async thread to give the table some time to update after the
//Synthetic comment -- @@ -430,7 +443,7 @@
* @param appName application name to filter by
*/
public void selectTransientAppFilter(String appName) {
        assert mTable.getDisplay().getThread() == Thread.currentThread();

LogCatFilter f = findTransientAppFilter(appName);
if (f == null) {
//Synthetic comment -- @@ -501,11 +514,7 @@
createLogcatViewTable(c);
}

    /** Create the search bar at the top of the logcat messages table. */
private void createLiveFilters(Composite parent) {
Composite c = new Composite(parent, SWT.NONE);
c.setLayout(new GridLayout(3, false));
//Synthetic comment -- @@ -557,8 +566,6 @@
mReceiver.clearMessages();
refreshLogCatTable();

// the filters view is not cleared unless the filters are re-applied.
updateAppliedFilters();
}
//Synthetic comment -- @@ -580,17 +587,17 @@
}
});

        mScrollLockCheckBox = new ToolItem(toolBar, SWT.CHECK);
        mScrollLockCheckBox.setImage(
                ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_SCROLL_LOCK,
toolBar.getDisplay()));
        mScrollLockCheckBox.setSelection(false);
        mScrollLockCheckBox.setToolTipText("Scroll Lock");
        mScrollLockCheckBox.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent event) {
                boolean scrollLock = mScrollLockCheckBox.getSelection();
                setScrollToLatestLog(!scrollLock);
}
});
}
//Synthetic comment -- @@ -620,13 +627,13 @@
Thread t = new Thread(new Runnable() {
@Override
public void run() {
                BufferedWriter w = null;
try {
                    w = new BufferedWriter(new FileWriter(fName));
for (LogCatMessage m : selectedMessages) {
w.append(m.toString());
w.newLine();
}
} catch (final IOException e) {
Display.getDefault().asyncExec(new Runnable() {
@Override
//Synthetic comment -- @@ -637,6 +644,14 @@
+ e.getMessage());
}
});
                } finally {
                    if (w != null) {
                        try {
                            w.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
}
}
});
//Synthetic comment -- @@ -675,44 +690,22 @@
}

private List<LogCatMessage> getSelectedLogCatMessages() {
        int[] indices = mTable.getSelectionIndices();
Arrays.sort(indices); /* Table.getSelectionIndices() does not specify an order */

List<LogCatMessage> selectedMessages = new ArrayList<LogCatMessage>(indices.length);
for (int i : indices) {
            selectedMessages.add((LogCatMessage) mTable.getItem(i).getData());
}

return selectedMessages;
}

    private List<LogCatMessage> applyCurrentFilters(List<LogCatMessage> msgList) {
        List<LogCatMessage> filteredItems = new ArrayList<LogCatMessage>(msgList.size());

        for (LogCatMessage msg: msgList) {
            if (isMessageAccepted(msg, mCurrentFilters)) {
filteredItems.add(msg);
}
}
//Synthetic comment -- @@ -720,33 +713,30 @@
return filteredItems;
}

    private boolean isMessageAccepted(LogCatMessage msg, List<LogCatFilter> filters) {
        for (LogCatFilter f : filters) {
            if (!f.matches(msg)) {
                // not accepted by this filter
                return false;
}
}

        // accepted by all filters
        return true;
}

private void createLogcatViewTable(Composite parent) {
        mTable = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI);

        mTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        mTable.getHorizontalBar().setVisible(true);

/** Columns to show in the table. */
String[] properties = {
"Level",
"Time",
"PID",
                "TID",
"Application",
"Tag",
"Text",
//Synthetic comment -- @@ -758,32 +748,31 @@
"    ",
"    00-00 00:00:00.0000 ",
"    0000",
                "    0000",
"    com.android.launcher",
"    SampleTagText",
"    Log Message field should be pretty long by default. As long as possible for correct display on Mac.",
};

for (int i = 0; i < properties.length; i++) {
            TableHelper.createTableColumn(mTable,
properties[i],                      /* Column title */
SWT.LEFT,                           /* Column Style */
sampleText[i],                      /* String to compute default col width */
getColPreferenceKey(properties[i]), /* Preference Store key for this column */
mPrefStore);
}

        // don't zebra stripe the table: When the buffer is full, and scroll lock is on, having
        // zebra striping means that the background could keep changing depending on the number
        // of new messages added to the bottom of the log.
        mTable.setLinesVisible(false);
        mTable.setHeaderVisible(true);

// Set the row height to be sufficient enough to display the current font.
// This is not strictly necessary, except that on WinXP, the rows showed up clipped. So
// we explicitly set it to be sure.
        mTable.addListener(SWT.MeasureItem, new Listener() {
@Override
public void handleEvent(Event event) {
event.height = event.gc.getFontMetrics().getHeight();
//Synthetic comment -- @@ -791,146 +780,48 @@
});

// Update the label provider whenever the text column's width changes
        TableColumn textColumn = mTable.getColumn(properties.length - 1);
textColumn.addControlListener(new ControlAdapter() {
@Override
public void controlResized(ControlEvent event) {
                recomputeWrapWidth();
}
});

initDoubleClickListener();
        recomputeWrapWidth();
}

    public void recomputeWrapWidth() {
        if (mTable == null || mTable.isDisposed()) {
            return;
}

        // get width of the last column (log message)
        TableColumn tc = mTable.getColumn(mTable.getColumnCount() - 1);
        int colWidth = tc.getWidth();

        // get font width
        GC gc = new GC(tc.getParent());
        gc.setFont(mFont);
        int avgCharWidth = gc.getFontMetrics().getAverageCharWidth();
        gc.dispose();

        int MIN_CHARS_PER_LINE = 50;    // show atleast these many chars per line
        mWrapWidthInChars = Math.max(colWidth/avgCharWidth, MIN_CHARS_PER_LINE);

        int OFFSET_AT_END_OF_LINE = 10; // leave some space at the end of the line
        mWrapWidthInChars -= OFFSET_AT_END_OF_LINE;
}

    private void setScrollToLatestLog(boolean scroll) {
mShouldScrollToLatestLog = scroll;

if (scroll) {
scrollToLatestLog();
}
}

private String getColPreferenceKey(String field) {
return LOGCAT_VIEW_COLSIZE_PREFKEY_PREFIX + field;
}
//Synthetic comment -- @@ -952,7 +843,7 @@
* Perform all necessary updates whenever a filter is selected (by user or programmatically).
*/
private void filterSelectionChanged() {
        int idx = mFiltersTableViewer.getTable().getSelectionIndex();
if (idx == -1) {
/* One of the filters should always be selected.
* On Linux, there is no way to deselect an item.
//Synthetic comment -- @@ -971,84 +862,80 @@
}

private void resetUnreadCountForSelectedFilter() {
        mLogCatFilters.get(mCurrentSelectedFilterIndex).resetUnreadCount();
refreshFiltersTable();
}

private void updateFiltersToolBar() {
/* The default filter at index 0 can neither be edited, nor removed. */
        boolean en = mCurrentSelectedFilterIndex != DEFAULT_FILTER_INDEX;
mEditFilterToolItem.setEnabled(en);
mDeleteFilterToolItem.setEnabled(en);
}

private void updateAppliedFilters() {
        mCurrentFilters = getFiltersToApply();
        reloadLogBuffer();
}

    private List<LogCatFilter> getFiltersToApply() {
/* list of filters to apply = saved filter + live filters */
        List<LogCatFilter> filters = new ArrayList<LogCatFilter>();

        if (mCurrentSelectedFilterIndex != DEFAULT_FILTER_INDEX) {
            filters.add(getSelectedSavedFilter());
        }

filters.addAll(getCurrentLiveFilters());
return filters;
}

    private List<LogCatFilter> getCurrentLiveFilters() {
        return LogCatFilter.fromString(
mLiveFilterText.getText(),                                  /* current query */
LogLevel.getByString(mLiveFilterLevelCombo.getText()));     /* current log level */
}

    private LogCatFilter getSelectedSavedFilter() {
        return mLogCatFilters.get(mCurrentSelectedFilterIndex);
}

@Override
public void setFocus() {
}

@Override
    public void bufferChanged(List<LogCatMessage> addedMessages,
            List<LogCatMessage> deletedMessages) {

        synchronized (mLogBuffer) {
            addedMessages = applyCurrentFilters(addedMessages);
            deletedMessages = applyCurrentFilters(deletedMessages);

            mLogBuffer.addAll(addedMessages);
            mDeletedLogCount += deletedMessages.size();
}

        refreshLogCatTable();
        updateUnreadCount(addedMessages);
        refreshFiltersTable();
    }

    private void reloadLogBuffer() {
        mTable.removeAll();

        synchronized (mLogBuffer) {
            mLogBuffer.clear();
            mDeletedLogCount = 0;
        }

        if (mReceiver == null) {
            return;
        }

        List<LogCatMessage> addedMessages = mReceiver.getMessages().getAllMessages();
        List<LogCatMessage> deletedMessages = Collections.emptyList();
        bufferChanged(addedMessages, deletedMessages);
}

/**
//Synthetic comment -- @@ -1089,7 +976,7 @@
*/
private void refreshLogCatTable() {
synchronized (this) {
            if (mCurrentRefresher == null) {
mCurrentRefresher = new LogCatTableRefresherTask();
Display.getDefault().asyncExec(mCurrentRefresher);
}
//Synthetic comment -- @@ -1099,24 +986,110 @@
private class LogCatTableRefresherTask implements Runnable {
@Override
public void run() {
            if (mTable.isDisposed()) {
return;
}
synchronized (LogCatPanel.this) {
mCurrentRefresher = null;
}

            int topIndex = mTable.getTopIndex();

            mTable.setRedraw(false);

            List<LogCatMessage> newMessages;
            int deletedCount;
            synchronized (mLogBuffer) {
                newMessages = new ArrayList<LogCatMessage>(mLogBuffer);
                mLogBuffer.clear();

                deletedCount = mDeletedLogCount;
                mDeletedLogCount = 0;
}

            for (LogCatMessage m: newMessages) {
                TableItem item = new TableItem(mTable, SWT.NONE);
                item.setData(m);
                item.setText(new String[] {
                        Character.toString(m.getLogLevel().getPriorityLetter()),
                        m.getTime(),
                        m.getPid(),
                        m.getTid(),
                        m.getAppName(),
                        m.getTag(),
                        wrapMessage(m.getMessage(), mWrapWidthInChars)
                });
                item.setForeground(getForegroundColor(m));
                item.setFont(mFont);
            }

            mTable.remove(0, deletedCount - 1);

            if (mShouldScrollToLatestLog) {
                scrollToLatestLog();
            } else {
                int index = Math.max(topIndex - deletedCount, 0);
                mTable.setTopIndex(index);
            }

            mTable.setRedraw(true);
}
}

/** Scroll to the last line. */
private void scrollToLatestLog() {
        mTable.setTopIndex(mTable.getItemCount() - 1);
    }

    /** Word wraps given string, breaking at the specified wrap width. */
    private String wrapMessage(String msg, int wrapWidth) {
        if (msg.length() < wrapWidth) {
            return msg;
        }

        StringBuffer sb = new StringBuffer(msg.length() + 20);

        int offset = 0;
        int len = msg.length();

        while (len > 0) {
            int copylen = Math.min(wrapWidth, len);
            sb.append(msg.substring(offset, offset + copylen));

            offset += copylen;
            len -= copylen;

            if (len > 0) {
                // if there is still text left, then append them after a wrap marker + newline
                sb.append(" \u23ce\n  "); //$NON-NLS-1$
            }
        }
        return sb.toString();
    }

    /* Default Colors for different log levels. */
    private static final Color INFO_MSG_COLOR = new Color(null, 0, 127, 0);
    private static final Color DEBUG_MSG_COLOR = new Color(null, 0, 0, 127);
    private static final Color ERROR_MSG_COLOR = new Color(null, 255, 0, 0);
    private static final Color WARN_MSG_COLOR = new Color(null, 255, 127, 0);
    private static final Color VERBOSE_MSG_COLOR = new Color(null, 0, 0, 0);

    private static Color getForegroundColor(LogCatMessage m) {
        LogLevel l = m.getLogLevel();

        if (l.equals(LogLevel.VERBOSE)) {
            return VERBOSE_MSG_COLOR;
        } else if (l.equals(LogLevel.INFO)) {
            return INFO_MSG_COLOR;
        } else if (l.equals(LogLevel.DEBUG)) {
            return DEBUG_MSG_COLOR;
        } else if (l.equals(LogLevel.ERROR)) {
            return ERROR_MSG_COLOR;
        } else if (l.equals(LogLevel.WARN)) {
            return WARN_MSG_COLOR;
        }

        return null;
}

private List<ILogCatMessageSelectionListener> mMessageSelectionListeners;
//Synthetic comment -- @@ -1124,7 +1097,7 @@
private void initDoubleClickListener() {
mMessageSelectionListeners = new ArrayList<ILogCatMessageSelectionListener>(1);

        mTable.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetDefaultSelected(SelectionEvent arg0) {
List<LogCatMessage> selectedMessages = getSelectedLogCatMessages();
//Synthetic comment -- @@ -1153,7 +1126,6 @@
public void setTableFocusListener(ITableFocusListener listener) {
mTableFocusListener = listener;

final IFocusedTableActivator activator = new IFocusedTableActivator() {
@Override
public void copy(Clipboard clipboard) {
//Synthetic comment -- @@ -1162,11 +1134,11 @@

@Override
public void selectAll() {
                mTable.selectAll();
}
};

        mTable.addFocusListener(new FocusListener() {
@Override
public void focusGained(FocusEvent e) {
mTableFocusListener.focusGained(activator);
//Synthetic comment -- @@ -1198,6 +1170,6 @@

/** Select all items in the logcat table. */
public void selectAll() {
        mTable.selectAll();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index c9606f6..8f2d52c 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
private LogCatOutputReceiver mCurrentLogCatOutputReceiver;
    private Set<ILogCatBufferChangeListener> mLogCatMessageListeners;
private LogCatMessageParser mLogCatMessageParser;
private LogCatPidToNameMapper mPidToNameMapper;
private IPreferenceStore mPrefStore;
//Synthetic comment -- @@ -55,7 +55,7 @@
mCurrentDevice = device;
mPrefStore = prefStore;

        mLogCatMessageListeners = new HashSet<ILogCatBufferChangeListener>();
mLogCatMessageParser = new LogCatMessageParser();
mPidToNameMapper = new LogCatPidToNameMapper(mCurrentDevice);

//Synthetic comment -- @@ -147,14 +147,16 @@
}

private void processLogLines(String[] lines) {
        List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
mPidToNameMapper);

        if (newMessages.size() > 0) {
            List<LogCatMessage> deletedMessages;
            synchronized (mLogMessages) {
                deletedMessages = mLogMessages.ensureSpace(newMessages.size());
                mLogMessages.appendMessages(newMessages);
}
            sendLogChangedEvent(newMessages, deletedMessages);
}
}

//Synthetic comment -- @@ -177,17 +179,18 @@
* Add to list of message event listeners.
* @param l listener to notified when messages are received from the device
*/
    public void addMessageReceivedEventListener(ILogCatBufferChangeListener l) {
mLogCatMessageListeners.add(l);
}

    public void removeMessageReceivedEventListener(ILogCatBufferChangeListener l) {
mLogCatMessageListeners.remove(l);
}

    private void sendLogChangedEvent(List<LogCatMessage> addedMessages,
            List<LogCatMessage> deletedMessages) {
        for (ILogCatBufferChangeListener l : mLogCatMessageListeners) {
            l.bufferChanged(addedMessages, deletedMessages);
}
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatViewerFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatViewerFilter.java
deleted file mode 100644
//Synthetic comment -- index f7b8dce..0000000

//Synthetic comment -- @@ -1,46 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java
//Synthetic comment -- index f9c94a7..1e50822 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.logcat.ILogCatBufferChangeListener;
import com.android.ddmuilib.logcat.LogCatMessage;
import com.android.ddmuilib.logcat.LogCatReceiver;
import com.android.ddmuilib.logcat.LogCatReceiverFactory;
//Synthetic comment -- @@ -98,7 +98,7 @@
return;
}

        data.receiver.removeMessageReceivedEventListener(data.bufferChangeListener);
}

public void monitorDevice(final IDevice device) {
//Synthetic comment -- @@ -113,10 +113,11 @@
}

LogCatReceiver r = LogCatReceiverFactory.INSTANCE.newReceiver(device, mPrefStore);
        ILogCatBufferChangeListener l = new ILogCatBufferChangeListener() {
@Override
            public void bufferChanged(List<LogCatMessage> addedMessages,
                    List<LogCatMessage> deletedMessages) {
                checkMessages(addedMessages, device);
}
};
r.addMessageReceivedEventListener(l);
//Synthetic comment -- @@ -205,11 +206,11 @@

private static class DeviceData {
public final LogCatReceiver receiver;
        public final ILogCatBufferChangeListener bufferChangeListener;

        public DeviceData(LogCatReceiver r, ILogCatBufferChangeListener l) {
receiver = r;
            bufferChangeListener = l;
}
}
}







