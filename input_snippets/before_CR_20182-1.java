
//<Beginning of snippet n. 0>


import android.app.Activity;
import android.bluetooth.BluetoothDevicePicker;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
}
}

            if (isAirplaneModeOn()) {
Intent in = new Intent(this, BluetoothOppBtErrorActivity.class);
in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
in.putExtra("title", this.getString(R.string.airplane_error_title));
finish();
}

    /* Returns true if airplane mode is currently on */
    private final boolean isAirplaneModeOn() {
        return Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
                0) == 1;
}
private Uri creatFileForSharedContent(Context context, CharSequence shareContent) {
if (shareContent == null) {
return null;

//<End of snippet n. 0>








