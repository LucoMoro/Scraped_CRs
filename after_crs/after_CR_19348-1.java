/*LayoutLib API: new log API + updated SceneStatus API.

Change-Id:I8fe107397c2322cca979e7953d2be5933a59d0bf*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index a493b56..04b268d 100755

//Synthetic comment -- @@ -47,10 +47,10 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutLog;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneParams.RenderingMode;
//Synthetic comment -- @@ -187,7 +187,7 @@
private Map<String, Map<String, IResourceValue>> mConfiguredFrameworkRes;
private Map<String, Map<String, IResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
    private LayoutLog mLog;

private boolean mNeedsRecompute = false;

//Synthetic comment -- @@ -1018,7 +1018,7 @@
// no root view yet indicates success and then update the canvas with it.

mCanvasViewer.getCanvas().setResult(
                        new BasicLayoutScene(SceneStatus.SUCCESS.createResult(),
null /*rootViewInfo*/, null /*image*/),
null /*explodeNodes*/);
return;
//Synthetic comment -- @@ -1331,23 +1331,34 @@
}

// Lazily create the logger the first time we need it
        if (mLog == null) {
            mLog = new LayoutLog() {
                @Override
                public void error(String tag, String message) {
AdtPlugin.printErrorToConsole(mEditedFile.getName(), message);
}

                @Override
                public void error(String tag, Throwable throwable) {
                    String message = throwable.getMessage();
if (message == null) {
                        message = throwable.getClass().getName();
}

PrintStream ps = new PrintStream(AdtPlugin.getErrorStream());
                    throwable.printStackTrace(ps);
}

                @Override
                public void error(String tag, String message, Throwable throwable) {
                    AdtPlugin.printErrorToConsole(mEditedFile.getName(), message);

                    PrintStream ps = new PrintStream(AdtPlugin.getErrorStream());
                    throwable.printStackTrace(ps);
                }

                @Override
                public void warning(String tag, String message) {
AdtPlugin.printToConsole(mEditedFile.getName(), message);
}
};
//Synthetic comment -- @@ -1435,7 +1446,7 @@
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
                mLog);

