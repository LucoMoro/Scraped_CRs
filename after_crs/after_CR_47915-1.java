/*Refactoring fixes

This CL contains a couple of tweaks to the
refactoring support. First, update <declare-styleable>
declarations for renamed custom views. Second, update
XML in downstream projects from library projects.
Third, allow invoking the rename refactoring from XML
files from class attributes and custom view tags as well
(until now it applied only to resource names.)
Finally, update unit test golden file format to include
whether each change is enabled, and tweak the code to
disable R class changes to handle some additional
scenarios.

Change-Id:I74ccbe1b0f15ec10429f8dda7674f51f9a6f83cd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index a5b29e3..b821777 100644

//Synthetic comment -- @@ -34,6 +34,8 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
import com.android.utils.SdkUtils;

//Synthetic comment -- @@ -82,6 +84,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
//Synthetic comment -- @@ -250,7 +253,17 @@
// Update layout files; we don't just need to react to custom view
// changes, we need to update fragment references and even tool:context activity
// references
        addLayoutFileChanges(mProject, result);

        // Also update in dependent projects
        ProjectState projectState = Sdk.getProjectState(mProject);
        if (projectState != null) {
            Collection<ProjectState> parentProjects = projectState.getFullParentProjects();
            for (ProjectState parentProject : parentProjects) {
                IProject project = parentProject.getProject();
                addLayoutFileChanges(project, result);
            }
        }

if (mRefactoringAppPackage) {
Change genChange = getGenPackageChange(pm);
//Synthetic comment -- @@ -335,10 +348,10 @@
addXmlFileChanges(mManifestFile, result, true);
}

    private void addLayoutFileChanges(IProject project, CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 7181f98..2146184 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
import com.android.utils.SdkUtils;

//Synthetic comment -- @@ -69,6 +71,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
//Synthetic comment -- @@ -166,7 +169,17 @@
// Update layout files; we don't just need to react to custom view
// changes, we need to update fragment references and even tool:context activity
// references
        addLayoutFileChanges(mProject, result);

        // Also update in dependent projects
        ProjectState projectState = Sdk.getProjectState(mProject);
        if (projectState != null) {
            Collection<ProjectState> parentProjects = projectState.getFullParentProjects();
            for (ProjectState parentProject : parentProjects) {
                IProject project = parentProject.getProject();
                addLayoutFileChanges(project, result);
            }
        }

return (result.getChildren().length == 0) ? null : result;
}
//Synthetic comment -- @@ -175,10 +188,10 @@
addXmlFileChanges(mManifestFile, result, true);
}

    private void addLayoutFileChanges(IProject project, CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 759879a..7843ab3 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.ide.eclipse.adt.internal.refactorings.core;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLASS;
import static com.android.SdkConstants.ATTR_CONTEXT;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.CLASS_VIEW;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.EXT_XML;
import static com.android.SdkConstants.R_CLASS;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.SdkConstants.VIEW_FRAGMENT;
import static com.android.SdkConstants.VIEW_TAG;
//Synthetic comment -- @@ -31,8 +34,14 @@
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.utils.SdkUtils;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -41,10 +50,13 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -54,6 +66,7 @@
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
//Synthetic comment -- @@ -70,6 +83,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
//Synthetic comment -- @@ -88,8 +102,19 @@
private IFile mManifestFile;
private String mOldFqcn;
private String mNewFqcn;
    private String mOldSimpleName;
    private String mNewSimpleName;
private String mOldDottedName;
private String mNewDottedName;
    private boolean mIsCustomView;

    /**
     * Set while we are creating an embedded Java refactoring. This could cause a recursive
     * invocation of the XML renaming refactoring to react to the field, so this is flag
     * during the call to the Java processor, and is used to ignore requests for adding in
     * field reactions during that time.
     */
    private static boolean sIgnore;

@Override
public String getName() {
//Synthetic comment -- @@ -104,6 +129,10 @@

@Override
protected boolean initialize(Object element) {
        if (sIgnore) {
            return false;
        }

if (element instanceof IType) {
IType type = (IType) element;
IJavaProject javaProject = (IJavaProject) type.getAncestor(IJavaElement.JAVA_PROJECT);
//Synthetic comment -- @@ -119,20 +148,35 @@
mProject.getName()));
return false;
}

            try {
                IType classView = javaProject.findType(CLASS_VIEW);
                if (classView != null) {
                    ITypeHierarchy hierarchy = type.newSupertypeHierarchy(new NullProgressMonitor());
                    if (hierarchy.contains(classView)) {
                        mIsCustomView = true;
                    }
                }
            } catch (CoreException e) {
                AdtPlugin.log(e, null);
            }

