/*ADT/GLE: Platform selection when rendering layouts.*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index a47bb80..28b46b6 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenDimensionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -56,7 +57,6 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//Synthetic comment -- @@ -106,6 +106,7 @@
private final static int LOCALE_REGION = 1;

private Label mCurrentLayoutLabel;

private Combo mDeviceCombo;
private Combo mDeviceConfigCombo;
//Synthetic comment -- @@ -113,13 +114,14 @@
private Combo mDockCombo;
private Combo mNightCombo;
private Combo mThemeCombo;
    private Button mCreateButton;

private int mPlatformThemeCount = 0;
/** updates are disabled if > 0 */
private int mDisableUpdates = 0;

private List<LayoutDevice> mDeviceList;

private final ArrayList<ResourceQualifier[] > mLocaleList =
new ArrayList<ResourceQualifier[]>();
//Synthetic comment -- @@ -158,6 +160,7 @@

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
Map<String, Map<String, IResourceValue>> getConfiguredProjectResources();
Map<String, Map<String, IResourceValue>> getConfiguredFrameworkResources();
}
//Synthetic comment -- @@ -428,11 +431,11 @@

GridLayout gl;
GridData gd;
        int cols = 9;  // device+config+locale+dock+day/night+separator*2+theme+createBtn

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(2 + customButtons.length, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -457,6 +460,18 @@
}
}

// ---- 2nd line: device/config/locale/theme Combos, create button.

setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -487,10 +502,8 @@
mLocaleCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
mLocaleCombo.setLayoutData(new GridData(
GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        mLocaleCombo.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                onLocaleChange();
            }
public void widgetSelected(SelectionEvent e) {
onLocaleChange();
}
//Synthetic comment -- @@ -502,10 +515,8 @@
for (DockMode mode : DockMode.values()) {
mDockCombo.add(mode.getLongDisplayValue());
}
        mDockCombo.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                onDockChange();
            }
public void widgetSelected(SelectionEvent e) {
onDockChange();
}
//Synthetic comment -- @@ -517,10 +528,8 @@
for (NightMode mode : NightMode.values()) {
mNightCombo.add(mode.getLongDisplayValue());
}
        mNightCombo.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                onDayChange();
            }
public void widgetSelected(SelectionEvent e) {
onDayChange();
}
//Synthetic comment -- @@ -550,15 +559,13 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mCreateButton = new Button(this, SWT.PUSH | SWT.FLAT);
        mCreateButton.setText("Create...");
        mCreateButton.setEnabled(false);
        mCreateButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                if (mListener != null) {
                    mListener.onCreate();
                }
}
});
}
//Synthetic comment -- @@ -610,26 +617,30 @@
mDisableUpdates++; // we do not want to trigger onXXXChange when setting
// new values in the widgets.

        // only attempt to do anything if the SDK and targets are loaded.
        LoadStatus sdkStatus = AdtPlugin.getDefault().getSdkLoadStatus();
        if (sdkStatus == LoadStatus.LOADED) {
            LoadStatus targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mTarget, null);

            if (targetStatus == LoadStatus.LOADED) {

                // update the current config selection to make sure it's
                // compatible with the new file
                adaptConfigSelection(true /*needBestMatch*/);

                // compute the final current config
                computeCurrentConfig(true /*force*/);

                // update the string showing the config value
                updateConfigDisplay(mEditedConfig);
}
}

        mDisableUpdates--;
}

