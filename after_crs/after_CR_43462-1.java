/*Ensure that themes are always style-prefixed

Plus a few other minor tweaks/bug fixes

Change-Id:Ic02db9123674ebfbc8977a94d1da4826ec13fb9b*/




//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 8c798c8..04fa987 100644

//Synthetic comment -- @@ -945,7 +945,7 @@
public static final String ATTR_REF_PREFIX = "?attr/";               //$NON-NLS-1$
public static final String R_PREFIX = "R.";                          //$NON-NLS-1$
public static final String R_ID_PREFIX = "R.id.";                    //$NON-NLS-1$
    public static final String R_LAYOUT_RESOURCE_PREFIX = "R.layout.";   //$NON-NLS-1$
public static final String R_DRAWABLE_PREFIX = "R.drawable.";        //$NON-NLS-1$
public static final String R_ATTR_PREFIX = "R.attr.";                //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 426dbae..d0d328e 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.assetstudio;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.DEFAULT_LAUNCHER_ICON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -481,7 +480,7 @@
// Initial image - use the most recently used image, or the default launcher
// icon created in our default projects, if there
if (mValues.imagePath != null) {
                sImagePath = mValues.imagePath.getPath();
}
if (sImagePath == null) {
IProject project = mValues.project;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 98f5317..253421f 100644

//Synthetic comment -- @@ -560,8 +560,8 @@
}

@Override
    public AdapterBinding getAdapterBinding(final ResourceReference adapterView,
            final Object adapterCookie, final Object viewObject) {
// Look for user-recorded preference for layout to be used for previews
if (adapterCookie instanceof UiViewElementNode) {
UiViewElementNode uiNode = (UiViewElementNode) adapterCookie;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ActivityMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ActivityMenuListener.java
//Synthetic comment -- index 1f85a32..158a647 100644

//Synthetic comment -- @@ -88,7 +88,7 @@

if (current != null) {
MenuItem item = new MenuItem(menu, SWT.PUSH);
            String label = ConfigurationChooser.getActivityLabel(current, true);
item.setText( String.format("Open %1$s...", label));
Image image = sharedImages.getImage(ISharedImages.IMG_OBJS_CUNIT);
item.setImage(image);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 2106f8d..2b5589b 100644

//Synthetic comment -- @@ -17,11 +17,13 @@
package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.SdkConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -30,13 +32,17 @@
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;
import com.android.resources.UiMode;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -49,6 +55,7 @@
import org.eclipse.core.runtime.QualifiedName;

import java.util.List;
import java.util.Map;

/**
* A {@linkplain Configuration} is a selection of device, orientation, theme,
//Synthetic comment -- @@ -397,6 +404,7 @@
*/
public void setTheme(String theme) {
mTheme = theme;
        checkThemePrefix();
}

/**
//Synthetic comment -- @@ -507,6 +515,67 @@
return sb.toString();
}

    /** Returns the preferred theme, or null */
    @Nullable
    String computePreferredTheme() {
        IProject project = mConfigChooser.getProject();
        ManifestInfo manifest = ManifestInfo.get(project);

        // Look up the screen size for the current state
        ScreenSize screenSize = null;
        Device device = getDevice();
        if (device != null) {
            List<State> states = device.getAllStates();
            for (State state : states) {
                FolderConfiguration folderConfig = DeviceConfigHelper.getFolderConfig(state);
                if (folderConfig != null) {
                    ScreenSizeQualifier qualifier = folderConfig.getScreenSizeQualifier();
                    screenSize = qualifier.getValue();
                    break;
                }
            }
        }

        // Look up the default/fallback theme to use for this project (which
        // depends on the screen size when no particular theme is specified
        // in the manifest)
        String defaultTheme = manifest.getDefaultTheme(getTarget(), screenSize);

        String preferred = defaultTheme;
        if (getTheme() == null) {
            // If we are rendering a layout in included context, pick the theme
            // from the outer layout instead

            String activity = getActivity();
            if (activity != null) {
                Map<String, String> activityThemes = manifest.getActivityThemes();
                preferred = activityThemes.get(activity);
            }
            if (preferred == null) {
                preferred = defaultTheme;
            }
            setTheme(preferred);
        }

        return preferred;
    }

    private void checkThemePrefix() {
        if (mTheme != null && !mTheme.startsWith(PREFIX_RESOURCE_REF)) {
            if (mTheme.isEmpty()) {
                computePreferredTheme();
                return;
            }
            ResourceRepository frameworkRes = mConfigChooser.getClient().getFrameworkResources();
            if (frameworkRes != null
                    && frameworkRes.hasResourceItem(ANDROID_STYLE_RESOURCE_PREFIX + mTheme)) {
                mTheme = ANDROID_STYLE_RESOURCE_PREFIX + mTheme;
            } else {
                mTheme = STYLE_RESOURCE_PREFIX + mTheme;
            }
        }
    }

/**
* Initializes a string previously created with
* {@link #toPersistentString()}
//Synthetic comment -- @@ -555,6 +624,8 @@
} else if (mTheme.startsWith(MARKER_PROJECT)) {
mTheme = STYLE_RESOURCE_PREFIX
+ mTheme.substring(MARKER_PROJECT.length());
                        } else {
                            checkThemePrefix();
}

mUiMode = UiMode.getEnum(values[4]);
//Synthetic comment -- @@ -604,7 +675,7 @@
@Nullable
static Pair<Locale, IAndroidTarget> loadRenderState(ConfigurationChooser chooser) {
IProject project = chooser.getProject();
        if (project == null || !project.isAccessible()) {
return null;
}

//Synthetic comment -- @@ -668,6 +739,9 @@
*/
void saveRenderState() {
IProject project = mConfigChooser.getProject();
        if (project == null) {
            return;
        }
try {
// Generate a persistent string from locale+target
StringBuilder sb = new StringBuilder();
//Synthetic comment -- @@ -700,7 +774,7 @@
* @return an id for the given target; never null
*/
@NonNull
    public static String targetToString(@NonNull IAndroidTarget target) {
return target.getFullName().replace(SEP, "");  //$NON-NLS-1$
}

//Synthetic comment -- @@ -715,7 +789,7 @@
* @return an {@link IAndroidTarget} that matches the given id, or null
*/
@Nullable
    public static IAndroidTarget stringToTarget(
@NonNull ConfigurationChooser chooser,
@NonNull String id) {
List<IAndroidTarget> targetList = chooser.getTargetList();
//Synthetic comment -- @@ -731,6 +805,30 @@
}

/**
     * Returns an {@link IAndroidTarget} that corresponds to the given id that was
     * originally returned by {@link #targetToString}. May be null, if the platform is no
     * longer available, or if the platform list has not yet been initialized.
     *
     * @param id the id that corresponds to the desired platform
     * @return an {@link IAndroidTarget} that matches the given id, or null
     */
    @Nullable
    public static IAndroidTarget stringToTarget(
            @NonNull String id) {
        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk != null) {
            IAndroidTarget[] targets = currentSdk.getTargets();
            for (IAndroidTarget target : targets) {
                if (id.equals(targetToString(target))) {
                    return target;
                }
            }
        }

        return null;
    }

    /**
* Returns the {@link State} by the given name for the given {@link Device}
*
* @param device the device








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index b512bcc..7412bf1 100644

//Synthetic comment -- @@ -44,7 +44,6 @@
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -58,7 +57,6 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
//Synthetic comment -- @@ -298,19 +296,42 @@
addDisposeListener(this);
}

    /**
     * Returns the edited file
     *
     * @return the file
     */
    @Nullable
    public IFile getEditedFile() {
return mEditedFile;
}

    /**
     * Returns the project of the edited file
     *
     * @return the project
     */
    @Nullable
    public IProject getProject() {
        if (mEditedFile != null) {
            return mEditedFile.getProject();
        } else {
            return null;
        }
}

ConfigurationClient getClient() {
return mClient;
}

    /**
     * Returns the project resources for the project being configured by this
     * chooser
     *
     * @return the project resources
     */
    @Nullable
    public ProjectResources getResources() {
return mResources;
}

//Synthetic comment -- @@ -328,7 +349,7 @@
*
* @return the project target
*/
    public IAndroidTarget getProjectTarget() {
return mProjectTarget;
}

//Synthetic comment -- @@ -746,6 +767,7 @@
if (target != null) {
targetData = Sdk.getCurrent().getTargetData(target);
selectTarget(target);
                            mConfiguration.setTarget(target, true);
}
}

//Synthetic comment -- @@ -1588,7 +1610,7 @@
String theme = mConfiguration.getTheme();
if (theme == null || theme.isEmpty() || mClient.getIncludedWithin() != null) {
mConfiguration.setTheme(null);
                    mConfiguration.computePreferredTheme();
}
assert mConfiguration.getTheme() != null;
}
//Synthetic comment -- @@ -1680,6 +1702,14 @@
break;
}
}
                if (!theme.startsWith(PREFIX_RESOURCE_REF)) {
                    // Arbitrary guess
                    if (theme.startsWith("Theme.")) {
                        theme = ANDROID_STYLE_RESOURCE_PREFIX + theme;
                    } else {
                        theme = STYLE_RESOURCE_PREFIX + theme;
                    }
                }
}

