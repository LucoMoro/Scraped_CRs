/*ADT: remove obsolete GEF-related method.

Remove the GEF selection synchronizer which was
necessary only for the obsolete GLE1.

Change-Id:I9ca4b1ac125e071fb3e19b69879df880c0b4be2c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
//Synthetic comment -- index c8fd5c8..73d856d 100755

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;

/**
//Synthetic comment -- @@ -73,12 +72,6 @@
*/
abstract void deactivated();

abstract void reloadPalette();

abstract void recomputeLayout();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 21d8272..2a78deb 100755

//Synthetic comment -- @@ -64,7 +64,6 @@
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -928,11 +927,6 @@
return mLayoutEditor.getUiRootNode();
}

/**
* Callback for XML model changed. Only update/recompute the layout if the editor is visible
*/







