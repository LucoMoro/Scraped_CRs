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
import com.android.resources.ResourceFolderType;
import com.android.utils.SdkUtils;

//Synthetic comment -- @@ -82,6 +84,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
//Synthetic comment -- @@ -250,7 +253,17 @@
// Update layout files; we don't just need to react to custom view
// changes, we need to update fragment references and even tool:context activity
// references
        addLayoutFileChanges(result);

if (mRefactoringAppPackage) {
Change genChange = getGenPackageChange(pm);
//Synthetic comment -- @@ -335,10 +348,10 @@
addXmlFileChanges(mManifestFile, result, true);
}

    private void addLayoutFileChanges(CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = mProject.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 7181f98..2146184 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.resources.ResourceFolderType;
import com.android.utils.SdkUtils;

//Synthetic comment -- @@ -69,6 +71,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
//Synthetic comment -- @@ -166,7 +169,17 @@
// Update layout files; we don't just need to react to custom view
// changes, we need to update fragment references and even tool:context activity
// references
        addLayoutFileChanges(result);

return (result.getChildren().length == 0) ? null : result;
}
//Synthetic comment -- @@ -175,10 +188,10 @@
addXmlFileChanges(mManifestFile, result, true);
}

    private void addLayoutFileChanges(CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = mProject.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 759879a..7843ab3 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.ide.eclipse.adt.internal.refactorings.core;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLASS;
import static com.android.SdkConstants.ATTR_CONTEXT;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.EXT_XML;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.SdkConstants.VIEW_FRAGMENT;
import static com.android.SdkConstants.VIEW_TAG;
//Synthetic comment -- @@ -31,8 +34,14 @@
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.resources.ResourceFolderType;
import com.android.utils.SdkUtils;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -41,10 +50,13 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -54,6 +66,7 @@
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
//Synthetic comment -- @@ -70,6 +83,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
//Synthetic comment -- @@ -88,8 +102,19 @@
private IFile mManifestFile;
private String mOldFqcn;
private String mNewFqcn;
private String mOldDottedName;
private String mNewDottedName;

@Override
public String getName() {
//Synthetic comment -- @@ -104,6 +129,10 @@

@Override
protected boolean initialize(Object element) {
if (element instanceof IType) {
IType type = (IType) element;
IJavaProject javaProject = (IJavaProject) type.getAncestor(IJavaElement.JAVA_PROJECT);
//Synthetic comment -- @@ -119,20 +148,35 @@
mProject.getName()));
return false;
}
mManifestFile = (IFile) manifestResource;
ManifestData manifestData;
manifestData = AndroidManifestHelper.parseForData(mManifestFile);
if (manifestData == null) {
return false;
}
            mOldDottedName = '.' + type.getElementName();
mOldFqcn = type.getFullyQualifiedName();
String packageName = type.getPackageFragment().getElementName();
            mNewDottedName = '.' + getArguments().getNewName();
if (packageName != null) {
mNewFqcn = packageName + mNewDottedName;
} else {
                mNewFqcn = getArguments().getNewName();
}
if (mOldFqcn == null || mNewFqcn == null) {
return false;
//Synthetic comment -- @@ -178,26 +222,118 @@
// Only show the children in the refactoring preview dialog
result.markAsSynthetic();

        addManifestFileChanges(result);
        addLayoutFileChanges(result);

return (result.getChildren().length == 0) ? null : result;
}

    private void addManifestFileChanges(CompositeChange result) {
        addXmlFileChanges(mManifestFile, result, true);
}

    private void addLayoutFileChanges(CompositeChange result) {
try {
// Update references in XML resource files
            IFolder resFolder = mProject.getFolder(SdkConstants.FD_RESOURCES);

IResource[] folders = resFolder.members();
for (IResource folder : folders) {
String folderName = folder.getName();
ResourceFolderType folderType = ResourceFolderType.getFolderType(folderName);
                if (folderType != ResourceFolderType.LAYOUT) {
continue;
}
if (!(folder instanceof IFolder)) {
//Synthetic comment -- @@ -211,7 +347,7 @@
String fileName = member.getName();

if (SdkUtils.endsWith(fileName, DOT_XML)) {
                            addXmlFileChanges(file, result, false);
}
}
}
//Synthetic comment -- @@ -221,7 +357,8 @@
}
}

    private boolean addXmlFileChanges(IFile file, CompositeChange changes, boolean isManifest) {
IModelManager modelManager = StructuredModelManager.getModelManager();
IStructuredModel model = null;
try {
//Synthetic comment -- @@ -236,9 +373,13 @@
Element root = domModel.getDocument().getDocumentElement();
if (root != null) {
List<TextEdit> edits = new ArrayList<TextEdit>();
                        if (isManifest) {
addManifestReplacements(edits, root, document);
} else {
addLayoutReplacements(edits, root, document);
}
if (!edits.isEmpty()) {
//Synthetic comment -- @@ -328,6 +469,28 @@
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
RenameFieldProcessor processor = new RenameFieldProcessor(field);
processor.setRenameGetter(false);
processor.setRenameSetter(false);
RenameRefactoring refactoring = new RenameRefactoring(processor);
        processor.setUpdateReferences(mUpdateReferences);
processor.setUpdateTextualMatches(false);
        processor.setNewElementName(mNewName);
try {
if (refactoring.isApplicable()) {
return refactoring;
//Synthetic comment -- @@ -469,7 +485,7 @@

// Look for the field change on the R.java class; it's a derived file
// and will generate file modified manually warnings. Disable it.
                disableResourceFileChange(fieldChanges);
}
}
}
//Synthetic comment -- @@ -680,6 +696,7 @@
}

String pkg = ManifestInfo.get(project).getPackage();
IType t = javaProject.findType(pkg + '.' + R_CLASS + '.' + type.getName());
if (t == null) {
return null;
//Synthetic comment -- @@ -717,19 +734,18 @@
* Searches for existing changes in the refactoring which modifies the R
* field to rename it. it's derived so performing this change will generate
* a "generated code was modified manually" warning
*/
    private void disableResourceFileChange(Change change) {
// Look for the field change on the R.java class; it's a derived file
// and will generate file modified manually warnings. Disable it.
if (change instanceof CompositeChange) {
for (Change outer : ((CompositeChange) change).getChildren()) {
                if (outer instanceof CompositeChange) {
                    for (Change inner : ((CompositeChange) outer).getChildren()) {
                        if (FN_RESOURCE_CLASS.equals(inner.getName())) {
                            inner.setEnabled(false);
                        }
                    }
                }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java
//Synthetic comment -- index 4ca7837..3b1fa52 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactorings.core;

import static com.android.SdkConstants.ANDROID_PREFIX;
import static com.android.SdkConstants.ANDROID_THEME_PREFIX;
import static com.android.SdkConstants.ATTR_NAME;
//Synthetic comment -- @@ -27,10 +28,20 @@
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
//Synthetic comment -- @@ -38,9 +49,13 @@
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
//Synthetic comment -- @@ -48,13 +63,16 @@
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Text action for XML files to invoke resource renaming
* <p>
* TODO: Handle other types of renaming: invoking class renaming when editing
* class names in layout files and manifest files, renaming attribute names when
* editing a styleable attribute, etc.
*/
public final class RenameResourceXmlTextAction extends Action {
private final ITextEditor mEditor;

//Synthetic comment -- @@ -73,6 +91,14 @@
if (!validateEditorInputState()) {
return;
}
IDocument document = getDocument();
if (document == null) {
return;
//Synthetic comment -- @@ -94,20 +120,41 @@
Shell shell = mEditor.getSite().getShell();
boolean canClear = false;

            IEditorInput input = mEditor.getEditorInput();
            if (input instanceof IFileEditorInput) {
                IFileEditorInput fileInput = (IFileEditorInput) input;
                IProject project = fileInput.getFile().getProject();
                RenameResourceWizard.renameResource(shell, project, type, name, null, canClear);
                return;
}
}

// Fallback: tell user the cursor isn't in the right place
MessageDialog.openInformation(mEditor.getSite().getShell(),
"Rename",
"Operation unavailable on the current selection.\n"
                        + "Select an Android resource name.");
}

private boolean validateEditorInputState() {
//Synthetic comment -- @@ -223,6 +270,110 @@
return null;
}

private ITextSelection getSelection() {
ISelectionProvider selectionProvider = mEditor.getSelectionProvider();
if (selectionProvider == null) {
//Synthetic comment -- @@ -246,4 +397,15 @@
}
return document;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 95ae529..872d18e 100644

//Synthetic comment -- @@ -405,6 +405,7 @@
Collections.reverse(mChanges);
CompositeChange change = new CompositeChange("Refactoring Application package name",
mChanges.toArray(new Change[mChanges.size()]));
return change;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
//Synthetic comment -- index bff6a0b..1df0b0c 100644

//Synthetic comment -- @@ -41,21 +41,21 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
"\n" +
                "* MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "* activity_main.xml - /testRefactor1/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -74,7 +74,7 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'",
false);
}

//Synthetic comment -- @@ -88,33 +88,33 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
"\n" +
                "* MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "* activity_main.xml - /testRefactor2/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor2/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -134,33 +134,33 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'\n" +
"\n" +
                "* MainActivity.java - /testRefactor2_renamesub/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -3 +3\n" +
"  + import com.example.refactoringtest.R;\n" +
"  +\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "* activity_main.xml - /testRefactor2_renamesub/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"my.pkg.name.MyFragment\"/>\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <my.pkg.name.CustomView1\n" +
"\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor2_renamesub/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"my.pkg.name.MainActivity\"\n" +
//Synthetic comment -- @@ -169,13 +169,13 @@
"  +             android:name=\"my.pkg.name.MainActivity2\"\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
"  @@ -15 +15\n" +
"  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
"  +     <my.pkg.name.subpackage.CustomView2\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
"  @@ -15 +15\n" +
"  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
"  +     <my.pkg.name.subpackage.CustomView2",
//Synthetic comment -- @@ -192,7 +192,7 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'",
false);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index 1a97b83..dd0d68d 100644

//Synthetic comment -- @@ -146,7 +146,11 @@

// Describe this change
indent(sb, indent);
            sb.append("* ");
sb.append(changeName);

IFile file = getFile(change);
//Synthetic comment -- @@ -580,6 +584,19 @@
"    }\n" +
"}\n";

protected static final String CUSTOM_VIEW_2 =
"package com.example.refactoringtest.subpackage;\n" +
"\n" +
//Synthetic comment -- @@ -625,6 +642,9 @@
"src/com/example/refactoringtest/CustomView1.java",
CUSTOM_VIEW_1,

"src/com/example/refactoringtest/subpackage/CustomView2.java",
CUSTOM_VIEW_2,









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index b7793bb..ccf6e4f 100644

//Synthetic comment -- @@ -42,19 +42,19 @@

"CHANGES:\n" +
"-------\n" +
                "* strings.xml - /testRefactor1/res/values/strings.xml\n" +
"  @@ -4 +4\n" +
"  -     <string name=\"app_name\">RefactoringTest</string>\n" +
"  +     <string name=\"myname\">RefactoringTest</string>\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor1/gen/com/example/refactoringtest/R.java\n" +
"  @@ -29 +29\n" +
"  -         public static final int app_name=0x7f040000;\n" +
"  +         public static final int myname=0x7f040000;\n" +
"\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -13 +13\n" +
"  -         android:label=\"@string/app_name\"\n" +
"  +         android:label=\"@string/myname\"\n" +
//Synthetic comment -- @@ -72,13 +72,13 @@

"CHANGES:\n" +
"-------\n" +
                "* activity_main.xml - /testRefactor2/res/menu/activity_main.xml\n" +
"  @@ -4 +4\n" +
"  -         android:id=\"@+id/menu_settings\"\n" +
"  +         android:id=\"@+id/new_id_for_the_action_bar\"\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor2/gen/com/example/refactoringtest/R.java\n" +
"  @@ -19 +19\n" +
"  -         public static final int menu_settings=0x7f070003;\n" +
"  +         public static final int new_id_for_the_action_bar=0x7f070003;");
//Synthetic comment -- @@ -93,7 +93,7 @@

"CHANGES:\n" +
"-------\n" +
                "* activity_main.xml - /testRefactor3/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
//Synthetic comment -- @@ -104,13 +104,13 @@
"  +         android:layout_below=\"@+id/output\"\n" +
"\n" +
"\n" +
                "* MainActivity.java - /testRefactor3/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -14 +14\n" +
"  -         View view1 = findViewById(R.id.textView1);\n" +
"  +         View view1 = findViewById(R.id.output);\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor3/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -126,7 +126,7 @@

"CHANGES:\n" +
"-------\n" +
                "* activity_main.xml - /testRefactor4/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
//Synthetic comment -- @@ -137,13 +137,13 @@
"  +         android:layout_below=\"@+id/output\"\n" +
"\n" +
"\n" +
                "* MainActivity.java - /testRefactor4/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -14 +14\n" +
"  -         View view1 = findViewById(R.id.textView1);\n" +
"  +         View view1 = findViewById(R.id.output);\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor4/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -158,21 +158,21 @@

"CHANGES:\n" +
"-------\n" +
                "* MainActivity.java - /testRefactor5/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlayout);\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor5/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlayout=0x7f030000;\n" +
"\n" +
"\n" +
                "* Rename 'testRefactor5/res/layout/activity_main.xml' to 'newlayout.xml'\n" +
"\n" +
                "* Rename 'testRefactor5/res/layout-land/activity_main.xml' to 'newlayout.xml'");
}

public void testRefactor6() throws Exception {
//Synthetic comment -- @@ -184,21 +184,21 @@

"CHANGES:\n" +
"-------\n" +
                "* R.java - /testRefactor6/gen/com/example/refactoringtest/R.java\n" +
"  @@ -14 +14\n" +
"  -         public static final int ic_launcher=0x7f020000;\n" +
"  +         public static final int newlauncher=0x7f020000;\n" +
"\n" +
"\n" +
                "* Rename 'testRefactor6/res/drawable-xhdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "* Rename 'testRefactor6/res/drawable-mdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "* Rename 'testRefactor6/res/drawable-ldpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "* Rename 'testRefactor6/res/drawable-hdpi/ic_launcher.png' to 'newlauncher.png'\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor6/AndroidManifest.xml\n" +
"  @@ -12 +12\n" +
"  -         android:icon=\"@drawable/ic_launcher\"\n" +
"  +         android:icon=\"@drawable/newlauncher\"");
//Synthetic comment -- @@ -216,21 +216,21 @@

"CHANGES:\n" +
"-------\n" +
                "* MainActivity.java - /testRefactor7/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlayout);\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor7/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlayout=0x7f030000;\n" +
"\n" +
"\n" +
                "* Rename 'testRefactor7/res/layout-land/activity_main.xml' to 'newlayout.xml'\n" +
"\n" +
                "* Rename 'testRefactor7/res/layout/activity_main.xml' to 'newlayout.xml'",
null);
}

