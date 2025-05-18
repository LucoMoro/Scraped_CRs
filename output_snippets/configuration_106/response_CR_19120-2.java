//<Beginning of snippet n. 0>
private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
private final UiViewElementNode mUiViewNode;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();
private LayoutScene mCurrentScene;

public CanvasViewInfo(CanvasViewInfo parent, UiViewElementNode viewInfo) {
    mParent = parent;
    mName = viewInfo.getClassName();
}

public String getViewKey() {
    return mName;
}

public void setCurrentScene(LayoutScene scene) {
    synchronized (mChildren) {
        this.mCurrentScene = scene;
    }
}

public LayoutScene getCurrentScene() {
    synchronized (mChildren) {
        return this.mCurrentScene;
    }
}

// ---- Implementation of IPropertySource

public Object getEditableValue() {
    return null;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private final Object renderLock = new Object();

public void renderScene(LayoutScene scene) {
    synchronized (renderLock) {
        synchronized (mChildren) {
            if (scene != null) {
                scene.dispose();
                this.mCurrentScene = scene;
            }
            model.refreshUi();
        }
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 4>
public class SceneResult {
    private final LayoutStatus mStatus;
    private final String mErrorMessage;
    private final Throwable mThrowable;

    public enum LayoutStatus { SUCCESS, ERROR, NOT_IMPLEMENTED, ANIMATION_CANCELED };

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

public SceneResult animate(int object, int animation, IAnimationListener listener) {
    if (listener != null) {
        if (listener.isCanceled()) {
            return new SceneResult(LayoutStatus.ANIMATION_CANCELED);
        }
        try {
            listener.done();
        } catch (Exception e) {
            return new SceneResult("Animation listener encountered an error", e);
        }
    }
    return new SceneResult(LayoutStatus.SUCCESS);
}
//<End of snippet n. 4>