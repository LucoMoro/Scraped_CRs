/*SdkMan2: Links to select new/updates or deselect all.

Change-Id:I9c831138342574b0462e39055be86bde7114f54a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 1ca4d00..e858831 100755

//Synthetic comment -- @@ -59,6 +59,7 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -208,28 +209,28 @@
// (e.g. API level or source)
mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
mTreeColumnName = mColumnName.getColumn();
mTreeColumnName.setText("Name");
        mTreeColumnName.setWidth(340);

mColumnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn2 = mColumnApi.getColumn();
        treeColumn2.setText("API");
treeColumn2.setAlignment(SWT.CENTER);
treeColumn2.setWidth(50);

mColumnRevision = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn3 = mColumnRevision.getColumn();
treeColumn3.setText("Rev.");
treeColumn3.setToolTipText("Revision currently installed");
        treeColumn3.setAlignment(SWT.CENTER);
        treeColumn3.setWidth(50);


mColumnStatus = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn4 = mColumnStatus.getColumn();
        treeColumn4.setText("Status");
treeColumn4.setAlignment(SWT.LEAD);
treeColumn4.setWidth(190);

mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
//Synthetic comment -- @@ -241,6 +242,7 @@
label3.setText("Show:");

mCheckFilterNew = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterNew.setText("Updates/New");
mCheckFilterNew.setToolTipText("Show Updates and New");
mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -249,7 +251,6 @@
}
});
mCheckFilterNew.setSelection(true);

mCheckFilterInstalled = new Button(mGroupOptions, SWT.CHECK);
mCheckFilterInstalled.setToolTipText("Show Installed");
//Synthetic comment -- @@ -263,6 +264,7 @@
mCheckFilterInstalled.setText("Installed");

mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterObsolete.setText("Obsolete");
mCheckFilterObsolete.setToolTipText("Also show obsolete packages");
mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -271,15 +273,23 @@
}
});
mCheckFilterObsolete.setSelection(false);

        Link linkSelectNew = new Link(mGroupOptions, SWT.NONE);
        linkSelectNew.setText("<a>Select New/Updates</a>");
        linkSelectNew.setToolTipText("Selects all items that are either new or updates.");
        GridDataBuilder.create(linkSelectNew).hFill().hGrab();
        linkSelectNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                onSelectNewUpdates();
            }
        });

mButtonInstall = new Button(mGroupOptions, SWT.NONE);
mButtonInstall.setText("Install Selected...");
        mButtonInstall.setToolTipText("Install all the selected packages");
        GridDataBuilder.create(mButtonInstall).hFill().vCenter().hGrab();
mButtonInstall.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -306,6 +316,7 @@
mCheckSortApi.setSelection(true);

mCheckSortSource = new Button(mGroupOptions, SWT.RADIO);
        mCheckSortSource.setText("Repository");
mCheckSortSource.setToolTipText("Sort by Repository");
mCheckSortSource.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -315,15 +326,25 @@
expandInitial(mCategories);
}
});

new Label(mGroupOptions, SWT.NONE);

        Link linkDeselect = new Link(mGroupOptions, SWT.NONE);
        linkDeselect.setText("<a>Deselect All</a>");
        linkDeselect.setToolTipText("Deselects all the currently selected items");
        GridDataBuilder.create(linkDeselect).hFill().hGrab();
        linkDeselect.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                onDeselectAll();
            }
        });

mButtonDelete = new Button(mGroupOptions, SWT.NONE);
mButtonDelete.setText("Delete...");
        mButtonDelete.setToolTipText("Delete an installed package");
        GridDataBuilder.create(mButtonDelete).hFill().vCenter().hGrab();
mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -994,6 +1015,32 @@
mButtonDelete.setEnabled(canDelete);
}

    private void onSelectNewUpdates() {
        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();
        synchronized(mPackages) {
            for (PkgCategory cat : mCategories) {
                boolean selected = false;
                for (PkgItem item : cat.getItems()) {
                    PkgState state = item.getState();
                    if (state == PkgState.NEW || state == PkgState.HAS_UPDATE) {
                        mTreeViewer.setChecked(item, true);
                        checkExpandItem(item, provider);
                        selected = true;
                    }
                }
                if (selected) {
                    mTreeViewer.setExpandedState(cat, true);
                }
            }
        }
        updateButtonsState();
    }

    private void onDeselectAll() {
        mTreeViewer.setCheckedElements(new Object[0]);
        updateButtonsState();
    }

private void onButtonInstall() {
ArrayList<Archive> archives = new ArrayList<Archive>();








