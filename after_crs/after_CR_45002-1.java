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
/** Variation version; see {@link #setVariation(int)} */
private int mVariation;

//Synthetic comment -- @@ -104,17 +97,20 @@
new ComplementingConfiguration(other.mConfigChooser, parent);
configuration.setDisplayName(other.getDisplayName());
configuration.setActivity(other.getActivity());
        configuration.mOverride = other.mOverride;
        configuration.mVariation = other.mVariation;
        configuration.mVariationCount = other.mVariationCount;
        configuration.syncFolderConfig();

return configuration;
}

    @Override
    public void syncFolderConfig() {
        super.syncFolderConfig();
        updateDisplayName();
    }

/**
* Sets the variation version for this
* {@linkplain ComplementingConfiguration}. There might be multiple
//Synthetic comment -- @@ -143,47 +139,18 @@
mVariationCount = count;
}

    /**
     * Updates the display name in this configuration based on the values and override settings
     */
    public void updateDisplayName() {
        setDisplayName(computeDisplayName());
}

@Override
@NonNull
public Locale getLocale() {
Locale locale = mParent.getLocale();
        if (isOverridingLocale() && locale != null) {
List<Locale> locales = mConfigChooser.getLocaleList();
for (Locale l : locales) {
// TODO: Try to be smarter about which one we pick; for example, try
//Synthetic comment -- @@ -195,10 +162,6 @@
break;
}
}
}

return locale;
//Synthetic comment -- @@ -208,7 +171,7 @@
@Nullable
public IAndroidTarget getTarget() {
IAndroidTarget target = mParent.getTarget();
        if (isOverridingTarget() && target != null) {
List<IAndroidTarget> targets = mConfigChooser.getTargetList();
if (!targets.isEmpty()) {
// Pick a different target: if you're showing the most recent render target,
//Synthetic comment -- @@ -228,24 +191,28 @@
target = mostRecent;
}
}
}

return target;
}

    private Device mPrevParentDevice;
    private Device mPrevDevice;

@Override
@Nullable
public Device getDevice() {
Device device = mParent.getDevice();
        if (isOverridingDevice() && device != null) {
            if (device == mPrevParentDevice) {
                return mPrevDevice;
            }

            mPrevParentDevice = device;

// Pick a different device
List<Device> devices = mConfigChooser.getDeviceList();

// Divide up the available devices into {@link #mVariationCount} + 1 buckets
// (the + 1 is for the bucket now taken up by the inherited value).
// Then assign buckets to each {@link #mVariation} version, and pick one
//Synthetic comment -- @@ -300,9 +267,7 @@
}
}

            mPrevDevice = device;
}

