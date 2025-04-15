/*Misc fixes

This changeset fixes a couple of misc problems I ran into:

(1) Save the current file when double clicking on an included view.
    When double clicking on an included view, the included view is
    opened in a "Show Included" context where the including file is
    surrounding the include. This will read the surrounding file from
    disk, and if we don't save the file when opening the file then
    it's possible to see a stale view (best case) or it won't work at
    all if the include tag itself isn't in the saved version of the
    file.

(2) Fix a bug in the "Select Same Type" context menu action where the
    list was cleared before reading out the item to be selected.

(3) Filter out a few more properties from the Extract Style
    refactoring dialog, and include margin attributes

Change-Id:I207cc5b736aacd3f067d6d36727ffcd7104a87c1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e7f8b84..e154900 100755

//Synthetic comment -- @@ -886,10 +886,21 @@
private void showInclude(String url) {
GraphicalEditorPart graphicalEditor = mLayoutEditor.getGraphicalEditor();
IPath filePath = graphicalEditor.findResourceFile(url);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
        IEditorSite editorSite = graphicalEditor.getEditorSite();
if (workspacePath.isPrefixOf(filePath)) {
IPath relativePath = filePath.makeRelativeTo(workspacePath);
IResource xmlFile = workspace.findMember(relativePath);
//Synthetic comment -- @@ -944,7 +955,6 @@
IFileStore fileStore = EFS.getLocalFileSystem().getStore(filePath);
// fileStore = fileStore.getChild(names[i]);
if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
                    IWorkbenchPage page = editorSite.getWorkbenchWindow().getActivePage();
try {
IDE.openEditorOnFileStore(page, fileStore);
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 3fdbe5a..c5a840b 100644

//Synthetic comment -- @@ -594,10 +594,11 @@
public void selectSameType() {
// Find all
if (mSelections.size() == 1) {
mSelections.clear();
mAltSelection = null;
            addSameType(mCanvas.getViewHierarchy().getRoot(),
                    mSelections.get(0).getViewInfo().getUiViewNode().getDescriptor());
fireSelectionChanged();
redraw();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java
//Synthetic comment -- index 9e8114a..d8dfe86 100644

//Synthetic comment -- @@ -21,7 +21,9 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HINT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.ide.common.layout.LayoutConstants.ATTR_STYLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
//Synthetic comment -- @@ -247,8 +249,12 @@

String name = attribute.getLocalName();
if (name == null || name.equals(ATTR_ID) || name.startsWith(ATTR_STYLE)
                        || name.startsWith(ATTR_LAYOUT_PREFIX) || name.equals(ATTR_TEXT)
                        || name.equals(ATTR_HINT) || name.equals(ATTR_SRC)) {
// Don't offer to extract attributes that don't make sense in
// styles (like "id" or "style"), or attributes that the user
// probably does not want to define in styles (like layout







