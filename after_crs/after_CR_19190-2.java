/*Fixing Issue 2021: ADT Layout Editor can't handle TabWidget

This change depends onhttps://review.source.android.com/#change,19189The problem is resolved in the following way :
- GraphicalEditorPart.renderWithBridge computes apiLevel based on project and add it as params to Bridge (GraphicalEditorPart.java, line 1441 "LayoutScene scene = layoutLib.getBridge().createScene(params);"
It used projectKey, this parameter is unused in the BridgeContext
- BridgeContext use projectKey to create usable ApplicationInfo

Change-Id:I88bda11892797804d7e08a428e096a65e784cd30*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 22a1a88..127592c 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -1419,10 +1420,12 @@
renderingMode = RenderingMode.V_SCROLL;
}
}
        ProjectState state = Sdk.getProjectState(iProject);
        IAndroidTarget target = state.getTarget();
        int apiLevel = target.getVersion().getApiLevel();
SceneParams params = new SceneParams(
topParser,
                apiLevel /* projectKey */,
width, height,
renderingMode,
density, xdpi, ydpi,







