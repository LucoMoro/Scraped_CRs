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
import java.util.ArrayList;

private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;
private ArrayList<String> mUsbIfaces;

private String[] mWifiRegexs;

ArrayList<String> available = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> tethered = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_TETHERED);
ArrayList<String> errored = intent.getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER);
updateState(available.toArray(new String[0]), tethered.toArray(new String[0]), errored.toArray(new String[0]));
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
        intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
    updateState(new String[0], new String[0], new String[0]);
}

private void updateState(String[] available, String[] tethered, String[] errored) {
    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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