/**
//Synthetic comment -- @@ -666,16 +677,20 @@

mDisableUpdates++; // we do not want to trigger onXXXChange when setting
// new values in the widgets.

        // this is going to be followed by a call to onTargetLoaded.
        // So we can only care about the layout devices in this case.
        initDevices();

        mDisableUpdates--;
}

/**
     * Answers to the XML model being loaded, either the first time or when the Targget/SDK changes.
* <p>This initializes the UI, either with the first compatible configuration found,
* or attempts to restore a configuration if one is found to have been saved in the file
* persistent storage.
//Synthetic comment -- @@ -696,80 +711,86 @@
if (sdkStatus == LoadStatus.LOADED) {
mDisableUpdates++; // we do not want to trigger onXXXChange when setting

            // init the devices if needed (new SDK or first time going through here)
            if (mSdkChanged || mFirstXmlModelChange) {
                initDevices();
            }

            IProject iProject = mEditedFile.getProject();

            Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null) {
                mTarget = currentSdk.getTarget(iProject);
            }

            LoadStatus targetStatus = LoadStatus.FAILED;
            if (mTarget != null) {
                targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mTarget, null);
            }

            if (targetStatus == LoadStatus.LOADED) {
                if (mResources == null) {
                    mResources = ResourceManager.getInstance().getProjectResources(iProject);
                }
                if (mEditedConfig == null) {
                    ResourceFolder resFolder = mResources.getResourceFolder(
                            (IFolder) mEditedFile.getParent());
                    mEditedConfig = resFolder.getConfiguration();
}

                targetData = Sdk.getCurrent().getTargetData(mTarget);

                // get the file stored state
                boolean loadedConfigData = false;
                try {
                    QualifiedName qname = new QualifiedName(AdtPlugin.PLUGIN_ID, CONFIG_STATE);
                    String data = mEditedFile.getPersistentProperty(qname);
                    if (data != null) {
                        loadedConfigData = mState.setData(data);
}
                } catch (CoreException e) {
                    // pass
}

                // update the themes and locales.
                updateThemes();
                updateLocales();

                // If the current state was loaded from the persistent storage, we update the
                // UI with it and then try to adapt it (which will handle incompatible
                // configuration).
                // Otherwise, just look for the first compatible configuration.
                if (loadedConfigData) {
                    // first make sure we have the config to adapt
                    selectDevice(mState.device);
                    fillConfigCombo(mState.configName);

                    adaptConfigSelection(false /*needBestMatch*/);

                    mDockCombo.select(DockMode.getIndex(mState.dock));
                    mNightCombo.select(NightMode.getIndex(mState.night));
                } else {
                    findAndSetCompatibleConfig(false /*favorCurrentConfig*/);

                    mDockCombo.select(0);
                    mNightCombo.select(0);
                }

                // update the string showing the config value
                updateConfigDisplay(mEditedConfig);

                // compute the final current config
                computeCurrentConfig(true /*force*/);
}

            mDisableUpdates--;
            mFirstXmlModelChange  = false;
}

return targetData;
//Synthetic comment -- @@ -969,7 +990,9 @@

private void updateConfigDisplay(FolderConfiguration fileConfig) {
String current = fileConfig.toDisplayString();
        mCurrentLayoutLabel.setText(current != null ? current : "(Default)");
}