//Synthetic comment -- @@ -258,17 +258,17 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename 'testRefactor8/res/layout/activity_main.xml' to 'newlauncher.xml'\n" +
"\n" +
                "* Rename 'testRefactor8/res/layout-land/activity_main.xml' to 'newlauncher.xml'\n" +
"\n" +
                "* MainActivity.java - /testRefactor8/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
"  +         setContentView(R.layout.newlauncher);\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor8/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
"  +         public static final int newlauncher=0x7f030000;",
//Synthetic comment -- @@ -302,13 +302,13 @@

"CHANGES:\n" +
"-------\n" +
                "* activity_main.xml - /testRefactor9/res/layout/activity_main.xml\n" +
"  @@ -8 +8\n" +
"  -         android:id=\"@+id/textView1\"\n" +
"  +         android:id=\"@+id/output\"\n" +
"\n" +
"\n" +
                "* R.java - /testRefactor9/gen/com/example/refactoringtest/R.java\n" +
"  @@ -20 +20\n" +
"  -         public static final int textView1=0x7f070000;\n" +
"  +         public static final int output=0x7f070000;");
//Synthetic comment -- @@ -324,7 +324,7 @@

"CHANGES:\n" +
"-------\n" +
                "* activity_main.xml - /testRefactor10/res/layout-land/activity_main.xml\n" +
