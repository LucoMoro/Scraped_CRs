/*Zoom with scrollwheel in Traceview.

The zoom factor is 2 (hardcoded).
The fixed point is given by the mouse position when zooming in.
When zooming out, we use the previous fixed point.
This commit do not launch an animation when zooming.

Change-Id:I020cce5ff12f80e70a49510dc334a250c23616ae*/
//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeLineView.java b/traceview/src/com/android/traceview/TimeLineView.java
//Synthetic comment -- index 875becf..bc565dd 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -284,6 +285,12 @@
}
});

mTimescale.addMouseListener(new MouseAdapter() {
@Override
public void mouseUp(MouseEvent me) {
//Synthetic comment -- @@ -842,7 +849,7 @@
}

private static enum GraphicsState {
        Normal, Marking, Scaling, Animating
};

private class Surface extends Canvas {
//Synthetic comment -- @@ -966,7 +973,8 @@
int xdim = dim.x - TotalXMargin;
mScaleInfo.setNumPixels(xdim);
boolean forceEndPoints = (mGraphicsState == GraphicsState.Scaling
                        || mGraphicsState == GraphicsState.Animating);
mScaleInfo.computeTicks(forceEndPoints);
mCachedMinVal = mScaleInfo.getMinVal();
mCachedMaxVal = mScaleInfo.getMaxVal();
//Synthetic comment -- @@ -1687,6 +1695,44 @@
update();
}

// No defined behavior yet for double-click.
private void mouseDoubleClick(MouseEvent me) {
}







