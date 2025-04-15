/*PagedView: tweak mSnapVelocity*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/PagedView.java b/src/com/android/launcher2/PagedView.java
//Synthetic comment -- index 3f5652e..413d1c5 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
private static final float SIGNIFICANT_MOVE_THRESHOLD = 0.4f;

// the velocity at which a fling gesture will cause us to snap to the next page
    protected int mSnapVelocity = 100;

protected float mDensity;
protected float mSmoothingTime;







