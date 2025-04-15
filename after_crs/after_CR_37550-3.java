/*logcat: Support searching through the message list.

This CL adds support for invoking a find dialog when
the logcat view is in focus. The dialog can be invoked
via Edit -> Find (Ctrl + F), or via the context menu.

The dialog provides a way to specify a search term and
allows the user to search forward or backward in the
list of messages.

Change-Id:I7e7c6b20a051c161f035b3b45aba5f119f2c11a9*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..f86d967

//Synthetic comment -- @@ -0,0 +1,123 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmuilib.logcat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * {@link FindDialog} provides a text box where users can enter text that should be
 * searched for in the list of logcat messages. The buttons "Find Previous" and "Find Next"
 * allow users to search forwards/backwards. This dialog simply provides a front end for the user
 * and the actual task of searching is delegated to the {@link IFindTarget}.
 */
public class FindDialog extends Dialog {
    private Label mStatusLabel;
    private Button mFindNext;
    private Button mFindPrevious;
    private final IFindTarget mTarget;
    private Text mSearchText;
    private String mPreviousSearchText;

    private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
    private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;

    public FindDialog(Shell shell, IFindTarget target) {
        super(shell);

        mTarget = target;

        setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
        setBlockOnOpen(true);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label lblMessage = new Label(panel, SWT.NONE);
        lblMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblMessage.setText("Find:");

        mSearchText = new Text(panel, SWT.BORDER);
        mSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mSearchText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                boolean hasText = !mSearchText.getText().isEmpty();
                mFindNext.setEnabled(hasText);
                mFindPrevious.setEnabled(hasText);
            }
        });

        mStatusLabel = new Label(panel, SWT.NONE);
        mStatusLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        gd.grabExcessHorizontalSpace = true;
        mStatusLabel.setLayoutData(gd);

        return panel;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
        mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", false);
        mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", /* default */ true);
        mFindNext.setEnabled(false);
        mFindPrevious.setEnabled(false);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.CLOSE_ID) {
            close();
            return;
        }

        if (buttonId == FIND_PREVIOUS_ID || buttonId == FIND_NEXT_ID) {
            if (mTarget != null) {
                String searchText = mSearchText.getText();
                boolean newSearch = !searchText.equals(mPreviousSearchText);
                mPreviousSearchText = searchText;
                boolean searchForward = buttonId == FIND_NEXT_ID;

                boolean hasMatches = mTarget.findAndSelect(searchText, newSearch, searchForward);
                if (!hasMatches) {
                    mStatusLabel.setText("String not found");
                    mStatusLabel.pack();
                } else {
                    mStatusLabel.setText("");
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java
new file mode 100644
//Synthetic comment -- index 0000000..f27c53e

//Synthetic comment -- @@ -0,0 +1,21 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmuilib.logcat;

public interface IFindTarget {
    boolean findAndSelect(String text, boolean isNewSearch, boolean searchForward);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index e2fb22f..06952f9 100644

//Synthetic comment -- @@ -908,7 +908,7 @@
/** Setup menu to be displayed when right clicking a log message. */
private void addRightClickMenu(final Table table) {
// This action will pop up a create filter dialog pre-populated with current selection
        final Action filterAction = new Action("Filter similar messages...") {
@Override
public void run() {
List<LogCatMessage> selectedMessages = getSelectedLogCatMessages();
//Synthetic comment -- @@ -922,8 +922,16 @@
}
};

        final Action findAction = new Action("Find...") {
            @Override
            public void run() {
                showFindDialog();
            };
        };

final MenuManager mgr = new MenuManager();
mgr.add(filterAction);
        mgr.add(findAction);
final Menu menu = mgr.createContextMenu(table);

table.addListener(SWT.MenuDetect, new Listener() {
//Synthetic comment -- @@ -1172,6 +1180,8 @@

deletedMessageCount = mDeletedLogCount;
mDeletedLogCount = 0;

                mFindTarget.scrollBy(deletedMessageCount);
}

int originalItemCount = mTable.getItemCount();
//Synthetic comment -- @@ -1432,4 +1442,53 @@
mErrorColor.dispose();
mAssertColor.dispose();
}

    private class LogcatFindTarget extends RollingBufferFindTarget {
        @Override
        public void selectAndReveal(int index) {
            mTable.deselectAll();
            mTable.select(index);
            mTable.showSelection();
        }

        @Override
        public int getItemCount() {
            return mTable.getItemCount();
        }

        @Override
        public String getItem(int index) {
            Object data = mTable.getItem(index).getData();
            if (data != null) {
                return data.toString();
            }

            return null;
        }

        @Override
        public int getStartingIndex() {
            // start searches from current selection if present, otherwise from the tail end
            // of the buffer
            int s = mTable.getSelectionIndex();
            if (s != -1) {
                return s;
            } else {
                return getItemCount() - 1;
            }
        };
    };

    private FindDialog mFindDialog;
    private LogcatFindTarget mFindTarget = new LogcatFindTarget();
    public void showFindDialog() {
        if (mFindDialog != null) {
            // if the dialog is already displayed
            return;
        }

        mFindDialog = new FindDialog(Display.getDefault().getActiveShell(), mFindTarget);
        mFindDialog.open(); // blocks until find dialog is closed
        mFindDialog = null;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java
new file mode 100644
//Synthetic comment -- index 0000000..b353a13

//Synthetic comment -- @@ -0,0 +1,115 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmuilib.logcat;

import java.util.regex.Pattern;

/**
 * {@link RollingBufferFindTarget} implements methods to find items inside a circular buffer.
 */
public abstract class RollingBufferFindTarget implements IFindTarget {
    private int mCurrentSearchIndex;

    // Single element cache of the last search regex
    private Pattern mLastSearchPattern;
    private String mLastSearchText;

    @Override
    public boolean findAndSelect(String text, boolean isNewSearch, boolean searchForward) {
        boolean found = false;
        int maxIndex = getItemCount();

        synchronized (this) {
            // Find starting index for this search
            if (isNewSearch) {
                // for new searches, start from an appropriate place as provided by the delegate
                mCurrentSearchIndex = getStartingIndex();
            } else {
                // for ongoing searches (finding next match for the same term), continue from
                // the current result index
                mCurrentSearchIndex = getNext(mCurrentSearchIndex, searchForward, maxIndex);
            }

            // Create a regex pattern based on the search term.
            Pattern pattern;
            if (text.equals(mLastSearchText)) {
                pattern = mLastSearchPattern;
            } else {
                pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
                mLastSearchPattern = pattern;
                mLastSearchText = text;
            }

            // Iterate through the list of items. The search ends if we have gone through
            // all items once.
            int index = mCurrentSearchIndex;
            do {
                String msgText = getItem(mCurrentSearchIndex);
                if (msgText != null && pattern.matcher(msgText).find()) {
                    found = true;
                    break;
                }

                mCurrentSearchIndex = getNext(mCurrentSearchIndex, searchForward, maxIndex);
            } while (index != mCurrentSearchIndex); // loop through entire contents once
        }

        if (found) {
            selectAndReveal(mCurrentSearchIndex);
        }

        return found;
    }

    /** Indicate that the log buffer has scrolled by certain number of elements */
    public void scrollBy(int delta) {
        synchronized (this) {
            if (mCurrentSearchIndex > 0) {
                mCurrentSearchIndex = Math.max(0, mCurrentSearchIndex - delta);
            }
        }
    }

    private int getNext(int index, boolean searchForward, int max) {
        // increment or decrement index
        index = searchForward ? index + 1 : index - 1;

        // take care of underflow
        if (index == -1) {
            index = max - 1;
        }

        // ..and overflow
        if (index == max) {
            index = 0;
        }

        return index;
    }

    /** Obtain the number of items in the buffer */
    public abstract int getItemCount();

    /** Obtain the item at given index */
    public abstract String getItem(int index);

    /** Select and reveal the item at given index */
    public abstract void selectAndReveal(int index);

    /** Obtain the index from which search should begin */
    public abstract int getStartingIndex();
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7afac24

//Synthetic comment -- @@ -0,0 +1,106 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmuilib.logcat;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class RollingBufferFindTest extends TestCase {
    public class FindTarget extends RollingBufferFindTarget {
        private int mSelectedItem = -1;
        private int mItemReadCount = 0;
        private List<String> mItems = Arrays.asList(
                "abc",
                "def",
                "abc",
                null,
                "xyz"
        );

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public String getItem(int index) {
            mItemReadCount++;
            return mItems.get(index);
        }

        @Override
        public void selectAndReveal(int index) {
            mSelectedItem = index;
        }

        @Override
        public int getStartingIndex() {
            return mItems.size() - 1;
        }
    }
    FindTarget mFindTarget = new FindTarget();

    public void testMultipleMatch() {
        mFindTarget.mSelectedItem = -1;

        String text = "abc";
        int lastIndex = mFindTarget.mItems.lastIndexOf(text);
        int firstIndex = mFindTarget.mItems.indexOf(text);

        // the first time we search through the buffer we should hit the item at lastIndex
        assertTrue(mFindTarget.findAndSelect(text, true, false));
        assertEquals(lastIndex, mFindTarget.mSelectedItem);

        // subsequent search should hit the item at first index
        assertTrue(mFindTarget.findAndSelect(text, false, false));
        assertEquals(firstIndex, mFindTarget.mSelectedItem);

        // search again should roll over and hit the last index
        assertTrue(mFindTarget.findAndSelect(text, false, false));
        assertEquals(lastIndex, mFindTarget.mSelectedItem);
    }

    public void testMissingItem() {
        mFindTarget.mSelectedItem = -1;
        mFindTarget.mItemReadCount = 0;

        // should not match
        assertFalse(mFindTarget.findAndSelect("nonexistent", true, false));

        // no item should be selected
        assertEquals(-1, mFindTarget.mSelectedItem);

        // but all items should have been read in once
        assertEquals(mFindTarget.getItemCount(), mFindTarget.mItemReadCount);
    }

    public void testSearchDirection() {
        String text = "abc";
        int lastIndex = mFindTarget.mItems.lastIndexOf(text);
        int firstIndex = mFindTarget.mItems.indexOf(text);

        // the first time we search through the buffer we should hit the "abc" from the last
        assertTrue(mFindTarget.findAndSelect(text, true, false));
        assertEquals(lastIndex, mFindTarget.mSelectedItem);

        // searching forward from there should also hit the first index
        assertTrue(mFindTarget.findAndSelect(text, false, true));
        assertEquals(firstIndex, mFindTarget.mSelectedItem);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index bf14de3..3514db0 100644

//Synthetic comment -- @@ -81,6 +81,14 @@
mLogCatPanel.selectAll();
}
});

        actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),
                new Action("Find") {
            @Override
            public void run() {
                mLogCatPanel.showFindDialog();
            }
        });
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorActionBarAdvisor.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorActionBarAdvisor.java
//Synthetic comment -- index 4cf7a94..e31e45e 100644

//Synthetic comment -- @@ -25,11 +25,14 @@
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

public class MonitorActionBarAdvisor extends ActionBarAdvisor {
private IWorkbenchAction mQuitAction;
private IWorkbenchAction mCopyAction;
private IWorkbenchAction mSelectAllAction;
    private IWorkbenchAction mFindAction;
private IWorkbenchAction mOpenPerspectiveAction;
private IWorkbenchAction mResetPerspectiveAction;
private IWorkbenchAction mPreferencesAction;
//Synthetic comment -- @@ -50,6 +53,10 @@
mSelectAllAction = ActionFactory.SELECT_ALL.create(window);
register(mSelectAllAction);

        mFindAction = ActionFactory.FIND.create(window);
        mFindAction.setText("Find...");     // replace the default "Find and Replace..."
        register(mFindAction);

mOpenPerspectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
register(mOpenPerspectiveAction);

//Synthetic comment -- @@ -83,6 +90,7 @@
// contents of Edit menu
editMenu.add(mCopyAction);
editMenu.add(mSelectAllAction);
        editMenu.add(mFindAction);

// contents of Window menu
windowMenu.add(mOpenPerspectiveAction);







