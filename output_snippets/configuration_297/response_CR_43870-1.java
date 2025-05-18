//<Beginning of snippet n. 0>

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;
private boolean mShouldScrollToLatestLog;

private String mLogFileExportFolder;

private Font mFont;

final ScrollBar vbar = mTable.getVerticalBar();
vbar.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        if (!mAutoScrollLock) {
            return;
        }
        boolean isAtBottom = (vbar.getThumb() + vbar.getSelection()) == vbar.getMaximum();
        mScrollLockCheckBox.setSelection(isAtBottom);
        if (isAtBottom) {
            setScrollToLatestLog(true);
        }
    }
});

/** Setup menu to be displayed when right clicking a log message. */
private void addRightClickMenu(final Table table) {
    // This action will pop up a create filter dialog pre-populated with current selection
}

private void setScrollToLatestLog(boolean scroll) {
    mShouldScrollToLatestLog = scroll;

    if (scroll) {
        boolean isAtBottom = (mTable.getVerticalBar().getThumb() + mTable.getVerticalBar().getSelection()) == mTable.getVerticalBar().getMaximum();
        if (isAtBottom) {
            scrollToLatestLog();
        }
    }

    mTable.setRedraw(false);

    // Obtain the list of new messages and the number of deleted messages.
    List<LogCatMessage> newMessages;
    int deletedMessageCount;

    mTable.setRedraw(true);
}

//<End of snippet n. 0>