mManifestFile = (IFile) manifestResource;
ManifestData manifestData;
manifestData = AndroidManifestHelper.parseForData(mManifestFile);
if (manifestData == null) {
return false;
}
            mOldSimpleName = type.getElementName();
            mOldDottedName = '.' + mOldSimpleName;
mOldFqcn = type.getFullyQualifiedName();
String packageName = type.getPackageFragment().getElementName();
            mNewSimpleName = getArguments().getNewName();
            mNewDottedName = '.' + mNewSimpleName;
if (packageName != null) {
mNewFqcn = packageName + mNewDottedName;
} else {
                mNewFqcn = mNewSimpleName;
}
if (mOldFqcn == null || mNewFqcn == null) {
return false;
//Synthetic comment -- @@ -178,26 +222,118 @@
// Only show the children in the refactoring preview dialog
result.markAsSynthetic();

        addManifestFileChanges(mManifestFile, result);
        addLayoutFileChanges(mProject, result);
        addJavaChanges(mProject, result, pm);

        // Also update in dependent projects
        // TODO: Also do the Java elements, if they are in Jar files, since the library
        // projects do this (and the JDT refactoring does not include them)
        ProjectState projectState = Sdk.getProjectState(mProject);
        if (projectState != null) {
            Collection<ProjectState> parentProjects = projectState.getFullParentProjects();
            for (ProjectState parentProject : parentProjects) {
                IProject project = parentProject.getProject();
                IResource manifestResource = project.findMember(AdtConstants.WS_SEP
                        + SdkConstants.FN_ANDROID_MANIFEST_XML);
                if (manifestResource != null && manifestResource.exists()
                        && manifestResource instanceof IFile) {
                    addManifestFileChanges((IFile) manifestResource, result);
                }
                addLayoutFileChanges(project, result);
                addJavaChanges(project, result, pm);
            }
        }

        // Look for the field change on the R.java class; it's a derived file
        // and will generate file modified manually warnings. Disable it.
        RenameResourceParticipant.disableRClassChanges(result);

return (result.getChildren().length == 0) ? null : result;
}

    private void addJavaChanges(IProject project, CompositeChange result, IProgressMonitor monitor) {
        if (!mIsCustomView) {
            return;
        }

        // Also rename styleables, if any
        try {
            // Find R class
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
            ManifestInfo info = ManifestInfo.get(project);
            info.getPackage();
            String rFqcn = info.getPackage() + '.' + R_CLASS;
            IType styleable = javaProject.findType(rFqcn + '.' + ResourceType.STYLEABLE.getName());
            if (styleable != null) {
                IField[] fields = styleable.getFields();
                CompositeChange fieldChanges = null;
                for (IField field : fields) {
                    String name = field.getElementName();
                    if (name.equals(mOldSimpleName) || name.startsWith(mOldSimpleName)
                            && name.length() > mOldSimpleName.length()
                            && name.charAt(mOldSimpleName.length()) == '_') {
                        // Rename styleable fields
                        String newName = name.equals(mOldSimpleName) ? mNewSimpleName :
                            mNewSimpleName + name.substring(mOldSimpleName.length());
                        RenameRefactoring refactoring =
                                RenameResourceParticipant.createFieldRefactoring(field,
                                        newName, true);

                        try {
                            sIgnore = true;
                            RefactoringStatus status = refactoring.checkAllConditions(monitor);
                            if (status != null && !status.hasError()) {
                                Change fieldChange = refactoring.createChange(monitor);
                                if (fieldChange != null) {
                                    if (fieldChanges == null) {
                                        fieldChanges = new CompositeChange(
                                                "Update custom view styleable fields");
                                        // Disable these changes. They sometimes end up
                                        // editing the wrong offsets. It looks like Eclipse
                                        // doesn't ensure that after applying each change it
                                        // also adjusts the other field offsets. I poked around
                                        // and couldn't find a way to do this properly, but
                                        // at least by listing the diffs here it shows what should
                                        // be done.
                                        fieldChanges.setEnabled(false);
                                    }
                                    // Disable change: see comment above.
                                    fieldChange.setEnabled(false);
                                    fieldChanges.add(fieldChange);
                                }
                            }
                        } catch (CoreException e) {
                            AdtPlugin.log(e, null);
                        } finally {
                            sIgnore = false;
                        }
                    }
                }
                if (fieldChanges != null) {
                    result.add(fieldChanges);
                }
            }
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }
}

    private void addManifestFileChanges(IFile manifestFile, CompositeChange result) {
        addXmlFileChanges(manifestFile, result, null);
    }

    private void addLayoutFileChanges(IProject project, CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {
String folderName = folder.getName();
ResourceFolderType folderType = ResourceFolderType.getFolderType(folderName);
                if (folderType != ResourceFolderType.LAYOUT &&
                        folderType != ResourceFolderType.VALUES) {
continue;
}
if (!(folder instanceof IFolder)) {
//Synthetic comment -- @@ -211,7 +347,7 @@
String fileName = member.getName();

if (SdkUtils.endsWith(fileName, DOT_XML)) {
                            addXmlFileChanges(file, result, folderType);
}
}
}
//Synthetic comment -- @@ -221,7 +357,8 @@
}
}

    private boolean addXmlFileChanges(IFile file, CompositeChange changes,
            ResourceFolderType folderType) {
IModelManager modelManager = StructuredModelManager.getModelManager();
IStructuredModel model = null;
try {
//Synthetic comment -- @@ -236,9 +373,13 @@
Element root = domModel.getDocument().getDocumentElement();
if (root != null) {
List<TextEdit> edits = new ArrayList<TextEdit>();
                        if (folderType == null) {
                            assert file.getName().equals(ANDROID_MANIFEST_XML);
addManifestReplacements(edits, root, document);
                        } else if (folderType == ResourceFolderType.VALUES) {
                            addValueReplacements(edits, root, document);
} else {
                            assert folderType == ResourceFolderType.LAYOUT;
addLayoutReplacements(edits, root, document);
}
if (!edits.isEmpty()) {
//Synthetic comment -- @@ -328,6 +469,28 @@
}
}

    private void addValueReplacements(
            @NonNull List<TextEdit> edits,
            @NonNull Element root,
            @NonNull IStructuredDocument document) {
        // Look for styleable renames for custom views
        String declareStyleable = ResourceType.DECLARE_STYLEABLE.getName();
        List<Element> topLevel = DomUtilities.getChildren(root);
        for (Element element : topLevel) {
            String tag = element.getTagName();
            if (declareStyleable.equals(tag)) {
                Attr nameNode = element.getAttributeNode(ATTR_NAME);
                if (nameNode != null && mOldSimpleName.equals(nameNode.getValue())) {
                    int start = RefactoringUtil.getAttributeValueRangeStart(nameNode, document);
                    if (start != -1) {
                        int end = start + mOldSimpleName.length();
                        edits.add(new ReplaceEdit(start, end - start, mNewSimpleName));
                    }
                }
            }
        }
    }

private void addManifestReplacements(
@NonNull List<TextEdit> edits,
@NonNull Element element,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipant.java
//Synthetic comment -- index 8bd0e13..438e822 100644

//Synthetic comment -- @@ -264,13 +264,29 @@

/** Create nested Java refactoring which updates the R field references, if applicable */
private RenameRefactoring createFieldRefactoring(IField field) {
        return createFieldRefactoring(field, mNewName, mUpdateReferences);
    }

    /**
     * Create nested Java refactoring which updates the R field references, if
     * applicable
     *
     * @param field the field to be refactored
     * @param newName the new name
     * @param updateReferences whether references should be updated
     * @return a new rename refactoring
     */
    public static RenameRefactoring createFieldRefactoring(
            @NonNull IField field,
            @NonNull String newName,
            boolean updateReferences) {
RenameFieldProcessor processor = new RenameFieldProcessor(field);
processor.setRenameGetter(false);
processor.setRenameSetter(false);
RenameRefactoring refactoring = new RenameRefactoring(processor);
        processor.setUpdateReferences(updateReferences);
processor.setUpdateTextualMatches(false);
        processor.setNewElementName(newName);
try {
if (refactoring.isApplicable()) {
return refactoring;
//Synthetic comment -- @@ -469,7 +485,7 @@

// Look for the field change on the R.java class; it's a derived file
// and will generate file modified manually warnings. Disable it.
                disableRClassChanges(fieldChanges);
}
}
}
//Synthetic comment -- @@ -680,6 +696,7 @@
}

String pkg = ManifestInfo.get(project).getPackage();
            // TODO: Rename in all libraries too?
IType t = javaProject.findType(pkg + '.' + R_CLASS + '.' + type.getName());
if (t == null) {
return null;
//Synthetic comment -- @@ -717,19 +734,18 @@
* Searches for existing changes in the refactoring which modifies the R
* field to rename it. it's derived so performing this change will generate
* a "generated code was modified manually" warning
     *
     * @param change the change to disable R file changes in
*/
    public static void disableRClassChanges(Change change) {
        if (change.getName().equals(FN_RESOURCE_CLASS)) {
            change.setEnabled(false);
        }
// Look for the field change on the R.java class; it's a derived file
// and will generate file modified manually warnings. Disable it.
if (change instanceof CompositeChange) {
for (Change outer : ((CompositeChange) change).getChildren()) {
                disableRClassChanges(outer);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java
//Synthetic comment -- index 4ca7837..3b1fa52 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactorings.core;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_PREFIX;
import static com.android.SdkConstants.ANDROID_THEME_PREFIX;
import static com.android.SdkConstants.ATTR_NAME;
//Synthetic comment -- @@ -27,10 +28,20 @@
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.ui.refactoring.reorg.RenameTypeWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
//Synthetic comment -- @@ -38,9 +49,13 @@
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
//Synthetic comment -- @@ -48,13 +63,16 @@
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Text action for XML files to invoke renaming
* <p>
* TODO: Handle other types of renaming: invoking class renaming when editing
* class names in layout files and manifest files, renaming attribute names when
* editing a styleable attribute, etc.
*/
@SuppressWarnings("restriction") // Java rename refactoring
public final class RenameResourceXmlTextAction extends Action {
private final ITextEditor mEditor;

//Synthetic comment -- @@ -73,6 +91,14 @@
if (!validateEditorInputState()) {
return;
}
        IFile file = getFile();
        if (file == null) {
            return;
        }
        IProject project = file.getProject();
        if (project == null) {
            return;
        }
IDocument document = getDocument();
if (document == null) {
return;
//Synthetic comment -- @@ -94,20 +120,41 @@
Shell shell = mEditor.getSite().getShell();
boolean canClear = false;

            RenameResourceWizard.renameResource(shell, project, type, name, null, canClear);
            return;
        }

        String className = findClassName(document, file, selection.getOffset());
        if (className != null) {
            assert className.equals(className.trim());
            IType type = findType(className, project);
            if (type != null) {
                RenameTypeProcessor processor = new RenameTypeProcessor(type);
                //processor.setNewElementName(className);
                processor.setUpdateQualifiedNames(true);
                processor.setUpdateSimilarDeclarations(false);
                //processor.setMatchStrategy(?);
                //processor.setFilePatterns(patterns);
                processor.setUpdateReferences(true);

                RenameRefactoring refactoring = new RenameRefactoring(processor);
                RenameTypeWizard wizard = new RenameTypeWizard(refactoring);
                RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
                try {
                    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    op.run(window.getShell(), wizard.getDefaultPageTitle());
                } catch (InterruptedException e) {
                }
}

            return;
}

// Fallback: tell user the cursor isn't in the right place
MessageDialog.openInformation(mEditor.getSite().getShell(),
"Rename",
"Operation unavailable on the current selection.\n"
                        + "Select an Android resource name or class.");
}

private boolean validateEditorInputState() {
//Synthetic comment -- @@ -223,6 +270,110 @@
return null;
}

    /**
     * Searches for a fully qualified class name around the caret, such as {@code foo.bar.MyClass}
     *
     * @param document the document to search in
     * @param file the file, if known
     * @param offset the offset to search at
     * @return a resource pair, or null if not found
     */
    @Nullable
    public static String findClassName(
            @NonNull IDocument document,
            @Nullable IFile file,
            int offset) {
        try {
            int max = document.getLength();
            if (offset >= max) {
                offset = max - 1;
            } else if (offset < 0) {
                offset = 0;
            } else if (offset > 0) {
                // If the caret is right after a resource name (meaning getChar(offset) points
                // to the following character), back up
                char c = document.getChar(offset);
                if (Character.isJavaIdentifierPart(c)) {
                    offset--;
                }
            }

            int start = offset;
            for (; start >= 0; start--) {
                char c = document.getChar(start);
                if (c == '"' || c == '<' || c == '/') {
                    start++;
                    break;
                } else if (c != '.' && !Character.isJavaIdentifierPart(c)) {
                    return null;
                }
            }
            // Search forwards for the end
            int end = start + 1;
            for (; end < max; end++) {
                char c = document.getChar(end);
                if (c != '.' && !Character.isJavaIdentifierPart(c)) {
                    if (c != '"' && c != '>' && !Character.isWhitespace(c)) {
                        return null;
                    }
                    break;
                }
            }
            if (end > start + 1) {
                String fqcn = document.get(start, end - start);
                int dot = fqcn.indexOf('.');
                if (dot == -1) { // Only support fully qualified names
                    return null;
                }
                if (dot == 0) { // Special case for manifests: prepend package
                    if (file != null && file.getName().equals(ANDROID_MANIFEST_XML)) {
                        ManifestInfo info = ManifestInfo.get(file.getProject());
                        return info.getPackage() + fqcn;
                    }
                    return null;
                }

                return fqcn;
            }
        } catch (BadLocationException e) {
            AdtPlugin.log(e, null);
        }

        return null;
    }

    @Nullable
    private IType findType(@NonNull String className, @NonNull IProject project) {
        IType type = null;
        try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
            type = javaProject.findType(className);
            if (type == null || !type.exists()) {
                return null;
            }
            if (!type.isBinary()) {
                return type;
            }
            // See if this class is coming through a library project jar file and
            // if so locate the real class
            ProjectState projectState = Sdk.getProjectState(project);
            if (projectState != null) {
                List<IProject> libraries = projectState.getFullLibraryProjects();
                for (IProject library : libraries) {
                    javaProject = BaseProjectHelper.getJavaProject(library);
                    type = javaProject.findType(className);
                    if (type != null && type.exists() && !type.isBinary()) {
                        return type;
                    }
                }
            }
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }

        return null;
    }

private ITextSelection getSelection() {
ISelectionProvider selectionProvider = mEditor.getSelectionProvider();
if (selectionProvider == null) {
//Synthetic comment -- @@ -246,4 +397,15 @@
}
return document;
}

    @Nullable
    private IFile getFile() {
        IEditorInput input = mEditor.getEditorInput();
        if (input instanceof IFileEditorInput) {
            IFileEditorInput fileInput = (IFileEditorInput) input;
            return fileInput.getFile();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 95ae529..872d18e 100644

//Synthetic comment -- @@ -405,6 +405,7 @@
Collections.reverse(mChanges);
CompositeChange change = new CompositeChange("Refactoring Application package name",
mChanges.toArray(new Change[mChanges.size()]));
            change.markAsSynthetic();
return change;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
//Synthetic comment -- index bff6a0b..1df0b0c 100644

//Synthetic comment -- @@ -41,21 +41,21 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "[x] activity_main.xml - /testRefactor1/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -74,7 +74,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename package 'com.example.refactoringtest' to 'my.pkg.name'",
false);
}

//Synthetic comment -- @@ -88,33 +88,33 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "[x] activity_main.xml - /testRefactor2/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor2/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -134,33 +134,33 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor2_renamesub/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "[x] activity_main.xml - /testRefactor2_renamesub/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor2_renamesub/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -169,13 +169,13 @@
"  +             android:name=\"my.pkg.name.MainActivity2\"\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
"  @@ -15 +15\n" +
"  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
"  +     <my.pkg.name.subpackage.CustomView2\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
"  @@ -15 +15\n" +
"  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
"  +     <my.pkg.name.subpackage.CustomView2",
//Synthetic comment -- @@ -192,7 +192,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'",
false);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index 1a97b83..dd0d68d 100644

//Synthetic comment -- @@ -146,7 +146,11 @@

// Describe this change
indent(sb, indent);
            if (change.isEnabled()) {
                sb.append("[x] ");
            } else {
                sb.append("[ ] ");
            }
sb.append(changeName);

IFile file = getFile(change);
//Synthetic comment -- @@ -580,6 +584,19 @@
"    }\n" +
"}\n";

    protected static final String CUSTOM_VIEW_1_STYLES =
            "<resources>\n" +
            "\n" +
            "    <!-- Aattributes for the custom view -->\n" +
            "    <declare-styleable name=\"CustomView1\">\n" +
            "        <attr name=\"exampleString\" format=\"string\" />\n" +
            "        <attr name=\"exampleDimension\" format=\"dimension\" />\n" +
            "        <attr name=\"exampleColor\" format=\"color\" />\n" +
            "        <attr name=\"exampleDrawable\" format=\"color|reference\" />\n" +
            "    </declare-styleable>\n" +
            "\n" +
            "</resources>";

protected static final String CUSTOM_VIEW_2 =
"package com.example.refactoringtest.subpackage;\n" +
"\n" +
//Synthetic comment -- @@ -625,6 +642,9 @@
"src/com/example/refactoringtest/CustomView1.java",
CUSTOM_VIEW_1,

        "res/values/attrs_custom_view.xml",
        CUSTOM_VIEW_1_STYLES,

"src/com/example/refactoringtest/subpackage/CustomView2.java",
CUSTOM_VIEW_2,









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index b7793bb..ccf6e4f 100644

//Synthetic comment -- @@ -42,19 +42,19 @@

"CHANGES:\n" +
"-------\n" +
                "[x] strings.xml - /testRefactor1/res/values/strings.xml\n" +
"  @@ -4 +4\n" +
"  -     <string name=\"app_name\">RefactoringTest</string>\n" +
"  +     <string name=\"myname\">RefactoringTest</string>\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor1/gen/com/example/refactoringtest/R.java\n" +
"  @@ -29 +29\n" +
"  -         public static final int app_name=0x7f040000;\n" +
"  +         public static final int myname=0x7f040000;\n" +
"\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -13 +13\n" +
"  -         android:label=\"@string/app_name\"\n" +
"  +         android:label=\"@string/myname\"\n" +
//Synthetic comment -- @@ -72,13 +72,13 @@

"CHANGES:\n" +
"-------\n" +
                "[x] activity_main.xml - /testRefactor2/res/menu/activity_main.xml\n" +
"  @@ -4 +4\n" +
"  -         android:id=\"@+id/menu_settings\"\n" +
"  +         android:id=\"@+id/new_id_for_the_action_bar\"\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor2/gen/com/example/refactoringtest/R.java\n" +
"  @@ -19 +19\n" +
"  -         public static final int menu_settings=0x7f070003;\n" +
"  +         public static final int new_id_for_the_action_bar=0x7f070003;");
//Synthetic comment -- @@ -93,7 +93,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] activity_main.xml - /testRefactor3/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
//Synthetic comment -- @@ -104,13 +104,13 @@
"  +         android:layout_below=\"@+id/output\"\n" +
"\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor3/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -14 +14\n" +
"  -         View view1 = findViewById(R.id.textView1);\n" +
"  +         View view1 = findViewById(R.id.output);\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor3/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -126,7 +126,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] activity_main.xml - /testRefactor4/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
//Synthetic comment -- @@ -137,13 +137,13 @@
"  +         android:layout_below=\"@+id/output\"\n" +
"\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor4/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -14 +14\n" +
"  -         View view1 = findViewById(R.id.textView1);\n" +
"  +         View view1 = findViewById(R.id.output);\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor4/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -158,21 +158,21 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MainActivity.java - /testRefactor5/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlayout);\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor5/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlayout=0x7f030000;\n" +
"\n" +
"\n" +
                "[x] Rename 'testRefactor5/res/layout/activity_main.xml' to 'newlayout.xml'\n" +
"\n" +
                "[x] Rename 'testRefactor5/res/layout-land/activity_main.xml' to 'newlayout.xml'");
}

public void testRefactor6() throws Exception {
//Synthetic comment -- @@ -184,21 +184,21 @@

"CHANGES:\n" +
"-------\n" +
                "[ ] R.java - /testRefactor6/gen/com/example/refactoringtest/R.java\n" +
"  @@ -14 +14\n" +
"  -         public static final int ic_launcher=0x7f020000;\n" +
"  +         public static final int newlauncher=0x7f020000;\n" +
"\n" +
"\n" +
                "[x] Rename 'testRefactor6/res/drawable-xhdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "[x] Rename 'testRefactor6/res/drawable-mdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "[x] Rename 'testRefactor6/res/drawable-ldpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "[x] Rename 'testRefactor6/res/drawable-hdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor6/AndroidManifest.xml\n" +
"  @@ -12 +12\n" +
"  -         android:icon=\"@drawable/ic_launcher\"\n" +
"  +         android:icon=\"@drawable/newlauncher\"");
//Synthetic comment -- @@ -216,21 +216,21 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MainActivity.java - /testRefactor7/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlayout);\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor7/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlayout=0x7f030000;\n" +
"\n" +
"\n" +
                "[x] Rename 'testRefactor7/res/layout-land/activity_main.xml' to 'newlayout.xml'\n" +
"\n" +
                "[x] Rename 'testRefactor7/res/layout/activity_main.xml' to 'newlayout.xml'",
null);
}

