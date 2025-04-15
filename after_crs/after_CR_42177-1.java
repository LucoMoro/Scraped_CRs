/*Add target SDK setting to the New Project wizard

Since the first page is getting too large, rather than add
one more, split some of the contents into a second page.
In addition, the new project dialog was missing a workset
chooser like the old wizard had, so add one in on the new
page now that we have plenty room for it.  Some other tweaks
as well based on input from Xav.

Change-Id:I4dd2334b2f5af623cba4bbb3a6d2d0b053d1763f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index c47cd2d..4a8ffd4 100644

//Synthetic comment -- @@ -62,6 +62,7 @@
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
//Synthetic comment -- @@ -81,6 +82,7 @@


/** Utility methods for ADT */
@SuppressWarnings("restriction") // WST API
public class AdtUtils {
/**
* Returns true if the given string ends with the given suffix, using a
//Synthetic comment -- @@ -367,6 +369,24 @@
* @return the current editor, or null
*/
public static IEditorPart getActiveEditor() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                return page.getActiveEditor();
            }
        }

        return null;
    }

    /**
     * Returns the current active workbench, or null if not found
     *
     * @return the current window, or null
     */
    @Nullable
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
IWorkbench workbench = PlatformUI.getWorkbench();
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
if (window == null) {
//Synthetic comment -- @@ -375,13 +395,24 @@
window = windows[0];
}
}

        return window;
    }

    /**
     * Returns the current active workbench part, or null if not found
     *
     * @return the current active workbench part, or null
     */
    @Nullable
    public static IWorkbenchPart getActivePart() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
if (window != null) {
            IWorkbenchPage activePage = window.getActivePage();
            if (activePage != null) {
                return activePage.getActivePart();
}
}
return null;
}

//Synthetic comment -- @@ -724,7 +755,6 @@
* @param appendValue if true, add this value as a comma separated value to
*            the existing attribute value, if any
*/
public static void setToolsAttribute(
@NonNull final AndroidXmlEditor editor,
@NonNull final Element element,
//Synthetic comment -- @@ -831,6 +861,33 @@
}

/**
     * Returns a string label for the given target, of the form
     * "API 16: Android 4.1 (Jelly Bean)".
     *
     * @param target the target to generate a string from
     * @return a suitable display string
     */
    @NonNull
    public static String getTargetLabel(@NonNull IAndroidTarget target) {
        if (target.isPlatform()) {
            AndroidVersion version = target.getVersion();
            String codename = target.getProperty(PkgProps.PLATFORM_CODENAME);
            String release = target.getProperty("ro.build.version.release"); //$NON-NLS-1$
            if (codename != null) {
                return String.format("API %1$d: Android %2$s (%3$s)",
                        version.getApiLevel(),
                        release,
                        codename);
            }
            return String.format("API %1$d: Android %2$s", version.getApiLevel(),
                    release);
        }

        return String.format("%1$s (API %2$s)", target.getFullName(),
                target.getVersion().getApiString());
    }

    /**
* Returns the Android version and code name of the given API level
*
* @param api the api level
//Synthetic comment -- @@ -864,21 +921,13 @@
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
                                return getTargetLabel(target);
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
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;

import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
//Synthetic comment -- @@ -39,6 +40,7 @@

private NewProjectWizardState mValues;
private ImportPage mImportPage;
    private IStructuredSelection mSelection;

/** Constructs a new wizard default project wizard */
public ImportProjectWizard() {
//Synthetic comment -- @@ -48,11 +50,16 @@
public void addPages() {
mValues = new NewProjectWizardState(Mode.ANY);
mImportPage = new ImportPage(mValues);
        if (mSelection != null) {
            mImportPage.init(mSelection, AdtUtils.getActivePart());
        }
addPage(mImportPage);
}

@Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
        mSelection = selection;

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
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -33,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
//Synthetic comment -- @@ -213,6 +215,11 @@
private final NewProjectWizardState mValues;
private final IRunnableContext mRunnableContext;

    /**
     * Creates a new {@linkplain NewProjectCreator}
     * @param values the wizard state with initial project parameters
     * @param runnableContext the context to run project creation in
     */
public NewProjectCreator(NewProjectWizardState values, IRunnableContext runnableContext) {
mValues = values;
mRunnableContext = runnableContext;
//Synthetic comment -- @@ -644,7 +651,14 @@
}
}

    /** Handler which can write contents into a project */
