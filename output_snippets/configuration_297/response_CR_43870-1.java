//<Beginning of snippet n. 0>


import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;
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
        if (isAtBottom) {
            setScrollToLatestLog(true);
            mScrollLockCheckBox.setSelection(true);
        } else {
            mScrollLockCheckBox.setSelection(false);
        }
    }
});

// Explicitly set the values to use for the scroll bar. In particular, we want these values
// to have a high enough accuracy that even small movements of the scroll bar have an

private void setScrollToLatestLog(boolean scroll) {
    if (!mAutoScrollLock && !isUserAtBottom()) {
        return;
    }

    mShouldScrollToLatestLog = scroll;

    if (scroll) {
        scrollToLatestLog();
    }

    mTable.setRedraw(false);

    // Obtain the list of new messages, and the number of deleted messages.
    List<LogCatMessage> newMessages;
    int deletedMessageCount;

    mTable.setRedraw(true);
}

// Method to check if the user is currently at the bottom of the scroll
private boolean isUserAtBottom() {
    return (vbar.getThumb() + vbar.getSelection()) == vbar.getMaximum();
}

/**

//<End of snippet n. 0>