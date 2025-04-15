/*Properly select dock/night mode combo when opening a layout.

Change-Id:I3c4f8b875f263970e4c8fc308b466ae14c69c438*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 052d4a5..0e824f1 100644

//Synthetic comment -- @@ -852,9 +852,6 @@
targetData = Sdk.getCurrent().getTargetData(mState.target);
} else {
findAndSetCompatibleConfig(false /*favorCurrentConfig*/);
// We don't want the -first- combobox item, we want the
// default one which is sometimes a different index
//mTargetCombo.select(0);
//Synthetic comment -- @@ -875,6 +872,28 @@
return targetData;
}

    private static class ConfigBundle {
        FolderConfiguration config;
        int localeIndex;
        int dockModeIndex;
        int nightModeIndex;

        ConfigBundle() {
            config = new FolderConfiguration();
            localeIndex = 0;
            dockModeIndex = 0;
            nightModeIndex = 0;
        }

        ConfigBundle(ConfigBundle bundle) {
            config = new FolderConfiguration();
            config.set(bundle.config);
            localeIndex = bundle.localeIndex;
            dockModeIndex = bundle.dockModeIndex;
            nightModeIndex = bundle.nightModeIndex;
        }
    }

/**
* Finds a device/config that can display {@link #mEditedConfig}.
* <p/>Once found the device and config combos are set to the config.
//Synthetic comment -- @@ -885,58 +904,64 @@
private void findAndSetCompatibleConfig(boolean favorCurrentConfig) {
LayoutDevice anyDeviceMatch = null; // a compatible device/config/locale
String anyConfigMatchName = null;
        ConfigBundle anyConfigBundle = null;

LayoutDevice bestDeviceMatch = null; // an actual best match
String bestConfigMatchName = null;
        ConfigBundle bestConfigBundle = null;

FolderConfiguration testConfig = new FolderConfiguration();

// get a locale that match the host locale roughly (may not be exact match on the region.)
        int localeHostMatch = getLocaleMatch();

        // build a list of combinations of non standard qualifiers to add to each device's
        // qualifier set when testing for a match.
        // These qualifiers are: locale, nightmode, car dock.
        List<ConfigBundle> addConfig = new ArrayList<ConfigBundle>();

        // If the edited file has locales, then we have to select a matching locale from
        // the list.
        // However, if it doesn't, we don't randomly take the first locale, we take one
        // matching the current host locale (making sure it actually exist in the project)
        int start, max;
        if (mEditedConfig.getLanguageQualifier() != null || localeHostMatch == -1) {
            // add all the locales
            start = 0;
            max = mLocaleList.size();
        } else {
            // only add the locale host match
            start = localeHostMatch;
            max = localeHostMatch + 1; // test is <
        }

        for (int i = start ; i < max ; i++) {
            ResourceQualifier[] l = mLocaleList.get(i);

            ConfigBundle bundle = new ConfigBundle();
            bundle.config.setLanguageQualifier((LanguageQualifier) l[LOCALE_LANG]);
            bundle.config.setRegionQualifier((RegionQualifier) l[LOCALE_REGION]);

            bundle.localeIndex = i;
            addConfig.add(bundle);
        }

        // add the dock mode to the bundle combinations.
        addDockModeToBundles(addConfig);

        // add the night mode to the bundle combinations.
        addNightModeToBundles(addConfig);

mainloop: for (LayoutDevice device : mDeviceList) {
for (DeviceConfig config : device.getConfigs()) {

                // loop on the list of qualifier adds
                for (ConfigBundle bundle : addConfig) {
                    // set the base config. This erase all data in testConfig.
                    testConfig.set(config.getConfig());

                    // add on top of it, the extra qualifiers
                    testConfig.add(bundle.config);

if (mEditedConfig.isMatchFor(testConfig)) {
// this is a basic match. record it in case we don't find a match
//Synthetic comment -- @@ -944,14 +969,14 @@
if (anyDeviceMatch == null) {
anyDeviceMatch = device;
anyConfigMatchName = config.getName();
                            anyConfigBundle = bundle;
}

if (isCurrentFileBestMatchFor(testConfig)) {
// this is what we want.
bestDeviceMatch = device;
bestConfigMatchName = config.getName();
                            bestConfigBundle = bundle;
break mainloop;
}
}
//Synthetic comment -- @@ -979,7 +1004,9 @@
// select the device anyway.
selectDevice(mState.device = anyDeviceMatch);
fillConfigCombo(anyConfigMatchName);
                mLocaleCombo.select(anyConfigBundle.localeIndex);
                mDockCombo.select(anyConfigBundle.dockModeIndex);
                mNightCombo.select(anyConfigBundle.nightModeIndex);

// TODO: display a better warning!
computeCurrentConfig();
//Synthetic comment -- @@ -988,7 +1015,7 @@
"'%1$s' is not a best match for any device/locale combination.",
mEditedConfig.toDisplayString()),
String.format(
                                "Displaying it with '%1$s' which is compatible, but will actually be displayed with another more specific version of the layout.",
mCurrentConfig.toDisplayString()));

} else {
//Synthetic comment -- @@ -999,10 +1026,48 @@
} else {
selectDevice(mState.device = bestDeviceMatch);
fillConfigCombo(bestConfigMatchName);
            mLocaleCombo.select(bestConfigBundle.localeIndex);
            mDockCombo.select(bestConfigBundle.dockModeIndex);
            mNightCombo.select(bestConfigBundle.nightModeIndex);
}
}

    private void addDockModeToBundles(List<ConfigBundle> addConfig) {
        ArrayList<ConfigBundle> list = new ArrayList<ConfigBundle>();

        // loop on each item and for each, add all variations of the dock modes
        for (ConfigBundle bundle : addConfig) {
            int index = 0;
            for (DockMode mode : DockMode.values()) {
                ConfigBundle b = new ConfigBundle(bundle);
                b.config.setDockModeQualifier(new DockModeQualifier(mode));
                b.dockModeIndex = index++;
                list.add(b);
            }
        }

        addConfig.clear();
        addConfig.addAll(list);
    }

    private void addNightModeToBundles(List<ConfigBundle> addConfig) {
        ArrayList<ConfigBundle> list = new ArrayList<ConfigBundle>();

        // loop on each item and for each, add all variations of the night modes
        for (ConfigBundle bundle : addConfig) {
            int index = 0;
            for (NightMode mode : NightMode.values()) {
                ConfigBundle b = new ConfigBundle(bundle);
                b.config.setNightModeQualifier(new NightModeQualifier(mode));
                b.nightModeIndex = index++;
                list.add(b);
            }
        }

        addConfig.clear();
        addConfig.addAll(list);
    }

/**
* Adapts the current device/config selection so that it's compatible with
* {@link #mEditedConfig}.
//Synthetic comment -- @@ -1241,7 +1306,7 @@
}
}

    private int getLocaleMatch() {
Locale locale = Locale.getDefault();
if (locale != null) {
String currentLanguage = locale.getLanguage();
//Synthetic comment -- @@ -1260,16 +1325,16 @@
if (langQ.getValue().equals(currentLanguage) &&
(regionQ.getValue().equals(currentRegion) ||
regionQ.getValue().equals(RegionQualifier.FAKE_REGION_VALUE))) {
                    return l;
}
}

// if no locale match the current local locale, it's likely that it is
// the default one which is the last one.
            return count - 1;
}

        return -1;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index 3a37634..b759e6e 100644

//Synthetic comment -- @@ -107,6 +107,18 @@
}

/**
     * Adds the non-qualifiers from the given config.
     * Qualifiers that are null in the given config do not change in the receiver.
     */
    public void add(FolderConfiguration config) {
        for (int i = 0 ; i < INDEX_COUNT ; i++) {
            if (config.mQualifiers[i] != null) {
                mQualifiers[i] = config.mQualifiers[i];
            }
        }
    }

    /**
* Returns the first invalid qualifier, or <code>null<code> if they are all valid (or if none
* exists).
*/







