/*logcat: Support auto scroll lock.

Another go at adding support for automatically enabling/disabling
scroll lock. Past attempts have been brittle because of differences
in how Windows, Linux and Mac handle the scrollbar.

The key to getting this work consistently across platforms seems
to be in explicitly setting the range of values for the scrollbar.
Setting the maximum to be a high enough value relative to the
thumb allows accurate detection of even minor movements away from
the bottom of the scrollbar.

Change-Id:Ic3259cc4e2e8a20f3a87ce4bf234217cea792f88*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/PrefsDialog.java b/ddms/app/src/com/android/ddms/PrefsDialog.java
//Synthetic comment -- index 84304df..acadeb8 100644

//Synthetic comment -- @@ -461,6 +461,12 @@
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








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index e7dcec9..9f38b29 100644

//Synthetic comment -- @@ -19,13 +19,13 @@
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
//Synthetic comment -- @@ -69,6 +69,7 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
//Synthetic comment -- @@ -97,6 +98,9 @@
/** Preference key to use for storing font settings. */
public static final String LOGCAT_VIEW_FONT_PREFKEY = "logcat.view.font";

    /** Preference key to use for deciding whether to automatically en/disable scroll lock. */
    public static final String AUTO_SCROLL_LOCK_PREFKEY = "logcat.view.auto-scroll-lock";

// Preference keys for message colors based on severity level
private static final String MSG_COLOR_PREFKEY_PREFIX = "logcat.msg.color.";
public static final String VERBOSE_COLOR_PREFKEY = MSG_COLOR_PREFKEY_PREFIX + "verbose"; //$NON-NLS-1$
//Synthetic comment -- @@ -171,6 +175,7 @@

private boolean mShouldScrollToLatestLog = true;
private ToolItem mScrollLockCheckBox;
    private boolean mAutoScrollLock;

private String mLogFileExportFolder;

//Synthetic comment -- @@ -207,6 +212,7 @@

mFont = getFontFromPrefStore();
loadMessageColorPreferences();
        mAutoScrollLock = mPrefStore.getBoolean(AUTO_SCROLL_LOCK_PREFKEY);
}

private void loadMessageColorPreferences() {
//Synthetic comment -- @@ -244,6 +250,7 @@
mPrefStore.setDefault(LogCatMessageList.MAX_MESSAGES_PREFKEY,
LogCatMessageList.MAX_MESSAGES_DEFAULT);
mPrefStore.setDefault(DISPLAY_FILTERS_COLUMN_PREFKEY, true);
        mPrefStore.setDefault(AUTO_SCROLL_LOCK_PREFKEY, true);

/* Default Colors for different log levels. */
PreferenceConverter.setDefault(mPrefStore, LogCatPanel.VERBOSE_COLOR_PREFKEY,
//Synthetic comment -- @@ -298,6 +305,8 @@
mReceiver.resizeFifo(mPrefStore.getInt(
LogCatMessageList.MAX_MESSAGES_PREFKEY));
reloadLogBuffer();
                } else if (changedProperty.equals(AUTO_SCROLL_LOCK_PREFKEY)) {
                    mAutoScrollLock = mPrefStore.getBoolean(AUTO_SCROLL_LOCK_PREFKEY);
}
}
});
//Synthetic comment -- @@ -682,13 +691,13 @@
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
//Synthetic comment -- @@ -905,6 +914,33 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java
//Synthetic comment -- index a0c5450..aa88eec 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
private IntegerFieldEditor mMaxMessages;
private BooleanFieldEditor mAutoMonitorLogcat;
private ComboFieldEditor mAutoMonitorLogcatLevel;
    private BooleanFieldEditor mAutoScrollLock;

public LogCatPreferencePage() {
super(GRID);
//Synthetic comment -- @@ -65,6 +66,11 @@
Messages.LogCatPreferencePage_MaxMessages, getFieldEditorParent());
addField(mMaxMessages);

        mAutoScrollLock = new BooleanFieldEditor(LogCatPanel.AUTO_SCROLL_LOCK_PREFKEY,
                "Automatically enable/disable scroll lock based on the scrollbar position",
                getFieldEditorParent());
        addField(mAutoScrollLock);

createHorizontalSeparator();

if (InstallDetails.isAdtInstalled()) {







