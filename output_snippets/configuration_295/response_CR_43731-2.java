//<Beginning of snippet n. 0>


"Maximum number of logcat messages to buffer",
getFieldEditorParent());
addField(maxMessages);
}
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.ITableFocusListener;
import com.android.ddmuilib.ITableFocusListener.IFocusedTableActivator;
import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.ImageLoader;
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;
import com.android.ddmuilib.AbstractBufferFindTarget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/** Preference key to use for storing font settings. */
public static final String LOGCAT_VIEW_FONT_PREFKEY = "logcat.view.font";

// Preference keys for message colors based on severity level
private static final String MSG_COLOR_PREFKEY_PREFIX = "logcat.msg.color.";
public static final String VERBOSE_COLOR_PREFKEY = MSG_COLOR_PREFKEY_PREFIX + "verbose"; //$NON-NLS-1$

private boolean mShouldScrollToLatestLog = true;
private ToolItem mScrollLockCheckBox;
private String mLogFileExportFolder;

mFont = getFontFromPrefStore();
loadMessageColorPreferences();
}

private void loadMessageColorPreferences() {
    mPrefStore.setDefault(LogCatMessageList.MAX_MESSAGES_PREFKEY,
    LogCatMessageList.MAX_MESSAGES_DEFAULT);
    mPrefStore.setDefault(DISPLAY_FILTERS_COLUMN_PREFKEY, true);

    /* Default Colors for different log levels. */
    PreferenceConverter.setDefault(mPrefStore, LogCatPanel.VERBOSE_COLOR_PREFKEY,
    mReceiver.resizeFifo(mPrefStore.getInt(
    LogCatMessageList.MAX_MESSAGES_PREFKEY));
    reloadLogBuffer();
}

private void setupScrollLockCheckBox() {
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
            updateScrollLockStatus();
        }
    });
}

void updateScrollLockStatus() {
    if (mShouldScrollToLatestLog) {
        setScrollToLatestLog(false); // Disable scroll to latest if user interacts
    } else {
        // Logic to check if at bottom and enable scrolling
        if (isAtBottom()) {
            setScrollToLatestLog(true);
        }
    }
}

private boolean isAtBottom() {
    // Implement logic to check if the scrollbar is at the bottom
    return true; // Placeholder: replace with actual scroll position check
}

private void monitorScrollPosition() {
    // Logic to detect when user scrolls away from the bottom.
    // Implement monitoring to toggle mShouldScrollToLatestLog based on position.
}

/** Setup menu to be displayed when right clicking a log message. */

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private IntegerFieldEditor mMaxMessages;
private BooleanFieldEditor mAutoMonitorLogcat;
private ComboFieldEditor mAutoMonitorLogcatLevel;

public LogCatPreferencePage() {
    super(GRID);
    Messages.LogCatPreferencePage_MaxMessages, getFieldEditorParent());
    addField(mMaxMessages);

    createHorizontalSeparator();

    if (InstallDetails.isAdtInstalled()) {

//<End of snippet n. 2>