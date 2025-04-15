/*ADT/GLE: Platform selection when rendering layouts.

Change-Id:Iabbd49cdd52419b947b83fb84f9fb3a5d4576471*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index a47bb80..3284d28 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenDimensionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.VersionQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -56,7 +57,6 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//Synthetic comment -- @@ -106,6 +106,7 @@
private final static int LOCALE_REGION = 1;

private Label mCurrentLayoutLabel;
    private Button mCreateButton;

private Combo mDeviceCombo;
private Combo mDeviceConfigCombo;
//Synthetic comment -- @@ -113,13 +114,14 @@
private Combo mDockCombo;
private Combo mNightCombo;
private Combo mThemeCombo;
    private Combo mApiCombo;

private int mPlatformThemeCount = 0;
/** updates are disabled if > 0 */
private int mDisableUpdates = 0;

private List<LayoutDevice> mDeviceList;
    private final List<IAndroidTarget> mTargetList = new ArrayList<IAndroidTarget>();

private final ArrayList<ResourceQualifier[] > mLocaleList =
new ArrayList<ResourceQualifier[]>();
//Synthetic comment -- @@ -158,6 +160,7 @@

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
        ProjectResources getFrameworkResources(IAndroidTarget target);
Map<String, Map<String, IResourceValue>> getConfiguredProjectResources();
Map<String, Map<String, IResourceValue>> getConfiguredFrameworkResources();
}
//Synthetic comment -- @@ -428,11 +431,11 @@

GridLayout gl;
GridData gd;
        int cols = 9;  // device+config+locale+dock+day/night+separator*2+theme+apiLevel

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(2 + customButtons.length + 1, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -457,6 +460,18 @@
}
}

        mCreateButton = new Button(labelParent, SWT.PUSH | SWT.FLAT);
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

// ---- 2nd line: device/config/locale/theme Combos, create button.

setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -487,10 +502,8 @@
mLocaleCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
mLocaleCombo.setLayoutData(new GridData(
GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        mLocaleCombo.addSelectionListener(new SelectionAdapter() {
            @Override
public void widgetSelected(SelectionEvent e) {
onLocaleChange();
}
//Synthetic comment -- @@ -502,10 +515,8 @@
for (DockMode mode : DockMode.values()) {
mDockCombo.add(mode.getLongDisplayValue());
}
        mDockCombo.addSelectionListener(new SelectionAdapter() {
            @Override
public void widgetSelected(SelectionEvent e) {
onDockChange();
}
//Synthetic comment -- @@ -517,10 +528,8 @@
for (NightMode mode : NightMode.values()) {
mNightCombo.add(mode.getLongDisplayValue());
}
        mNightCombo.addSelectionListener(new SelectionAdapter() {
            @Override
public void widgetSelected(SelectionEvent e) {
onDayChange();
}
//Synthetic comment -- @@ -550,15 +559,13 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mApiCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mApiCombo.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        mApiCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                onApiLevelChange();
}
});
}
//Synthetic comment -- @@ -610,26 +617,28 @@
mDisableUpdates++; // we do not want to trigger onXXXChange when setting
// new values in the widgets.

        try {
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
        } finally {
            mDisableUpdates--;
}
}

/**
//Synthetic comment -- @@ -666,16 +675,18 @@

mDisableUpdates++; // we do not want to trigger onXXXChange when setting
// new values in the widgets.
        try {
            // this is going to be followed by a call to onTargetLoaded.
            // So we can only care about the layout devices in this case.
            initDevices();
            initTargets();
        } finally {
            mDisableUpdates--;
        }
}

/**
     * Answers to the XML model being loaded, either the first time or when the Target/SDK changes.
* <p>This initializes the UI, either with the first compatible configuration found,
* or attempts to restore a configuration if one is found to have been saved in the file
* persistent storage.
//Synthetic comment -- @@ -696,80 +707,84 @@
if (sdkStatus == LoadStatus.LOADED) {
mDisableUpdates++; // we do not want to trigger onXXXChange when setting

            try {
                // init the devices if needed (new SDK or first time going through here)
                if (mSdkChanged || mFirstXmlModelChange) {
                    initDevices();
                    initTargets();
}

                IProject iProject = mEditedFile.getProject();

                Sdk currentSdk = Sdk.getCurrent();
                if (currentSdk != null) {
                    mTarget = currentSdk.getTarget(iProject);
                }

                LoadStatus targetStatus = LoadStatus.FAILED;
                if (mTarget != null) {
                    targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mTarget, null);
                    initTargets();
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
            } finally {
                mDisableUpdates--;
                mFirstXmlModelChange = false;
}
}

return targetData;
//Synthetic comment -- @@ -969,7 +984,9 @@

private void updateConfigDisplay(FolderConfiguration fileConfig) {
String current = fileConfig.toDisplayString();
        String layoutLabel = current != null ? current : "(Default)";
        mCurrentLayoutLabel.setText(layoutLabel);
        mCurrentLayoutLabel.setToolTipText(layoutLabel);
}

private void saveState(boolean force) {
//Synthetic comment -- @@ -1029,74 +1046,77 @@

mDisableUpdates++;

        try {
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
                        mLocaleCombo.add(
                                String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
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
                // FIXME: this may fails if the layout was deleted (and was the last one to have
                // that local. (we have other problem in this case though)
                setLocaleCombo(mState.locale[LOCALE_LANG],
                        mState.locale[LOCALE_REGION]);
            } else {
                mLocaleCombo.select(0);
            }

            mThemeCombo.getParent().layout();
        } finally {
            mDisableUpdates--;
}
}

/**
//Synthetic comment -- @@ -1108,105 +1128,107 @@
return; // can't do anything w/o it.
}

        ProjectResources frameworkProject = mListener.getFrameworkResources(getRenderingTarget());

mDisableUpdates++;

        try {
            // Reset the combo
            mThemeCombo.removeAll();
            mPlatformThemeCount = 0;

            ArrayList<String> themes = new ArrayList<String>();

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
        } finally {
            mDisableUpdates--;
}
}

// ---- getters for the config selection values ----
//Synthetic comment -- @@ -1324,6 +1346,54 @@
return mThemeCombo.getSelectionIndex() >= mPlatformThemeCount;
}

    public IAndroidTarget getRenderingTarget() {
        int index = mApiCombo.getSelectionIndex();
        if (index >= 0) {
            return mTargetList.get(index);
        }

        return null;
    }

    /**
     * Loads the list of {@link IAndroidTarget} and inits the UI with it.
     */
    private void initTargets() {
        // do we have a selection already?
        IAndroidTarget renderingTarget = getRenderingTarget();

        mApiCombo.removeAll();
        mTargetList.clear();

        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk != null) {
            IAndroidTarget[] targets = currentSdk.getTargets();
            int match = -1;
            for (int i = 0 ; i < targets.length; i++) {
                // FIXME: support add-on rendering and check based on project minSdkVersion
                if (targets[i].isPlatform()) {
                    mApiCombo.add(targets[i].getFullName());
                    mTargetList.add(targets[i]);

                    if (renderingTarget != null) {
                        if (renderingTarget == targets[i]) {
                            match = mTargetList.indexOf(targets[i]);
                        }
                    } else if (mTarget == targets[i]) {
                        match = mTargetList.indexOf(targets[i]);
                    }
                }
            }

            mApiCombo.setEnabled(mTargetList.size() > 1);
            if (match == -1) {
                mApiCombo.deselectAll();
            } else {
                mApiCombo.select(match);
            }
        }
    }

/**
* Loads the list of {@link LayoutDevice} and inits the UI with it.
*/
//Synthetic comment -- @@ -1458,28 +1528,30 @@
// Update the UI with no triggered event
mDisableUpdates++;

        try {
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
        } finally {
            mDisableUpdates--;
}

// recompute the current config
computeCurrentConfig(false /*force*/);

//Synthetic comment -- @@ -1592,7 +1664,7 @@
* Call back for language combo selection
*/
private void onLocaleChange() {
        // because mLocaleList triggers onLocaleChange at each modification, the filling
// of the combo with data will trigger notifications, and we don't want that.
if (mDisableUpdates > 0) {
return;
//Synthetic comment -- @@ -1616,6 +1688,28 @@
}

/**
     * Call back for api level combo selection
     */
    private void onApiLevelChange() {
        // because mApiCombo triggers onApiLevelChange at each modification, the filling
        // of the combo with data will trigger notifications, and we don't want that.
        if (mDisableUpdates > 0) {
            return;
        }

        boolean computeOk = computeCurrentConfig(false /*force*/);

        // force a theme update to reflect the new rendering target.
        // This must be done after computeCurrentConfig since it'll depend on the currentConfig
        // to figure out the theme list.
        updateThemes();

        if (computeOk &&  mListener != null) {
            mListener.onConfigurationChange();
        }
    }

    /**
* Saves the current state and the current configuration
* @param force forces saving the states even if updates are disabled
*
//Synthetic comment -- @@ -1634,9 +1728,9 @@
mCurrentConfig.set(config);

// replace the locale qualifiers with the one coming from the locale combo
            int index = mLocaleCombo.getSelectionIndex();
            if (index != -1) {
                ResourceQualifier[] localeQualifiers = mLocaleList.get(index);

mCurrentConfig.setLanguageQualifier(
(LanguageQualifier)localeQualifiers[LOCALE_LANG]);
//Synthetic comment -- @@ -1644,7 +1738,7 @@
(RegionQualifier)localeQualifiers[LOCALE_REGION]);
}

            index = mDockCombo.getSelectionIndex();
if (index == -1) {
index = 0; // no selection = 0
}
//Synthetic comment -- @@ -1657,6 +1751,20 @@
mCurrentConfig.setNightModeQualifier(
new NightModeQualifier(NightMode.getByIndex(index)));

            // replace the API level by the selection of the combo
            index = mApiCombo.getSelectionIndex();
            if (index == -1) {
                index = mTargetList.indexOf(mTarget);
            }
            if (index != -1) {
                IAndroidTarget target = mTargetList.get(index);

                if (target != null) {
                    mCurrentConfig.setVersionQualifier(
                            new VersionQualifier(target.getVersion().getApiLevel()));
                }
            }

// update the create button.
checkCreateEnable();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 74f12e7..1c5868c 100755

//Synthetic comment -- @@ -596,28 +596,33 @@
}

/**
         * Returns a {@link ProjectResources} for the framework resources based on the current
         * configuration selection.
* @return the framework resources or null if not found.
*/
public ProjectResources getFrameworkResources() {
            return getFrameworkResources(getRenderingTarget());
        }

        /**
         * Returns a {@link ProjectResources} for the framework resources of a given
         * target.
         * @param target the target for which to return the framework resources.
         * @return the framework resources or null if not found.
         */
        public ProjectResources getFrameworkResources(IAndroidTarget target) {
            if (target != null) {
                AndroidTargetData data = Sdk.getCurrent().getTargetData(target);

                if (data != null) {
                    return data.getFrameworkResources();
}
}

return null;
}


public ProjectResources getProjectResources() {
if (mEditedFile != null) {
ResourceManager manager = ResourceManager.getInstance();
//Synthetic comment -- @@ -757,21 +762,18 @@
}
}

        public void onTargetLoaded(IAndroidTarget loadedTarget) {
            IAndroidTarget target = getRenderingTarget();
            if (target != null && target.equals(loadedTarget)) {
updateEditor();
}
}

public void onSdkLoaded() {
            IAndroidTarget target = getRenderingTarget();
            if (target != null) {
                mConfigComposite.onSdkLoaded(target);
                mConfigListener.onConfigurationChange();
}
}

//Synthetic comment -- @@ -932,13 +934,10 @@
}

public void onSdkChange() {
        IAndroidTarget target = getRenderingTarget();
        if (target != null) {
            mConfigComposite.onSdkLoaded(target);
            mConfigListener.onConfigurationChange();
}
}

//Synthetic comment -- @@ -1016,7 +1015,7 @@
return;
}

            LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);

