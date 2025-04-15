/*34885: ADT Import Package Naming Convention Issue

Strip out the package prefix for the first activity, and also
handle Enter keys in the location text field to refresh the
project list.

Change-Id:I14caa12f20acdcb5a5ea1c5a487c0587d04bf6fd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportPage.java
//Synthetic comment -- index 7ccab06..f503c1e 100644

//Synthetic comment -- @@ -36,8 +36,12 @@
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -59,7 +63,7 @@

/** WizardPage for importing Android projects */
class ImportPage extends WizardPage implements SelectionListener, IStructuredContentProvider,
        ICheckStateListener, ILabelProvider, IColorProvider {
private final NewProjectWizardState mValues;
private List<ImportedProject> mProjectPaths;
private final IProject[] mExistingProjects;
//Synthetic comment -- @@ -104,7 +108,8 @@

mDir = new Text(container, SWT.BORDER);
mDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mDir.addSelectionListener(this);

mBrowseButton = new Button(container, SWT.NONE);
mBrowseButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//Synthetic comment -- @@ -309,6 +314,32 @@
public void widgetDefaultSelected(SelectionEvent e) {
}

// ---- Implements IStructuredContentProvider ----

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java
//Synthetic comment -- index dc2f1b5..55e72be 100644

//Synthetic comment -- @@ -91,6 +91,10 @@
}
}
}
}
}








