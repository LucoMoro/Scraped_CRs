/*Renamed some resource enum/qualifiers to be more consistent with the platform.

Change-Id:I9c5ea5183e115582e0b805ca3ec75aad70e4a65c*/
//Synthetic comment -- diff --git a/common/src/com/android/resources/ScreenSize.java b/common/src/com/android/resources/ScreenLayoutSize.java
similarity index 82%
rename from common/src/com/android/resources/ScreenSize.java
rename to common/src/com/android/resources/ScreenLayoutSize.java
//Synthetic comment -- index b6ffc50..12c4b91 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
* Screen size enum.
* <p/>This is used in the manifest in the uses-configuration node and in the resource folder names.
*/
public enum ScreenSize implements ResourceEnum {
SMALL("small", "Small", "Small Screen"), //$NON-NLS-1$
NORMAL("normal", "Normal", "Normal Screen"), //$NON-NLS-1$
LARGE("large", "Large", "Large Screen"), //$NON-NLS-1$
//Synthetic comment -- @@ -30,7 +30,7 @@
private final String mShortDisplayValue;
private final String mLongDisplayValue;

    private ScreenSize(String value, String shortDisplayValue, String longDisplayValue) {
mValue = value;
mShortDisplayValue = shortDisplayValue;
mLongDisplayValue = longDisplayValue;
//Synthetic comment -- @@ -41,8 +41,8 @@
* @param value The qualifier value.
* @return the enum for the qualifier value or null if no matching was found.
*/
    public static ScreenSize getEnum(String value) {
        for (ScreenSize orient : values()) {
if (orient.mValue.equals(value)) {
return orient;
}
//Synthetic comment -- @@ -63,9 +63,9 @@
return mLongDisplayValue;
}

    public static int getIndex(ScreenSize orientation) {
int i = 0;
        for (ScreenSize orient : values()) {
if (orient == orientation) {
return i;
}
//Synthetic comment -- @@ -76,9 +76,9 @@
return -1;
}

    public static ScreenSize getByIndex(int index) {
int i = 0;
        for (ScreenSize orient : values()) {
if (i == index) {
return orient;
}








//Synthetic comment -- diff --git a/common/src/com/android/resources/DockMode.java b/common/src/com/android/resources/UiMode.java
similarity index 78%
rename from common/src/com/android/resources/DockMode.java
rename to common/src/com/android/resources/UiMode.java
//Synthetic comment -- index 71515f9..36c903b 100644

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.resources;

/**
 * Dock enum.
* <p/>This is used in the resource folder names.
*/
public enum DockMode implements ResourceEnum {
    NONE("", "No Dock"),
CAR("car", "Car Dock"),
DESK("desk", "Desk Dock"),
TELEVISION("television", "Television");
//Synthetic comment -- @@ -29,7 +29,7 @@
private final String mValue;
private final String mDisplayValue;

    private DockMode(String value, String display) {
mValue = value;
mDisplayValue = display;
}
//Synthetic comment -- @@ -39,8 +39,8 @@
* @param value The qualifier value.
* @return the enum for the qualifier value or null if no matching was found.
*/
    public static DockMode getEnum(String value) {
        for (DockMode mode : values()) {
if (mode.mValue.equals(value)) {
return mode;
}
//Synthetic comment -- @@ -61,9 +61,9 @@
return mDisplayValue;
}

    public static int getIndex(DockMode value) {
int i = 0;
        for (DockMode mode : values()) {
if (mode == value) {
return i;
}
//Synthetic comment -- @@ -74,9 +74,9 @@
return -1;
}

    public static DockMode getByIndex(int index) {
int i = 0;
        for (DockMode value : values()) {
if (i == index) {
return value;
}
//Synthetic comment -- @@ -86,10 +86,10 @@
}

public boolean isFakeValue() {
        return this == NONE; // NONE is not a real enum. it's used for internal state only.
}

public boolean isValidValueForDevice() {
        return this != NONE;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index 86b8cd5..eba18e7 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
//Synthetic comment -- @@ -246,7 +246,7 @@
public boolean accept(ResourceQualifier qualifier) {
if (qualifier instanceof LanguageQualifier ||
qualifier instanceof RegionQualifier ||
                        qualifier instanceof DockModeQualifier ||
qualifier instanceof NightModeQualifier ||
qualifier instanceof VersionQualifier) {
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 4c6bac8..d1430c4 100644

//Synthetic comment -- @@ -24,16 +24,16 @@
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -48,12 +48,12 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.resources.Density;
import com.android.resources.DockMode;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.PlatformPackage;
//Synthetic comment -- @@ -146,7 +146,7 @@
private Combo mDeviceCombo;
private Combo mDeviceConfigCombo;
private Combo mLocaleCombo;
    private Combo mDockCombo;
private Combo mNightCombo;
private Combo mThemeCombo;
private Combo mTargetCombo;
//Synthetic comment -- @@ -251,8 +251,8 @@
String configName;
ResourceQualifier[] locale;
String theme;
        /** dock mode. Guaranteed to be non null */
        DockMode dock = DockMode.NONE;
/** night mode. Guaranteed to be non null */
NightMode night = NightMode.NOTNIGHT;
/** the version being targeted for rendering */
//Synthetic comment -- @@ -276,7 +276,7 @@
sb.append(SEP);
sb.append(theme);
sb.append(SEP);
                sb.append(dock.getResourceValue());
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);
//Synthetic comment -- @@ -313,9 +313,9 @@
}

theme = values[3];
                            dock = DockMode.getEnum(values[4]);
                            if (dock == null) {
                                dock = DockMode.NONE;
}
night = NightMode.getEnum(values[5]);
if (night == null) {
//Synthetic comment -- @@ -487,13 +487,13 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mDockCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mDockCombo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
| GridData.GRAB_HORIZONTAL));
        for (DockMode mode : DockMode.values()) {
            mDockCombo.add(mode.getLongDisplayValue());
}
        mDockCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onDockChange();
//Synthetic comment -- @@ -715,7 +715,7 @@

adaptConfigSelection(false /*needBestMatch*/);

                        mDockCombo.select(DockMode.getIndex(mState.dock));
mNightCombo.select(NightMode.getIndex(mState.night));
mTargetCombo.select(mTargetList.indexOf(mState.target));

//Synthetic comment -- @@ -959,7 +959,7 @@
selectDevice(mState.device = match.device);
fillConfigCombo(match.name);
mLocaleCombo.select(match.bundle.localeIndex);
                mDockCombo.select(match.bundle.dockModeIndex);
mNightCombo.select(match.bundle.nightModeIndex);

// TODO: display a better warning!
//Synthetic comment -- @@ -982,7 +982,7 @@
selectDevice(mState.device = match.device);
fillConfigCombo(match.name);
mLocaleCombo.select(match.bundle.localeIndex);
            mDockCombo.select(match.bundle.dockModeIndex);
mNightCombo.select(match.bundle.nightModeIndex);
}
}
//Synthetic comment -- @@ -992,14 +992,14 @@
*/
private static class TabletConfigComparator implements Comparator<ConfigMatch> {
public int compare(ConfigMatch o1, ConfigMatch o2) {
            ScreenSize ss1 = o1.testConfig.getScreenSizeQualifier().getValue();
            ScreenSize ss2 = o2.testConfig.getScreenSizeQualifier().getValue();

// X-LARGE is better than all others (which are considered identical)
// if both X-LARGE, then LANDSCAPE is better than all others (which are identical)

            if (ss1 == ScreenSize.XLARGE) {
                if (ss2 == ScreenSize.XLARGE) {
ScreenOrientation so1 =
o1.testConfig.getScreenOrientationQualifier().getValue();
ScreenOrientation so2 =
//Synthetic comment -- @@ -1019,7 +1019,7 @@
} else {
return -1;
}
            } else if (ss2 == ScreenSize.XLARGE) {
return 1;
} else {
return 0;
//Synthetic comment -- @@ -1044,14 +1044,14 @@

public int compare(ConfigMatch o1, ConfigMatch o2) {
int dpi1 = Density.DEFAULT_DENSITY;
            if (o1.testConfig.getPixelDensityQualifier() != null) {
                dpi1 = o1.testConfig.getPixelDensityQualifier().getValue().getDpiValue();
dpi1 = mDensitySort.get(dpi1, 100 /* valueIfKeyNotFound*/);
}

int dpi2 = Density.DEFAULT_DENSITY;
            if (o2.testConfig.getPixelDensityQualifier() != null) {
                dpi2 = o2.testConfig.getPixelDensityQualifier().getValue().getDpiValue();
dpi2 = mDensitySort.get(dpi2, 100 /* valueIfKeyNotFound*/);
}

//Synthetic comment -- @@ -1138,9 +1138,9 @@
// loop on each item and for each, add all variations of the dock modes
for (ConfigBundle bundle : addConfig) {
int index = 0;
            for (DockMode mode : DockMode.values()) {
ConfigBundle b = new ConfigBundle(bundle);
                b.config.setDockModeQualifier(new DockModeQualifier(mode));
b.dockModeIndex = index++;
list.add(b);
}
//Synthetic comment -- @@ -1305,9 +1305,9 @@
mState.theme = mThemeCombo.getItem(index);
}

            index = mDockCombo.getSelectionIndex();
if (index != -1) {
                mState.dock = DockMode.getByIndex(index);
}

index = mNightCombo.getSelectionIndex();
//Synthetic comment -- @@ -1471,12 +1471,12 @@
ManifestInfo manifest = ManifestInfo.get(project);

// Look up the screen size for the current configuration
                ScreenSize screenSize = null;
if (mState.device != null) {
List<DeviceConfig> configs = mState.device.getConfigs();
for (DeviceConfig config : configs) {
                        ScreenSizeQualifier qualifier =
                            config.getConfig().getScreenSizeQualifier();
screenSize = qualifier.getValue();
break;
}
//Synthetic comment -- @@ -1683,7 +1683,7 @@
*/
public Density getDensity() {
if (mCurrentConfig != null) {
            PixelDensityQualifier qual = mCurrentConfig.getPixelDensityQualifier();
if (qual != null) {
// just a sanity check
Density d = qual.getValue();
//Synthetic comment -- @@ -2214,11 +2214,11 @@
(RegionQualifier)localeQualifiers[LOCALE_REGION]);
}

            index = mDockCombo.getSelectionIndex();
if (index == -1) {
index = 0; // no selection = 0
}
            mCurrentConfig.setDockModeQualifier(new DockModeQualifier(DockMode.getByIndex(index)));

index = mNightCombo.getSelectionIndex();
if (index == -1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index a03d038..a6d7263 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
//Synthetic comment -- @@ -85,7 +85,7 @@
private final Density mDensity;
private final float mXdpi;
private final float mYdpi;
    private final ScreenSizeQualifier mScreenSize;

// The following fields are optional or configurable using the various chained
// setters:
//Synthetic comment -- @@ -112,7 +112,7 @@
mDensity = config.getDensity();
mXdpi = config.getXDpi();
mYdpi = config.getYDpi();
        mScreenSize = config.getCurrentConfig().getScreenSizeQualifier();
mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver = editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
//Synthetic comment -- @@ -363,8 +363,8 @@
}
}

        if (mScreenSize != null) {
            params.setConfigScreenSize(mScreenSize.getValue());
}

if (mOverrideBgColor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 475f5a7..7fd6ee6 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_THEME;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
import static com.android.sdklib.xml.AndroidManifest.NODE_USES_SDK;
import static org.eclipse.jdt.core.search.IJavaSearchConstants.REFERENCES;

import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -34,7 +35,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.ScreenSize;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
//Synthetic comment -- @@ -288,7 +289,7 @@
* @param screenSize the screen size to obtain a default theme for, or null if unknown
* @return the theme to use for this project, never null
*/
    public String getDefaultTheme(IAndroidTarget renderingTarget, ScreenSize screenSize) {
sync();

if (mManifestTheme != null) {
//Synthetic comment -- @@ -303,7 +304,7 @@
int apiLevel = Math.min(mTargetSdk, renderingTargetSdk);
// For now this theme works only on XLARGE screens. When it works for all sizes,
// add that new apiLevel to this check.
        if (apiLevel >= 11 && screenSize == ScreenSize.XLARGE) {
return PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
} else {
return PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 107ac4e..a02fbd1 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.LanguageQualifier;
//Synthetic comment -- @@ -39,15 +39,15 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -121,12 +121,12 @@
sIconMap.put(NetworkCodeQualifier.class,       factory.getIcon("mnc")); //$NON-NLS-1$
sIconMap.put(LanguageQualifier.class,          factory.getIcon("language")); //$NON-NLS-1$
sIconMap.put(RegionQualifier.class,            factory.getIcon("region")); //$NON-NLS-1$
        sIconMap.put(ScreenSizeQualifier.class,        factory.getIcon("size")); //$NON-NLS-1$
sIconMap.put(ScreenRatioQualifier.class,       factory.getIcon("ratio")); //$NON-NLS-1$
sIconMap.put(ScreenOrientationQualifier.class, factory.getIcon("orientation")); //$NON-NLS-1$
        sIconMap.put(DockModeQualifier.class,          factory.getIcon("dockmode")); //$NON-NLS-1$
sIconMap.put(NightModeQualifier.class,         factory.getIcon("nightmode")); //$NON-NLS-1$
        sIconMap.put(PixelDensityQualifier.class,      factory.getIcon("dpi")); //$NON-NLS-1$
sIconMap.put(TouchScreenQualifier.class,       factory.getIcon("touch")); //$NON-NLS-1$
sIconMap.put(KeyboardStateQualifier.class,     factory.getIcon("keyboard")); //$NON-NLS-1$
sIconMap.put(TextInputMethodQualifier.class,   factory.getIcon("text_input")); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java
//Synthetic comment -- index 60b528a..d002d35 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;

//Synthetic comment -- @@ -167,10 +167,10 @@
node.setTextContent(Integer.toString(ncq.getCode()));
}

        ScreenSizeQualifier ssq = config.getScreenSizeQualifier();
        if (ssq != null) {
Element node = createNode(doc, configNode, LayoutDevicesXsd.NODE_SCREEN_SIZE);
            node.setTextContent(ssq.getFolderSegment());
}

ScreenRatioQualifier srq = config.getScreenRatioQualifier();
//Synthetic comment -- @@ -185,10 +185,10 @@
node.setTextContent(soq.getFolderSegment());
}

        PixelDensityQualifier pdq = config.getPixelDensityQualifier();
        if (pdq != null) {
Element node = createNode(doc, configNode, LayoutDevicesXsd.NODE_PIXEL_DENSITY);
            node.setTextContent(pdq.getFolderSegment());
}

TouchScreenQualifier ttq = config.getTouchTypeQualifier();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java
//Synthetic comment -- index eb3853a..8aad5cb 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.resources.Density;
//Synthetic comment -- @@ -34,9 +34,9 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.ScreenSize;
import com.android.resources.TouchScreen;

import org.xml.sax.Attributes;
//Synthetic comment -- @@ -131,9 +131,9 @@
Integer.parseInt(mStringAccumulator.toString()));
mCurrentConfig.setNetworkCodeQualifier(ncq);
} else if (LayoutDevicesXsd.NODE_SCREEN_SIZE.equals(localName)) {
            ScreenSizeQualifier ssq = new ScreenSizeQualifier(
                    ScreenSize.getEnum(mStringAccumulator.toString()));
            mCurrentConfig.setScreenSizeQualifier(ssq);
} else if (LayoutDevicesXsd.NODE_SCREEN_RATIO.equals(localName)) {
ScreenRatioQualifier srq = new ScreenRatioQualifier(
ScreenRatio.getEnum(mStringAccumulator.toString()));
//Synthetic comment -- @@ -143,9 +143,9 @@
ScreenOrientation.getEnum(mStringAccumulator.toString()));
mCurrentConfig.setScreenOrientationQualifier(soq);
} else if (LayoutDevicesXsd.NODE_PIXEL_DENSITY.equals(localName)) {
            PixelDensityQualifier pdq = new PixelDensityQualifier(
Density.getEnum(mStringAccumulator.toString()));
            mCurrentConfig.setPixelDensityQualifier(pdq);
} else if (LayoutDevicesXsd.NODE_TOUCH_TYPE.equals(localName)) {
TouchScreenQualifier tsq = new TouchScreenQualifier(
TouchScreen.getEnum(mStringAccumulator.toString()));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index ffef837..6312ff9 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.AndroidConstants;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.LanguageQualifier;
//Synthetic comment -- @@ -26,32 +26,32 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.resources.Density;
import com.android.resources.DockMode;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.NightMode;
import com.android.resources.ResourceEnum;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.ScreenSize;
import com.android.resources.TouchScreen;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -182,7 +182,7 @@
}

/**
     * Implementation of {@link VerifyListener} for the Pixel Density qualifier.
*/
public static class DensityVerifier extends DigitVerifier { }

//Synthetic comment -- @@ -429,12 +429,13 @@
new SmallestScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenWidthQualifier.class, new ScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenHeightQualifier.class, new ScreenHeightEdit(mQualifierEditParent));
            mUiMap.put(ScreenSizeQualifier.class, new ScreenSizeEdit(mQualifierEditParent));
mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
            mUiMap.put(DockModeQualifier.class, new DockModeEdit(mQualifierEditParent));
mUiMap.put(NightModeQualifier.class, new NightModeEdit(mQualifierEditParent));
            mUiMap.put(PixelDensityQualifier.class, new PixelDensityEdit(mQualifierEditParent));
mUiMap.put(TouchScreenQualifier.class, new TouchEdit(mQualifierEditParent));
mUiMap.put(KeyboardStateQualifier.class, new KeyboardEdit(mQualifierEditParent));
mUiMap.put(TextInputMethodQualifier.class, new TextInputEdit(mQualifierEditParent));
//Synthetic comment -- @@ -1171,17 +1172,17 @@


/**
     * Edit widget for {@link ScreenSizeQualifier}.
*/
    private class ScreenSizeEdit extends QualifierEditBase {

private Combo mSize;

        public ScreenSizeEdit(Composite parent) {
            super(parent, ScreenSizeQualifier.NAME);

mSize = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
            fillCombo(mSize, ScreenSize.values());

mSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mSize.addSelectionListener(new SelectionListener() {
//Synthetic comment -- @@ -1199,14 +1200,14 @@
int index = mSize.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setScreenSizeQualifier(new ScreenSizeQualifier(
                    ScreenSize.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setScreenSizeQualifier(
                        new ScreenSizeQualifier());
}

// notify of change
//Synthetic comment -- @@ -1215,13 +1216,13 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            ScreenSizeQualifier q = (ScreenSizeQualifier)qualifier;

            ScreenSize value = q.getValue();
if (value == null) {
mSize.clearSelection();
} else {
                mSize.select(ScreenSize.getIndex(value));
}
}
}
//Synthetic comment -- @@ -1341,18 +1342,18 @@
/**
* Edit widget for {@link DockModeQualifier}.
*/
    private class DockModeEdit extends QualifierEditBase {

        private Combo mDockMode;

        public DockModeEdit(Composite parent) {
            super(parent, DockModeQualifier.NAME);

            mDockMode = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
            fillCombo(mDockMode, DockMode.values());

            mDockMode.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            mDockMode.addSelectionListener(new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
onDockModeChange();
}
//Synthetic comment -- @@ -1364,16 +1365,16 @@

protected void onDockModeChange() {
// update the current config
            int index = mDockMode.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setDockModeQualifier(
                        new DockModeQualifier(DockMode.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setDockModeQualifier(new DockModeQualifier());
}

// notify of change
//Synthetic comment -- @@ -1382,13 +1383,13 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            DockModeQualifier q = (DockModeQualifier)qualifier;

            DockMode value = q.getValue();
if (value == null) {
                mDockMode.clearSelection();
} else {
                mDockMode.select(DockMode.getIndex(value));
}
}
}
//Synthetic comment -- @@ -1450,13 +1451,13 @@


/**
     * Edit widget for {@link PixelDensityQualifier}.
*/
    private class PixelDensityEdit extends QualifierEditBase {
private Combo mDensity;

        public PixelDensityEdit(Composite parent) {
            super(parent, PixelDensityQualifier.NAME);

mDensity = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
fillCombo(mDensity, Density.values());
//Synthetic comment -- @@ -1477,14 +1478,14 @@
int index = mDensity.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setPixelDensityQualifier(new PixelDensityQualifier(
Density.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setPixelDensityQualifier(
                        new PixelDensityQualifier());
}

// notify of change
//Synthetic comment -- @@ -1493,7 +1494,7 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            PixelDensityQualifier q = (PixelDensityQualifier)qualifier;

Density value = q.getValue();
if (value == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 67d838e..c060712 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.resources.ScreenSize.LARGE;
import static com.android.resources.ScreenSize.NORMAL;
import static com.android.resources.ScreenSize.XLARGE;

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index b7ebb49..30e9bf7 100644

//Synthetic comment -- @@ -28,14 +28,17 @@
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -49,9 +52,9 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.ScreenSize;
import com.android.resources.TouchScreen;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -274,10 +277,13 @@
FolderConfiguration config = new FolderConfiguration();

// this matches an ADP1.
        config.addQualifier(new ScreenSizeQualifier(ScreenSize.NORMAL));
config.addQualifier(new ScreenRatioQualifier(ScreenRatio.NOTLONG));
config.addQualifier(new ScreenOrientationQualifier(ScreenOrientation.PORTRAIT));
        config.addQualifier(new PixelDensityQualifier(Density.MEDIUM));
config.addQualifier(new TouchScreenQualifier(TouchScreen.FINGER));
config.addQualifier(new KeyboardStateQualifier(KeyboardState.HIDDEN));
config.addQualifier(new TextInputMethodQualifier(Keyboard.QWERTY));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index f2a6b54..46f19cf 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.ide.eclipse.mock.Mocks;
import com.android.resources.DockMode;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
//Synthetic comment -- @@ -34,6 +33,7 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ScreenOrientation;
import com.android.resources.TouchScreen;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -168,7 +168,7 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                DockMode.DESK.getResourceValue(), // dock mode
NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode
//Synthetic comment -- @@ -197,7 +197,7 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                DockMode.DESK.getResourceValue(), // dock mode
NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/SingleResourceFile.java b/ide_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index cd2b627..9c8977e 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.common.rendering.api.DensityBasedResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.configuration.PixelDensityQualifier;
import com.android.io.IAbstractFile;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -57,7 +57,7 @@
mResourceName = getResourceName(mType);

// test if there's a density qualifier associated with the resource
        PixelDensityQualifier qualifier = folder.getConfiguration().getPixelDensityQualifier();

if (qualifier == null) {
mValue = new ResourceValue(mType, getResourceName(mType),








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/PixelDensityQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/DensityQualifier.java
similarity index 85%
rename from ide_common/src/com/android/ide/common/resources/configuration/PixelDensityQualifier.java
rename to ide_common/src/com/android/ide/common/resources/configuration/DensityQualifier.java
//Synthetic comment -- index 80842a8..bb23b44 100644

//Synthetic comment -- @@ -25,18 +25,18 @@
/**
* Resource Qualifier for Screen Pixel Density.
*/
public final class PixelDensityQualifier extends EnumBasedResourceQualifier {
private final static Pattern sDensityLegacyPattern = Pattern.compile("^(\\d+)dpi$");//$NON-NLS-1$

    public static final String NAME = "Pixel Density";

private Density mValue = Density.MEDIUM;

    public PixelDensityQualifier() {
// pass
}

    public PixelDensityQualifier(Density value) {
mValue = value;
}

//Synthetic comment -- @@ -79,9 +79,9 @@
}

if (density != null) {
            PixelDensityQualifier qualifier = new PixelDensityQualifier();
qualifier.mValue = density;
            config.setPixelDensityQualifier(qualifier);
return true;
}

//Synthetic comment -- @@ -90,7 +90,7 @@

@Override
public boolean isMatchFor(ResourceQualifier qualifier) {
        if (qualifier instanceof PixelDensityQualifier) {
// as long as there's a density qualifier, it's always a match.
// The best match will be found later.
return true;
//Synthetic comment -- @@ -105,8 +105,8 @@
return true;
}

        PixelDensityQualifier compareQ = (PixelDensityQualifier)compareTo;
        PixelDensityQualifier referenceQ = (PixelDensityQualifier)reference;

if (compareQ.mValue == referenceQ.mValue) {
// what we have is already the best possible match (exact match)








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index 8b04bac..c0d1293 100644

//Synthetic comment -- @@ -50,10 +50,10 @@
private final static int INDEX_SMALLEST_SCREEN_WIDTH = 4;
private final static int INDEX_SCREEN_WIDTH          = 5;
private final static int INDEX_SCREEN_HEIGHT         = 6;
    private final static int INDEX_SCREEN_SIZE           = 7;
private final static int INDEX_SCREEN_RATIO          = 8;
private final static int INDEX_SCREEN_ORIENTATION    = 9;
    private final static int INDEX_DOCK_MODE             = 10;
private final static int INDEX_NIGHT_MODE            = 11;
private final static int INDEX_PIXEL_DENSITY         = 12;
private final static int INDEX_TOUCH_TYPE            = 13;
//Synthetic comment -- @@ -229,8 +229,8 @@
} else if (qualifier instanceof ScreenHeightQualifier) {
mQualifiers[INDEX_SCREEN_HEIGHT] = qualifier;

        } else if (qualifier instanceof ScreenSizeQualifier) {
            mQualifiers[INDEX_SCREEN_SIZE] = qualifier;

} else if (qualifier instanceof ScreenRatioQualifier) {
mQualifiers[INDEX_SCREEN_RATIO] = qualifier;
//Synthetic comment -- @@ -238,13 +238,13 @@
} else if (qualifier instanceof ScreenOrientationQualifier) {
mQualifiers[INDEX_SCREEN_ORIENTATION] = qualifier;

        } else if (qualifier instanceof DockModeQualifier) {
            mQualifiers[INDEX_DOCK_MODE] = qualifier;

} else if (qualifier instanceof NightModeQualifier) {
mQualifiers[INDEX_NIGHT_MODE] = qualifier;

        } else if (qualifier instanceof PixelDensityQualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;

} else if (qualifier instanceof TouchScreenQualifier) {
//Synthetic comment -- @@ -350,12 +350,12 @@
return (ScreenHeightQualifier) mQualifiers[INDEX_SCREEN_HEIGHT];
}

    public void setScreenSizeQualifier(ScreenSizeQualifier qualifier) {
        mQualifiers[INDEX_SCREEN_SIZE] = qualifier;
}

    public ScreenSizeQualifier getScreenSizeQualifier() {
        return (ScreenSizeQualifier)mQualifiers[INDEX_SCREEN_SIZE];
}

public void setScreenRatioQualifier(ScreenRatioQualifier qualifier) {
//Synthetic comment -- @@ -374,12 +374,12 @@
return (ScreenOrientationQualifier)mQualifiers[INDEX_SCREEN_ORIENTATION];
}

    public void setDockModeQualifier(DockModeQualifier qualifier) {
        mQualifiers[INDEX_DOCK_MODE] = qualifier;
}

    public DockModeQualifier getDockModeQualifier() {
        return (DockModeQualifier)mQualifiers[INDEX_DOCK_MODE];
}

public void setNightModeQualifier(NightModeQualifier qualifier) {
//Synthetic comment -- @@ -390,12 +390,12 @@
return (NightModeQualifier)mQualifiers[INDEX_NIGHT_MODE];
}

    public void setPixelDensityQualifier(PixelDensityQualifier qualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;
}

    public PixelDensityQualifier getPixelDensityQualifier() {
        return (PixelDensityQualifier)mQualifiers[INDEX_PIXEL_DENSITY];
}

public void setTouchTypeQualifier(TouchScreenQualifier qualifier) {
//Synthetic comment -- @@ -457,7 +457,7 @@
/**
* Updates the {@link SmallestScreenWidthQualifier}, {@link ScreenWidthQualifier}, and
* {@link ScreenHeightQualifier} based on the (required) values of
     * {@link ScreenDimensionQualifier} {@link PixelDensityQualifier}, and
* {@link ScreenOrientationQualifier}.
*
* Also the density cannot be {@link Density#NODPI} as it's not valid on a device.
//Synthetic comment -- @@ -469,7 +469,7 @@
ResourceQualifier orientQ = mQualifiers[INDEX_SCREEN_ORIENTATION];

if (sizeQ != null && densityQ != null && orientQ != null) {
            Density density = ((PixelDensityQualifier) densityQ).getValue();
if (density == Density.NODPI) {
return;
}
//Synthetic comment -- @@ -846,12 +846,12 @@
mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = new SmallestScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_WIDTH] = new ScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_HEIGHT] = new ScreenHeightQualifier();
        mQualifiers[INDEX_SCREEN_SIZE] = new ScreenSizeQualifier();
mQualifiers[INDEX_SCREEN_RATIO] = new ScreenRatioQualifier();
mQualifiers[INDEX_SCREEN_ORIENTATION] = new ScreenOrientationQualifier();
        mQualifiers[INDEX_DOCK_MODE] = new DockModeQualifier();
mQualifiers[INDEX_NIGHT_MODE] = new NightModeQualifier();
        mQualifiers[INDEX_PIXEL_DENSITY] = new PixelDensityQualifier();
mQualifiers[INDEX_TOUCH_TYPE] = new TouchScreenQualifier();
mQualifiers[INDEX_KEYBOARD_STATE] = new KeyboardStateQualifier();
mQualifiers[INDEX_TEXT_INPUT_METHOD] = new TextInputMethodQualifier();








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenSizeQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenLayoutSizeQualifier.java
similarity index 72%
rename from ide_common/src/com/android/ide/common/resources/configuration/ScreenSizeQualifier.java
rename to ide_common/src/com/android/ide/common/resources/configuration/ScreenLayoutSizeQualifier.java
//Synthetic comment -- index 7ab6dd8..0f22d36 100644

//Synthetic comment -- @@ -17,26 +17,26 @@
package com.android.ide.common.resources.configuration;

import com.android.resources.ResourceEnum;
import com.android.resources.ScreenSize;

/**
* Resource Qualifier for Screen Size. Size can be "small", "normal", "large" and "x-large"
*/
public class ScreenSizeQualifier extends EnumBasedResourceQualifier {

public static final String NAME = "Screen Size";

    private ScreenSize mValue = null;


    public ScreenSizeQualifier() {
}

    public ScreenSizeQualifier(ScreenSize value) {
mValue = value;
}

    public ScreenSize getValue() {
return mValue;
}

//Synthetic comment -- @@ -57,10 +57,10 @@

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
        ScreenSize size = ScreenSize.getEnum(value);
if (size != null) {
            ScreenSizeQualifier qualifier = new ScreenSizeQualifier(size);
            config.setScreenSizeQualifier(qualifier);
return true;
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/DockModeQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/UiModeQualifier.java
similarity index 66%
rename from ide_common/src/com/android/ide/common/resources/configuration/DockModeQualifier.java
rename to ide_common/src/com/android/ide/common/resources/configuration/UiModeQualifier.java
//Synthetic comment -- index 2c832eb..f9ad0b7 100644

//Synthetic comment -- @@ -16,27 +16,27 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.DockMode;
import com.android.resources.ResourceEnum;

/**
 * Resource Qualifier for Navigation Method.
*/
public final class DockModeQualifier extends EnumBasedResourceQualifier {

    public static final String NAME = "Dock Mode";

    private DockMode mValue;

    public DockModeQualifier() {
// pass
}

    public DockModeQualifier(DockMode value) {
mValue = value;
}

    public DockMode getValue() {
return mValue;
}

//Synthetic comment -- @@ -52,15 +52,15 @@

@Override
public String getShortName() {
        return "Dock Mode";
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
        DockMode mode = DockMode.getEnum(value);
if (mode != null) {
            DockModeQualifier qualifier = new DockModeQualifier(mode);
            config.setDockModeQualifier(qualifier);
return true;
}

//Synthetic comment -- @@ -69,13 +69,13 @@

@Override
public boolean isMatchFor(ResourceQualifier qualifier) {
        // only NONE is a match other DockModes
        if (mValue == DockMode.NONE) {
return true;
}

// others must be an exact match
        return ((DockModeQualifier)qualifier).mValue == mValue;
}

@Override
//Synthetic comment -- @@ -84,8 +84,8 @@
return true;
}

        DockModeQualifier compareQualifier = (DockModeQualifier)compareTo;
        DockModeQualifier referenceQualifier = (DockModeQualifier)reference;

if (compareQualifier.getValue() == referenceQualifier.getValue()) {
// what we have is already the best possible match (exact match)
//Synthetic comment -- @@ -93,8 +93,8 @@
} else  if (mValue == referenceQualifier.mValue) {
// got new exact value, this is the best!
return true;
        } else if (mValue == DockMode.NONE) {
            // else "none" can be a match in case there's no exact match
return true;
}









//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java
//Synthetic comment -- index c2b7452..1653805 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.DockMode;

import junit.framework.TestCase;

public class DockModeQualifierTest extends TestCase {

    private DockModeQualifier mCarQualifier;
    private DockModeQualifier mDeskQualifier;
    private DockModeQualifier mTVQualifier;
    private DockModeQualifier mNoneQualifier;

@Override
protected void setUp() throws Exception {
super.setUp();
        mCarQualifier = new DockModeQualifier(DockMode.CAR);
        mDeskQualifier = new DockModeQualifier(DockMode.DESK);
        mTVQualifier = new DockModeQualifier(DockMode.TELEVISION);
        mNoneQualifier = new DockModeQualifier(DockMode.NONE);
}

@Override








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/PixelDensityQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/PixelDensityQualifierTest.java
//Synthetic comment -- index b99f2af..4ab493a 100644

//Synthetic comment -- @@ -22,13 +22,13 @@

public class PixelDensityQualifierTest extends TestCase {

    private PixelDensityQualifier pdq;
private FolderConfiguration config;

@Override
protected void setUp() throws Exception {
super.setUp();
        pdq = new PixelDensityQualifier();
config = new FolderConfiguration();
}

//Synthetic comment -- @@ -41,9 +41,9 @@

public void testCheckAndSet() {
assertEquals(true, pdq.checkAndSet("ldpi", config));//$NON-NLS-1$
        assertTrue(config.getPixelDensityQualifier() != null);
        assertEquals(Density.LOW, config.getPixelDensityQualifier().getValue());
        assertEquals("ldpi", config.getPixelDensityQualifier().toString()); //$NON-NLS-1$
}

public void testFailures() {
//Synthetic comment -- @@ -55,10 +55,10 @@
}

public void testIsBetterMatchThan() {
        PixelDensityQualifier ldpi = new PixelDensityQualifier(Density.LOW);
        PixelDensityQualifier mdpi = new PixelDensityQualifier(Density.MEDIUM);
        PixelDensityQualifier hdpi = new PixelDensityQualifier(Density.HIGH);
        PixelDensityQualifier xhdpi = new PixelDensityQualifier(Density.XHIGH);

// first test that each Q is a better match than all other Qs when the ref is the same Q.
assertTrue(ldpi.isBetterMatchThan(mdpi, ldpi));








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java
//Synthetic comment -- index b19f125..d05399d 100644

//Synthetic comment -- @@ -16,19 +16,19 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.ScreenSize;

import junit.framework.TestCase;

public class ScreenSizeQualifierTest extends TestCase {

    private ScreenSizeQualifier ssq;
private FolderConfiguration config;

@Override
protected void setUp() throws Exception {
super.setUp();
        ssq = new ScreenSizeQualifier();
config = new FolderConfiguration();
}

//Synthetic comment -- @@ -41,29 +41,29 @@

public void testSmall() {
assertEquals(true, ssq.checkAndSet("small", config)); //$NON-NLS-1$
        assertTrue(config.getScreenSizeQualifier() != null);
        assertEquals(ScreenSize.SMALL, config.getScreenSizeQualifier().getValue());
        assertEquals("small", config.getScreenSizeQualifier().toString()); //$NON-NLS-1$
}

public void testNormal() {
assertEquals(true, ssq.checkAndSet("normal", config)); //$NON-NLS-1$
        assertTrue(config.getScreenSizeQualifier() != null);
        assertEquals(ScreenSize.NORMAL, config.getScreenSizeQualifier().getValue());
        assertEquals("normal", config.getScreenSizeQualifier().toString()); //$NON-NLS-1$
}

public void testLarge() {
assertEquals(true, ssq.checkAndSet("large", config)); //$NON-NLS-1$
        assertTrue(config.getScreenSizeQualifier() != null);
        assertEquals(ScreenSize.LARGE, config.getScreenSizeQualifier().getValue());
        assertEquals("large", config.getScreenSizeQualifier().toString()); //$NON-NLS-1$
}

public void testXLarge() {
assertEquals(true, ssq.checkAndSet("xlarge", config)); //$NON-NLS-1$
        assertTrue(config.getScreenSizeQualifier() != null);
        assertEquals(ScreenSize.XLARGE, config.getScreenSizeQualifier().getValue());
        assertEquals("xlarge", config.getScreenSizeQualifier().toString()); //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 2cfe770..32c1ff2 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.common.rendering.api;

import com.android.resources.Density;
import com.android.resources.ScreenSize;

/**
* Base class for rendering parameters. This include the generic parameters but not what needs
//Synthetic comment -- @@ -46,7 +46,7 @@

private IImageFactory mImageFactory = null;

    private ScreenSize mConfigScreenSize = null;
private String mAppIcon = null;
private String mAppLabel = null;
private String mLocale = null;
//Synthetic comment -- @@ -138,7 +138,7 @@
mImageFactory = imageFactory;
}

    public void setConfigScreenSize(ScreenSize size) {
mConfigScreenSize  = size;
}

//Synthetic comment -- @@ -218,7 +218,7 @@
return mImageFactory;
}

    public ScreenSize getConfigScreenSize() {
return mConfigScreenSize;
}