"  @@ -10 +10\n" +
"  -         tools:listitem=\"@layout/preview\" >\n" +
"  +         tools:listitem=\"@layout/newlayout\" >\n" +








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java
//Synthetic comment -- index 742aa31..438906d 100644

//Synthetic comment -- @@ -32,25 +32,23 @@

"CHANGES:\n" +
"-------\n" +
                "* Refactoring Application package name\n" +
                "\n" +
                "  * MainActivity2.java - /testRefactor1/src/com/example/refactoringtest/MainActivity2.java\n" +
                "    @@ -7 +7\n" +
                "    + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "  * MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
                "    @@ -7 +7\n" +
                "    + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "  * Make Manifest edits - /testRefactor1/AndroidManifest.xml\n" +
                "    @@ -3 +3\n" +
                "    -     package=\"com.example.refactoringtest\"\n" +
                "    +     package=\"my.pkg.name\"\n" +
                "    @@ -25 +25\n" +
                "    -             android:name=\".MainActivity2\"\n" +
                "    +             android:name=\"com.example.refactoringtest.MainActivity2\"");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -61,30 +59,28 @@

"CHANGES:\n" +
"-------\n" +
                "* Refactoring Application package name\n" +
                "\n" +
                "  * MyFragment.java - /testRefactor2/src/com/example/refactoringtest/MyFragment.java\n" +
                "    @@ -3 +3\n" +
                "    + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "  * MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
                "    @@ -7 +7\n" +
                "    + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "  * CustomView1.java - /testRefactor2/src/com/example/refactoringtest/CustomView1.java\n" +
                "    @@ -5 +5\n" +
                "    + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "  * Make Manifest edits - /testRefactor2/AndroidManifest.xml\n" +
                "    @@ -3 +3\n" +
                "    -     package=\"com.example.refactoringtest\"\n" +
                "    +     package=\"my.pkg.name\"\n" +
                "    @@ -25 +25\n" +
                "    -             android:name=\".MainActivity2\"\n" +
                "    +             android:name=\"com.example.refactoringtest.MainActivity2\"");
}

