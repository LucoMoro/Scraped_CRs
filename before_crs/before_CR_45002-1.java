/*Fix multiconfiguration editing bugs

This CL fixes a couple of bugs, related to showing the correct labels,
and picking multiple screen sizes. Also cleans up the code a bit.

Change-Id:I5c01555aad8339f68788d8aed1f707d30993ae1b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index 7ff0354..1fdcad3 100644

//Synthetic comment -- @@ -47,13 +47,6 @@
* inherited device is a tablet, or vice versa.
*/
public class ComplementingConfiguration extends NestedConfiguration {
    /**
     * If non zero, keep the display name up to date with the label for the
     * given overridden attribute, according to the flag constants in
     * {@link ConfigurationClient}
     */
    private int mUpdateDisplayName;

/** Variation version; see {@link #setVariation(int)} */
private int mVariation;

//Synthetic comment -- @@ -104,17 +97,20 @@
new ComplementingConfiguration(other.mConfigChooser, parent);
configuration.setDisplayName(other.getDisplayName());
configuration.setActivity(other.getActivity());
        configuration.mUpdateDisplayName = other.mUpdateDisplayName;
        configuration.mOverrideLocale = other.mOverrideLocale;
        configuration.mOverrideTarget = other.mOverrideTarget;
        configuration.mOverrideDevice = other.mOverrideDevice;
        configuration.mOverrideDeviceState = other.mOverrideDeviceState;
        configuration.mOverrideNightMode = other.mOverrideNightMode;
        configuration.mOverrideUiMode = other.mOverrideUiMode;

return configuration;
}

