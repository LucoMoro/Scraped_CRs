//<Beginning of snippet n. 0>


import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

new IAnimationListener() {
    private int mCount = 0;
    private boolean mPendingDrawing = false;

    public void onNewFrame(final BufferedImage image) {
        mCount++;
        mImageOverlay.setImage(image);
        synchronized (this) {
            if (!mPendingDrawing) {
                getDisplay().asyncExec(new Runnable() {
                    
//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public interface IAnimationListener {
    /**
     * Called when a new animation frame is available for display.
     */
    void onNewFrame(BufferedImage image);

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
 * Returns the {@link ViewInfo} object for the top level view.
 * <p>
 * This is reset to a new instance every time {@link #render()} is called and can be
 * <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
 * {@link SceneStatus#ERROR_UNKNOWN} or {@link SceneStatus#NOT_IMPLEMENTED}.
 */

/**
 * Inserts a new child in a ViewGroup object.
 * <p/>
 * This does nothing more than change the layout. To render the scene in its new state, a
 * call to {@link #render()} is required.
 * <p/>
 * Any amount of actions can be taken on the scene before {@link #render()} is called.
 *
 * @return a {@link SceneResult} indicating the status of the action.
 */
public SceneResult insertChild(Object parentView, IXmlPullParser childXml, int index) {
    if (parentView == null || index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Invalid parent view or index.");
    }

    try {
        // Assume parentView is a ViewGroup and we have a way to parse childXml.
        ViewGroup viewGroup = (ViewGroup) parentView;
        View childView = createViewFromXml(childXml);
        viewGroup.addView(childView, index);
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, e);
    }
}

/**
 * Moves a child in a ViewGroup object.
 * <p/>
 * This does nothing more than change the layout. To render the scene in its new state, a
 * call to {@link #render()} is required.
 * <p/>
 * Any amount of actions can be taken on the scene before {@link #render()} is called.
 *
 * @return a {@link SceneResult} indicating the status of the action.
 */
public SceneResult moveChild(Object parentView, IXmlPullParser layoutParamsXml, int index) {
    if (parentView == null || index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Invalid parent view or index.");
    }

    try {
        // Logic to get the current view and re-position based on layoutParamsXml.
        ViewGroup viewGroup = (ViewGroup) parentView;
        View viewToMove = viewGroup.getChildAt(index);
        viewGroup.removeViewAt(index);
        viewGroup.addView(viewToMove); // Or add it back with a new position based on layoutParamsXml.
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, e);
    }
}

/**
 * Removes a child from a ViewGroup object.
 * <p/>
 * This does nothing more than change the layout. To render the scene in its new state, a
 * call to {@link #render()} is required.
 * <p/>
 * Any amount of actions can be taken on the scene before {@link #render()} is called.
 *
 * @return a {@link SceneResult} indicating the status of the action.
 */
public SceneResult removeChild(Object parentView, int index) {
    if (parentView == null || index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Invalid parent view or index.");
    }

    try {
        ViewGroup viewGroup = (ViewGroup) parentView;
        viewGroup.removeViewAt(index);
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, e);
    }
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


NOT_IMPLEMENTED,
ERROR_TIMEOUT,
ERROR_LOCK_INTERRUPTED,
ERROR_UNKNOWN;

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
 * Creates a {@link SceneResult} object with the given SceneStatus.
 *
 * @param status the status
 */
private SceneResult(SceneStatus status) {
    mStatus = status;
    mErrorMessage = null;
    mThrowable = null;

//<End of snippet n. 2>







