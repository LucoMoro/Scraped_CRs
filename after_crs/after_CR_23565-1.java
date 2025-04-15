/*17384: Clicking in the layout editor does not show its properties

In Eclipse 3.6.2, the property sheet implementation no longer listens
to selection events; it only listens to post-selection events.

This is fixed in Eclipse 3.7, but we want to work on 3.6.2 too so work
around Eclipse issue #162079.

Change-Id:I9596364d3c5b7483a5c9053075b9b930293dfab9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 41017a0..5d0f5b6 100644

//Synthetic comment -- @@ -40,12 +40,12 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
//Synthetic comment -- index a1438d9..cc13343 100755

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
//Synthetic comment -- @@ -40,7 +43,7 @@
* canvas' selection changes are broadcasted to anyone listening, which includes
* the part itself as well as the associated outline and property sheet pages.
*/
class LayoutCanvasViewer extends Viewer implements IPostSelectionProvider {

private LayoutCanvas mCanvas;
private final LayoutEditor mLayoutEditor;
//Synthetic comment -- @@ -58,6 +61,7 @@
private ISelectionChangedListener mSelectionListener = new ISelectionChangedListener() {
public void selectionChanged(SelectionChangedEvent event) {
fireSelectionChanged(event);
            firePostSelectionChanged(event);
}
};

//Synthetic comment -- @@ -127,4 +131,26 @@
mCanvas = null;
}
}

    private ListenerList mPostChangedListeners = new ListenerList();

    public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
        mPostChangedListeners.add(listener);
    }

    public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
        mPostChangedListeners.remove(listener);
    }

    protected void firePostSelectionChanged(final SelectionChangedEvent event) {
        Object[] listeners = mPostChangedListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    l.selectionChanged(event);
                }
            });
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 01e72d0..aeb05a2 100644

//Synthetic comment -- @@ -94,7 +94,6 @@
/** List of clients listening to selection changes. */
private final ListenerList mSelectionListeners = new ListenerList();

/**
* Constructs a new {@link SelectionManager} associated with the given layout canvas.
*
//Synthetic comment -- @@ -105,12 +104,12 @@
}

public void addSelectionChangedListener(ISelectionChangedListener listener) {
        mSelectionListeners.add(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        mSelectionListeners.remove(listener);
    }

/**
* Returns the native {@link SelectionItem} list.







