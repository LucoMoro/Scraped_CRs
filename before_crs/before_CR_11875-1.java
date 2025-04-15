/*Modify the Power Control widget to use
PowerManager.setScreenBrightnessOverride instead of
HardwareService.setBrightness.

This is due to a bug where the method in HardwareService turns on ALL
lights and does not reset any sort of timer on them, so they stay on.*/
//Synthetic comment -- diff --git a/src/com/android/settings/widget/SettingsAppWidgetProvider.java b/src/com/android/settings/widget/SettingsAppWidgetProvider.java
//Synthetic comment -- index d8747c9..96b8678 100644

//Synthetic comment -- @@ -31,8 +31,10 @@
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IHardwareService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
//Synthetic comment -- @@ -337,9 +339,10 @@
*/
private void toggleBrightness(Context context) {
try {
            IHardwareService hardware = IHardwareService.Stub.asInterface(
                    ServiceManager.getService("hardware"));
            if (hardware != null) {
ContentResolver cr = context.getContentResolver();
int brightness = Settings.System.getInt(cr,
Settings.System.SCREEN_BRIGHTNESS);
//Synthetic comment -- @@ -352,7 +355,7 @@
} else {
brightness = MINIMUM_BACKLIGHT;
}
                hardware.setBacklights(brightness);
Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS, brightness);
brightness = Settings.System.getInt(cr,
Settings.System.SCREEN_BRIGHTNESS);







