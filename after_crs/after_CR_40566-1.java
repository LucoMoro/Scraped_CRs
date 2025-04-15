/*Catch exceptions during class initialization.

This primarily fixes running various unit tests that are
accessing this class (to test static methods) but fail
because class initialization looking for icons fails
when not running as a plugin test.

Change-Id:I77d62215f34ab8c8ed4c7ee96b9e6669d0468085*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 68eb85d..4cc56d5 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
//Synthetic comment -- @@ -26,6 +25,7 @@
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceDeltaKind;
//Synthetic comment -- @@ -120,27 +120,31 @@
FolderConfiguration.getQualifierCount());

static {
        try {
            IconFactory factory = IconFactory.getInstance();
            sIconMap.put(CountryCodeQualifier.class,        factory.getIcon("mcc")); //$NON-NLS-1$
            sIconMap.put(NetworkCodeQualifier.class,        factory.getIcon("mnc")); //$NON-NLS-1$
            sIconMap.put(LanguageQualifier.class,           factory.getIcon("language")); //$NON-NLS-1$
            sIconMap.put(RegionQualifier.class,             factory.getIcon("region")); //$NON-NLS-1$
            sIconMap.put(ScreenSizeQualifier.class,         factory.getIcon("size")); //$NON-NLS-1$
            sIconMap.put(ScreenRatioQualifier.class,        factory.getIcon("ratio")); //$NON-NLS-1$
            sIconMap.put(ScreenOrientationQualifier.class,  factory.getIcon("orientation")); //$NON-NLS-1$
            sIconMap.put(UiModeQualifier.class,             factory.getIcon("dockmode")); //$NON-NLS-1$
            sIconMap.put(NightModeQualifier.class,          factory.getIcon("nightmode")); //$NON-NLS-1$
            sIconMap.put(DensityQualifier.class,            factory.getIcon("dpi")); //$NON-NLS-1$
            sIconMap.put(TouchScreenQualifier.class,        factory.getIcon("touch")); //$NON-NLS-1$
            sIconMap.put(KeyboardStateQualifier.class,      factory.getIcon("keyboard")); //$NON-NLS-1$
            sIconMap.put(TextInputMethodQualifier.class,    factory.getIcon("text_input")); //$NON-NLS-1$
            sIconMap.put(NavigationStateQualifier.class,    factory.getIcon("navpad")); //$NON-NLS-1$
            sIconMap.put(NavigationMethodQualifier.class,   factory.getIcon("navpad")); //$NON-NLS-1$
            sIconMap.put(ScreenDimensionQualifier.class,    factory.getIcon("dimension")); //$NON-NLS-1$
            sIconMap.put(VersionQualifier.class,            factory.getIcon("version")); //$NON-NLS-1$
            sIconMap.put(ScreenWidthQualifier.class,        factory.getIcon("width")); //$NON-NLS-1$
            sIconMap.put(ScreenHeightQualifier.class,       factory.getIcon("height")); //$NON-NLS-1$
            sIconMap.put(SmallestScreenWidthQualifier.class,factory.getIcon("swidth")); //$NON-NLS-1$
        } catch (Throwable t) {
            AdtPlugin.log(t , null);
        }
}

/**







