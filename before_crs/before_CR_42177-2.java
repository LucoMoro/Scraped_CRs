/*Add target SDK setting to the New Project wizard

Since the first page is getting too large, rather than add
one more, split some of the contents into a second page.
In addition, the new project dialog was missing a workset
chooser like the old wizard had, so add one in on the new
page now that we have plenty room for it.  Some other tweaks
as well based on input from Xav.

Change-Id:I4dd2334b2f5af623cba4bbb3a6d2d0b053d1763f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index d690473..1e55274 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
//Synthetic comment -- @@ -82,6 +83,7 @@


/** Utility methods for ADT */
public class AdtUtils {
/**
* Returns true if the given string ends with the given suffix, using a
//Synthetic comment -- @@ -368,6 +370,24 @@
* @return the current editor, or null
*/
public static IEditorPart getActiveEditor() {
IWorkbench workbench = PlatformUI.getWorkbench();
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
if (window == null) {
//Synthetic comment -- @@ -376,13 +396,24 @@
window = windows[0];
}
}
if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                return page.getActiveEditor();
}
}

return null;
}

//Synthetic comment -- @@ -725,7 +756,6 @@
* @param appendValue if true, add this value as a comma separated value to
*            the existing attribute value, if any
*/
    @SuppressWarnings("restriction") // DOM model
public static void setToolsAttribute(
@NonNull final AndroidXmlEditor editor,
@NonNull final Element element,
//Synthetic comment -- @@ -834,6 +864,33 @@
}

/**
* Returns the Android version and code name of the given API level
*
* @param api the api level
//Synthetic comment -- @@ -870,21 +927,13 @@
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
                                String codename = target.getProperty(PkgProps.PLATFORM_CODENAME);
                                if (codename != null) {
                                    return String.format("API %1$d: Android %2$s (%3$s)", api,
                                            target.getProperty("ro.build.version.release"), //$NON-NLS-1$
                                            codename);
                                }
                                return String.format("API %1$d: Android %2$s", api,
                                        target.getProperty("ro.build.version.release")); //$NON-NLS-1$
}
}
}
}

return "API " + api;

}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportProjectWizard.java
//Synthetic comment -- index 1181951..1004fd6 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;

import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
//Synthetic comment -- @@ -39,6 +40,7 @@

private NewProjectWizardState mValues;
private ImportPage mImportPage;

/** Constructs a new wizard default project wizard */
public ImportProjectWizard() {
//Synthetic comment -- @@ -48,11 +50,16 @@
public void addPages() {
mValues = new NewProjectWizardState(Mode.ANY);
mImportPage = new ImportPage(mValues);
addPage(mImportPage);
}

@Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
setHelpAvailable(false); // TODO have help
ImageDescriptor desc = AdtPlugin.getImageDescriptor(PROJECT_LOGO_LARGE);
setDefaultPageImageDescriptor(desc);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 6bbf566..b8f7c50 100644

//Synthetic comment -- @@ -18,11 +18,12 @@

import static com.android.SdkConstants.FN_PROJECT_PROPERTIES;
import static com.android.sdklib.internal.project.ProjectProperties.PROPERTY_LIBRARY;

import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -33,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
//Synthetic comment -- @@ -213,6 +215,11 @@
private final NewProjectWizardState mValues;
private final IRunnableContext mRunnableContext;

public NewProjectCreator(NewProjectWizardState values, IRunnableContext runnableContext) {
mValues = values;
mRunnableContext = runnableContext;
//Synthetic comment -- @@ -644,7 +651,14 @@
}
}

public interface ProjectPopulator {
public void populate(IProject project) throws InvocationTargetException;
}

//Synthetic comment -- @@ -660,12 +674,13 @@
* @return The project newly created
* @throws StreamException
*/
    private IProject createEclipseProject(IProgressMonitor monitor,
            IProject project,
            IProjectDescription description,
            Map<String, Object> parameters,
            Map<String, String> dictionary,
            ProjectPopulator projectPopulator)
throws CoreException, IOException, StreamException {

// get the project target
//Synthetic comment -- @@ -719,7 +734,7 @@
addSampleCode(project, sourceFolders[0], parameters, dictionary, monitor);

// add the string definition file if needed
            if (dictionary.size() > 0) {
addStringDictionaryFile(project, dictionary, monitor);
}

//Synthetic comment -- @@ -804,13 +819,26 @@
return project;
}

