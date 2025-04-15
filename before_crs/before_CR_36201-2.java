/*Add ability to right click on a message to create filter

This patch allows users to create logcat filters based on an
existing message. When a message is right clicked, it brings up
a menu with an entry that says "Filter similar messages..".
Selecting that will bring up the "Add new filter" dialog with
its input fields prefilled based on the message contents.

This fixes:http://code.google.com/p/android/issues/detail?id=25835Change-Id:If324330cbf78b503be61c31ebcff053d0f869c1e*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 45f0c69..53255cb 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
//Synthetic comment -- @@ -49,6 +51,8 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
//Synthetic comment -- @@ -59,6 +63,7 @@
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
//Synthetic comment -- @@ -369,9 +374,11 @@
});
}

    private void addNewFilter() {
LogCatFilterSettingsDialog d = new LogCatFilterSettingsDialog(
Display.getCurrent().getActiveShell());
if (d.open() != Window.OK) {
return;
}
//Synthetic comment -- @@ -394,6 +401,11 @@
saveFilterPreferences();
}

private void deleteSelectedFilter() {
int selectedIndex = mFiltersTableViewer.getTable().getSelectionIndex();
if (selectedIndex <= 0) {
//Synthetic comment -- @@ -794,10 +806,49 @@
}
});

initDoubleClickListener();
recomputeWrapWidth();
}

public void recomputeWrapWidth() {
if (mTable == null || mTable.isDisposed()) {
return;







