/*Offer to install fragment compatibility library

This changeset changes the popup displayed when you drop a fragment
tag in a pre-API-11 project.  Instead of just displaying an error
message, you now get a dialog which asks if you want to install the
project, and if you click the "Install" button it invokes the SDK
manager to install the right package, and then copies it into the
project.

In addition, the fragment selector now has a "Create New" action which
invokes the New Class wizard pre-configured with the right fragment
class.

Change-Id:I4ce1b51fbfe939a21e91379eb6b77ff722c13f0f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index 9b960cb..118e70a 100755

//Synthetic comment -- @@ -33,6 +33,7 @@
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
//Synthetic comment -- @@ -89,7 +90,7 @@
.getAdapter(IProject.class);
}
if (project != null) {
                    install(project, false /* waitForFinish */);
}
}
}
//Synthetic comment -- @@ -99,7 +100,16 @@
mSelection = selection;
}

    /**
     * Install the compatibility jar into the given project.
     *
     * @param project The Android project to install the compatibility jar into
     * @param waitForFinish If true, block until the task has finished
     * @return true if the installation was successful (or if <code>waitForFinish</code>
     *    is false, if the installation is likely to be successful - e.g. the user has
     *    at least agreed to all installation prompts.)
     */
    public static boolean install(final IProject project, boolean waitForFinish) {

final IJavaProject javaProject = JavaCore.create(project);
if (javaProject == null) {
//Synthetic comment -- @@ -110,9 +120,9 @@
final Sdk sdk = Sdk.getCurrent();
if (sdk == null) {
AdtPlugin.printErrorToConsole(
                    AddCompatibilityJarAction.class.getSimpleName(),   // tag
"Error: Android SDK is not loaded yet."); //$NON-NLS-1$
            return false;
}

// TODO: For the generic action, check the library isn't in the project already.
//Synthetic comment -- @@ -130,7 +140,7 @@

if (!result.getFirst().booleanValue()) {
AdtPlugin.printErrorToConsole("Failed to install Android Compatibility library");
            return false;
}

// TODO these "v4" values needs to be dynamic, e.g. we could try to match
//Synthetic comment -- @@ -143,12 +153,12 @@
if (!jarPath.isFile()) {
AdtPlugin.printErrorToConsole("Android Compatibility JAR not found:",
jarPath.getAbsolutePath());
            return false;
}

// Then run an Eclipse asynchronous job to update the project

        Job job = new Job("Add Compatibility Library to Project") {
@Override
protected IStatus run(IProgressMonitor monitor) {
try {
//Synthetic comment -- @@ -183,10 +193,22 @@
}
}
}
        };
        job.schedule();

        if (waitForFinish) {
            try {
                job.join();
                return job.getState() == IStatus.OK;
            } catch (InterruptedException e) {
                AdtPlugin.log(e, null);
            }
        }

        return true;
}

    private static IResource copyJarIntoProject(
IProject project,
File jarPath,
IProgressMonitor monitor) throws IOException, CoreException {
//Synthetic comment -- @@ -202,6 +224,8 @@
// Only modify the file if necessary so that we don't trigger unnecessary recompilations
if (!destPath.isFile() || !isSameFile(jarPath, destPath)) {
copyFile(jarPath, destPath);
            // Make sure Eclipse discovers java.io file changes
            resFolder.refreshLocal(1, new NullProgressMonitor());
}

return destFile;
//Synthetic comment -- @@ -213,7 +237,7 @@
* @param source the source file to copy
* @param destination the destination file to write
*/
    private static boolean isSameFile(File source, File destination) throws IOException {

if (source.length() != destination.length()) {
return false;
//Synthetic comment -- @@ -273,7 +297,7 @@
* @param source the source file to copy
* @param destination the destination file to write
*/
    private static void copyFile(File source, File destination) throws IOException {
FileInputStream fis = null;
FileOutputStream fos = null;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
//Synthetic comment -- index bb519fb..a43eaff 100644

//Synthetic comment -- @@ -289,7 +289,7 @@

if (validator != null) {
// Ensure wide enough to accommodate validator error message
                dlg.setSize(85, 10);
dlg.setInputValidator(validator);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index b90b869..c1c5e5a 100644

//Synthetic comment -- @@ -168,7 +168,7 @@

if (validator != null) {
// Ensure wide enough to accommodate validator error message
                dlg.setSize(85, 10);
dlg.setInputValidator(validator);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 2d0dc66..fbe2957 100755

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.ide.common.layout.ViewRule;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.actions.AddCompatibilityJarAction;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
//Synthetic comment -- @@ -43,6 +44,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -60,9 +62,10 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
//Synthetic comment -- @@ -70,16 +73,24 @@
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.dialogs.ITypeInfoFilterExtension;
import org.eclipse.jdt.ui.dialogs.ITypeInfoRequestor;
import org.eclipse.jdt.ui.dialogs.TypeSelectionExtension;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
//Synthetic comment -- @@ -94,6 +105,7 @@
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
* The rule engine manages the layout rules and interacts with them.
//Synthetic comment -- @@ -926,7 +938,7 @@

if (validator != null) {
// Ensure wide enough to accommodate validator error message
                    dlg.setSize(85, 10);
dlg.setInputValidator(validator);
}

//Synthetic comment -- @@ -988,7 +1000,7 @@
// Compute a search scope: We need to merge all the subclasses
// android.app.Fragment and android.support.v4.app.Fragment
IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
                final IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
if (javaProject != null) {
IType oldFragmentType = javaProject.findType(CLASS_V4_FRAGMENT);

//Synthetic comment -- @@ -997,23 +1009,26 @@
IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);
if (target.getVersion().getApiLevel() < 11 && oldFragmentType == null) {
// Compatibility library must be present
                        MessageDialog dialog =
                            new MessageDialog(
                                    Display.getCurrent().getActiveShell(),
                              "Fragment Warning",
                              null,
                              "Fragments require API level 11 or higher, or a compatibility "
                              + "library for older versions.\n\n"
                              + " Do you want to install the compatibility library?",
                              MessageDialog.QUESTION,
                              new String[] { "Install", "Cancel" },
                              1 /* default button: Cancel */);
                          int answer = dialog.open();
                          if (answer == 0) {
                              if (!AddCompatibilityJarAction.install(mProject,
                                      true /*waitForFinish*/)) {
                                  return null;
                              }
                          } else {
                              return null;
                          }
}

// Look up sub-types of each (new fragment class and compatibility fragment
//Synthetic comment -- @@ -1040,7 +1055,11 @@
}

Shell parent = AdtPlugin.getDisplay().getActiveShell();
                final AtomicReference<String> returnValue =
                    new AtomicReference<String>();
                final AtomicReference<SelectionDialog> dialogHolder =
                    new AtomicReference<SelectionDialog>();
                final SelectionDialog dialog = JavaUI.createTypeDialog(
parent,
new ProgressMonitorDialog(parent),
scope,
//Synthetic comment -- @@ -1049,6 +1068,25 @@
"?", //$NON-NLS-1$
new TypeSelectionExtension() {
@Override
                            public Control createContentArea(Composite parentComposite) {
                                Composite composite = new Composite(parentComposite, SWT.NONE);
                                composite.setLayout(new GridLayout(1, false));
                                Button button = new Button(composite, SWT.PUSH);
                                button.setText("Create New...");
                                button.addSelectionListener(new SelectionAdapter() {
                                    @Override
                                    public void widgetSelected(SelectionEvent e) {
                                        String fqcn = createNewFragmentClass(javaProject);
                                        if (fqcn != null) {
                                            returnValue.set(fqcn);
                                            dialogHolder.get().close();
                                        }
                                    }
                                });
                                return composite;
                            }

                            @Override
public ITypeInfoFilterExtension getFilterExtension() {
return new ITypeInfoFilterExtension() {
public boolean select(ITypeInfoRequestor typeInfoRequestor) {
//Synthetic comment -- @@ -1063,12 +1101,16 @@
};
}
});
                dialogHolder.set(dialog);

dialog.setTitle("Choose Fragment Class");
dialog.setMessage("Select a Fragment class (? = any character, * = any string):");
if (dialog.open() == IDialogConstants.CANCEL_ID) {
return null;
}
                if (returnValue.get() != null) {
                    return returnValue.get();
                }

Object[] types = dialog.getResult();
if (types != null && types.length > 0) {
//Synthetic comment -- @@ -1082,4 +1124,35 @@
return null;
}
}

    private String createNewFragmentClass(IJavaProject javaProject) {
        NewClassWizardPage page = new NewClassWizardPage();

        IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);
        String superClass;
        if (target.getVersion().getApiLevel() < 11) {
            superClass = CLASS_V4_FRAGMENT;
        } else {
            superClass = CLASS_FRAGMENT;
        }
        page.setSuperClass(superClass, true /* canBeModified */);
        IPackageFragmentRoot root = ManifestInfo.getSourcePackageRoot(javaProject);
        if (root != null) {
            page.setPackageFragmentRoot(root, true /* canBeModified */);
        }
        ManifestInfo manifestInfo = ManifestInfo.get(mProject);
        IPackageFragment pkg = manifestInfo.getPackageFragment();
        if (pkg != null) {
            page.setPackageFragment(pkg, true /* canBeModified */);
        }
        OpenNewClassWizardAction action = new OpenNewClassWizardAction();
        action.setConfiguredWizardPage(page);
        action.run();
        IType createdType = page.getCreatedType();
        if (createdType != null) {
            return createdType.getFullyQualifiedName();
        } else {
            return null;
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 0feab55..475f5a7 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchScope;
//Synthetic comment -- @@ -330,6 +331,28 @@
}

/**
     * Returns the {@link IPackageFragment} for the package registered in the manifest
     *
     * @return the {@link IPackageFragment} for the package registered in the manifest
     */
    public IPackageFragment getPackageFragment() {
        sync();
        try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
            if (javaProject != null) {
                IPackageFragmentRoot root = ManifestInfo.getSourcePackageRoot(javaProject);
                if (root != null) {
                    return root.getPackageFragment(mPackage);
                }
            }
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }

        return null;
    }

    /**
* Returns the activity associated with the given layout file. Makes an educated guess
* by peeking at the usages of the R.layout.name field corresponding to the layout and
* if it finds a usage.
//Synthetic comment -- @@ -540,7 +563,7 @@
}

/** Returns the first package root for the given java project */
    public static IPackageFragmentRoot getSourcePackageRoot(IJavaProject javaProject) {
IPackageFragmentRoot packageRoot = null;
List<IPath> sources = BaseProjectHelper.getSourceClasspaths(javaProject);








