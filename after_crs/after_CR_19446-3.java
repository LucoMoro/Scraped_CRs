/*Clean up the api around Layoutlib.

Move (Style/DensityBased)ResourceValue into layoutlib_api
and make the API use that instead of the interface.
We'll get ride of the interfaces once only obsolete platforms
use them.

In ide-commons also got rid of LayoutBridgeWrapper and moved
the code in LayoutLibrary which does not expose the bridge
anymore, and instead expose an API similar to the LayoutBridge
class.

Updated ADT to use LayoutLibrary directly instead of going through
LayoutLibrary.getBridge(). This allows us to hide some
things like querying the API level and relying instead on
Capabilities (with special handle for legacy bridges).

Also added an error message to LayoutLibrary to display why
it may have failed to load.
Added a check to the API level and don't load layoutlib
that are more recent than the client.

Change-Id:Ie4e615d8d32485ee577bb88e95cd3f562bf590cb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 1cad70a..a7cdf35 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.ResourceDensity;
import com.android.layoutlib.api.ViewInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -549,7 +549,7 @@
case COMPLEX_UNIT_DIP:
case COMPLEX_UNIT_SP: // intended fall-through since we don't
// adjust for font size
                            f *= (float)mDensityValue / ResourceDensity.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
f *= mXdpi * (1.0f / 72);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index ee6aeca..8286d3b 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.layoutlib.api.ResourceValue;
import com.android.layoutlib.api.StyleResourceValue;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.resources.Density;
import com.android.sdklib.resources.DockMode;
//Synthetic comment -- @@ -183,8 +183,8 @@
ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
ProjectResources getFrameworkResources(IAndroidTarget target);
        Map<String, Map<String, ResourceValue>> getConfiguredProjectResources();
        Map<String, Map<String, ResourceValue>> getConfiguredFrameworkResources();
}

/**
//Synthetic comment -- @@ -1221,17 +1221,17 @@
// get the themes, and languages from the Framework.
if (frameworkProject != null) {
// get the configured resources for the framework
                Map<String, Map<String, ResourceValue>> frameworResources =
frameworkProject.getConfiguredResources(getCurrentConfig());

if (frameworResources != null) {
// get the styles.
                    Map<String, ResourceValue> styles = frameworResources.get(
ResourceType.STYLE.getName());


// collect the themes out of all the styles.
                    for (ResourceValue value : styles.values()) {
String name = value.getName();
if (name.startsWith("Theme.") || name.equals("Theme")) {
themes.add(value.getName());
//Synthetic comment -- @@ -1256,18 +1256,18 @@
// in cases where the opened file is not linked to a project, this could be null.
if (project != null) {
// get the configured resources for the project
                Map<String, Map<String, ResourceValue>> configuredProjectRes =
mListener.getConfiguredProjectResources();

if (configuredProjectRes != null) {
// get the styles.
                    Map<String, ResourceValue> styleMap = configuredProjectRes.get(
ResourceType.STYLE.getName());

if (styleMap != null) {
// collect the themes out of all the styles, ie styles that extend,
// directly or indirectly a platform theme.
                        for (ResourceValue value : styleMap.values()) {
if (isTheme(value, styleMap)) {
themes.add(value.getName());
}
//Synthetic comment -- @@ -1901,9 +1901,9 @@
* @param styleMap the map of styles for the current project. Key is the style name.
* @return True if the given <var>style</var> is a theme.
*/
    private boolean isTheme(ResourceValue value, Map<String, ResourceValue> styleMap) {
        if (value instanceof StyleResourceValue) {
            StyleResourceValue style = (StyleResourceValue)value;

boolean frameworkStyle = false;
String parentStyle = style.getParentStyle();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ce21726..fd9c609 100644

//Synthetic comment -- @@ -47,11 +47,11 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.Capabilities;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutLog;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.ResourceValue;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.layoutlib.api.SceneResult.SceneStatus;
//Synthetic comment -- @@ -205,8 +205,8 @@
*/
private String mIncludedWithinId;

    private Map<String, Map<String, ResourceValue>> mConfiguredFrameworkRes;
    private Map<String, Map<String, ResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
private LayoutLog mLog;

//Synthetic comment -- @@ -623,7 +623,7 @@
preRenderingTargetChangeCleanUp(oldTarget);
}

        public Map<String, Map<String, ResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
ProjectResources frameworkRes = getFrameworkResources();

//Synthetic comment -- @@ -639,7 +639,7 @@
return mConfiguredFrameworkRes;
}

        public Map<String, Map<String, ResourceValue>> getConfiguredProjectResources() {
if (mConfiguredProjectRes == null && mConfigComposite != null) {
ProjectResources project = getProjectResources();

//Synthetic comment -- @@ -1023,7 +1023,7 @@
AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
if (targetData != null) {
LayoutLibrary layoutLib = targetData.getLayoutLibrary();
            setClippingSupport(layoutLib.supports(Capabilities.UNBOUND_RENDERING));
}

mConfigListener.onConfigurationChange();
//Synthetic comment -- @@ -1240,7 +1240,7 @@
if (data != null) {
LayoutLibrary layoutLib = data.getLayoutLibrary();

                    if (layoutLib.getStatus() == LoadStatus.LOADED) {
return layoutLib;
} else if (displayError) { // getBridge() == null
// SDK is loaded but not the layout library!
//Synthetic comment -- @@ -1250,7 +1250,9 @@
displayError("Eclipse is loading framework information and the layout library from the SDK folder.\n%1$s will refresh automatically once the process is finished.",
mEditedFile.getName());
} else {
                            String message = layoutLib.getLoadMessage();
                            displayError("Eclipse failed to load the framework information and the layout library!" +
                                    message != null ? "\n" + message : "");
}
}
} else { // data == null
//Synthetic comment -- @@ -1380,11 +1382,11 @@
}

// Get the resources of the file's project.
        Map<String, Map<String, ResourceValue>> configuredProjectRes =
mConfigListener.getConfiguredProjectResources();

// Get the framework resources
        Map<String, Map<String, ResourceValue>> frameworkResources =
mConfigListener.getConfiguredFrameworkResources();

// Abort the rendering if the resources are not found.
//Synthetic comment -- @@ -1479,9 +1481,9 @@
String contextLayoutName = mIncludedWithinId;
if (contextLayoutName != null) {
// Find the layout file.
            Map<String, ResourceValue> layouts = configuredProjectRes.get(
ResourceType.LAYOUT.getName());
            ResourceValue contextLayout = layouts.get(contextLayoutName);
if (contextLayout != null) {
String path = contextLayout.getValue();

//Synthetic comment -- @@ -1535,7 +1537,7 @@
// set the Image Overlay as the image factory.
params.setImageFactory(getCanvasControl().getImageOverlay());

        LayoutScene scene = layoutLib.createScene(params);

return scene;
}
//Synthetic comment -- @@ -1567,10 +1569,7 @@
LayoutLibrary layoutLib = data.getLayoutLibrary();

// layoutLib can never be null.
                layoutLib.clearCaches(mEditedFile.getProject());
}
}

