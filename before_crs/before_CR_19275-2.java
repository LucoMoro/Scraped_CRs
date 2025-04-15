/*Layoutlib API: updated API for insert/move/remove child.

Also added some new SceneStatus enums.

Change-Id:I225c58201d81cb1109d1d533fea48b6eacb2e286*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index ed42207..b431f07 100755

//Synthetic comment -- @@ -86,7 +86,6 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -997,9 +996,9 @@
new IAnimationListener() {
private int mCount = 0;
private boolean mPendingDrawing = false;
                                public void onNewFrame(final BufferedImage image) {
mCount++;
                                    mImageOverlay.setImage(image);
synchronized (this) {
if (mPendingDrawing == false) {
getDisplay().asyncExec(new Runnable() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 3429367..8c3d954 100644

//Synthetic comment -- @@ -37,8 +37,18 @@
public interface IAnimationListener {
/**
* Called when a new animation frame is available for display.
*/
        void onNewFrame(BufferedImage image);

/**
* Called when the animation is done playing.
//Synthetic comment -- @@ -46,7 +56,7 @@
void done(SceneResult result);

/**
         * Returns true if the animation is canceled.
*/
boolean isCanceled();
}
//Synthetic comment -- @@ -61,6 +71,7 @@
/**
* Returns the {@link ViewInfo} object for the top level view.
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
* {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
//Synthetic comment -- @@ -145,44 +156,73 @@
}

/**
     * Inserts a new child in a ViewGroup object.
* <p/>
     * This does nothing more than change the layouy. To render the scene in its new state, a
     * call to {@link #render()} is required.
* <p/>
     * Any amount of actions can be taken on the scene before {@link #render()} is called.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult insertChild(Object parentView, IXmlPullParser childXml, int index) {
return NOT_IMPLEMENTED.getResult();
}

/**
     * Inserts a new child in a ViewGroup object.
* <p/>
     * This does nothing more than change the layouy. To render the scene in its new state, a
     * call to {@link #render()} is required.
* <p/>
     * Any amount of actions can be taken on the scene before {@link #render()} is called.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult moveChild(Object parentView, IXmlPullParser layoutParamsXml, int index) {
return NOT_IMPLEMENTED.getResult();
}

/**
* Removes a child from a ViewGroup object.
* <p/>
     * This does nothing more than change the layouy. To render the scene in its new state, a
* call to {@link #render()} is required.
* <p/>
* Any amount of actions can be taken on the scene before {@link #render()} is called.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult removeChild(Object parentView, int index) {
return NOT_IMPLEMENTED.getResult();
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 57094ea..784bc29 100644

//Synthetic comment -- @@ -30,6 +30,10 @@
NOT_IMPLEMENTED,
ERROR_TIMEOUT,
ERROR_LOCK_INTERRUPTED,
ERROR_UNKNOWN;

/**
//Synthetic comment -- @@ -58,9 +62,7 @@
* the given message.
*/
public SceneResult(String errorMessage) {
        mStatus = SceneStatus.ERROR_UNKNOWN;
        mErrorMessage = errorMessage;
        mThrowable = null;
}

/**
//Synthetic comment -- @@ -68,17 +70,47 @@
* the given message and {@link Throwable}
*/
public SceneResult(String errorMessage, Throwable t) {
        mStatus = SceneStatus.ERROR_UNKNOWN;
mErrorMessage = errorMessage;
mThrowable = t;
}

/**
     * Creates a{@link SceneResult} object with the given SceneStatus.
*
* @param status the status
*/
    private SceneResult(SceneStatus status) {
mStatus = status;
mErrorMessage = null;
mThrowable = null;







