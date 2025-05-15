
//<Beginning of snippet n. 0>


density, xdpi, ydpi,
resolver,
mProjectCallback,
                1 /*minSdkVersion*/,
                1 /*targetSdkVersion */,
logger);

if (transparentBackground) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


160, // ydpi
resolver,
projectCallBack,
                    1, // minSdkVersion
                    1, // targetSdkVersion
null //logger
));


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private final float mYdpi;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
    private final int mMinSdkVersion;
    private final int mTargetSdkVersion;
private final LayoutLog mLog;

private boolean mCustomBackgroundEnabled;
* value is the resource value.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
     * @param minSdkVersion the minSdkVersion of the project
     * @param targetSdkVersion the targetSdkVersion of the project
* @param log the object responsible for displaying warning/errors to the user.
*/
public Params(ILayoutPullParser layoutDescription,
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
RenderResources renderResources,
            IProjectCallback projectCallback,
            int minSdkVersion, int targetSdkVersion,
            LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
mScreenWidth = screenWidth;
mYdpi = ydpi;
mRenderResources = renderResources;
mProjectCallback = projectCallback;
        mMinSdkVersion = minSdkVersion;
        mTargetSdkVersion = targetSdkVersion;
mLog = log;
mCustomBackgroundEnabled = false;
mTimeout = DEFAULT_TIMEOUT;
mYdpi = params.mYdpi;
mRenderResources = params.mRenderResources;
mProjectCallback = params.mProjectCallback;
        mMinSdkVersion = params.mMinSdkVersion;
        mTargetSdkVersion = params.mTargetSdkVersion;
mLog = params.mLog;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
return mProjectKey;
}

    public int getMinSdkVersion() {
        return mMinSdkVersion;
    }

    public int getTargetSdkVersion() {
        return mTargetSdkVersion;
    }

public int getScreenWidth() {
return mScreenWidth;
}

//<End of snippet n. 2>








