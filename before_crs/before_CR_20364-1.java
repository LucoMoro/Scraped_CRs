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
import java.util.Map;
import java.util.SortedSet;

//Synthetic comment -- @@ -892,17 +893,50 @@

FolderConfiguration testConfig = new FolderConfiguration();

mainloop: for (LayoutDevice device : mDeviceList) {
for (DeviceConfig config : device.getConfigs()) {
testConfig.set(config.getConfig());

                // look on the locales.
                for (int i = 0 ; i < mLocaleList.size() ; i++) {
                    ResourceQualifier[] locale = mLocaleList.get(i);

// update the test config with the locale qualifiers
                    testConfig.setLanguageQualifier((LanguageQualifier)locale[LOCALE_LANG]);
                    testConfig.setRegionQualifier((RegionQualifier)locale[LOCALE_REGION]);

if (mEditedConfig.isMatchFor(testConfig)) {
// this is a basic match. record it in case we don't find a match
//Synthetic comment -- @@ -910,14 +944,14 @@
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
//Synthetic comment -- @@ -1207,6 +1241,37 @@
}
}

/**
* Updates the theme combo.
* This must be called from the UI thread.







