/*Replace reflection with Sdk.makeRelativeTo

Replace reflection-based invocation of IPath.makeRelativeTo
(which is not available in Eclipse 3.4) with usage of
Sdk.makeRelativeTo, our own copy of the equivalent code.

Change-Id:If67d985b96b845d85cf7fc13d47e9adeee49e8a8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index ff5d3c4..430fd68 100755

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.LayoutScene.IAnimationListener;
//Synthetic comment -- @@ -86,8 +87,6 @@
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -726,31 +725,7 @@
IPath workspacePath = workspace.getLocation();
IEditorSite editorSite = graphicalEditor.getEditorSite();
if (workspacePath.isPrefixOf(filePath)) {
            IPath relativePath = Sdk.makeRelativeTo(filePath, workspacePath);
IResource xmlFile = workspace.findMember(relativePath);
try {
EditorUtility.openInEditor(xmlFile, true);







