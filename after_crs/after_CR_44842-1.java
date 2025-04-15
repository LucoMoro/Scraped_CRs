/*Hide tvdpi previews for now

Ideally, hook this up to render capabilities later.

Change-Id:I7606dbb89d9fda643b175a51177624b564c417cf*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index 66a29c8..d42c3f6 100644

//Synthetic comment -- @@ -17,7 +17,9 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderPreviewManager;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.UiMode;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -282,6 +284,11 @@
for (Device d : devices) {
double size = getScreenSize(d);
if (size >= from && size < to) {
                    if (RenderPreviewManager.HIDE_TVDPI &&
                            getDensity(d) == Density.TV) {
                        continue;
                    }

device = d;
break;
}
//Synthetic comment -- @@ -294,6 +301,26 @@

return device;
}

    /**
     * Returns the density of the given device
     *
     * @param device the device to check
     * @return the density or null
     */
    @Nullable
    public static Density getDensity(@NonNull Device device) {
        Hardware hardware = device.getDefaultHardware();
        if (hardware != null) {
            Screen screen = hardware.getScreen();
            if (screen != null) {
                return screen.getPixelDensity();
            }
        }

        return null;
    }

private static double getScreenSize(@NonNull Device device) {
Hardware hardware = device.getDefaultHardware();
if (hardware != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 80391cd..a7c3e88 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -73,6 +74,9 @@
* managing the image buffer cache, etc
*/
public class RenderPreviewManager {
    /** TODO: Tie this to a {@link Capability} instead */
    public static boolean HIDE_TVDPI = true;

private static double sScale = 1.0;
private static final int RENDER_DELAY = 150;
private static final int PREVIEW_VGAP = 18;
//Synthetic comment -- @@ -938,6 +942,10 @@
if (Density.LOW.equals(d)) {
interesting = false;
}

                    if (HIDE_TVDPI && d == Density.TV) {
                        interesting = false;
                    }
}
}








