/*Add min/targetSdkVersion to the LayoutLib API.

This will allow some widgets to properly behave based on the
minSdkVersion and targetSdkVersion of the project during
rendering.

Change-Id:I96d432b8b92fbc211ce122da51bd18049708c14f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index a857dc7..15d46e9 100644

//Synthetic comment -- @@ -1642,6 +1642,8 @@
density, xdpi, ydpi,
resolver,
mProjectCallback,
logger);

if (transparentBackground) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 05b360f..2d6d014 100644

//Synthetic comment -- @@ -215,6 +215,8 @@
160, // ydpi
resolver,
projectCallBack,
null //logger
));









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java
//Synthetic comment -- index 3cede41..4bb41f1 100644

//Synthetic comment -- @@ -54,6 +54,8 @@
private final float mYdpi;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
private final LayoutLog mLog;

private boolean mCustomBackgroundEnabled;
//Synthetic comment -- @@ -85,6 +87,8 @@
* value is the resource value.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param log the object responsible for displaying warning/errors to the user.
*/
public Params(ILayoutPullParser layoutDescription,
//Synthetic comment -- @@ -92,7 +96,9 @@
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
RenderResources renderResources,
            IProjectCallback projectCallback, LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
mScreenWidth = screenWidth;
//Synthetic comment -- @@ -103,6 +109,8 @@
mYdpi = ydpi;
mRenderResources = renderResources;
mProjectCallback = projectCallback;
mLog = log;
mCustomBackgroundEnabled = false;
mTimeout = DEFAULT_TIMEOUT;
//Synthetic comment -- @@ -122,6 +130,8 @@
mYdpi = params.mYdpi;
mRenderResources = params.mRenderResources;
mProjectCallback = params.mProjectCallback;
mLog = params.mLog;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
//Synthetic comment -- @@ -150,6 +160,14 @@
return mProjectKey;
}

public int getScreenWidth() {
return mScreenWidth;
}







