/*Hide tvdpi previews for now

Ideally, hook this up to render capabilities later.

Also customize the detailed message in the layout editor
when the Nexus 7 is chosen.

Change-Id:I7606dbb89d9fda643b175a51177624b564c417cf*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index 66a29c8..d42c3f6 100644

//Synthetic comment -- @@ -17,7 +17,9 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.resources.NightMode;
import com.android.resources.UiMode;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -282,6 +284,11 @@
for (Device d : devices) {
double size = getScreenSize(d);
if (size >= from && size < to) {
device = d;
break;
}
//Synthetic comment -- @@ -294,6 +301,26 @@

return device;
}
private static double getScreenSize(@NonNull Device device) {
Hardware hardware = device.getDefaultHardware();
if (hardware != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 93fdb5e..9b8eeea 100644

//Synthetic comment -- @@ -2048,6 +2048,21 @@
"or fix the theme style references.\n\n");
}

if (hasAaptErrors && logger.seenTagPrefix(LayoutLog.TAG_RESOURCES_PREFIX)) {
// Text will automatically be wrapped by the error widget so no reason
// to insert linebreaks in this error message:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 80391cd..a7c3e88 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -73,6 +74,9 @@
* managing the image buffer cache, etc
*/
public class RenderPreviewManager {
private static double sScale = 1.0;
private static final int RENDER_DELAY = 150;
private static final int PREVIEW_VGAP = 18;
//Synthetic comment -- @@ -938,6 +942,10 @@
if (Density.LOW.equals(d)) {
interesting = false;
}
}
}