// ---- Test infrastructure ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java
//Synthetic comment -- index 0fb8523..262ea42 100644

//Synthetic comment -- @@ -46,17 +46,17 @@

"CHANGES:\n" +
"-------\n" +
                "* Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "* Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "* customviews.xml - /testRefactor1/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.subpackage.CustomView1\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor1/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.subpackage.CustomView1");
//Synthetic comment -- @@ -71,11 +71,11 @@

"CHANGES:\n" +
"-------\n" +
                "* Move resource 'testRefactorFragment/src/com/example/refactoringtest/MyFragment.java' to 'subpackage'\n" +
"\n" +
                "* Move resource 'testRefactorFragment/src/com/example/refactoringtest/MyFragment.java' to 'subpackage'\n" +
"\n" +
                "* activity_main.xml - /testRefactorFragment/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"com.example.refactoringtest.subpackage.MyFragment\"/>");
//Synthetic comment -- @@ -90,9 +90,9 @@

"CHANGES:\n" +
"-------\n" +
                "* Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
"\n" +
                "* Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'");
}

// ---- Test infrastructure ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java
//Synthetic comment -- index f54a49a..f65124a 100644

//Synthetic comment -- @@ -36,21 +36,21 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'\n" +
"\n" +
                "* activity_main.xml - /testRefactor1/res/layout/activity_main.xml\n" +
