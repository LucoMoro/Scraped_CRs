/*NPW: Allow specifying custom location for project.

Change-Id:Ide4e3ea8bf0f53cd4fc21dd23329f45c7f901618*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index ad34309..2933f8c 100644

//Synthetic comment -- @@ -801,7 +801,8 @@
IProject project,
IAndroidTarget target,
ProjectPopulator projectPopulator,
            boolean isLibrary)
throws CoreException, IOException, StreamException {
NewProjectCreator creator = new NewProjectCreator(null, null);

//Synthetic comment -- @@ -816,6 +817,14 @@
IWorkspace workspace = ResourcesPlugin.getWorkspace();
final IProjectDescription description = workspace.newProjectDescription(project.getName());

return creator.createEclipseProject(monitor, project, description, parameters,
dictionary, projectPopulator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 0025742..0cdf6f5 100644

//Synthetic comment -- @@ -30,7 +30,9 @@
import com.android.sdklib.IAndroidTarget;
import com.google.common.collect.Maps;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.fieldassist.ControlDecoration;
//Synthetic comment -- @@ -51,9 +53,12 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -86,6 +91,13 @@
private Button mChooseSdkButton;
private Button mCustomIconToggle;
private Button mLibraryToggle;
private Label mHelpIcon;
private Label mTipLabel;

//Synthetic comment -- @@ -273,6 +285,26 @@
mLibraryToggle.setSelection(mValues.isLibrary);
mLibraryToggle.addSelectionListener(this);

Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));

//Synthetic comment -- @@ -326,6 +358,17 @@
validatePage();
}

// ---- Implements ModifyListener ----

@Override
//Synthetic comment -- @@ -337,6 +380,7 @@
Object source = e.getSource();
if (source == mProjectText) {
mValues.projectName = mProjectText.getText();
mValues.projectModified = true;

try {
//Synthetic comment -- @@ -362,17 +406,37 @@
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
//Synthetic comment -- @@ -445,11 +509,39 @@
mValues.createIcon = mCustomIconToggle.getSelection();
} else if (source == mLibraryToggle) {
mValues.isLibrary = mLibraryToggle.getSelection();
}

validatePage();
}

private String getSelectedMinSdk() {
// If you're using a preview build, such as android-JellyBean, you have
// to use the codename, e.g. JellyBean, as the minimum SDK as well.
//Synthetic comment -- @@ -575,6 +667,12 @@
status = packageStatus;
}

if (status == null || status.getSeverity() != IStatus.ERROR) {
if (mValues.target == null) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -655,6 +753,52 @@
return status;
}

private void updateDecorator(ControlDecoration decorator, IStatus status, boolean hasInfo) {
if (hasInfo) {
int severity = status != null ? status.getSeverity() : IStatus.OK;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 312fbcf..f704475 100644

//Synthetic comment -- @@ -282,7 +282,7 @@

IProgressMonitor monitor = new NullProgressMonitor();
NewProjectCreator.create(monitor, newProject, mValues.target, projectPopulator,
                    mValues.isLibrary);

try {
newProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 610b1c0..2cf9948 100644

//Synthetic comment -- @@ -91,4 +91,7 @@

/** State for the template wizard, used to embed an activity template */
public NewTemplateWizardState activityValues = new NewTemplateWizardState();
}