//Synthetic comment -- @@ -258,17 +258,17 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename 'testRefactor8/res/layout/activity_main.xml' to 'newlauncher.xml'\n" +
"\n" +
                "[x] Rename 'testRefactor8/res/layout-land/activity_main.xml' to 'newlauncher.xml'\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor8/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlauncher);\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor8/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlauncher=0x7f030000;",
//Synthetic comment -- @@ -302,13 +302,13 @@

"CHANGES:\n" +
"-------\n" +
                "[x] activity_main.xml - /testRefactor9/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
"\n" +
"\n" +
                "[ ] R.java - /testRefactor9/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -324,7 +324,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] activity_main.xml - /testRefactor10/res/layout-land/activity_main.xml\n" +
"  @@ -10 +10\n" +
"  -         tools:listitem=\"@layout/preview\" >\n" +
"  +         tools:listitem=\"@layout/newlayout\" >\n" +








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java
//Synthetic comment -- index 742aa31..438906d 100644

//Synthetic comment -- @@ -32,25 +32,23 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MainActivity2.java - /testRefactor1/src/com/example/refactoringtest/MainActivity2.java\n" +
                "  @@ -7 +7\n" +
                "  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -7 +7\n" +
                "  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] Make Manifest edits - /testRefactor1/AndroidManifest.xml\n" +
                "  @@ -3 +3\n" +
                "  -     package=\"com.example.refactoringtest\"\n" +
                "  +     package=\"my.pkg.name\"\n" +
                "  @@ -25 +25\n" +
                "  -             android:name=\".MainActivity2\"\n" +
                "  +             android:name=\"com.example.refactoringtest.MainActivity2\"");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -61,30 +59,28 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MyFragment.java - /testRefactor2/src/com/example/refactoringtest/MyFragment.java\n" +
                "  @@ -3 +3\n" +
                "  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -7 +7\n" +
                "  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] CustomView1.java - /testRefactor2/src/com/example/refactoringtest/CustomView1.java\n" +
                "  @@ -5 +5\n" +
                "  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] Make Manifest edits - /testRefactor2/AndroidManifest.xml\n" +
                "  @@ -3 +3\n" +
                "  -     package=\"com.example.refactoringtest\"\n" +
                "  +     package=\"my.pkg.name\"\n" +
                "  @@ -25 +25\n" +
                "  -             android:name=\".MainActivity2\"\n" +
                "  +             android:name=\"com.example.refactoringtest.MainActivity2\"");
}

