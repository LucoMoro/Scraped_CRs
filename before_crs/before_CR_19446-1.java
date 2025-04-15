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
LayoutLibrary.getBridge().

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
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.IDensityBasedResourceValue.Density;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -549,7 +549,7 @@
case COMPLEX_UNIT_DIP:
case COMPLEX_UNIT_SP: // intended fall-through since we don't
// adjust for font size
                            f *= (float)mDensityValue / Density.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
f *= mXdpi * (1.0f / 72);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index ee6aeca..8286d3b 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.resources.Density;
import com.android.sdklib.resources.DockMode;
//Synthetic comment -- @@ -183,8 +183,8 @@
ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
ProjectResources getFrameworkResources(IAndroidTarget target);
        Map<String, Map<String, IResourceValue>> getConfiguredProjectResources();
        Map<String, Map<String, IResourceValue>> getConfiguredFrameworkResources();
}

/**
//Synthetic comment -- @@ -1221,17 +1221,17 @@
// get the themes, and languages from the Framework.
if (frameworkProject != null) {
// get the configured resources for the framework
                Map<String, Map<String, IResourceValue>> frameworResources =
frameworkProject.getConfiguredResources(getCurrentConfig());

if (frameworResources != null) {
// get the styles.
                    Map<String, IResourceValue> styles = frameworResources.get(
ResourceType.STYLE.getName());


// collect the themes out of all the styles.
                    for (IResourceValue value : styles.values()) {
String name = value.getName();
if (name.startsWith("Theme.") || name.equals("Theme")) {
themes.add(value.getName());
//Synthetic comment -- @@ -1256,18 +1256,18 @@
// in cases where the opened file is not linked to a project, this could be null.
if (project != null) {
// get the configured resources for the project
                Map<String, Map<String, IResourceValue>> configuredProjectRes =
mListener.getConfiguredProjectResources();

if (configuredProjectRes != null) {
// get the styles.
                    Map<String, IResourceValue> styleMap = configuredProjectRes.get(
ResourceType.STYLE.getName());

if (styleMap != null) {
// collect the themes out of all the styles, ie styles that extend,
// directly or indirectly a platform theme.
                        for (IResourceValue value : styleMap.values()) {
if (isTheme(value, styleMap)) {
themes.add(value.getName());
}
//Synthetic comment -- @@ -1901,9 +1901,9 @@
* @param styleMap the map of styles for the current project. Key is the style name.
* @return True if the given <var>style</var> is a theme.
*/
    private boolean isTheme(IResourceValue value, Map<String, IResourceValue> styleMap) {
        if (value instanceof IStyleResourceValue) {
            IStyleResourceValue style = (IStyleResourceValue)value;

boolean frameworkStyle = false;
String parentStyle = style.getParentStyle();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ce21726..d12b7d6 100644

//Synthetic comment -- @@ -47,11 +47,10 @@
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
import com.android.layoutlib.api.SceneResult.SceneStatus;
//Synthetic comment -- @@ -205,8 +204,8 @@
*/
private String mIncludedWithinId;

    private Map<String, Map<String, IResourceValue>> mConfiguredFrameworkRes;
    private Map<String, Map<String, IResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
private LayoutLog mLog;

//Synthetic comment -- @@ -623,7 +622,7 @@
preRenderingTargetChangeCleanUp(oldTarget);
}

        public Map<String, Map<String, IResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
ProjectResources frameworkRes = getFrameworkResources();

//Synthetic comment -- @@ -639,7 +638,7 @@
return mConfiguredFrameworkRes;
}

        public Map<String, Map<String, IResourceValue>> getConfiguredProjectResources() {
if (mConfiguredProjectRes == null && mConfigComposite != null) {
ProjectResources project = getProjectResources();

//Synthetic comment -- @@ -1023,7 +1022,7 @@
AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
if (targetData != null) {
LayoutLibrary layoutLib = targetData.getLayoutLibrary();
            setClippingSupport(layoutLib.getBridge().getApiLevel() >= 4);
}

mConfigListener.onConfigurationChange();
//Synthetic comment -- @@ -1240,7 +1239,7 @@
if (data != null) {
LayoutLibrary layoutLib = data.getLayoutLibrary();

                    if (layoutLib.getBridge() != null) { // layoutLib can never be null.
return layoutLib;
} else if (displayError) { // getBridge() == null
// SDK is loaded but not the layout library!
//Synthetic comment -- @@ -1250,7 +1249,9 @@
displayError("Eclipse is loading framework information and the layout library from the SDK folder.\n%1$s will refresh automatically once the process is finished.",
mEditedFile.getName());
} else {
                            displayError("Eclipse failed to load the framework information and the layout library!");
}
}
} else { // data == null
//Synthetic comment -- @@ -1380,11 +1381,11 @@
}

// Get the resources of the file's project.
        Map<String, Map<String, IResourceValue>> configuredProjectRes =
mConfigListener.getConfiguredProjectResources();

// Get the framework resources
        Map<String, Map<String, IResourceValue>> frameworkResources =
mConfigListener.getConfiguredFrameworkResources();

// Abort the rendering if the resources are not found.
//Synthetic comment -- @@ -1479,9 +1480,9 @@
String contextLayoutName = mIncludedWithinId;
if (contextLayoutName != null) {
// Find the layout file.
            Map<String, IResourceValue> layouts = configuredProjectRes.get(
ResourceType.LAYOUT.getName());
            IResourceValue contextLayout = layouts.get(contextLayoutName);
if (contextLayout != null) {
String path = contextLayout.getValue();

//Synthetic comment -- @@ -1535,7 +1536,7 @@
// set the Image Overlay as the image factory.
params.setImageFactory(getCanvasControl().getImageOverlay());

        LayoutScene scene = layoutLib.getBridge().createScene(params);

return scene;
}
//Synthetic comment -- @@ -1567,10 +1568,7 @@
LayoutLibrary layoutLib = data.getLayoutLibrary();

// layoutLib can never be null.
                LayoutBridge bridge = layoutLib.getBridge();
                if (bridge != null) {
                    bridge.clearCaches(mEditedFile.getProject());
                }
}
}

