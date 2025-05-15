
//<Beginning of snippet n. 0>


import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
new IAnimationListener() {
private int mCount = 0;
private boolean mPendingDrawing = false;
                                public void onNewFrame(LayoutScene scene) {
mCount++;
                                    mImageOverlay.setImage(scene.getImage());
synchronized (this) {
if (mPendingDrawing == false) {
getDisplay().asyncExec(new Runnable() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public interface IAnimationListener {
/**
* Called when a new animation frame is available for display.
         *
         * <p>The {@link LayoutScene} object is provided as a convenience. It should be queried
         * for the image through {@link LayoutScene#getImage()}.
         *
         * <p>If no {@link IImageFactory} is used, then each new animation frame will be rendered
         * in its own new {@link BufferedImage} object. However if an image factory is used, and it
         * always re-use the same object, then the image is only guaranteed to be valid during
         * this method call. As soon as this method return the image content will be overridden
         * with new drawing.
         *
*/
        void onNewFrame(LayoutScene scene);

/**
* Called when the animation is done playing.
void done(SceneResult result);

/**
         * Return true to cancel the animation.
*/
boolean isCanceled();
}
/**
* Returns the {@link ViewInfo} object for the top level view.
* <p>
     *
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
* {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
}

/**
     * Inserts a new child in a ViewGroup object, and renders the result.
* <p/>
     * The child is first inflated and then added to its parent, before the given sibling.
     * If the sibling is <code>null</code>, then it is added at the end of the ViewGroup.
* <p/>
     * If an animation listener is passed then the rendering is done asynchronously and the
     * result is sent to the listener.
     * If the listener is null, then the rendering is done synchronously.
     *
     * The child stays in the view hierarchy after the rendering is done. To remove it call
     * {@link #removeChild(Object, int)}.
     *
     * @param parentView the parent View object to receive the new child.
     * @param childXml an {@link IXmlPullParser} containing the content of the new child.
     * @param beforeSibling the object in <var>parentView</var> to insert before. If
     *             <code>null</code>, insert at the end.
     * @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult insertChild(Object parentView, IXmlPullParser childXml, Object beforeSibling,
            IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}

/**
     * Move a new child to a different ViewGroup object.
* <p/>
     * The child is first removed from its current parent, and then added to its new parent, before
     * the given sibling. If the sibling is <code>null</code>, then it is added at the end
     * of the ViewGroup.
* <p/>
     * If an animation listener is passed then the rendering is done asynchronously and the
     * result is sent to the listener.
     * If the listener is null, then the rendering is done synchronously.
     *
     * The child stays in the view hierarchy after the rendering is done. To remove it call
     * {@link #removeChild(Object, int)}.
     *
     * @param parentView the parent View object to receive the child. Can be the current parent
     *             already.
     * @param childView the view to move.
     * @param beforeSibling the object in <var>parentView</var> to insert before. If
     *             <code>null</code>, insert at the end.
     * @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult moveChild(Object parentView, Object childView, Object beforeSibling,
            IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}

/**
* Removes a child from a ViewGroup object.
* <p/>
     * This does nothing more than change the layout. To render the scene in its new state, a
* call to {@link #render()} is required.
* <p/>
* Any amount of actions can be taken on the scene before {@link #render()} is called.
*
     * @param childView the view object to remove from its parent
     * @param listener an optional {@link IAnimationListener}.
     *
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult removeChild(Object childView, IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


NOT_IMPLEMENTED,
ERROR_TIMEOUT,
ERROR_LOCK_INTERRUPTED,
        ERROR_INFLATION,
        ERROR_NOT_INFLATED,
        ERROR_RENDER,
        ERROR_ANIM_NOT_FOUND,
ERROR_UNKNOWN;

/**
* Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
* the given message.
*/
    public SceneResult(int foo, String errorMessage) {
        this(SceneStatus.ERROR_UNKNOWN, errorMessage, null);
}

/**
* Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
* the given message and {@link Throwable}
*/
    public SceneResult(int foo, String errorMessage, Throwable t) {
        this(SceneStatus.ERROR_UNKNOWN, errorMessage, t);
    }

    /**
     * Creates a {@link SceneResult} object with the given SceneStatus, and the given message
     * and {@link Throwable}.
     * <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
     *
     * @param status the status
     */
    public SceneResult(SceneStatus status, String errorMessage) {
        this(status, errorMessage, null);
    }

    /**
     * Creates a {@link SceneResult} object with the given SceneStatus, and the given message
     * and {@link Throwable}
     * <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
     *
     * @param status the status
     */
    public SceneResult(SceneStatus status, String errorMessage, Throwable t) {
        assert status != SceneStatus.SUCCESS;
        mStatus = status;
mErrorMessage = errorMessage;
mThrowable = t;
}

/**
     * Creates a {@link SceneResult} object with the given SceneStatus.
     * <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
*
* @param status the status
*/
    public SceneResult(SceneStatus status) {
mStatus = status;
mErrorMessage = null;
mThrowable = null;

//<End of snippet n. 2>








