
//<Beginning of snippet n. 0>


"Maximum number of logcat messages to buffer",
getFieldEditorParent());
addField(maxMessages);

                BooleanFieldEditor autoScrollLock = new BooleanFieldEditor(
                        LogCatPanel.AUTO_SCROLL_LOCK_PREFKEY,
                        "Automatically enable/disable scroll lock based on the scrollbar position",
                        getFieldEditorParent());
                addField(autoScrollLock);
}
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.ITableFocusListener;
import com.android.ddmuilib.ITableFocusListener.IFocusedTableActivator;
import com.android.ddmuilib.ImageLoader;
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
/** Preference key to use for storing font settings. */
public static final String LOGCAT_VIEW_FONT_PREFKEY = "logcat.view.font";

    /** Preference key to use for deciding whether to automatically en/disable scroll lock. */
    public static final String AUTO_SCROLL_LOCK_PREFKEY = "logcat.view.auto-scroll-lock";

// Preference keys for message colors based on severity level
private static final String MSG_COLOR_PREFKEY_PREFIX = "logcat.msg.color.";
public static final String VERBOSE_COLOR_PREFKEY = MSG_COLOR_PREFKEY_PREFIX + "verbose"; //$NON-NLS-1$

private boolean mShouldScrollToLatestLog = true;
private ToolItem mScrollLockCheckBox;
    private boolean mAutoScrollLock;

private String mLogFileExportFolder;


mFont = getFontFromPrefStore();
loadMessageColorPreferences();
        mAutoScrollLock = mPrefStore.getBoolean(AUTO_SCROLL_LOCK_PREFKEY);
}

private void loadMessageColorPreferences() {
mPrefStore.setDefault(LogCatMessageList.MAX_MESSAGES_PREFKEY,
LogCatMessageList.MAX_MESSAGES_DEFAULT);
mPrefStore.setDefault(DISPLAY_FILTERS_COLUMN_PREFKEY, true);
        mPrefStore.setDefault(AUTO_SCROLL_LOCK_PREFKEY, true);

/* Default Colors for different log levels. */
PreferenceConverter.setDefault(mPrefStore, LogCatPanel.VERBOSE_COLOR_PREFKEY,
mReceiver.resizeFifo(mPrefStore.getInt(
LogCatMessageList.MAX_MESSAGES_PREFKEY));
reloadLogBuffer();
                } else if (changedProperty.equals(AUTO_SCROLL_LOCK_PREFKEY)) {
                    mAutoScrollLock = mPrefStore.getBoolean(AUTO_SCROLL_LOCK_PREFKEY);
}
}
});
mScrollLockCheckBox.setImage(
ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_SCROLL_LOCK,
toolBar.getDisplay()));
        mScrollLockCheckBox.setSelection(true);
mScrollLockCheckBox.setToolTipText("Scroll Lock");
mScrollLockCheckBox.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent event) {
boolean scrollLock = mScrollLockCheckBox.getSelection();
                setScrollToLatestLog(scrollLock);
}
});
}
dispose();
}
});

        final ScrollBar vbar = mTable.getVerticalBar();
        vbar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!mAutoScrollLock) {
                    return;
                }

                boolean isAtBottom = (vbar.getThumb() + vbar.getSelection()) == vbar.getMaximum();
                setScrollToLatestLog(isAtBottom);
                mScrollLockCheckBox.setSelection(isAtBottom);
            }
        });

        // Explicitly set the values to use for the scroll bar. In particular, we want these values
        // to have a high enough accuracy that even small movements of the scroll bar have an
        // effect on the selection. The auto scroll lock detection assumes that the scroll bar is
        // at the bottom iff selection + thumb == max.
        final int MAX = 10000;
        final int THUMB = 10;
        vbar.setValues(MAX - THUMB, // selection
                0,                  // min
                MAX,                // max
                THUMB,              // thumb
                1,                  // increment
                THUMB);             // page increment
}

/** Setup menu to be displayed when right clicking a log message. */

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private IntegerFieldEditor mMaxMessages;
private BooleanFieldEditor mAutoMonitorLogcat;
private ComboFieldEditor mAutoMonitorLogcatLevel;
    private BooleanFieldEditor mAutoScrollLock;

public LogCatPreferencePage() {
super(GRID);
Messages.LogCatPreferencePage_MaxMessages, getFieldEditorParent());
addField(mMaxMessages);

        mAutoScrollLock = new BooleanFieldEditor(LogCatPanel.AUTO_SCROLL_LOCK_PREFKEY,
                "Automatically enable/disable scroll lock based on the scrollbar position",
                getFieldEditorParent());
        addField(mAutoScrollLock);

createHorizontalSeparator();

if (InstallDetails.isAdtInstalled()) {

//<End of snippet n. 2>








