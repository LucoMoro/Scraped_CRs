/*Run lint in the layout editor after undo/redo

Lint already runs automatically after each edit in the layout editor.
However, the undo and redo actions in the layout editor are mapped
directly through to the XML editor's undo/redo actions (and when run
their edits trigger a model refresh). This meant that after an undo or
redo, lint would not run to update. If you for example delete a button
that has a lint error on it, then undo that edit, the button comes
back without the lint error.

This CL fixes this: it now wraps the Undo/Redo actions with an action
wrapper which first runs the delegated action, and then runs the edit
hooks (e.g. lint) afterwards.

Change-Id:I66431fd7989e1f3c0776563d159ca7b89fef8431*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DelegatingAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DelegatingAction.java
new file mode 100644
//Synthetic comment -- index 0000000..7a41b5b

//Synthetic comment -- @@ -0,0 +1,203 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 1e1813f..97a08f6 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.resources.Density;
import com.android.sdklib.SdkConstants;
import com.android.util.XmlUtils;
//Synthetic comment -- @@ -180,6 +181,12 @@
/** Copy action for the Edit or context menu. */
private Action mCopyAction;

/** Root of the context menu. */
private MenuManager mMenuManager;

//Synthetic comment -- @@ -1240,6 +1247,21 @@
bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), mPasteAction);
bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), mDeleteAction);
bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), mSelectAllAction);
} else {
bars.setGlobalActionHandler(ActionFactory.CUT.getId(),
editor.getAction(ActionFactory.CUT.getId()));
//Synthetic comment -- @@ -1251,13 +1273,12 @@
editor.getAction(ActionFactory.DELETE.getId()));
bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
editor.getAction(ActionFactory.SELECT_ALL.getId()));
}

        IAction undoAction = editor.getAction(ActionFactory.UNDO.getId());
        bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
        IAction redoAction = editor.getAction(ActionFactory.REDO.getId());
        bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

bars.updateActionBars();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintEditAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintEditAction.java
new file mode 100644
//Synthetic comment -- index 0000000..bf05ce0

//Synthetic comment -- @@ -0,0 +1,49 @@







