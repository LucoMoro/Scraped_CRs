/*Use GridData/LayoutBuilder in PackagesPage.

Change-Id:I82eb07760e264884c510250245405aa2dfc1cdbe*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 8372a38..1ca4d00 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.sdkuilib.internal.repository.PackageLoader.PkgState;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -51,8 +53,6 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//Synthetic comment -- @@ -173,26 +173,23 @@
}

private void createContents(Composite parent) {
        GridLayoutBuilder.create(parent).noMargins().columns(2);

mGroupSdk = new Composite(parent, SWT.NONE);
        GridDataBuilder.create(mGroupSdk).hFill().vCenter().hGrab().hSpan(2);
        GridLayoutBuilder.create(mGroupSdk).columns(2);

Label label1 = new Label(mGroupSdk, SWT.NONE);
label1.setText("SDK Path:");

mTextSdkOsPath = new Text(mGroupSdk, SWT.NONE);
        GridDataBuilder.create(mTextSdkOsPath).hFill().vCenter().hGrab();
mTextSdkOsPath.setEnabled(false);

mGroupPackages = new Group(parent, SWT.NONE);
        GridDataBuilder.create(mGroupPackages).fill().grab().hSpan(2);
mGroupPackages.setText("Packages");
        GridLayoutBuilder.create(mGroupPackages).columns(1);

mTreeViewer = new CheckboxTreeViewer(mGroupPackages, SWT.BORDER);

//Synthetic comment -- @@ -205,7 +202,7 @@
mTree = mTreeViewer.getTree();
mTree.setLinesVisible(true);
mTree.setHeaderVisible(true);
        GridDataBuilder.create(mTree).fill().grab();

// column name icon is set in sortPackages() depending on the current filter type
// (e.g. API level or source)
//Synthetic comment -- @@ -235,11 +232,10 @@
treeColumn4.setText("Status");

mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
        GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
        GridLayoutBuilder.create(mGroupOptions).columns(6).noMargins();

        // Options line 1, 6 columns

Label label3 = new Label(mGroupOptions, SWT.NONE);
label3.setText("Show:");
//Synthetic comment -- @@ -278,10 +274,10 @@
mCheckFilterObsolete.setText("Obsolete");

Label placeholder2 = new Label(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(placeholder2).hFill().vCenter().hGrab();

mButtonInstall = new Button(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(mButtonInstall).hFill().vCenter().hGrab();
mButtonInstall.setToolTipText("Install all the selected packages");
mButtonInstall.setText("Install Selected...");
mButtonInstall.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -291,6 +287,8 @@
}
});

        // Options line 2, 6 columns

Label label2 = new Label(mGroupOptions, SWT.NONE);
label2.setText("Sort by:");

//Synthetic comment -- @@ -323,7 +321,7 @@
new Label(mGroupOptions, SWT.NONE);

mButtonDelete = new Button(mGroupOptions, SWT.NONE);
        GridDataBuilder.create(mButtonDelete).hFill().vCenter().hGrab();
mButtonDelete.setToolTipText("Delete an installed package");
mButtonDelete.setText("Delete...");
mButtonDelete.addSelectionListener(new SelectionAdapter() {







