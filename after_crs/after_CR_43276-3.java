/*Allow users to save current GL state to a text file.

Change-Id:I7140f8e71167dc4a8fd8481782f29822844e28c7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java
//Synthetic comment -- index 9ccd1df..faa9561 100644

//Synthetic comment -- @@ -22,13 +22,19 @@
import com.android.ide.eclipse.gltrace.model.GLTrace;
import com.android.ide.eclipse.gltrace.state.GLState;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
import com.android.ide.eclipse.gltrace.state.StatePrettyPrinter;
import com.android.ide.eclipse.gltrace.state.transforms.IStateTransform;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
//Synthetic comment -- @@ -40,13 +46,19 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
//Synthetic comment -- @@ -59,6 +71,7 @@
*/
public class StateViewPage extends Page implements ISelectionListener, ISelectionProvider {
public static final String ID = "com.android.ide.eclipse.gltrace.views.GLState"; //$NON-NLS-1$
    private static String sLastUsedPath;
private static final ILock sGlStateLock = Job.getJobManager().newLock();

private GLTrace mTrace;
//Synthetic comment -- @@ -116,6 +129,48 @@
mTreeViewer.setLabelProvider(mLabelProvider);
mTreeViewer.setInput(mState);
mTreeViewer.refresh();

        final IToolBarManager manager = getSite().getActionBars().getToolBarManager();
        manager.add(new Action("Save to File",
                PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_ETOOL_SAVEAS_EDIT)) {
            @Override
            public void run() {
                saveCurrentState();
            }
        });
    }

    private void saveCurrentState() {
        final Shell shell = mTreeViewer.getTree().getShell();
        FileDialog fd = new FileDialog(shell, SWT.SAVE);
        fd.setFilterExtensions(new String[] { "*.txt" });
        if (sLastUsedPath != null) {
            fd.setFilterPath(sLastUsedPath);
        }

        String path = fd.open();
        if (path == null) {
            return;
        }

        File f = new File(path);
        sLastUsedPath = f.getParent();

        // export state to f
        StatePrettyPrinter pp = new StatePrettyPrinter();
        synchronized (sGlStateLock) {
            mState.prettyPrint(pp);
        }

        try {
            Files.write(pp.toString(), f, Charsets.UTF_8);
        } catch (IOException e) {
            ErrorDialog.openError(shell,
                    "Export GL State",
                    "Unexpected error while writing GL state to file.",
                    new Status(Status.ERROR, GlTracePlugin.PLUGIN_ID, e.toString()));
        }
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLAbstractAtomicProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLAbstractAtomicProperty.java
//Synthetic comment -- index d8d9b99..cb96312 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/**
* Abstract implementation of {@link IGLProperty}. This provides the basics that can be
* used by leaf level properties.
//Synthetic comment -- @@ -56,4 +57,9 @@
return null;
}
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, getStringValue());
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLBooleanProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLBooleanProperty.java
//Synthetic comment -- index c193d83..22d8d47 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/** Properties that hold boolean values. */
public final class GLBooleanProperty extends GLAbstractAtomicProperty {
private final Boolean mDefaultValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLCompositeProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLCompositeProperty.java
//Synthetic comment -- index 421b7e4..ff8b83a 100644

//Synthetic comment -- @@ -123,4 +123,14 @@
throw new UnsupportedOperationException(
"Values cannot be obtained for composite properties."); //$NON-NLS-1$
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (IGLProperty p : mPropertiesMap.values()) {
            p.prettyPrint(pp);
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java
//Synthetic comment -- index 6573c77..6840abf 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
* A list property is a container for a list of properties, addressed by index.
//Synthetic comment -- @@ -166,4 +167,16 @@
public int size() {
return mList.size();
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (int i = 0; i < mList.size(); i++) {
            pp.prettyPrint(String.format(Locale.US, "Index %d:", i));
            IGLProperty p = mList.get(i);
            p.prettyPrint(pp);
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java
//Synthetic comment -- index 6bf4e96..ee063a2 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GLSparseArrayProperty implements IGLProperty {
private final GLStateType mType;
//Synthetic comment -- @@ -149,4 +150,21 @@
throw new UnsupportedOperationException(
"Values cannot be obtained for composite properties."); //$NON-NLS-1$
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (int i = 0; i < mSparseArray.size(); i++) {
            int key = mSparseArray.keyAt(i);
            pp.prettyPrint(String.format(Locale.US, "Index %d:", key));
            IGLProperty prop = mSparseArray.get(key);

            assert prop != null;
            if (prop != null) {
                prop.prettyPrint(pp);
            }
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/IGLProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/IGLProperty.java
//Synthetic comment -- index 51a526b..ed87fd2 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/**
* The GL state is modeled as a hierarchical set of properties, all of which implement
* this interface.
//Synthetic comment -- @@ -56,4 +57,7 @@

/** Deep clone this property. */
IGLProperty clone();

    /** Pretty print current property value to the given writer. */
    void prettyPrint(StatePrettyPrinter pp);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..0ad9aa4

//Synthetic comment -- @@ -0,0 +1,67 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.gltrace.state;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.utils.SdkUtils;

public class StatePrettyPrinter {
    private static final int SPACES_PER_INDENT = 4;
    private final String mLineSeparator = SdkUtils.getLineSeparator();

    private StringBuilder mSb = new StringBuilder(1000);
    private int mIndentLevel = 0;

    public void prettyPrint(@NonNull GLStateType name, @Nullable String value) {
        indentLine(mIndentLevel * SPACES_PER_INDENT);

        mSb.append(name.toString());

        if (value != null) {
            mSb.append(':');
            mSb.append(value);
        }
        mSb.append(mLineSeparator);
    }

    public void prettyPrint(@NonNull String s) {
        indentLine(mIndentLevel * SPACES_PER_INDENT);

        mSb.append(s);
        mSb.append(mLineSeparator);
    }

    private void indentLine(int spaces) {
        for (int i = 0; i < spaces; i++) {
            mSb.append(' ');
        }
    }

    public void incrementIndentLevel() {
        mIndentLevel++;
    }

    public void decrementIndentLevel() {
        mIndentLevel--;
    }

    @Override
    public String toString() {
        return mSb.toString();
    }
}







