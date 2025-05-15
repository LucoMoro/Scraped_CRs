
//<Beginning of snippet n. 0>


import android.app.Activity;
import android.bluetooth.BluetoothDevicePicker;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
}
}

            if (!isBluetoothAllowed()) {
Intent in = new Intent(this, BluetoothOppBtErrorActivity.class);
in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
in.putExtra("title", this.getString(R.string.airplane_error_title));
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

//<End of snippet n. 0>








