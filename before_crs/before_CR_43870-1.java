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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
//Synthetic comment -- @@ -177,6 +178,11 @@
private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;

private String mLogFileExportFolder;

private Font mFont;
//Synthetic comment -- @@ -916,18 +922,26 @@
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
//Synthetic comment -- @@ -943,6 +957,24 @@
THUMB);             // page increment
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

// Obtain the list of new messages, and the number of deleted messages.
List<LogCatMessage> newMessages;
int deletedMessageCount;
//Synthetic comment -- @@ -1285,6 +1320,15 @@
}

mTable.setRedraw(true);
}

/**







