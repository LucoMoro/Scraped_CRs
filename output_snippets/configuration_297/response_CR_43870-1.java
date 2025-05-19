//<Beginning of snippet n. 0>

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

private ToolItem mScrollLockCheckBox;
private boolean mAutoScrollLock;

final ScrollBar vbar = mTable.getVerticalBar();
vbar.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        boolean isUserScroll = (e.detail == SWT.Selection);
        if (!mAutoScrollLock) {
            return;
        }

        boolean isAtBottom = (vbar.getThumb() + vbar.getSelection() >= vbar.getMaximum() - vbar.getThumb());
        if (isUserScroll && isAtBottom) {
            setScrollToLatestLog(true);
        }
        mScrollLockCheckBox.setSelection(isAtBottom);
    }
});

/** Setup menu to be displayed when right clicking a log message. */
private void addRightClickMenu(final Table table) {
// This action will pop up a create filter dialog pre-populated with current selection
}

private void setScrollToLatestLog(boolean scroll) {
    mShouldScrollToLatestLog = scroll;

    if (scroll) {
        if (!mAutoScrollLock) {
            return;
        }
        scrollToLatestLog();
    }
}

//<End of snippet n. 0>