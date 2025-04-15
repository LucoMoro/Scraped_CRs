/*Enable landscape mode in launcher for larger phones. Also, speed up fling velocity in launcher

Change-Id:I52969b468047077069e41eed4f26544063ca1691*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index c182209..f4fdd7d 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
/**
* The velocity at which a fling gesture will cause us to snap to the next screen
*/
    private static final int SNAP_VELOCITY = 600;

private final WallpaperManager mWallpaperManager;

//Synthetic comment -- @@ -127,7 +127,7 @@

private WorkspaceOvershootInterpolator mScrollInterpolator;

    private static final float BASELINE_FLING_VELOCITY = 2500.f;
private static final float FLING_VELOCITY_INFLUENCE = 0.4f;

private static class WorkspaceOvershootInterpolator implements Interpolator {







