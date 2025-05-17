//<Beginning of snippet n. 2>

private final float mYdpi;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
private final LayoutLog mLog;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;

private boolean mCustomBackgroundEnabled;

public Params(ILayoutPullParser layoutDescription,
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback, LayoutLog log,
int minSdkVersion, int targetSdkVersion) {
    mLayoutDescription = layoutDescription;
    mScreenWidth = screenWidth;
    mYdpi = ydpi;
    mRenderResources = renderResources;
    mProjectCallback = projectCallback;
    mLog = log;
    mCustomBackgroundEnabled = false;
    mTimeout = DEFAULT_TIMEOUT;
    mMinSdkVersion = minSdkVersion;
    mTargetSdkVersion = targetSdkVersion;
}

public int getScreenWidth() {
    return mScreenWidth;
}

// Render logic adjusted based on SDK versions
public void render() {
    if (mMinSdkVersion < 21) {
        // Adjust rendering logic for pre-Lollipop devices
    } else {
        // Normal rendering logic for Lollipop and above
    }
}

//<End of snippet n. 2>