// TODO: Handle the case where you have a theme persisted that isn't available??
//Synthetic comment -- @@ -1748,55 +1778,6 @@
}
}

@Nullable
private String getPreferredActivity(@NonNull IFile file) {
// Store/restore the activity context in the config state to help with








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java
//Synthetic comment -- index 239f396..0f6c9eb 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
manager.add(new Separator());
}

        String preferred = configuration.computePreferredTheme();
if (preferred != null && !preferred.equals(currentTheme)) {
manager.add(new SelectThemeAction(configChooser,
ResourceHelper.styleToTheme(preferred),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 60e9920..5ca8e9c 100644

//Synthetic comment -- @@ -377,11 +377,15 @@
*
* @return a pair of possibly null color descriptions
*/
    @NonNull
private Pair<RGB, RGB> getColorsFromTheme() {
RGB background = null;
RGB foreground = null;

ResourceResolver resources = mPalette.getEditor().getResourceResolver();
        if (resources == null) {
            return Pair.of(background, foreground);
        }
StyleResourceValue theme = resources.getCurrentTheme();
if (theme != null) {
background = resolveThemeColor(resources, "windowBackground"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index 72577a5..d2d7878 100644

//Synthetic comment -- @@ -137,7 +137,7 @@
// TODO: do I have to strip off the @ too?
isFramework = isFramework
|| value.startsWith(ANDROID_PREFIX)
                                    || value.startsWith(ANDROID_THEME_PREFIX);
ResourceValue v = resolver.findResValue(text, isFramework);
if (v != null && !value.equals(v.getValue())) {
resValue = v;







