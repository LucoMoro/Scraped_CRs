/*gltrace: Draw a border around image displayed in canvas.

Helps frame the image when the image is mostly transparent.

Change-Id:I933247e6ee4b634ecfe26c3dea2408d57f930047*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java
//Synthetic comment -- index 79ee99d..4ff12c3 100644

//Synthetic comment -- @@ -20,12 +20,14 @@
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
//Synthetic comment -- @@ -176,7 +178,7 @@
}

private void paintCanvas(GC gc) {
        gc.fillRectangle(getClientArea()); // clear entire client area
if (mImage == null) {
return;
}
//Synthetic comment -- @@ -194,17 +196,27 @@
gc.drawImage(mImage,
0, 0, rect.width, rect.height,
0, 0, client.width, (int)(rect.height * sx));
                drawBorder(gc, 0, 0, client.width, (int)(rect.height * sx));
} else {
// scale client width to maintain aspect ratio
gc.drawImage(mImage,
0, 0, rect.width, rect.height,
0, 0, (int)(rect.width * sy), client.height);
                drawBorder(gc, 0, 0, (int)(rect.width * sy), client.height);
}
} else {
gc.drawImage(mImage, mOrigin.x, mOrigin.y);
            drawBorder(gc, mOrigin.x, mOrigin.y, rect.width, rect.height);
}
}

    private void drawBorder(GC gc, int x, int y, int width, int height) {
        Color origFg = gc.getForeground();
        gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        gc.drawRectangle(x, y, width, height);
        gc.setForeground(origFg);
    }

@Override
public void dispose() {
if (mImage != null && !mImage.isDisposed()) {