// ---- Test infrastructure ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java
//Synthetic comment -- index 0fb8523..262ea42 100644

//Synthetic comment -- @@ -46,17 +46,17 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "[x] Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "[x] customviews.xml - /testRefactor1/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.subpackage.CustomView1\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor1/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.subpackage.CustomView1");
//Synthetic comment -- @@ -71,11 +71,11 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Move resource 'testRefactorFragment/src/com/example/refactoringtest/MyFragment.java' to 'subpackage'\n" +
"\n" +
                "[x] Move resource 'testRefactorFragment/src/com/example/refactoringtest/MyFragment.java' to 'subpackage'\n" +
"\n" +
                "[x] activity_main.xml - /testRefactorFragment/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"com.example.refactoringtest.subpackage.MyFragment\"/>");
//Synthetic comment -- @@ -90,9 +90,9 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "[x] Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'");
}

// ---- Test infrastructure ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java
//Synthetic comment -- index f54a49a..f65124a 100644

//Synthetic comment -- @@ -36,21 +36,21 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'\n" +
"\n" +
                "[x] activity_main.xml - /testRefactor1/res/layout/activity_main.xml\n" +
"  @@ -5 +5\n" +
"  -     tools:context=\".MainActivity\" >\n" +
"  +     tools:context=\".NewActivityName\" >\n" +
"\n" +
"\n" +
                "[x] activity_main.xml - /testRefactor1/res/layout-land/activity_main.xml\n" +
