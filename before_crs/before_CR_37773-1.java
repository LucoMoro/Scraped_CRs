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

                mHScale.setClientSize(getClientArea().width);
                mVScale.setClientSize(getClientArea().height);

// Update the zoom level in the canvas when you toggle the zoom
                getDisplay().asyncExec(mZoomCheck);
}
});

//Synthetic comment -- @@ -329,14 +343,15 @@
return;
}

            IEditorPart editor = getEditorDelegate().getEditor();
            IWorkbenchPage page = editor.getSite().getPage();
            Boolean zoomed = page.isPageZoomed();
            if (mWasZoomed != zoomed) {
                if (mWasZoomed != null) {
                    setFitScale(true /*onlyZoomOut*/);
}
                mWasZoomed = zoomed;
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java
//Synthetic comment -- index 6a6f564..a0672c6 100644

//Synthetic comment -- @@ -16,10 +16,12 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewReference;
//Synthetic comment -- @@ -73,13 +75,19 @@
*/
private boolean mInitialized;

/**
* Start the coordinator
*
* @param window the associated window
*/
public static void start(@NonNull IWorkbenchWindow window) {
LayoutWindowCoordinator coordinator = new LayoutWindowCoordinator(window);

IPartService service = window.getPartService();
if (service != null) {
//Synthetic comment -- @@ -88,7 +96,27 @@
}
}

    private LayoutWindowCoordinator(IWorkbenchWindow window) {
mWindow = window;

initialize();
//Synthetic comment -- @@ -122,8 +150,9 @@
mOutlineOpen = true;
}
}
        mEditorMaximized = activePage.isPageZoomed();
        syncActive();
}

static IViewReference findPropertySheetView(IWorkbenchPage activePage) {
//Synthetic comment -- @@ -135,6 +164,43 @@
}

/**
* Syncs the given editor's view state such that the property sheet and or
* outline are shown or hidden according to the visibility of the global
* outline and property sheet views.
//Synthetic comment -- @@ -258,8 +324,9 @@
}
}

mEditorMaximized = visibleCount <= 1;
        if (mEditorMaximized) {
// Only consider -maximizing- the window to be occasion for handling
// a "property sheet closed" event as a "show outline.
// And in fact we may want to remove it once you re-expose things