//Synthetic comment -- @@ -1622,9 +1621,7 @@
// clear the cache in the bridge in case a bitmap/9-patch changed.
LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);
if (layoutLib != null) {
                    layoutLib.clearCaches(mEditedFile.getProject());
}
}

//Synthetic comment -- @@ -1735,16 +1732,16 @@
// There is code to handle this, but it's in layoutlib; we should
// expose that and use it here.

        Map<String, Map<String, ResourceValue>> map;
map = isFrameworkResource ? mConfiguredFrameworkRes : mConfiguredProjectRes;
if (map == null) {
// Not yet configured
return null;
}

        Map<String, ResourceValue> layoutMap = map.get(type.getName());
if (layoutMap != null) {
            ResourceValue value = layoutMap.get(name);
if (value != null) {
String valueStr = value.getValue();
if (valueStr.startsWith("?")) { //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 2bc9f34..cf624f8 100755

//Synthetic comment -- @@ -37,7 +37,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.layoutlib.api.Capabilities;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.ViewInfo;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -725,10 +725,7 @@
boolean hasTransparency = false;
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
if (layoutLibrary != null) {
                hasTransparency = layoutLibrary.supports(Capabilities.TRANSPARENCY);
}

LayoutScene scene = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index a96a4e8..3ad97e9 100644

//Synthetic comment -- @@ -16,11 +16,10 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.layoutlib.ValueResourceParser;
import com.android.ide.common.layoutlib.ValueResourceParser.IValueResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.layoutlib.api.ResourceValue;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;

//Synthetic comment -- @@ -160,7 +159,7 @@
}

@Override
    public ResourceValue getValue(ResourceType type, String name) {
update();

// get the list for the given type








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 36ce3b0..3795811 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
//Synthetic comment -- @@ -27,7 +26,7 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.layoutlib.api.ResourceValue;
import com.android.sdklib.io.IAbstractFolder;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -372,11 +371,11 @@
* Returns the resources values matching a given {@link FolderConfiguration}.
* @param referenceConfig the configuration that each value must match.
*/
    public Map<String, Map<String, ResourceValue>> getConfiguredResources(
FolderConfiguration referenceConfig) {

        Map<String, Map<String, ResourceValue>> map =
            new HashMap<String, Map<String, ResourceValue>>();

// if the project contains libraries, we need to add the libraries resources here
// so that they are accessible to the layout rendering.
//Synthetic comment -- @@ -401,12 +400,12 @@

// we don't want to simply replace the whole map, but instead merge the
// content of any sub-map
                        Map<String, Map<String, ResourceValue>> libMap =
libRes.getConfiguredResources(referenceConfig);

                        for (Entry<String, Map<String, ResourceValue>> entry : libMap.entrySet()) {
// get the map currently in the result map for this resource type
                            Map<String, ResourceValue> tempMap = map.get(entry.getKey());
if (tempMap == null) {
// since there's no current map for this type, just add the map
// directly coming from the library resources
//Synthetic comment -- @@ -430,10 +429,10 @@
// in the XML files.
if (mIdResourceList.size() > 0) {
String idType = ResourceType.ID.getName();
            Map<String, ResourceValue> idMap = map.get(idType);

if (idMap == null) {
                idMap = new HashMap<String, ResourceValue>();
map.put(idType, idMap);
}
for (IdResourceItem id : mIdResourceList) {
//Synthetic comment -- @@ -449,12 +448,12 @@
// we don't process ID resources since we already did it above.
if (key != ResourceType.ID) {
// get the local results
                Map<String, ResourceValue> localResMap = getConfiguredResource(key,
referenceConfig);

// check if a map for this type already exists
String resName = key.getName();
                Map<String, ResourceValue> resMap = map.get(resName);
if (resMap == null) {
// just use the local results.
map.put(resName, localResMap);
//Synthetic comment -- @@ -601,13 +600,13 @@
* @param type the type of the resources.
* @param referenceConfig the configuration to best match.
*/
    private Map<String, ResourceValue> getConfiguredResource(ResourceType type,
FolderConfiguration referenceConfig) {
// get the resource item for the given type
List<ProjectResourceItem> items = mResourceMap.get(type);

// create the map
        HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>();

for (ProjectResourceItem item : items) {
// get the source files generating this resource
//Synthetic comment -- @@ -620,7 +619,7 @@
ResourceFile matchResFile = (ResourceFile)match;

// get the value of this configured resource.
                ResourceValue value = matchResFile.getValue(type, item.getName());

if (value != null) {
map.put(item.getName(), value);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index 3a349be..1cba12e 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.layoutlib.api.ResourceValue;
import com.android.sdklib.io.IAbstractFile;

import java.util.Collection;
//Synthetic comment -- @@ -95,7 +95,7 @@
* @param type the type of the resource.
* @param name the name of the resource.
*/
    public abstract ResourceValue getValue(ResourceType type, String name);

@Override
public String toString() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 637d20b..2bd9406 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.PixelDensityQualifier;
import com.android.layoutlib.api.DensityBasedResourceValue;
import com.android.layoutlib.api.ResourceDensity;
import com.android.layoutlib.api.ResourceValue;
import com.android.sdklib.io.IAbstractFile;

import java.util.ArrayList;
//Synthetic comment -- @@ -56,7 +55,7 @@

private String mResourceName;
private ResourceType mType;
    private ResourceValue mValue;

public SingleResourceFile(IAbstractFile file, ResourceFolder folder) {
super(file, folder);
//Synthetic comment -- @@ -80,7 +79,7 @@
mType.getName(),
getResourceName(mType),
file.getOsLocation(),
                    ResourceDensity.getEnum(qualifier.getValue().getDpiValue()),
isFramework());
}
}
//Synthetic comment -- @@ -124,7 +123,7 @@
* The value returned is the full absolute path of the file in OS form.
*/
@Override
    public ResourceValue getValue(ResourceType type, String name) {
return mValue;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 3667d3f..a539513 100644

//Synthetic comment -- @@ -247,11 +247,11 @@
* <p/>Valid {@link LayoutBridge} objects are always initialized before being returned.
*/
public synchronized LayoutLibrary getLayoutLibrary() {
        if (mLayoutBridgeInit == false && mLayoutLibrary.getStatus() == LoadStatus.LOADED) {
            mLayoutLibrary.init(mTarget.getPath(IAndroidTarget.FONTS), getEnumValueMap());
mLayoutBridgeInit = true;
}

return mLayoutLibrary;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index e75b846..27e8743 100644

//Synthetic comment -- @@ -767,7 +767,7 @@
if (data != null) {
LayoutLibrary layoutLib = data.getLayoutLibrary();
if (layoutLib != null && layoutLib.getStatus() == LoadStatus.LOADED) {
                                layoutLib.clearCaches(project);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 53a36fb..dd3393d 100644

//Synthetic comment -- @@ -33,9 +33,9 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.ResourceValue;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -154,8 +154,8 @@
}

LayoutLibrary layoutLib = data.getLayoutLibrary();
        if (layoutLib.getStatus() != LoadStatus.LOADED) {
            fail("Fail to load the bridge: " + layoutLib.getLoadMessage());
}

FolderWrapper resFolder = new FolderWrapper(sampleProject, SdkConstants.FD_RES);
//Synthetic comment -- @@ -180,9 +180,9 @@
FolderConfiguration config = getConfiguration();

// get the configured resources
        Map<String, Map<String, ResourceValue>> configuredFramework =
framework.getConfiguredResources(config);
        Map<String, Map<String, ResourceValue>> configuredProject =
project.getConfiguredResources(config);

boolean saveFiles = System.getenv("save_file") != null;
//Synthetic comment -- @@ -199,7 +199,7 @@

ProjectCallBack projectCallBack = new ProjectCallBack();

            LayoutScene scene = layoutLib.createScene(new SceneParams(
parser,
null /*projectKey*/,
320,








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
deleted file mode 100644
//Synthetic comment -- index 1d4ff62..0000000

//Synthetic comment -- @@ -1,223 +0,0 @@








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutLibrary.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutLibrary.java
//Synthetic comment -- index 5ec7ae5..b66c27b 100644

//Synthetic comment -- @@ -18,14 +18,29 @@

import com.android.ide.common.log.ILogger;
import com.android.ide.common.sdk.LoadStatus;
import com.android.layoutlib.api.Capabilities;
import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutLog;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.layoutlib.api.SceneResult.SceneStatus;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Map;

/**
* Class representing and allowing to load the layoutlib jar file.
//Synthetic comment -- @@ -37,25 +52,31 @@

/** Link to the layout bridge */
private final LayoutBridge mBridge;
    /** Link to a ILayoutBridge in case loaded an older library */
    private final ILayoutBridge mLegacyBridge;
/** Status of the layoutlib.jar loading */
private final LoadStatus mStatus;
    /** Message associated with the {@link LoadStatus}. This is mostly used when
     * {@link #getStatus()} returns {@link LoadStatus#FAILED}.
     */
    private final String mLoadMessage;
/** classloader used to load the jar file */
private final ClassLoader mClassLoader;

/**
* Returns the {@link LoadStatus} of the loading of the layoutlib jar file.
*/
public LoadStatus getStatus() {
return mStatus;
}

    /** Returns the message associated with the {@link LoadStatus}. This is mostly used when
     * {@link #getStatus()} returns {@link LoadStatus#FAILED}.
     */
    public String getLoadMessage() {
        return mLoadMessage;
    }

/**
* Returns the classloader used to load the classes in the layoutlib jar file.
*/
//Synthetic comment -- @@ -77,7 +98,9 @@
public static LayoutLibrary load(String layoutLibJarOsPath, ILogger log) {

LoadStatus status = LoadStatus.LOADING;
        String message = null;
LayoutBridge bridge = null;
        ILayoutBridge legacyBridge = null;
ClassLoader classLoader = null;

try {
//Synthetic comment -- @@ -108,36 +131,323 @@
if (bridgeObject instanceof LayoutBridge) {
bridge = (LayoutBridge)bridgeObject;
} else if (bridgeObject instanceof ILayoutBridge) {
                            legacyBridge = (ILayoutBridge) bridgeObject;
}
}
}

                if (bridge == null && legacyBridge == null) {
status = LoadStatus.FAILED;
                    message = "Failed to load " + CLASS_BRIDGE; //$NON-NLS-1$
if (log != null) {
                        log.error(null,
                                "Failed to load " + //$NON-NLS-1$
                                CLASS_BRIDGE +
                                " from " +          //$NON-NLS-1$
                                layoutLibJarOsPath);
}
} else {
                    // mark the lib as loaded, unless it's overridden below.
status = LoadStatus.LOADED;

                    // check the API, only if it's not a legacy bridge
                    if (bridge != null) {
                        int api = bridge.getApiLevel();
                        if (api > LayoutBridge.API_CURRENT) {
                            status = LoadStatus.FAILED;
                            message = "LayoutLib is too recent. Update your tool!";
                        }
                    }
}
}
} catch (Throwable t) {
status = LoadStatus.FAILED;
            Throwable cause = t;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            message = "Failed to load the LayoutLib: " + cause.getMessage();
// log the error.
if (log != null) {
                log.error(t, message);
}
}

        return new LayoutLibrary(bridge, legacyBridge, classLoader, status, message);
}

    // ------ Layout Lib API proxy

    /**
     * Returns whether the LayoutLibrary supports a given {@link Capabilities}.
     * @return true if it supports it.
     *
     * @see LayoutBridge#getCapabilities()
     *
     */
    public boolean supports(Capabilities capability) {
        if (mBridge != null) {
            return mBridge.getCapabilities().contains(capability);
        }

        if (mLegacyBridge != null) {
            switch (capability) {
                case UNBOUND_RENDERING:
                    // legacy stops at 4. 5 is new API.
                    return getLegacyApiLevel() == 4;
            }
        }

        return false;
    }

    /**
     * Initializes the Bridge object.
     *
     * @param fontOsLocation the location of the fonts.
     * @param enumValueMap map attrName => { map enumFlagName => Integer value }.
     * @return true if success.
     *
     * @see LayoutBridge#init(String, Map)
     */
    public boolean init(String fontOsLocation, Map<String, Map<String, Integer>> enumValueMap) {
        if (mBridge != null) {
            return mBridge.init(fontOsLocation, enumValueMap);
        } else if (mLegacyBridge != null) {
            return mLegacyBridge.init(fontOsLocation, enumValueMap);
        }

        return false;
    }

    /**
     * Prepares the layoutlib to unloaded.
     *
     * @see LayoutBridge#dispose()
     */
    public boolean dispose() {
        if (mBridge != null) {
            return mBridge.dispose();
        }

        return true;
    }

    /**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link LayoutScene} on which further actions can be taken.
     *
     * @return a new {@link ILayoutScene} object that contains the result of the scene creation and
     * first rendering or null if {@link #getStatus()} doesn't return {@link LoadStatus#LOADED}.
     *
     * @see LayoutBridge#createScene(SceneParams)
     */
    public LayoutScene createScene(SceneParams params) {
        if (mBridge != null) {
            mBridge.createScene(params);
        } else if (mLegacyBridge != null) {
            return createLegacyScene(params);
        }

        return null;
    }

    /**
     * Clears the resource cache for a specific project.
     * <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
     * until this method is called.
     * <p/>The cache is not configuration dependent and should only be cleared when a
     * resource changes (at this time only bitmaps and 9 patches go into the cache).
     *
     * @param projectKey the key for the project.
     *
     * @see LayoutBridge#clearCaches(Object)
     */
    public void clearCaches(Object projectKey) {
        if (mBridge != null) {
            mBridge.clearCaches(projectKey);
        } else if (mLegacyBridge != null) {
            mLegacyBridge.clearCaches(projectKey);
        }

    }

    // ------ Implementation

    private LayoutLibrary(LayoutBridge bridge, ILayoutBridge legacyBridge, ClassLoader classLoader,
            LoadStatus status, String message) {
mBridge = bridge;
        mLegacyBridge = legacyBridge;
mClassLoader = classLoader;
mStatus = status;
        mLoadMessage = message;
    }

    /**
     * Returns the API level of the legacy bridge.
     * <p/>
     * This handles the case where ILayoutBridge does not have a {@link ILayoutBridge#getApiLevel()}
     * (at API level 1).
     * <p/>
     * {@link ILayoutBridge#getApiLevel()} should never called directly.
     *
     * @return the api level of {@link #mLegacyBridge}.
     */
    private int getLegacyApiLevel() {
        int apiLevel = 1;
        try {
            apiLevel = mLegacyBridge.getApiLevel();
        } catch (AbstractMethodError e) {
            // the first version of the api did not have this method
            // so this is 1
        }

        return apiLevel;
    }

    private LayoutScene createLegacyScene(SceneParams params) {
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

        // convert the map of ResourceValue into IResourceValue. Super ugly but works.
        @SuppressWarnings("unchecked")
        Map<String, Map<String, IResourceValue>> projectMap =
            (Map<String, Map<String, IResourceValue>>)(Map) params.getProjectResources();
        @SuppressWarnings("unchecked")
        Map<String, Map<String, IResourceValue>> frameworkMap =
            (Map<String, Map<String, IResourceValue>>)(Map) params.getFrameworkResources();

        ILayoutResult result = null;

        if (apiLevel == 4) {
            // Final ILayoutBridge API added support for "render full height"
            result = mLegacyBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    projectMap, frameworkMap,
                    params.getProjectCallback(), logWrapper);
        } else if (apiLevel == 3) {
            // api 3 add density support.
            result = mLegacyBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    projectMap, frameworkMap,
                    params.getProjectCallback(), logWrapper);
        } else if (apiLevel == 2) {
            // api 2 added boolean for separation of project/framework theme
            result = mLegacyBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    projectMap, frameworkMap,
                    params.getProjectCallback(), logWrapper);
        } else {
            // First api with no density/dpi, and project theme boolean mixed
            // into the theme name.

            // change the string if it's a custom theme to make sure we can
            // differentiate them
            String themeName = params.getThemeName();
            if (params.getIsProjectTheme()) {
                themeName = "*" + themeName; //$NON-NLS-1$
            }

            result = mLegacyBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    themeName,
                    projectMap, frameworkMap,
                    params.getProjectCallback(), logWrapper);
        }

        // clean up that is not done by the ILayoutBridge itself
        cleanUp();

        return convertToScene(result);
    }

    /**
     * Converts a {@link ILayoutResult} to a {@link LayoutScene}.
     */
    private LayoutScene convertToScene(ILayoutResult result) {

        SceneResult sceneResult;
        ViewInfo rootViewInfo;

        if (result.getSuccess() == ILayoutResult.SUCCESS) {
            sceneResult = SceneStatus.SUCCESS.createResult();
            rootViewInfo = convertToViewInfo(result.getRootView());
        } else {
            sceneResult = SceneStatus.ERROR_UNKNOWN.createResult(result.getErrorMessage());
            rootViewInfo = null;
        }

        // create a BasicLayoutScene. This will return the given values but return the default
        // implementation for all method.
        // ADT should gracefully handle the default implementations of LayoutScene
        return new BasicLayoutScene(sceneResult, rootViewInfo, result.getImage());
    }

    /**
     * Converts a {@link ILayoutViewInfo} (and its children) to a {@link ViewInfo}.
     */
    private ViewInfo convertToViewInfo(ILayoutViewInfo view) {
        // create the view info.
        ViewInfo viewInfo = new ViewInfo(view.getName(), view.getViewKey(),
                view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        // then convert the children
        ILayoutViewInfo[] children = view.getChildren();
        if (children != null) {
            ArrayList<ViewInfo> convertedChildren = new ArrayList<ViewInfo>(children.length);
            for (ILayoutViewInfo child : children) {
                convertedChildren.add(convertToViewInfo(child));
            }
            viewInfo.setChildren(convertedChildren);
        }

        return viewInfo;
    }

    /**
     * Post rendering clean-up that must be done here because it's not done in any layoutlib using
     * {@link ILayoutBridge}.
     */
    private void cleanUp() {
        try {
            Class<?> looperClass = mClassLoader.loadClass("android.os.Looper"); //$NON-NLS-1$
            Field threadLocalField = looperClass.getField("sThreadLocal"); //$NON-NLS-1$
            if (threadLocalField != null) {
                threadLocalField.setAccessible(true);
                // get object. Field is static so no need to pass an object
                ThreadLocal<?> threadLocal = (ThreadLocal<?>) threadLocalField.get(null);
                if (threadLocal != null) {
                    threadLocal.remove();
                }
            }
        } catch (Exception e) {
            // do nothing.
        }
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/ValueResourceParser.java b/ide_common/src/com/android/ide/common/layoutlib/ValueResourceParser.java
//Synthetic comment -- index c784f1f..cc65b9d 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.ResourceValue;
import com.android.layoutlib.api.StyleResourceValue;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
//Synthetic comment -- @@ -31,25 +34,25 @@
private final static String ATTR_NAME = "name";
private final static String ATTR_TYPE = "type";
private final static String ATTR_PARENT = "parent";

// Resource type definition
private final static String RES_STYLE = "style";
private final static String RES_ATTR = "attr";

private final static String DEFAULT_NS_PREFIX = "android:";
private final static int DEFAULT_NS_PREFIX_LEN = DEFAULT_NS_PREFIX.length();

public interface IValueResourceRepository {
void addResourceValue(String resType, ResourceValue value);
}

private boolean inResources = false;
private int mDepth = 0;
private StyleResourceValue mCurrentStyle = null;
private ResourceValue mCurrentValue = null;
private IValueResourceRepository mRepository;
private final boolean mIsFramework;

public ValueResourceParser(IValueResourceRepository repository, boolean isFramework) {
mRepository = repository;
mIsFramework = isFramework;
//Synthetic comment -- @@ -60,7 +63,7 @@
if (mCurrentValue != null) {
mCurrentValue.setValue(trimXmlWhitespaces(mCurrentValue.getValue()));
}

if (inResources && qName.equals(NODE_RESOURCES)) {
inResources = false;
} else if (mDepth == 2) {
//Synthetic comment -- @@ -69,7 +72,7 @@
} else if (mDepth == 3) {
mCurrentValue = null;
}

mDepth--;
super.endElement(uri, localName, qName);
}
//Synthetic comment -- @@ -85,7 +88,7 @@
}
} else if (mDepth == 2 && inResources == true) {
String type;

// if the node is <item>, we get the type from the attribute "type"
if (NODE_ITEM.equals(qName)) {
type = attributes.getValue(ATTR_TYPE);
//Synthetic comment -- @@ -118,16 +121,16 @@
if (name.startsWith(DEFAULT_NS_PREFIX)) {
name = name.substring(DEFAULT_NS_PREFIX_LEN);
}

mCurrentValue = new ResourceValue(null, name, mIsFramework);
                    mCurrentStyle.addValue(mCurrentValue);
}
}
} finally {
super.startElement(uri, localName, qName, attributes);
}
}

@Override
public void characters(char[] ch, int start, int length) throws SAXException {
if (mCurrentValue != null) {
//Synthetic comment -- @@ -139,7 +142,7 @@
}
}
}

public static String trimXmlWhitespaces(String value) {
if (value == null) {
return null;
//Synthetic comment -- @@ -147,7 +150,7 @@

// look for carriage return and replace all whitespace around it by just 1 space.
int index;

while ((index = value.indexOf('\n')) != -1) {
// look for whitespace on each side
int left = index - 1;
//Synthetic comment -- @@ -158,7 +161,7 @@
break;
}
}

int right = index + 1;
int count = value.length();
while (right < count) {
//Synthetic comment -- @@ -168,7 +171,7 @@
break;
}
}

// remove all between left and right (non inclusive) and replace by a single space.
String leftString = null;
if (left >= 0) {
//Synthetic comment -- @@ -178,7 +181,7 @@
if (right < count) {
rightString = value.substring(right);
}

if (leftString != null) {
value = leftString;
if (rightString != null) {
//Synthetic comment -- @@ -188,21 +191,21 @@
value = rightString != null ? rightString : "";
}
}

// now we un-escape the string
int length = value.length();
char[] buffer = value.toCharArray();

for (int i = 0 ; i < length ; i++) {
if (buffer[i] == '\\' && i + 1 < length) {
if (buffer[i+1] == 'u') {
if (i + 5 < length) {
// this is unicode char \u1234
int unicodeChar = Integer.parseInt(new String(buffer, i+2, 4), 16);

// put the unicode char at the location of the \
buffer[i] = (char)unicodeChar;

// offset the rest of the buffer since we go from 6 to 1 char
if (i + 6 < buffer.length) {
System.arraycopy(buffer, i+6, buffer, i+1, length - i - 6);
//Synthetic comment -- @@ -214,14 +217,14 @@
// replace the 'n' char with \n
buffer[i+1] = '\n';
}

// offset the buffer to erase the \
System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
length--;
}
}
}

return new String(buffer, 0, length);
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java b/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java
//Synthetic comment -- index 0edc764..ab2f358 100644

//Synthetic comment -- @@ -23,6 +23,10 @@
*
*/
public enum Capabilities {
    /** Ability to render at full size, as required by the layout, and unbound by the screen */
    UNBOUND_RENDERING,
    /** Ability to override the background of the rendering with transparency. */
    TRANSPARENCY,
/** Ability to call {@link LayoutScene#render()} and {@link LayoutScene#render(long)}. */
RENDER,
/** Ability to call<br>








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/DensityBasedResourceValue.java
similarity index 65%
rename from ide_common/src/com/android/ide/common/layoutlib/DensityBasedResourceValue.java
rename to layoutlib_api/src/com/android/layoutlib/api/DensityBasedResourceValue.java
//Synthetic comment -- index e1c0caa..78b084c 100644

//Synthetic comment -- @@ -14,21 +14,31 @@
* limitations under the License.
*/

package com.android.layoutlib.api;

@SuppressWarnings("deprecation")
public class DensityBasedResourceValue extends ResourceValue implements IDensityBasedResourceValue {

    private ResourceDensity mDensity;

    public DensityBasedResourceValue(String type, String name, String value,
            ResourceDensity density, boolean isFramework) {
super(type, name, value, isFramework);
mDensity = density;
}

    /**
     * Returns the density for which this resource is configured.
     * @return the density.
     */
    public ResourceDensity getResourceDensity() {
return mDensity;
}

    /** Legacy method, do not call
     * @deprecated use {@link #getResourceDensity()} instead.
     */
    public Density getDensity() {
        return Density.getEnum(mDensity.getDpi());
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IDensityBasedResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/IDensityBasedResourceValue.java
//Synthetic comment -- index e969034..fbea9ef 100644

//Synthetic comment -- @@ -18,9 +18,15 @@

/**
* Represents an Android Resources that has a density info attached to it.
 * @deprecated use {@link DensityBasedResourceValue}.
*/
public interface IDensityBasedResourceValue extends IResourceValue {

    /**
     * Density.
     *
     * @deprecated use {@link ResourceDensity}.
     */
public static enum Density {
XHIGH(320),
HIGH(240),
//Synthetic comment -- @@ -28,8 +34,6 @@
LOW(120),
NODPI(0);

private final int mValue;

Density(int value) {
//Synthetic comment -- @@ -58,6 +62,7 @@

/**
* Returns the density associated to the resource.
     * @deprecated use {@link DensityBasedResourceValue#getResourceDensity()}
*/
Density getDensity();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/IResourceValue.java
//Synthetic comment -- index 1da9508..56d4f07 100644

//Synthetic comment -- @@ -18,9 +18,10 @@

/**
* Represents an android resource with a name and a string value.
 * @deprecated use {@link ResourceValue}.
*/
public interface IResourceValue {

/**
* Returns the type of the resource. For instance "drawable", "color", etc...
*/
//Synthetic comment -- @@ -35,7 +36,7 @@
* Returns the value of the resource, as defined in the XML. This can be <code>null</code>
*/
String getValue();

/**
* Returns whether the resource is a framework resource (<code>true</code>) or a project
* resource (<code>false</false>).








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IStyleResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/IStyleResourceValue.java
//Synthetic comment -- index 2f17e69..acdb388 100644

//Synthetic comment -- @@ -19,9 +19,10 @@

/**
* Represents an android style resources with a name and a list of children {@link IResourceValue}.
 * @deprecated USe {@link StyleResourceValue}.
*/
public interface IStyleResourceValue extends IResourceValue {

/**
* Returns the parent style name or <code>null</code> if unknown.
*/
//Synthetic comment -- @@ -29,7 +30,9 @@

/**
* Find an item in the list by name
     * @param name the name of the resource
     *
     * @deprecated use {@link StyleResourceValue#findValue(String)}
*/
IResourceValue findItem(String name);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ResourceDensity.java b/layoutlib_api/src/com/android/layoutlib/api/ResourceDensity.java
new file mode 100644
//Synthetic comment -- index 0000000..bfbf536

//Synthetic comment -- @@ -0,0 +1,59 @@
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

/**
 * Enum representing the density class of Android resources.
 */
public enum ResourceDensity {
    XHIGH(320),
    HIGH(240),
    MEDIUM(160),
    LOW(120),
    NODPI(0);

    public final static int DEFAULT_DENSITY = 160;

    private final int mDpi;

    ResourceDensity(int dpi) {
        mDpi = dpi;
    }

    /**
     * Returns the DPI associated with the density.
     * @return
     */
    public int getDpi() {
        return mDpi;
    }

    /**
     * Returns the enum matching the given dpi.
     * @param dpi The dpi
     * @return the enum for the dpi or null if no match was found.
     */
    public static ResourceDensity getEnum(int dpi) {
        for (ResourceDensity d : values()) {
            if (d.mDpi == dpi) {
                return d;
            }
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/ResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/ResourceValue.java
similarity index 63%
rename from ide_common/src/com/android/ide/common/layoutlib/ResourceValue.java
rename to layoutlib_api/src/com/android/layoutlib/api/ResourceValue.java
//Synthetic comment -- index 382d961..41f440d 100644

//Synthetic comment -- @@ -14,16 +14,18 @@
* limitations under the License.
*/

package com.android.layoutlib.api;

/**
 * Represents an android resource with a name and a string value.
 */
@SuppressWarnings("deprecation")
public class ResourceValue implements IResourceValue {
private final String mType;
private final String mName;
private String mValue = null;
private final boolean mIsFramwork;

public ResourceValue(String type, String name, boolean isFramwork) {
mType = type;
mName = name;
//Synthetic comment -- @@ -37,27 +39,49 @@
mIsFramwork = isFramework;
}

    /**
     * Returns the type of the resource. For instance "drawable", "color", etc...
     */
public String getType() {
return mType;
}

    /**
     * Returns the name of the resource, as defined in the XML.
     */
public final String getName() {
return mName;
}

    /**
     * Returns the value of the resource, as defined in the XML. This can be <code>null</code>
     */
public final String getValue() {
return mValue;
}

    /**
     * Returns whether the resource is a framework resource (<code>true</code>) or a project
     * resource (<code>false</false>).
     */
    public final boolean isFramework() {
        return mIsFramwork;
}


    /**
     * Sets the value of the resource.
     * @param value the new value
     */
    public void setValue(String value) {
        mValue = value;
    }

    /**
     * Sets the value from another resource.
     * @param value the resource value
     */
    public void replaceWith(ResourceValue value) {
        mValue = value.mValue;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 56014f0..309f9fd 100644

//Synthetic comment -- @@ -55,8 +55,8 @@
private float mYdpi;
private String mThemeName;
private boolean mIsProjectTheme;
    private Map<String, Map<String, ResourceValue>> mProjectResources;
    private Map<String, Map<String, ResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
private LayoutLog mLog;

//Synthetic comment -- @@ -96,8 +96,8 @@
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
String themeName, boolean isProjectTheme,
            Map<String, Map<String, ResourceValue>> projectResources,
            Map<String, Map<String, ResourceValue>> frameworkResources,
IProjectCallback projectCallback, LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
//Synthetic comment -- @@ -194,11 +194,11 @@
return mIsProjectTheme;
}

    public Map<String, Map<String, ResourceValue>> getProjectResources() {
return mProjectResources;
}

    public Map<String, Map<String, ResourceValue>> getFrameworkResources() {
return mFrameworkResources;
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/StyleResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/StyleResourceValue.java
similarity index 78%
rename from ide_common/src/com/android/ide/common/layoutlib/StyleResourceValue.java
rename to layoutlib_api/src/com/android/layoutlib/api/StyleResourceValue.java
//Synthetic comment -- index 721b16c..55618c1 100644

//Synthetic comment -- @@ -14,17 +14,15 @@
* limitations under the License.
*/

package com.android.layoutlib.api;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public final class StyleResourceValue extends ResourceValue implements IStyleResourceValue {

private String mParentStyle = null;
    private HashMap<String, ResourceValue> mItems = new HashMap<String, ResourceValue>();

public StyleResourceValue(String type, String name, boolean isFramework) {
super(type, name, isFramework);
//Synthetic comment -- @@ -38,23 +36,32 @@
public String getParentStyle() {
return mParentStyle;
}

    public ResourceValue findValue(String name) {
return mItems.get(name);
}

    public void addValue(ResourceValue value) {
mItems.put(value.getName(), value);
}

@Override
public void replaceWith(ResourceValue value) {
        assert value instanceof StyleResourceValue;
super.replaceWith(value);

if (value instanceof StyleResourceValue) {
mItems.clear();
mItems.putAll(((StyleResourceValue)value).mItems);
}
}

    /**
     * Legacy method.
     * @deprecated use {@link #getValue()}
     */
    public IResourceValue findItem(String name) {
        return mItems.get(name);
    }

}







