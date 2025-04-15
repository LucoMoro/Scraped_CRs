/*Prevent NPEs in device comparator

This changeset just adds some extra null checking
on various fields accessed by the the phone and
tablet comparators in the configuration composite.

This should prevent the various NPEs reported in
issue 17522, issue 24578, and issue 24050.

Change-Id:I93221a833e598484c11b30b0c5021a97e5d7440a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 9c8d33b..88ec00d 100644

//Synthetic comment -- @@ -1158,7 +1158,7 @@
final String name;
final ConfigBundle bundle;

        public ConfigMatch(@NonNull FolderConfiguration testConfig, Device device, String name,
ConfigBundle bundle) {
this.testConfig = testConfig;
this.device = device;
//Synthetic comment -- @@ -1317,18 +1317,40 @@
private static class TabletConfigComparator implements Comparator<ConfigMatch> {
@Override
public int compare(ConfigMatch o1, ConfigMatch o2) {
            FolderConfiguration config1 = o1 != null ? o1.testConfig : null;
            FolderConfiguration config2 = o2 != null ? o2.testConfig : null;
            if (config1 == null) {
                if (config2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (config2 == null) {
                return 1;
            }

            ScreenSizeQualifier size1 = config1.getScreenSizeQualifier();
            ScreenSizeQualifier size2 = config2.getScreenSizeQualifier();
            ScreenSize ss1 = size1 != null ? size1.getValue() : ScreenSize.NORMAL;
            ScreenSize ss2 = size2 != null ? size2.getValue() : ScreenSize.NORMAL;

// X-LARGE is better than all others (which are considered identical)
// if both X-LARGE, then LANDSCAPE is better than all others (which are identical)

if (ss1 == ScreenSize.XLARGE) {
if (ss2 == ScreenSize.XLARGE) {
                    ScreenOrientationQualifier orientation1 =
                            config1.getScreenOrientationQualifier();
                    ScreenOrientation so1 = orientation1.getValue();
                    if (so1 == null) {
                        so1 = ScreenOrientation.PORTRAIT;
                    }
                    ScreenOrientationQualifier orientation2 =
                            config2.getScreenOrientationQualifier();
                    ScreenOrientation so2 = orientation2.getValue();
                    if (so2 == null) {
                        so2 = ScreenOrientation.PORTRAIT;
                    }

if (so1 == ScreenOrientation.LANDSCAPE) {
if (so2 == ScreenOrientation.LANDSCAPE) {
//Synthetic comment -- @@ -1369,24 +1391,55 @@

@Override
public int compare(ConfigMatch o1, ConfigMatch o2) {
            FolderConfiguration config1 = o1 != null ? o1.testConfig : null;
            FolderConfiguration config2 = o2 != null ? o2.testConfig : null;
            if (config1 == null) {
                if (config2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (config2 == null) {
                return 1;
}

            int dpi1 = Density.DEFAULT_DENSITY;
int dpi2 = Density.DEFAULT_DENSITY;

            DensityQualifier dpiQualifier1 = config1.getDensityQualifier();
            if (dpiQualifier1 != null) {
                Density value = dpiQualifier1.getValue();
                dpi1 = value != null ? value.getDpiValue() : Density.DEFAULT_DENSITY;
}
            dpi1 = mDensitySort.get(dpi1, 100 /* valueIfKeyNotFound*/);

            DensityQualifier dpiQualifier2 = config2.getDensityQualifier();
            if (dpiQualifier2 != null) {
                Density value = dpiQualifier2.getValue();
                dpi2 = value != null ? value.getDpiValue() : Density.DEFAULT_DENSITY;
            }
            dpi2 = mDensitySort.get(dpi2, 100 /* valueIfKeyNotFound*/);

if (dpi1 == dpi2) {
// portrait is better
                ScreenOrientation so1 = ScreenOrientation.PORTRAIT;
                ScreenOrientationQualifier orientationQualifier1 =
                        config1.getScreenOrientationQualifier();
                if (orientationQualifier1 != null) {
                    so1 = orientationQualifier1.getValue();
                    if (so1 == null) {
                        so1 = ScreenOrientation.PORTRAIT;
                    }
                }
                ScreenOrientation so2 = ScreenOrientation.PORTRAIT;
                ScreenOrientationQualifier orientationQualifier2 =
                        config2.getScreenOrientationQualifier();
                if (orientationQualifier2 != null) {
                    so2 = orientationQualifier2.getValue();
                    if (so2 == null) {
                        so2 = ScreenOrientation.PORTRAIT;
                    }
                }

if (so1 == ScreenOrientation.PORTRAIT) {
if (so2 == ScreenOrientation.PORTRAIT) {







