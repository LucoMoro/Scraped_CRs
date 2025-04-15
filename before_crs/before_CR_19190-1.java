/*Fixing Issue 2021: ADT Layout Editor can't handle TabWidget

	modified:   eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java

Change-Id:I88bda11892797804d7e08a428e096a65e784cd30*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 22a1a88..127592c 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -1419,10 +1420,12 @@
renderingMode = RenderingMode.V_SCROLL;
}
}

SceneParams params = new SceneParams(
topParser,
                iProject /* projectKey */,
width, height,
renderingMode,
density, xdpi, ydpi,







