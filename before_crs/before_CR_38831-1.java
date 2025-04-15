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

        public ConfigMatch(FolderConfiguration testConfig, Device device, String name,
ConfigBundle bundle) {
this.testConfig = testConfig;
this.device = device;
//Synthetic comment -- @@ -1317,18 +1317,40 @@
private static class TabletConfigComparator implements Comparator<ConfigMatch> {
@Override
public int compare(ConfigMatch o1, ConfigMatch o2) {
            ScreenSize ss1 = o1.testConfig.getScreenSizeQualifier().getValue();
            ScreenSize ss2 = o2.testConfig.getScreenSizeQualifier().getValue();

// X-LARGE is better than all others (which are considered identical)
// if both X-LARGE, then LANDSCAPE is better than all others (which are identical)

if (ss1 == ScreenSize.XLARGE) {
if (ss2 == ScreenSize.XLARGE) {
                    ScreenOrientation so1 =
                        o1.testConfig.getScreenOrientationQualifier().getValue();
                    ScreenOrientation so2 =
                        o2.testConfig.getScreenOrientationQualifier().getValue();

if (so1 == ScreenOrientation.LANDSCAPE) {
if (so2 == ScreenOrientation.LANDSCAPE) {
//Synthetic comment -- @@ -1369,24 +1391,55 @@

@Override
public int compare(ConfigMatch o1, ConfigMatch o2) {
            int dpi1 = Density.DEFAULT_DENSITY;
            if (o1.testConfig.getDensityQualifier() != null) {
                dpi1 = o1.testConfig.getDensityQualifier().getValue().getDpiValue();
                dpi1 = mDensitySort.get(dpi1, 100 /* valueIfKeyNotFound*/);
}

int dpi2 = Density.DEFAULT_DENSITY;
            if (o2.testConfig.getDensityQualifier() != null) {
                dpi2 = o2.testConfig.getDensityQualifier().getValue().getDpiValue();
                dpi2 = mDensitySort.get(dpi2, 100 /* valueIfKeyNotFound*/);
}

if (dpi1 == dpi2) {
// portrait is better
                ScreenOrientation so1 =
                    o1.testConfig.getScreenOrientationQualifier().getValue();
                ScreenOrientation so2 =
                    o2.testConfig.getScreenOrientationQualifier().getValue();

if (so1 == ScreenOrientation.PORTRAIT) {
if (so2 == ScreenOrientation.PORTRAIT) {







