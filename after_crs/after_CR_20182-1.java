/*Check if BT is allowed in airplane mode

Currenty, the bluetooth sharing activity always quits with an error
message if airplane mode is on. Instead, the activity should check
if bluetooth may be enabled in airplane mode, given the current
system settings.

Change-Id:If9272a941504054d7819e0248f93a8b5bca8d41e*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java b/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java
//Synthetic comment -- index 353cadc..d358b8c 100644

//Synthetic comment -- @@ -43,6 +43,7 @@
import android.app.Activity;
import android.bluetooth.BluetoothDevicePicker;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//Synthetic comment -- @@ -118,7 +119,7 @@
}
}

            if (!isBluetoothAllowed()) {
Intent in = new Intent(this, BluetoothOppBtErrorActivity.class);
in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
in.putExtra("title", this.getString(R.string.airplane_error_title));
//Synthetic comment -- @@ -164,11 +165,39 @@
finish();
}

    /* Returns true if Bluetooth is allowed given current airplane mode settings. */
    private final boolean isBluetoothAllowed() {
        final ContentResolver resolver = this.getContentResolver();

        // Check if airplane mode is on
        final boolean isAirplaneModeOn = Settings.System.getInt(resolver,
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        if (!isAirplaneModeOn) {
            return true;
        }

        // Check if airplane mode matters
        final String airplaneModeRadios = Settings.System.getString(resolver,
                Settings.System.AIRPLANE_MODE_RADIOS);
        final boolean isAirplaneSensitive = airplaneModeRadios == null ? true :
                airplaneModeRadios.contains(Settings.System.RADIO_BLUETOOTH);
        if (!isAirplaneSensitive) {
            return true;
        }

        // Check if Bluetooth may be enabled in airplane mode
        final String airplaneModeToggleableRadios = Settings.System.getString(resolver,
                Settings.System.AIRPLANE_MODE_TOGGLEABLE_RADIOS);
        final boolean isAirplaneToggleable = airplaneModeToggleableRadios == null ? false :
                airplaneModeToggleableRadios.contains(Settings.System.RADIO_BLUETOOTH);
        if (isAirplaneToggleable) {
            return true;
        }

        // If we get here we're not allowed to use Bluetooth right now
        return false;
}

private Uri creatFileForSharedContent(Context context, CharSequence shareContent) {
if (shareContent == null) {
return null;







