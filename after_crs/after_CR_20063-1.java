/*Enable code specific to Eclipse 3.5

Change-Id:I29f6d1d74ce2979882988c18bc3e79d995445015*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 8365a37..a284c0a 100644

//Synthetic comment -- @@ -613,7 +613,8 @@
// the dragged view
int deltaX = (int) (scale * (boundingBox.x - p.x));
int deltaY = (int) (scale * (boundingBox.y - p.y));
                        e.offsetX = -deltaX;
                        e.offsetY = -deltaY;

// View rules may need to know it as well
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index e70687e..ecf152f 100644

//Synthetic comment -- @@ -28,8 +28,6 @@
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -57,7 +55,7 @@

@Override
public boolean performDrop(Object data) {
        final DropTargetEvent event = getCurrentEvent();
if (event == null) {
return false;
}
//Synthetic comment -- @@ -166,7 +164,7 @@
@Override
public boolean validateDrop(Object target, int operation,
TransferData transferType) {
        DropTargetEvent event = getCurrentEvent();
if (event == null) {
return false;
}
//Synthetic comment -- @@ -219,23 +217,4 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 9c7c472..5b00a70 100755

//Synthetic comment -- @@ -664,9 +664,9 @@
// mouse pointer

Rectangle imageBounds = mImage.getBounds();
                event.offsetX = imageBounds.width / 2;
                event.offsetY = imageBounds.height / 2;

}

event.doit = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 29a6059..3d872c7 100644

//Synthetic comment -- @@ -16,9 +16,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -28,7 +26,6 @@
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.List;

/**
//Synthetic comment -- @@ -173,58 +170,6 @@
}

/**
* Creates a new image from a source image where the contents from a given set of
* bounding boxes are copied into the new image and the rest is left transparent. A
* scale can be applied to make the resulting image larger or smaller than the source







