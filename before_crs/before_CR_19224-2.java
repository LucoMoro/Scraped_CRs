/*ADT: more animation stuff.

Change-Id:I020342a4fe205161328f22e9dabb0b03343677b5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 89eb9a4..a6c55c2 100755

//Synthetic comment -- @@ -1,5 +1,4 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -346,6 +345,8 @@
mImageOverlay.dispose();
mImageOverlay = null;
}
}

/** Returns the Rules Engine, associated with the current project. */
//Synthetic comment -- @@ -1004,7 +1005,8 @@
// Add test action
// Don't add it at the top above (by the cut action) because the
// dynamic context menu makes some assumptions about where things are
        manager.add(new Action("Run My Test", IAction.AS_PUSH_BUTTON) {
@Override
public void run() {
List<CanvasSelection> selection = mSelectionManager.getSelections();
//Synthetic comment -- @@ -1017,14 +1019,21 @@

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
//Synthetic comment -- @@ -1032,6 +1041,23 @@
}

public void done(SceneResult result) {
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 58e3d1a..2d7577c 100644

//Synthetic comment -- @@ -98,6 +98,16 @@
private LayoutScene mScene;

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 899fd49..3429367 100644

//Synthetic comment -- @@ -116,7 +116,7 @@
* The {@link LayoutBridge} is only able to inflate or render one layout at a time. There
* is an internal lock object whenever such an action occurs. The timeout parameter is used
* when attempting to acquire the lock. If the timeout expires, the method will return
     * SceneResult.sdfdsf
*
* @param timeout timeout for the rendering, in milliseconds.
*
//Synthetic comment -- @@ -134,13 +134,13 @@
* <p/>
* Any amount of actions can be taken on the scene before {@link #render()} is called.
*
     * @param object
* @param propertyName
* @param propertyValue
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult setProperty(int object, String propertyName, String propertyValue) {
return NOT_IMPLEMENTED.getResult();
}

//Synthetic comment -- @@ -154,7 +154,21 @@
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult insertChild() {
return NOT_IMPLEMENTED.getResult();
}

//Synthetic comment -- @@ -168,7 +182,7 @@
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult removeChild() {
return NOT_IMPLEMENTED.getResult();
}








