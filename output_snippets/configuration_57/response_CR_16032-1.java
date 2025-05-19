//<Beginning of snippet n. 0>

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;

private String[] mWifiRegexs;

ArrayList<String> availableList = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> tetheredList = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> erroredList = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER);

String[] available = availableList != null ? availableList.toArray(new String[0]) : new String[0];
String[] tethered = tetheredList != null ? tetheredList.toArray(new String[0]) : new String[0];
String[] errored = erroredList != null ? erroredList.toArray(new String[0]) : new String[0];

updateState(available, tethered, errored);

if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) || intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
    updateState(new String[0], new String[0], new String[0]);
}

private void updateState(String[] available, String[] tethered, String[] errored) {
    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean usbAvailable = false;
    boolean usbTethered = false;
    boolean usbErrored = false;
    boolean massStorageActive = Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
    for (String s : available) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) {
                usbAvailable = true;
            }
        }
    }
    for (String s : tethered) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) usbTethered = true;
        }
    }
    for (String s : errored) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) usbErrored = true;
        }
    }
}

//<End of snippet n. 0>