/*Properly select dock/night mode combo when opening a layout.

Change-Id:I3c4f8b875f263970e4c8fc308b466ae14c69c438*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 052d4a5..b4d87d8 100644

//Synthetic comment -- @@ -852,9 +852,6 @@
targetData = Sdk.getCurrent().getTargetData(mState.target);
} else {
findAndSetCompatibleConfig(false /*favorCurrentConfig*/);

                        mDockCombo.select(0);
                        mNightCombo.select(0);
// We don't want the -first- combobox item, we want the
// default one which is sometimes a different index
//mTargetCombo.select(0);
//Synthetic comment -- @@ -875,6 +872,28 @@
return targetData;
}

/**
* Finds a device/config that can display {@link #mEditedConfig}.
* <p/>Once found the device and config combos are set to the config.
//Synthetic comment -- @@ -885,58 +904,65 @@
private void findAndSetCompatibleConfig(boolean favorCurrentConfig) {
LayoutDevice anyDeviceMatch = null; // a compatible device/config/locale
String anyConfigMatchName = null;
        int anyLocaleIndex = -1;

LayoutDevice bestDeviceMatch = null; // an actual best match
String bestConfigMatchName = null;
        int bestLocaleIndex = -1;

FolderConfiguration testConfig = new FolderConfiguration();

// get a locale that match the host locale roughly (may not be exact match on the region.)
        ResourceQualifier[] localeHostMatch = getLocaleMatch();

mainloop: for (LayoutDevice device : mDeviceList) {
for (DeviceConfig config : device.getConfigs()) {
                testConfig.set(config.getConfig());

                // If the edited file has locales, then we have to select a matching locale from
                // the list.
                // However, if it doesn't, we don't randomly take the first locale, we take one
                // matching the current host locale (making sure it actually exist in the project)
                if (mEditedConfig.getLanguageQualifier() != null || localeHostMatch == null) {
                    // look on the locales.
                    for (int i = 0 ; i < mLocaleList.size() ; i++) {
                        ResourceQualifier[] locale = mLocaleList.get(i);

                        // update the test config with the locale qualifiers
                        testConfig.setLanguageQualifier((LanguageQualifier)locale[LOCALE_LANG]);
                        testConfig.setRegionQualifier((RegionQualifier)locale[LOCALE_REGION]);

                        if (mEditedConfig.isMatchFor(testConfig)) {
                            // this is a basic match. record it in case we don't find a match
                            // where the edited file is a best config.
                            if (anyDeviceMatch == null) {
                                anyDeviceMatch = device;
                                anyConfigMatchName = config.getName();
                                anyLocaleIndex = i;
                            }

                            if (isCurrentFileBestMatchFor(testConfig)) {
                                // this is what we want.
                                bestDeviceMatch = device;
                                bestConfigMatchName = config.getName();
                                bestLocaleIndex = i;
                                break mainloop;
                            }
                        }
                    }
                } else {
                    // update the test config with the locale qualifiers
                    testConfig.setLanguageQualifier(
                            (LanguageQualifier)localeHostMatch[LOCALE_LANG]);
                    testConfig.setRegionQualifier(
                            (RegionQualifier)localeHostMatch[LOCALE_REGION]);

if (mEditedConfig.isMatchFor(testConfig)) {
// this is a basic match. record it in case we don't find a match
//Synthetic comment -- @@ -944,14 +970,14 @@
if (anyDeviceMatch == null) {
anyDeviceMatch = device;
anyConfigMatchName = config.getName();
                            anyLocaleIndex = mLocaleList.indexOf(localeHostMatch);
}

if (isCurrentFileBestMatchFor(testConfig)) {
// this is what we want.
bestDeviceMatch = device;
bestConfigMatchName = config.getName();
                            bestLocaleIndex = mLocaleList.indexOf(localeHostMatch);
break mainloop;
}
}
//Synthetic comment -- @@ -979,7 +1005,9 @@
// select the device anyway.
selectDevice(mState.device = anyDeviceMatch);
fillConfigCombo(anyConfigMatchName);
                mLocaleCombo.select(anyLocaleIndex);

// TODO: display a better warning!
computeCurrentConfig();
//Synthetic comment -- @@ -988,7 +1016,7 @@
"'%1$s' is not a best match for any device/locale combination.",
mEditedConfig.toDisplayString()),
String.format(
                                "Displaying it with '%1$s'",
mCurrentConfig.toDisplayString()));

} else {
//Synthetic comment -- @@ -999,10 +1027,48 @@
} else {
selectDevice(mState.device = bestDeviceMatch);
fillConfigCombo(bestConfigMatchName);
            mLocaleCombo.select(bestLocaleIndex);
}
}

/**
* Adapts the current device/config selection so that it's compatible with
* {@link #mEditedConfig}.
//Synthetic comment -- @@ -1241,7 +1307,7 @@
}
}

    private ResourceQualifier[] getLocaleMatch() {
Locale locale = Locale.getDefault();
if (locale != null) {
String currentLanguage = locale.getLanguage();
//Synthetic comment -- @@ -1260,16 +1326,16 @@
if (langQ.getValue().equals(currentLanguage) &&
(regionQ.getValue().equals(currentRegion) ||
regionQ.getValue().equals(RegionQualifier.FAKE_REGION_VALUE))) {
                    return localeArray;
}
}

// if no locale match the current local locale, it's likely that it is
// the default one which is the last one.
            return mLocaleList.get(count - 1);
}

        return null;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index 3a37634..b759e6e 100644

//Synthetic comment -- @@ -107,6 +107,18 @@
}

/**
* Returns the first invalid qualifier, or <code>null<code> if they are all valid (or if none
* exists).
*/







