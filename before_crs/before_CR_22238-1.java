/*Fix phone sort in configuration chooser.

When trying to figure out the best device to use to render a given
layout, we sort all the potential candidates. The sort would
favor lower densities before higher ones.

This new sort gives us flexibility in which density are higher
priority.
The current order is: high, med, xhigh, low.

Change-Id:Ie0d5a583bb850d0a2888e973a9e93cfac27bc4d3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index f941ceb..c6c1d37 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
import com.android.resources.ScreenSize;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.Pair;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -984,15 +985,28 @@
* Note: this comparator imposes orderings that are inconsistent with equals.
*/
private static class PhoneConfigComparator implements Comparator<ConfigMatch> {
public int compare(ConfigMatch o1, ConfigMatch o2) {
int dpi1 = Density.DEFAULT_DENSITY;
if (o1.testConfig.getPixelDensityQualifier() != null) {
dpi1 = o1.testConfig.getPixelDensityQualifier().getValue().getDpiValue();
}

int dpi2 = Density.DEFAULT_DENSITY;
if (o2.testConfig.getPixelDensityQualifier() != null) {
dpi2 = o2.testConfig.getPixelDensityQualifier().getValue().getDpiValue();
}

if (dpi1 == dpi2) {







