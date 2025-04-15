/*ADT: Animation preparation.

Update layoutlib API to work better with the new scene
locking mechanism (for concurrent renderings), new error
types in SceneResult, and updated Animation listener.

ADT changes to record the view object in CanvasViewInfo,
and the current LayoutScene in ViewHierarchy.

Added a test menu item to start an animation. This
is temporary and will be fixed later.

Change-Id:I67df2d116afdfd23c093e4645d4a0f99695c5d95*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 303c58d..fb151d6 100755

//Synthetic comment -- @@ -59,6 +59,7 @@
private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
    private final Object mViewObject;
private final UiViewElementNode mUiViewNode;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();
//Synthetic comment -- @@ -84,6 +85,7 @@
int parentX, int parentY) {
mParent = parent;
mName = viewInfo.getClassName();
        mViewObject = viewInfo.getViewObject();

// The ViewInfo#getViewKey() method returns a cookie uniquely identifying the object
// they represent on this side of the API.
//Synthetic comment -- @@ -208,6 +210,14 @@
return mName;
}

    /**
     * Returns the View object associated with the {@link CanvasViewInfo}.
     * @return the view object or null.
     */
    public Object getViewObject() {
        return mViewObject;
    }

// ---- Implementation of IPropertySource

public Object getEditableValue() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 55d296d..cdbdf38 100755

//Synthetic comment -- @@ -1277,10 +1277,6 @@
}
}

model.refreshUi();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 337e76e..90026d4 100644

//Synthetic comment -- @@ -90,6 +90,10 @@
return mImage;
}

    public Image getImage() {
        return mImage;
    }

@Override
public void paint(GC gc) {
if (mImage != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8a7dd7a..78715ae 100755

//Synthetic comment -- @@ -29,6 +29,8 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.LayoutScene.IAnimationListener;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -71,6 +73,7 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -892,6 +895,46 @@

manager.add(new Separator());

        // Add test action
        // Don't add it at the top above (by the cut action) because the
        // dynamic context menu makes some assumptions about where things are
        manager.add(new Action("Run My Test", IAction.AS_PUSH_BUTTON) {
            @Override
            public void run() {
                List<CanvasSelection> selection = mSelectionManager.getSelections();
                CanvasSelection canvasSelection = selection.get(0);
                CanvasViewInfo info = canvasSelection.getViewInfo();

                Object viewObject = info.getViewObject();
                if (viewObject != null) {
                    LayoutScene scene = mViewHierarchy.getScene();

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
                                    return false;
                                }

                                public void done(SceneResult result) {
                                }
                            });
                }
            }
        });


        manager.add(new Separator());

manager.add(mDeleteAction);
manager.add(mSelectAllAction);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index c737fc4..58e3d1a 100644

//Synthetic comment -- @@ -95,6 +95,8 @@
*/
private boolean mExplodedParents;

    private LayoutScene mScene;

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -111,6 +113,12 @@
*            nodes are padded during certain interactions.
*/
/* package */ void setResult(LayoutScene scene, Set<UiElementNode> explodedNodes) {
        // replace the previous scene, so the previous scene must be disposed.
        if (mScene != null) {
            mScene.dispose();
        }

        mScene = scene;
mIsResultValid = (scene != null && scene.getResult() == SceneResult.SUCCESS);
mExplodedParents = false;

//Synthetic comment -- @@ -202,6 +210,14 @@
}

/**
     * Returns the current {@link LayoutScene}.
     * @return the scene or null if none have been set.
     */
    public LayoutScene getScene() {
        return mScene;
    }

    /**
* Returns true when the last {@link #setResult} provided a valid
* {@link LayoutScene}.
* <p/>








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 85c7365..899fd49 100644

//Synthetic comment -- @@ -16,7 +16,9 @@

package com.android.layoutlib.api;

import static com.android.layoutlib.api.SceneResult.SceneStatus.NOT_IMPLEMENTED;

import com.android.layoutlib.api.SceneResult.SceneStatus;

import java.awt.image.BufferedImage;
import java.util.Map;
//Synthetic comment -- @@ -41,19 +43,19 @@
/**
* Called when the animation is done playing.
*/
        void done(SceneResult result);

/**
* Returns true if the animation is canceled.
*/
        boolean isCanceled();
}

/**
* Returns the last operation result.
*/
public SceneResult getResult() {
        return NOT_IMPLEMENTED.getResult();
}