if (layoutLib != null) {
// if drawing in real size, (re)set the scaling factor.
//Synthetic comment -- @@ -1068,61 +1067,66 @@
/**
* Returns a {@link LayoutLibrary} that is ready for rendering, or null if the bridge
* is not available or not ready yet (due to SDK loading still being in progress etc).
     * If enabled, any reasons preventing the bridge from being returned are displayed to the
     * editor's error area.
     *
     * @param displayError whether to display the loading error or not.
*
* @return LayoutBridge the layout bridge for rendering this editor's scene
*/
    private LayoutLibrary getReadyLayoutLib(boolean displayError) {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
            IAndroidTarget target = getRenderingTarget();

            if (target != null) {
                AndroidTargetData data = currentSdk.getTargetData(target);
                if (data != null) {
                    LayoutLibrary layoutLib = data.getLayoutLibrary();

                    if (layoutLib.getBridge() != null) { // layoutLib can never be null.
                        return layoutLib;
                    } else if (displayError) { // getBridge() == null
                        // SDK is loaded but not the layout library!

                        // check whether the bridge managed to load, or not
                        if (layoutLib.getStatus() == LoadStatus.LOADING) {
                            displayError("Eclipse is loading framework information and the layout library from the SDK folder.\n%1$s will refresh automatically once the process is finished.",
                                         mEditedFile.getName());
                        } else {
                            displayError("Eclipse failed to load the framework information and the layout library!");
                        }
                    }
                } else { // data == null
                    // It can happen that the workspace refreshes while the SDK is loading its
                    // data, which could trigger a redraw of the opened layout if some resources
                    // changed while Eclipse is closed.
                    // In this case data could be null, but this is not an error.
                    // We can just silently return, as all the opened editors are automatically
                    // refreshed once the SDK finishes loading.
                    LoadStatus targetLoadStatus = currentSdk.checkAndLoadTargetData(target, null);

                    // display error is asked.
                    if (displayError) {
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
                    }
                }

            } else if (displayError) { // target == null
displayError("The project target is not set.");
}
        } else if (displayError) { // currentSdk == null
displayError("Eclipse is loading the SDK.\n%1$s will refresh automatically once the process is finished.",
mEditedFile.getName());
}
//Synthetic comment -- @@ -1130,6 +1134,37 @@
return null;
}

    /**
     * Returns the {@link IAndroidTarget} used for the rendering.
     * <p/>
     * This first looks for the rendering target setup in the config UI, and if nothing has
     * been setup yet, returns the target of the project.
     *
     * @return an IAndroidTarget object or null if no target is setup and the project has no
     * target set.
     *
     */
    private IAndroidTarget getRenderingTarget() {
        // if the SDK is null no targets are loaded.
        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk == null) {
            return null;
        }

        // attempt to get a target from the configuration selector.
        IAndroidTarget renderingTarget = mConfigComposite.getRenderingTarget();
        if (renderingTarget != null) {
            return renderingTarget;
        }

        // fall back to the project target
        if (mEditedFile != null) {
            return currentSdk.getTarget(mEditedFile.getProject());
        }

        return null;
    }