public interface ProjectPopulator {
        /**
         * Add contents into the given project
         *
         * @param project the project to write into
         * @throws InvocationTargetException if anything goes wrong
         */
public void populate(IProject project) throws InvocationTargetException;
}

//Synthetic comment -- @@ -660,12 +674,13 @@
* @return The project newly created
* @throws StreamException
*/
    private IProject createEclipseProject(
            @NonNull IProgressMonitor monitor,
            @NonNull IProject project,
            @NonNull IProjectDescription description,
            @NonNull Map<String, Object> parameters,
            @Nullable Map<String, String> dictionary,
            @Nullable ProjectPopulator projectPopulator)
throws CoreException, IOException, StreamException {

// get the project target
//Synthetic comment -- @@ -719,7 +734,7 @@
addSampleCode(project, sourceFolders[0], parameters, dictionary, monitor);

// add the string definition file if needed
            if (dictionary != null && dictionary.size() > 0) {
addStringDictionaryFile(project, dictionary, monitor);
}

//Synthetic comment -- @@ -804,13 +819,26 @@
return project;
}

    /**
     * Creates a new project
     *
     * @param monitor An existing monitor.
     * @param project The project to create.
     * @param target the build target to associate with the project
     * @param projectPopulator a handler for writing the template contents
     * @param isLibrary whether this project should be marked as a library project
     * @param projectLocation the location to write the project into
     * @param workingSets Eclipse working sets, if any, to add the project to
     * @throws CoreException if anything goes wrong
     */
public static void create(
            @NonNull IProgressMonitor monitor,
            @NonNull final IProject project,
            @NonNull IAndroidTarget target,
            @Nullable final ProjectPopulator projectPopulator,
boolean isLibrary,
            @NonNull String projectLocation,
            @NonNull final IWorkingSet[] workingSets)
throws CoreException {
final NewProjectCreator creator = new NewProjectCreator(null, null);

//Synthetic comment -- @@ -847,6 +875,13 @@
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Unexpected error while creating project", e));
}
                if (workingSets != null && workingSets.length > 0) {
                    IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
                    if (javaProject != null) {
                        Display.getDefault().syncExec(new WorksetAdder(javaProject,
                                workingSets));
                    }
                }
}
};

//Synthetic comment -- @@ -1276,8 +1311,9 @@
* @param project the project to add the file to.
* @param destName the name to write the file as
* @param source the file to add. It'll keep the same filename once copied into the project.
     * @param monitor the monitor to report progress to
     * @throws FileNotFoundException if the file to be added does not exist
     * @throws CoreException if writing the file does not work
