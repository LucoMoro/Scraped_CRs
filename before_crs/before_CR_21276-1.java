/*Adding right-click menu to LogCat DDMS.

Change-Id:Ib38a9ef4673f5f15b313aca4cba6f90f4a2e7dd0*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80ed6e9..6cd63fa 100644

//Synthetic comment -- @@ -42,9 +42,13 @@
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -54,6 +58,8 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
//Synthetic comment -- @@ -323,6 +329,51 @@
}

/**
* Create the log view with some default parameters
* @param colors The display color object
* @param filterStorage the storage for user defined filters.
//Synthetic comment -- @@ -394,6 +445,9 @@
// pass
}


/**
* Creates a control capable of displaying some information.  This is
//Synthetic comment -- @@ -410,6 +464,10 @@
// create the tab folder
mFolders = new TabFolder(top, SWT.NONE);
mFolders.setLayoutData(new GridData(GridData.FILL_BOTH));
mFolders.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -426,6 +484,28 @@
}
});


Composite bottom = new Composite(top, SWT.NONE);
bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));







