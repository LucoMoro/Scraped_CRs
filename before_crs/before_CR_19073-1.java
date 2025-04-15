/*Replace IPath.makeRelativeTo call with reflection

I broke the build because on Eclipse 3.4 there is no makeRelativeTo
method on IPath. This replaces that call with reflection. This won't
work on 3.4, but it's a quick fix for the broken build.

Change-Id:Ia917cf5a745f76bbb96f59fae93e2a4fc4f5f900*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index c0e69b1..e5113f8 100755

//Synthetic comment -- @@ -83,6 +83,8 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -710,7 +712,31 @@
IPath workspacePath = workspace.getLocation();
IEditorSite editorSite = graphicalEditor.getEditorSite();
if (workspacePath.isPrefixOf(filePath)) {
            IPath relativePath = filePath.makeRelativeTo(workspacePath);
IResource xmlFile = workspace.findMember(relativePath);
try {
EditorUtility.openInEditor(xmlFile, true);







