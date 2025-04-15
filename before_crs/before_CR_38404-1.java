/*NPW: Allow specifying custom location for project.

(cherry picked from commit c9144be8def70a05dc90bed61a63bc58321aa963)

Change-Id:I00551c9cf125fcac7bb4501ef5c3ddae74ff9207*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 83ea758..5cb011d 100644

//Synthetic comment -- @@ -805,7 +805,8 @@
IProject project,
IAndroidTarget target,
ProjectPopulator projectPopulator,
            boolean isLibrary)
throws CoreException, IOException, StreamException {
NewProjectCreator creator = new NewProjectCreator(null, null);

//Synthetic comment -- @@ -820,6 +821,15 @@
IWorkspace workspace = ResourcesPlugin.getWorkspace();
final IProjectDescription description = workspace.newProjectDescription(project.getName());

return creator.createEclipseProject(monitor, project, description, parameters,
dictionary, projectPopulator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index d4aff90..dcb897e 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.sdklib.IAndroidTarget;
import com.google.common.collect.Maps;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
//Synthetic comment -- @@ -52,7 +53,9 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.File;
//Synthetic comment -- @@ -87,6 +90,13 @@
private Button mChooseSdkButton;
private Button mCustomIconToggle;
private Button mLibraryToggle;
private Label mHelpIcon;
private Label mTipLabel;

//Synthetic comment -- @@ -268,6 +278,26 @@
mLibraryToggle.setSelection(mValues.isLibrary);
mLibraryToggle.addSelectionListener(this);

Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));

//Synthetic comment -- @@ -337,6 +367,17 @@
validatePage();
}

// ---- Implements ModifyListener ----

@Override
//Synthetic comment -- @@ -348,6 +389,7 @@
Object source = e.getSource();
if (source == mProjectText) {
mValues.projectName = mProjectText.getText();
mValues.projectModified = true;

try {
//Synthetic comment -- @@ -373,17 +415,37 @@
if (!mValues.projectModified) {
mValues.projectName = mValues.applicationName;
mProjectText.setText(mValues.applicationName);
}
updateActivityNames(mValues.applicationName);
} finally {
mIgnore = false;
}
suggestPackage(mValues.applicationName);
}

validatePage();
}

private void updateActivityNames(String name) {
try {
mIgnore = true;
//Synthetic comment -- @@ -456,11 +518,39 @@
mValues.createIcon = mCustomIconToggle.getSelection();
} else if (source == mLibraryToggle) {
mValues.isLibrary = mLibraryToggle.getSelection();
}

validatePage();
}

private String getSelectedMinSdk() {
// If you're using a preview build, such as android-JellyBean, you have
// to use the codename, e.g. JellyBean, as the minimum SDK as well.
//Synthetic comment -- @@ -580,18 +670,18 @@
status = projectStatus;
}

            IStatus locationStatus = validateLocation();
            if (locationStatus != null && (status == null
                    || locationStatus.getSeverity() > status.getSeverity())) {
                status = locationStatus;
            }

IStatus packageStatus = validatePackageName();
if (packageStatus != null && (status == null
|| packageStatus.getSeverity() > status.getSeverity())) {
status = packageStatus;
}

if (status == null || status.getSeverity() != IStatus.ERROR) {
if (mValues.target == null) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -654,21 +744,6 @@
return status;
}

    private IStatus validateLocation() {
        // Validate location
        if (mValues.projectName != null) {
            File dest = Platform.getLocation().append(mValues.projectName).toFile();
            if (dest.exists()) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format(
                                "There is already a file or directory named \"%1$s\" in the selected location.",
                        mValues.projectName));
            }
        }

        return null;
    }

private IStatus validatePackageName() {

IStatus status;
//Synthetic comment -- @@ -694,6 +769,68 @@
return status;
}

private void updateDecorator(ControlDecoration decorator, IStatus status, boolean hasInfo) {
if (hasInfo) {
int severity = status != null ? status.getSeverity() : IStatus.OK;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 890074c..8903495 100644

//Synthetic comment -- @@ -301,7 +301,7 @@
};

NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
                    mValues.isLibrary);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 6e3d113..7e8878f 100644

//Synthetic comment -- @@ -92,4 +92,7 @@

/** State for the template wizard, used to embed an activity template */
public NewTemplateWizardState activityValues = new NewTemplateWizardState();
}







