/*logcat: Only listen to user generated scroll events

Scrolling behavior should not be altered when the scroll bar
location changes due to the addition of new messages.

Change-Id:Id38deb9f17d6d58bea6f25b33fed23c887c5cc5d*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 9f38b29..b195707 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
//Synthetic comment -- @@ -177,6 +178,11 @@
private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;

    // Lock under which the vertical scroll bar listener should be added
    private final Object mScrollBarSelectionListenerLock = new Object();
    private SelectionListener mScrollBarSelectionListener;
    private boolean mScrollBarListenerSet = false;

private String mLogFileExportFolder;

private Font mFont;
//Synthetic comment -- @@ -916,18 +922,26 @@
});

final ScrollBar vbar = mTable.getVerticalBar();
        mScrollBarSelectionListener = new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
if (!mAutoScrollLock) {
return;
}

                // thumb + selection < max => bar is not at the bottom.
                // we subtract an arbitrary amount (100 below) to make sure that instances like
                // half a line displayed don't alter the status
                int diff = vbar.getThumb() + vbar.getSelection() - vbar.getMaximum();
                boolean isAtBottom = Math.abs(diff) < 100;

                if (isAtBottom != mShouldScrollToLatestLog) {
                    setScrollToLatestLog(isAtBottom);
                    mScrollLockCheckBox.setSelection(isAtBottom);
                }
}
        };
        startScrollBarMonitor(vbar);

// Explicitly set the values to use for the scroll bar. In particular, we want these values
// to have a high enough accuracy that even small movements of the scroll bar have an
//Synthetic comment -- @@ -943,6 +957,24 @@
THUMB);             // page increment
}

    private void startScrollBarMonitor(ScrollBar vbar) {
        synchronized (mScrollBarSelectionListenerLock) {
            if (!mScrollBarListenerSet) {
                mScrollBarListenerSet = true;
                vbar.addSelectionListener(mScrollBarSelectionListener);
            }
        }
    }

    private void stopScrollBarMonitor(ScrollBar vbar) {
        synchronized (mScrollBarSelectionListenerLock) {
            if (mScrollBarListenerSet) {
                mScrollBarListenerSet = false;
                vbar.removeSelectionListener(mScrollBarSelectionListener);
            }
        }
    }

/** Setup menu to be displayed when right clicking a log message. */
private void addRightClickMenu(final Table table) {
// This action will pop up a create filter dialog pre-populated with current selection
//Synthetic comment -- @@ -1013,7 +1045,6 @@

private void setScrollToLatestLog(boolean scroll) {
mShouldScrollToLatestLog = scroll;
if (scroll) {
scrollToLatestLog();
}
//Synthetic comment -- @@ -1209,6 +1240,10 @@

mTable.setRedraw(false);

            // the scroll bar should only listen to user generated scroll events, not the
            // scroll events that happen due to the addition of logs
            stopScrollBarMonitor(mTable.getVerticalBar());

// Obtain the list of new messages, and the number of deleted messages.
List<LogCatMessage> newMessages;
int deletedMessageCount;
//Synthetic comment -- @@ -1285,6 +1320,15 @@
}

mTable.setRedraw(true);

            // re-enable listening to scroll bar events, but do so in a separate thread to make
            // sure that the current task (LogCatRefresherTask) has completed first
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    startScrollBarMonitor(mTable.getVerticalBar());
                }
            });
}

/**







