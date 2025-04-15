/*Add ability to right click on a message to create filter

This patch allows users to create logcat filters based on an
existing message. When a message is right clicked, it brings up
a menu with an entry that says "Filter similar messages..".
Selecting that will bring up the "Add new filter" dialog with
its input fields prefilled based on the message contents.

This fixes:http://code.google.com/p/android/issues/detail?id=25835Change-Id:If324330cbf78b503be61c31ebcff053d0f869c1e*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 45f0c69..d82895f 100644

//Synthetic comment -- @@ -25,6 +25,10 @@
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
//Synthetic comment -- @@ -49,6 +53,8 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
//Synthetic comment -- @@ -59,6 +65,7 @@
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
//Synthetic comment -- @@ -369,9 +376,11 @@
});
}

    private void addNewFilter(String defaultTag, String defaultText, String defaultPid,
            String defaultAppName, LogLevel defaultLevel) {
LogCatFilterSettingsDialog d = new LogCatFilterSettingsDialog(
Display.getCurrent().getActiveShell());
        d.setDefaults("", defaultTag, defaultText, defaultPid, defaultAppName, defaultLevel); //$NON-NLS-1$
if (d.open() != Window.OK) {
return;
}
//Synthetic comment -- @@ -394,6 +403,11 @@
saveFilterPreferences();
}

    private void addNewFilter() {
        addNewFilter("", "", "",            //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "", LogLevel.VERBOSE);      //$NON-NLS-1$
    }

private void deleteSelectedFilter() {
int selectedIndex = mFiltersTableViewer.getTable().getSelectionIndex();
if (selectedIndex <= 0) {
//Synthetic comment -- @@ -794,10 +808,55 @@
}
});

        addRightClickMenu(mTable);
initDoubleClickListener();
recomputeWrapWidth();
}

    /** Setup menu to be displayed when right clicking a log message. */
    private void addRightClickMenu(final Table table) {
        // This action will pop up a create filter dialog pre-populated with current selection
        final Action filterAction = new Action("Filter similar messages..") {
            @Override
            public void run() {
                List<LogCatMessage> selectedMessages = getSelectedLogCatMessages();
                if (selectedMessages.size() == 0) {
                    addNewFilter();
                } else {
                    LogCatMessage m = selectedMessages.get(0);
                    addNewFilter(m.getTag(), m.getMessage(), m.getPid(), m.getAppName(),
                            m.getLogLevel());
                }
            }
        };

        final MenuManager mgr = new MenuManager();
        mgr.setRemoveAllWhenShown(true);
        mgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager arg0) {
                mgr.add(filterAction);
            }
        });
        final Menu menu = mgr.createContextMenu(table);

        table.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Point pt = table.getDisplay().map(null, table, new Point(event.x, event.y));
                Rectangle clientArea = table.getClientArea();

                // The click location is in the header if it is between
                // clientArea.y and clientArea.y + header height
                boolean header = pt.y > clientArea.y
                                    && pt.y < (clientArea.y + table.getHeaderHeight());

                // Show the menu only if it is not inside the header
                table.setMenu(header ? null : menu);
            }
        });
    }

public void recomputeWrapWidth() {
if (mTable == null || mTable.isDisposed()) {
return;







