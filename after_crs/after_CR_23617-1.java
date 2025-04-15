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








//Synthetic comment -- diff --git a/common/src/com/android/resources/DockMode.java b/common/src/com/android/resources/UiMode.java
similarity index 78%
rename from common/src/com/android/resources/DockMode.java
rename to common/src/com/android/resources/UiMode.java
//Synthetic comment -- index 71515f9..36c903b 100644

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.resources;

/**
 * UI Mode enum.
* <p/>This is used in the resource folder names.
*/
public enum UiMode implements ResourceEnum {
    NORMAL("", "Normal"),
CAR("car", "Car Dock"),
DESK("desk", "Desk Dock"),
TELEVISION("television", "Television");
//Synthetic comment -- @@ -29,7 +29,7 @@
private final String mValue;
private final String mDisplayValue;

    private UiMode(String value, String display) {
mValue = value;
mDisplayValue = display;
}
//Synthetic comment -- @@ -39,8 +39,8 @@
* @param value The qualifier value.
* @return the enum for the qualifier value or null if no matching was found.
*/
    public static UiMode getEnum(String value) {
        for (UiMode mode : values()) {
if (mode.mValue.equals(value)) {
return mode;
}
//Synthetic comment -- @@ -61,9 +61,9 @@
return mDisplayValue;
}

    public static int getIndex(UiMode value) {
int i = 0;
        for (UiMode mode : values()) {
if (mode == value) {
return i;
}
//Synthetic comment -- @@ -74,9 +74,9 @@
return -1;
}

    public static UiMode getByIndex(int index) {
int i = 0;
        for (UiMode value : values()) {
if (i == index) {
return value;
}
//Synthetic comment -- @@ -86,10 +86,10 @@
}

public boolean isFakeValue() {
        return this == NORMAL; // NORMAL is not a real enum. it's used for internal state only.
}

public boolean isValidValueForDevice() {
        return this != NORMAL;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index 86b8cd5..eba18e7 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
//Synthetic comment -- @@ -246,7 +246,7 @@
public boolean accept(ResourceQualifier qualifier) {
if (qualifier instanceof LanguageQualifier ||
qualifier instanceof RegionQualifier ||
                        qualifier instanceof UiModeQualifier ||
qualifier instanceof NightModeQualifier ||
qualifier instanceof VersionQualifier) {
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 4c6bac8..d1430c4 100644

//Synthetic comment -- @@ -24,16 +24,16 @@
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -48,12 +48,12 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.UiMode;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.PlatformPackage;
//Synthetic comment -- @@ -146,7 +146,7 @@
private Combo mDeviceCombo;
private Combo mDeviceConfigCombo;
private Combo mLocaleCombo;
    private Combo mUiModeCombo;
private Combo mNightCombo;
private Combo mThemeCombo;
private Combo mTargetCombo;
//Synthetic comment -- @@ -251,8 +251,8 @@
String configName;
ResourceQualifier[] locale;
String theme;
        /** UI mode. Guaranteed to be non null */
        UiMode uiMode = UiMode.NORMAL;
/** night mode. Guaranteed to be non null */
NightMode night = NightMode.NOTNIGHT;
/** the version being targeted for rendering */
//Synthetic comment -- @@ -276,7 +276,7 @@
sb.append(SEP);
sb.append(theme);
sb.append(SEP);
                sb.append(uiMode.getResourceValue());
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);
//Synthetic comment -- @@ -313,9 +313,9 @@
}

theme = values[3];
                            uiMode = UiMode.getEnum(values[4]);
                            if (uiMode == null) {
                                uiMode = UiMode.NORMAL;
}
night = NightMode.getEnum(values[5]);
if (night == null) {
//Synthetic comment -- @@ -487,13 +487,13 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mUiModeCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mUiModeCombo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
| GridData.GRAB_HORIZONTAL));
        for (UiMode mode : UiMode.values()) {
            mUiModeCombo.add(mode.getLongDisplayValue());
}
        mUiModeCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onDockChange();
//Synthetic comment -- @@ -715,7 +715,7 @@

adaptConfigSelection(false /*needBestMatch*/);

                        mUiModeCombo.select(UiMode.getIndex(mState.uiMode));
mNightCombo.select(NightMode.getIndex(mState.night));
mTargetCombo.select(mTargetList.indexOf(mState.target));

//Synthetic comment -- @@ -959,7 +959,7 @@
selectDevice(mState.device = match.device);
fillConfigCombo(match.name);
mLocaleCombo.select(match.bundle.localeIndex);
                mUiModeCombo.select(match.bundle.dockModeIndex);
mNightCombo.select(match.bundle.nightModeIndex);

// TODO: display a better warning!
//Synthetic comment -- @@ -982,7 +982,7 @@
selectDevice(mState.device = match.device);
fillConfigCombo(match.name);
mLocaleCombo.select(match.bundle.localeIndex);
            mUiModeCombo.select(match.bundle.dockModeIndex);
mNightCombo.select(match.bundle.nightModeIndex);
}
}
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
//Synthetic comment -- @@ -1044,14 +1044,14 @@

public int compare(ConfigMatch o1, ConfigMatch o2) {
int dpi1 = Density.DEFAULT_DENSITY;
            if (o1.testConfig.getDensityQualifier() != null) {
                dpi1 = o1.testConfig.getDensityQualifier().getValue().getDpiValue();
dpi1 = mDensitySort.get(dpi1, 100 /* valueIfKeyNotFound*/);
}

int dpi2 = Density.DEFAULT_DENSITY;
            if (o2.testConfig.getDensityQualifier() != null) {
                dpi2 = o2.testConfig.getDensityQualifier().getValue().getDpiValue();
dpi2 = mDensitySort.get(dpi2, 100 /* valueIfKeyNotFound*/);
}

//Synthetic comment -- @@ -1138,9 +1138,9 @@
// loop on each item and for each, add all variations of the dock modes
for (ConfigBundle bundle : addConfig) {
int index = 0;
            for (UiMode mode : UiMode.values()) {
ConfigBundle b = new ConfigBundle(bundle);
                b.config.setUiModeQualifier(new UiModeQualifier(mode));
b.dockModeIndex = index++;
list.add(b);
}
//Synthetic comment -- @@ -1305,9 +1305,9 @@
mState.theme = mThemeCombo.getItem(index);
}

            index = mUiModeCombo.getSelectionIndex();
if (index != -1) {
                mState.uiMode = UiMode.getByIndex(index);
}

index = mNightCombo.getSelectionIndex();
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
//Synthetic comment -- @@ -1683,7 +1683,7 @@
*/
public Density getDensity() {
if (mCurrentConfig != null) {
            DensityQualifier qual = mCurrentConfig.getDensityQualifier();
if (qual != null) {
// just a sanity check
Density d = qual.getValue();
//Synthetic comment -- @@ -2214,11 +2214,11 @@
(RegionQualifier)localeQualifiers[LOCALE_REGION]);
}

            index = mUiModeCombo.getSelectionIndex();
if (index == -1) {
index = 0; // no selection = 0
}
            mCurrentConfig.setUiModeQualifier(new UiModeQualifier(UiMode.getByIndex(index)));

index = mNightCombo.getSelectionIndex();
if (index == -1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index a03d038..a6d7263 100644

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
import com.android.resources.ScreenLayoutSize;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
//Synthetic comment -- @@ -288,7 +289,7 @@
* @param screenSize the screen size to obtain a default theme for, or null if unknown
* @return the theme to use for this project, never null
*/
    public String getDefaultTheme(IAndroidTarget renderingTarget, ScreenLayoutSize screenSize) {
sync();

if (mManifestTheme != null) {
//Synthetic comment -- @@ -303,7 +304,7 @@
int apiLevel = Math.min(mTargetSdk, renderingTargetSdk);
// For now this theme works only on XLARGE screens. When it works for all sizes,
// add that new apiLevel to this check.
        if (apiLevel >= 11 && screenSize == ScreenLayoutSize.XLARGE) {
return PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
} else {
return PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 107ac4e..a02fbd1 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.LanguageQualifier;
//Synthetic comment -- @@ -39,15 +39,15 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -121,12 +121,12 @@
sIconMap.put(NetworkCodeQualifier.class,       factory.getIcon("mnc")); //$NON-NLS-1$
sIconMap.put(LanguageQualifier.class,          factory.getIcon("language")); //$NON-NLS-1$
sIconMap.put(RegionQualifier.class,            factory.getIcon("region")); //$NON-NLS-1$
        sIconMap.put(ScreenLayoutSizeQualifier.class,  factory.getIcon("size")); //$NON-NLS-1$
sIconMap.put(ScreenRatioQualifier.class,       factory.getIcon("ratio")); //$NON-NLS-1$
sIconMap.put(ScreenOrientationQualifier.class, factory.getIcon("orientation")); //$NON-NLS-1$
        sIconMap.put(UiModeQualifier.class,            factory.getIcon("dockmode")); //$NON-NLS-1$
sIconMap.put(NightModeQualifier.class,         factory.getIcon("nightmode")); //$NON-NLS-1$
        sIconMap.put(DensityQualifier.class,           factory.getIcon("dpi")); //$NON-NLS-1$
sIconMap.put(TouchScreenQualifier.class,       factory.getIcon("touch")); //$NON-NLS-1$
sIconMap.put(KeyboardStateQualifier.class,     factory.getIcon("keyboard")); //$NON-NLS-1$
sIconMap.put(TextInputMethodQualifier.class,   factory.getIcon("text_input")); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java
//Synthetic comment -- index 60b528a..d002d35 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;

//Synthetic comment -- @@ -167,10 +167,10 @@
node.setTextContent(Integer.toString(ncq.getCode()));
}

        ScreenLayoutSizeQualifier slsq = config.getScreenLayoutSizeQualifier();
        if (slsq != null) {
Element node = createNode(doc, configNode, LayoutDevicesXsd.NODE_SCREEN_SIZE);
            node.setTextContent(slsq.getFolderSegment());
}

ScreenRatioQualifier srq = config.getScreenRatioQualifier();
//Synthetic comment -- @@ -185,10 +185,10 @@
node.setTextContent(soq.getFolderSegment());
}

        DensityQualifier dq = config.getDensityQualifier();
        if (dq != null) {
Element node = createNode(doc, configNode, LayoutDevicesXsd.NODE_PIXEL_DENSITY);
            node.setTextContent(dq.getFolderSegment());
}

TouchScreenQualifier ttq = config.getTouchTypeQualifier();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java
//Synthetic comment -- index eb3853a..8aad5cb 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
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
//Synthetic comment -- @@ -143,9 +143,9 @@
ScreenOrientation.getEnum(mStringAccumulator.toString()));
mCurrentConfig.setScreenOrientationQualifier(soq);
} else if (LayoutDevicesXsd.NODE_PIXEL_DENSITY.equals(localName)) {
            DensityQualifier dq = new DensityQualifier(
Density.getEnum(mStringAccumulator.toString()));
            mCurrentConfig.setDensityQualifier(dq);
} else if (LayoutDevicesXsd.NODE_TOUCH_TYPE.equals(localName)) {
TouchScreenQualifier tsq = new TouchScreenQualifier(
TouchScreen.getEnum(mStringAccumulator.toString()));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index ffef837..6312ff9 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.AndroidConstants;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.LanguageQualifier;
//Synthetic comment -- @@ -26,32 +26,32 @@
import com.android.ide.common.resources.configuration.NavigationStateQualifier;
import com.android.ide.common.resources.configuration.NetworkCodeQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.resources.Density;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.NightMode;
import com.android.resources.ResourceEnum;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.TouchScreen;
import com.android.resources.UiMode;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -182,7 +182,7 @@
}

/**
     * Implementation of {@link VerifyListener} for the Density qualifier.
*/
public static class DensityVerifier extends DigitVerifier { }

//Synthetic comment -- @@ -429,12 +429,13 @@
new SmallestScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenWidthQualifier.class, new ScreenWidthEdit(mQualifierEditParent));
mUiMap.put(ScreenHeightQualifier.class, new ScreenHeightEdit(mQualifierEditParent));
            mUiMap.put(ScreenLayoutSizeQualifier.class,
                    new ScreenLayoutSizeEdit(mQualifierEditParent));
mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
            mUiMap.put(UiModeQualifier.class, new UiModeEdit(mQualifierEditParent));
mUiMap.put(NightModeQualifier.class, new NightModeEdit(mQualifierEditParent));
            mUiMap.put(DensityQualifier.class, new DensityEdit(mQualifierEditParent));
mUiMap.put(TouchScreenQualifier.class, new TouchEdit(mQualifierEditParent));
mUiMap.put(KeyboardStateQualifier.class, new KeyboardEdit(mQualifierEditParent));
mUiMap.put(TextInputMethodQualifier.class, new TextInputEdit(mQualifierEditParent));
//Synthetic comment -- @@ -1171,17 +1172,17 @@


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
//Synthetic comment -- @@ -1199,14 +1200,14 @@
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
//Synthetic comment -- @@ -1215,13 +1216,13 @@

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
//Synthetic comment -- @@ -1341,18 +1342,18 @@
/**
* Edit widget for {@link DockModeQualifier}.
*/
    private class UiModeEdit extends QualifierEditBase {

        private Combo mUiMode;

        public UiModeEdit(Composite parent) {
            super(parent, UiModeQualifier.NAME);

            mUiMode = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
            fillCombo(mUiMode, UiMode.values());

            mUiMode.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            mUiMode.addSelectionListener(new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
onDockModeChange();
}
//Synthetic comment -- @@ -1364,16 +1365,16 @@

protected void onDockModeChange() {
// update the current config
            int index = mUiMode.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setUiModeQualifier(
                        new UiModeQualifier(UiMode.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setUiModeQualifier(new UiModeQualifier());
}

// notify of change
//Synthetic comment -- @@ -1382,13 +1383,13 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            UiModeQualifier q = (UiModeQualifier)qualifier;

            UiMode value = q.getValue();
if (value == null) {
                mUiMode.clearSelection();
} else {
                mUiMode.select(UiMode.getIndex(value));
}
}
}
//Synthetic comment -- @@ -1450,13 +1451,13 @@


/**
     * Edit widget for {@link DensityQualifier}.
*/
    private class DensityEdit extends QualifierEditBase {
private Combo mDensity;

        public DensityEdit(Composite parent) {
            super(parent, DensityQualifier.NAME);

mDensity = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
fillCombo(mDensity, Density.values());
//Synthetic comment -- @@ -1477,14 +1478,14 @@
int index = mDensity.getSelectionIndex();

if (index != -1) {
                mSelectedConfiguration.setDensityQualifier(new DensityQualifier(
Density.getByIndex(index)));
} else {
// empty selection, means no qualifier.
// Since the qualifier classes are immutable, and we don't want to
// remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setDensityQualifier(
                        new DensityQualifier());
}

// notify of change
//Synthetic comment -- @@ -1493,7 +1494,7 @@

@Override
public void setQualifier(ResourceQualifier qualifier) {
            DensityQualifier q = (DensityQualifier)qualifier;

Density value = q.getValue();
if (value == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 67d838e..c060712 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.resources.ScreenLayoutSize.LARGE;
import static com.android.resources.ScreenLayoutSize.NORMAL;
import static com.android.resources.ScreenLayoutSize.XLARGE;

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index b7ebb49..30e9bf7 100644

//Synthetic comment -- @@ -28,14 +28,17 @@
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.KeyboardStateQualifier;
import com.android.ide.common.resources.configuration.NavigationMethodQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenLayoutSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -49,9 +52,9 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.ResourceType;
import com.android.resources.ScreenLayoutSize;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.TouchScreen;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -274,10 +277,13 @@
FolderConfiguration config = new FolderConfiguration();

// this matches an ADP1.
        config.addQualifier(new SmallestScreenWidthQualifier(320));
        config.addQualifier(new ScreenWidthQualifier(320));
        config.addQualifier(new ScreenHeightQualifier(480));
        config.addQualifier(new ScreenLayoutSizeQualifier(ScreenLayoutSize.NORMAL));
config.addQualifier(new ScreenRatioQualifier(ScreenRatio.NOTLONG));
config.addQualifier(new ScreenOrientationQualifier(ScreenOrientation.PORTRAIT));
        config.addQualifier(new DensityQualifier(Density.MEDIUM));
config.addQualifier(new TouchScreenQualifier(TouchScreen.FINGER));
config.addQualifier(new KeyboardStateQualifier(KeyboardState.HIDDEN));
config.addQualifier(new TextInputMethodQualifier(Keyboard.QWERTY));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index f2a6b54..46f19cf 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.ide.eclipse.mock.Mocks;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
//Synthetic comment -- @@ -34,6 +33,7 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ScreenOrientation;
import com.android.resources.TouchScreen;
import com.android.resources.UiMode;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -168,7 +168,7 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                UiMode.DESK.getResourceValue(), // dock mode
NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode
//Synthetic comment -- @@ -197,7 +197,7 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                UiMode.DESK.getResourceValue(), // dock mode
NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/SingleResourceFile.java b/ide_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index cd2b627..9c8977e 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.common.rendering.api.DensityBasedResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.io.IAbstractFile;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -57,7 +57,7 @@
mResourceName = getResourceName(mType);

// test if there's a density qualifier associated with the resource
        DensityQualifier qualifier = folder.getConfiguration().getDensityQualifier();

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
public final class DensityQualifier extends EnumBasedResourceQualifier {
private final static Pattern sDensityLegacyPattern = Pattern.compile("^(\\d+)dpi$");//$NON-NLS-1$

    public static final String NAME = "Density";

private Density mValue = Density.MEDIUM;

    public DensityQualifier() {
// pass
}

    public DensityQualifier(Density value) {
mValue = value;
}

//Synthetic comment -- @@ -79,9 +79,9 @@
}

if (density != null) {
            DensityQualifier qualifier = new DensityQualifier();
qualifier.mValue = density;
            config.setDensityQualifier(qualifier);
return true;
}

//Synthetic comment -- @@ -90,7 +90,7 @@

@Override
public boolean isMatchFor(ResourceQualifier qualifier) {
        if (qualifier instanceof DensityQualifier) {
// as long as there's a density qualifier, it's always a match.
// The best match will be found later.
return true;
//Synthetic comment -- @@ -105,8 +105,8 @@
return true;
}

        DensityQualifier compareQ = (DensityQualifier)compareTo;
        DensityQualifier referenceQ = (DensityQualifier)reference;

if (compareQ.mValue == referenceQ.mValue) {
// what we have is already the best possible match (exact match)








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index 8b04bac..c0d1293 100644

//Synthetic comment -- @@ -50,10 +50,10 @@
private final static int INDEX_SMALLEST_SCREEN_WIDTH = 4;
private final static int INDEX_SCREEN_WIDTH          = 5;
private final static int INDEX_SCREEN_HEIGHT         = 6;
    private final static int INDEX_SCREEN_LAYOUT_SIZE    = 7;
private final static int INDEX_SCREEN_RATIO          = 8;
private final static int INDEX_SCREEN_ORIENTATION    = 9;
    private final static int INDEX_UI_MODE               = 10;
private final static int INDEX_NIGHT_MODE            = 11;
private final static int INDEX_PIXEL_DENSITY         = 12;
private final static int INDEX_TOUCH_TYPE            = 13;
//Synthetic comment -- @@ -229,8 +229,8 @@
} else if (qualifier instanceof ScreenHeightQualifier) {
mQualifiers[INDEX_SCREEN_HEIGHT] = qualifier;

        } else if (qualifier instanceof ScreenLayoutSizeQualifier) {
            mQualifiers[INDEX_SCREEN_LAYOUT_SIZE] = qualifier;

} else if (qualifier instanceof ScreenRatioQualifier) {
mQualifiers[INDEX_SCREEN_RATIO] = qualifier;
//Synthetic comment -- @@ -238,13 +238,13 @@
} else if (qualifier instanceof ScreenOrientationQualifier) {
mQualifiers[INDEX_SCREEN_ORIENTATION] = qualifier;

        } else if (qualifier instanceof UiModeQualifier) {
            mQualifiers[INDEX_UI_MODE] = qualifier;

} else if (qualifier instanceof NightModeQualifier) {
mQualifiers[INDEX_NIGHT_MODE] = qualifier;

        } else if (qualifier instanceof DensityQualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;

} else if (qualifier instanceof TouchScreenQualifier) {
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
//Synthetic comment -- @@ -374,12 +374,12 @@
return (ScreenOrientationQualifier)mQualifiers[INDEX_SCREEN_ORIENTATION];
}

    public void setUiModeQualifier(UiModeQualifier qualifier) {
        mQualifiers[INDEX_UI_MODE] = qualifier;
}

    public UiModeQualifier getUiModeQualifier() {
        return (UiModeQualifier)mQualifiers[INDEX_UI_MODE];
}

public void setNightModeQualifier(NightModeQualifier qualifier) {
//Synthetic comment -- @@ -390,12 +390,12 @@
return (NightModeQualifier)mQualifiers[INDEX_NIGHT_MODE];
}

    public void setDensityQualifier(DensityQualifier qualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;
}

    public DensityQualifier getDensityQualifier() {
        return (DensityQualifier)mQualifiers[INDEX_PIXEL_DENSITY];
}

public void setTouchTypeQualifier(TouchScreenQualifier qualifier) {
//Synthetic comment -- @@ -457,7 +457,7 @@
/**
* Updates the {@link SmallestScreenWidthQualifier}, {@link ScreenWidthQualifier}, and
* {@link ScreenHeightQualifier} based on the (required) values of
     * {@link ScreenDimensionQualifier} {@link DensityQualifier}, and
* {@link ScreenOrientationQualifier}.
*
* Also the density cannot be {@link Density#NODPI} as it's not valid on a device.
//Synthetic comment -- @@ -469,7 +469,7 @@
ResourceQualifier orientQ = mQualifiers[INDEX_SCREEN_ORIENTATION];

if (sizeQ != null && densityQ != null && orientQ != null) {
            Density density = ((DensityQualifier) densityQ).getValue();
if (density == Density.NODPI) {
return;
}
//Synthetic comment -- @@ -846,12 +846,12 @@
mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = new SmallestScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_WIDTH] = new ScreenWidthQualifier();
mQualifiers[INDEX_SCREEN_HEIGHT] = new ScreenHeightQualifier();
        mQualifiers[INDEX_SCREEN_LAYOUT_SIZE] = new ScreenLayoutSizeQualifier();
mQualifiers[INDEX_SCREEN_RATIO] = new ScreenRatioQualifier();
mQualifiers[INDEX_SCREEN_ORIENTATION] = new ScreenOrientationQualifier();
        mQualifiers[INDEX_UI_MODE] = new UiModeQualifier();
mQualifiers[INDEX_NIGHT_MODE] = new NightModeQualifier();
        mQualifiers[INDEX_PIXEL_DENSITY] = new DensityQualifier();
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









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/DockModeQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/UiModeQualifier.java
similarity index 66%
rename from ide_common/src/com/android/ide/common/resources/configuration/DockModeQualifier.java
rename to ide_common/src/com/android/ide/common/resources/configuration/UiModeQualifier.java
//Synthetic comment -- index 2c832eb..f9ad0b7 100644

//Synthetic comment -- @@ -16,27 +16,27 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.ResourceEnum;
import com.android.resources.UiMode;

/**
 * Resource Qualifier for UI Mode.
*/
public final class UiModeQualifier extends EnumBasedResourceQualifier {

    public static final String NAME = "UI Mode";

    private UiMode mValue;

    public UiModeQualifier() {
// pass
}

    public UiModeQualifier(UiMode value) {
mValue = value;
}

    public UiMode getValue() {
return mValue;
}

//Synthetic comment -- @@ -52,15 +52,15 @@

@Override
public String getShortName() {
        return NAME;
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
        UiMode mode = UiMode.getEnum(value);
if (mode != null) {
            UiModeQualifier qualifier = new UiModeQualifier(mode);
            config.setUiModeQualifier(qualifier);
return true;
}

//Synthetic comment -- @@ -69,13 +69,13 @@

@Override
public boolean isMatchFor(ResourceQualifier qualifier) {
        // only normal is a match for all UI mode, because it's not an actual mode.
        if (mValue == UiMode.NORMAL) {
return true;
}

// others must be an exact match
        return ((UiModeQualifier)qualifier).mValue == mValue;
}

@Override
//Synthetic comment -- @@ -84,8 +84,8 @@
return true;
}

        UiModeQualifier compareQualifier = (UiModeQualifier)compareTo;
        UiModeQualifier referenceQualifier = (UiModeQualifier)reference;

if (compareQualifier.getValue() == referenceQualifier.getValue()) {
// what we have is already the best possible match (exact match)
//Synthetic comment -- @@ -93,8 +93,8 @@
} else  if (mValue == referenceQualifier.mValue) {
// got new exact value, this is the best!
return true;
        } else if (mValue == UiMode.NORMAL) {
            // else "normal" can be a match in case there's no exact match
return true;
}









//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java
//Synthetic comment -- index c2b7452..1653805 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package com.android.ide.common.resources.configuration;

import com.android.resources.UiMode;

import junit.framework.TestCase;

public class DockModeQualifierTest extends TestCase {

    private UiModeQualifier mCarQualifier;
    private UiModeQualifier mDeskQualifier;
    private UiModeQualifier mTVQualifier;
    private UiModeQualifier mNoneQualifier;

@Override
protected void setUp() throws Exception {
super.setUp();
        mCarQualifier = new UiModeQualifier(UiMode.CAR);
        mDeskQualifier = new UiModeQualifier(UiMode.DESK);
        mTVQualifier = new UiModeQualifier(UiMode.TELEVISION);
        mNoneQualifier = new UiModeQualifier(UiMode.NORMAL);
}

@Override








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/PixelDensityQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/PixelDensityQualifierTest.java
//Synthetic comment -- index b99f2af..4ab493a 100644

//Synthetic comment -- @@ -22,13 +22,13 @@

public class PixelDensityQualifierTest extends TestCase {

    private DensityQualifier pdq;
private FolderConfiguration config;

@Override
protected void setUp() throws Exception {
super.setUp();
        pdq = new DensityQualifier();
config = new FolderConfiguration();
}

//Synthetic comment -- @@ -41,9 +41,9 @@

public void testCheckAndSet() {
assertEquals(true, pdq.checkAndSet("ldpi", config));//$NON-NLS-1$
        assertTrue(config.getDensityQualifier() != null);
        assertEquals(Density.LOW, config.getDensityQualifier().getValue());
        assertEquals("ldpi", config.getDensityQualifier().toString()); //$NON-NLS-1$
}

public void testFailures() {
//Synthetic comment -- @@ -55,10 +55,10 @@
}

public void testIsBetterMatchThan() {
        DensityQualifier ldpi = new DensityQualifier(Density.LOW);
        DensityQualifier mdpi = new DensityQualifier(Density.MEDIUM);
        DensityQualifier hdpi = new DensityQualifier(Density.HIGH);
        DensityQualifier xhdpi = new DensityQualifier(Density.XHIGH);

// first test that each Q is a better match than all other Qs when the ref is the same Q.
assertTrue(ldpi.isBetterMatchThan(mdpi, ldpi));








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/ScreenSizeQualifierTest.java
//Synthetic comment -- index b19f125..d05399d 100644

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
//Synthetic comment -- index 2cfe770..32c1ff2 100644

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








