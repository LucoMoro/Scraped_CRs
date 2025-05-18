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

import java.io.InputStream;
import android.net.ConnectivityManager;

private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;

private String[] mWifiRegexs;

private String[] available;
private String[] tethered;
private String[] errored;

if (intent.getAction().equals(Intent.ACTION_TETHER_STATE_CHANGED)) {
    available = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER) != null 
                 ? intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER).toArray(new String[0]) 
                 : new String[0];
    tethered = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER) != null 
                ? intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER).toArray(new String[0]) 
                : new String[0];
    errored = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER) != null 
               ? intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER).toArray(new String[0]) 
               : new String[0];
    updateState(available, tethered, errored);
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
        intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
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
//<End of snippet n. 0>