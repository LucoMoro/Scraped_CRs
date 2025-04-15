/*Add app name/icon and current render locale to the LayoutLib API.

Also minor update to the API by replacing an int with Density
since the enum is now accessible to the API and layoutlib.

Change-Id:Ic37770a9276d12af90c60199a84b04cb64e7c3a1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 5886cff..c1ba22c 100644

//Synthetic comment -- @@ -532,6 +532,7 @@
removeMarkersFromProject(project, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromProject(project, AndroidConstants.MARKER_XML);
removeMarkersFromProject(project, AndroidConstants.MARKER_AIDL);
removeMarkersFromProject(project, AndroidConstants.MARKER_ANDROID);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 790cbc2..8ec3111 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
private boolean mZeroAttributeIsPadding = false;
private boolean mIncreaseExistingPadding = false;
private List<ViewElementDescriptor> mLayoutDescriptors;
    private final int mDensityValue;
private final float mXdpi;

/**
//Synthetic comment -- @@ -106,12 +106,12 @@
*/
public UiElementPullParser(UiElementNode top, boolean explodeRendering,
Set<UiElementNode> explodeNodes,
            int densityValue, float xdpi, IProject project) {
super();
mRoot = top;
mExplodedRendering = explodeRendering;
mExplodeNodes = explodeNodes;
        mDensityValue = densityValue;
mXdpi = xdpi;
if (mExplodedRendering) {
// get the layout descriptor
//Synthetic comment -- @@ -571,7 +571,7 @@
case COMPLEX_UNIT_DIP:
case COMPLEX_UNIT_SP: // intended fall-through since we don't
// adjust for font size
                            f *= (float)mDensityValue / Density.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
f *= mXdpi * (1.0f / 72);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index eda6514..13426f5 100644

//Synthetic comment -- @@ -74,7 +74,6 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -1618,7 +1617,7 @@
}
}

        int density = mConfigComposite.getDensity().getDpiValue();
float xdpi = mConfigComposite.getXDpi();
float ydpi = mConfigComposite.getYDpi();
boolean isProjectTheme = mConfigComposite.isProjectTheme();
//Synthetic comment -- @@ -1673,6 +1672,22 @@
mTargetSdkVersion,
logger);

ScreenSizeQualifier ssq = mConfigComposite.getCurrentConfig().getScreenSizeQualifier();
if (ssq != null) {
params.setConfigScreenSize(ssq.getValue());
//Synthetic comment -- @@ -2429,7 +2444,7 @@
int oldMinSdkVersion = mMinSdkVersion;
int oldTargetSdkVersion = mTargetSdkVersion;

        IAbstractFile manifestFile = AndroidManifestParser.getManifest(
new IFolderWrapper(mEditedFile.getProject()));

if (manifestFile != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 13d9a49..b2c8cd8 100644

//Synthetic comment -- @@ -213,7 +213,7 @@
320,
480,
RenderingMode.NORMAL,
                    160, //density
160, //xdpi
160, // ydpi
resolver,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index e0292ee..fbd3aa0 100644

//Synthetic comment -- @@ -171,7 +171,7 @@
ui, // model
false, // explodedView
null, // explodeNodes
                    Density.MEDIUM.getDpiValue(), // density (default from ConfigurationComposite)
Density.MEDIUM.getDpiValue(), // xdpi (default from ConfigurationComposite)
null // iProject
);








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index aa5608d..6edfb94 100644

//Synthetic comment -- @@ -390,7 +390,7 @@
params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
resources.getThemeName(), resources.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(),
//Synthetic comment -- @@ -400,7 +400,7 @@
result = mLegacyBridge.computeLayout(
(IXmlPullParser) params.getLayoutDescription(), params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
resources.getThemeName(), resources.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(), logWrapper);








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java
//Synthetic comment -- index 12bd57c..ca60640 100644

//Synthetic comment -- @@ -44,4 +44,11 @@
public Density getDensity() {
return Density.getEnum(mDensity.getDpiValue());
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java
//Synthetic comment -- index fb4b423..83b997a 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.rendering.api;

import com.android.resources.ScreenSize;


//Synthetic comment -- @@ -51,7 +52,7 @@
private final int mScreenWidth;
private final int mScreenHeight;
private final RenderingMode mRenderingMode;
    private final int mDensity;
private final float mXdpi;
private final float mYdpi;
private final RenderResources mRenderResources;
//Synthetic comment -- @@ -67,6 +68,9 @@
private IImageFactory mImageFactory = null;

private ScreenSize mConfigScreenSize = null;

/**
*
//Synthetic comment -- @@ -98,7 +102,7 @@
public Params(ILayoutPullParser layoutDescription,
Object projectKey,
int screenWidth, int screenHeight, RenderingMode renderingMode,
            int density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
//Synthetic comment -- @@ -142,6 +146,9 @@
mTimeout = params.mTimeout;
mImageFactory = params.mImageFactory;
mConfigScreenSize = params.mConfigScreenSize;
}

public void setOverrideBgColor(int color) {
//Synthetic comment -- @@ -161,6 +168,18 @@
mConfigScreenSize  = size;
}

public ILayoutPullParser getLayoutDescription() {
return mLayoutDescription;
}
//Synthetic comment -- @@ -189,7 +208,7 @@
return mRenderingMode;
}

    public int getDensity() {
return mDensity;
}

//Synthetic comment -- @@ -232,4 +251,16 @@
public ScreenSize getConfigScreenSize() {
return mConfigScreenSize;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java
//Synthetic comment -- index 76561c8..f15d903 100644

//Synthetic comment -- @@ -91,4 +91,12 @@
public void replaceWith(ResourceValue value) {
mValue = value.mValue;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 6ed6e49..2cb6ace 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
public final static String ATTRIBUTE_GLESVERSION = "glEsVersion";
public final static String ATTRIBUTE_PROCESS = "process";
public final static String ATTRIBUTE_DEBUGGABLE = "debuggable";
public final static String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
public final static String ATTRIBUTE_TARGET_SDK_VERSION = "targetSdkVersion";
public final static String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
//Synthetic comment -- @@ -76,6 +78,22 @@
public final static String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";

/**
* Returns the package for a given project.
* @param projectFolder the folder of the project.
* @return the package info or null (or empty) if not found.
//Synthetic comment -- @@ -84,8 +102,12 @@
*/
public static String getPackage(IAbstractFolder projectFolder)
throws XPathExpressionException, StreamException {
        IAbstractFile file = projectFolder.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);
        return getPackage(file);
}

/**
//Synthetic comment -- @@ -123,7 +145,8 @@
String value = xPath.evaluate(
"/"  + NODE_MANIFEST +
"/"  + NODE_APPLICATION +
                "/@" + ATTRIBUTE_DEBUGGABLE,
new InputSource(manifestFile.getContents()));

// default is not debuggable, which is the same behavior as parseBoolean
//Synthetic comment -- @@ -240,6 +263,43 @@
}
}


/**
* Combines a java package, with a class value from the manifest to make a fully qualified








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 9a67735..09e81f7 100644

//Synthetic comment -- @@ -632,7 +632,7 @@

public static ManifestData parse(IAbstractFolder projectFolder)
throws SAXException, IOException, StreamException, ParserConfigurationException {
        IAbstractFile manifestFile = getManifest(projectFolder);
if (manifestFile == null) {
throw new FileNotFoundException();
}
//Synthetic comment -- @@ -666,20 +666,4 @@

return null;
}

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
}







