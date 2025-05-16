//<Beginning of snippet n. 0>
private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
private final UiViewElementNode mUiViewNode;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

public CanvasViewInfo(CanvasViewInfo parent, UiViewElementNode viewInfo) {
    mParent = parent;
    mName = viewInfo.getClassName();
}

// The ViewInfo#getViewKey() method returns a cookie uniquely identifying the object
// they represent on this side of the API.
public String getViewKey() {
    return mName;
}

// ---- Implementation of IPropertySource

public Object getEditableValue() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
    scene.dispose();
    model.refreshUi();
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
private boolean mExplodedParents;

public void setResult(LayoutScene scene, Set<UiElementNode> explodedNodes) {
    mIsResultValid = (scene != null && scene.getResult() == SceneResult.SUCCESS);
    mExplodedParents = false;
}

public boolean isResultValid() {
    return mIsResultValid;
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
package com.android.layoutlib.api;

import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IAnimationListener {
    void done();
    void isCanceled();
}

public SceneResult getResult() {
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

public SceneResult render() {
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

public SceneResult setProperty(int object, String propertyName, String propertyValue) {
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

public SceneResult insertChild() {
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

public SceneResult removeChild() {
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}

public SceneResult animate(int object, int animation, IAnimationListener listener) {
    // implementation of animation logic
    return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
public class SceneParams {
    public static enum RenderingMode {
        NORMAL(false, false),
        V_SCROLL(false, true);
    }

    private Map<String, Map<String, IResourceValue>> mFrameworkResources;
    private IProjectCallback mProjectCallback;
    private ILayoutLog mLogger;
    private boolean mCustomBackgroundEnabled;
    private int mCustomBackgroundColor;

    public SceneParams(IProjectCallback projectCallback, ILayoutLog logger) {
        mProjectCallback = projectCallback;
        mLogger = logger;
        mCustomBackgroundEnabled = false;
    }

    public IXmlPullParser getLayoutDescription() {
        return mLayoutDescription;
    }

    public int getCustomBackgroundColor() {
        return mCustomBackgroundColor;
    }

    public void setCustomBackgroundColor(int color) {
        mCustomBackgroundColor = color;
    }
}
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
package com.android.layoutlib.api;

public class SceneResult {
    private final LayoutStatus mStatus;
    private final String mErrorMessage;
    private final Throwable mThrowable;

    public enum LayoutStatus { SUCCESS, ERROR, NOT_IMPLEMENTED };

    public static final SceneResult SUCCESS = new SceneResult(LayoutStatus.SUCCESS);

    public SceneResult(String errorMessage) {
        mStatus = LayoutStatus.ERROR;
        mErrorMessage = errorMessage;
        mThrowable = null;
    }

    public SceneResult(String errorMessage, Throwable t) {
        mStatus = LayoutStatus.ERROR;
        mErrorMessage = errorMessage;
        mThrowable = t;
    }

    /*package*/ SceneResult(LayoutStatus status) {
        mStatus = status;
        mErrorMessage = null;
        mThrowable = null;
    }

    public LayoutStatus getStatus() {
        return mStatus;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public Throwable getException() {
        return mThrowable;
    }
}
//<End of snippet n. 6>