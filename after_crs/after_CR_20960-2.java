/*Add app name/icon and current render locale to the LayoutLib API.

This allows the layoutlib to render system/title/action bars
as a window decor like it would look on the device.
This can be disabled with RenderParams.setForceNoDecor().
(BTW, Params was renamed RenderParams too)

Also minor update to the API by replacing an int with Density
since the enum is now accessible to the API and layoutlib.

Change-Id:Ic37770a9276d12af90c60199a84b04cb64e7c3a1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 5886cff..c1ba22c 100644

//Synthetic comment -- @@ -532,6 +532,7 @@
removeMarkersFromProject(project, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromProject(project, AndroidConstants.MARKER_XML);
removeMarkersFromProject(project, AndroidConstants.MARKER_AIDL);
        removeMarkersFromProject(project, AndroidConstants.MARKER_RENDERSCRIPT);
removeMarkersFromProject(project, AndroidConstants.MARKER_ANDROID);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 790cbc2..8ec3111 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
private boolean mZeroAttributeIsPadding = false;
private boolean mIncreaseExistingPadding = false;
private List<ViewElementDescriptor> mLayoutDescriptors;
    private final Density mDensity;
private final float mXdpi;

/**
//Synthetic comment -- @@ -106,12 +106,12 @@
*/
public UiElementPullParser(UiElementNode top, boolean explodeRendering,
Set<UiElementNode> explodeNodes,
            Density density, float xdpi, IProject project) {
super();
mRoot = top;
mExplodedRendering = explodeRendering;
mExplodeNodes = explodeNodes;
        mDensity = density;
mXdpi = xdpi;
if (mExplodedRendering) {
// get the layout descriptor
//Synthetic comment -- @@ -571,7 +571,7 @@
case COMPLEX_UNIT_DIP:
case COMPLEX_UNIT_SP: // intended fall-through since we don't
// adjust for font size
                            f *= (float)mDensity.getDpiValue() / Density.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
f *= mXdpi * (1.0f / 72);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index eda6514..f09ef99 100644

//Synthetic comment -- @@ -26,11 +26,11 @@
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -74,7 +74,6 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -1618,7 +1617,7 @@
}
}

        Density density = mConfigComposite.getDensity();
float xdpi = mConfigComposite.getXDpi();
float ydpi = mConfigComposite.getYDpi();
boolean isProjectTheme = mConfigComposite.isProjectTheme();
//Synthetic comment -- @@ -1661,7 +1660,7 @@
configuredProjectRes, frameworkResources,
theme, isProjectTheme);

        RenderParams params = new RenderParams(
topParser,
iProject /* projectKey */,
width, height,
//Synthetic comment -- @@ -1673,6 +1672,22 @@
mTargetSdkVersion,
logger);

        // FIXME make persistent and only reload when the manifest (or at least resources) chanage.
        IFolderWrapper projectFolder = new IFolderWrapper(getProject());
        IAbstractFile manifest = AndroidManifest.getManifest(projectFolder);
        if (manifest != null) {
            try {
                params.setAppIcon(AndroidManifest.getApplicationIcon(manifest));
            } catch (Exception e) {
                // ignore.
            }
            try {
                params.setAppLabel(AndroidManifest.getApplicationLabel(manifest));
            } catch (Exception e) {
                // ignore.
            }
        }