"  @@ -5 +5\n" +
"  -     tools:context=\".MainActivity\" >\n" +
"  +     tools:context=\".NewActivityName\" >\n" +
"\n" +
"\n" +
                "* activity_main.xml - /testRefactor1/res/layout-land/activity_main.xml\n" +
"  @@ -5 +5\n" +
"  -     tools:context=\".MainActivity\" >\n" +
"  +     tools:context=\".NewActivityName\" >\n" +
"\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
"  @@ -16 +16\n" +
"  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
"  +             android:name=\"com.example.refactoringtest.NewActivityName\"");
//Synthetic comment -- @@ -65,9 +65,9 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename compilation unit 'MainActivity2.java' to 'NewActivityName.java'\n" +
"\n" +
                "* AndroidManifest.xml - /testRefactor1b/AndroidManifest.xml\n" +
"  @@ -25 +25\n" +
"  -             android:name=\".MainActivity2\"\n" +
"  +             android:name=\".NewActivityName\"");
//Synthetic comment -- @@ -82,7 +82,7 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -94,15 +94,21 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename compilation unit 'CustomView1.java' to 'NewCustomViewName.java'\n" +
"\n" +
                "* customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.NewCustomViewName\n" +
"\n" +
"\n" +
                "* customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
"  @@ -9 +9\n" +
"  -     <com.example.refactoringtest.CustomView1\n" +
"  +     <com.example.refactoringtest.NewCustomViewName");
//Synthetic comment -- @@ -117,9 +123,9 @@

"CHANGES:\n" +
"-------\n" +
            "* Rename compilation unit 'MyFragment.java' to 'NewFragmentName.java'\n" +
"\n" +
            "* activity_main.xml - /testRefactorFragment/res/layout/activity_main.xml\n" +
"  @@ -33 +33\n" +
"  -     <fragment android:name=\"com.example.refactoringtest.MyFragment\"/>\n" +
"  +     <fragment android:name=\"com.example.refactoringtest.NewFragmentName\"/>");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextActionTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextActionTest.java
//Synthetic comment -- index 047168d..11fc81a 100644

//Synthetic comment -- @@ -52,6 +52,43 @@
checkWord("\n^?foo\"", Pair.of(ResourceType.ATTR, "foo"));
}

private void checkWord(String contents, Pair<ResourceType, String> expectedResource)
throws Exception {
int cursor = contents.indexOf('^');







