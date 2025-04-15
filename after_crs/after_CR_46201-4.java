/*Add AdtPlugin.getShell()

A lot of SWT and JFace methods require a Shell, and we had a lot of
the following calls sprinkled throughout the codebase:

AdtPlugin.getDisplay().getActiveShell()

However, getActiveShell() can return null in quite a few cases (where
it's not necessary), for example during focus transfers.

This CL adds a new method, AdtPlugin.getShell(), which first tries the
above call, but if that fails, will try harder to find another valid
shell (e.g. the first shell in Display.getShells()).

Change-Id:I3214ad56042be6eaf9b0fe0843c820e973fba8c0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 12a861b..f8971dc 100644

//Synthetic comment -- @@ -326,19 +326,50 @@
return sPlugin;
}

    /**
     * Returns the current display, if any
     *
     * @return the display
     */
    @NonNull
public static Display getDisplay() {
synchronized (AdtPlugin.class) {
            if (sPlugin != null) {
                IWorkbench bench = sPlugin.getWorkbench();
                if (bench != null) {
                    Display display = bench.getDisplay();
                    if (display != null) {
                        return display;
                    }
                }
}
}

        Display display = Display.getCurrent();
        if (display != null) {
            return display;
}

        return Display.getDefault();
    }

    /**
     * Returns the shell, if any
     *
     * @return the shell, if any
     */
    @Nullable
    public static Shell getShell() {
        Display display = AdtPlugin.getDisplay();
        Shell shell = display.getActiveShell();
        if (shell == null) {
            Shell[] shells = display.getShells();
            if (shells.length > 0) {
                shell = shells[0];
            }
        }

        return shell;
}

/** Returns the adb path relative to the sdk folder */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddSupportJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddSupportJarAction.java
old mode 100755
new mode 100644
//Synthetic comment -- index 44321aa..c21c8a4

//Synthetic comment -- @@ -184,7 +184,7 @@
// and get the installation path of the library.

AdtUpdateDialog window = new AdtUpdateDialog(
                AdtPlugin.getShell(),
new AdtConsoleSdkLog(),
sdkLocation);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AvdManagerAction.java
old mode 100755
new mode 100644
//Synthetic comment -- index 46177b0..2597090

//Synthetic comment -- @@ -55,7 +55,7 @@

// Runs the updater window, directing all logs to the ADT console.
AvdManagerWindow window = new AvdManagerWindow(
                    AdtPlugin.getShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation(),
AvdInvocationContext.IDE);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
old mode 100755
new mode 100644
//Synthetic comment -- index 9cae8a4..4155989

//Synthetic comment -- @@ -137,7 +137,7 @@
final AtomicBoolean returnValue = new AtomicBoolean(false);

final CloseableProgressMonitorDialog p =
            new CloseableProgressMonitorDialog(AdtPlugin.getShell());
