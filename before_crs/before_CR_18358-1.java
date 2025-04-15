/*Select dropped elements

This changeset makes the drop handler select the set of	elements that
were just manipulated. This means that if you drop a new element, it
is both more visible (useful if you drop it into a crowded layout) and
immediately available for customization via the property sheet. This
is also important when you drag & drop to move existing elements since
without this, dragging a selected element meant you would *lose*
selection.

There are some timing tricks involved similar to yesterday's focus
checkin because in multi-document drag & drop the view hiearchy (which
we must consult to find the visual objects corresponding to the XML
just added to the model) may not yet be ready.

Change-Id:Ifa2d492a03a02f24303dd7cc95ab653523fb1758*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index d6d624c..5efdda7 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.DropFeedback;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
//Synthetic comment -- @@ -26,8 +27,14 @@
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TransferData;

import java.util.Arrays;

/**
* Handles drop operations on top of the canvas.
//Synthetic comment -- @@ -274,12 +281,41 @@

Point where = mCanvas.displayToCanvasPoint(event.x, event.y);

updateDropFeedback(mFeedback, event);
mCanvas.getRulesEngine().callOnDropped(mTargetNode,
elements,
mFeedback,
where);

clearDropInfo();
mCanvas.redraw();
// Request focus: This is *necessary* when you are dragging from one canvas editor
//Synthetic comment -- @@ -289,6 +325,26 @@
}

/**
* Updates the {@link DropFeedback#isCopy} and {@link DropFeedback#sameCanvas} fields
* of the given {@link DropFeedback}. This is generally called right before invoking
* one of the callOnXyz methods of GRE to refresh the fields.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 88064ce..b637223 100755

//Synthetic comment -- @@ -112,6 +112,7 @@
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
//Synthetic comment -- @@ -1243,6 +1244,29 @@
}

/**
* Deselects a view info.
* Returns true if the object was actually selected.
* Callers are responsible for calling redraw() and updateOulineSelection() after.
//Synthetic comment -- @@ -1400,6 +1424,23 @@
}

/**
* Used by {@link #onSelectAll()} to add all current view infos to the selection list.
*
* @param canvasViewInfo The root to add. This info and all its children will be added to the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 09529ea..558fb53 100755

//Synthetic comment -- @@ -88,7 +88,14 @@
mBounds.set(bounds);
}

    /* package */ UiViewElementNode getNode() {
return mNode;
}








