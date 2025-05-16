//<Beginning of snippet n. 0>


import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;
private boolean mUserScroll = false;

private String mLogFileExportFolder;
private Font mFont;

final ScrollBar vbar = mTable.getVerticalBar();
vbar.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        mUserScroll = true;

        boolean isAtBottom = (vbar.getThumb() + vbar.getSelection()) == vbar.getMaximum();
        setScrollToLatestLog(isAtBottom);
        mScrollLockCheckBox.setSelection(isAtBottom);
    }
});

// Explicitly set the values to use for the scroll bar. In particular, we want these values
// to have a high enough accuracy that even small movements of the scroll bar have an
THUMB);             // page increment
}

/** Setup menu to be displayed when right-clicking a log message. */
private void addRightClickMenu(final Table table) {
// This action will pop up a create filter dialog pre-populated with current selection
}

private void setScrollToLatestLog(boolean scroll) {
    mShouldScrollToLatestLog = scroll;

    if (scroll && !mUserScroll) {
        scrollToLatestLog();
    }

    mTable.setRedraw(false);

    // Obtain the list of new messages, and the number of deleted messages.
    List<LogCatMessage> newMessages;
    int deletedMessageCount;

    mTable.setRedraw(true);
}

private void scrollToLatestLog() {
    // scrolling logic to move to the latest log
}

//<End of snippet n. 0>