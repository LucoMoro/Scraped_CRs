/*Fix layout window coordinator for Eclipse 4.2

Eclipse 4.x does not fire change events when the main editor window is
maximized or un-maximized:https://bugs.eclipse.org/bugs/show_bug.cgi?id=382120This CL works around that by using the layout editor's controlResized
listener to query the state flags of the IEditorPart and update the
maximized state as necessary. This fixes ADT issuehttp://code.google.com/p/android/issues/detail?id=32866Change-Id:Icb13e7191b12959c7a7038c6bc6099666985fec5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 371852c..1e1813f 100644

//Synthetic comment -- @@ -287,11 +287,25 @@
public void controlResized(ControlEvent e) {
super.controlResized(e);

                // Check editor state:
                LayoutWindowCoordinator coordinator = LayoutWindowCoordinator.get();
                if (coordinator != null) {
                    IEditorSite editorSite = getEditorDelegate().getEditor().getEditorSite();
                    coordinator.syncMaximizedState(editorSite.getPage());
                }

                Rectangle clientArea = getClientArea();
                mHScale.setClientSize(clientArea.width);
                mVScale.setClientSize(clientArea.height);

// Update the zoom level in the canvas when you toggle the zoom
                if (coordinator != null) {
                    mZoomCheck.run();
                } else {
                    // During startup, delay updates which can trigger further layout
                    getDisplay().asyncExec(mZoomCheck);

                }
}
});

//Synthetic comment -- @@ -329,14 +343,15 @@
return;
}

            LayoutWindowCoordinator coordinator = LayoutWindowCoordinator.get();
            if (coordinator != null) {
                Boolean zoomed = coordinator.isEditorMaximized();
                if (mWasZoomed != zoomed) {
                    if (mWasZoomed != null) {
                        setFitScale(true /*onlyZoomOut*/);
                    }
                    mWasZoomed = zoomed;
}
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java
//Synthetic comment -- index 6a6f564..a0672c6 100644

//Synthetic comment -- @@ -16,10 +16,12 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewReference;
//Synthetic comment -- @@ -73,13 +75,19 @@
*/
private boolean mInitialized;

    /** Singleton reference */
    private static LayoutWindowCoordinator sSingleton;

/**
* Start the coordinator
*
* @param window the associated window
*/
public static void start(@NonNull IWorkbenchWindow window) {
        assert sSingleton == null;

LayoutWindowCoordinator coordinator = new LayoutWindowCoordinator(window);
        sSingleton = coordinator;

IPartService service = window.getPartService();
if (service != null) {
//Synthetic comment -- @@ -88,7 +96,27 @@
}
}

    /**
     * Returns the coordinator. This method will return null if it is called before
     * {@link #start} has been called, and non null after.
     *
     * @return the coordinator
     */
    @Nullable
    public static LayoutWindowCoordinator get() {
        return sSingleton;
    }

    /**
     * Returns true if the main editor window is maximized
     *
     * @return true if the main editor window is maximized
     */
    public boolean isEditorMaximized() {
        return mEditorMaximized;
    }

    private LayoutWindowCoordinator(@NonNull IWorkbenchWindow window) {
mWindow = window;

initialize();
//Synthetic comment -- @@ -122,8 +150,9 @@
mOutlineOpen = true;
}
}
        if (!syncMaximizedState(activePage)) {
            syncActive();
        }
}

static IViewReference findPropertySheetView(IWorkbenchPage activePage) {
//Synthetic comment -- @@ -135,6 +164,43 @@
}

/**
     * Checks the maximized state of the page and updates internal state if
     * necessary.
     * <p>
     * This is used in Eclipse 4.x, where the {@link IPartListener2} does not
     * fire {@link IPartListener2#partHidden(IWorkbenchPartReference)} when the
     * editor is maximized anymore (see issue
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=382120 for details).
     * Instead, the layout editor listens for resize events, and upon resize it
     * looks up the part state and calls this method to ensure that the right
     * maximized state is known to the layout coordinator.
     *
     * @param page the active workbench page
     * @return true if the state changed, false otherwise
     */
    public boolean syncMaximizedState(IWorkbenchPage page) {
        boolean maximized = isPageZoomed(page);
        if (mEditorMaximized != maximized) {
            mEditorMaximized = maximized;
            syncActive();
            return true;
        }
        return false;
    }

    private boolean isPageZoomed(IWorkbenchPage page) {
        IWorkbenchPartReference reference = page.getActivePartReference();
        if (reference != null && reference instanceof IEditorReference) {
            int state = page.getPartState(reference);
            boolean maximized = (state & IWorkbenchPage.STATE_MAXIMIZED) != 0;
            return maximized;
        }

        // If the active reference isn't the editor, then the editor can't be maximized
        return false;
    }

    /**
* Syncs the given editor's view state such that the property sheet and or
* outline are shown or hidden according to the visibility of the global
* outline and property sheet views.
//Synthetic comment -- @@ -258,8 +324,9 @@
}
}

        boolean wasMaximized = mEditorMaximized;
mEditorMaximized = visibleCount <= 1;
        if (mEditorMaximized && !wasMaximized) {
// Only consider -maximizing- the window to be occasion for handling
// a "property sheet closed" event as a "show outline.
// And in fact we may want to remove it once you re-expose things







