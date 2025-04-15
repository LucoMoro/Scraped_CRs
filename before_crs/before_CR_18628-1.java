/*Select element surrounding caret when switching from editor

If you edit the XML, and then switch back to the visual editor, this
changeset will cause the element surrounding the caret (if any) to be
selected in the visual editor.

Change-Id:I1f03856b3b3946fe23d6e654773ee4318d0d56ed*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 5a749b1..a4a133e 100644

//Synthetic comment -- @@ -48,9 +48,6 @@
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -743,23 +740,8 @@
/**
* Returns the XML DOM node corresponding to the given offset of the given document.
*/
    protected Node getNode(ITextViewer viewer, int offset) {
        Node node = null;
        try {
            IModelManager mm = StructuredModelManager.getModelManager();
            if (mm != null) {
                IStructuredModel model = mm.getExistingModelForRead(viewer.getDocument());
                if (model != null) {
                    for(; offset >= 0 && node == null; --offset) {
                        node = (Node) model.getIndexedRegion(offset);
                    }
                }
            }
        } catch (Exception e) {
            // Ignore exceptions.
        }

        return node;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index a1b9d13..117138e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
//Synthetic comment -- @@ -95,7 +96,7 @@
public static final int TEXT_WIDTH_HINT = 50;

/** Page index of the text editor (always the last page) */
    private int mTextPageIndex;
/** The text editor */
private StructuredTextEditor mTextEditor;
/** Listener for the XML model from the StructuredEditor */
//Synthetic comment -- @@ -621,6 +622,39 @@
}

/**
* Returns a version of the model that has been shared for read.
* <p/>
* Callers <em>must</em> call model.releaseFromRead() when done, typically








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index ff176de..3875896 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
//Synthetic comment -- @@ -51,6 +52,7 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -307,6 +309,21 @@

@Override
protected void pageChange(int newPageIndex) {
super.pageChange(newPageIndex);

if (mGraphicalEditor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f9b916f..253ceed 100755

//Synthetic comment -- @@ -98,6 +98,7 @@
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import java.awt.image.BufferedImage;
import java.io.File;
//Synthetic comment -- @@ -457,6 +458,14 @@
}

/**
* Listens to changes from the Configuration UI banner and triggers layout rendering when
* changed. Also provide the Configuration UI with the list of resources/layout to display.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 29094b7..ebb053a 100755

//Synthetic comment -- @@ -1294,6 +1294,25 @@
}

/**
* Deselects a view info.
* Returns true if the object was actually selected.
* Callers are responsible for calling redraw() and updateOulineSelection() after.
//Synthetic comment -- @@ -1369,6 +1388,46 @@
return null;
}


/**
* Tries to find the inner most child matching the given x,y coordinates







