//<Beginning of snippet n. 0>


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemProperties;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.webkit.WebView;

private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;
private String[] mWifiRegexs;

private void handleIntent(Intent intent) {
    ArrayList<String> available = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER);
    ArrayList<String> tethered = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_TETHERED);
    ArrayList<String> errored = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER);
    
    if (available != null && tethered != null && errored != null) {
        String[] availableArray = available.toArray(new String[0]);
        String[] tetheredArray = tethered.toArray(new String[0]);
        String[] erroredArray = errored.toArray(new String[0]);
        updateState(availableArray, tetheredArray, erroredArray);
    } else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
               intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
        updateState(new String[0], new String[0], new String[0]);
    }
}

private void updateState(String[] available, String[] tethered, String[] errored) {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean usbAvailable = false;
    boolean usbTethered = false;
    boolean usbErrored = false;
    boolean massStorageActive = Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
    
    for (String s : available) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) {
                usbAvailable = true;
                break; // Break once a match is found
            }
        }
    }
    
    for (String s : tethered) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) {
                usbTethered = true;
                break; // Break once a match is found
            }
        }
    }
    
    for (String s : errored) {
        for (String regex : mUsbRegexs) {
            if (s.matches(regex)) {
                usbErrored = true;
                break; // Break once a match is found
            }
        }
    }
}

//<End of snippet n. 0>