*/
public static void addLocalFile(IProject project, File source, String destName,
IProgressMonitor monitor) throws FileNotFoundException, CoreException {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 17ea1bf..ff03b33 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;

import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
//Synthetic comment -- @@ -50,6 +51,7 @@
private SampleSelectionPage mSamplePage;
private ApplicationInfoPage mPropertiesPage;
private final Mode mMode;
    private IStructuredSelection mSelection;

/** Constructs a new wizard default project wizard */
public NewProjectWizard() {
//Synthetic comment -- @@ -77,6 +79,11 @@

if (mMode != Mode.SAMPLE) {
mNamePage = new ProjectNamePage(mValues);

            if (mSelection != null) {
                mNamePage.init(mSelection, AdtUtils.getActivePart());
            }

addPage(mNamePage);
}

//Synthetic comment -- @@ -103,6 +110,8 @@

@Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
        mSelection = selection;

setHelpAvailable(false); // TODO have help
ImageDescriptor desc = AdtPlugin.getImageDescriptor(PROJECT_LOGO_LARGE);
setDefaultPageImageDescriptor(desc);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 31c73a2..31c7225 100644

//Synthetic comment -- @@ -105,7 +105,7 @@
setWorkingSets(new IWorkingSet[0]);
}

    void init(IStructuredSelection selection, IWorkbenchPart activePart) {
setWorkingSets(WorkingSetHelper.getSelectedWorkingSet(selection, activePart));
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java
//Synthetic comment -- index f759887..9d2dd9a 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
public boolean isPageComplete() {
// Ensure that the Finish button isn't enabled until
// the user has reached and completed this page
        if (!mShown && mValues.createActivity) {
return false;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 54e8780..1c07518 100644

//Synthetic comment -- @@ -16,9 +16,9 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;


import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.eclipse.adt.AdtUtils.extractClassName;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewTemplatePage.WIZARD_PAGE_WIDTH;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -53,16 +53,12 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -74,6 +70,7 @@
*/
public class NewProjectPage extends WizardPage
implements ModifyListener, SelectionListener, FocusListener {
    private static final int FIELD_WIDTH = 300;
private static final String SAMPLE_PACKAGE_PREFIX = "com.example."; //$NON-NLS-1$
/** Suffix added by default to activity names */
static final String ACTIVITY_NAME_SUFFIX = "Activity";              //$NON-NLS-1$
//Synthetic comment -- @@ -84,33 +81,25 @@
private final NewProjectWizardState mValues;
private Map<String, Integer> mMinNameToApi;
private Parameter mThemeParameter;
    private Combo mThemeCombo;

private Text mProjectText;
private Text mPackageText;
private Text mApplicationText;
private Combo mMinSdkCombo;
    private Combo mTargetSdkCombo;
private Combo mBuildSdkCombo;
private Label mHelpIcon;
private Label mTipLabel;

    private boolean mIgnore;
private ControlDecoration mApplicationDec;
private ControlDecoration mProjectDec;
private ControlDecoration mPackageDec;
private ControlDecoration mBuildTargetDec;
private ControlDecoration mMinSdkDec;
    private ControlDecoration mTargetSdkDec;
    private ControlDecoration mThemeDec;

NewProjectPage(NewProjectWizardState values) {
super("newAndroidApp"); //$NON-NLS-1$
//Synthetic comment -- @@ -133,7 +122,9 @@
applicationLabel.setText("Application Name:");

mApplicationText = new Text(container, SWT.BORDER);
        GridData gdApplicationText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
        gdApplicationText.widthHint = FIELD_WIDTH;
        mApplicationText.setLayoutData(gdApplicationText);
mApplicationText.addModifyListener(this);
mApplicationText.addFocusListener(this);
mApplicationDec = createFieldDecoration(mApplicationText,
//Synthetic comment -- @@ -144,7 +135,9 @@
projectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
projectLabel.setText("Project Name:");
mProjectText = new Text(container, SWT.BORDER);
        GridData gdProjectText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
        gdProjectText.widthHint = FIELD_WIDTH;
        mProjectText.setLayoutData(gdProjectText);
mProjectText.addModifyListener(this);
mProjectText.addFocusListener(this);
mProjectDec = createFieldDecoration(mProjectText,
//Synthetic comment -- @@ -156,7 +149,9 @@
packageLabel.setText("Package Name:");

mPackageText = new Text(container, SWT.BORDER);
        GridData gdPackageText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
        gdPackageText.widthHint = FIELD_WIDTH;
        mPackageText.setLayoutData(gdPackageText);
mPackageText.addModifyListener(this);
mPackageText.addFocusListener(this);
mPackageDec = createFieldDecoration(mPackageText,
//Synthetic comment -- @@ -173,28 +168,35 @@
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);

        // Min SDK

        Label minSdkLabel = new Label(container, SWT.NONE);
        minSdkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        minSdkLabel.setText("Minimum Required SDK:");

        mMinSdkCombo = new Combo(container, SWT.READ_ONLY);
        GridData gdMinSdkCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gdMinSdkCombo.widthHint = FIELD_WIDTH;
        mMinSdkCombo.setLayoutData(gdMinSdkCombo);

// Pick most recent platform
        IAndroidTarget[] targets = getCompilationTargets();
        mMinNameToApi = Maps.newHashMap();
        List<String> targetLabels = new ArrayList<String>(targets.length);
        for (IAndroidTarget target : targets) {
            String targetLabel;
            if (target.isPlatform()
                    && target.getVersion().getApiLevel() <= AdtUtils.getHighestKnownApiLevel()) {
                targetLabel = AdtUtils.getAndroidName(target.getVersion().getApiLevel());
            } else {
                targetLabel = AdtUtils.getTargetLabel(target);
            }
            targetLabels.add(targetLabel);
            mMinNameToApi.put(targetLabel, target.getVersion().getApiLevel());
        }

List<String> codeNames = Lists.newArrayList();
        int buildTargetIndex = -1;
for (int i = 0, n = targets.length; i < n; i++) {
IAndroidTarget target = targets[i];
AndroidVersion version = target.getVersion();
//Synthetic comment -- @@ -208,34 +210,10 @@
&& (mValues.target == null ||
apiLevel > mValues.target.getVersion().getApiLevel())) {
mValues.target = target;
                buildTargetIndex = i;
}
}
        List<String> labels = new ArrayList<String>(24);
for (String label : AdtUtils.getKnownVersions()) {
labels.add(label);
}
//Synthetic comment -- @@ -261,9 +239,56 @@
mMinSdkDec = createFieldDecoration(mMinSdkCombo,
"Choose the lowest version of Android that your application will support. Lower " +
"API levels target more devices, but means fewer features are available. By " +
                "targeting API 8 and later, you reach approximately 95% of the market.");
new Label(container, SWT.NONE);

        // Target SDK
        Label targetSdkLabel = new Label(container, SWT.NONE);
        targetSdkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        targetSdkLabel.setText("Target SDK:");

        mTargetSdkCombo = new Combo(container, SWT.READ_ONLY);
        GridData gdTargetSdkCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gdTargetSdkCombo.widthHint = FIELD_WIDTH;
        mTargetSdkCombo.setLayoutData(gdTargetSdkCombo);

        mTargetSdkCombo.setItems(versions);
        mTargetSdkCombo.select(mValues.targetSdkLevel - 1);

        mTargetSdkCombo.addSelectionListener(this);
        mTargetSdkCombo.addFocusListener(this);
        mTargetSdkDec = createFieldDecoration(mTargetSdkCombo,
                "Choose the highest API level that the application is known to work with. " +
                "This attribute informs the system that you have tested against the target " +
                "version and the system should not enable any compatibility behaviors to " +
                "maintain your app's forward-compatibility with the target version. " +
                "The application is still able to run on older versions " +
                "(down to minSdkVersion). Your application may look dated if you are not " +
                "targeting the current version.");
        new Label(container, SWT.NONE);

        // Build Version

        Label buildSdkLabel = new Label(container, SWT.NONE);
        buildSdkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        buildSdkLabel.setText("Compile With:");

        mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
        GridData gdBuildSdkCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gdBuildSdkCombo.widthHint = FIELD_WIDTH;
        mBuildSdkCombo.setLayoutData(gdBuildSdkCombo);
        mBuildSdkCombo.setData(targets);
        mBuildSdkCombo.setItems(targetLabels.toArray(new String[targetLabels.size()]));
        if (buildTargetIndex != -1) {
            mBuildSdkCombo.select(buildTargetIndex);
        }

        mBuildSdkCombo.addSelectionListener(this);
        mBuildSdkCombo.addFocusListener(this);
        mBuildTargetDec = createFieldDecoration(mBuildSdkCombo,
                "Choose a target API to compile your code against, from your installed SDKs. " +
                "This is typically the most recent version, or the first version that supports " +
                "all the APIs you want to directly access without reflection.");
new Label(container, SWT.NONE);

TemplateMetadata metadata = mValues.template.getTemplate();
//Synthetic comment -- @@ -271,54 +296,25 @@
mThemeParameter = metadata.getParameter("baseTheme"); //$NON-NLS-1$
if (mThemeParameter != null && mThemeParameter.element != null) {
Label themeLabel = new Label(container, SWT.NONE);
                themeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
themeLabel.setText("Theme:");

mThemeCombo = NewTemplatePage.createOptionCombo(mThemeParameter, container,
mValues.parameters, this, this);
                GridData gdThemeCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
                gdThemeCombo.widthHint = FIELD_WIDTH;
                mThemeCombo.setLayoutData(gdThemeCombo);
new Label(container, SWT.NONE);

                mThemeDec = createFieldDecoration(mThemeCombo,
                        "Choose the base theme to use for the application");
}
}

new Label(container, SWT.NONE);
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
//Synthetic comment -- @@ -344,7 +340,41 @@
data.horizontalSpan = 4;
data.widthHint = WIZARD_PAGE_WIDTH;
dummy.setLayoutData(data);
    }

    /**
     * Updates the theme selection such that it's valid for the current build
     * and min sdk targets. Also runs {@link #validatePage} in case no valid entry was found.
     * Does nothing if called on a template that does not supply a theme.
     */
    void updateTheme() {
        if (mThemeParameter != null) {
            // Pick the highest theme version that works for the current SDK level
            Parameter parameter = NewTemplatePage.getParameter(mThemeCombo);
            assert parameter == mThemeParameter;
            if (parameter != null) {
                String[] optionIds = (String[]) mThemeCombo.getData(ATTR_ID);
                for (int index = optionIds.length - 1; index >= 0; index--) {
                    IStatus status = NewTemplatePage.validateCombo(null, mThemeParameter,
                            index, mValues.minSdkLevel, mValues.getBuildApi());
                    if (status == null || status.isOK()) {
                        String optionId = optionIds[index];
                        parameter.value = optionId;
                        parameter.edited = optionId != null && !optionId.toString().isEmpty();
                        mValues.parameters.put(parameter.id, optionId);
                        try {
                            mIgnore = true;
                            mThemeCombo.select(index);
                        } finally {
                            mIgnore = false;
                        }
                        break;
                    }
                }
            }

            validatePage();
        }
}

private IAndroidTarget[] getCompilationTargets() {
//Synthetic comment -- @@ -393,17 +423,6 @@
validatePage();
}

// ---- Implements ModifyListener ----

@Override
//Synthetic comment -- @@ -448,8 +467,6 @@
mIgnore = false;
}
suggestPackage(mValues.applicationName);
}

validatePage();
//Synthetic comment -- @@ -497,12 +514,9 @@
projectName = "";
}

        if (mValues.useDefaultLocation) {
IPath workspace = Platform.getLocation();
String projectLocation = workspace.append(projectName).toOSString();
mValues.projectLocation = projectLocation;
}
}
//Synthetic comment -- @@ -530,100 +544,10 @@
}

