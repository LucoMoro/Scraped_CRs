
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
private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;
    private ArrayList mUsbIfaces;

private String[] mWifiRegexs;

ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> errored = intent.getStringArrayListExtra(
ConnectivityManager.EXTRA_ERRORED_TETHER);
                updateState(available.toArray(), active.toArray(), errored.toArray());
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
updateState();
updateState(available, tethered, errored);
}

    private void updateState(Object[] available, Object[] tethered,
            Object[] errored) {
ConnectivityManager cm =
(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
boolean usbTethered = false;
boolean usbErrored = false;
boolean massStorageActive =
Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
        for (Object o : available) {
            String s = (String)o;
for (String regex : mUsbRegexs) {
if (s.matches(regex)) {
usbAvailable = true;
}
}
}
        for (Object o : tethered) {
            String s = (String)o;
for (String regex : mUsbRegexs) {
if (s.matches(regex)) usbTethered = true;
}
}
        for (Object o: errored) {
            String s = (String)o;
for (String regex : mUsbRegexs) {
if (s.matches(regex)) usbErrored = true;
}

//<End of snippet n. 0>