p.setOpenOnRun(true);
try {
p.run(true /*fork*/, true /*cancelable*/, new IRunnableWithProgress() {
//Synthetic comment -- @@ -262,7 +262,7 @@
// log window now.)

SdkUpdaterWindow window = new SdkUpdaterWindow(
                AdtPlugin.getShell(),
new AdtConsoleSdkLog() {
@Override
public void info(@NonNull String msgFormat, Object... args) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ConvertSwitchQuickFixProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ConvertSwitchQuickFixProcessor.java
//Synthetic comment -- index 5a71edc..a99dc76 100644

//Synthetic comment -- @@ -185,7 +185,7 @@

@Override
public void apply(IDocument document) {
            Shell shell = AdtPlugin.getShell();
ConvertSwitchDialog dialog = new ConvertSwitchDialog(shell, mExpression);
dialog.open();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExportScreenshotAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExportScreenshotAction.java
//Synthetic comment -- index 6829c40..ac3328d 100644

//Synthetic comment -- @@ -42,7 +42,7 @@

@Override
public void run() {
        Shell shell = AdtPlugin.getShell();

ImageOverlay imageOverlay = mCanvas.getImageOverlay();
BufferedImage image = imageOverlay.getAwtImage();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 445b295..051c568 100644

//Synthetic comment -- @@ -2366,7 +2366,7 @@
@SuppressWarnings("restriction")
String id = BuildPathsPropertyPage.PROP_ID;
PreferencesUtil.createPropertyDialogOn(
                            AdtPlugin.getShell(),
getProject(), id, null, null).open();
break;
case LINK_OPEN_CLASS:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 5a926e8..496173a 100644

//Synthetic comment -- @@ -813,7 +813,7 @@
if (x <= left) {
// Edit. For now, just rename
InputDialog d = new InputDialog(
                        AdtPlugin.getShell(),
"Rename Preview",  // title
"Name:",
getDisplayName(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 0214433..4b0f484 100644

//Synthetic comment -- @@ -682,7 +682,7 @@
name = getUniqueName();
}
InputDialog d = new InputDialog(
                AdtPlugin.getShell(),
"Add as Thumbnail Preview",  // title
"Name of thumbnail:",
name,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 5162a48..460ef21 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
@Override
public void displayAlert(@NonNull String message) {
MessageDialog.openInformation(
                AdtPlugin.getShell(),
mFqcn,  // title
message);
}
//Synthetic comment -- @@ -175,7 +175,7 @@
}

InputDialog d = new InputDialog(
                    AdtPlugin.getShell(),
mFqcn,  // title
message,
value == null ? "" : value, //$NON-NLS-1$
//Synthetic comment -- @@ -324,7 +324,7 @@
// get the resource repository for this project and the system resources.
ResourceRepository projectRepository =
ResourceManager.getInstance().getProjectResources(project);
            Shell shell = AdtPlugin.getShell();
if (shell == null) {
return null;
}
//Synthetic comment -- @@ -363,7 +363,7 @@
GraphicalEditorPart editor = mRulesEngine.getEditor();
IProject project = editor.getProject();
if (project != null) {
            Shell shell = AdtPlugin.getShell();
if (shell == null) {
return null;
}
//Synthetic comment -- @@ -462,7 +462,7 @@
scope = SearchEngine.createJavaSearchScope(subTypes, IJavaSearchScope.SOURCES);
}

            Shell parent = AdtPlugin.getShell();
final AtomicReference<String> returnValue =
new AtomicReference<String>();
final AtomicReference<SelectionDialog> dialogHolder =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index d9aab14..31d4d0f 100644

//Synthetic comment -- @@ -606,7 +606,7 @@
// open the chooser dialog. It'll fill 'response' with the device to use
// or the AVD to launch.
DeviceChooserDialog dialog = new DeviceChooserDialog(
                            AdtPlugin.getShell(),
response, launchInfo.getPackageName(),
desiredProjectTarget, minApiVersion);
if (dialog.open() == Dialog.OK) {
//Synthetic comment -- @@ -1377,7 +1377,7 @@
}
}
} catch (CoreException e) {
            MessageDialog.openError(AdtPlugin.getShell(),
"Launch Error", e.getStatus().getMessage());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java
//Synthetic comment -- index 44b676f..628972f 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
@Override
protected void apply(IDocument document, IStructuredModel model, Node node, int start,
int end) {
        Shell shell = AdtPlugin.getShell();
InputDensityDialog densityDialog = new InputDensityDialog(shell);
if (densityDialog.open() == Window.OK) {
int dpi = densityDialog.getDensity();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 5d2a2bb..4c356b7 100644

//Synthetic comment -- @@ -505,7 +505,7 @@
sb.append(issue.getMoreInfo());
}

            MessageDialog.openInformation(AdtPlugin.getShell(), "More Info",
sb.toString());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java
//Synthetic comment -- index e468a5a..1de903e 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
List<IProject> projects = AdtUtils.getSelectedProjects(selection);

if (projects.isEmpty() && warn) {
            MessageDialog.openWarning(AdtPlugin.getShell(), "Lint",
"Could not run Lint: Select an Android project first.");
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/RenamePackageAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/RenamePackageAction.java
//Synthetic comment -- index c556f14..bb475aa 100644

//Synthetic comment -- @@ -102,7 +102,7 @@
// enforce a save as a convenience.
RefactoringSaveHelper save_helper = new RefactoringSaveHelper(
RefactoringSaveHelper.SAVE_ALL_ALWAYS_ASK);
                    if (save_helper.saveEditors(AdtPlugin.getShell())) {
promptNewName(project);
}
}
//Synthetic comment -- @@ -142,7 +142,7 @@
}
};

        InputDialog dialog = new InputDialog(AdtPlugin.getShell(),
"Rename Application Package", "Enter new package name:", oldPackageNameString,
validator);

//Synthetic comment -- @@ -165,7 +165,7 @@
new ApplicationPackageNameRefactoringWizard(package_name_refactoring);
RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
try {
            op.run(AdtPlugin.getShell(), package_name_refactoring.getName());
} catch (InterruptedException e) {
Status s = new Status(Status.ERROR, AdtPlugin.PLUGIN_ID, e.getMessage(), e);
AdtPlugin.getDefault().getLog().log(s);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index a215cdc..ce828cf 100644

//Synthetic comment -- @@ -641,7 +641,7 @@
@Nullable
private String createNewFile(ResourceType type) {
// Show a name/value dialog entering the key name and the value
        Shell shell = AdtPlugin.getShell();
if (shell == null) {
return null;
}
//Synthetic comment -- @@ -649,7 +649,7 @@
ResourceNameValidator validator = ResourceNameValidator.create(true /*allowXmlExtension*/,
mProject, mResourceType);
InputDialog d = new InputDialog(
                AdtPlugin.getShell(),
"Enter name",  // title
"Enter name",
"", //$NON-NLS-1$
//Synthetic comment -- @@ -674,7 +674,7 @@
@Nullable
private String createNewValue(ResourceType type) {
// Show a name/value dialog entering the key name and the value
        Shell shell = AdtPlugin.getShell();
if (shell == null) {
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 2f7ad7a..245d84e 100644

//Synthetic comment -- @@ -522,10 +522,10 @@
if (core.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
// The error indicates the file system is not case sensitive
// and there's a resource with a similar name.
                    MessageDialog.openError(AdtPlugin.getShell(),
"Error", "Error: Case Variant Exists");
} else {
                    ErrorDialog.openError(AdtPlugin.getShell(),
"Error", core.getMessage(), core.getStatus());
}
} else {
//Synthetic comment -- @@ -539,7 +539,7 @@
if (msg == null) {
msg = t.toString();
}
                MessageDialog.openError(AdtPlugin.getShell(), "Error", msg);
}
e.printStackTrace();
} catch (InterruptedException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 1cf5daf..1a2d3ca 100644

//Synthetic comment -- @@ -811,7 +811,7 @@
scope = SearchEngine.createJavaSearchScope(classes, IJavaSearchScope.SOURCES);
}

            Shell parent = AdtPlugin.getShell();
final SelectionDialog dialog = JavaUI.createTypeDialog(
parent,
new ProgressMonitorDialog(parent),







