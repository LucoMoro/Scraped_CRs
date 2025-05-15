//<Beginning of snippet n. 0>
density, xdpi, ydpi,
resolver,
mProjectCallback,
logger,
minSdkVersion,
targetSdkVersion);

if (transparentBackground) {

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
160, // ydpi
resolver,
projectCallback,
null, // logger
minSdkVersion,
targetSdkVersion
));

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
private final float mYdpi;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
private final LayoutLog mLog;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;

private boolean mCustomBackgroundEnabled;
/**
* value is the resource value.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param log the object responsible for displaying warning/errors to the user.
*/
public Params(ILayoutPullParser layoutDescription,
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback, LayoutLog log,
int minSdkVersion, int targetSdkVersion) {
    mLayoutDescription = layoutDescription;
    mProjectKey = projectKey;
    mScreenWidth = screenWidth;
    mYdpi = ydpi;
    mRenderResources = renderResources;
    mProjectCallback = projectCallback;
    mLog = log;
    mMinSdkVersion = minSdkVersion;
    mTargetSdkVersion = targetSdkVersion;
    mCustomBackgroundEnabled = false;
    mTimeout = DEFAULT_TIMEOUT;
}

public int getScreenWidth() {
    return mScreenWidth;
}
//<End of snippet n. 2>