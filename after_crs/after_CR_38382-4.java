/*NPW: Allow specifying custom location for project.

Change-Id:Ide4e3ea8bf0f53cd4fc21dd23329f45c7f901618*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 83ea758..5cb011d 100644

//Synthetic comment -- @@ -805,7 +805,8 @@
IProject project,
IAndroidTarget target,
ProjectPopulator projectPopulator,
            boolean isLibrary,
            String projectLocation)
throws CoreException, IOException, StreamException {
NewProjectCreator creator = new NewProjectCreator(null, null);

//Synthetic comment -- @@ -820,6 +821,15 @@
IWorkspace workspace = ResourcesPlugin.getWorkspace();
final IProjectDescription description = workspace.newProjectDescription(project.getName());

        if (projectLocation != null) {
            IPath path = new Path(projectLocation);
            IPath parent = new Path(path.toFile().getParent());
            IPath workspaceLocation = Platform.getLocation();
            if (!workspaceLocation.equals(parent)) {
                description.setLocation(path);
            }
        }

return creator.createEclipseProject(monitor, project, description, parameters,
dictionary, projectPopulator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index d4aff90..dcb897e 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.sdklib.IAndroidTarget;
import com.google.common.collect.Maps;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
//Synthetic comment -- @@ -52,7 +53,9 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
//Synthetic comment -- @@ -87,6 +90,13 @@
private Button mChooseSdkButton;
private Button mCustomIconToggle;
private Button mLibraryToggle;

    private Button mUseDefaultLocationToggle;
    private Label mLocationLabel;
    private Text mLocationText;
    private Button mChooseLocationButton;
    private static String sLastProjectLocation = System.getProperty("user.home"); //$NON-NLS-1$

private Label mHelpIcon;
private Label mTipLabel;

//Synthetic comment -- @@ -268,6 +278,26 @@
mLibraryToggle.setSelection(mValues.isLibrary);
mLibraryToggle.addSelectionListener(this);

        mUseDefaultLocationToggle = new Button(container, SWT.CHECK);
        mUseDefaultLocationToggle.setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mUseDefaultLocationToggle.setText("Create Project in Workspace");
        mUseDefaultLocationToggle.addSelectionListener(this);

        mLocationLabel = new Label(container, SWT.NONE);
        mLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        mLocationLabel.setText("Location:");

        mLocationText = new Text(container, SWT.BORDER);
        mLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mLocationText.addModifyListener(this);

        mChooseLocationButton = new Button(container, SWT.NONE);
        mChooseLocationButton.setText("Browse...");
        mChooseLocationButton.addSelectionListener(this);
        mChooseLocationButton.setEnabled(false);
        setUseCustomLocation(false);

Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));

//Synthetic comment -- @@ -337,6 +367,17 @@
validatePage();
}

    private void setUseCustomLocation(boolean en) {
        mUseDefaultLocationToggle.setSelection(!en);
        if (!en) {
            updateProjectLocation(mValues.projectName);
        }

        mLocationLabel.setEnabled(en);
        mLocationText.setEnabled(en);
        mChooseLocationButton.setEnabled(en);
    }

// ---- Implements ModifyListener ----

@Override
//Synthetic comment -- @@ -348,6 +389,7 @@
Object source = e.getSource();
if (source == mProjectText) {
mValues.projectName = mProjectText.getText();
            updateProjectLocation(mValues.projectName);
mValues.projectModified = true;

try {
//Synthetic comment -- @@ -373,17 +415,37 @@
if (!mValues.projectModified) {
mValues.projectName = mValues.applicationName;
mProjectText.setText(mValues.applicationName);
                    updateProjectLocation(mValues.applicationName);
}
updateActivityNames(mValues.applicationName);
} finally {
mIgnore = false;
}
suggestPackage(mValues.applicationName);
        } else if (source == mLocationText) {
            mValues.projectLocation = mLocationText.getText().trim();
}

validatePage();
}

    /** If the project should be created in the workspace, then update the project location
     * based on the project name. */
    private void updateProjectLocation(String projectName) {
        if (projectName == null) {
            projectName = "";
        }

        boolean useDefaultLocation = mUseDefaultLocationToggle.getSelection();

        if (useDefaultLocation) {
            IPath workspace = Platform.getLocation();
            String projectLocation = workspace.append(projectName).toOSString();
            mLocationText.setText(projectLocation);
            mValues.projectLocation = projectLocation;
        }
    }

