/*SmoothPagedView: tweak DEFAULT_TENSION value*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/SmoothPagedView.java b/src/com/android/launcher2/SmoothPagedView.java
//Synthetic comment -- index fe763f5..e2755c3 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
private Interpolator mScrollInterpolator;

private static class WorkspaceOvershootInterpolator implements Interpolator {
        private static final float DEFAULT_TENSION = 0.3f;
private float mTension;

public WorkspaceOvershootInterpolator() {