if (transparentBackground) {
// It doesn't matter what the background color is as long as the alpha








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index 6f4fea1..1d4ff62 100644

//Synthetic comment -- @@ -17,8 +17,10 @@
package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutLog;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
//Synthetic comment -- @@ -78,6 +80,23 @@
public LayoutScene createScene(SceneParams params) {
int apiLevel = mBridge.getApiLevel();

        // create a log wrapper since the older api requires a ILayoutLog
        final LayoutLog log = params.getLog();
        ILayoutLog logWrapper = new ILayoutLog() {

            public void warning(String message) {
                log.warning(null, message);
            }

            public void error(Throwable t) {
                log.error(null, t);
            }

            public void error(String message) {
                log.error(null, message);
            }
        };

ILayoutResult result = null;

if (apiLevel == 4) {
//Synthetic comment -- @@ -89,7 +108,7 @@
params.getDensity(), params.getXdpi(), params.getYdpi(),
params.getThemeName(), params.getIsProjectTheme(),
params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
} else if (apiLevel == 3) {
// api 3 add density support.
result = mBridge.computeLayout(
//Synthetic comment -- @@ -98,7 +117,7 @@
params.getDensity(), params.getXdpi(), params.getYdpi(),
params.getThemeName(), params.getIsProjectTheme(),
params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
} else if (apiLevel == 2) {
// api 2 added boolean for separation of project/framework theme
result = mBridge.computeLayout(
//Synthetic comment -- @@ -106,7 +125,7 @@
params.getScreenWidth(), params.getScreenHeight(),
params.getThemeName(), params.getIsProjectTheme(),
params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
} else {
// First api with no density/dpi, and project theme boolean mixed
// into the theme name.
//Synthetic comment -- @@ -123,7 +142,7 @@
params.getScreenWidth(), params.getScreenHeight(),
themeName,
params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
}

// clean up that is not done by the ILayoutBridge itself
//Synthetic comment -- @@ -147,10 +166,10 @@
ViewInfo rootViewInfo;

if (result.getSuccess() == ILayoutResult.SUCCESS) {
            sceneResult = SceneStatus.SUCCESS.createResult();
rootViewInfo = convertToViewInfo(result.getRootView());
} else {
            sceneResult = SceneStatus.ERROR_UNKNOWN.createResult(result.getErrorMessage());
rootViewInfo = null;
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutLog.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutLog.java
//Synthetic comment -- index cae15d3..9931f3e 100644

//Synthetic comment -- @@ -18,25 +18,27 @@

/**
* Callback interface to display warnings/errors that happened during the computation and
 * rendering of the layout.
 * @deprecated use {@link LayoutLog}.
*/
@Deprecated
public interface ILayoutLog {

/**
     * Logs a warning message.
     * @param message the message to log.
*/
void warning(String message);

/**
     * Logs an error message.
     * @param message the message to log.
*/
void error(String message);

/**
     * Logs an exception
     * @param t the {@link Throwable} to log.
*/
void error(Throwable t);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutLog.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutLog.java
new file mode 100644
//Synthetic comment -- index 0000000..052750e

//Synthetic comment -- @@ -0,0 +1,38 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.layoutlib.api;

public class LayoutLog {

    public void error(String tag, String message) {
    }

    public void error(String tag, Throwable t) {
    }

    public void warning(String tag, String message) {
    }

    /**
     * Logs an error message and a {@link Throwable}.
     * @param message the message to log.
     * @param throwable the {@link Throwable} to log.
     */
    public void error(String tag, String message, Throwable throwable) {

    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 96741c3..0883b45 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
* Returns the last operation result.
*/
public SceneResult getResult() {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -134,7 +134,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult render(long timeout) {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -152,7 +152,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult setProperty(Object objectView, String propertyName, String propertyValue) {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -182,7 +182,7 @@
*/
public SceneResult insertChild(Object parentView, IXmlPullParser childXml, int index,
IAnimationListener listener) {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -218,7 +218,7 @@
*/
public SceneResult moveChild(Object parentView, Object childView, int index,
Map<String, String> layoutParams, IAnimationListener listener) {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -235,7 +235,7 @@
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult removeChild(Object childView, IAnimationListener listener) {
        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -252,7 +252,7 @@
*/
public SceneResult animate(Object targetObject, String animationName,
boolean isFrameworkAnimation, IAnimationListener listener) {
        return NOT_IMPLEMENTED.createResult();
}

/**








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 1bbd96a..56014f0 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
private Map<String, Map<String, IResourceValue>> mProjectResources;
private Map<String, Map<String, IResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
    private LayoutLog mLog;

private boolean mCustomBackgroundEnabled;
private int mCustomBackgroundColor;
//Synthetic comment -- @@ -89,7 +89,7 @@
* value is the resource value.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
     * @param log the object responsible for displaying warning/errors to the user.
*/
public SceneParams(IXmlPullParser layoutDescription,
Object projectKey,
//Synthetic comment -- @@ -98,7 +98,7 @@
String themeName, boolean isProjectTheme,
Map<String, Map<String, IResourceValue>> projectResources,
Map<String, Map<String, IResourceValue>> frameworkResources,
            IProjectCallback projectCallback, LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
mScreenWidth = screenWidth;
//Synthetic comment -- @@ -112,7 +112,7 @@
mProjectResources = projectResources;
mFrameworkResources = frameworkResources;
mProjectCallback = projectCallback;
        mLog = log;
mCustomBackgroundEnabled = false;
mTimeout = DEFAULT_TIMEOUT;
}
//Synthetic comment -- @@ -134,7 +134,7 @@
mProjectResources = params.mProjectResources;
mFrameworkResources = params.mFrameworkResources;
mProjectCallback = params.mProjectCallback;
        mLog = params.mLog;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
//Synthetic comment -- @@ -206,8 +206,8 @@
return mProjectCallback;
}

    public LayoutLog getLog() {
        return mLog;
}

public boolean isCustomBackgroundEnabled() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 1a46167..1417f3d 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
* Returns a {@link SceneResult} object with this status.
* @return an instance of SceneResult;
*/
        public SceneResult createResult() {
// don't want to get generic error that way.
assert this != ERROR_UNKNOWN;

//Synthetic comment -- @@ -71,8 +71,8 @@
*
* @see SceneResult#getData()
*/
        public SceneResult createResult(Object data) {
            SceneResult res = createResult();

if (data != null) {
res = res.getCopyWithData(data);
//Synthetic comment -- @@ -87,7 +87,7 @@
* @param throwable the throwable
* @return an instance of SceneResult.
*/
        public SceneResult createResult(String errorMessage, Throwable throwable) {
return new SceneResult(this, errorMessage, throwable);
}

//Synthetic comment -- @@ -96,7 +96,7 @@
* @param errorMessage the error message
* @return an instance of SceneResult.
*/
        public SceneResult createResult(String errorMessage) {
return new SceneResult(this, errorMessage, null /*throwable*/);
}
}