"  @@ -5 +5\n" +
"  -     tools:context=\".MainActivity\" >\n" +
"  +     tools:context=\".NewActivityName\" >\n" +
"\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"com.example.refactoringtest.NewActivityName\"");
//Synthetic comment -- @@ -65,9 +65,9 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename compilation unit 'MainActivity2.java' to 'NewActivityName.java'\n" +
"\n" +
                "[x] AndroidManifest.xml - /testRefactor1b/AndroidManifest.xml\n" +
"  @@ -25 +25\n" +
"  -             android:name=\".MainActivity2\"\n" +
"  +             android:name=\".NewActivityName\"");
//Synthetic comment -- @@ -82,7 +82,7 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -94,15 +94,21 @@

"CHANGES:\n" +
"-------\n" +
                "[x] Rename compilation unit 'CustomView1.java' to 'NewCustomViewName.java'\n" +
"\n" +
                "[x] attrs_custom_view.xml - /testRefactor2/res/values/attrs_custom_view.xml\n" +
                "  @@ -4 +4\n" +
                "  -     <declare-styleable name=\"CustomView1\">\n" +
                "  +     <declare-styleable name=\"NewCustomViewName\">\n" +
                "\n" +
                "\n" +
                "[x] customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.NewCustomViewName\n" +
