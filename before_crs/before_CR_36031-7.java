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
 * Listeners interested in log cat messages should implement this interface.
*/
public interface ILogCatMessageEventListener {
    /** Called on reception of logcat messages.
     * @param receivedMessages list of messages received
*/
    void messageReceived(List<LogCatMessage> receivedMessages);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterContentProvider.java
//Synthetic comment -- index 164f484..68c08d4 100644

//Synthetic comment -- @@ -39,10 +39,6 @@
*/
@Override
public Object[] getElements(Object model) {
        if (model instanceof List<?>) {
            return ((List<?>) model).toArray();
        }
        return null;
}

}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilterSettingsDialog.java
//Synthetic comment -- index f68ee05..39b3fa9 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
* on the dialog is valid or not. If it is not valid, the message
* field stores the reason why it isn't.
*/
    private final class DialogStatus {
final boolean valid;
final String message;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageContentProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageContentProvider.java
deleted file mode 100644
//Synthetic comment -- index bd7b520..0000000

//Synthetic comment -- @@ -1,43 +0,0 @@
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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A JFace content provider for the LogCat log messages, used in the {@link LogCatPanel}.
 */
public final class LogCatMessageContentProvider implements IStructuredContentProvider {
    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public Object[] getElements(Object model) {
        if (model instanceof LogCatMessageList) {
            Object[] e = ((LogCatMessageList) model).toArray();
            return e;
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageLabelProvider.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageLabelProvider.java
deleted file mode 100644
//Synthetic comment -- index 1d83a9c..0000000

//Synthetic comment -- @@ -1,144 +0,0 @@
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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

/**
 * A JFace Column label provider for the LogCat log messages. It expects elements of type
 * {@link LogCatMessage}.
 */
public final class LogCatMessageLabelProvider extends ColumnLabelProvider {
    private static final int INDEX_LOGLEVEL = 0;
    private static final int INDEX_LOGTIME = 1;
    private static final int INDEX_PID = 2;
    private static final int INDEX_APPNAME = 3;
    private static final int INDEX_TAG = 4;
    private static final int INDEX_TEXT = 5;

    /* Default Colors for different log levels. */
    private static final Color INFO_MSG_COLOR =    new Color(null, 0, 127, 0);
    private static final Color DEBUG_MSG_COLOR =   new Color(null, 0, 0, 127);
    private static final Color ERROR_MSG_COLOR =   new Color(null, 255, 0, 0);
    private static final Color WARN_MSG_COLOR =    new Color(null, 255, 127, 0);
    private static final Color VERBOSE_MSG_COLOR = new Color(null, 0, 0, 0);

    /** Amount of pixels to shift the tooltip by. */
    private static final Point LOGCAT_TOOLTIP_SHIFT = new Point(10, 10);

    private Font mLogFont;
    private int mWrapWidth = 100;

    /**
     * Construct a column label provider for the logcat table.
     * @param font default font to use
     */
    public LogCatMessageLabelProvider(Font font) {
        mLogFont = font;
    }

    private String getCellText(LogCatMessage m, int columnIndex) {
        switch (columnIndex) {
            case INDEX_LOGLEVEL:
                return Character.toString(m.getLogLevel().getPriorityLetter());
            case INDEX_LOGTIME:
                return m.getTime();
            case INDEX_PID:
                return m.getPid();
            case INDEX_APPNAME:
                return m.getAppName();
            case INDEX_TAG:
                return m.getTag();
            case INDEX_TEXT:
                return m.getMessage();
            default:
                return "";
        }
    }

    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        if (!(element instanceof LogCatMessage)) {
            return;
        }
        LogCatMessage m = (LogCatMessage) element;

        String text = getCellText(m, cell.getColumnIndex());
        cell.setText(text);
        cell.setFont(mLogFont);
        cell.setForeground(getForegroundColor(m));
    }

    private Color getForegroundColor(LogCatMessage m) {
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

    public void setFont(Font preferredFont) {
        if (mLogFont != null) {
            mLogFont.dispose();
        }

        mLogFont = preferredFont;
    }

    public void setMinimumLengthForToolTips(int widthInChars) {
        mWrapWidth  = widthInChars;
    }

    /**
     * Obtain the tool tip to show for a particular logcat message.
     * We display a tool tip only for messages longer than the width set by
     * {@link #setMinimumLengthForToolTips(int)}.
     */
    @Override
    public String getToolTipText(Object element) {
        String text = element.toString();
        if (text.length() > mWrapWidth) {
            return text;
        } else {
            return null;
        }
    }

    @Override
    public Point getToolTipShift(Object object) {
        // The only reason we override this method is that the default shift amounts
        // don't seem to work on OS X Lion.
        return LOGCAT_TOOLTIP_SHIFT;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java
//Synthetic comment -- index 0d0e3c2..080dbc1 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//Synthetic comment -- @@ -33,7 +35,6 @@

private int mFifoSize;
private BlockingQueue<LogCatMessage> mQ;
    private LogCatMessage[] mQArray;

/**
* Construct an empty message list.
//Synthetic comment -- @@ -43,7 +44,6 @@
mFifoSize = maxMessages;

mQ = new ArrayBlockingQueue<LogCatMessage>(mFifoSize);
        mQArray = new LogCatMessage[mFifoSize];
}

/**
//Synthetic comment -- @@ -64,8 +64,6 @@
mQ.offer(curMessages[i]);
}
}

        mQArray = new LogCatMessage[mFifoSize];
}

/**
//Synthetic comment -- @@ -73,17 +71,30 @@
* message will be popped off of it.
* @param m log to be inserted
*/
    public synchronized void appendMessage(final LogCatMessage m) {
        if (mQ.remainingCapacity() == 0) {
            /* make space by removing the first entry */
            mQ.poll();
}
        mQ.offer(m);
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

    /**
     * Clear all messages in the list.
     */
public synchronized void clear() {
mQ.clear();
}

    /**
     * Obtain all the messages currently present in the list.
     * @return array containing all the log messages
     */
    public Object[] toArray() {
        if (mQ.size() == mFifoSize) {
            /*
             * Once the queue is full, it stays full until the user explicitly clears
             * all the logs. Optimize for this case by not reallocating the array.
             */
            return mQ.toArray(mQArray);
        }
        return mQ.toArray();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 7aa0328..ca686f9 100644

//Synthetic comment -- @@ -30,12 +30,7 @@
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -50,9 +45,11 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
//Synthetic comment -- @@ -63,7 +60,6 @@
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
//Synthetic comment -- @@ -83,7 +79,7 @@
* LogCatPanel displays a table listing the logcat messages.
*/
public final class LogCatPanel extends SelectionDependentPanel
                        implements ILogCatMessageEventListener {
/** Preference key to use for storing list of logcat filters. */
public static final String LOGCAT_FILTERS_LIST = "logcat.view.filters.list";

//Synthetic comment -- @@ -121,20 +117,20 @@
private static final String IMAGE_SAVE_LOG_TO_FILE = "save.png"; //$NON-NLS-1$
private static final String IMAGE_CLEAR_LOG = "clear.png"; //$NON-NLS-1$
private static final String IMAGE_DISPLAY_FILTERS = "displayfilters.png"; //$NON-NLS-1$
    private static final String IMAGE_PAUSE_LOGCAT = "pause_logcat.png"; //$NON-NLS-1$

private static final int[] WEIGHTS_SHOW_FILTERS = new int[] {15, 85};
private static final int[] WEIGHTS_LOGCAT_ONLY = new int[] {0, 100};

private LogCatReceiver mReceiver;
private IPreferenceStore mPrefStore;

private List<LogCatFilter> mLogCatFilters;
private int mCurrentSelectedFilterIndex;

    private int mRemovedEntriesCount = 0;
    private int mPreviousRemainingCapacity = 0;

private ToolItem mNewFilterToolItem;
private ToolItem mDeleteFilterToolItem;
private ToolItem mEditFilterToolItem;
//Synthetic comment -- @@ -143,28 +139,37 @@
private Combo mLiveFilterLevelCombo;
private Text mLiveFilterText;

    private TableViewer mViewer;

private boolean mShouldScrollToLatestLog = true;
    private ToolItem mPauseLogcatCheckBox;
    private boolean mLastItemPainted = false;

private String mLogFileExportFolder;
    private LogCatMessageLabelProvider mLogCatMessageLabelProvider;

private SashForm mSash;

/**
* Construct a logcat panel.
* @param prefStore preference store where UI preferences will be saved
*/
public LogCatPanel(IPreferenceStore prefStore) {
mPrefStore = prefStore;

initializeFilters();

setupDefaultPreferences();
initializePreferenceUpdateListeners();
}

private void initializeFilters() {
//Synthetic comment -- @@ -196,15 +201,24 @@
@Override
public void propertyChange(PropertyChangeEvent event) {
String changedProperty = event.getProperty();

if (changedProperty.equals(LogCatPanel.LOGCAT_VIEW_FONT_PREFKEY)) {
                    mLogCatMessageLabelProvider.setFont(getFontFromPrefStore());
                    refreshLogCatTable();
                } else if (changedProperty.equals(
                        LogCatMessageList.MAX_MESSAGES_PREFKEY)) {
mReceiver.resizeFifo(mPrefStore.getInt(
LogCatMessageList.MAX_MESSAGES_PREFKEY));
                    refreshLogCatTable();
}
}
});
//Synthetic comment -- @@ -246,7 +260,7 @@

mReceiver = LogCatReceiverFactory.INSTANCE.newReceiver(device, mPrefStore);
mReceiver.addMessageReceivedEventListener(this);
        mViewer.setInput(mReceiver.getMessages());

// Always scroll to last line whenever the selected device changes.
// Run this in a separate async thread to give the table some time to update after the
//Synthetic comment -- @@ -430,7 +444,7 @@
* @param appName application name to filter by
*/
public void selectTransientAppFilter(String appName) {
        assert mViewer.getTable().getDisplay().getThread() == Thread.currentThread();

LogCatFilter f = findTransientAppFilter(appName);
if (f == null) {
//Synthetic comment -- @@ -501,11 +515,7 @@
createLogcatViewTable(c);
}

    /**
     * Create the search bar at the top of the logcat messages table.
     * FIXME: Currently, this feature is incomplete: The UI elements are created, but they
     * are all set to disabled state.
     */
private void createLiveFilters(Composite parent) {
Composite c = new Composite(parent, SWT.NONE);
c.setLayout(new GridLayout(3, false));
//Synthetic comment -- @@ -557,8 +567,6 @@
mReceiver.clearMessages();
refreshLogCatTable();

                    mRemovedEntriesCount = 0;

// the filters view is not cleared unless the filters are re-applied.
updateAppliedFilters();
}
//Synthetic comment -- @@ -580,17 +588,17 @@
}
});

        mPauseLogcatCheckBox = new ToolItem(toolBar, SWT.CHECK);
        mPauseLogcatCheckBox.setImage(
                ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_PAUSE_LOGCAT,
toolBar.getDisplay()));
        mPauseLogcatCheckBox.setSelection(false);
        mPauseLogcatCheckBox.setToolTipText("Pause receiving new logcat messages.");
        mPauseLogcatCheckBox.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent event) {
                boolean pauseLogcat = mPauseLogcatCheckBox.getSelection();
                setScrollToLatestLog(!pauseLogcat, false);
}
});
}
//Synthetic comment -- @@ -620,13 +628,13 @@
Thread t = new Thread(new Runnable() {
@Override
public void run() {
try {
                    BufferedWriter w = new BufferedWriter(new FileWriter(fName));
for (LogCatMessage m : selectedMessages) {
w.append(m.toString());
w.newLine();
}
                    w.close();
} catch (final IOException e) {
Display.getDefault().asyncExec(new Runnable() {
@Override
//Synthetic comment -- @@ -637,6 +645,14 @@
+ e.getMessage());
}
});
}
}
});
//Synthetic comment -- @@ -675,44 +691,22 @@
}

