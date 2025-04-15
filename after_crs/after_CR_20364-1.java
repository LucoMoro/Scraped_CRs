/*Choose a good locale when opening a file for the first time.

Because the selection of the full config to use for display
is complicated (due to having to find something that matches
the configuration of the edited file), this is a bit more
complex than simply changing the select in updateLocales().

In fact the selection made there when there is no stored config
is pointless. Instead it's decided later by findAndSetCompatibleConfig
which find the proper combination of device and locale*

* this is actually broken since we added the nightmode and dock mode.
We need to include those in the list of config we try out. I'll fix
this in a later patch.

Change-Id:I83599dc7f0d1a921f9568b6de5c065ca1e82e103*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 5fd953d..052d4a5 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

//Synthetic comment -- @@ -892,17 +893,50 @@

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
//Synthetic comment -- @@ -910,14 +944,14 @@
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
//Synthetic comment -- @@ -1207,6 +1241,37 @@
}
}

    private ResourceQualifier[] getLocaleMatch() {
        Locale locale = Locale.getDefault();
        if (locale != null) {
            String currentLanguage = locale.getLanguage();
            String currentRegion = locale.getCountry();

            final int count = mLocaleList.size();
            for (int l = 0 ; l < count ; l++) {
                ResourceQualifier[] localeArray = mLocaleList.get(l);
                LanguageQualifier langQ = (LanguageQualifier)localeArray[LOCALE_LANG];
                RegionQualifier regionQ = (RegionQualifier)localeArray[LOCALE_REGION];

                // there's always a ##/Other or ##/Any (which is the same, the region
                // contains FAKE_REGION_VALUE). If we don't find a perfect region match
                // we take the fake region. Since it's last in the list, this makes the
                // test easy.
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
* Updates the theme combo.
* This must be called from the UI thread.