public static void create(
            IProgressMonitor monitor,
            final IProject project,
            IAndroidTarget target,
            final ProjectPopulator projectPopulator,
boolean isLibrary,
            String projectLocation)
throws CoreException {
final NewProjectCreator creator = new NewProjectCreator(null, null);

//Synthetic comment -- @@ -847,6 +875,13 @@
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Unexpected error while creating project", e));
}
}
};

//Synthetic comment -- @@ -1276,8 +1311,9 @@
* @param project the project to add the file to.
* @param destName the name to write the file as
* @param source the file to add. It'll keep the same filename once copied into the project.
     * @throws FileNotFoundException
     * @throws CoreException
*/
public static void addLocalFile(IProject project, File source, String destName,
IProgressMonitor monitor) throws FileNotFoundException, CoreException {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 17ea1bf..ff03b33 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;

import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
//Synthetic comment -- @@ -50,6 +51,7 @@
private SampleSelectionPage mSamplePage;
private ApplicationInfoPage mPropertiesPage;
private final Mode mMode;

/** Constructs a new wizard default project wizard */
public NewProjectWizard() {
//Synthetic comment -- @@ -77,6 +79,11 @@

if (mMode != Mode.SAMPLE) {
mNamePage = new ProjectNamePage(mValues);
addPage(mNamePage);
}

//Synthetic comment -- @@ -103,6 +110,8 @@

@Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
setHelpAvailable(false); // TODO have help
ImageDescriptor desc = AdtPlugin.getImageDescriptor(PROJECT_LOGO_LARGE);
setDefaultPageImageDescriptor(desc);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 31c73a2..31c7225 100644

//Synthetic comment -- @@ -105,7 +105,7 @@
setWorkingSets(new IWorkingSet[0]);
}

    public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
setWorkingSets(WorkingSetHelper.getSelectedWorkingSet(selection, activePart));
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java
//Synthetic comment -- index f759887..9d2dd9a 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
public boolean isPageComplete() {
// Ensure that the Finish button isn't enabled until
// the user has reached and completed this page
        if (!mShown) {
return false;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 54e8780..1c07518 100644

//Synthetic comment -- @@ -16,9 +16,9 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;


import static com.android.ide.eclipse.adt.AdtUtils.extractClassName;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewTemplatePage.WIZARD_PAGE_WIDTH;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_ID;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -53,16 +53,12 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -74,6 +70,7 @@
*/
public class NewProjectPage extends WizardPage
implements ModifyListener, SelectionListener, FocusListener {
private static final String SAMPLE_PACKAGE_PREFIX = "com.example."; //$NON-NLS-1$
/** Suffix added by default to activity names */
static final String ACTIVITY_NAME_SUFFIX = "Activity";              //$NON-NLS-1$
//Synthetic comment -- @@ -84,33 +81,25 @@
private final NewProjectWizardState mValues;
private Map<String, Integer> mMinNameToApi;
private Parameter mThemeParameter;

private Text mProjectText;
private Text mPackageText;
private Text mApplicationText;
private Combo mMinSdkCombo;

    private boolean mIgnore;
private Combo mBuildSdkCombo;
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

private ControlDecoration mApplicationDec;
private ControlDecoration mProjectDec;
private ControlDecoration mPackageDec;
private ControlDecoration mBuildTargetDec;
private ControlDecoration mMinSdkDec;
    private Combo mThemeCombo;

NewProjectPage(NewProjectWizardState values) {
super("newAndroidApp"); //$NON-NLS-1$
//Synthetic comment -- @@ -133,7 +122,9 @@
applicationLabel.setText("Application Name:");

mApplicationText = new Text(container, SWT.BORDER);
        mApplicationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
mApplicationText.addModifyListener(this);
mApplicationText.addFocusListener(this);
mApplicationDec = createFieldDecoration(mApplicationText,
//Synthetic comment -- @@ -144,7 +135,9 @@
projectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
projectLabel.setText("Project Name:");
mProjectText = new Text(container, SWT.BORDER);
        mProjectText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
mProjectText.addModifyListener(this);
mProjectText.addFocusListener(this);
mProjectDec = createFieldDecoration(mProjectText,
//Synthetic comment -- @@ -156,7 +149,9 @@
packageLabel.setText("Package Name:");

mPackageText = new Text(container, SWT.BORDER);
        mPackageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
mPackageText.addModifyListener(this);
mPackageText.addFocusListener(this);
mPackageDec = createFieldDecoration(mPackageText,
//Synthetic comment -- @@ -173,28 +168,35 @@
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);

        Label buildSdkLabel = new Label(container, SWT.NONE);
        buildSdkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        buildSdkLabel.setText("Build SDK:");

        mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
        mBuildSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        IAndroidTarget[] targets = getCompilationTargets();
        mMinNameToApi = Maps.newHashMap();
        List<String> labels = new ArrayList<String>(targets.length);
        for (IAndroidTarget target : targets) {
            String targetLabel = String.format("%1$s (API %2$s)", target.getFullName(),
                    target.getVersion().getApiString());
            labels.add(targetLabel);
            mMinNameToApi.put(targetLabel, target.getVersion().getApiLevel());

        }
        mBuildSdkCombo.setData(targets);
        mBuildSdkCombo.setItems(labels.toArray(new String[labels.size()]));

// Pick most recent platform
List<String> codeNames = Lists.newArrayList();
        int selectIndex = -1;
for (int i = 0, n = targets.length; i < n; i++) {
IAndroidTarget target = targets[i];
AndroidVersion version = target.getVersion();
//Synthetic comment -- @@ -208,34 +210,10 @@
&& (mValues.target == null ||
apiLevel > mValues.target.getVersion().getApiLevel())) {
mValues.target = target;
                selectIndex = i;
}
}
        if (selectIndex != -1) {
            mBuildSdkCombo.select(selectIndex);
        }

        mBuildSdkCombo.addSelectionListener(this);
        mBuildSdkCombo.addFocusListener(this);
        mBuildTargetDec = createFieldDecoration(mBuildSdkCombo,
                "Choose a target API to compile your code against. This is typically the most " +
                "recent version, or the first version that supports all the APIs you want to " +
                "directly access");


        mChooseSdkButton = new Button(container, SWT.NONE);
        mChooseSdkButton.setText("Choose...");
        mChooseSdkButton.addSelectionListener(this);
        mChooseSdkButton.setEnabled(false);

        Label minSdkLabel = new Label(container, SWT.NONE);
        minSdkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        minSdkLabel.setText("Minimum Required SDK:");

        mMinSdkCombo = new Combo(container, SWT.READ_ONLY);
        mMinSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        labels = new ArrayList<String>(24);
for (String label : AdtUtils.getKnownVersions()) {
labels.add(label);
}
//Synthetic comment -- @@ -261,9 +239,56 @@
mMinSdkDec = createFieldDecoration(mMinSdkCombo,
"Choose the lowest version of Android that your application will support. Lower " +
"API levels target more devices, but means fewer features are available. By " +
                "targeting API 8 and later, you reach approximately 93% of the market.");

new Label(container, SWT.NONE);
new Label(container, SWT.NONE);

TemplateMetadata metadata = mValues.template.getTemplate();
//Synthetic comment -- @@ -271,54 +296,25 @@
mThemeParameter = metadata.getParameter("baseTheme"); //$NON-NLS-1$
if (mThemeParameter != null && mThemeParameter.element != null) {
Label themeLabel = new Label(container, SWT.NONE);
                themeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
themeLabel.setText("Theme:");

mThemeCombo = NewTemplatePage.createOptionCombo(mThemeParameter, container,
mValues.parameters, this, this);
                mThemeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
new Label(container, SWT.NONE);
                new Label(container, SWT.NONE);
}
}

new Label(container, SWT.NONE);
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);

        mCustomIconToggle = new Button(container, SWT.CHECK);
        mCustomIconToggle.setSelection(true);
        mCustomIconToggle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mCustomIconToggle.setText("Create custom launcher icon");
        mCustomIconToggle.setSelection(mValues.createIcon);
        mCustomIconToggle.addSelectionListener(this);

        mLibraryToggle = new Button(container, SWT.CHECK);
        mLibraryToggle.setSelection(true);
        mLibraryToggle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mLibraryToggle.setText("Mark this project as a library");
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
//Synthetic comment -- @@ -344,7 +340,41 @@
data.horizontalSpan = 4;
data.widthHint = WIZARD_PAGE_WIDTH;
dummy.setLayoutData(data);

}

private IAndroidTarget[] getCompilationTargets() {
//Synthetic comment -- @@ -393,17 +423,6 @@
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
//Synthetic comment -- @@ -448,8 +467,6 @@
mIgnore = false;
}
suggestPackage(mValues.applicationName);
        } else if (source == mLocationText) {
            mValues.projectLocation = mLocationText.getText().trim();
}

validatePage();
//Synthetic comment -- @@ -497,12 +514,9 @@
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
//Synthetic comment -- @@ -530,100 +544,10 @@
}

Object source = e.getSource();
        if (source == mChooseSdkButton) {
            // TODO: Open SDK chooser
            assert false;
        } else if (source == mMinSdkCombo) {
            mValues.minSdk = getSelectedMinSdk();
            // If higher than build target, adjust build target
            // TODO: implement

            Integer minSdk = mMinNameToApi.get(mValues.minSdk);
            if (minSdk == null) {
                try {
                    minSdk = Integer.parseInt(mValues.minSdk);
                } catch (NumberFormatException nufe) {
                    minSdk = 1;
                }
            }
            mValues.iconState.minSdk = minSdk.intValue();
            mValues.minSdkLevel = minSdk.intValue();
        } else if (source == mBuildSdkCombo) {
            mValues.target = getSelectedBuildTarget();

            // If lower than min sdk target, adjust min sdk target
            if (mValues.target.getVersion().isPreview()) {
                mValues.minSdk = mValues.target.getVersion().getCodename();
                try {
                    mIgnore = true;
                    mMinSdkCombo.setText(mValues.minSdk);
                } finally {
                    mIgnore = false;
                }
            } else {
                String minSdk = mValues.minSdk;
                int buildApiLevel = mValues.target.getVersion().getApiLevel();
                if (minSdk != null && !minSdk.isEmpty()
                        && Character.isDigit(minSdk.charAt(0))
                        && buildApiLevel < Integer.parseInt(minSdk)) {
                    mValues.minSdk = Integer.toString(buildApiLevel);
                    try {
                        mIgnore = true;
                        setSelectedMinSdk(buildApiLevel);
                    } finally {
                        mIgnore = false;
                    }
                }
            }
        } else if (source == mCustomIconToggle) {
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
        } else if (source == mThemeCombo) {
            String[] optionIds = (String[]) mThemeCombo.getData(ATTR_ID);
            int index = mThemeCombo.getSelectionIndex();
            if (index != -1 && index < optionIds.length) {
                String optionId = optionIds[index];
                Parameter parameter = NewTemplatePage.getParameter(mThemeCombo);
                if (parameter != null) {
                    parameter.value = optionId;
                    parameter.edited = optionId != null && !optionId.toString().isEmpty();
                    mValues.parameters.put(parameter.id, optionId);
                }
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
//Synthetic comment -- @@ -715,6 +639,10 @@
}
mPackageText.setSelection(0, length);
}
}
mTipLabel.setText(tip);
mHelpIcon.setVisible(tip.length() > 0);
//Synthetic comment -- @@ -735,6 +663,12 @@
updateDecorator(mApplicationDec, null, true);
updateDecorator(mPackageDec, null, true);
updateDecorator(mProjectDec, null, true);
} else {
IStatus appStatus = validateAppName();
if (appStatus != null && (status == null
//Synthetic comment -- @@ -754,7 +688,7 @@
status = packageStatus;
}

            IStatus locationStatus = validateProjectLocation();
if (locationStatus != null && (status == null
|| locationStatus.getSeverity() > status.getSeverity())) {
status = locationStatus;
//Synthetic comment -- @@ -784,13 +718,19 @@
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"The minimum SDK version is higher than the build target version");
}
}
}

            if (mThemeParameter != null
                    && (status == null || status.getSeverity() != IStatus.ERROR)) {
                status = NewTemplatePage.validateCombo(status, mThemeParameter,
                        mValues.minSdkLevel, mValues.getBuildApi());
}
}

