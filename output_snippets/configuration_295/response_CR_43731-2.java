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

public static final String LOGCAT_VIEW_FONT_PREFKEY = "logcat.view.font";

// Preference keys for message colors based on severity level
private static final String MSG_COLOR_PREFKEY_PREFIX = "logcat.msg.color.";
public static final String VERBOSE_COLOR_PREFKEY = MSG_COLOR_PREFKEY_PREFIX + "verbose"; //$NON-NLS-1$

private boolean mShouldScrollToLatestLog = true;
private ToolItem mScrollLockCheckBox;

private String mLogFileExportFolder;
private static final int MAX_MESSAGES_DEFAULT = 1000;  // Example default value, set appropriately
private static final String MAX_MESSAGES_PREFKEY = "max.messages.pref.key"; // Example key, replace as necessary
private int mMaxMessages;
private int mMaxScrollValue;
private static final int SCROLLBAR_MIN_VALUE = 0;
private int scrollThreshold = 5; // Flexible threshold for scrollbar

mFont = getFontFromPrefStore();
loadMessageColorPreferences();
}

private void loadMessageColorPreferences() {
mPrefStore.setDefault(MAX_MESSAGES_PREFKEY, MAX_MESSAGES_DEFAULT);
mPrefStore.setDefault(DISPLAY_FILTERS_COLUMN_PREFKEY, true);

/* Default Colors for different log levels. */
PreferenceConverter.setDefault(mPrefStore, LogCatPanel.VERBOSE_COLOR_PREFKEY,
mReceiver.resizeFifo(mPrefStore.getInt(MAX_MESSAGES_PREFKEY));
updateMaxScrollValue();
reloadLogBuffer();
}
}
});
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
if (!scrollLock) {
    updateScrollBar();
}
updateScrollLockIndicator();
mShouldScrollToLatestLog = !scrollLock; // Synchronize the state
}
});
}
dispose();
}
});
}

/** Setup menu to be displayed when right clicking a log message. */

private void updateMaxScrollValue() {
mMaxScrollValue = Math.max(mMaxMessages, SCROLLBAR_MIN_VALUE); // Ensure mMaxScrollValue adapts
}

private void updateScrollLockIndicator() {
if (mShouldScrollToLatestLog) {
    // Update visual feedback for scroll lock state
} else {
    // Update visual feedback for unlocked state
}
}

private boolean isScrollbarNearBottom() {
int scrollPosition = getScrollPosition();
return (scrollPosition >= mMaxScrollValue - scrollThreshold); // Use adaptable threshold
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private IntegerFieldEditor mMaxMessages;
private BooleanFieldEditor mAutoMonitorLogcat;
private ComboFieldEditor mAutoMonitorLogcatLevel;

public LogCatPreferencePage() {
super(GRID);
mMaxMessages = new IntegerFieldEditor(MAX_MESSAGES_PREFKEY, 
Messages.LogCatPreferencePage_MaxMessages, getFieldEditorParent());
mMaxMessages.setValidRange(1, Integer.MAX_VALUE); // Ensure valid range is set
addField(mMaxMessages);

createHorizontalSeparator();

if (InstallDetails.isAdtInstalled()) {

//<End of snippet n. 2>