ScreenSizeQualifier ssq = mConfigComposite.getCurrentConfig().getScreenSizeQualifier();
if (ssq != null) {
params.setConfigScreenSize(ssq.getValue());
//Synthetic comment -- @@ -2429,7 +2444,7 @@
int oldMinSdkVersion = mMinSdkVersion;
int oldTargetSdkVersion = mTargetSdkVersion;

        IAbstractFile manifestFile = AndroidManifest.getManifest(
new IFolderWrapper(mEditedFile.getProject()));

if (manifestFile != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 00604d6..08aef31 100755

//Synthetic comment -- @@ -27,9 +27,9 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 2fb2ebe..1d92f3a 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 13d9a49..0fa0df9 100644

//Synthetic comment -- @@ -19,10 +19,10 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -207,13 +207,13 @@
configuredProject, configuredFramework,
"Theme", false /*isProjectTheme*/);

            RenderSession session = layoutLib.createSession(new RenderParams(
parser,
null /*projectKey*/,
320,
480,
RenderingMode.NORMAL,
                    Density.MEDIUM,
160, //xdpi
160, // ydpi
resolver,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index e0292ee..fbd3aa0 100644

//Synthetic comment -- @@ -171,7 +171,7 @@
ui, // model
false, // explodedView
null, // explodeNodes
                    Density.MEDIUM, // density (default from ConfigurationComposite)
Density.MEDIUM.getDpiValue(), // xdpi (default from ConfigurationComposite)
null // iProject
);








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index aa5608d..43444de 100644

//Synthetic comment -- @@ -21,12 +21,12 @@
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.rendering.api.Result.Status;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.common.rendering.legacy.ILegacyPullParser;
//Synthetic comment -- @@ -58,7 +58,7 @@
* Use {@link #load(String, ILogger)} to load the jar file.
* <p/>
* Use the layout library with:
 * {@link #init(String, Map)}, {@link #supports(Capability)}, {@link #createSession(RenderParams)},
* {@link #dispose()}, {@link #clearCaches(Object)}.
*
* <p/>
//Synthetic comment -- @@ -274,9 +274,9 @@
* @return a new {@link ILayoutScene} object that contains the result of the scene creation and
* first rendering or null if {@link #getStatus()} doesn't return {@link LoadStatus#LOADED}.
*
     * @see Bridge#createSession(RenderParams)
*/
    public RenderSession createSession(RenderParams params) {
if (mBridge != null) {
return mBridge.createSession(params);
} else if (mLegacyBridge != null) {
//Synthetic comment -- @@ -338,7 +338,7 @@
return apiLevel;
}

    private RenderSession createLegacySession(RenderParams params) {
if (params.getLayoutDescription() instanceof IXmlPullParser == false) {
throw new IllegalArgumentException("Parser must be of type ILegacyPullParser");
}
//Synthetic comment -- @@ -390,7 +390,7 @@
params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
                    params.getDensity().getDpiValue(), params.getXdpi(), params.getYdpi(),
resources.getThemeName(), resources.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(),
//Synthetic comment -- @@ -400,7 +400,7 @@
result = mLegacyBridge.computeLayout(
(IXmlPullParser) params.getLayoutDescription(), params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
                    params.getDensity().getDpiValue(), params.getXdpi(), params.getYdpi(),
resources.getThemeName(), resources.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(), logWrapper);








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index d014b6c..755c736 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
* @return a new {@link RenderSession} object that contains the result of the scene creation and
* first rendering.
*/
    public RenderSession createSession(RenderParams params) {
return null;
}

//Synthetic comment -- @@ -89,7 +89,7 @@
* <p/>The cache is not configuration dependent and should only be cleared when a
* resource changes (at this time only bitmaps and 9 patches go into the cache).
* <p/>
     * The project key provided must be similar to the one passed in {@link RenderParams}.
*
* @param projectKey the key for the project.
*/








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java
//Synthetic comment -- index 12bd57c..ca60640 100644

//Synthetic comment -- @@ -44,4 +44,11 @@
public Density getDensity() {
return Density.getEnum(mDensity.getDpiValue());
}

    @Override
    public String toString() {
        return "DensityBasedResourceValue ["
                + getResourceType() + "/" + getName() + " = " + getValue()
                + " (density:" + mDensity +", framework:" + isFramework() + ")]";
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IImageFactory.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IImageFactory.java
//Synthetic comment -- index c05c7c6..7681243 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
* rendered. The goal is to let the layoutlib caller create an image that's optimized for its use
* case.
*
 * If no factory is passed in {@link RenderParams#setImageFactory(IImageFactory)}, then a default
* {@link BufferedImage} of type {@link BufferedImage#TYPE_INT_ARGB} is created.
*
*/








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
similarity index 85%
rename from layoutlib_api/src/com/android/ide/common/rendering/api/Params.java
rename to layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index fb4b423..296982c 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.ide.common.rendering.api;

import com.android.resources.Density;
import com.android.resources.ScreenSize;


public class RenderParams {

public final static long DEFAULT_TIMEOUT = 250; //ms

//Synthetic comment -- @@ -51,7 +52,7 @@
private final int mScreenWidth;
private final int mScreenHeight;
private final RenderingMode mRenderingMode;
    private final Density mDensity;
private final float mXdpi;
private final float mYdpi;
private final RenderResources mRenderResources;
//Synthetic comment -- @@ -67,6 +68,10 @@
private IImageFactory mImageFactory = null;

private ScreenSize mConfigScreenSize = null;
    private String mAppIcon = null;
    private String mAppLabel = null;
    private String mLocale = null;
    private boolean mForceNoDecor;

/**
*
//Synthetic comment -- @@ -95,10 +100,10 @@
* @param targetSdkVersion the targetSdkVersion of the project
* @param log the object responsible for displaying warning/errors to the user.
*/
    public RenderParams(ILayoutPullParser layoutDescription,
Object projectKey,
int screenWidth, int screenHeight, RenderingMode renderingMode,
            Density density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
//Synthetic comment -- @@ -123,7 +128,7 @@
/**
* Copy constructor.
*/
    public RenderParams(RenderParams params) {
mLayoutDescription = params.mLayoutDescription;
mProjectKey = params.mProjectKey;
mScreenWidth = params.mScreenWidth;
//Synthetic comment -- @@ -142,6 +147,10 @@
mTimeout = params.mTimeout;
mImageFactory = params.mImageFactory;
mConfigScreenSize = params.mConfigScreenSize;
        mAppIcon = params.mAppIcon;
        mAppLabel = params.mAppLabel;
        mLocale = params.mLocale;
        mForceNoDecor = params.mForceNoDecor;
}

public void setOverrideBgColor(int color) {
//Synthetic comment -- @@ -161,6 +170,22 @@
mConfigScreenSize  = size;
}

    public void setAppIcon(String appIcon) {
        mAppIcon = appIcon;
    }

    public void setAppLabel(String appLabel) {
        mAppLabel = appLabel;
    }

    public void setLocale(String locale) {
        mLocale = locale;
    }

    public void setForceNoDecor() {
        mForceNoDecor = true;
    }

public ILayoutPullParser getLayoutDescription() {
return mLayoutDescription;
}
//Synthetic comment -- @@ -189,7 +214,7 @@
return mRenderingMode;
}

    public Density getDensity() {
return mDensity;
}

//Synthetic comment -- @@ -232,4 +257,20 @@
public ScreenSize getConfigScreenSize() {
return mConfigScreenSize;
}

    public String getAppIcon() {
        return mAppIcon;
    }

    public String getAppLabel() {
        return mAppLabel;
    }

    public String getLocale() {
        return mLocale;
    }

    public boolean isForceNoDecor() {
        return mForceNoDecor;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index 9bdd146..a2e087c 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
/**
* An object allowing interaction with an Android layout.
*
 * This is returned by {@link Bridge#createScene(RenderParams)}.
* and can then be used for subsequent actions on the layout.
*
* @since 5
//Synthetic comment -- @@ -102,7 +102,7 @@
* @return a {@link Result} indicating the status of the action.
*/
public Result render() {
        return render(RenderParams.DEFAULT_TIMEOUT);
}

/**








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java
//Synthetic comment -- index 76561c8..f15d903 100644

//Synthetic comment -- @@ -91,4 +91,12 @@
public void replaceWith(ResourceValue value) {
mValue = value.mValue;
}

    @Override
    public String toString() {
        return "ResourceValue [" + mType + "/" + mName + " = " + mValue
                + " (framework:" + mIsFramwork + ")]";
    }


}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 6ed6e49..2cb6ace 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
public final static String ATTRIBUTE_GLESVERSION = "glEsVersion";
public final static String ATTRIBUTE_PROCESS = "process";
public final static String ATTRIBUTE_DEBUGGABLE = "debuggable";
    public final static String ATTRIBUTE_LABEL = "label";
    public final static String ATTRIBUTE_ICON = "icon";
public final static String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
public final static String ATTRIBUTE_TARGET_SDK_VERSION = "targetSdkVersion";
public final static String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
//Synthetic comment -- @@ -76,6 +78,22 @@
public final static String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";

/**
     * Returns an {@link IAbstractFile} object representing the manifest for the given project.
     *
     * @param project The project containing the manifest file.
     * @return An IAbstractFile object pointing to the manifest or null if the manifest
     *         is missing.
     */
    public static IAbstractFile getManifest(IAbstractFolder projectFolder) {
        IAbstractFile file = projectFolder.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);
        if (file.exists()) {
            return file;
        }

        return null;
    }

    /**
* Returns the package for a given project.
* @param projectFolder the folder of the project.
* @return the package info or null (or empty) if not found.
//Synthetic comment -- @@ -84,8 +102,12 @@
*/
public static String getPackage(IAbstractFolder projectFolder)
throws XPathExpressionException, StreamException {
        IAbstractFile file = getManifest(projectFolder);
        if (file != null) {
            return getPackage(file);
        }

        return null;
}

/**
//Synthetic comment -- @@ -123,7 +145,8 @@
String value = xPath.evaluate(
"/"  + NODE_MANIFEST +
"/"  + NODE_APPLICATION +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":"  + ATTRIBUTE_DEBUGGABLE,
new InputSource(manifestFile.getContents()));

// default is not debuggable, which is the same behavior as parseBoolean
//Synthetic comment -- @@ -240,6 +263,43 @@
}
}

    /**
     * Returns the application icon  for a given manifest.
     * @param manifestFile the manifest to parse.
     * @return the icon or null (or empty) if not found.
     * @throws XPathExpressionException
     * @throws StreamException If any error happens when reading the manifest.
     */
    public static String getApplicationIcon(IAbstractFile manifestFile)
            throws XPathExpressionException, StreamException {
        XPath xPath = AndroidXPathFactory.newXPath();

        return xPath.evaluate(
                "/"  + NODE_MANIFEST +
                "/"  + NODE_APPLICATION +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":"  + ATTRIBUTE_ICON,
                new InputSource(manifestFile.getContents()));
    }

    /**
     * Returns the application label  for a given manifest.
     * @param manifestFile the manifest to parse.
     * @return the label or null (or empty) if not found.
     * @throws XPathExpressionException
     * @throws StreamException If any error happens when reading the manifest.
     */
    public static String getApplicationLabel(IAbstractFile manifestFile)
            throws XPathExpressionException, StreamException {
        XPath xPath = AndroidXPathFactory.newXPath();

        return xPath.evaluate(
                "/"  + NODE_MANIFEST +
                "/"  + NODE_APPLICATION +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":"  + ATTRIBUTE_LABEL,
                new InputSource(manifestFile.getContents()));
    }

/**
* Combines a java package, with a class value from the manifest to make a fully qualified








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 9a67735..09e81f7 100644

//Synthetic comment -- @@ -632,7 +632,7 @@

public static ManifestData parse(IAbstractFolder projectFolder)
throws SAXException, IOException, StreamException, ParserConfigurationException {
        IAbstractFile manifestFile = AndroidManifest.getManifest(projectFolder);
if (manifestFile == null) {
throw new FileNotFoundException();
}
//Synthetic comment -- @@ -666,20 +666,4 @@

return null;
}
}







