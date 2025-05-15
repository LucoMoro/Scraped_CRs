
//<Beginning of snippet n. 0>


private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
    private final Object mViewObject;
private final UiViewElementNode mUiViewNode;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();
int parentX, int parentY) {
mParent = parent;
mName = viewInfo.getClassName();
        mViewObject = viewInfo.getViewObject();

// The ViewInfo#getViewKey() method returns a cookie uniquely identifying the object
// they represent on this side of the API.
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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}
}

model.refreshUi();
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.LayoutScene.IAnimationListener;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

/**
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
* the interaction with the widgets.

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
                                    private int count = 0;

                                public void onNewFrame(BufferedImage image) {
                                    try {
                                        ImageIO.write(image, "png",
                                                new File("/Users/xav/Desktop/anim/anim" + count++ + ".png"));
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
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


//<End of snippet n. 2>










//<Beginning of snippet n. 3>


*/
private boolean mExplodedParents;

    private LayoutScene mScene;

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
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

//<End of snippet n. 3>










//<Beginning of snippet n. 4>



package com.android.layoutlib.api;

import static com.android.layoutlib.api.SceneResult.SceneStatus.NOT_IMPLEMENTED;

import com.android.layoutlib.api.SceneResult.SceneStatus;

import java.awt.image.BufferedImage;
import java.util.Map;
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
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
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
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult setProperty(int object, String propertyName, String propertyValue) {
        return NOT_IMPLEMENTED.getResult();
}

/**
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult insertChild() {
        return NOT_IMPLEMENTED.getResult();
}

/**
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult removeChild() {
        return NOT_IMPLEMENTED.getResult();
}

/**
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

//<End of snippet n. 4>










//<Beginning of snippet n. 5>



public class SceneParams {

    public final static long DEFAULT_TIMEOUT = 250; //ms

public static enum RenderingMode {
NORMAL(false, false),
V_SCROLL(false, true),
private Map<String, Map<String, IResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
private ILayoutLog mLogger;

private boolean mCustomBackgroundEnabled;
private int mCustomBackgroundColor;
    private long mTimeout;

/**
*
mProjectCallback = projectCallback;
mLogger = logger;
mCustomBackgroundEnabled = false;
        mTimeout = DEFAULT_TIMEOUT;
}

/**
mLogger = params.mLogger;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
        mTimeout = params.mTimeout;
}

public void setCustomBackgroundColor(int color) {
mCustomBackgroundColor = color;
}

    public void setCustomTimeout(long timeout) {
        mTimeout = timeout;
    }

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
public int getCustomBackgroundColor() {
return mCustomBackgroundColor;
}

    public long getTimeout() {
        return mTimeout;
    }
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>



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

//<End of snippet n. 6>








