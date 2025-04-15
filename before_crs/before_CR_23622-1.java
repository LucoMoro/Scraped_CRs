/*Revert ScreenLayoutSize to ScreenSize.

I hadn't noticed that LayoutLib actually uses ScrenSize
through the tools-common-prebuilt.jar, so this API is (somewhat*)
frozen.

* Somewhat because LayoutLib.jar actually only uses Density and
ScreenSize but not the other enums (yet?) so the rename of DockMode
is not a problem.

Change-Id:Ida3360b0111abd96d2a27c16833b282706774fb6*/
//Synthetic comment -- diff --git a/common/src/com/android/resources/ScreenLayoutSize.java b/common/src/com/android/resources/ScreenSize.java
similarity index 82%
rename from common/src/com/android/resources/ScreenLayoutSize.java
rename to common/src/com/android/resources/ScreenSize.java
//Synthetic comment -- index 12c4b91..b6ffc50 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
* Screen size enum.
* <p/>This is used in the manifest in the uses-configuration node and in the resource folder names.
*/
public enum ScreenLayoutSize implements ResourceEnum {
SMALL("small", "Small", "Small Screen"), //$NON-NLS-1$
NORMAL("normal", "Normal", "Normal Screen"), //$NON-NLS-1$
LARGE("large", "Large", "Large Screen"), //$NON-NLS-1$
//Synthetic comment -- @@ -30,7 +30,7 @@
private final String mShortDisplayValue;
private final String mLongDisplayValue;

