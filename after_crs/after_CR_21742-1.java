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
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -284,6 +285,12 @@
}
});

        mSurface.addMouseWheelListener(new MouseWheelListener() {
            public void mouseScrolled(MouseEvent me) {
                mSurface.mouseScrolled(me);
            }
        });

mTimescale.addMouseListener(new MouseAdapter() {
@Override
public void mouseUp(MouseEvent me) {
//Synthetic comment -- @@ -842,7 +849,7 @@
}

private static enum GraphicsState {
        Normal, Marking, Scaling, Animating, Scrolling
};

private class Surface extends Canvas {
//Synthetic comment -- @@ -966,7 +973,8 @@
int xdim = dim.x - TotalXMargin;
mScaleInfo.setNumPixels(xdim);
boolean forceEndPoints = (mGraphicsState == GraphicsState.Scaling
                        || mGraphicsState == GraphicsState.Animating
                        || mGraphicsState == GraphicsState.Scrolling);
mScaleInfo.computeTicks(forceEndPoints);
mCachedMinVal = mScaleInfo.getMinVal();
mCachedMaxVal = mScaleInfo.getMaxVal();
//Synthetic comment -- @@ -1687,6 +1695,44 @@
update();
}

        private void mouseScrolled(MouseEvent me) {
            mGraphicsState = GraphicsState.Scrolling;
            double tMin = mScaleInfo.getMinVal();
            double tMax = mScaleInfo.getMaxVal();
            double zoomFactor = 2;
            double tMinRef = mLimitMinVal;
            double tMaxRef = mLimitMaxVal;
            double t; // the fixed point
            double tMinNew;
            double tMaxNew;
            if (me.count > 0) {
                // we zoom in
                Point dim = mSurface.getSize();
                int x = me.x;
                if (x < LeftMargin)
                    x = LeftMargin;
                if (x > dim.x - RightMargin)
                    x = dim.x - RightMargin;
                double ppr = mScaleInfo.getPixelsPerRange();
                t = tMin + ((x - LeftMargin) / ppr);
                tMinNew = Math.max(tMinRef, t - (t - tMin) / zoomFactor);
                tMaxNew = Math.min(tMaxRef, t + (tMax - t) / zoomFactor);
            } else {
                // we zoom out
                double factor = (tMax - tMin) / (tMaxRef - tMinRef);
                if (factor < 1) {
                    t = (factor * tMinRef - tMin) / (factor - 1);
                    tMinNew = Math.max(tMinRef, t - zoomFactor * (t - tMin));
                    tMaxNew = Math.min(tMaxRef, t + zoomFactor * (tMax - t));
                } else {
                    return;
                }
            }
            mScaleInfo.setMinVal(tMinNew);
            mScaleInfo.setMaxVal(tMaxNew);
            mSurface.redraw();
        }

// No defined behavior yet for double-click.
private void mouseDoubleClick(MouseEvent me) {
}