private void saveState(boolean force) {
//Synthetic comment -- @@ -1029,74 +1052,79 @@

mDisableUpdates++;

        // Reset the combo
        mLocaleCombo.removeAll();
        mLocaleList.clear();

        SortedSet<String> languages = null;
        boolean hasLocale = false;

        // get the languages from the project.
        ProjectResources project = mListener.getProjectResources();

        // in cases where the opened file is not linked to a project, this could be null.
        if (project != null) {
            // now get the languages from the project.
            languages = project.getLanguages();

            for (String language : languages) {
                hasLocale = true;

                LanguageQualifier langQual = new LanguageQualifier(language);

                // find the matching regions and add them
                SortedSet<String> regions = project.getRegions(language);
                for (String region : regions) {
                    mLocaleCombo.add(String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
                    RegionQualifier regionQual = new RegionQualifier(region);
                    mLocaleList.add(new ResourceQualifier[] { langQual, regionQual });
}

                // now the entry for the other regions the language alone
                if (regions.size() > 0) {
                    mLocaleCombo.add(String.format("%1$s / Other", language)); //$NON-NLS-1$
                } else {
                    mLocaleCombo.add(String.format("%1$s / Any", language)); //$NON-NLS-1$
                }
                // create a region qualifier that will never be matched by qualified resources.
                mLocaleList.add(new ResourceQualifier[] {
                        langQual,
                        new RegionQualifier(RegionQualifier.FAKE_REGION_VALUE)
                });
}
}

        // add a locale not present in the project resources. This will let the dev
        // tests his/her default values.
        if (hasLocale) {
            mLocaleCombo.add("Other");
        } else {
            mLocaleCombo.add("Any locale");
        }

        // create language/region qualifier that will never be matched by qualified resources.
        mLocaleList.add(new ResourceQualifier[] {
                new LanguageQualifier(LanguageQualifier.FAKE_LANG_VALUE),
                new RegionQualifier(RegionQualifier.FAKE_REGION_VALUE)
        });

        if (mState.locale != null) {
            // FIXME: this may fails if the layout was deleted (and was the last one to have that local.
            // (we have other problem in this case though)
            setLocaleCombo(mState.locale[LOCALE_LANG],
                    mState.locale[LOCALE_REGION]);
        } else {
            mLocaleCombo.select(0);
        }

        mThemeCombo.getParent().layout();

        mDisableUpdates--;
}

/**
//Synthetic comment -- @@ -1108,105 +1136,109 @@
return; // can't do anything w/o it.
}

        ProjectResources frameworkProject = mListener.getFrameworkResources();

mDisableUpdates++;

        // Reset the combo
        mThemeCombo.removeAll();
        mPlatformThemeCount = 0;

        ArrayList<String> themes = new ArrayList<String>();

        // get the themes, and languages from the Framework.
        if (frameworkProject != null) {
            // get the configured resources for the framework
            Map<String, Map<String, IResourceValue>> frameworResources =
                mListener.getConfiguredFrameworkResources();

            if (frameworResources != null) {
                // get the styles.
                Map<String, IResourceValue> styles = frameworResources.get(
                        ResourceType.STYLE.getName());


                // collect the themes out of all the styles.
                for (IResourceValue value : styles.values()) {
                    String name = value.getName();
                    if (name.startsWith("Theme.") || name.equals("Theme")) {
                        themes.add(value.getName());
                        mPlatformThemeCount++;
                    }
                }

                // sort them and add them to the combo
                Collections.sort(themes);

                for (String theme : themes) {
                    mThemeCombo.add(theme);
                }

                mPlatformThemeCount = themes.size();
                themes.clear();
            }
        }

        // now get the themes and languages from the project.
        ProjectResources project = mListener.getProjectResources();
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
}

                    // sort them and add them the to the combo.
                    if (mPlatformThemeCount > 0 && themes.size() > 0) {
                        mThemeCombo.add(THEME_SEPARATOR);
                    }

Collections.sort(themes);

for (String theme : themes) {
mThemeCombo.add(theme);
}
}
}
        }

        // try to reselect the previous theme.
        if (mState.theme != null) {
            final int count = mThemeCombo.getItemCount();
            for (int i = 0 ; i < count ; i++) {
                if (mState.theme.equals(mThemeCombo.getItem(i))) {
                    mThemeCombo.select(i);
                    break;
}
}
            mThemeCombo.setEnabled(true);
        } else if (mThemeCombo.getItemCount() > 0) {
            mThemeCombo.select(0);
            mThemeCombo.setEnabled(true);
        } else {
            mThemeCombo.setEnabled(false);
}

        mThemeCombo.getParent().layout();

        mDisableUpdates--;
}

// ---- getters for the config selection values ----
//Synthetic comment -- @@ -1324,6 +1356,55 @@
return mThemeCombo.getSelectionIndex() >= mPlatformThemeCount;
}

/**
* Loads the list of {@link LayoutDevice} and inits the UI with it.
*/
//Synthetic comment -- @@ -1458,28 +1539,32 @@
// Update the UI with no triggered event
mDisableUpdates++;

        LayoutDevice oldCurrent = mState.device;

        // but first, update the device combo
        initDevices();

        // attempts to reselect the current device.
        if (selectDevice(oldCurrent)) {
            // current device still exists.
            // reselect the config
            selectConfig(mState.configName);

            // reset the UI as if it was just a replacement file, since we can keep
            // the current device (and possibly config).
            adaptConfigSelection(false /*needBestMatch*/);

        } else {
            // find a new device/config to match the current file.
            findAndSetCompatibleConfig(false /*favorCurrentConfig*/);
}

        mDisableUpdates--;

// recompute the current config
computeCurrentConfig(false /*force*/);

//Synthetic comment -- @@ -1592,7 +1677,7 @@
* Call back for language combo selection
*/
private void onLocaleChange() {
        // because mLocaleList triggers onLanguageChange at each modification, the filling
// of the combo with data will trigger notifications, and we don't want that.
if (mDisableUpdates > 0) {
return;
//Synthetic comment -- @@ -1616,6 +1701,28 @@
}

/**
* Saves the current state and the current configuration
* @param force forces saving the states even if updates are disabled
*
//Synthetic comment -- @@ -1634,9 +1741,9 @@
mCurrentConfig.set(config);

// replace the locale qualifiers with the one coming from the locale combo
            int localeIndex = mLocaleCombo.getSelectionIndex();
            if (localeIndex != -1) {
                ResourceQualifier[] localeQualifiers = mLocaleList.get(localeIndex);

mCurrentConfig.setLanguageQualifier(
(LanguageQualifier)localeQualifiers[LOCALE_LANG]);
//Synthetic comment -- @@ -1644,7 +1751,7 @@
(RegionQualifier)localeQualifiers[LOCALE_REGION]);
}

            int index = mDockCombo.getSelectionIndex();
if (index == -1) {
index = 0; // no selection = 0
}
//Synthetic comment -- @@ -1657,6 +1764,20 @@
mCurrentConfig.setNightModeQualifier(
new NightModeQualifier(NightMode.getByIndex(index)));

// update the create button.
checkCreateEnable();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 74f12e7..86f5d39 100755

//Synthetic comment -- @@ -596,28 +596,42 @@
}

/**
         * Returns a {@link ProjectResources} for the framework resources.
* @return the framework resources or null if not found.
*/
public ProjectResources getFrameworkResources() {
            if (mEditedFile != null) {
                Sdk currentSdk = Sdk.getCurrent();
                if (currentSdk != null) {
                    IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());

                    if (target != null) {
                        AndroidTargetData data = currentSdk.getTargetData(target);

                        if (data != null) {
                            return data.getFrameworkResources();
                        }
                    }
}
}

return null;
}

public ProjectResources getProjectResources() {
if (mEditedFile != null) {
ResourceManager manager = ResourceManager.getInstance();
//Synthetic comment -- @@ -757,21 +771,18 @@
}
}

        public void onTargetLoaded(IAndroidTarget target) {
            IProject project = getProject();
            if (target != null && target.equals(Sdk.getCurrent().getTarget(project))) {
updateEditor();
}
}

public void onSdkLoaded() {
            Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null && mEditedFile != null) {
                IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
                if (target != null) {
                    mConfigComposite.onSdkLoaded(target);
                    mConfigListener.onConfigurationChange();
                }
}
}