/**
* Sets the variation version for this
* {@linkplain ComplementingConfiguration}. There might be multiple
//Synthetic comment -- @@ -143,47 +139,18 @@
mVariationCount = count;
}

    @Override
    public void setOverrideDevice(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_DEVICE;
        super.setOverrideDevice(override);
    }

    @Override
    public void setOverrideDeviceState(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_DEVICE_CONFIG;
        super.setOverrideDeviceState(override);
    }

    @Override
    public void setOverrideLocale(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_LOCALE;
        super.setOverrideLocale(override);
    }

    @Override
    public void setOverrideTarget(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_RENDER_TARGET;
        super.setOverrideTarget(override);
    }

    @Override
    public void setOverrideNightMode(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_NIGHT_MODE;
        super.setOverrideNightMode(override);
    }

    @Override
    public void setOverrideUiMode(boolean override) {
        mUpdateDisplayName |= ConfigurationClient.CHANGED_UI_MODE;
        super.setOverrideUiMode(override);
}

@Override
@NonNull
public Locale getLocale() {
Locale locale = mParent.getLocale();
        if (mOverrideLocale && locale != null) {
List<Locale> locales = mConfigChooser.getLocaleList();
for (Locale l : locales) {
// TODO: Try to be smarter about which one we pick; for example, try
//Synthetic comment -- @@ -195,10 +162,6 @@
break;
}
}

            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_LOCALE) != 0) {
                setDisplayName(ConfigurationChooser.getLocaleLabel(mConfigChooser, locale, false));
            }
}

return locale;
//Synthetic comment -- @@ -208,7 +171,7 @@
@Nullable
public IAndroidTarget getTarget() {
IAndroidTarget target = mParent.getTarget();
        if (mOverrideTarget && target != null) {
List<IAndroidTarget> targets = mConfigChooser.getTargetList();
if (!targets.isEmpty()) {
// Pick a different target: if you're showing the most recent render target,
//Synthetic comment -- @@ -228,24 +191,28 @@
target = mostRecent;
}
}

            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_RENDER_TARGET) != 0) {
                setDisplayName(ConfigurationChooser.getRenderingTargetLabel(target, false));
            }
}

return target;
}

@Override
@Nullable
public Device getDevice() {
Device device = mParent.getDevice();
        if (mOverrideDevice && device != null) {
// Pick a different device
List<Device> devices = mConfigChooser.getDeviceList();


// Divide up the available devices into {@link #mVariationCount} + 1 buckets
// (the + 1 is for the bucket now taken up by the inherited value).
// Then assign buckets to each {@link #mVariation} version, and pick one
//Synthetic comment -- @@ -300,9 +267,7 @@
}
}

            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_DEVICE) != 0) {
                setDisplayName(ConfigurationChooser.getDeviceLabel(device, true));
            }
}

return device;
//Synthetic comment -- @@ -343,18 +308,12 @@
@Nullable
public State getDeviceState() {
State state = mParent.getDeviceState();
        if (mOverrideDeviceState && state != null) {
State alternate = getNextDeviceState(state);

            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_DEVICE_CONFIG) != 0) {
                if (alternate != null) {
                    setDisplayName(alternate.getName());
                }
            }

return alternate;
} else {
            if (mOverrideDevice && state != null) {
// If the device differs, I need to look up a suitable equivalent state
// on our device
Device device = getDevice();
//Synthetic comment -- @@ -371,11 +330,8 @@
@NonNull
public NightMode getNightMode() {
NightMode nightMode = mParent.getNightMode();
        if (mOverrideNightMode && nightMode != null) {
nightMode = nightMode == NightMode.NIGHT ? NightMode.NOTNIGHT : NightMode.NIGHT;
            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_NIGHT_MODE) != 0) {
                setDisplayName(nightMode.getLongDisplayValue());
            }
return nightMode;
} else {
return nightMode;
//Synthetic comment -- @@ -386,15 +342,12 @@
@NonNull
public UiMode getUiMode() {
UiMode uiMode = mParent.getUiMode();
        if (mOverrideUiMode && uiMode != null) {
// TODO: Use manifest's supports screen to decide which are most relevant
// (as well as which available configuration qualifiers are present in the
// layout)
UiMode[] values = UiMode.values();
uiMode = values[(uiMode.ordinal() + 1) % values.length];
            if ((mUpdateDisplayName & ConfigurationClient.CHANGED_UI_MODE) != 0) {
                setDisplayName(uiMode.getLongDisplayValue());
            }
return uiMode;
} else {
return uiMode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 00caccd..979e102 100644

//Synthetic comment -- @@ -63,6 +63,26 @@
* etc for use when rendering a layout.
*/
public class Configuration {
/**
* Setting name for project-wide setting controlling rendering target and locale which
* is shared for all files








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index 592fa32..a95fc31 100644

//Synthetic comment -- @@ -24,13 +24,13 @@
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.ide.eclipse.adt.AdtUtils.isUiThread;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_ALL;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_DEVICE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_DEVICE_CONFIG;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_FOLDER;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_LOCALE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_RENDER_TARGET;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_THEME;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -603,14 +603,14 @@
*/
public void setConfiguration(@NonNull Configuration configuration) {
if (mClient != null) {
            mClient.aboutToChange(CHANGED_ALL);
}

Configuration oldConfiguration = mConfiguration;
mConfiguration = configuration;

if (mClient != null) {
            mClient.changed(CHANGED_ALL);
}

selectTheme(configuration.getTheme());
//Synthetic comment -- @@ -622,7 +622,7 @@

// This may be a second refresh after triggered by theme above
if (mClient != null) {
            boolean accepted = mClient.changed(CHANGED_ALL);
if (!accepted) {
configuration = oldConfiguration;
selectTheme(configuration.getTheme());
//Synthetic comment -- @@ -811,7 +811,7 @@
matcher.adaptConfigSelection(true /*needBestMatch*/);
mConfiguration.syncFolderConfig();
if (mClient != null) {
                mClient.changed(CHANGED_ALL);
}
}
}
//Synthetic comment -- @@ -1356,7 +1356,7 @@
mConfiguration.syncFolderConfig();

// Notify
        boolean accepted = mClient.changed(CHANGED_DEVICE | CHANGED_DEVICE_CONFIG);
if (!accepted) {
mConfiguration.setDevice(prevDevice, true);
mConfiguration.setDeviceState(prevState, true);
//Synthetic comment -- @@ -1384,7 +1384,7 @@
mConfiguration.setDeviceState(state, false);

if (mClient != null) {
            boolean accepted = mClient.changed(CHANGED_DEVICE | CHANGED_DEVICE_CONFIG);
if (!accepted) {
mConfiguration.setDeviceState(prev, false);
selectDeviceState(prev);
//Synthetic comment -- @@ -1413,7 +1413,7 @@
mConfiguration.setLocale(locale, false);

if (mClient != null) {
            boolean accepted = mClient.changed(CHANGED_LOCALE);
if (!accepted) {
mConfiguration.setLocale(prev, false);
selectLocale(prev);
//Synthetic comment -- @@ -1434,7 +1434,7 @@
mConfiguration.setTheme((String) mThemeCombo.getData());

if (mClient != null) {
            boolean accepted = mClient.changed(CHANGED_THEME);
if (!accepted) {
mConfiguration.setTheme(prev);
selectTheme(prev);
//Synthetic comment -- @@ -1450,7 +1450,7 @@
return;
}

        if (mClient.changed(CHANGED_FOLDER)) {
saveConstraints();
}
}
//Synthetic comment -- @@ -1503,7 +1503,7 @@
// tell the listener a new rendering target is being set. Need to do this before updating
// mRenderingTarget.
if (prevTarget != null) {
            changeFlags |= CHANGED_RENDER_TARGET;
mClient.aboutToChange(changeFlags);
}

//Synthetic comment -- @@ -1518,12 +1518,12 @@
// updateThemes may change the theme (based on theme availability in the new rendering
// target) so mark theme change if necessary
if (!Objects.equal(oldTheme, mConfiguration.getTheme())) {
            changeFlags |= CHANGED_THEME;
}

if (target != null) {
            changeFlags |= CHANGED_RENDER_TARGET;
            changeFlags |= CHANGED_FOLDER; // In case we added a -vNN qualifier
}

// Store project-wide render-target setting
//Synthetic comment -- @@ -1571,7 +1571,7 @@
if (locale != null) {
boolean localeChanged = setLocale(locale);
if (localeChanged) {
                    changeFlags |= CHANGED_LOCALE;
}
} else {
locale = Locale.ANY;
//Synthetic comment -- @@ -1584,7 +1584,7 @@
IAndroidTarget target = pair != null ? pair.getSecond() : configurationTarget;
if (target != null && configurationTarget != target) {
if (mClient != null && configurationTarget != null) {
                changeFlags |= CHANGED_RENDER_TARGET;
mClient.aboutToChange(changeFlags);
}

//Synthetic comment -- @@ -1605,7 +1605,7 @@
// Compute the new configuration; we want to do this both for locale changes
// and for render targets.
mConfiguration.syncFolderConfig();
        changeFlags |= CHANGED_FOLDER; // in case we added/remove a -v<NN> qualifier

if (renderTargetChanged) {
// force a theme update to reflect the new rendering target.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationClient.java
//Synthetic comment -- index a7c26d4..0c0fd0b 100644

//Synthetic comment -- @@ -19,14 +19,9 @@
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.resources.NightMode;
import com.android.resources.ResourceType;
import com.android.resources.UiMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;

import java.util.Map;

//Synthetic comment -- @@ -34,31 +29,13 @@
* Interface implemented by clients who embed a {@link ConfigurationChooser}.
*/
public interface ConfigurationClient {
    /** The {@link FolderConfiguration} in the configuration has changed */
    public static final int CHANGED_FOLDER        = 1 << 0;
    /** The {@link Device} in the configuration has changed */
    public static final int CHANGED_DEVICE        = 1 << 1;
    /** The {@link State} in the configuration has changed */
    public static final int CHANGED_DEVICE_CONFIG = 1 << 2;
    /** The theme in the configuration has changed */
    public static final int CHANGED_THEME         = 1 << 3;
    /** The locale in the configuration has changed */
    public static final int CHANGED_LOCALE        = 1 << 4;
    /** The rendering {@link IAndroidTarget} in the configuration has changed */
    public static final int CHANGED_RENDER_TARGET = 1 << 5;
    /** The {@link NightMode} in the configuration has changed */
    public static final int CHANGED_NIGHT_MODE = 1 << 6;
    /** The {@link UiMode} in the configuration has changed */
    public static final int CHANGED_UI_MODE = 1 << 7;

    /** Everything has changed */
    public static final int CHANGED_ALL = 0xFFFF;

/**
* The configuration is about to be changed.
*
     * @param flags details about what changed; consult the {@code CHANGED_} flags
     *   such as {@link #CHANGED_DEVICE}, {@link #CHANGED_LOCALE}, etc.
*/
void aboutToChange(int flags);

//Synthetic comment -- @@ -70,8 +47,9 @@
* file to edit the new configuration -- and the current configuration
* should go back to editing the state prior to this change.
*
     * @param flags details about what changed; consult the {@code CHANGED_} flags
     *   such as {@link #CHANGED_DEVICE}, {@link #CHANGED_LOCALE}, etc.
* @return true if the change was accepted, false if it was rejected.
*/
boolean changed(int flags);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java
//Synthetic comment -- index 7c2559c..00038df 100644

//Synthetic comment -- @@ -38,13 +38,18 @@
* be "en", but otherwise inherit everything else.
*/
public class NestedConfiguration extends Configuration {
protected Configuration mParent;
    protected boolean mOverrideLocale;
    protected boolean mOverrideTarget;
    protected boolean mOverrideDevice;
    protected boolean mOverrideDeviceState;
    protected boolean mOverrideNightMode;
    protected boolean mOverrideUiMode;

/**
* Constructs a new {@linkplain NestedConfiguration}.
//Synthetic comment -- @@ -67,6 +72,16 @@
}

/**
* Creates a new {@linkplain NestedConfiguration} that has the same overriding
* attributes as the given other {@linkplain NestedConfiguration}, and gets
* its values from the given {@linkplain Configuration}.
//Synthetic comment -- @@ -83,34 +98,29 @@
@NonNull Configuration parent) {
NestedConfiguration configuration =
new NestedConfiguration(other.mConfigChooser, parent);
configuration.setDisplayName(values.getDisplayName());
configuration.setActivity(values.getActivity());

        configuration.mOverrideLocale = other.mOverrideLocale;
        if (configuration.mOverrideLocale) {
configuration.setLocale(values.getLocale(), true);
}
        configuration.mOverrideTarget = other.mOverrideTarget;
        if (configuration.mOverrideTarget) {
configuration.setTarget(values.getTarget(), true);
}
        configuration.mOverrideDevice = other.mOverrideDevice;
        configuration.mOverrideDeviceState = other.mOverrideDeviceState;
        if (configuration.mOverrideDevice) {
configuration.setDevice(values.getDevice(), true);
}
        if (configuration.mOverrideDeviceState) {
configuration.setDeviceState(values.getDeviceState(), true);
}

        configuration.mOverrideNightMode = other.mOverrideNightMode;
        if (configuration.mOverrideNightMode) {
configuration.setNightMode(values.getNightMode(), true);
}
        configuration.mOverrideUiMode = other.mOverrideUiMode;
        if (configuration.mOverrideUiMode) {
configuration.setUiMode(values.getUiMode(), true);
}

return configuration;
}
//Synthetic comment -- @@ -159,7 +169,7 @@
* @param override if true, override the inherited value
*/
public void setOverrideLocale(boolean override) {
        mOverrideLocale = override;
}

/**
//Synthetic comment -- @@ -167,14 +177,14 @@
*
* @return true if the locale is overridden
*/
    public boolean isOverridingLocale() {
        return mOverrideLocale;
}

@Override
@NonNull
public Locale getLocale() {
        if (mOverrideLocale) {
return super.getLocale();
} else {
return mParent.getLocale();
//Synthetic comment -- @@ -183,7 +193,7 @@

@Override
public void setLocale(@NonNull Locale locale, boolean skipSync) {
        if (mOverrideLocale) {
super.setLocale(locale, skipSync);
} else {
mParent.setLocale(locale, skipSync);
//Synthetic comment -- @@ -196,13 +206,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideTarget(boolean override) {
        mOverrideTarget = override;
}

@Override
@Nullable
public IAndroidTarget getTarget() {
        if (mOverrideTarget) {
return super.getTarget();
} else {
return mParent.getTarget();
//Synthetic comment -- @@ -211,7 +230,7 @@

@Override
public void setTarget(IAndroidTarget target, boolean skipSync) {
        if (mOverrideTarget) {
super.setTarget(target, skipSync);
} else {
mParent.setTarget(target, skipSync);
//Synthetic comment -- @@ -224,13 +243,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideDevice(boolean override) {
        mOverrideDevice = override;
}

@Override
@Nullable
public Device getDevice() {
        if (mOverrideDevice) {
return super.getDevice();
} else {
return mParent.getDevice();
//Synthetic comment -- @@ -239,7 +267,7 @@

@Override
public void setDevice(Device device, boolean skipSync) {
        if (mOverrideDevice) {
super.setDevice(device, skipSync);
} else {
mParent.setDevice(device, skipSync);
//Synthetic comment -- @@ -252,17 +280,26 @@
* @param override if true, override the inherited value
*/
public void setOverrideDeviceState(boolean override) {
        mOverrideDeviceState = override;
}

@Override
@Nullable
public State getDeviceState() {
        if (mOverrideDeviceState) {
return super.getDeviceState();
} else {
State state = mParent.getDeviceState();
            if (mOverrideDevice) {
// If the device differs, I need to look up a suitable equivalent state
// on our device
if (state != null) {
//Synthetic comment -- @@ -279,10 +316,10 @@

@Override
public void setDeviceState(State state, boolean skipSync) {
        if (mOverrideDeviceState) {
super.setDeviceState(state, skipSync);
} else {
            if (mOverrideDevice) {
Device device = super.getDevice();
if (device != null) {
State equivalentState = device.getState(state.getName());
//Synthetic comment -- @@ -301,13 +338,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideNightMode(boolean override) {
        mOverrideNightMode = override;
}

@Override
@NonNull
public NightMode getNightMode() {
        if (mOverrideNightMode) {
return super.getNightMode();
} else {
return mParent.getNightMode();
//Synthetic comment -- @@ -316,7 +362,7 @@

@Override
public void setNightMode(@NonNull NightMode night, boolean skipSync) {
        if (mOverrideNightMode) {
super.setNightMode(night, skipSync);
} else {
mParent.setNightMode(night, skipSync);
//Synthetic comment -- @@ -329,13 +375,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideUiMode(boolean override) {
        mOverrideUiMode = override;
}

@Override
@NonNull
public UiMode getUiMode() {
        if (mOverrideUiMode) {
return super.getUiMode();
} else {
return mParent.getUiMode();
//Synthetic comment -- @@ -344,7 +399,7 @@

@Override
public void setUiMode(@NonNull UiMode uiMode, boolean skipSync) {
        if (mOverrideUiMode) {
super.setUiMode(uiMode, skipSync);
} else {
mParent.setUiMode(uiMode, skipSync);
//Synthetic comment -- @@ -373,16 +428,70 @@
super.setActivity(activity);
}

@Override
public String toString() {
return Objects.toStringHelper(this.getClass())
.add("parent", mParent.getDisplayName())          //$NON-NLS-1$
.add("display", getDisplayName())                 //$NON-NLS-1$
                .add("overrideLocale", mOverrideLocale)           //$NON-NLS-1$
                .add("overrideTarget", mOverrideTarget)           //$NON-NLS-1$
                .add("overrideDevice", mOverrideDevice)           //$NON-NLS-1$
                .add("overrideDeviceState", mOverrideDeviceState) //$NON-NLS-1$
.add("persistent", toPersistentString())          //$NON-NLS-1$
.toString();
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 67a890f..71dbb43 100644

//Synthetic comment -- @@ -30,9 +30,12 @@
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser.NAME_CONFIG_STATE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;

import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_EAST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_WEST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_COLLAPSED;
//Synthetic comment -- @@ -642,7 +645,7 @@
// ---- Implements ConfigurationClient ----
@Override
public void aboutToChange(int flags) {
        if ((flags & CHANGED_RENDER_TARGET) != 0) {
IAndroidTarget oldTarget = mConfigChooser.getConfiguration().getTarget();
preRenderingTargetChangeCleanUp(oldTarget);
}
//Synthetic comment -- @@ -730,7 +733,7 @@
}
}

        if ((flags & CHANGED_RENDER_TARGET) != 0) {
Configuration configuration = mConfigChooser.getConfiguration();
IAndroidTarget target = configuration.getTarget();
Sdk current = Sdk.getCurrent();
//Synthetic comment -- @@ -740,7 +743,7 @@
}
}

        if ((flags & (CHANGED_DEVICE | CHANGED_DEVICE_CONFIG)) != 0) {
// When the device changes, zoom the view to fit, but only up to 100% (e.g. zoom
// out to fit the content, or zoom back in if we were zoomed out more from the
// previous view, but only up to 100% such that we never blow up pixels
//Synthetic comment -- @@ -907,7 +910,7 @@
IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
if (target != null) {
mConfigChooser.onSdkLoaded(target);
                    changed(CHANGED_FOLDER | CHANGED_RENDER_TARGET);
}
}
}
//Synthetic comment -- @@ -1168,7 +1171,7 @@
AndroidTargetData targetData = mConfigChooser.onXmlModelLoaded();
updateCapabilities(targetData);

        changed(CHANGED_FOLDER | CHANGED_RENDER_TARGET);
}

/** Updates the capabilities for the given target data (which may be null) */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index bf64848..898c3d0 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
//Synthetic comment -- @@ -1663,6 +1665,9 @@
/** @see #setPreview(RenderPreview) */
private RenderPreview mPreview;

/**
* Sets the {@link RenderPreview} associated with the currently rendering
* configuration.
//Synthetic comment -- @@ -1691,6 +1696,23 @@
return mPreview;
}

/** Ensures that the configuration previews are up to date for this canvas */
public void syncPreviewMode() {
if (mImageOverlay != null && mImageOverlay.getImage() != null &&








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 07f640b..ca27a1d 100644

//Synthetic comment -- @@ -18,11 +18,11 @@
import static com.android.SdkConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_DEVICE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_FOLDER;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_LOCALE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_RENDER_TARGET;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationClient.CHANGED_THEME;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils.SHADOW_SIZE;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils.SMALL_SHADOW_SIZE;

//Synthetic comment -- @@ -526,7 +526,6 @@

GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
        FolderConfiguration config = mConfiguration.getFullConfig();
RenderService renderService = RenderService.create(editor, mConfiguration, resolver);

if (mIncludedWithin != null) {
//Synthetic comment -- @@ -953,10 +952,23 @@
* @param x the left edge of the preview rectangle
* @param y the top edge of the preview rectangle
*/
    int paintTitle(GC gc, int x, int y, boolean showFile) {
int titleHeight = 0;

        String displayName = getDisplayName();
if (displayName != null && displayName.length() > 0) {
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

//Synthetic comment -- @@ -1030,8 +1042,8 @@
return;
}

        if ((flags & (CHANGED_FOLDER | CHANGED_THEME | CHANGED_DEVICE
                | CHANGED_RENDER_TARGET | CHANGED_LOCALE)) != 0) {
mResourceResolver = null;
// Handle inheritance
mConfiguration.syncFolderConfig();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index f90e9ac..efe40a0 100644

//Synthetic comment -- @@ -573,7 +573,21 @@

int x = destX + destWidth / 2 - preview.getWidth() / 2;
int y = destY + destHeight;
                preview.paintTitle(gc, x, y, false /*showFile*/);
}

// Zoom overlay
//Synthetic comment -- @@ -1151,6 +1165,8 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ConfigurationChooser chooser = editor.getConfigurationChooser();

Configuration originalConfiguration = chooser.getConfiguration();

// The new configuration is the configuration which will become the configuration








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java
//Synthetic comment -- index 938f52b..2b8cfbf 100644

//Synthetic comment -- @@ -268,6 +268,9 @@
Map<String, ComplexProperty> categoryToProperty = new HashMap<String, ComplexProperty>();
Multimap<String, Property> categoryToProperties = ArrayListMultimap.create();


ViewElementDescriptor parent = (ViewElementDescriptor) properties.get(0).getDescriptor()
.getParent();







