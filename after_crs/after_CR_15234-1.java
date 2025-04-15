/*ADT: Refactor AndroidEditor into AndroidXmlEditor.

Next we'll introduce a new AndroidTextEditor base class.

Change-Id:I2cdf4c7cb7a2eec03f7c523294a14f98bfd072f8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/RenamePackageAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/RenamePackageAction.java
//Synthetic comment -- index 7e5cc8f..e8f8ed0 100644

//Synthetic comment -- @@ -531,7 +531,7 @@
// count must be 4.
// We don't need to check the type folder name because
// a/ we only accept
                        // an AndroidXmlEditor source and b/ aapt generates a
// compilation error for
// unknown folders.
IPath path = file.getFullPath();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 75d33b3..9324cc4 100644

//Synthetic comment -- @@ -89,7 +89,7 @@

private final int mDescriptorId;

    private AndroidXmlEditor mEditor;

/**
* Constructor for AndroidContentAssist
//Synthetic comment -- @@ -118,7 +118,7 @@
public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

if (mEditor == null) {
            mEditor = getAndroidXmlEditor(viewer);
if (mEditor == null) {
// This should not happen. Duck and forget.
AdtPlugin.log(IStatus.ERROR, "Editor not found during completion");
//Synthetic comment -- @@ -782,18 +782,18 @@
}

/**
     * Returns the active {@link AndroidXmlEditor} matching this source viewer.
*/
    private AndroidXmlEditor getAndroidXmlEditor(ITextViewer viewer) {
IWorkbenchWindow wwin = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
if (wwin != null) {
IWorkbenchPage page = wwin.getActivePage();
if (page != null) {
IEditorPart editor = page.getActiveEditor();
                if (editor instanceof AndroidXmlEditor) {
                    ISourceViewer ssviewer = ((AndroidXmlEditor) editor).getStructuredSourceViewer();
if (ssviewer == viewer) {
                        return (AndroidXmlEditor) editor;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
similarity index 96%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidEditor.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index c2b1b28..fd2ee77 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
* Derived classes must implement createFormPages to create the forms before the
* source editor. This can be a no-op if desired.
*/
public abstract class AndroidXmlEditor extends FormEditor implements IResourceChangeListener {

/** Preference name for the current page of this file */
private static final String PREF_CURRENT_PAGE = "_current_page";
//Synthetic comment -- @@ -114,9 +114,9 @@
* <p/>The editor will setup a {@link ITargetChangeListener} and call
* {@link #initUiRootNode(boolean)}, when the SDK or the target changes.
*
     * @see #AndroidXmlEditor(boolean)
*/
    public AndroidXmlEditor() {
this(true);
}

//Synthetic comment -- @@ -124,7 +124,7 @@
* Creates a form editor.
* @param addTargetListener whether to create an {@link ITargetChangeListener}.
*/
    public AndroidXmlEditor(boolean addTargetListener) {
super();

if (addTargetListener) {
//Synthetic comment -- @@ -133,7 +133,7 @@
mTargetListener = new TargetChangeListener() {
@Override
public IProject getProject() {
                    return AndroidXmlEditor.this.getProject();
}

@Override
//Synthetic comment -- @@ -164,7 +164,7 @@
abstract protected void createFormPages();

/**
     * Called by the base class {@link AndroidXmlEditor} once all pages (custom form pages
* as well as text editor page) have been created. This give a chance to deriving
* classes to adjust behavior once the text page has been created.
*/
//Synthetic comment -- @@ -270,7 +270,7 @@
// AssertionError from setActivePage when the index is out of bounds.
// Generally speaking we just want to ignore any exception and fall back on the
// first page rather than crash the editor load. Logging the error is enough.
                AdtPlugin.log(e, "Selecting page '%s' in AndroidXmlEditor failed", defaultPageId);
}
}
}
//Synthetic comment -- @@ -821,7 +821,7 @@
* model's ID or base Location. A typical use might be if a client might
* want to suspend processing until all changes have been made.
* <p/>
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelAboutToBeChanged(IStructuredModel model) {
// pass
//Synthetic comment -- @@ -832,7 +832,7 @@
* made. A typical use might be to refresh, or to resume processing that
* was suspended as a result of modelAboutToBeChanged.
* <p/>
         * This AndroidXmlEditor implementation calls the xmlModelChanged callback.
*/
public void modelChanged(IStructuredModel model) {
xmlModelChanged(getXmlDocument(model));
//Synthetic comment -- @@ -843,7 +843,7 @@
* in isDirty. A model becomes dirty when any change is made, and becomes
* not-dirty when the model is saved.
* <p/>
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
// pass
//Synthetic comment -- @@ -855,7 +855,7 @@
* released it. Note: baseLocation is not (necessarily) changed in this
* event, but may not be accurate.
* <p/>
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelResourceDeleted(IStructuredModel model) {
// pass
//Synthetic comment -- @@ -866,21 +866,21 @@
* case, the two paramenters are the same instance, and only contain the
* new info for id and base location.
* <p/>
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
// pass
}

/**
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
// pass
}

/**
         * This AndroidXmlEditor implementation of IModelChangedListener is empty.
*/
public void modelReinitialized(IStructuredModel structuredModel) {
// pass








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 1b8dd6e..63246bd 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IUnknownDescriptorProvider;
//Synthetic comment -- @@ -60,7 +60,7 @@
/**
* Multi-page form editor for /res/layout XML files.
*/
public class LayoutEditor extends AndroidXmlEditor implements IShowEditorInput, IPartListener {

public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".layout.LayoutEditor"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index a95ce9e..d043664 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
//Synthetic comment -- @@ -1269,7 +1269,7 @@
}

/** Get the XML text directly from the editor. */
        private String getXmlTextFromEditor(AndroidXmlEditor editor, Node xml_node) {
String data = null;
IStructuredModel model = editor.getModelForRead();
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index a2bfd92..5f1b889 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
//Synthetic comment -- @@ -162,7 +162,7 @@
// ---- XML Editing ---

public void editXml(String undoName, final Closure c) {
        final AndroidXmlEditor editor = mNode.getEditor();

if (editor.isEditXmlModelPending()) {
throw new RuntimeException("Error: calls to INode.editXml cannot be nested!");
//Synthetic comment -- @@ -190,7 +190,7 @@
}

private void checkEditOK() {
        final AndroidXmlEditor editor = mNode.getEditor();
if (!editor.isEditXmlModelPending()) {
throw new RuntimeException("Error: XML edit call without using INode.editXml!");
}
//Synthetic comment -- @@ -327,7 +327,7 @@
* isn't reloading, or we wouldn't be here editing XML for a groovy script.)
*/
private ViewElementDescriptor getFqcnViewDescritor(String fqcn) {
        AndroidXmlEditor editor = mNode.getEditor();
if (editor != null) {
AndroidTargetData data = editor.getTargetData();
if (data != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/ElementCreateCommand.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/ElementCreateCommand.java
//Synthetic comment -- index 562570f..59f2169 100644

//Synthetic comment -- @@ -17,7 +17,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.parts;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor.UiEditorActions;
//Synthetic comment -- @@ -70,7 +70,7 @@
super.execute();
UiElementNode uiParent = mParentPart.getUiNode();
if (uiParent != null) {
            final AndroidXmlEditor editor = uiParent.getEditor();
if (editor instanceof LayoutEditor) {
((LayoutEditor) editor).wrapUndoRecording(
String.format("Create %1$s", mDescriptor.getXmlLocalName()),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index fe42dea..38e2527 100644

//Synthetic comment -- @@ -74,7 +74,9 @@
IAndroidTarget target = currentSdk.getTarget(project);
if (target != null) {
AndroidTargetData data = currentSdk.getTargetData(target);
                        if (data != null) {
                            layoutDescriptors = data.getLayoutDescriptors().getLayoutDescriptors();
                        }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index c4a42f8..edf0103 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.pages.ApplicationPage;
//Synthetic comment -- @@ -54,7 +54,7 @@
/**
* Multi-page form editor for AndroidManifest.xml.
*/
public final class ManifestEditor extends AndroidXmlEditor {

public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".manifest.ManifestEditor"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiClassAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiClassAttributeNode.java
//Synthetic comment -- index 522f28a..348e0a3 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -431,7 +431,7 @@

private IProject getProject() {
UiElementNode uiNode = getUiParent();
        AndroidXmlEditor editor = uiNode.getEditor();
IEditorInput input = editor.getEditorInput();
if (input instanceof IFileEditorInput) {
// from the file editor we can get the IFile object, and from it, the IProject.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiPackageAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiPackageAttributeNode.java
//Synthetic comment -- index d345d32..c7a5e28 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.model;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
//Synthetic comment -- @@ -242,7 +242,7 @@
*/
private IProject getProject() {
UiElementNode uiNode = getUiParent();
        AndroidXmlEditor editor = uiNode.getEditor();
IEditorInput input = editor.getEditorInput();
if (input instanceof IFileEditorInput) {
// from the file editor we can get the IFile object, and from it, the IProject.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java
//Synthetic comment -- index 33a5d3d..2e257c9 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -39,7 +39,7 @@
/**
* Multi-page form editor for /res/menu XML files. 
*/
public class MenuEditor extends AndroidXmlEditor {

public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".menu.MenuEditor"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/ResourcesEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/ResourcesEditor.java
//Synthetic comment -- index a48b712..bf22ee5 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -40,7 +40,7 @@
/**
* Multi-page form editor for /res/values and /res/drawable XML files. 
*/
public class ResourcesEditor extends AndroidXmlEditor {

public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".resources.ResourcesEditor"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/SectionHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/SectionHelper.java
//Synthetic comment -- index 69c21e6..ac4cd03 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.swt.events.MouseEvent;
//Synthetic comment -- @@ -335,7 +335,7 @@
FormText text = toolkit.createFormText(parent, true /* track focus */);
if (setupLayoutData) {
TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
            twd.maxWidth = AndroidXmlEditor.TEXT_WIDTH_HINT;
if (parent.getLayout() instanceof TableWrapLayout) {
twd.colspan = ((TableWrapLayout) parent.getLayout()).numColumns;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java
//Synthetic comment -- index 9eebfe7..ba4fd5d 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui.tree;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.apache.xml.serialize.Method;
//Synthetic comment -- @@ -49,7 +49,7 @@
public class CopyCutAction extends Action {
private List<UiElementNode> mUiNodes;
private boolean mPerformCut;
    private final AndroidXmlEditor mEditor;
private final Clipboard mClipboard;
private final ICommitXml mXmlCommit;

//Synthetic comment -- @@ -59,7 +59,7 @@
* @param selected The UI node to cut or copy. It *must* have a non-null XML node.
* @param performCut True if the operation is cut, false if it is copy.
*/
    public CopyCutAction(AndroidXmlEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
UiElementNode selected, boolean performCut) {
this(editor, clipboard, xmlCommit, toList(selected), performCut);
}
//Synthetic comment -- @@ -71,7 +71,7 @@
*                 The list becomes owned by the {@link CopyCutAction}.
* @param performCut True if the operation is cut, false if it is copy.
*/
    public CopyCutAction(AndroidXmlEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
List<UiElementNode> selected, boolean performCut) {
super(performCut ? "Cut" : "Copy");
mEditor = editor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/NewItemSelectionDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/NewItemSelectionDialog.java
//Synthetic comment -- index dc53292..287f5a6 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui.tree;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -153,7 +153,7 @@
*/
private String getLeafFileName(UiElementNode ui_node) {
if (ui_node != null) {
            AndroidXmlEditor editor = ui_node.getEditor();
if (editor != null) {
IEditorInput editorInput = editor.getEditorInput();
if (editorInput instanceof FileEditorInput) {
//Synthetic comment -- @@ -177,7 +177,7 @@
*/
private String getLastUsedXmlName(UiElementNode ui_node) {
if (ui_node != null) {
            AndroidXmlEditor editor = ui_node.getEditor();
if (editor != null) {
IEditorInput editorInput = editor.getEditorInput();
if (editorInput instanceof FileEditorInput) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java
//Synthetic comment -- index 4de52c4..d649af7 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui.tree;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

//Synthetic comment -- @@ -40,10 +40,10 @@
*/
public class PasteAction extends Action {
private UiElementNode mUiNode;
    private final AndroidXmlEditor mEditor;
private final Clipboard mClipboard;

    public PasteAction(AndroidXmlEditor editor, Clipboard clipboard, UiElementNode ui_node) {
super("Paste");
mEditor = editor;
mClipboard = clipboard;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java
//Synthetic comment -- index a8c3870..0e160b9 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui.tree;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -377,7 +377,7 @@
if (parentNumCol > 1) {
masterTable = SectionHelper.createTableLayout(masterTable, toolkit, 1);
TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
            twd.maxWidth = AndroidXmlEditor.TEXT_WIDTH_HINT;
twd.colspan = parentNumCol;
masterTable.setLayoutData(twd);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java
//Synthetic comment -- index ca8d9f3..589429c 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.ui.tree;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
//Synthetic comment -- @@ -90,7 +90,7 @@
private static final int TREE_HEIGHT_HINT = 50;

/** Container editor */
    AndroidXmlEditor mEditor;
/** The root {@link UiElementNode} which contains all the elements that are to be
*  manipulated by this tree view. In general this is the manifest UI node. */
private UiElementNode mUiRootNode;
//Synthetic comment -- @@ -153,7 +153,7 @@
* @param title Title for the section
* @param description Description for the section
*/
    public UiTreeBlock(AndroidXmlEditor editor,
UiElementNode uiRootNode,
boolean autoCreateRoot,
ElementDescriptor[] descriptorFilters,
//Synthetic comment -- @@ -168,7 +168,7 @@
}

/** @returns The container editor */
    AndroidXmlEditor getEditor() {
return mEditor;
}

//Synthetic comment -- @@ -246,7 +246,7 @@
// However the class must be adapted to create an adapted toolkit tree.
final Tree tree = toolkit.createTree(grid, SWT.MULTI);
GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = AndroidXmlEditor.TEXT_WIDTH_HINT;
gd.heightHint = TREE_HEIGHT_HINT;
tree.setLayoutData(gd);

//Synthetic comment -- @@ -336,12 +336,16 @@
// Remove listeners when the tree widget gets disposed.
tree.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
                if (mUiRootNode != null) {
                    UiElementNode node = mUiRootNode.getUiParent() != null ?
                                            mUiRootNode.getUiParent() :
                                            mUiRootNode;

                    if (node != null) {
                        node.removeUpdateListener(mUiRefreshListener);
                    }
                    mUiRootNode.removeUpdateListener(mUiEnableListener);
                }

AdtPlugin.getDefault().removeTargetListener(targetListener);
if (mClipboard != null) {
//Synthetic comment -- @@ -384,18 +388,24 @@
mUiRootNode = uiRootNode;
mDescriptorFilters = descriptorFilters;

        mTreeViewer.setContentProvider(
                new UiModelTreeContentProvider(mUiRootNode, mDescriptorFilters));

// Listen on structural changes on the root node of the tree
// If the node has a parent, listen on the parent instead.
        if (mUiRootNode != null) {
            node = mUiRootNode.getUiParent() != null ? mUiRootNode.getUiParent() : mUiRootNode;

            if (node != null) {
                node.addUpdateListener(mUiRefreshListener);
            }

            // Use the root node to listen to its presence.
            mUiRootNode.addUpdateListener(mUiEnableListener);

            // Initialize the enabled/disabled state
            mUiEnableListener.uiElementNodeUpdated(mUiRootNode, null /* state, not used */);
        }

if (forceRefresh) {
mTreeViewer.refresh();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index e874870..9cce764 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IUnknownDescriptorProvider;
//Synthetic comment -- @@ -84,9 +84,9 @@
/** The parent element node in the UI model. It is null for a root element or until
*  the node is attached to its parent. */
private UiElementNode mUiParent;
    /** The {@link AndroidXmlEditor} handling the UI hierarchy. This is defined only for the
*  root node. All children have the value set to null and query their parent. */
    private AndroidXmlEditor mEditor;
/** The XML {@link Document} model that is being mirror by the UI model. This is defined
*  only for the root node. All children have the value set to null and query their parent. */
private Document mXmlDocument;
//Synthetic comment -- @@ -412,11 +412,11 @@
}

/**
     * Sets the {@link AndroidXmlEditor} handling this {@link UiElementNode} hierarchy.
* <p/>
* The editor must always be set on the root node. This method takes care of that.
*/
    public void setEditor(AndroidXmlEditor editor) {
if (mUiParent == null) {
mEditor = editor;
} else {
//Synthetic comment -- @@ -425,14 +425,14 @@
}

/**
     * Returns the {@link AndroidXmlEditor} that embeds this {@link UiElementNode}.
* <p/>
* The value is initially null until the node is attached to its parent -- the value
* of the root node is then propagated.
*
     * @return The embedding {@link AndroidXmlEditor} or null.
*/
    public AndroidXmlEditor getEditor() {
return mUiParent == null ? mEditor : mUiParent.getEditor();
}

//Synthetic comment -- @@ -690,7 +690,7 @@
*/
public void reloadFromXmlNode(Node xml_node) {
// The editor needs to be preserved, it is not affected by an XML change.
        AndroidXmlEditor editor = getEditor();
clearContent();
setEditor(editor);
if (xml_node != null) {
//Synthetic comment -- @@ -1224,7 +1224,7 @@
* Note that the caller MUST ensure that modifying the underlying XML model is
* safe and must take care of marking the model as dirty if necessary.
*
     * @see AndroidXmlEditor#editXmlModel(Runnable)
*
* @param uiAttr The attribute node to commit. Must be a child of this UiElementNode.
* @param newValue The new value to set.
//Synthetic comment -- @@ -1270,7 +1270,7 @@
* Note that the caller MUST ensure that modifying the underlying XML model is
* safe and must take care of marking the model as dirty if necessary.
*
     * @see AndroidXmlEditor#editXmlModel(Runnable)
*
* @return True if one or more values were actually modified or removed,
*         false if nothing changed.
//Synthetic comment -- @@ -1629,7 +1629,7 @@
}

final UiAttributeNode fAttribute = attribute;
            AndroidXmlEditor editor = getEditor();
editor.editXmlModel(new Runnable() {
public void run() {
commitAttributeToXml(fAttribute, newValue);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiFlagAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiFlagAttributeNode.java
//Synthetic comment -- index eb9903f..6cf2194 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
//Synthetic comment -- @@ -143,7 +143,7 @@
if (values == null) {
// or from the AndroidTargetData
UiElementNode uiNode = getUiParent();
            AndroidXmlEditor editor = uiNode.getEditor();
AndroidTargetData data = editor.getTargetData();
if (data != null) {
values = data.getAttributeValues(element_name, attr_name);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiListAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiListAttributeNode.java
//Synthetic comment -- index ee2916c..5747450 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
//Synthetic comment -- @@ -156,7 +156,7 @@
if (values == null) {
// or from the AndroidTargetData
UiElementNode uiNode = getUiParent();
            AndroidXmlEditor editor = uiNode.getEditor();
AndroidTargetData data = editor.getTargetData();
if (data != null) {
// get the great-grand-parent descriptor.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 8588a05..87c55d0 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
//Synthetic comment -- @@ -122,7 +122,7 @@
public String showDialog(Shell shell, String currentValue) {
// we need to get the project of the file being edited.
UiElementNode uiNode = getUiParent();
        AndroidXmlEditor editor = uiNode.getEditor();
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
//Synthetic comment -- @@ -198,7 +198,7 @@
boolean isSystem = false;

UiElementNode uiNode = getUiParent();
        AndroidXmlEditor editor = uiNode.getEditor();

if (prefix == null || prefix.indexOf("android:") < 0) {                 //$NON-NLS-1$
IProject project = editor.getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiTextAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiTextAttributeNode.java
//Synthetic comment -- index 40496ab..fe0f858 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
//Synthetic comment -- @@ -133,7 +133,7 @@
Object data = textWidget.getLayoutData();
if (data == null) {
} else if (data instanceof GridData) {
                ((GridData)data).widthHint = AndroidXmlEditor.TEXT_WIDTH_HINT;
} else if (data instanceof TableWrapData) {
((TableWrapData)data).maxWidth = 100;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java
//Synthetic comment -- index aca6c8f..177cb14 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.FirstElementParser;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -39,7 +39,7 @@
/**
* Multi-page form editor for /res/xml XML files.
*/
public class XmlEditor extends AndroidXmlEditor {

public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".xml.XmlEditor"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index cbffd34..e4a7856 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -369,7 +369,7 @@
//    project/res/<type>[-<configuration>]/*.xml
// There is no support for sub folders, so the segment count must be 4.
// We don't need to check the type folder name because a/ we only accept
                // an AndroidXmlEditor source and b/ aapt generates a compilation error for
// unknown folders.
IPath path = mFile.getFullPath();
// check if we are inside the project/res/* folder.
//Synthetic comment -- @@ -478,18 +478,18 @@
IProgressMonitor monitor) {

try {
            if (!(mEditor instanceof AndroidXmlEditor)) {
status.addFatalError("Only the Android XML Editor is currently supported.");
return status.isOK();
}

            AndroidXmlEditor editor = (AndroidXmlEditor) mEditor;
IStructuredModel smodel = null;
Node node = null;
String currAttrName = null;

try {
                // See the portability note in AndroidXmlEditor#getModelForRead() javadoc.
smodel = editor.getModelForRead();
if (smodel != null) {
// The structured model gives the us the actual XML Node element where the
//Synthetic comment -- @@ -654,7 +654,7 @@
* This sets mTokenString to null by side-effect when it fails and
* adds a fatal error to the status as needed.
*/
    private void validateSelectedAttribute(AndroidXmlEditor editor, Node node,
String attrName, RefactoringStatus status) {
UiElementNode rootUiNode = editor.getUiRootNode();
UiElementNode currentUiNode =