Object source = e.getSource();

validatePage();
}

private String getSelectedMinSdk() {
// If you're using a preview build, such as android-JellyBean, you have
// to use the codename, e.g. JellyBean, as the minimum SDK as well.
//Synthetic comment -- @@ -715,6 +639,10 @@
}
mPackageText.setSelection(0, length);
}
        } else if (source == mTargetSdkCombo) {
            tip = mTargetSdkDec.getDescriptionText();
        } else if (source == mThemeCombo) {
            tip = mThemeDec.getDescriptionText();
}
mTipLabel.setText(tip);
mHelpIcon.setVisible(tip.length() > 0);
//Synthetic comment -- @@ -735,6 +663,12 @@
updateDecorator(mApplicationDec, null, true);
updateDecorator(mPackageDec, null, true);
updateDecorator(mProjectDec, null, true);
            updateDecorator(mThemeDec, null, true);
            /* These never get marked with errors:
            updateDecorator(mBuildTargetDec, null, true);
            updateDecorator(mMinSdkDec, null, true);
            updateDecorator(mTargetSdkDec, null, true);
            */
} else {
IStatus appStatus = validateAppName();
if (appStatus != null && (status == null
//Synthetic comment -- @@ -754,7 +688,7 @@
status = packageStatus;
}

            IStatus locationStatus = ProjectContentsPage.validateLocationInWorkspace(mValues);
if (locationStatus != null && (status == null
|| locationStatus.getSeverity() > status.getSeverity())) {
status = locationStatus;
//Synthetic comment -- @@ -784,13 +718,19 @@
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"The minimum SDK version is higher than the build target version");
}
                    if (status == null || status.getSeverity() != IStatus.ERROR) {
                        if (mValues.targetSdkLevel < mValues.minSdkLevel) {
                            status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
                                "The target SDK version should be higher than the minimum SDK version");
                        }
                    }
}
}

            IStatus themeStatus = validateTheme();
            if (themeStatus != null && (status == null
                    || themeStatus.getSeverity() > status.getSeverity())) {
                status = themeStatus;
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

    private IStatus validateTheme() {
        IStatus status = null;

        if (mThemeParameter != null) {
            status = NewTemplatePage.validateCombo(null, mThemeParameter,
                    mThemeCombo.getSelectionIndex(),  mValues.minSdkLevel,
                    mValues.getBuildApi());

            updateDecorator(mThemeDec, status, true);
}

        return status;
}

private void updateDecorator(ControlDecoration decorator, IStatus status, boolean hasInfo) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index c9e801e..72e9985 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.annotations.NonNull;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
//Synthetic comment -- @@ -79,6 +80,7 @@
public static final String DEFAULT_LAUNCHER_ICON = "launcher_icon";   //$NON-NLS-1$

private NewProjectPage mMainPage;
    private ProjectContentsPage mContentsPage;
private ActivityPage mActivityPage;
private NewTemplatePage mTemplatePage;
private NewProjectWizardState mValues;
//Synthetic comment -- @@ -93,6 +95,8 @@

mValues = new NewProjectWizardState();
mMainPage = new NewProjectPage(mValues);
        mContentsPage = new ProjectContentsPage(mValues);
        mContentsPage.init(selection, AdtUtils.getActivePart());
mActivityPage = new ActivityPage(mValues, true, true);
}