//Synthetic comment -- @@ -932,13 +943,10 @@
}

public void onSdkChange() {
        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk != null) {
            IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
            if (target != null) {
                mConfigComposite.onSdkLoaded(target);
                mConfigListener.onConfigurationChange();
            }
}
}

//Synthetic comment -- @@ -1016,7 +1024,7 @@
return;
}

            LayoutLibrary layoutLib = getReadyLayoutLib();

if (layoutLib != null) {
// if drawing in real size, (re)set the scaling factor.
//Synthetic comment -- @@ -1068,61 +1076,66 @@
/**
* Returns a {@link LayoutLibrary} that is ready for rendering, or null if the bridge
* is not available or not ready yet (due to SDK loading still being in progress etc).
     * Any reasons preventing the bridge from being returned are displayed to the editor's
     * error area.
*
* @return LayoutBridge the layout bridge for rendering this editor's scene
*/
    private LayoutLibrary getReadyLayoutLib() {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
            IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
            if (target == null) {
displayError("The project target is not set.");
                return null;
}

            AndroidTargetData data = currentSdk.getTargetData(target);
            if (data == null) {
                // It can happen that the workspace refreshes while the SDK is loading its
                // data, which could trigger a redraw of the opened layout if some resources
                // changed while Eclipse is closed.
                // In this case data could be null, but this is not an error.
                // We can just silently return, as all the opened editors are automatically
                // refreshed once the SDK finishes loading.
                LoadStatus targetLoadStatus = currentSdk.checkAndLoadTargetData(target, null);
                switch (targetLoadStatus) {
                    case LOADING:
                        displayError("The project target (%1$s) is still loading.\n%2$s will refresh automatically once the process is finished.",
                                target.getName(), mEditedFile.getName());

                        break;
                    case FAILED: // known failure
                    case LOADED: // success but data isn't loaded?!?!
                        displayError("The project target (%s) was not properly loaded.",
                                target.getName());
                        break;
                }

                return null;
            }

            LayoutLibrary layoutLib = data.getLayoutLibrary();

            if (layoutLib.getBridge() != null) { // layoutLib can never be null.
                return layoutLib;
            } else {
                // SDK is loaded but not the layout library!

                // check whether the bridge managed to load, or not
                if (layoutLib.getStatus() == LoadStatus.LOADING) {
                    displayError("Eclipse is loading framework information and the layout library from the SDK folder.\n%1$s will refresh automatically once the process is finished.",
                                 mEditedFile.getName());
                } else {
                    displayError("Eclipse failed to load the framework information and the layout library!");
                }
            }
        } else {
displayError("Eclipse is loading the SDK.\n%1$s will refresh automatically once the process is finished.",
mEditedFile.getName());
}
//Synthetic comment -- @@ -1130,6 +1143,37 @@
return null;
}

