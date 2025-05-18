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
    void onNewFrame(BufferedImage image);
    void done(SceneResult result);
    boolean isCanceled();
}

public SceneResult insertChild(Object parentView, IXmlPullParser childXml, int index) {
    if (parentView == null || childXml == null) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView or childXml cannot be null");
    }
    if (!(parentView instanceof ViewGroup)) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView must be an instance of ViewGroup");
    }
    if (index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index cannot be negative");
    }
    try {
        ViewGroup group = (ViewGroup) parentView;
        if (index > group.getChildCount()) {
            return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index exceeds the number of children in parentView");
        }
        group.addView(childXml.parse(), index);
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (IndexOutOfBoundsException e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index out of bounds", e);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Error inserting child", e);
    }
}

public SceneResult moveChild(Object parentView, IXmlPullParser layoutParamsXml, int index) {
    if (parentView == null || layoutParamsXml == null) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView or layoutParamsXml cannot be null");
    }
    if (!(parentView instanceof ViewGroup)) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView must be an instance of ViewGroup");
    }
    if (index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index cannot be negative");
    }
    try {
        ViewGroup group = (ViewGroup) parentView;
        if (index >= group.getChildCount()) {
            return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index exceeds the number of children in parentView");
        }
        group.removeViewAt(index);
        group.addView(layoutParamsXml.parse(), index);
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (IndexOutOfBoundsException e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index out of bounds", e);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Error moving child", e);
    }
}

public SceneResult removeChild(Object parentView, int index) {
    if (parentView == null) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView cannot be null");
    }
    if (!(parentView instanceof ViewGroup)) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "parentView must be an instance of ViewGroup");
    }
    if (index < 0) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index cannot be negative");
    }
    try {
        ViewGroup group = (ViewGroup) parentView;
        if (index >= group.getChildCount()) {
            return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index exceeds the number of children in parentView");
        }
        group.removeViewAt(index);
        return new SceneResult(SceneStatus.SUCCESS);
    } catch (IndexOutOfBoundsException e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Index out of bounds", e);
    } catch (Exception e) {
        return new SceneResult(SceneStatus.ERROR_UNKNOWN, "Error removing child", e);
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


NOT_IMPLEMENTED,
ERROR_TIMEOUT,
ERROR_LOCK_INTERRUPTED,
ERROR_UNKNOWN;

public SceneResult(String errorMessage) {
    mStatus = SceneStatus.ERROR_UNKNOWN;
    mErrorMessage = errorMessage;
    mThrowable = null;
}

public SceneResult(String errorMessage, Throwable t) {
    mStatus = SceneStatus.ERROR_UNKNOWN;
    mErrorMessage = errorMessage;
    mThrowable = t;
}

private SceneResult(SceneStatus status) {
    mStatus = status;
    mErrorMessage = null;
    mThrowable = null;
//<End of snippet n. 2>