private boolean ensureModelValid(UiDocumentNode model) {
// check there is actually a model (maybe the file is empty).
if (model.getUiChildren().size() == 0) {
//Synthetic comment -- @@ -1186,7 +1221,7 @@
if (!ensureModelValid(model)) {
return null;
}
        LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
//Synthetic comment -- @@ -1355,7 +1390,7 @@
mConfiguredProjectRes = null;

// clear the cache in the bridge in case a bitmap/9-patch changed.
                LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);
if (layoutLib != null) {
if (layoutLib.getBridge() != null) {
layoutLib.getBridge().clearCaches(mEditedFile.getProject());
//Synthetic comment -- @@ -1386,16 +1421,7 @@
}

public LayoutLibrary getLayoutLibrary() {
        return getReadyLayoutLib(false /*displayError*/);
}

// ---- Error handling ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java
//Synthetic comment -- index f3df195..199e804 100644

//Synthetic comment -- @@ -76,6 +76,14 @@
return ""; //$NON-NLS-1$
}

    public VersionQualifier(int apiLevel) {
        mVersion = apiLevel;
    }

    public VersionQualifier() {
        //pass
    }

public int getVersion() {
return mVersion;
}
//Synthetic comment -- @@ -126,6 +134,38 @@
}

@Override
    public boolean isMatchFor(ResourceQualifier qualifier) {
        if (qualifier instanceof VersionQualifier) {
            // it is considered a match if the api level is equal or lower to the given qualifier
            return mVersion <= ((VersionQualifier) qualifier).mVersion;
        }

        return false;
    }

    @Override
    public boolean isBetterMatchThan(ResourceQualifier compareTo, ResourceQualifier reference) {
        if (compareTo == null) {
            return true;
        }

        VersionQualifier compareQ = (VersionQualifier)compareTo;
        VersionQualifier referenceQ = (VersionQualifier)reference;

        if (compareQ.mVersion == referenceQ.mVersion) {
            // what we have is already the best possible match (exact match)
            return false;
        } else if (mVersion == referenceQ.mVersion) {
            // got new exact value, this is the best!
            return true;
        } else {
            // in all case we're going to prefer the higher version (since they have been filtered
            // to not be too high
            return mVersion > compareQ.mVersion;
        }
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
     * Returns the actual  android.view.ViewGroup$LayoutParams (or child class) object.
     * This can be used to query the object properties that are not in the XML and not in
     * the map returned by {@link #getDefaultPropertyValues()}.
*/
public Object getLayoutParamsObject() {
return mLayoutParamsObject;