private List<LogCatMessage> getSelectedLogCatMessages() {
        Table table = mViewer.getTable();
        int[] indices = table.getSelectionIndices();
Arrays.sort(indices); /* Table.getSelectionIndices() does not specify an order */

        // Get items from the table's input as opposed to getting each table item's data.
        // Retrieving table item's data can return NULL in case of a virtual table if the item
        // has not been displayed yet.
        Object input = mViewer.getInput();
        if (!(input instanceof LogCatMessageList)) {
            return Collections.emptyList();
        }

        List<LogCatMessage> filteredItems = applyCurrentFilters((LogCatMessageList) input);
List<LogCatMessage> selectedMessages = new ArrayList<LogCatMessage>(indices.length);
for (int i : indices) {
            // consider removed logcat message entries
            i -= mRemovedEntriesCount;
            if (i >= 0 && i < filteredItems.size()) {
                LogCatMessage m = filteredItems.get(i);
                selectedMessages.add(m);
            }
}

return selectedMessages;
}

    private List<LogCatMessage> applyCurrentFilters(LogCatMessageList msgList) {
        Object[] items = msgList.toArray();
        List<LogCatMessage> filteredItems = new ArrayList<LogCatMessage>(items.length);
        List<LogCatViewerFilter> filters = getFiltersToApply();

        for (Object item : items) {
            if (!(item instanceof LogCatMessage)) {
                continue;
            }

            LogCatMessage msg = (LogCatMessage) item;
            if (!isMessageFiltered(msg, filters)) {
filteredItems.add(msg);
}
}
//Synthetic comment -- @@ -720,33 +714,30 @@
return filteredItems;
}

    private boolean isMessageFiltered(LogCatMessage msg, List<LogCatViewerFilter> filters) {
        for (LogCatViewerFilter f : filters) {
            if (!f.select(null, null, msg)) {
                // message does not make it through this filter
                return true;
}
}

        return false;
}

