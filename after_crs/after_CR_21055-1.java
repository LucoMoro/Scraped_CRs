/*Fix Extract as Include

Two fixes for Extract as Include:

(1) After extracting the view fragments from the source layout, save
    the layout file. This is necessary in order to make the "Show
    Included In" functionality work on the newly extracted fragment,
    since the include-finder which identifies candidates for inclusion
    is triggered by file saves, not buffer edits, and secondly even
    the Show Included In facility itself requires the outer buffer to
    be a saved file, not an edited buffer since the parser reads from
    disk.

(2) Insert a space after the closing quote in the include string; this
    could lead to broken XML in some instances.

Change-Id:I10ede72ef40dc0afaa93322637397245ac9a54ee*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java
//Synthetic comment -- index f4d31d7..d3ff090 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
//Synthetic comment -- @@ -476,6 +477,7 @@
sb.append("<include layout=\"@layout/"); //$NON-NLS-1$
sb.append(newName);
sb.append('"');
        sb.append(' ');

// Create new id for the include itself
if (referenceId != null) {
//Synthetic comment -- @@ -788,6 +790,13 @@
}
}
});

        // Save file to trigger include finder scanning (as well as making the
        // actual show-include feature work since it relies on reading files from
        // disk, not a live buffer)
        IEditorPart editorPart = editor;
        IWorkbenchPage page = editorPart.getEditorSite().getPage();
        page.saveEditor(editorPart, false);
}

/**







