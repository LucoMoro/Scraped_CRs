/*More robust float values equality checks.

Floating point values cannot be reliably checked for equality. This
change replaces naive approach by distance checking. Additionaly,
conditions order is flipped to check [likely very fast] boolean
variable first and do [likely nanosecond slower] floating point
computation only if necessary.

Change-Id:I09a3d75793cdbe009a2bf95e08f28ef3cecdf6f3*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 4e55529..3bc38fa 100644

//Synthetic comment -- @@ -2314,11 +2314,11 @@
// reset mLastHeightSent to force VIEW_SIZE_CHANGED sent to WebKit
mLastHeightSent = 0;
}
        if (force || areDifferent(scale, mActualScale)) {
if (mDrawHistory) {
// If history Picture is drawn, don't update scroll. They will
// be updated when we get out of that mode.
                if (!mPreviewZoomOnly && areDifferent(scale, mActualScale)) {
mCallbackProxy.onScaleChanged(mActualScale, scale);
}
mActualScale = scale;
//Synthetic comment -- @@ -2336,7 +2336,7 @@
* (mZoomCenterY - getTitleHeight());

// now update our new scale and inverse
                if (!mPreviewZoomOnly && areDifferent(scale, mActualScale)) {
mCallbackProxy.onScaleChanged(mActualScale, scale);
}
mActualScale = scale;
//Synthetic comment -- @@ -2365,6 +2365,13 @@
}
}

    /** Delta used for floating point equality checks. */
    private static final float FP_EQUALITY_DELTA = 1e-8f;

    private static boolean areDifferent(final float x, final float y) {
        return Math.abs(x - y) > FP_EQUALITY_DELTA;
    }

// Used to avoid sending many visible rect messages.
private Rect mLastVisibleRectSent;
private Rect mLastGlobalRect;