//Synthetic comment -- @@ -829,7 +769,6 @@
}

private IStatus validatePackageName() {

IStatus status;
if (mValues.packageName == null || mValues.packageName.startsWith(SAMPLE_PACKAGE_PREFIX)) {
if (mValues.packageName != null
//Synthetic comment -- @@ -853,66 +792,18 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index d060aff..72e9985 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.annotations.NonNull;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
//Synthetic comment -- @@ -79,6 +80,7 @@
public static final String DEFAULT_LAUNCHER_ICON = "launcher_icon";   //$NON-NLS-1$

private NewProjectPage mMainPage;
private ActivityPage mActivityPage;
private NewTemplatePage mTemplatePage;
private NewProjectWizardState mValues;
//Synthetic comment -- @@ -93,6 +95,8 @@

mValues = new NewProjectWizardState();
mMainPage = new NewProjectPage(mValues);
mActivityPage = new ActivityPage(mValues, true, true);
}

//Synthetic comment -- @@ -100,12 +104,17 @@
public void addPages() {
super.addPages();
addPage(mMainPage);
addPage(mActivityPage);
}

@Override
public IWizardPage getNextPage(IWizardPage page) {
if (page == mMainPage) {
if (mValues.createIcon) {
// Bundle asset studio wizard to create the launcher icon
CreateAssetSetWizardState iconState = mValues.iconState;
//Synthetic comment -- @@ -131,7 +140,11 @@
p.setTitle("Configure Launcher Icon");
return p;
} else {
                return mActivityPage;
}
}

//Synthetic comment -- @@ -316,7 +329,7 @@
};

NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
                    mValues.isLibrary, mValues.projectLocation);

// For new projects, ensure that we're actually using the preferred compliance,
// not just the default one
//Synthetic comment -- @@ -391,10 +404,8 @@
parameters.put(ATTR_APP_TITLE, mValues.applicationName);
parameters.put(ATTR_MIN_API, mValues.minSdk);
parameters.put(ATTR_MIN_API_LEVEL, mValues.minSdkLevel);
        int buildApiLevel = mValues.target.getVersion().getApiLevel();
        parameters.put(ATTR_BUILD_API, buildApiLevel);
        parameters.put(ATTR_TARGET_API, buildApiLevel);

parameters.put(ATTR_COPY_ICONS, !mValues.createIcon);
parameters.putAll(mValues.parameters);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 3af40a8..9cd3a6d 100644

//Synthetic comment -- @@ -18,9 +18,12 @@

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.CATEGORY_PROJECTS;

import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.sdklib.IAndroidTarget;

import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -71,12 +74,15 @@
/** The compilation target to use for this project */
public IAndroidTarget target;

    /** The minimum SDK API level to use */
public String minSdk;

    /** The minimum API level, as a string (if the API is a preview release with a codename) */
public int minSdkLevel;

/** Whether this project should be marked as a library project */
public boolean isLibrary;

//Synthetic comment -- @@ -96,12 +102,18 @@
/** State for the template wizard, used to embed an activity template */
public NewTemplateWizardState activityValues = new NewTemplateWizardState();

/** Folder where the project should be created. */
public String projectLocation;

/** Configured parameters, by id */
public final Map<String, Object> parameters = new HashMap<String, Object>();

/**
* Returns the build target API level
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 660b2bb..1cf5daf 100644

//Synthetic comment -- @@ -651,9 +651,15 @@
/** Validates the given combo */
static IStatus validateCombo(IStatus status, Parameter parameter, int minSdk, int buildApi) {
Combo combo = (Combo) parameter.control;
        Integer[] optionIds = (Integer[]) combo.getData(ATTR_MIN_API);
int index = combo.getSelectionIndex();

// Check minSdk
if (index != -1 && index < optionIds.length) {
Integer requiredMinSdk = optionIds[index];
//Synthetic comment -- @@ -674,7 +680,7 @@
status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
String.format(
"%1$s \"%2$s\"  requires a build target API version of at " +
                        "least %3$d, and the current min version is %4$d",
parameter.name, combo.getItems()[index], requiredBuildApi, buildApi));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ProjectContentsPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ProjectContentsPage.java
new file mode 100644
//Synthetic comment -- index 0000000..7d7881f

//Synthetic comment -- @@ -0,0 +1,380 @@







