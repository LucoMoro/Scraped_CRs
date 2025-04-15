/*ADT GLE2: ContentOutline

Change-Id:I76e8bef9a881012a582f69d0dbf1a580c63cd6f3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ContentOutline2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ContentOutline2.java
new file mode 100755
//Synthetic comment -- index 0000000..c284b3b

//Synthetic comment -- @@ -0,0 +1,89 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 *
 */
public class ContentOutline2 implements IContentOutlinePage {

    private TreeViewer mTreeViewer;

    public ContentOutline2() {
    }

    public void createControl(Composite parent) {
        Tree tree = new Tree(parent, SWT.DEFAULT /*style*/);
        mTreeViewer = new TreeViewer(tree);
        //mTreeViewer.setContentProvider(new UiModelTreeContentProvider(mUiRootNode, mDescriptorFilters));
        //mTreeViewer.setLabelProvider(new UiModelTreeLabelProvider());
        //mTreeViewer.setInput("unused"); //$NON-NLS-1$
    }

    public void dispose() {
        Control c = getControl();
        if (c != null && !c.isDisposed()) {
            mTreeViewer = null;
            c.dispose();
        }
    }

    public Control getControl() {
        return mTreeViewer == null ? null : mTreeViewer.getControl();
    }

    public void setActionBars(IActionBars arg0) {
        // TODO Auto-generated method stub

    }

    public void setFocus() {
        // TODO Auto-generated method stub

    }

    public void addSelectionChangedListener(ISelectionChangedListener arg0) {
        // TODO Auto-generated method stub

    }

    public void setSelection(ISelection arg0) {
        // TODO Auto-generated method stub

    }

    public ISelection getSelection() {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
        // TODO Auto-generated method stub

    }

}