    private ScreenLayoutSize(String value, String shortDisplayValue, String longDisplayValue) {
mValue = value;
mShortDisplayValue = shortDisplayValue;
mLongDisplayValue = longDisplayValue;
//Synthetic comment -- @@ -41,8 +41,8 @@
* @param value The qualifier value.
* @return the enum for the qualifier value or null if no matching was found.
*/
    public static ScreenLayoutSize getEnum(String value) {
        for (ScreenLayoutSize orient : values()) {
if (orient.mValue.equals(value)) {
return orient;
}
//Synthetic comment -- @@ -63,9 +63,9 @@
return mLongDisplayValue;
}

    public static int getIndex(ScreenLayoutSize orientation) {
int i = 0;
        for (ScreenLayoutSize orient : values()) {
if (orient == orientation) {
return i;
}
//Synthetic comment -- @@ -76,9 +76,9 @@
return -1;
}

    public static ScreenLayoutSize getByIndex(int index) {
int i = 0;
        for (ScreenLayoutSize orient : values()) {
if (i == index) {
return orient;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 5cd9aa3..8212877 100644

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -44,15 +44,15 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.UiMode;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -992,14 +992,14 @@
*/
private static class TabletConfigComparator implements Comparator<ConfigMatch> {
public int compare(ConfigMatch o1, ConfigMatch o2) {
            ScreenLayoutSize ss1 = o1.testConfig.getScreenLayoutSizeQualifier().getValue();
            ScreenLayoutSize ss2 = o2.testConfig.getScreenLayoutSizeQualifier().getValue();

// X-LARGE is better than all others (which are considered identical)
// if both X-LARGE, then LANDSCAPE is better than all others (which are identical)

            if (ss1 == ScreenLayoutSize.XLARGE) {
                if (ss2 == ScreenLayoutSize.XLARGE) {
ScreenOrientation so1 =
o1.testConfig.getScreenOrientationQualifier().getValue();
ScreenOrientation so2 =
//Synthetic comment -- @@ -1019,7 +1019,7 @@
} else {
return -1;
}
            } else if (ss2 == ScreenLayoutSize.XLARGE) {
return 1;
} else {
return 0;
//Synthetic comment -- @@ -1471,12 +1471,12 @@
ManifestInfo manifest = ManifestInfo.get(project);

// Look up the screen size for the current configuration
                ScreenLayoutSize screenSize = null;
if (mState.device != null) {
List<DeviceConfig> configs = mState.device.getConfigs();
for (DeviceConfig config : configs) {
                        ScreenLayoutSizeQualifier qualifier =
                            config.getConfig().getScreenLayoutSizeQualifier();
screenSize = qualifier.getValue();
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index a6d7263..a03d038 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
//Synthetic comment -- @@ -85,7 +85,7 @@
private final Density mDensity;
private final float mXdpi;
private final float mYdpi;
    private final ScreenLayoutSizeQualifier mScreenLayoutSize;

// The following fields are optional or configurable using the various chained
// setters:
//Synthetic comment -- @@ -112,7 +112,7 @@
mDensity = config.getDensity();
mXdpi = config.getXDpi();
mYdpi = config.getYDpi();
        mScreenLayoutSize = config.getCurrentConfig().getScreenLayoutSizeQualifier();
mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver = editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
//Synthetic comment -- @@ -363,8 +363,8 @@
}
}

        if (mScreenLayoutSize != null) {
            params.setConfigScreenSize(mScreenLayoutSize.getValue());
}

if (mOverrideBgColor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 7fd6ee6..991e9a5 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.ScreenLayoutSize;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
//Synthetic comment -- @@ -289,7 +289,7 @@
* @param screenSize the screen size to obtain a default theme for, or null if unknown
* @return the theme to use for this project, never null
*/
    public String getDefaultTheme(IAndroidTarget renderingTarget, ScreenLayoutSize screenSize) {
sync();

if (mManifestTheme != null) {
//Synthetic comment -- @@ -304,7 +304,7 @@
int apiLevel = Math.min(mTargetSdk, renderingTargetSdk);
// For now this theme works only on XLARGE screens. When it works for all sizes,
// add that new apiLevel to this check.
        if (apiLevel >= 11 && screenSize == ScreenLayoutSize.XLARGE) {
return PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
} else {
return PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index a02fbd1..20bde36 100644

//Synthetic comment -- @@ -42,9 +42,9 @@
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
//Synthetic comment -- @@ -121,7 +121,7 @@
sIconMap.put(NetworkCodeQualifier.class,       factory.getIcon("mnc")); //$NON-NLS-1$
sIconMap.put(LanguageQualifier.class,          factory.getIcon("language")); //$NON-NLS-1$
sIconMap.put(RegionQualifier.class,            factory.getIcon("region")); //$NON-NLS-1$
        sIconMap.put(ScreenLayoutSizeQualifier.class,  factory.getIcon("size")); //$NON-NLS-1$
sIconMap.put(ScreenRatioQualifier.class,       factory.getIcon("ratio")); //$NON-NLS-1$
sIconMap.put(ScreenOrientationQualifier.class, factory.getIcon("orientation")); //$NON-NLS-1$
sIconMap.put(UiModeQualifier.class,            factory.getIcon("dockmode")); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java
//Synthetic comment -- index d002d35..ad0e03f 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;

//Synthetic comment -- @@ -167,7 +167,7 @@
node.setTextContent(Integer.toString(ncq.getCode()));
}

        ScreenLayoutSizeQualifier slsq = config.getScreenLayoutSizeQualifier();
if (slsq != null) {
Element node = createNode(doc, configNode, LayoutDevicesXsd.NODE_SCREEN_SIZE);
node.setTextContent(slsq.getFolderSegment());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java
//Synthetic comment -- index 8aad5cb..5e19209 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.resources.Density;
//Synthetic comment -- @@ -34,9 +34,9 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.TouchScreen;

import org.xml.sax.Attributes;
//Synthetic comment -- @@ -131,9 +131,9 @@
Integer.parseInt(mStringAccumulator.toString()));
mCurrentConfig.setNetworkCodeQualifier(ncq);
} else if (LayoutDevicesXsd.NODE_SCREEN_SIZE.equals(localName)) {
            ScreenLayoutSizeQualifier ssq = new ScreenLayoutSizeQualifier(
                    ScreenLayoutSize.getEnum(mStringAccumulator.toString()));
            mCurrentConfig.setScreenLayoutSizeQualifier(ssq);
} else if (LayoutDevicesXsd.NODE_SCREEN_RATIO.equals(localName)) {
ScreenRatioQualifier srq = new ScreenRatioQualifier(
ScreenRatio.getEnum(mStringAccumulator.toString()));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index 6312ff9..ec1f4d9 100644

//Synthetic comment -- @@ -30,9 +30,9 @@
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
//Synthetic comment -- @@ -47,9 +47,9 @@
import com.android.resources.NavigationState;
import com.android.resources.NightMode;
import com.android.resources.ResourceEnum;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.TouchScreen;
import com.android.resources.UiMode;

//Synthetic comment -- @@ -429,8 +429,7 @@
new SmallestScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenWidthQualifier.class, new ScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenHeightQualifier.class, new ScreenHeightEdit(mQualifierEditParent));
            mUiMap.put(ScreenLayoutSizeQualifier.class,
                    new ScreenLayoutSizeEdit(mQualifierEditParent));
mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
mUiMap.put(UiModeQualifier.class, new UiModeEdit(mQualifierEditParent));
//Synthetic comment -- @@ -1172,17 +1171,17 @@


/**
     * Edit widget for {@link ScreenLayoutSizeQualifier}.
*/
    private class ScreenLayoutSizeEdit extends QualifierEditBase {

private Combo mSize;

        public ScreenLayoutSizeEdit(Composite parent) {
            super(parent, ScreenLayoutSizeQualifier.NAME);

mSize = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
            fillCombo(mSize, ScreenLayoutSize.values());

mSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mSize.addSelectionListener(new SelectionListener() {
//Synthetic comment -- @@ -1200,14 +1199,14 @@
int index = mSize.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setScreenLayoutSizeQualifier(new ScreenLayoutSizeQualifier(
                        ScreenLayoutSize.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setScreenLayoutSizeQualifier(
                        new ScreenLayoutSizeQualifier());
}

// notify of change
//Synthetic comment -- @@ -1216,13 +1215,13 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            ScreenLayoutSizeQualifier q = (ScreenLayoutSizeQualifier)qualifier;

            ScreenLayoutSize value = q.getValue();
if (value == null) {
mSize.clearSelection();
} else {
                mSize.select(ScreenLayoutSize.getIndex(value));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index c060712..67d838e 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.resources.ScreenLayoutSize.LARGE;
import static com.android.resources.ScreenLayoutSize.NORMAL;
import static com.android.resources.ScreenLayoutSize.XLARGE;

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 30e9bf7..e38dfb9 100644

//Synthetic comment -- @@ -34,9 +34,9 @@
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
//Synthetic comment -- @@ -52,9 +52,9 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.ResourceType;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.TouchScreen;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -280,7 +280,7 @@
config.addQualifier(new SmallestScreenWidthQualifier(320));
config.addQualifier(new ScreenWidthQualifier(320));
config.addQualifier(new ScreenHeightQualifier(480));
        config.addQualifier(new ScreenLayoutSizeQualifier(ScreenLayoutSize.NORMAL));
config.addQualifier(new ScreenRatioQualifier(ScreenRatio.NOTLONG));
config.addQualifier(new ScreenOrientationQualifier(ScreenOrientation.PORTRAIT));
config.addQualifier(new DensityQualifier(Density.MEDIUM));








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index c0d1293..cda30c5 100644

//Synthetic comment -- @@ -229,7 +229,7 @@
} else if (qualifier instanceof ScreenHeightQualifier) {
mQualifiers[INDEX_SCREEN_HEIGHT] = qualifier;

        } else if (qualifier instanceof ScreenLayoutSizeQualifier) {
mQualifiers[INDEX_SCREEN_LAYOUT_SIZE] = qualifier;

} else if (qualifier instanceof ScreenRatioQualifier) {
//Synthetic comment -- @@ -350,12 +350,12 @@
return (ScreenHeightQualifier) mQualifiers[INDEX_SCREEN_HEIGHT];
}

    public void setScreenLayoutSizeQualifier(ScreenLayoutSizeQualifier qualifier) {
mQualifiers[INDEX_SCREEN_LAYOUT_SIZE] = qualifier;
}

    public ScreenLayoutSizeQualifier getScreenLayoutSizeQualifier() {
        return (ScreenLayoutSizeQualifier)mQualifiers[INDEX_SCREEN_LAYOUT_SIZE];
}

public void setScreenRatioQualifier(ScreenRatioQualifier qualifier) {
//Synthetic comment -- @@ -846,7 +846,7 @@
mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = new SmallestScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_WIDTH] = new ScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_HEIGHT] = new ScreenHeightQualifier();
        mQualifiers[INDEX_SCREEN_LAYOUT_SIZE] = new ScreenLayoutSizeQualifier();
mQualifiers[INDEX_SCREEN_RATIO] = new ScreenRatioQualifier();
mQualifiers[INDEX_SCREEN_ORIENTATION] = new ScreenOrientationQualifier();
mQualifiers[INDEX_UI_MODE] = new UiModeQualifier();








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenLayoutSizeQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenSizeQualifier.java
similarity index 72%
rename from ide_common/src/com/android/ide/common/resources/configuration/ScreenLayoutSizeQualifier.java
rename to ide_common/src/com/android/ide/common/resources/configuration/ScreenSizeQualifier.java
//Synthetic comment -- index 0f22d36..7ab6dd8 100644

//Synthetic comment -- @@ -17,26 +17,26 @@
package com.android.ide.common.resources.configuration;

import com.android.resources.ResourceEnum;
import com.android.resources.ScreenLayoutSize;

/**
* Resource Qualifier for Screen Size. Size can be "small", "normal", "large" and "x-large"
*/
public class ScreenLayoutSizeQualifier extends EnumBasedResourceQualifier {

public static final String NAME = "Screen Size";

    private ScreenLayoutSize mValue = null;


    public ScreenLayoutSizeQualifier() {
}

    public ScreenLayoutSizeQualifier(ScreenLayoutSize value) {
mValue = value;
}

    public ScreenLayoutSize getValue() {
return mValue;
}

//Synthetic comment -- @@ -57,10 +57,10 @@

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
        ScreenLayoutSize size = ScreenLayoutSize.getEnum(value);
if (size != null) {
            ScreenLayoutSizeQualifier qualifier = new ScreenLayoutSizeQualifier(size);
            config.setScreenLayoutSizeQualifier(qualifier);
return true;
}









//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java
//Synthetic comment -- index d05399d..b19f125 100644

//Synthetic comment -- @@ -16,19 +16,19 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.ScreenLayoutSize;

import junit.framework.TestCase;

public class ScreenSizeQualifierTest extends TestCase {

    private ScreenLayoutSizeQualifier ssq;
private FolderConfiguration config;

@Override
protected void setUp() throws Exception {
super.setUp();
        ssq = new ScreenLayoutSizeQualifier();
config = new FolderConfiguration();
}

//Synthetic comment -- @@ -41,29 +41,29 @@

public void testSmall() {
assertEquals(true, ssq.checkAndSet("small", config)); //$NON-NLS-1$
        assertTrue(config.getScreenLayoutSizeQualifier() != null);
        assertEquals(ScreenLayoutSize.SMALL, config.getScreenLayoutSizeQualifier().getValue());
        assertEquals("small", config.getScreenLayoutSizeQualifier().toString()); //$NON-NLS-1$
}

public void testNormal() {
assertEquals(true, ssq.checkAndSet("normal", config)); //$NON-NLS-1$
        assertTrue(config.getScreenLayoutSizeQualifier() != null);
        assertEquals(ScreenLayoutSize.NORMAL, config.getScreenLayoutSizeQualifier().getValue());
        assertEquals("normal", config.getScreenLayoutSizeQualifier().toString()); //$NON-NLS-1$
}

public void testLarge() {
assertEquals(true, ssq.checkAndSet("large", config)); //$NON-NLS-1$
        assertTrue(config.getScreenLayoutSizeQualifier() != null);
        assertEquals(ScreenLayoutSize.LARGE, config.getScreenLayoutSizeQualifier().getValue());
        assertEquals("large", config.getScreenLayoutSizeQualifier().toString()); //$NON-NLS-1$
}

public void testXLarge() {
assertEquals(true, ssq.checkAndSet("xlarge", config)); //$NON-NLS-1$
        assertTrue(config.getScreenLayoutSizeQualifier() != null);
        assertEquals(ScreenLayoutSize.XLARGE, config.getScreenLayoutSizeQualifier().getValue());
        assertEquals("xlarge", config.getScreenLayoutSizeQualifier().toString()); //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 32c1ff2..2cfe770 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.common.rendering.api;

import com.android.resources.Density;
import com.android.resources.ScreenLayoutSize;

/**
* Base class for rendering parameters. This include the generic parameters but not what needs
//Synthetic comment -- @@ -46,7 +46,7 @@

private IImageFactory mImageFactory = null;

    private ScreenLayoutSize mConfigScreenSize = null;
private String mAppIcon = null;
private String mAppLabel = null;
private String mLocale = null;
//Synthetic comment -- @@ -138,7 +138,7 @@
mImageFactory = imageFactory;
}

    public void setConfigScreenSize(ScreenLayoutSize size) {
mConfigScreenSize  = size;
}

//Synthetic comment -- @@ -218,7 +218,7 @@
return mImageFactory;
}

    public ScreenLayoutSize getConfigScreenSize() {
return mConfigScreenSize;
}








