/*Add a horizontal scrollbar.

Change-Id:I077240805c45f71c8e270cada39bad3168061dfa*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeLineView.java b/traceview/src/com/android/traceview/TimeLineView.java
//Synthetic comment -- index bc565dd..c9eb8e7 100644

//Synthetic comment -- @@ -236,7 +236,15 @@
mSurface.redraw();
}
});

        final ScrollBar hBar = mSurface.getHorizontalBar();
        hBar.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                mSurface.setScaleFromHorizontalScrollBar(hBar.getSelection());
                mSurface.redraw();
            }
        });

mSurface.addListener(SWT.Resize, new Listener() {
public void handleEvent(Event e) {
Point dim = mSurface.getSize();
//Synthetic comment -- @@ -855,7 +863,7 @@
private class Surface extends Canvas {

public Surface(Composite parent) {
            super(parent, SWT.NO_BACKGROUND | SWT.V_SCROLL | SWT.H_SCROLL);
Display display = getDisplay();
mNormalCursor = new Cursor(display, SWT.CURSOR_CROSS);
mIncreasingCursor = new Cursor(display, SWT.CURSOR_SIZEE);
//Synthetic comment -- @@ -924,6 +932,41 @@
mScaleInfo.setMaxVal(mLimitMaxVal);
}

        public void setScaleFromHorizontalScrollBar(int selection) {
            double minVal = mScaleInfo.getMinVal();
            double maxVal = mScaleInfo.getMaxVal();
            double visibleRange = maxVal - minVal;

            minVal = mLimitMinVal + selection;
            maxVal = minVal + visibleRange;
            if (maxVal > mLimitMaxVal) {
                maxVal = mLimitMaxVal;
                minVal = maxVal - visibleRange;
            }
            mScaleInfo.setMinVal(minVal);
            mScaleInfo.setMaxVal(maxVal);

            mGraphicsState = GraphicsState.Scrolling;
        }

        private void updateHorizontalScrollBar() {
            double minVal = mScaleInfo.getMinVal();
            double maxVal = mScaleInfo.getMaxVal();
            double visibleRange = maxVal - minVal;
            double fullRange = mLimitMaxVal - mLimitMinVal;

            ScrollBar hBar = getHorizontalBar();
            if (fullRange > visibleRange) {
                hBar.setVisible(true);
                hBar.setMinimum(0);
                hBar.setMaximum((int)Math.ceil(fullRange));
                hBar.setThumb((int)Math.ceil(visibleRange));
                hBar.setSelection((int)Math.floor(minVal - mLimitMinVal));
            } else {
                hBar.setVisible(false);
            }
        }

private void draw(Display display, GC gc) {
if (mSegments.length == 0) {
// gc.setBackground(colorBackground);
//Synthetic comment -- @@ -985,6 +1028,9 @@

// Compute the strips
computeStrips();

                // Update the horizontal scrollbar.
                updateHorizontalScrollBar();
}

if (mNumRows > 2) {







