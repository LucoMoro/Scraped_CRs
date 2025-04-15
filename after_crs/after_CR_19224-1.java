/*ADT: play animation

Change-Id:I020342a4fe205161328f22e9dabb0b03343677b5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 89eb9a4..cb76258 100755

//Synthetic comment -- @@ -1017,14 +1017,21 @@

scene.animate(viewObject, "testanim", false /*isFrameworkAnimation*/,
new IAnimationListener() {
                                private int count = 0;
                                private BufferedImage mImage;
                                private boolean mPendingDrawing = false;
                                public synchronized void onNewFrame(final BufferedImage image) {
                                    count++;
                                    mImage = image;
                                    if (mPendingDrawing == false) {
                                        getDisplay().asyncExec(new Runnable() {
                                            public void run() {
                                                drawImage();
                                            }
                                        });

                                        mPendingDrawing = true;
                                    }
}

public boolean isCanceled() {
//Synthetic comment -- @@ -1032,6 +1039,23 @@
}

public void done(SceneResult result) {
                                    System.out.println("Animation count: " + count);
                                }

                                /**
                                 * this is called from the UI thread from the asyncRunnable.
                                 */
                                public void drawImage() {
                                    // get last image
                                    BufferedImage image;
                                    synchronized (this) {
                                        image = mImage;
                                        mImage = null;
                                        mPendingDrawing = false;
                                    }

                                    mImageOverlay.setImage(image);
                                    redraw();
}
});
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 899fd49..dcf04ea 100644

//Synthetic comment -- @@ -116,7 +116,7 @@
* The {@link LayoutBridge} is only able to inflate or render one layout at a time. There
* is an internal lock object whenever such an action occurs. The timeout parameter is used
* when attempting to acquire the lock. If the timeout expires, the method will return
     * {@link SceneStatus#ERROR_TIMEOUT}.
*
* @param timeout timeout for the rendering, in milliseconds.
*







