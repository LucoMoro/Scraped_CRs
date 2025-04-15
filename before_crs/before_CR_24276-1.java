/*SdkMan2: Links to select new/updates or deselect all.

Change-Id:I9c831138342574b0462e39055be86bde7114f54a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 1ca4d00..e858831 100755

//Synthetic comment -- @@ -59,6 +59,7 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -208,28 +209,28 @@
// (e.g. API level or source)
mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
mTreeColumnName = mColumnName.getColumn();
        mTreeColumnName.setWidth(340);
mTreeColumnName.setText("Name");

mColumnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn2 = mColumnApi.getColumn();
treeColumn2.setAlignment(SWT.CENTER);
treeColumn2.setWidth(50);
        treeColumn2.setText("API");

mColumnRevision = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn3 = mColumnRevision.getColumn();
        treeColumn3.setAlignment(SWT.CENTER);
        treeColumn3.setWidth(50);
treeColumn3.setText("Rev.");
treeColumn3.setToolTipText("Revision currently installed");


mColumnStatus = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn4 = mColumnStatus.getColumn();
treeColumn4.setAlignment(SWT.LEAD);
treeColumn4.setWidth(190);
        treeColumn4.setText("Status");

mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
//Synthetic comment -- @@ -241,6 +242,7 @@
label3.setText("Show:");

mCheckFilterNew = new Button(mGroupOptions, SWT.CHECK);
mCheckFilterNew.setToolTipText("Show Updates and New");
mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -249,7 +251,6 @@
}
});
mCheckFilterNew.setSelection(true);
        mCheckFilterNew.setText("Updates/New");

mCheckFilterInstalled = new Button(mGroupOptions, SWT.CHECK);
mCheckFilterInstalled.setToolTipText("Show Installed");
//Synthetic comment -- @@ -263,6 +264,7 @@
mCheckFilterInstalled.setText("Installed");

mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
mCheckFilterObsolete.setToolTipText("Also show obsolete packages");
mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -271,15 +273,23 @@
}
});
mCheckFilterObsolete.setSelection(false);
        mCheckFilterObsolete.setText("Obsolete");

        Label placeholder2 = new Label(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(placeholder2).hFill().vCenter().hGrab();

mButtonInstall = new Button(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(mButtonInstall).hFill().vCenter().hGrab();
        mButtonInstall.setToolTipText("Install all the selected packages");
mButtonInstall.setText("Install Selected...");
mButtonInstall.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -306,6 +316,7 @@
mCheckSortApi.setSelection(true);

mCheckSortSource = new Button(mGroupOptions, SWT.RADIO);
mCheckSortSource.setToolTipText("Sort by Repository");
mCheckSortSource.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -315,15 +326,25 @@
expandInitial(mCategories);
}
});
        mCheckSortSource.setText("Repository");

new Label(mGroupOptions, SWT.NONE);
        new Label(mGroupOptions, SWT.NONE);

mButtonDelete = new Button(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(mButtonDelete).hFill().vCenter().hGrab();
        mButtonDelete.setToolTipText("Delete an installed package");
mButtonDelete.setText("Delete...");
mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -994,6 +1015,32 @@
mButtonDelete.setEnabled(canDelete);
}

private void onButtonInstall() {
ArrayList<Archive> archives = new ArrayList<Archive>();