//Synthetic comment -- @@ -100,12 +104,17 @@
public void addPages() {
super.addPages();
addPage(mMainPage);
        addPage(mContentsPage);
addPage(mActivityPage);
}

@Override
public IWizardPage getNextPage(IWizardPage page) {
if (page == mMainPage) {
            return mContentsPage;
        }

        if (page == mContentsPage) {
if (mValues.createIcon) {
// Bundle asset studio wizard to create the launcher icon
CreateAssetSetWizardState iconState = mValues.iconState;
//Synthetic comment -- @@ -131,7 +140,11 @@
p.setTitle("Configure Launcher Icon");
return p;
} else {
                if (mValues.createActivity) {
                    return mActivityPage;
                } else {
                    return null;
                }
}
}

//Synthetic comment -- @@ -316,7 +329,7 @@
};

NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
                    mValues.isLibrary, mValues.projectLocation, mValues.workingSets);

// For new projects, ensure that we're actually using the preferred compliance,
// not just the default one
//Synthetic comment -- @@ -391,7 +404,7 @@
parameters.put(ATTR_APP_TITLE, mValues.applicationName);
parameters.put(ATTR_MIN_API, mValues.minSdk);
parameters.put(ATTR_MIN_API_LEVEL, mValues.minSdkLevel);
        parameters.put(ATTR_TARGET_API, mValues.targetSdkLevel);