/**
//Synthetic comment -- @@ -61,7 +63,7 @@
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
//Synthetic comment -- @@ -74,7 +76,7 @@
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
//Synthetic comment -- @@ -98,10 +100,30 @@
* In case of success, this should be followed by calls to {@link #getRootView()} and
* {@link #getImage()} to access the result of the rendering.
*
     * This is equivalent to calling <code>render(SceneParams.DEFAULT_TIMEOUT)</code>
     *
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult render() {
        return render(SceneParams.DEFAULT_TIMEOUT);
    }

    /**
     * Re-renders the layout as-is, with a given timeout in case other renderings are being done.
     * In case of success, this should be followed by calls to {@link #getRootView()} and
     * {@link #getImage()} to access the result of the rendering.
     *
     * The {@link LayoutBridge} is only able to inflate or render one layout at a time. There
     * is an internal lock object whenever such an action occurs. The timeout parameter is used
     * when attempting to acquire the lock. If the timeout expires, the method will return
     * SceneResult.sdfdsf
     *
     * @param timeout timeout for the rendering, in milliseconds.
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult render(long timeout) {
        return NOT_IMPLEMENTED.getResult();
}

/**
//Synthetic comment -- @@ -119,7 +141,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult setProperty(int object, String propertyName, String propertyValue) {
        return NOT_IMPLEMENTED.getResult();
}

/**
//Synthetic comment -- @@ -133,7 +155,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult insertChild() {
        return NOT_IMPLEMENTED.getResult();
}

/**
//Synthetic comment -- @@ -147,7 +169,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult removeChild() {
        return NOT_IMPLEMENTED.getResult();
}

/**
//Synthetic comment -- @@ -156,10 +178,15 @@
* The animation playback is asynchronous and the rendered frame is sent vi the
* <var>listener</var>.
*
     * @param targetObject the view object to animate
     * @param animationName the name of the animation (res/anim) to play.
     * @param listener the listener callback.
     *
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult animate(Object targetObject, String animationName,
            boolean isFrameworkAnimation, IAnimationListener listener) {
        return NOT_IMPLEMENTED.getResult();
}

/**








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 36c7a0a..0eb9768 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

public class SceneParams {

    public final static long DEFAULT_TIMEOUT = 250; //ms

public static enum RenderingMode {
NORMAL(false, false),
V_SCROLL(false, true),
//Synthetic comment -- @@ -57,8 +59,10 @@
private Map<String, Map<String, IResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
private ILayoutLog mLogger;

private boolean mCustomBackgroundEnabled;
private int mCustomBackgroundColor;
    private long mTimeout;

/**
*
//Synthetic comment -- @@ -108,6 +112,7 @@
mProjectCallback = projectCallback;
mLogger = logger;
mCustomBackgroundEnabled = false;
        mTimeout = DEFAULT_TIMEOUT;
}

/**
//Synthetic comment -- @@ -130,6 +135,7 @@
mLogger = params.mLogger;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
        mTimeout = params.mTimeout;
}

public void setCustomBackgroundColor(int color) {
//Synthetic comment -- @@ -137,6 +143,10 @@
mCustomBackgroundColor = color;
}

    public void setCustomTimeout(long timeout) {
        mTimeout = timeout;
    }

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
//Synthetic comment -- @@ -200,4 +210,8 @@
public int getCustomBackgroundColor() {
return mCustomBackgroundColor;
}

    public long getTimeout() {
        return mTimeout;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 386eecc..57094ea 100644

//Synthetic comment -- @@ -16,43 +16,70 @@

package com.android.layoutlib.api;

/**
* Scene result class.
*/
public class SceneResult {

    private final SceneStatus mStatus;
private final String mErrorMessage;
private final Throwable mThrowable;

    public enum SceneStatus {
        SUCCESS,
        NOT_IMPLEMENTED,
        ERROR_TIMEOUT,
        ERROR_LOCK_INTERRUPTED,
        ERROR_UNKNOWN;

        /**
         * Returns a {@link SceneResult} object with this status.
         * @return an instance of SceneResult;
         */
        public SceneResult getResult() {
            // don't want to get generic error that way.
            assert this != ERROR_UNKNOWN;

            if (this == SUCCESS) {
                return SceneResult.SUCCESS;
            }

            return new SceneResult(this);
        }
    }

/**
* Singleton SUCCESS {@link SceneResult} object.
*/
    public static final SceneResult SUCCESS = new SceneResult(SceneStatus.SUCCESS);

/**
     * Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
     * the given message.
*/
public SceneResult(String errorMessage) {
        mStatus = SceneStatus.ERROR_UNKNOWN;
mErrorMessage = errorMessage;
mThrowable = null;
}

/**
     * Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
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
}
//Synthetic comment -- @@ -60,19 +87,21 @@
/**
* Returns the status. This is never null.
*/
    public SceneStatus getStatus() {
return mStatus;
}

/**
     * Returns the error message. This is only non-null when {@link #getStatus()} returns
     * {@link SceneStatus#ERROR_UNKNOWN}
*/
public String getErrorMessage() {
return mErrorMessage;
}

/**
     * Returns the exception. This is only non-null when {@link #getStatus()} returns
     * {@link SceneStatus#ERROR_UNKNOWN}
*/
public Throwable getException() {
return mThrowable;







