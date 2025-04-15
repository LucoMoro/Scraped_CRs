/*ADT: play animation

Change-Id:I020342a4fe205161328f22e9dabb0b03343677b5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 89eb9a4..cb76258 100755

//Synthetic comment -- @@ -1017,14 +1017,21 @@

scene.animate(viewObject, "testanim", false /*isFrameworkAnimation*/,
new IAnimationListener() {

                                public void onNewFrame(final BufferedImage image) {
                                    getDisplay().asyncExec(new Runnable() {
                                        public void run() {
                                            mImageOverlay.setImage(image);
                                            redraw();
                                        }
                                    });
}

public boolean isCanceled() {
//Synthetic comment -- @@ -1032,6 +1039,23 @@
}

public void done(SceneResult result) {
}
});
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 899fd49..dcf04ea 100644

//Synthetic comment -- @@ -116,7 +116,7 @@
* The {@link LayoutBridge} is only able to inflate or render one layout at a time. There
* is an internal lock object whenever such an action occurs. The timeout parameter is used
* when attempting to acquire the lock. If the timeout expires, the method will return
     * SceneResult.sdfdsf
*
* @param timeout timeout for the rendering, in milliseconds.
*