parameters.put(ATTR_BUILD_API, mValues.target.getVersion().getApiLevel());
parameters.put(ATTR_COPY_ICONS, !mValues.createIcon);
parameters.putAll(mValues.parameters);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 3af40a8..9cd3a6d 100644

//Synthetic comment -- @@ -18,9 +18,12 @@

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.CATEGORY_PROJECTS;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.ui.IWorkingSet;

import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -71,12 +74,15 @@
/** The compilation target to use for this project */
public IAndroidTarget target;

    /** The minimum SDK API level, as a string (if the API is a preview release with a codename) */
public String minSdk;

    /** The minimum SDK API level to use */
public int minSdkLevel;

    /** The target SDK level */
    public int targetSdkLevel = AdtUtils.getHighestKnownApiLevel();

/** Whether this project should be marked as a library project */
public boolean isLibrary;

//Synthetic comment -- @@ -96,12 +102,18 @@
/** State for the template wizard, used to embed an activity template */
public NewTemplateWizardState activityValues = new NewTemplateWizardState();

    /** Whether a custom location should be used */
    public boolean useDefaultLocation = true;

/** Folder where the project should be created. */
public String projectLocation;

/** Configured parameters, by id */
public final Map<String, Object> parameters = new HashMap<String, Object>();

    /** The set of chosen working sets to use when creating the project */
    public IWorkingSet[] workingSets = new IWorkingSet[0];

