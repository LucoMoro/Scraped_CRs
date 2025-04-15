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
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.NonNull;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

/**
 * Implementation of {@link IAction} which delegates to a different
 * {@link IAction} which allows a subclass to wrap and customize some of the
 * behavior of a different action
 */
public class DelegatingAction implements IAction {
    private final IAction mAction;

    /**
     * Construct a new delegate of the given action
     *
     * @param action the action to be delegated
     */
    public DelegatingAction(@NonNull IAction action) {
        mAction = action;
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        mAction.addPropertyChangeListener(listener);
    }

    @Override
    public int getAccelerator() {
        return mAction.getAccelerator();
    }

    @Override
    public String getActionDefinitionId() {
        return mAction.getActionDefinitionId();
    }

    @Override
    public String getDescription() {
        return mAction.getDescription();
    }

    @Override
    public ImageDescriptor getDisabledImageDescriptor() {
        return mAction.getDisabledImageDescriptor();
    }

    @Override
    public HelpListener getHelpListener() {
        return mAction.getHelpListener();
    }

    @Override
    public ImageDescriptor getHoverImageDescriptor() {
        return mAction.getHoverImageDescriptor();
    }

    @Override
    public String getId() {
        return mAction.getId();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return mAction.getImageDescriptor();
    }

    @Override
    public IMenuCreator getMenuCreator() {
        return mAction.getMenuCreator();
    }

    @Override
    public int getStyle() {
        return mAction.getStyle();
    }

    @Override
    public String getText() {
        return mAction.getText();
    }

    @Override
    public String getToolTipText() {
        return mAction.getToolTipText();
    }

    @Override
    public boolean isChecked() {
        return mAction.isChecked();
    }

    @Override
    public boolean isEnabled() {
        return mAction.isEnabled();
    }

    @Override
    public boolean isHandled() {
        return mAction.isHandled();
    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        mAction.removePropertyChangeListener(listener);
    }

    @Override
    public void run() {
        mAction.run();
    }

    @Override
    public void runWithEvent(Event event) {
        mAction.runWithEvent(event);
    }

    @Override
    public void setActionDefinitionId(String id) {
        mAction.setActionDefinitionId(id);
    }

    @Override
    public void setChecked(boolean checked) {
        mAction.setChecked(checked);
    }

    @Override
    public void setDescription(String text) {
        mAction.setDescription(text);
    }

    @Override
    public void setDisabledImageDescriptor(ImageDescriptor newImage) {
        mAction.setDisabledImageDescriptor(newImage);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mAction.setEnabled(enabled);
    }

    @Override
    public void setHelpListener(HelpListener listener) {
        mAction.setHelpListener(listener);
    }

    @Override
    public void setHoverImageDescriptor(ImageDescriptor newImage) {
        mAction.setHoverImageDescriptor(newImage);
    }

    @Override
    public void setId(String id) {
        mAction.setId(id);
    }

    @Override
    public void setImageDescriptor(ImageDescriptor newImage) {
        mAction.setImageDescriptor(newImage);
    }

    @Override
    public void setMenuCreator(IMenuCreator creator) {
        mAction.setMenuCreator(creator);
    }

    @Override
    public void setText(String text) {
        mAction.setText(text);
    }

    @Override
    public void setToolTipText(String text) {
        mAction.setToolTipText(text);
    }

    @Override
    public void setAccelerator(int keycode) {
        mAction.setAccelerator(keycode);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 1e1813f..97a08f6 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.lint.LintEditAction;
import com.android.resources.Density;
import com.android.sdklib.SdkConstants;
import com.android.util.XmlUtils;
//Synthetic comment -- @@ -180,6 +181,12 @@
/** Copy action for the Edit or context menu. */
private Action mCopyAction;

    /** Undo action: delegates to the text editor */
    private IAction mUndoAction;

    /** Redo action: delegates to the text editor */
    private IAction mRedoAction;

/** Root of the context menu. */
private MenuManager mMenuManager;

//Synthetic comment -- @@ -1240,6 +1247,21 @@
bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), mPasteAction);
bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), mDeleteAction);
bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), mSelectAllAction);

            // Delegate the Undo and Redo actions to the text editor ones, but wrap them
            // such that we run lint to update the results on the current page (this is
            // normally done on each editor operation that goes through
            // {@link AndroidXmlEditor#wrapUndoEditXmlModel}, but not undo/redo)
            if (mUndoAction == null) {
                IAction undoAction = editor.getAction(ActionFactory.UNDO.getId());
                mUndoAction = new LintEditAction(undoAction, getEditorDelegate().getEditor());
            }
            bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), mUndoAction);
            if (mRedoAction == null) {
                IAction redoAction = editor.getAction(ActionFactory.REDO.getId());
                mRedoAction = new LintEditAction(redoAction, getEditorDelegate().getEditor());
            }
            bars.setGlobalActionHandler(ActionFactory.REDO.getId(), mRedoAction);
} else {
bars.setGlobalActionHandler(ActionFactory.CUT.getId(),
editor.getAction(ActionFactory.CUT.getId()));
//Synthetic comment -- @@ -1251,13 +1273,12 @@
editor.getAction(ActionFactory.DELETE.getId()));
bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
editor.getAction(ActionFactory.SELECT_ALL.getId()));
            bars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
                    editor.getAction(ActionFactory.UNDO.getId()));
            bars.setGlobalActionHandler(ActionFactory.REDO.getId(),
                    editor.getAction(ActionFactory.REDO.getId()));
}

bars.updateActionBars();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintEditAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintEditAction.java
new file mode 100644
//Synthetic comment -- index 0000000..bf05ce0

//Synthetic comment -- @@ -0,0 +1,49 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.lint;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DelegatingAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;

/**
 * Action intended to wrap an existing XML editor action, and then runs lint after
 * the edit.
 */
public class LintEditAction extends DelegatingAction {
    private final AndroidXmlEditor mEditor;

    /**
     * Creates a new {@link LintEditAction} associated with the given editor to
     * wrap the given action
     *
     * @param action the action to be wrapped
     * @param editor the editor associated with the action
     */
    public LintEditAction(@NonNull IAction action, @NonNull AndroidXmlEditor editor) {
        super(action);
        mEditor = editor;
    }

    @Override
    public void runWithEvent(Event event) {
        super.runWithEvent(event);
        mEditor.runEditHooks();
    }
}