//Synthetic comment -- @@ -1622,9 +1620,7 @@
// clear the cache in the bridge in case a bitmap/9-patch changed.
LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);
if (layoutLib != null) {
                    if (layoutLib.getBridge() != null) {
                        layoutLib.getBridge().clearCaches(mEditedFile.getProject());
                    }
}
}

//Synthetic comment -- @@ -1735,16 +1731,16 @@
// There is code to handle this, but it's in layoutlib; we should
// expose that and use it here.

        Map<String, Map<String, IResourceValue>> map;
map = isFrameworkResource ? mConfiguredFrameworkRes : mConfiguredProjectRes;
if (map == null) {
// Not yet configured
return null;
}

        Map<String, IResourceValue> layoutMap = map.get(type.getName());
if (layoutMap != null) {
            IResourceValue value = layoutMap.get(name);
if (value != null) {
String valueStr = value.getValue();
if (valueStr.startsWith("?")) { //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 2bc9f34..46ce8df 100755

//Synthetic comment -- @@ -37,7 +37,6 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.ViewInfo;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -725,10 +724,7 @@
boolean hasTransparency = false;
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
if (layoutLibrary != null) {
                LayoutBridge bridge = layoutLibrary.getBridge();
                if (bridge != null) {
                    hasTransparency = bridge.getApiLevel() >= 5;
                }
}

LayoutScene scene = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index a96a4e8..3ad97e9 100644

//Synthetic comment -- @@ -16,11 +16,10 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.layoutlib.ResourceValue;
import com.android.ide.common.layoutlib.ValueResourceParser;
import com.android.ide.common.layoutlib.ValueResourceParser.IValueResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.layoutlib.api.IResourceValue;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;

//Synthetic comment -- @@ -160,7 +159,7 @@
}

@Override
    public IResourceValue getValue(ResourceType type, String name) {
update();

// get the list for the given type








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 36ce3b0..3795811 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.layoutlib.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
//Synthetic comment -- @@ -27,7 +26,7 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.layoutlib.api.IResourceValue;
import com.android.sdklib.io.IAbstractFolder;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -372,11 +371,11 @@
* Returns the resources values matching a given {@link FolderConfiguration}.
* @param referenceConfig the configuration that each value must match.
*/
    public Map<String, Map<String, IResourceValue>> getConfiguredResources(
FolderConfiguration referenceConfig) {

        Map<String, Map<String, IResourceValue>> map =
            new HashMap<String, Map<String, IResourceValue>>();

// if the project contains libraries, we need to add the libraries resources here
// so that they are accessible to the layout rendering.
//Synthetic comment -- @@ -401,12 +400,12 @@

// we don't want to simply replace the whole map, but instead merge the
// content of any sub-map
                        Map<String, Map<String, IResourceValue>> libMap =
libRes.getConfiguredResources(referenceConfig);

                        for (Entry<String, Map<String, IResourceValue>> entry : libMap.entrySet()) {
// get the map currently in the result map for this resource type
                            Map<String, IResourceValue> tempMap = map.get(entry.getKey());
if (tempMap == null) {
// since there's no current map for this type, just add the map
// directly coming from the library resources
//Synthetic comment -- @@ -430,10 +429,10 @@
// in the XML files.
if (mIdResourceList.size() > 0) {
String idType = ResourceType.ID.getName();
            Map<String, IResourceValue> idMap = map.get(idType);

if (idMap == null) {
                idMap = new HashMap<String, IResourceValue>();
map.put(idType, idMap);
}
for (IdResourceItem id : mIdResourceList) {
//Synthetic comment -- @@ -449,12 +448,12 @@
// we don't process ID resources since we already did it above.
if (key != ResourceType.ID) {
// get the local results
                Map<String, IResourceValue> localResMap = getConfiguredResource(key,
referenceConfig);

// check if a map for this type already exists
String resName = key.getName();
                Map<String, IResourceValue> resMap = map.get(resName);
if (resMap == null) {
// just use the local results.
map.put(resName, localResMap);
//Synthetic comment -- @@ -601,13 +600,13 @@
* @param type the type of the resources.
* @param referenceConfig the configuration to best match.
*/
    private Map<String, IResourceValue> getConfiguredResource(ResourceType type,
FolderConfiguration referenceConfig) {
// get the resource item for the given type
List<ProjectResourceItem> items = mResourceMap.get(type);

// create the map
        HashMap<String, IResourceValue> map = new HashMap<String, IResourceValue>();

for (ProjectResourceItem item : items) {
// get the source files generating this resource
//Synthetic comment -- @@ -620,7 +619,7 @@
ResourceFile matchResFile = (ResourceFile)match;

// get the value of this configured resource.
                IResourceValue value = matchResFile.getValue(type, item.getName());

if (value != null) {
map.put(item.getName(), value);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index 3a349be..1cba12e 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.layoutlib.api.IResourceValue;
import com.android.sdklib.io.IAbstractFile;

import java.util.Collection;
//Synthetic comment -- @@ -95,7 +95,7 @@
* @param type the type of the resource.
* @param name the name of the resource.
*/
    public abstract IResourceValue getValue(ResourceType type, String name);

@Override
public String toString() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 637d20b..2bd9406 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.layoutlib.DensityBasedResourceValue;
import com.android.ide.common.layoutlib.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.PixelDensityQualifier;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IDensityBasedResourceValue.Density;
import com.android.sdklib.io.IAbstractFile;

import java.util.ArrayList;
//Synthetic comment -- @@ -56,7 +55,7 @@

private String mResourceName;
private ResourceType mType;
    private IResourceValue mValue;

public SingleResourceFile(IAbstractFile file, ResourceFolder folder) {
super(file, folder);
//Synthetic comment -- @@ -80,7 +79,7 @@
mType.getName(),
getResourceName(mType),
file.getOsLocation(),
                    Density.getEnum(qualifier.getValue().getDpiValue()),
isFramework());
}
}
//Synthetic comment -- @@ -124,7 +123,7 @@
* The value returned is the full absolute path of the file in OS form.
*/
@Override
    public IResourceValue getValue(ResourceType type, String name) {
return mValue;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 3667d3f..a539513 100644

//Synthetic comment -- @@ -247,11 +247,11 @@
* <p/>Valid {@link LayoutBridge} objects are always initialized before being returned.
*/
public synchronized LayoutLibrary getLayoutLibrary() {
        if (mLayoutBridgeInit == false && mLayoutLibrary.getBridge() != null) {
            mLayoutLibrary.getBridge().init(mTarget.getPath(IAndroidTarget.FONTS),
                    getEnumValueMap());
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
                                layoutLib.getBridge().clearCaches(project);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 53a36fb..45ae0d8 100644

//Synthetic comment -- @@ -33,9 +33,9 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -180,9 +180,9 @@
FolderConfiguration config = getConfiguration();

// get the configured resources
        Map<String, Map<String, IResourceValue>> configuredFramework =
framework.getConfiguredResources(config);
        Map<String, Map<String, IResourceValue>> configuredProject =
project.getConfiguredResources(config);

boolean saveFiles = System.getenv("save_file") != null;








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
deleted file mode 100644
//Synthetic comment -- index 1d4ff62..0000000

//Synthetic comment -- @@ -1,223 +0,0 @@
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

package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutLog;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.layoutlib.api.SceneResult.SceneStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * {@link LayoutBridge} wrapper around a {@link ILayoutBridge}.
 * <p/>
 * The goal is to let tools only uses the latest API by providing a conversion interface
 * between the really old API ({@link ILayoutBridge}) and the new one ({@link ILayoutBridge}).
 *
 */
@SuppressWarnings("deprecation")
class LayoutBridgeWrapper extends LayoutBridge {

    private final ILayoutBridge mBridge;
    private final ClassLoader mClassLoader;

    LayoutBridgeWrapper(ILayoutBridge bridge, ClassLoader classLoader) {
        mBridge = bridge;
        mClassLoader = classLoader;
    }

    @Override
    public int getApiLevel() {
        int apiLevel = 1;
        try {
            apiLevel = mBridge.getApiLevel();
        } catch (AbstractMethodError e) {
            // the first version of the api did not have this method
            // so this is 1
        }

        return apiLevel;
    }

    @Override
    public boolean init(String fontOsLocation, Map<String, Map<String, Integer>> enumValueMap) {
        return mBridge.init(fontOsLocation, enumValueMap);
    }

    @Override
    public boolean dispose() {
        // there's no dispose in ILayoutBridge
        return true;
    }


    @Override
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
            // Final ILayoutBridge API added support for "render full height"
            result = mBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
        } else if (apiLevel == 3) {
            // api 3 add density support.
            result = mBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
        } else if (apiLevel == 2) {
            // api 2 added boolean for separation of project/framework theme
            result = mBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getThemeName(), params.getIsProjectTheme(),
                    params.getProjectResources(), params.getFrameworkResources(),
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

            result = mBridge.computeLayout(
                    params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    themeName,
                    params.getProjectResources(), params.getFrameworkResources(),
                    params.getProjectCallback(), logWrapper);
        }

        // clean up that is not done by the ILayoutBridge itself
        cleanUp();

        return convertToScene(result);
    }


    @Override
    public void clearCaches(Object projectKey) {
        mBridge.clearCaches(projectKey);
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








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutLibrary.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutLibrary.java
//Synthetic comment -- index 5ec7ae5..89acc57 100644

//Synthetic comment -- @@ -18,14 +18,30 @@

import com.android.ide.common.log.ILogger;
import com.android.ide.common.sdk.LoadStatus;
import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.LayoutBridge;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
* Class representing and allowing to load the layoutlib jar file.
//Synthetic comment -- @@ -37,25 +53,31 @@

/** Link to the layout bridge */
private final LayoutBridge mBridge;
/** Status of the layoutlib.jar loading */
private final LoadStatus mStatus;
/** classloader used to load the jar file */
private final ClassLoader mClassLoader;

/**
     * Returns the loaded {@link LayoutBridge} object or null if the loading failed.
     */
    public LayoutBridge getBridge() {
        return mBridge;
    }

    /**
* Returns the {@link LoadStatus} of the loading of the layoutlib jar file.
*/
public LoadStatus getStatus() {
return mStatus;
}

/**
* Returns the classloader used to load the classes in the layoutlib jar file.
*/
//Synthetic comment -- @@ -77,7 +99,9 @@
public static LayoutLibrary load(String layoutLibJarOsPath, ILogger log) {

LoadStatus status = LoadStatus.LOADING;
LayoutBridge bridge = null;
ClassLoader classLoader = null;

try {
//Synthetic comment -- @@ -108,36 +132,323 @@
if (bridgeObject instanceof LayoutBridge) {
bridge = (LayoutBridge)bridgeObject;
} else if (bridgeObject instanceof ILayoutBridge) {
                            bridge = new LayoutBridgeWrapper((ILayoutBridge) bridgeObject,
                                    classLoader);
}
}
}

                if (bridge == null) {
status = LoadStatus.FAILED;
if (log != null) {
                        log.error(null, "Failed to load " + CLASS_BRIDGE); //$NON-NLS-1$
}
} else {
                    // mark the lib as loaded.
status = LoadStatus.LOADED;
}
}
} catch (Throwable t) {
status = LoadStatus.FAILED;
// log the error.
if (log != null) {
                log.error(t, "Failed to load the LayoutLib");
}
}

        return new LayoutLibrary(bridge, classLoader, status);
}

    private LayoutLibrary(LayoutBridge bridge, ClassLoader classLoader, LoadStatus status) {
mBridge = bridge;
mClassLoader = classLoader;
mStatus = status;
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/ValueResourceParser.java b/ide_common/src/com/android/ide/common/layoutlib/ValueResourceParser.java
//Synthetic comment -- index c784f1f..cc65b9d 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.common.layoutlib;

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
                    mCurrentStyle.addItem(mCurrentValue);
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








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/DensityBasedResourceValue.java
similarity index 65%
rename from ide_common/src/com/android/ide/common/layoutlib/DensityBasedResourceValue.java
rename to layoutlib_api/src/com/android/layoutlib/api/DensityBasedResourceValue.java
//Synthetic comment -- index e1c0caa..78b084c 100644

//Synthetic comment -- @@ -14,21 +14,31 @@
* limitations under the License.
*/

package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.IDensityBasedResourceValue;

public class DensityBasedResourceValue extends ResourceValue implements IDensityBasedResourceValue {

    private Density mDensity;

    public DensityBasedResourceValue(String type, String name, String value, Density density,
            boolean isFramework) {
super(type, name, value, isFramework);
mDensity = density;
}

    public Density getDensity() {
return mDensity;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IDensityBasedResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/IDensityBasedResourceValue.java
//Synthetic comment -- index e969034..fbea9ef 100644

//Synthetic comment -- @@ -18,9 +18,15 @@

/**
* Represents an Android Resources that has a density info attached to it.
*/
public interface IDensityBasedResourceValue extends IResourceValue {

public static enum Density {
XHIGH(320),
HIGH(240),
//Synthetic comment -- @@ -28,8 +34,6 @@
LOW(120),
NODPI(0);

        public final static int DEFAULT_DENSITY = 160;

private final int mValue;

Density(int value) {
//Synthetic comment -- @@ -58,6 +62,7 @@

/**
* Returns the density associated to the resource.
*/
Density getDensity();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/IResourceValue.java
//Synthetic comment -- index 1da9508..56d4f07 100644

//Synthetic comment -- @@ -18,9 +18,10 @@

/**
* Represents an android resource with a name and a string value.
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
*/
public interface IStyleResourceValue extends IResourceValue {
    
/**
* Returns the parent style name or <code>null</code> if unknown.
*/
//Synthetic comment -- @@ -29,7 +30,9 @@

/**
* Find an item in the list by name
     * @param name
*/
IResourceValue findItem(String name);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ResourceDensity.java b/layoutlib_api/src/com/android/layoutlib/api/ResourceDensity.java
new file mode 100644
//Synthetic comment -- index 0000000..bfbf536

//Synthetic comment -- @@ -0,0 +1,59 @@








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/ResourceValue.java b/layoutlib_api/src/com/android/layoutlib/api/ResourceValue.java
similarity index 63%
rename from ide_common/src/com/android/ide/common/layoutlib/ResourceValue.java
rename to layoutlib_api/src/com/android/layoutlib/api/ResourceValue.java
//Synthetic comment -- index 382d961..41f440d 100644

//Synthetic comment -- @@ -14,16 +14,18 @@
* limitations under the License.
*/

package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.IResourceValue;

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

public String getType() {
return mType;
}

public final String getName() {
return mName;
}
    
public final String getValue() {
return mValue;
}
    
    public final void setValue(String value) {
        mValue = value;
    }
    
    public void replaceWith(ResourceValue value) {
        mValue = value.mValue;
}

    public boolean isFramework() {
        return mIsFramwork;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 56014f0..309f9fd 100644

//Synthetic comment -- @@ -55,8 +55,8 @@
private float mYdpi;
private String mThemeName;
private boolean mIsProjectTheme;
    private Map<String, Map<String, IResourceValue>> mProjectResources;
    private Map<String, Map<String, IResourceValue>> mFrameworkResources;
private IProjectCallback mProjectCallback;
private LayoutLog mLog;

//Synthetic comment -- @@ -96,8 +96,8 @@
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
String themeName, boolean isProjectTheme,
            Map<String, Map<String, IResourceValue>> projectResources,
            Map<String, Map<String, IResourceValue>> frameworkResources,
IProjectCallback projectCallback, LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
//Synthetic comment -- @@ -194,11 +194,11 @@
return mIsProjectTheme;
}

    public Map<String, Map<String, IResourceValue>> getProjectResources() {
return mProjectResources;
}

    public Map<String, Map<String, IResourceValue>> getFrameworkResources() {
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

package com.android.ide.common.layoutlib;

import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;

import java.util.HashMap;

public final class StyleResourceValue extends ResourceValue implements IStyleResourceValue {

private String mParentStyle = null;
    private HashMap<String, IResourceValue> mItems = new HashMap<String, IResourceValue>();

public StyleResourceValue(String type, String name, boolean isFramework) {
super(type, name, isFramework);
//Synthetic comment -- @@ -38,23 +36,32 @@
public String getParentStyle() {
return mParentStyle;
}
    
    public IResourceValue findItem(String name) {
return mItems.get(name);
}
    
    public void addItem(IResourceValue value) {
mItems.put(value.getName(), value);
}
    
@Override
public void replaceWith(ResourceValue value) {
super.replaceWith(value);
        
if (value instanceof StyleResourceValue) {
mItems.clear();
mItems.putAll(((StyleResourceValue)value).mItems);
}
}

}







