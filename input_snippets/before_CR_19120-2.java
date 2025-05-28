
//<Beginning of snippet n. 0>


private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
private final UiViewElementNode mUiViewNode;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();
int parentX, int parentY) {
mParent = parent;
mName = viewInfo.getClassName();

// The ViewInfo#getViewKey() method returns a cookie uniquely identifying the object
// they represent on this side of the API.
return mName;
}

// ---- Implementation of IPropertySource

public Object getEditableValue() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}
}

        // at the moment we don't keep the scene around for future actions,
        // so we must dispose it asap.
        scene.dispose();

model.refreshUi();
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.LayoutScene;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
* the interaction with the widgets.

manager.add(new Separator());

manager.add(mDeleteAction);
manager.add(mSelectAllAction);


//<End of snippet n. 2>










//<Beginning of snippet n. 3>


*/
private boolean mExplodedParents;

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*            nodes are padded during certain interactions.
*/
/* package */ void setResult(LayoutScene scene, Set<UiElementNode> explodedNodes) {
mIsResultValid = (scene != null && scene.getResult() == SceneResult.SUCCESS);
mExplodedParents = false;

}

/**
* Returns true when the last {@link #setResult} provided a valid
* {@link LayoutScene}.
* <p/>

//<End of snippet n. 3>










//<Beginning of snippet n. 4>



package com.android.layoutlib.api;

import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;
import java.util.Map;
/**
* Called when the animation is done playing.
*/
        void done();

/**
* Returns true if the animation is canceled.
*/
        void isCanceled();
}

/**
* Returns the last operation result.
*/
public SceneResult getResult() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link LayoutStatus#ERROR} or {@link LayoutStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
* <p>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link LayoutStatus#ERROR} or {@link LayoutStatus#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
*/
* In case of success, this should be followed by calls to {@link #getRootView()} and
* {@link #getImage()} to access the result of the rendering.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult render() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult setProperty(int object, String propertyName, String propertyValue) {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult insertChild() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult removeChild() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**
* The animation playback is asynchronous and the rendered frame is sent vi the
* <var>listener</var>.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult animate(int object, int animation, IAnimationListener listener) {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

/**

//<End of snippet n. 4>










//<Beginning of snippet n. 5>



public class SceneParams {

public static enum RenderingMode {
NORMAL(false, false),
V_SCROLL(false, true),
private Map<String, Map<String, IResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
private ILayoutLog mLogger;
private boolean mCustomBackgroundEnabled;
private int mCustomBackgroundColor;

/**
*
mProjectCallback = projectCallback;
mLogger = logger;
mCustomBackgroundEnabled = false;
}

/**
mLogger = params.mLogger;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
}

public void setCustomBackgroundColor(int color) {
mCustomBackgroundColor = color;
}

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
public int getCustomBackgroundColor() {
return mCustomBackgroundColor;
}
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>



package com.android.layoutlib.api;


/**
* Scene result class.
*/
public class SceneResult {

    private final LayoutStatus mStatus;
private final String mErrorMessage;
private final Throwable mThrowable;

    public enum LayoutStatus { SUCCESS, ERROR, NOT_IMPLEMENTED };

/**
* Singleton SUCCESS {@link SceneResult} object.
*/
    public static final SceneResult SUCCESS = new SceneResult(LayoutStatus.SUCCESS);

/**
     * Creates an error {@link SceneResult} object with the given message.
*/
public SceneResult(String errorMessage) {
        mStatus = LayoutStatus.ERROR;
mErrorMessage = errorMessage;
mThrowable = null;
}

/**
     * Creates an error {@link SceneResult} object with the given message and {@link Throwable}
*/
public SceneResult(String errorMessage, Throwable t) {
        mStatus = LayoutStatus.ERROR;
mErrorMessage = errorMessage;
mThrowable = t;
}

    /*package*/ SceneResult(LayoutStatus status) {
        mStatus = LayoutStatus.NOT_IMPLEMENTED;
mErrorMessage = null;
mThrowable = null;
}
/**
* Returns the status. This is never null.
*/
    public LayoutStatus getStatus() {
return mStatus;
}

/**
     * Returns the error message. This can be null if the status is {@link LayoutStatus#SUCCESS}.
*/
public String getErrorMessage() {
return mErrorMessage;
}

/**
     * Returns the exception. This can be null.
*/
public Throwable getException() {
return mThrowable;

//<End of snippet n. 6>