/**
* Returns the build target API level
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 660b2bb..1cf5daf 100644

//Synthetic comment -- @@ -651,9 +651,15 @@
/** Validates the given combo */
static IStatus validateCombo(IStatus status, Parameter parameter, int minSdk, int buildApi) {
Combo combo = (Combo) parameter.control;
int index = combo.getSelectionIndex();
        return validateCombo(status, parameter, index, minSdk, buildApi);
    }

    /** Validates the given combo assuming the value at the given index is chosen */
    static IStatus validateCombo(IStatus status, Parameter parameter, int index,
            int minSdk, int buildApi) {
        Combo combo = (Combo) parameter.control;
        Integer[] optionIds = (Integer[]) combo.getData(ATTR_MIN_API);
// Check minSdk
if (index != -1 && index < optionIds.length) {
Integer requiredMinSdk = optionIds[index];
//Synthetic comment -- @@ -674,7 +680,7 @@
status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
String.format(
"%1$s \"%2$s\"  requires a build target API version of at " +
                        "least %3$d, and the current version is %4$d",
parameter.name, combo.getItems()[index], requiredBuildApi, buildApi));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ProjectContentsPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ProjectContentsPage.java
new file mode 100644
//Synthetic comment -- index 0000000..7d7881f

//Synthetic comment -- @@ -0,0 +1,380 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.wizards.templates;


import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.WorkingSetGroup;
import com.android.ide.eclipse.adt.internal.wizards.newproject.WorkingSetHelper;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;

import java.io.File;

/**
 * Second wizard page in the "New Project From Template" wizard
 */
public class ProjectContentsPage extends WizardPage
        implements ModifyListener, SelectionListener, FocusListener {

    private final NewProjectWizardState mValues;

    private boolean mIgnore;
    private Button mCustomIconToggle;
    private Button mLibraryToggle;

    private Button mUseDefaultLocationToggle;
    private Label mLocationLabel;
    private Text mLocationText;
    private Button mChooseLocationButton;
    private static String sLastProjectLocation = System.getProperty("user.home"); //$NON-NLS-1$
    private Button mCreateActivityToggle;
    private WorkingSetGroup mWorkingSetGroup;

    ProjectContentsPage(NewProjectWizardState values) {
        super("newAndroidApp"); //$NON-NLS-1$
        mValues = values;
        setTitle("New Android Application");
        setDescription("Configure Project");

        mWorkingSetGroup = new WorkingSetGroup();
        setWorkingSets(new IWorkingSet[0]);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout gl_container = new GridLayout(4, false);
        gl_container.horizontalSpacing = 10;
        container.setLayout(gl_container);

        mCustomIconToggle = new Button(container, SWT.CHECK);
        mCustomIconToggle.setSelection(true);
        mCustomIconToggle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mCustomIconToggle.setText("Create custom launcher icon");
        mCustomIconToggle.setSelection(mValues.createIcon);
        mCustomIconToggle.addSelectionListener(this);

        mCreateActivityToggle = new Button(container, SWT.CHECK);
        mCreateActivityToggle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,
                4, 1));
        mCreateActivityToggle.setText("Create activity");
        mCreateActivityToggle.setSelection(mValues.createActivity);
        mCreateActivityToggle.addSelectionListener(this);

        new Label(container, SWT.NONE).setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));

        mLibraryToggle = new Button(container, SWT.CHECK);
        mLibraryToggle.setSelection(true);
        mLibraryToggle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mLibraryToggle.setText("Mark this project as a library");
        mLibraryToggle.setSelection(mValues.isLibrary);
        mLibraryToggle.addSelectionListener(this);

        // Blank line
        new Label(container, SWT.NONE).setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));

        mUseDefaultLocationToggle = new Button(container, SWT.CHECK);
        mUseDefaultLocationToggle.setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        mUseDefaultLocationToggle.setText("Create Project in Workspace");
        mUseDefaultLocationToggle.addSelectionListener(this);

        mLocationLabel = new Label(container, SWT.NONE);
        mLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        mLocationLabel.setText("Location:");

        mLocationText = new Text(container, SWT.BORDER);
        mLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        mLocationText.addModifyListener(this);

        mChooseLocationButton = new Button(container, SWT.NONE);
        mChooseLocationButton.setText("Browse...");
        mChooseLocationButton.addSelectionListener(this);
        mChooseLocationButton.setEnabled(false);
        setUseCustomLocation(!mValues.useDefaultLocation);

        new Label(container, SWT.NONE).setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));

        Composite group = mWorkingSetGroup.createControl(container);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            try {
                mIgnore = true;
                mUseDefaultLocationToggle.setSelection(mValues.useDefaultLocation);
                mLocationText.setText(mValues.projectLocation);
            } finally {
                mIgnore = false;
            }
        }

        validatePage();
    }

    private void setUseCustomLocation(boolean en) {
        mValues.useDefaultLocation = !en;
        mUseDefaultLocationToggle.setSelection(!en);
        if (!en) {
            updateProjectLocation(mValues.projectName);
        }

        mLocationLabel.setEnabled(en);
        mLocationText.setEnabled(en);
        mChooseLocationButton.setEnabled(en);
    }

    void init(IStructuredSelection selection, IWorkbenchPart activePart) {
        setWorkingSets(WorkingSetHelper.getSelectedWorkingSet(selection, activePart));
    }

    /**
     * Returns the working sets to which the new project should be added.
     *
     * @return the selected working sets to which the new project should be added
     */
    private IWorkingSet[] getWorkingSets() {
        return mWorkingSetGroup.getSelectedWorkingSets();
    }

    /**
     * Sets the working sets to which the new project should be added.
     *
     * @param workingSets the initial selected working sets
     */
    private void setWorkingSets(IWorkingSet[] workingSets) {
        assert workingSets != null;
        mWorkingSetGroup.setWorkingSets(workingSets);
    }

    @Override
    public IWizardPage getNextPage() {
        // Sync working set data to the value object, since the WorkingSetGroup
        // doesn't let us add listeners to do this lazily
        mValues.workingSets = getWorkingSets();

        return super.getNextPage();
    }

    // ---- Implements ModifyListener ----

    @Override
    public void modifyText(ModifyEvent e) {
        if (mIgnore) {
            return;
        }

        Object source = e.getSource();
        if (source == mLocationText) {
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

    // ---- Implements SelectionListener ----

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (mIgnore) {
            return;
        }

        Object source = e.getSource();
        if (source == mCustomIconToggle) {
            mValues.createIcon = mCustomIconToggle.getSelection();
        } else if (source == mLibraryToggle) {
            mValues.isLibrary = mLibraryToggle.getSelection();
        } else if (source == mCreateActivityToggle) {
            mValues.createActivity = mCreateActivityToggle.getSelection();
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

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    // ---- Implements FocusListener ----

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    // Validation

    void validatePage() {
        IStatus status = validateProjectLocation();

        setPageComplete(status == null || status.getSeverity() != IStatus.ERROR);
        if (status != null) {
            setMessage(status.getMessage(),
                    status.getSeverity() == IStatus.ERROR
                        ? IMessageProvider.ERROR : IMessageProvider.WARNING);
        } else {
            setErrorMessage(null);
            setMessage(null);
        }
    }

    static IStatus validateLocationInWorkspace(NewProjectWizardState values) {
        if (values.useDefaultLocation) {
            return null;
        }

        // Validate location
        if (values.projectName != null) {
            File dest = Platform.getLocation().append(values.projectName).toFile();
            if (dest.exists()) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
                   "There is already a file or directory named \"%1$s\" in the selected location.",
                        values.projectName));
            }
        }

        return null;
    }

    private IStatus validateProjectLocation() {
        if (mValues.useDefaultLocation) {
            return validateLocationInWorkspace(mValues);
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
        // exists and is a writable folder
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
}