private void createLogcatViewTable(Composite parent) {
        // The SWT.VIRTUAL bit causes the table to be rendered faster. However it makes all rows
        // to be of the same height, thereby clipping any rows with multiple lines of text.
        // In such a case, users can view the full text by hovering over the item and looking at
        // the tooltip.
        final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
        mViewer = new TableViewer(table);

        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.getHorizontalBar().setVisible(true);

/** Columns to show in the table. */
String[] properties = {
"Level",
"Time",
"PID",
"Application",
"Tag",
"Text",
//Synthetic comment -- @@ -758,179 +749,84 @@
"    ",
"    00-00 00:00:00.0000 ",
"    0000",
"    com.android.launcher",
"    SampleTagText",
"    Log Message field should be pretty long by default. As long as possible for correct display on Mac.",
};

        mLogCatMessageLabelProvider = new LogCatMessageLabelProvider(getFontFromPrefStore());
for (int i = 0; i < properties.length; i++) {
            TableColumn tc = TableHelper.createTableColumn(mViewer.getTable(),
properties[i],                      /* Column title */
SWT.LEFT,                           /* Column Style */
sampleText[i],                      /* String to compute default col width */
getColPreferenceKey(properties[i]), /* Preference Store key for this column */
mPrefStore);
            TableViewerColumn tvc = new TableViewerColumn(mViewer, tc);
            tvc.setLabelProvider(mLogCatMessageLabelProvider);
}

        mViewer.getTable().setLinesVisible(true); /* zebra stripe the table */
        mViewer.getTable().setHeaderVisible(true);
        mViewer.setContentProvider(new LogCatMessageContentProvider());
        WrappingToolTipSupport.enableFor(mViewer, ToolTip.NO_RECREATE);

// Set the row height to be sufficient enough to display the current font.
// This is not strictly necessary, except that on WinXP, the rows showed up clipped. So
// we explicitly set it to be sure.
        mViewer.getTable().addListener(SWT.MeasureItem, new Listener() {
@Override
public void handleEvent(Event event) {
                event.height = event.gc.getFontMetrics().getHeight();
}
});

// Update the label provider whenever the text column's width changes
        TableColumn textColumn = mViewer.getTable().getColumn(properties.length - 1);
textColumn.addControlListener(new ControlAdapter() {
@Override
public void controlResized(ControlEvent event) {
                TableColumn tc = (TableColumn) event.getSource();
                int width = tc.getWidth();
                GC gc = new GC(tc.getParent());
                int avgCharWidth = gc.getFontMetrics().getAverageCharWidth();
                gc.dispose();

                if (mLogCatMessageLabelProvider != null) {
                    mLogCatMessageLabelProvider.setMinimumLengthForToolTips(width/avgCharWidth);
                }
}
});

        setupAutoScrollLockBehavior();
initDoubleClickListener();
}

    /**
     * Setup to automatically enable or disable scroll lock. From a user's perspective,
     * the logcat window will: <ul>
     * <li> Automatically scroll and reveal new entries if the scrollbar is at the bottom. </li>
     * <li> Not scroll even when new messages are received if the scrollbar is not at the bottom.
     * </li>
     * </ul>
     * This requires that we are able to detect where the scrollbar is and what direction
     * it is moving. Unfortunately, that proves to be very platform dependent. Here's the behavior
     * of the scroll events on different platforms: <ul>
     * <li> On Windows, scroll bar events specify which direction the scrollbar is moving, but
     * it is not possible to determine if the scrollbar is right at the end. </li>
     * <li> On Mac/Cocoa, scroll bar events do not specify the direction of movement (it is always
     * set to SWT.DRAG), and it is not possible to identify where the scrollbar is since
     * small movements of the scrollbar are not reflected in sb.getSelection(). </li>
     * <li> On Linux/gtk, we don't get the direction, but we can accurately locate the
     * scrollbar location using getSelection(), getThumb() and getMaximum().
     * </ul>
     */
    private void setupAutoScrollLockBehavior() {
        if (DdmConstants.CURRENT_PLATFORM == DdmConstants.PLATFORM_WINDOWS) {
            // On Windows, it is not possible to detect whether the scrollbar is at the
            // bottom using the values of ScrollBar.getThumb, getSelection and getMaximum.
            // Instead we resort to the following workaround: attach to the paint listener
            // and see if the last item has been painted since the previous scroll event.
            // If the last item has been painted, then we assume that we are at the bottom.
            mViewer.getTable().addListener(SWT.PaintItem, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    TableItem item = (TableItem) event.item;
                    TableItem[] items = mViewer.getTable().getItems();
                    if (items.length > 0 && items[items.length - 1] == item) {
                        mLastItemPainted = true;
                    }
                }
            });
            mViewer.getTable().getVerticalBar().addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    boolean scrollToLast;
                    if (event.detail == SWT.ARROW_UP || event.detail == SWT.PAGE_UP
                            || event.detail == SWT.HOME) {
                        // if we know that we are moving up, then do not scroll down
                        scrollToLast = false;
                    } else {
                        // otherwise, enable scrollToLast only if the last item was displayed
                        scrollToLast = mLastItemPainted;
                    }

                    setScrollToLatestLog(scrollToLast, true);
                    mLastItemPainted = false;
                }
            });
        } else if (DdmConstants.CURRENT_PLATFORM == DdmConstants.PLATFORM_LINUX) {
            // On Linux/gtk, we do not get any details regarding the scroll event (up/down/etc).
            // So we completely rely on whether the scrollbar is at the bottom or not.
            mViewer.getTable().getVerticalBar().addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    ScrollBar sb = (ScrollBar) event.getSource();
                    boolean scrollToLast = sb.getSelection() + sb.getThumb() == sb.getMaximum();
                    setScrollToLatestLog(scrollToLast, true);
                }
            });
        } else {
            // On Mac, we do not get any details regarding the (trackball) scroll event,
            // nor can we rely on getSelection() changing for small movements. As a result, we
            // do not setup any auto scroll lock behavior. Mac users have to manually pause and
            // unpause if they are looking at a particular item in a high volume stream of events.
}
}

    private void setScrollToLatestLog(boolean scroll, boolean updateCheckbox) {
mShouldScrollToLatestLog = scroll;

        if (updateCheckbox) {
            mPauseLogcatCheckBox.setSelection(!scroll);
        }

if (scroll) {
            mViewer.refresh();
scrollToLatestLog();
}
}

    private static class WrappingToolTipSupport extends ColumnViewerToolTipSupport {
        protected WrappingToolTipSupport(ColumnViewer viewer, int style,
                boolean manualActivation) {
            super(viewer, style, manualActivation);
        }

        @Override
        protected Composite createViewerToolTipContentArea(Event event, ViewerCell cell,
                Composite parent) {
            Composite comp = new Composite(parent, SWT.NONE);
            GridLayout l = new GridLayout(1, false);
            l.horizontalSpacing = 0;
            l.marginWidth = 0;
            l.marginHeight = 0;
            l.verticalSpacing = 0;
            comp.setLayout(l);

            Text text = new Text(comp, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
            text.setEditable(false);
            text.setText(cell.getElement().toString());
            text.setLayoutData(new GridData(500, 150));

            return comp;
        }

        @Override
        public boolean isHideOnMouseDown() {
            return false;
        }

        public static final void enableFor(ColumnViewer viewer, int style) {
            new WrappingToolTipSupport(viewer, style, false);
        }
    }

private String getColPreferenceKey(String field) {
return LOGCAT_VIEW_COLSIZE_PREFKEY_PREFIX + field;
}
//Synthetic comment -- @@ -952,7 +848,7 @@
* Perform all necessary updates whenever a filter is selected (by user or programmatically).
*/
private void filterSelectionChanged() {
        int idx = getSelectedSavedFilterIndex();
if (idx == -1) {
/* One of the filters should always be selected.
* On Linux, there is no way to deselect an item.
//Synthetic comment -- @@ -971,84 +867,80 @@
}

private void resetUnreadCountForSelectedFilter() {
        int index = getSelectedSavedFilterIndex();
        mLogCatFilters.get(index).resetUnreadCount();

refreshFiltersTable();
}

    private int getSelectedSavedFilterIndex() {
        return mFiltersTableViewer.getTable().getSelectionIndex();
    }

private void updateFiltersToolBar() {
/* The default filter at index 0 can neither be edited, nor removed. */
        boolean en = getSelectedSavedFilterIndex() != 0;
mEditFilterToolItem.setEnabled(en);
mDeleteFilterToolItem.setEnabled(en);
}

private void updateAppliedFilters() {
        List<LogCatViewerFilter> filters = getFiltersToApply();
        mViewer.setFilters(filters.toArray(new LogCatViewerFilter[filters.size()]));

        /* whenever filters are changed, the number of displayed logs changes
         * drastically. Display the latest log in such a situation. */
        scrollToLatestLog();
}

    private List<LogCatViewerFilter> getFiltersToApply() {
/* list of filters to apply = saved filter + live filters */
        List<LogCatViewerFilter> filters = new ArrayList<LogCatViewerFilter>();
        filters.add(getSelectedSavedFilter());
filters.addAll(getCurrentLiveFilters());
return filters;
}

    private List<LogCatViewerFilter> getCurrentLiveFilters() {
        List<LogCatViewerFilter> liveFilters = new ArrayList<LogCatViewerFilter>();

        List<LogCatFilter> liveFilterSettings = LogCatFilter.fromString(
mLiveFilterText.getText(),                                  /* current query */
LogLevel.getByString(mLiveFilterLevelCombo.getText()));     /* current log level */
        for (LogCatFilter s : liveFilterSettings) {
            liveFilters.add(new LogCatViewerFilter(s));
        }

        return liveFilters;
}

    private LogCatViewerFilter getSelectedSavedFilter() {
        int index = getSelectedSavedFilterIndex();
        return new LogCatViewerFilter(mLogCatFilters.get(index));
}


@Override
public void setFocus() {
}

    /**
     * Update view whenever a message is received.
     * @param receivedMessages list of messages from logcat
     * Implements {@link ILogCatMessageEventListener#messageReceived()}.
     */
@Override
    public void messageReceived(List<LogCatMessage> receivedMessages) {
        refreshLogCatTable();

        if (mShouldScrollToLatestLog) {
            updateUnreadCount(receivedMessages);
            refreshFiltersTable();
        } else {
            LogCatMessageList messageList = mReceiver.getMessages();
            int remainingCapacity = messageList.remainingCapacity();
            if (remainingCapacity == 0) {
                mRemovedEntriesCount +=
                        receivedMessages.size() - mPreviousRemainingCapacity;
            }
            mPreviousRemainingCapacity = remainingCapacity;
}
}

/**
//Synthetic comment -- @@ -1089,7 +981,7 @@
*/
private void refreshLogCatTable() {
synchronized (this) {
            if (mCurrentRefresher == null && mShouldScrollToLatestLog) {
mCurrentRefresher = new LogCatTableRefresherTask();
Display.getDefault().asyncExec(mCurrentRefresher);
}
//Synthetic comment -- @@ -1099,24 +991,122 @@
private class LogCatTableRefresherTask implements Runnable {
@Override
public void run() {
            if (mViewer.getTable().isDisposed()) {
return;
}
synchronized (LogCatPanel.this) {
mCurrentRefresher = null;
}

            if (mShouldScrollToLatestLog) {
                mViewer.refresh();
                scrollToLatestLog();
}
}
}

/** Scroll to the last line. */
private void scrollToLatestLog() {
        mRemovedEntriesCount = 0;
        mViewer.getTable().setTopIndex(mViewer.getTable().getItemCount() - 1);
}

private List<ILogCatMessageSelectionListener> mMessageSelectionListeners;
//Synthetic comment -- @@ -1124,7 +1114,7 @@
private void initDoubleClickListener() {
mMessageSelectionListeners = new ArrayList<ILogCatMessageSelectionListener>(1);

        mViewer.getTable().addSelectionListener(new SelectionAdapter() {
@Override
public void widgetDefaultSelected(SelectionEvent arg0) {
List<LogCatMessage> selectedMessages = getSelectedLogCatMessages();
//Synthetic comment -- @@ -1153,7 +1143,6 @@
public void setTableFocusListener(ITableFocusListener listener) {
mTableFocusListener = listener;

        final Table table = mViewer.getTable();
final IFocusedTableActivator activator = new IFocusedTableActivator() {
@Override
public void copy(Clipboard clipboard) {
//Synthetic comment -- @@ -1162,11 +1151,11 @@

@Override
public void selectAll() {
                table.selectAll();
}
};

        table.addFocusListener(new FocusListener() {
@Override
public void focusGained(FocusEvent e) {
mTableFocusListener.focusGained(activator);
//Synthetic comment -- @@ -1198,6 +1187,6 @@

/** Select all items in the logcat table. */
public void selectAll() {
        mViewer.getTable().selectAll();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index c9606f6..8f2d52c 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
private LogCatOutputReceiver mCurrentLogCatOutputReceiver;
    private Set<ILogCatMessageEventListener> mLogCatMessageListeners;
private LogCatMessageParser mLogCatMessageParser;
private LogCatPidToNameMapper mPidToNameMapper;
private IPreferenceStore mPrefStore;
//Synthetic comment -- @@ -55,7 +55,7 @@
mCurrentDevice = device;
mPrefStore = prefStore;

        mLogCatMessageListeners = new HashSet<ILogCatMessageEventListener>();
mLogCatMessageParser = new LogCatMessageParser();
mPidToNameMapper = new LogCatPidToNameMapper(mCurrentDevice);

//Synthetic comment -- @@ -147,14 +147,16 @@
}

private void processLogLines(String[] lines) {
        List<LogCatMessage> messages = mLogCatMessageParser.processLogLines(lines,
mPidToNameMapper);

        if (messages.size() > 0) {
            for (LogCatMessage m : messages) {
                mLogMessages.appendMessage(m);
}
            sendMessageReceivedEvent(messages);
}
}

//Synthetic comment -- @@ -177,17 +179,18 @@
* Add to list of message event listeners.
* @param l listener to notified when messages are received from the device
*/
    public void addMessageReceivedEventListener(ILogCatMessageEventListener l) {
mLogCatMessageListeners.add(l);
}

    public void removeMessageReceivedEventListener(ILogCatMessageEventListener l) {
mLogCatMessageListeners.remove(l);
}

    private void sendMessageReceivedEvent(List<LogCatMessage> messages) {
        for (ILogCatMessageEventListener l : mLogCatMessageListeners) {
            l.messageReceived(messages);
}
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatViewerFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatViewerFilter.java
deleted file mode 100644
//Synthetic comment -- index f7b8dce..0000000

//Synthetic comment -- @@ -1,46 +0,0 @@
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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * A JFace {@link ViewerFilter} for the {@link LogCatPanel} displaying logcat messages.
 * This is a simple wrapper around {@link LogCatFilter}.
 */
public final class LogCatViewerFilter extends ViewerFilter {
    private LogCatFilter mFilter;

    /**
     * Construct a {@link ViewerFilter} filtering logcat messages based on
     * user provided filter settings for PID, Tag and log level.
     * @param filter filter to use
     */
    public LogCatViewerFilter(LogCatFilter filter) {
        mFilter = filter;
    }

    @Override
    public boolean select(Viewer viewer, Object parent, Object element) {
        if (!(element instanceof LogCatMessage)) {
            return false;
        }

        LogCatMessage m = (LogCatMessage) element;
        return mFilter.matches(m);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java
//Synthetic comment -- index f9c94a7..1e50822 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.logcat.ILogCatMessageEventListener;
import com.android.ddmuilib.logcat.LogCatMessage;
import com.android.ddmuilib.logcat.LogCatReceiver;
import com.android.ddmuilib.logcat.LogCatReceiverFactory;
//Synthetic comment -- @@ -98,7 +98,7 @@
return;
}

        data.receiver.removeMessageReceivedEventListener(data.messageEventListener);
}

public void monitorDevice(final IDevice device) {
//Synthetic comment -- @@ -113,10 +113,11 @@
}

LogCatReceiver r = LogCatReceiverFactory.INSTANCE.newReceiver(device, mPrefStore);
        ILogCatMessageEventListener l = new ILogCatMessageEventListener() {
@Override
            public void messageReceived(List<LogCatMessage> receivedMessages) {
                checkMessages(receivedMessages, device);
}
};
r.addMessageReceivedEventListener(l);
//Synthetic comment -- @@ -205,11 +206,11 @@

private static class DeviceData {
public final LogCatReceiver receiver;
        public final ILogCatMessageEventListener messageEventListener;

        public DeviceData(LogCatReceiver r, ILogCatMessageEventListener l) {
receiver = r;
            messageEventListener = l;
}
}
}







