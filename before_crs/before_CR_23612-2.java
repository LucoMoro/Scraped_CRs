/*Add support for the new width/height resource qualifiers.

Also add Television to the dock mode (which is really UI Mode).

Change-Id:I97e3fbea1806a32d8267d8e462211add2b122ed0*/
//Synthetic comment -- diff --git a/common/src/com/android/resources/DockMode.java b/common/src/com/android/resources/DockMode.java
//Synthetic comment -- index bbae6bf..71515f9 100644

//Synthetic comment -- @@ -23,7 +23,8 @@
public enum DockMode implements ResourceEnum {
NONE("", "No Dock"),
CAR("car", "Car Dock"),
    DESK("desk", "Desk Dock");

private final String mValue;
private final String mDisplayValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index 2a2f0d0..01cdde0 100644

//Synthetic comment -- @@ -192,7 +192,12 @@
* @return true if the parsing failed, false if success.
*/
public static boolean parseOutput(List<String> results, IProject project) {
        return parseOutput(results.toArray(new String[results.size()]), project);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index d9b3911..86b8cd5 100644

//Synthetic comment -- @@ -131,6 +131,7 @@

public void getConfig(FolderConfiguration config) {
config.set(mConfig);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java
//Synthetic comment -- index 3ad4e61..eb3853a 100644

//Synthetic comment -- @@ -120,6 +120,7 @@
mCurrentDevice = null;
mDefaultConfig = null;
} else if (LayoutDevicesXsd.NODE_CONFIG.equals(localName)) {
mCurrentConfig = null;
} else if (LayoutDevicesXsd.NODE_COUNTRY_CODE.equals(localName)) {
CountryCodeQualifier ccq = new CountryCodeQualifier(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index 74120fd..ffef837 100644

//Synthetic comment -- @@ -30,9 +30,12 @@
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
//Synthetic comment -- @@ -422,6 +425,10 @@
mUiMap.put(NetworkCodeQualifier.class, new MNCEdit(mQualifierEditParent));
mUiMap.put(LanguageQualifier.class, new LanguageEdit(mQualifierEditParent));
mUiMap.put(RegionQualifier.class, new RegionEdit(mQualifierEditParent));
mUiMap.put(ScreenSizeQualifier.class, new ScreenSizeEdit(mQualifierEditParent));
mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
//Synthetic comment -- @@ -966,6 +973,204 @@
}

/**
* Edit widget for {@link ScreenSizeQualifier}.
*/
private class ScreenSizeEdit extends QualifierEditBase {








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index 09cf9e4..8b04bac 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package com.android.ide.common.resources.configuration;

import com.android.AndroidConstants;
import com.android.resources.ResourceFolderType;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -41,24 +43,27 @@

private final ResourceQualifier[] mQualifiers = new ResourceQualifier[INDEX_COUNT];

    private final static int INDEX_COUNTRY_CODE       = 0;
    private final static int INDEX_NETWORK_CODE       = 1;
    private final static int INDEX_LANGUAGE           = 2;
    private final static int INDEX_REGION             = 3;
    private final static int INDEX_SCREEN_SIZE        = 4;
    private final static int INDEX_SCREEN_RATIO       = 5;
    private final static int INDEX_SCREEN_ORIENTATION = 6;
    private final static int INDEX_DOCK_MODE          = 7;
    private final static int INDEX_NIGHT_MODE         = 8;
    private final static int INDEX_PIXEL_DENSITY      = 9;
    private final static int INDEX_TOUCH_TYPE         = 10;
    private final static int INDEX_KEYBOARD_STATE     = 11;
    private final static int INDEX_TEXT_INPUT_METHOD  = 12;
    private final static int INDEX_NAVIGATION_STATE   = 13;
    private final static int INDEX_NAVIGATION_METHOD  = 14;
    private final static int INDEX_SCREEN_DIMENSION   = 15;
    private final static int INDEX_VERSION            = 16;
    private final static int INDEX_COUNT              = 17;

/**
* Creates a {@link FolderConfiguration} matching the folder segments.
//Synthetic comment -- @@ -205,38 +210,64 @@
public void addQualifier(ResourceQualifier qualifier) {
if (qualifier instanceof CountryCodeQualifier) {
mQualifiers[INDEX_COUNTRY_CODE] = qualifier;
} else if (qualifier instanceof NetworkCodeQualifier) {
mQualifiers[INDEX_NETWORK_CODE] = qualifier;
} else if (qualifier instanceof LanguageQualifier) {
mQualifiers[INDEX_LANGUAGE] = qualifier;
} else if (qualifier instanceof RegionQualifier) {
mQualifiers[INDEX_REGION] = qualifier;
} else if (qualifier instanceof ScreenSizeQualifier) {
mQualifiers[INDEX_SCREEN_SIZE] = qualifier;
} else if (qualifier instanceof ScreenRatioQualifier) {
mQualifiers[INDEX_SCREEN_RATIO] = qualifier;
} else if (qualifier instanceof ScreenOrientationQualifier) {
mQualifiers[INDEX_SCREEN_ORIENTATION] = qualifier;
} else if (qualifier instanceof DockModeQualifier) {
mQualifiers[INDEX_DOCK_MODE] = qualifier;
} else if (qualifier instanceof NightModeQualifier) {
mQualifiers[INDEX_NIGHT_MODE] = qualifier;
} else if (qualifier instanceof PixelDensityQualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;
} else if (qualifier instanceof TouchScreenQualifier) {
mQualifiers[INDEX_TOUCH_TYPE] = qualifier;
} else if (qualifier instanceof KeyboardStateQualifier) {
mQualifiers[INDEX_KEYBOARD_STATE] = qualifier;
} else if (qualifier instanceof TextInputMethodQualifier) {
mQualifiers[INDEX_TEXT_INPUT_METHOD] = qualifier;
} else if (qualifier instanceof NavigationStateQualifier) {
mQualifiers[INDEX_NAVIGATION_STATE] = qualifier;
} else if (qualifier instanceof NavigationMethodQualifier) {
mQualifiers[INDEX_NAVIGATION_METHOD] = qualifier;
} else if (qualifier instanceof ScreenDimensionQualifier) {
mQualifiers[INDEX_SCREEN_DIMENSION] = qualifier;
} else if (qualifier instanceof VersionQualifier) {
mQualifiers[INDEX_VERSION] = qualifier;
}
}

//Synthetic comment -- @@ -295,6 +326,30 @@
return (RegionQualifier)mQualifiers[INDEX_REGION];
}

public void setScreenSizeQualifier(ScreenSizeQualifier qualifier) {
mQualifiers[INDEX_SCREEN_SIZE] = qualifier;
}
//Synthetic comment -- @@ -400,6 +455,61 @@
}

/**
* Returns whether an object is equals to the receiver.
*/
@Override
//Synthetic comment -- @@ -704,6 +814,7 @@
return false;
}
}
return true;
}

//Synthetic comment -- @@ -732,6 +843,9 @@
mQualifiers[INDEX_NETWORK_CODE] = new NetworkCodeQualifier();
mQualifiers[INDEX_LANGUAGE] = new LanguageQualifier();
mQualifiers[INDEX_REGION] = new RegionQualifier();
mQualifiers[INDEX_SCREEN_SIZE] = new ScreenSizeQualifier();
mQualifiers[INDEX_SCREEN_RATIO] = new ScreenRatioQualifier();
mQualifiers[INDEX_SCREEN_ORIENTATION] = new ScreenOrientationQualifier();








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java
//Synthetic comment -- index a58789a..9b42b88 100644

//Synthetic comment -- @@ -144,7 +144,7 @@

@Override
public String getShortDisplayValue() {
        if (mValue1 != -1 && mValue2 != -1) {
return String.format("%1$dx%2$d", mValue1, mValue2);
}

//Synthetic comment -- @@ -153,7 +153,7 @@

@Override
public String getLongDisplayValue() {
        if (mValue1 != -1 && mValue2 != -1) {
return String.format("Screen resolution %1$dx%2$d", mValue1, mValue2);
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..2899631

//Synthetic comment -- @@ -0,0 +1,169 @@








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..8748864

//Synthetic comment -- @@ -0,0 +1,169 @@








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..e151805

//Synthetic comment -- @@ -0,0 +1,169 @@








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java
//Synthetic comment -- index 195d474..c2b7452 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

private DockModeQualifier mCarQualifier;
private DockModeQualifier mDeskQualifier;
private DockModeQualifier mNoneQualifier;

@Override
//Synthetic comment -- @@ -31,6 +32,7 @@
super.setUp();
mCarQualifier = new DockModeQualifier(DockMode.CAR);
mDeskQualifier = new DockModeQualifier(DockMode.DESK);
mNoneQualifier = new DockModeQualifier(DockMode.NONE);
}

//Synthetic comment -- @@ -39,11 +41,13 @@
super.tearDown();
mCarQualifier = null;
mDeskQualifier = null;
mNoneQualifier = null;
}

public void testIsBetterMatchThan() {
assertTrue(mNoneQualifier.isBetterMatchThan(mCarQualifier, mDeskQualifier));
assertFalse(mNoneQualifier.isBetterMatchThan(mDeskQualifier, mDeskQualifier));
assertTrue(mNoneQualifier.isBetterMatchThan(mDeskQualifier, mCarQualifier));
assertFalse(mNoneQualifier.isBetterMatchThan(mCarQualifier, mCarQualifier));
//Synthetic comment -- @@ -53,17 +57,27 @@

assertTrue(mCarQualifier.isBetterMatchThan(mDeskQualifier, mCarQualifier));
assertFalse(mCarQualifier.isBetterMatchThan(mDeskQualifier, mDeskQualifier));
}

public void testIsMatchFor() {
assertTrue(mNoneQualifier.isMatchFor(mCarQualifier));
assertTrue(mNoneQualifier.isMatchFor(mDeskQualifier));
assertTrue(mCarQualifier.isMatchFor(mCarQualifier));
assertTrue(mDeskQualifier.isMatchFor(mDeskQualifier));

assertFalse(mCarQualifier.isMatchFor(mNoneQualifier));
assertFalse(mCarQualifier.isMatchFor(mDeskQualifier));
assertFalse(mDeskQualifier.isMatchFor(mCarQualifier));
assertFalse(mDeskQualifier.isMatchFor(mNoneQualifier));
}
}







