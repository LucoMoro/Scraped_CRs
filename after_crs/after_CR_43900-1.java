/*SDK Manager: fix window width.

SDK Bug 38063: "Manage Add-on siter" table doesn't follow size properly.
SDK Bug 38064: Install/Delete buttons cut off if window not large enough.

Change-Id:I0169177b8bd1f0d8f24ce5a0d108dc00d26aebbe*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 6174ba6..2b9f072 100755

//Synthetic comment -- @@ -134,7 +134,7 @@
GridLayoutBuilder.create(root).columns(3);

Label label = new Label(root, SWT.NONE);
        GridDataBuilder.create(label).hGrab().vCenter().hSpan(3);
label.setText(
"This lets select which official 3rd-party sites you want to load.\n" +
"\n" +








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 025be46..bee159c 100755

//Synthetic comment -- @@ -289,9 +289,9 @@

mGroupOptions = new Composite(groupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
        GridLayoutBuilder.create(mGroupOptions).columns(7).noMargins();

        // Options line 1, 7 columns

Label label3 = new Label(mGroupOptions, SWT.NONE);
label3.setText("Show:");
//Synthetic comment -- @@ -337,7 +337,7 @@
linkSelectNew.setText(
String.format("Select <a>%1$s</a> or <a>%2$s</a>", strLinkNew, strLinkUpdates));
linkSelectNew.setToolTipText("Selects all items that are either new or updates.");
        GridDataBuilder.create(linkSelectNew).hFill();
linkSelectNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -347,10 +347,14 @@
}
});

        // placeholder between "select all" and "install"
        Label placeholder = new Label(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(placeholder).hFill().hGrab();

mButtonInstall = new Button(mGroupOptions, SWT.NONE);
mButtonInstall.setText("");  //$NON-NLS-1$  placeholder, filled in updateButtonsState()
mButtonInstall.setToolTipText("Install one or more packages");
        GridDataBuilder.create(mButtonInstall).vCenter().wHint(120);
mButtonInstall.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -358,7 +362,7 @@
}
});

        // Options line 2, 7 columns

Label label2 = new Label(mGroupOptions, SWT.NONE);
label2.setText("Sort by:");
//Synthetic comment -- @@ -392,12 +396,13 @@
}
});

        // placeholder between "repository" and "deselect"
new Label(mGroupOptions, SWT.NONE);

Link linkDeselect = new Link(mGroupOptions, SWT.NONE);
linkDeselect.setText("<a>Deselect All</a>");
linkDeselect.setToolTipText("Deselects all the currently selected items");
        GridDataBuilder.create(linkDeselect).hFill();
linkDeselect.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -406,10 +411,14 @@
}
});

        // placeholder between "deselect" and "delete"
        placeholder = new Label(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(placeholder).hFill().hGrab();

mButtonDelete = new Button(mGroupOptions, SWT.NONE);
mButtonDelete.setText("");  //$NON-NLS-1$  placeholder, filled in updateButtonsState()
mButtonDelete.setToolTipText("Delete one ore more installed packages");
        GridDataBuilder.create(mButtonDelete).vCenter().wHint(120);
mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 41235c6..69c8518 100755

//Synthetic comment -- @@ -181,7 +181,7 @@
glShell.marginHeight = 0;
mShell.setLayout(glShell);

        mShell.setMinimumSize(new Point(550, 300));
mShell.setSize(700, 500);
mShell.setText(APP_NAME);