private boolean ensureModelValid(UiDocumentNode model) {
// check there is actually a model (maybe the file is empty).
if (model.getUiChildren().size() == 0) {
//Synthetic comment -- @@ -1186,7 +1230,7 @@
if (!ensureModelValid(model)) {
return null;
}
        LayoutLibrary layoutLib = getReadyLayoutLib();

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
//Synthetic comment -- @@ -1355,7 +1399,7 @@
mConfiguredProjectRes = null;

// clear the cache in the bridge in case a bitmap/9-patch changed.
                LayoutLibrary layoutLib = getLayoutLibrary();
if (layoutLib != null) {
if (layoutLib.getBridge() != null) {
layoutLib.getBridge().clearCaches(mEditedFile.getProject());
//Synthetic comment -- @@ -1386,16 +1430,7 @@
}

public LayoutLibrary getLayoutLibrary() {
        // clear the cache in the bridge in case a bitmap/9-patch changed.
        IAndroidTarget target = Sdk.getCurrent().getTarget(mEditedFile.getProject());
        if (target != null) {
            AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
            if (data != null) {
                return data.getLayoutLibrary();
            }
        }

        return null;
}

// ---- Error handling ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java
//Synthetic comment -- index f3df195..199e804 100644

//Synthetic comment -- @@ -76,6 +76,14 @@
return ""; //$NON-NLS-1$
}

public int getVersion() {
return mVersion;
}
//Synthetic comment -- @@ -126,6 +134,38 @@
}

@Override
public int hashCode() {
return mVersion;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index 57be46f..4a6c93d 100644

//Synthetic comment -- @@ -123,9 +123,9 @@
}

/**
     * Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not in the map returned
     * by {@link #getDefaultPropertyValues()}.
*/
public Object getLayoutParamsObject() {
return mLayoutParamsObject;







