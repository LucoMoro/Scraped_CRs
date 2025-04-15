/*Fix keybindings in various XML editors for Eclipse 4.x.

This changeset attempts to fix the issue with cut/copy/paste (and
other keybindings) not working properly in the non-layout XML
files. The symptom is that the action appears not to work, but is
actually applied to a different editor.

I haven't figured out why this happens in e4; after you switch focus
the editor action map should switch, but the fix here is to listen for
part changes, and manually reset the action bindings whenever this
happens.

This fixes issue 34630 (as well as several duplicates filed recently).

(cherry picked from commit 8c83dcc3ec2cb72e44ddac7372980732f439d70d)

Change-Id:I3b597294def9e5f073e7e9e4dd3e0a128de38397*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 6368783..6f7c5d3 100644

//Synthetic comment -- @@ -310,7 +310,7 @@
mIsCreatingPage = true;
createFormPages();
createTextEditor();
        createUndoRedoActions();
postCreatePages();
mIsCreatingPage = false;
}
//Synthetic comment -- @@ -357,7 +357,7 @@
* multi-page editor) by re-using the actions defined by the {@link StructuredTextEditor}
* (aka the XML text editor.)
*/
    private void createUndoRedoActions() {
IActionBars bars = getEditorSite().getActionBars();
if (bars != null) {
IAction action = mTextEditor.getAction(ActionFactory.UNDO.getId());
//Synthetic comment -- @@ -1577,6 +1577,15 @@
return null;
}

/**
* Listen to changes in the underlying XML model in the structured editor.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlDelegate.java
//Synthetic comment -- index 08bdcf9..6edb68c 100755

//Synthetic comment -- @@ -217,4 +217,12 @@
public IFormPage delegatePostSetActivePage(IFormPage superReturned, String pageIndex) {
return superReturned;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java
//Synthetic comment -- index a7b3660..e3b5721 100755

//Synthetic comment -- @@ -42,6 +42,7 @@
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IShowEditorInput;
//Synthetic comment -- @@ -400,6 +401,21 @@
return super.supportsFormatOnGuiEdit();
}


// --------------------
// Base methods exposed so that XmlEditorDelegate can access them








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index f904f6e..6e151cc 100644

//Synthetic comment -- @@ -66,7 +66,6 @@
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IShowEditorInput;
//Synthetic comment -- @@ -94,7 +93,7 @@
* Multi-page form editor for /res/layout XML files.
*/
public class LayoutEditorDelegate extends CommonXmlDelegate
         implements IShowEditorInput, IPartListener, CommonXmlDelegate.IActionContributorDelegate {

public static class Creator implements IDelegateCreator {
@Override
//Synthetic comment -- @@ -223,7 +222,6 @@
mGraphicalEditor.dispose();
mGraphicalEditor = null;
}
        getEditor().getSite().getPage().removePartListener(this);
}

/**
//Synthetic comment -- @@ -283,9 +281,6 @@
mGraphicalEditor.replaceFile(editedFile);
}
}

            // put in place the listener to handle layout recompute only when needed.
            getEditor().getSite().getPage().addPartListener(this);
} catch (PartInitException e) {
AdtPlugin.log(e, "Error creating nested page"); //$NON-NLS-1$
}
//Synthetic comment -- @@ -724,50 +719,22 @@
}


    // ----- IPartListener Methods ----

@Override
    public void partActivated(IWorkbenchPart part) {
        if (part == getEditor()) {
            if (mGraphicalEditor != null) {
                if (getEditor().getActivePage() == mGraphicalEditorIndex) {
                    mGraphicalEditor.activated();
                } else {
                    mGraphicalEditor.deactivated();
                }
            }
        }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
        partActivated(part);
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        // pass
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
        if (part == getEditor()) {
            if (mGraphicalEditor != null && getEditor().getActivePage() == mGraphicalEditorIndex) {
mGraphicalEditor.deactivated();
}
}
}

@Override
    public void partOpened(IWorkbenchPart part) {
        /*
         * We used to automatically bring the outline and the property sheet to view
         * when opening the editor. This behavior has always been a mixed bag and not
         * exactly satisfactory. GLE1 is being useless/deprecated and GLE2 will need to
         * improve on that, so right now let's comment this out.
         */
        //EclipseUiHelper.showView(EclipseUiHelper.CONTENT_OUTLINE_VIEW_ID, false /* activate */);
        //EclipseUiHelper.showView(EclipseUiHelper.PROPERTY_SHEET_VIEW_ID, false /* activate */);
}

// ---- Local Methods ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java
//Synthetic comment -- index 5375dea..52f8623 100644

//Synthetic comment -- @@ -350,6 +350,10 @@

@Override
public void partActivated(IWorkbenchPartReference partRef) {
}

@Override
//Synthetic comment -- @@ -358,5 +362,9 @@

@Override
public void partDeactivated(IWorkbenchPartReference partRef) {
}
}
\ No newline at end of file







