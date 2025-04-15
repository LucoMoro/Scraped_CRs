/*Fix memory Leak due to SurfaceView not being detached.

Change-Id:If3f3577eaaa33e4043e8a5e6417f663bcaaf6fbb*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/AllApps3D.java b/src/com/android/launcher2/AllApps3D.java
//Synthetic comment -- index b8aa8ec..376b1fe 100644

//Synthetic comment -- @@ -213,6 +213,7 @@
destroyRenderScript();
sRS = null;
sRollo = null;
            super.onDetachedFromWindow();
}
}