"\n" +
"\n" +
                "[x] customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.NewCustomViewName");
//Synthetic comment -- @@ -117,9 +123,9 @@

"CHANGES:\n" +
"-------\n" +
            "[x] Rename compilation unit 'MyFragment.java' to 'NewFragmentName.java'\n" +
"\n" +
            "[x] activity_main.xml - /testRefactorFragment/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"com.example.refactoringtest.NewFragmentName\"/>");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextActionTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextActionTest.java
//Synthetic comment -- index 047168d..11fc81a 100644

//Synthetic comment -- @@ -52,6 +52,43 @@
checkWord("\n^?foo\"", Pair.of(ResourceType.ATTR, "foo"));
}

    public void testClassNames() throws Exception {
        checkClassName("^foo", null);
        checkClassName("<^foo>", null);
        checkClassName("'foo.bar.Baz'^", null);
        checkClassName("<^foo.bar.Baz ", "foo.bar.Baz");
        checkClassName("<^foo.bar.Baz>", "foo.bar.Baz");
        checkClassName("<foo.^bar.Baz>", "foo.bar.Baz");
        checkClassName("<foo.bar.Baz^>", "foo.bar.Baz");
        checkClassName("<foo.bar.Baz^ >", "foo.bar.Baz");
        checkClassName("<foo.bar$Baz^ >", "foo.bar.Baz");
        checkClassName("</^foo.bar.Baz>", "foo.bar.Baz");
        checkClassName("</foo.^bar.Baz>", "foo.bar.Baz");

        checkClassName("\"^foo.bar.Baz\"", "foo.bar.Baz");
        checkClassName("\"foo.^bar.Baz\"", "foo.bar.Baz");
        checkClassName("\"foo.bar.Baz^\"", "foo.bar.Baz");
        checkClassName("\"foo.bar$Baz^\"", "foo.bar.Baz");

        checkClassName("<foo.^bar@Baz>", null);
    }

    private void checkClassName(String contents, String expectedClassName)
            throws Exception {
        int cursor = contents.indexOf('^');
        assertTrue("Must set cursor position with ^ in " + contents, cursor != -1);
        contents = contents.substring(0, cursor) + contents.substring(cursor + 1);
        assertEquals(-1, contents.indexOf('^'));
        assertEquals(-1, contents.indexOf('['));
        assertEquals(-1, contents.indexOf(']'));

        IDocument document = new Document();
        document.replace(0, 0, contents);
        String className =
                RenameResourceXmlTextAction.findClassName(document, null, cursor);
        assertEquals(expectedClassName, className);
    }

private void checkWord(String contents, Pair<ResourceType, String> expectedResource)
throws Exception {
int cursor = contents.indexOf('^');







