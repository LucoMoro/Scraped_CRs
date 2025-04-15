/*Hide tvdpi and ldpi devices from the device menu

(until ninepatch rendering bug is fixed)

Change-Id:I1373355e28d695c921840cbc18c2182053daf30e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index 32f8e9d..5e503f9 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.internal.avd.AvdInfo;
//Synthetic comment -- @@ -146,6 +147,17 @@
// Nexus
for (List<Device> devices : manufacturers.values()) {
for (Device device : devices) {
                            Screen screen = device.getDefaultHardware().getScreen();
                            if (screen != null) {
                                Density density = screen.getPixelDensity();
                                if (density == Density.TV || density == Density.LOW) {
                                    // Skip tvdpi and ldpi devices for now, until
                                    //  http://code.google.com/p/android/issues/detail?id=37472
                                    // is fixed.
                                    continue;
                                }
                            }

if (isNexus(device)) {
if (device.getManufacturer().equals(GENERIC)) {
generic.add(device);