return device;
//Synthetic comment -- @@ -343,18 +308,12 @@
@Nullable
public State getDeviceState() {
State state = mParent.getDeviceState();
        if (isOverridingDeviceState() && state != null) {
State alternate = getNextDeviceState(state);

return alternate;
} else {
            if (isOverridingDevice() && state != null) {
// If the device differs, I need to look up a suitable equivalent state
// on our device
Device device = getDevice();
//Synthetic comment -- @@ -371,11 +330,8 @@
@NonNull
public NightMode getNightMode() {
NightMode nightMode = mParent.getNightMode();
        if (isOverridingNightMode() && nightMode != null) {
nightMode = nightMode == NightMode.NIGHT ? NightMode.NOTNIGHT : NightMode.NIGHT;
return nightMode;
} else {
return nightMode;
//Synthetic comment -- @@ -386,15 +342,12 @@
@NonNull
public UiMode getUiMode() {
UiMode uiMode = mParent.getUiMode();
        if (isOverridingUiMode() && uiMode != null) {
// TODO: Use manifest's supports screen to decide which are most relevant
// (as well as which available configuration qualifiers are present in the
// layout)
UiMode[] values = UiMode.values();
uiMode = values[(uiMode.ordinal() + 1) % values.length];
return uiMode;
} else {
return uiMode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 00caccd..979e102 100644

//Synthetic comment -- @@ -63,6 +63,26 @@
* etc for use when rendering a layout.
*/
public class Configuration {
    /** The {@link FolderConfiguration} in change flags or override flags */
    public static final int CFG_FOLDER       = 1 << 0;
    /** The {@link Device} in change flags or override flags */
    public static final int CFG_DEVICE       = 1 << 1;
    /** The {@link State} in change flags or override flags */
    public static final int CFG_DEVICE_STATE = 1 << 2;
    /** The theme in change flags or override flags */
    public static final int CFG_THEME        = 1 << 3;
    /** The locale in change flags or override flags */
    public static final int CFG_LOCALE       = 1 << 4;
    /** The rendering {@link IAndroidTarget} in change flags or override flags */
    public static final int CFG_TARGET       = 1 << 5;
    /** The {@link NightMode} in change flags or override flags */
    public static final int CFG_NIGHT_MODE   = 1 << 6;
    /** The {@link UiMode} in change flags or override flags */
    public static final int CFG_UI_MODE      = 1 << 7;

    /** References all attributes */
    public static final int CFG_ALL = 0xFFFF;

/**
* Setting name for project-wide setting controlling rendering target and locale which
* is shared for all files








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index 592fa32..a95fc31 100644

//Synthetic comment -- @@ -24,13 +24,13 @@
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.ide.eclipse.adt.AdtUtils.isUiThread;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_ALL;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_DEVICE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_DEVICE_STATE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_FOLDER;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_LOCALE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_TARGET;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_THEME;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -603,14 +603,14 @@
*/
public void setConfiguration(@NonNull Configuration configuration) {
if (mClient != null) {
            mClient.aboutToChange(CFG_ALL);
}

Configuration oldConfiguration = mConfiguration;
mConfiguration = configuration;

if (mClient != null) {
            mClient.changed(CFG_ALL);
}

selectTheme(configuration.getTheme());
//Synthetic comment -- @@ -622,7 +622,7 @@

// This may be a second refresh after triggered by theme above
if (mClient != null) {
            boolean accepted = mClient.changed(CFG_ALL);
if (!accepted) {
configuration = oldConfiguration;
selectTheme(configuration.getTheme());
//Synthetic comment -- @@ -811,7 +811,7 @@
matcher.adaptConfigSelection(true /*needBestMatch*/);
mConfiguration.syncFolderConfig();
if (mClient != null) {
                mClient.changed(CFG_ALL);
}
}
}
//Synthetic comment -- @@ -1356,7 +1356,7 @@
mConfiguration.syncFolderConfig();

// Notify
        boolean accepted = mClient.changed(CFG_DEVICE | CFG_DEVICE_STATE);
if (!accepted) {
mConfiguration.setDevice(prevDevice, true);
mConfiguration.setDeviceState(prevState, true);
//Synthetic comment -- @@ -1384,7 +1384,7 @@
mConfiguration.setDeviceState(state, false);

if (mClient != null) {
            boolean accepted = mClient.changed(CFG_DEVICE | CFG_DEVICE_STATE);
if (!accepted) {
mConfiguration.setDeviceState(prev, false);
selectDeviceState(prev);
//Synthetic comment -- @@ -1413,7 +1413,7 @@
mConfiguration.setLocale(locale, false);

if (mClient != null) {
            boolean accepted = mClient.changed(CFG_LOCALE);
if (!accepted) {
mConfiguration.setLocale(prev, false);
selectLocale(prev);
//Synthetic comment -- @@ -1434,7 +1434,7 @@
mConfiguration.setTheme((String) mThemeCombo.getData());

if (mClient != null) {
            boolean accepted = mClient.changed(CFG_THEME);
if (!accepted) {
mConfiguration.setTheme(prev);
selectTheme(prev);
//Synthetic comment -- @@ -1450,7 +1450,7 @@
return;
}

        if (mClient.changed(CFG_FOLDER)) {
saveConstraints();
}
}
//Synthetic comment -- @@ -1503,7 +1503,7 @@
// tell the listener a new rendering target is being set. Need to do this before updating
// mRenderingTarget.
if (prevTarget != null) {
            changeFlags |= CFG_TARGET;
mClient.aboutToChange(changeFlags);
}

//Synthetic comment -- @@ -1518,12 +1518,12 @@
// updateThemes may change the theme (based on theme availability in the new rendering
// target) so mark theme change if necessary
if (!Objects.equal(oldTheme, mConfiguration.getTheme())) {
            changeFlags |= CFG_THEME;
}

if (target != null) {
            changeFlags |= CFG_TARGET;
            changeFlags |= CFG_FOLDER; // In case we added a -vNN qualifier
}

// Store project-wide render-target setting
//Synthetic comment -- @@ -1571,7 +1571,7 @@
if (locale != null) {
boolean localeChanged = setLocale(locale);
if (localeChanged) {
                    changeFlags |= CFG_LOCALE;
}
} else {
locale = Locale.ANY;
//Synthetic comment -- @@ -1584,7 +1584,7 @@
IAndroidTarget target = pair != null ? pair.getSecond() : configurationTarget;
if (target != null && configurationTarget != target) {
if (mClient != null && configurationTarget != null) {
                changeFlags |= CFG_TARGET;
mClient.aboutToChange(changeFlags);
}

//Synthetic comment -- @@ -1605,7 +1605,7 @@
// Compute the new configuration; we want to do this both for locale changes
// and for render targets.
mConfiguration.syncFolderConfig();
        changeFlags |= CFG_FOLDER; // in case we added/remove a -v<NN> qualifier

if (renderTargetChanged) {
// force a theme update to reflect the new rendering target.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationClient.java
//Synthetic comment -- index a7c26d4..0c0fd0b 100644

//Synthetic comment -- @@ -19,14 +19,9 @@
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;

import java.util.Map;

//Synthetic comment -- @@ -34,31 +29,13 @@
* Interface implemented by clients who embed a {@link ConfigurationChooser}.
*/
public interface ConfigurationClient {
/**
* The configuration is about to be changed.
*
     * @param flags details about what changed; consult the {@code CFG_} flags
     *            in {@link Configuration} such as
     *            {@link Configuration#CFG_DEVICE},
     *            {@link Configuration#CFG_LOCALE}, etc.
*/
void aboutToChange(int flags);

//Synthetic comment -- @@ -70,8 +47,9 @@
* file to edit the new configuration -- and the current configuration
* should go back to editing the state prior to this change.
*
     * @param flags details about what changed; consult the {@code CFG_} flags
     *            such as {@link Configuration#CFG_DEVICE},
     *            {@link Configuration#CFG_LOCALE}, etc.
* @return true if the change was accepted, false if it was rejected.
*/
boolean changed(int flags);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java
//Synthetic comment -- index 7c2559c..00038df 100644

//Synthetic comment -- @@ -38,13 +38,18 @@
* be "en", but otherwise inherit everything else.
*/
public class NestedConfiguration extends Configuration {
    /**
     * The configuration we are inheriting values from, other than those
     * indicated by {@link #mOverride}
     */
protected Configuration mParent;

    /**
     * If non zero, keep the display name up to date with the label for the
     * given overridden attribute, according to the flag constants in
     * {@link ConfigurationClient}
     */
    protected int mOverride;

/**
* Constructs a new {@linkplain NestedConfiguration}.
//Synthetic comment -- @@ -67,6 +72,16 @@
}

/**
     * Returns the override flags for this configuration. Corresponds to
     * the {@code CFG_} flags in {@link ConfigurationClient}.
     *
     * @return the bitmask
     */
    public int getOverrideFlags() {
        return mOverride;
    }

    /**
* Creates a new {@linkplain NestedConfiguration} that has the same overriding
* attributes as the given other {@linkplain NestedConfiguration}, and gets
* its values from the given {@linkplain Configuration}.
//Synthetic comment -- @@ -83,34 +98,29 @@
@NonNull Configuration parent) {
NestedConfiguration configuration =
new NestedConfiguration(other.mConfigChooser, parent);
        configuration.mOverride = other.mOverride;
configuration.setDisplayName(values.getDisplayName());
configuration.setActivity(values.getActivity());

        if (configuration.isOverridingLocale()) {
configuration.setLocale(values.getLocale(), true);
}
        if (configuration.isOverridingTarget()) {
configuration.setTarget(values.getTarget(), true);
}
        if (configuration.isOverridingDevice()) {
configuration.setDevice(values.getDevice(), true);
}
        if (configuration.isOverridingDeviceState()) {
configuration.setDeviceState(values.getDeviceState(), true);
}
        if (configuration.isOverridingNightMode()) {
configuration.setNightMode(values.getNightMode(), true);
}
        if (configuration.isOverridingUiMode()) {
configuration.setUiMode(values.getUiMode(), true);
}
        configuration.syncFolderConfig();

return configuration;
}
//Synthetic comment -- @@ -159,7 +169,7 @@
* @param override if true, override the inherited value
*/
public void setOverrideLocale(boolean override) {
        mOverride |= CFG_LOCALE;
}

/**
//Synthetic comment -- @@ -167,14 +177,14 @@
*
* @return true if the locale is overridden
*/
    public final boolean isOverridingLocale() {
        return (mOverride & CFG_LOCALE) != 0;
}

@Override
@NonNull
public Locale getLocale() {
        if (isOverridingLocale()) {
return super.getLocale();
} else {
return mParent.getLocale();
//Synthetic comment -- @@ -183,7 +193,7 @@

@Override
public void setLocale(@NonNull Locale locale, boolean skipSync) {
        if (isOverridingLocale()) {
super.setLocale(locale, skipSync);
} else {
mParent.setLocale(locale, skipSync);
//Synthetic comment -- @@ -196,13 +206,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideTarget(boolean override) {
        mOverride |= CFG_TARGET;
    }

    /**
     * Returns true if the target is overridden
     *
     * @return true if the target is overridden
     */
    public final boolean isOverridingTarget() {
        return (mOverride & CFG_TARGET) != 0;
}

@Override
@Nullable
public IAndroidTarget getTarget() {
        if (isOverridingTarget()) {
return super.getTarget();
} else {
return mParent.getTarget();
//Synthetic comment -- @@ -211,7 +230,7 @@

@Override
public void setTarget(IAndroidTarget target, boolean skipSync) {
        if (isOverridingTarget()) {
super.setTarget(target, skipSync);
} else {
mParent.setTarget(target, skipSync);
//Synthetic comment -- @@ -224,13 +243,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideDevice(boolean override) {
        mOverride |= CFG_DEVICE;
    }

    /**
     * Returns true if the device is overridden
     *
     * @return true if the device is overridden
     */
    public final boolean isOverridingDevice() {
        return (mOverride & CFG_DEVICE) != 0;
}

@Override
@Nullable
public Device getDevice() {
        if (isOverridingDevice()) {
return super.getDevice();
} else {
return mParent.getDevice();
//Synthetic comment -- @@ -239,7 +267,7 @@

@Override
public void setDevice(Device device, boolean skipSync) {
        if (isOverridingDevice()) {
super.setDevice(device, skipSync);
} else {
mParent.setDevice(device, skipSync);
//Synthetic comment -- @@ -252,17 +280,26 @@
* @param override if true, override the inherited value
*/
public void setOverrideDeviceState(boolean override) {
        mOverride |= CFG_DEVICE_STATE;
    }

    /**
     * Returns true if the device state is overridden
     *
     * @return true if the device state is overridden
     */
    public final boolean isOverridingDeviceState() {
        return (mOverride & CFG_DEVICE_STATE) != 0;
}

@Override
@Nullable
public State getDeviceState() {
        if (isOverridingDeviceState()) {
return super.getDeviceState();
} else {
State state = mParent.getDeviceState();
            if (isOverridingDevice()) {
// If the device differs, I need to look up a suitable equivalent state
// on our device
if (state != null) {
//Synthetic comment -- @@ -279,10 +316,10 @@

@Override
public void setDeviceState(State state, boolean skipSync) {
        if (isOverridingDeviceState()) {
super.setDeviceState(state, skipSync);
} else {
            if (isOverridingDevice()) {
Device device = super.getDevice();
if (device != null) {
State equivalentState = device.getState(state.getName());
//Synthetic comment -- @@ -301,13 +338,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideNightMode(boolean override) {
        mOverride |= CFG_NIGHT_MODE;
    }

    /**
     * Returns true if the night mode is overridden
     *
     * @return true if the night mode is overridden
     */
    public final boolean isOverridingNightMode() {
        return (mOverride & CFG_NIGHT_MODE) != 0;
}

@Override
@NonNull
public NightMode getNightMode() {
        if (isOverridingNightMode()) {
return super.getNightMode();
} else {
return mParent.getNightMode();
//Synthetic comment -- @@ -316,7 +362,7 @@

@Override
public void setNightMode(@NonNull NightMode night, boolean skipSync) {
        if (isOverridingNightMode()) {
super.setNightMode(night, skipSync);
} else {
mParent.setNightMode(night, skipSync);
//Synthetic comment -- @@ -329,13 +375,22 @@
* @param override if true, override the inherited value
*/
public void setOverrideUiMode(boolean override) {
        mOverride |= CFG_UI_MODE;
    }

    /**
     * Returns true if the UI mode is overridden
     *
     * @return true if the UI mode is overridden
     */
    public final boolean isOverridingUiMode() {
        return (mOverride & CFG_UI_MODE) != 0;
}

@Override
@NonNull
public UiMode getUiMode() {
        if (isOverridingUiMode()) {
return super.getUiMode();
} else {
return mParent.getUiMode();
//Synthetic comment -- @@ -344,7 +399,7 @@

@Override
public void setUiMode(@NonNull UiMode uiMode, boolean skipSync) {
        if (isOverridingUiMode()) {
super.setUiMode(uiMode, skipSync);
} else {
mParent.setUiMode(uiMode, skipSync);
//Synthetic comment -- @@ -373,16 +428,70 @@
super.setActivity(activity);
}

    /**
     * Returns a computed display name (ignoring the value stored by
     * {@link #setDisplayName(String)}) by looking at the override flags
     * and picking a suitable name.
     *
     * @return a suitable display name
     */
    @Nullable
    public String computeDisplayName() {
        return computeDisplayName(mOverride, this);
    }

    /**
     * Computes a display name for the given configuration, using the given
     * override flags (which correspond to the {@code CFG_} constants in
     * {@link ConfigurationClient}
     *
     * @param flags the override bitmask
     * @param configuration the configuration to fetch values from
     * @return a suitable display name
     */
    @Nullable
    public static String computeDisplayName(int flags, @NonNull Configuration configuration) {
        if ((flags & CFG_LOCALE) != 0) {
            return ConfigurationChooser.getLocaleLabel(configuration.mConfigChooser,
                    configuration.getLocale(), false);
        }

        if ((flags & CFG_TARGET) != 0) {
            return ConfigurationChooser.getRenderingTargetLabel(configuration.getTarget(), false);
        }

        if ((flags & CFG_DEVICE) != 0) {
            return ConfigurationChooser.getDeviceLabel(configuration.getDevice(), true);
        }

        if ((flags & CFG_DEVICE_STATE) != 0) {
            State deviceState = configuration.getDeviceState();
            if (deviceState != null) {
                return deviceState.getName();
            }
        }

        if ((flags & CFG_NIGHT_MODE) != 0) {
            return configuration.getNightMode().getLongDisplayValue();
        }

        if ((flags & CFG_UI_MODE) != 0) {
            configuration.getUiMode().getLongDisplayValue();
        }

        return null;
    }

@Override
public String toString() {
return Objects.toStringHelper(this.getClass())
.add("parent", mParent.getDisplayName())          //$NON-NLS-1$
.add("display", getDisplayName())                 //$NON-NLS-1$
                .add("overrideLocale", isOverridingLocale())           //$NON-NLS-1$
                .add("overrideTarget", isOverridingTarget())           //$NON-NLS-1$
                .add("overrideDevice", isOverridingDevice())           //$NON-NLS-1$
                .add("overrideDeviceState", isOverridingDeviceState()) //$NON-NLS-1$
.add("persistent", toPersistentString())          //$NON-NLS-1$
.toString();
}
\ No newline at end of file
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 67a890f..71dbb43 100644

//Synthetic comment -- @@ -30,9 +30,12 @@
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_DEVICE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_DEVICE_STATE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_FOLDER;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_TARGET;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser.NAME_CONFIG_STATE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_EAST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_WEST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_COLLAPSED;
//Synthetic comment -- @@ -642,7 +645,7 @@
// ---- Implements ConfigurationClient ----
@Override
public void aboutToChange(int flags) {
        if ((flags & CFG_TARGET) != 0) {
IAndroidTarget oldTarget = mConfigChooser.getConfiguration().getTarget();
preRenderingTargetChangeCleanUp(oldTarget);
}
//Synthetic comment -- @@ -730,7 +733,7 @@
}
}

        if ((flags & CFG_TARGET) != 0) {
Configuration configuration = mConfigChooser.getConfiguration();
IAndroidTarget target = configuration.getTarget();
Sdk current = Sdk.getCurrent();
//Synthetic comment -- @@ -740,7 +743,7 @@
}
}

        if ((flags & (CFG_DEVICE | CFG_DEVICE_STATE)) != 0) {
// When the device changes, zoom the view to fit, but only up to 100% (e.g. zoom
// out to fit the content, or zoom back in if we were zoomed out more from the
// previous view, but only up to 100% such that we never blow up pixels
//Synthetic comment -- @@ -907,7 +910,7 @@
IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
if (target != null) {
mConfigChooser.onSdkLoaded(target);
                    changed(CFG_FOLDER | CFG_TARGET);
}
}
}
//Synthetic comment -- @@ -1168,7 +1171,7 @@
AndroidTargetData targetData = mConfigChooser.onXmlModelLoaded();
updateCapabilities(targetData);

        changed(CFG_FOLDER | CFG_TARGET);
}

/** Updates the capabilities for the given target data (which may be null) */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index bf64848..898c3d0 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ComplementingConfiguration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
//Synthetic comment -- @@ -1663,6 +1665,9 @@
/** @see #setPreview(RenderPreview) */
private RenderPreview mPreview;

    /** @see #setPreviewName(String) */
    private String mPreviewName;

/**
* Sets the {@link RenderPreview} associated with the currently rendering
* configuration.
//Synthetic comment -- @@ -1691,6 +1696,23 @@
return mPreview;
}

    @Nullable
    public String getPreviewName() {
        if (mPreview == null) {
            return null;
        }

        Configuration configuration = mPreview.getConfiguration();
        if (configuration instanceof ComplementingConfiguration) {

        }
        return mPreviewName;
    }

    public void setPreviewName(@Nullable String name) {
        mPreviewName = name;
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
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_DEVICE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_FOLDER;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_LOCALE;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_TARGET;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration.CFG_THEME;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils.SHADOW_SIZE;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils.SMALL_SHADOW_SIZE;

//Synthetic comment -- @@ -526,7 +526,6 @@

GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
RenderService renderService = RenderService.create(editor, mConfiguration, resolver);

if (mIncludedWithin != null) {
//Synthetic comment -- @@ -953,10 +952,23 @@
* @param x the left edge of the preview rectangle
* @param y the top edge of the preview rectangle
*/
    private int paintTitle(GC gc, int x, int y, boolean showFile) {
        String displayName = getDisplayName();
        return paintTitle(gc, x, y, showFile, displayName);
    }

    /**
     * Paints the preview title at the given position (and returns the required
     * height)
     *
     * @param gc the graphics context to paint into
     * @param x the left edge of the preview rectangle
     * @param y the top edge of the preview rectangle
     * @param displayName the title string to be used
     */
    int paintTitle(GC gc, int x, int y, boolean showFile, String displayName) {
int titleHeight = 0;

if (displayName != null && displayName.length() > 0) {
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

//Synthetic comment -- @@ -1030,8 +1042,8 @@
return;
}

        if ((flags & (CFG_FOLDER | CFG_THEME | CFG_DEVICE
                | CFG_TARGET | CFG_LOCALE)) != 0) {
mResourceResolver = null;
// Handle inheritance
mConfiguration.syncFolderConfig();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index f90e9ac..efe40a0 100644

//Synthetic comment -- @@ -573,7 +573,21 @@

int x = destX + destWidth / 2 - preview.getWidth() / 2;
int y = destY + destHeight;

                String displayName = null;
                Configuration configuration = preview.getConfiguration();
                if (configuration instanceof NestedConfiguration) {
                    // Use override flags from stashed preview, but configuration
                    // data from live (not complementing) configured configuration
                    int flags = ((NestedConfiguration) configuration).getOverrideFlags();
                    displayName = NestedConfiguration.computeDisplayName(flags,
                            getChooser().getConfiguration());
                } else {
                    displayName = mCanvas.getPreviewName();
                }
                if (displayName != null) {
                    preview.paintTitle(gc, x, y, false /*showFile*/, displayName);
                }
}

// Zoom overlay
//Synthetic comment -- @@ -1151,6 +1165,8 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ConfigurationChooser chooser = editor.getConfigurationChooser();

        mCanvas.setPreviewName(preview.getDisplayName());

Configuration originalConfiguration = chooser.getConfiguration();

// The new configuration is the configuration which will become the configuration








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java
//Synthetic comment -- index 938f52b..2b8cfbf 100644

//Synthetic comment -- @@ -268,6 +268,9 @@
Map<String, ComplexProperty> categoryToProperty = new HashMap<String, ComplexProperty>();
Multimap<String, Property> categoryToProperties = ArrayListMultimap.create();

        if (properties.isEmpty()) {
            return properties;
        }

ViewElementDescriptor parent = (ViewElementDescriptor) properties.get(0).getDescriptor()
.getParent();