private void updateActivityNames(String name) {
try {
mIgnore = true;
//Synthetic comment -- @@ -456,11 +518,39 @@
mValues.createIcon = mCustomIconToggle.getSelection();
} else if (source == mLibraryToggle) {
mValues.isLibrary = mLibraryToggle.getSelection();
        } else if (source == mUseDefaultLocationToggle) {
            boolean useDefault = mUseDefaultLocationToggle.getSelection();
            setUseCustomLocation(!useDefault);
        } else if (source == mChooseLocationButton) {
            String dir = promptUserForLocation(getShell());
            if (dir != null) {
                mLocationText.setText(dir);
                mValues.projectLocation = dir;
            }
}

validatePage();
}

    private String promptUserForLocation(Shell shell) {
        DirectoryDialog dd = new DirectoryDialog(getShell());
        dd.setMessage("Select folder where project should be created");

        String curLocation = mLocationText.getText().trim();
        if (!curLocation.isEmpty()) {
            dd.setFilterPath(curLocation);
        } else if (sLastProjectLocation != null) {
            dd.setFilterPath(sLastProjectLocation);
        }

        String dir = dd.open();
        if (dir != null) {
            sLastProjectLocation = dir;
        }

        return dir;
    }

private String getSelectedMinSdk() {
// If you're using a preview build, such as android-JellyBean, you have
// to use the codename, e.g. JellyBean, as the minimum SDK as well.
//Synthetic comment -- @@ -580,18 +670,18 @@
status = projectStatus;
}

IStatus packageStatus = validatePackageName();
if (packageStatus != null && (status == null
|| packageStatus.getSeverity() > status.getSeverity())) {
status = packageStatus;
}

            IStatus locationStatus = validateProjectLocation();
            if (locationStatus != null && (status == null
                    || locationStatus.getSeverity() > status.getSeverity())) {
                status = locationStatus;
            }

if (status == null || status.getSeverity() != IStatus.ERROR) {
if (mValues.target == null) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -654,21 +744,6 @@
return status;
}

private IStatus validatePackageName() {

IStatus status;
//Synthetic comment -- @@ -694,6 +769,68 @@
return status;
}

    private IStatus validateLocationInWorkspace() {
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


    private IStatus validateProjectLocation() {
        if (mUseDefaultLocationToggle.getSelection()) {
            return validateLocationInWorkspace();
        }

        String location = mLocationText.getText();
        if (location.trim().isEmpty()) {
            return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    "Provide a valid file system location where the project should be created.");
        }

        File f = new File(location);
        if (f.exists()) {
            if (!f.isDirectory()) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format("'%s' is not a valid folder.", location));
            }

            File[] children = f.listFiles();
            if (children != null && children.length > 0) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format("Folder '%s' is not empty.", location));
            }
        }

        // if the folder doesn't exist, then make sure that the parent
        // exists and is a writeable folder
        File parent = f.getParentFile();
        if (!parent.exists()) {
            return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    String.format("Folder '%s' does not exist.", parent.getName()));
        }

        if (!parent.isDirectory()) {
            return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    String.format("'%s' is not a folder.", parent.getName()));
        }

        if (!parent.canWrite()) {
            return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    String.format("'%s' is not writeable.", parent.getName()));
        }

        return null;
    }

private void updateDecorator(ControlDecoration decorator, IStatus status, boolean hasInfo) {
if (hasInfo) {
int severity = status != null ? status.getSeverity() : IStatus.OK;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 890074c..8903495 100644

//Synthetic comment -- @@ -301,7 +301,7 @@
};

NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
                    mValues.isLibrary, mValues.projectLocation);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 6e3d113..7e8878f 100644

//Synthetic comment -- @@ -92,4 +92,7 @@

/** State for the template wizard, used to embed an activity template */
public NewTemplateWizardState activityValues = new NewTemplateWizardState();

    /** Folder where the project should be created. */
    public String projectLocation;
}







