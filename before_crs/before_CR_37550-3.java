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








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java
new file mode 100644
//Synthetic comment -- index 0000000..f27c53e

//Synthetic comment -- @@ -0,0 +1,21 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index e2fb22f..06952f9 100644

//Synthetic comment -- @@ -908,7 +908,7 @@
/** Setup menu to be displayed when right clicking a log message. */
private void addRightClickMenu(final Table table) {
// This action will pop up a create filter dialog pre-populated with current selection
        final Action filterAction = new Action("Filter similar messages..") {
@Override
public void run() {
List<LogCatMessage> selectedMessages = getSelectedLogCatMessages();
//Synthetic comment -- @@ -922,8 +922,16 @@
}
};

final MenuManager mgr = new MenuManager();
mgr.add(filterAction);
final Menu menu = mgr.createContextMenu(table);

table.addListener(SWT.MenuDetect, new Listener() {
//Synthetic comment -- @@ -1172,6 +1180,8 @@

deletedMessageCount = mDeletedLogCount;
mDeletedLogCount = 0;
}

int originalItemCount = mTable.getItemCount();
//Synthetic comment -- @@ -1432,4 +1442,53 @@
mErrorColor.dispose();
mAssertColor.dispose();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java
new file mode 100644
//Synthetic comment -- index 0000000..b353a13

//Synthetic comment -- @@ -0,0 +1,115 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7afac24

//Synthetic comment -- @@ -0,0 +1,106 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index bf14de3..3514db0 100644

//Synthetic comment -- @@ -81,6 +81,14 @@
mLogCatPanel.selectAll();
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

public class MonitorActionBarAdvisor extends ActionBarAdvisor {
private IWorkbenchAction mQuitAction;
private IWorkbenchAction mCopyAction;
private IWorkbenchAction mSelectAllAction;
private IWorkbenchAction mOpenPerspectiveAction;
private IWorkbenchAction mResetPerspectiveAction;
private IWorkbenchAction mPreferencesAction;
//Synthetic comment -- @@ -50,6 +53,10 @@
mSelectAllAction = ActionFactory.SELECT_ALL.create(window);
register(mSelectAllAction);

mOpenPerspectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
register(mOpenPerspectiveAction);

//Synthetic comment -- @@ -83,6 +90,7 @@
// contents of Edit menu
editMenu.add(mCopyAction);
editMenu.add(mSelectAllAction);

// contents of Window menu
windowMenu.add(mOpenPerspectiveAction);







