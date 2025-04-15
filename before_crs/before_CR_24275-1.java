/*Use GridData/LayoutBuilder in PackagesPage.

Change-Id:I82eb07760e264884c510250245405aa2dfc1cdbe*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 8372a38..1ca4d00 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.sdkuilib.internal.repository.PackageLoader.PkgState;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -51,8 +53,6 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//Synthetic comment -- @@ -173,26 +173,23 @@
}

private void createContents(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

mGroupSdk = new Composite(parent, SWT.NONE);
        mGroupSdk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        mGroupSdk.setLayout(new GridLayout(2, false));

Label label1 = new Label(mGroupSdk, SWT.NONE);
label1.setText("SDK Path:");

mTextSdkOsPath = new Text(mGroupSdk, SWT.NONE);
        mTextSdkOsPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
mTextSdkOsPath.setEnabled(false);

mGroupPackages = new Group(parent, SWT.NONE);
        mGroupPackages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
mGroupPackages.setText("Packages");
        mGroupPackages.setLayout(new GridLayout(1, false));

mTreeViewer = new CheckboxTreeViewer(mGroupPackages, SWT.BORDER);

//Synthetic comment -- @@ -205,7 +202,7 @@
mTree = mTreeViewer.getTree();
mTree.setLinesVisible(true);
mTree.setHeaderVisible(true);
        mTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

// column name icon is set in sortPackages() depending on the current filter type
// (e.g. API level or source)
//Synthetic comment -- @@ -235,11 +232,10 @@
treeColumn4.setText("Status");

mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
        mGroupOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_GroupOptions = new GridLayout(6, false);
        gl_GroupOptions.marginWidth = 0;
        gl_GroupOptions.marginHeight = 0;
        mGroupOptions.setLayout(gl_GroupOptions);

Label label3 = new Label(mGroupOptions, SWT.NONE);
label3.setText("Show:");
//Synthetic comment -- @@ -278,10 +274,10 @@
mCheckFilterObsolete.setText("Obsolete");

Label placeholder2 = new Label(mGroupOptions, SWT.NONE);
        placeholder2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

mButtonInstall = new Button(mGroupOptions, SWT.NONE);
        mButtonInstall.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
mButtonInstall.setToolTipText("Install all the selected packages");
mButtonInstall.setText("Install Selected...");
mButtonInstall.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -291,6 +287,8 @@
}
});

Label label2 = new Label(mGroupOptions, SWT.NONE);
label2.setText("Sort by:");

//Synthetic comment -- @@ -323,7 +321,7 @@
new Label(mGroupOptions, SWT.NONE);

mButtonDelete = new Button(mGroupOptions, SWT.NONE);
        mButtonDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
mButtonDelete.setToolTipText("Delete an installed package");
mButtonDelete.setText("Delete...");
mButtonDelete.addSelectionListener(new SelectionAdapter() {







