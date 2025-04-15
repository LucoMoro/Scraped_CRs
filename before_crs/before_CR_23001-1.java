/*Improve the choice of default configuration

When you open a new layout, the layout editor needs to pick an initial
configuration for the layout.  Currently, the choice of layout is
based on the project API level (such that it picks tablet screen and
landscape orientation for API 11, and phone and portrait for lower
APIs).

This changeset adds another factor: the currently used configuration.
If you have another layout visible and you open a new layout, then the
configuration for the current layout will be used for the new layout
(if it is a compatible match).

Change-Id:I66302e7ffb13c9b66e6fd0f7f347ebf4df142f77*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index d304303..dcc94a9 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -74,6 +75,11 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -863,6 +869,8 @@
// add the night mode to the bundle combinations.
addNightModeToBundles(configBundles);

for (LayoutDevice device : mDeviceList) {
for (DeviceConfig config : device.getConfigs()) {

//Synthetic comment -- @@ -1045,10 +1053,47 @@
Collections.sort(matches, new PhoneConfigComparator());
}

// the list has been sorted so that the first item is the best config
return matches.get(0);
}

private void addDockModeToBundles(List<ConfigBundle> addConfig) {
ArrayList<ConfigBundle> list = new ArrayList<ConfigBundle>();

//Synthetic comment -- @@ -1282,16 +1327,16 @@
SortedSet<String> regions = projectRes.getRegions(language);
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







