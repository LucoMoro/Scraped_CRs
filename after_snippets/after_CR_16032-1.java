
//<Beginning of snippet n. 0>


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.webkit.WebView;

import java.io.InputStream;
private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;

private String[] mWifiRegexs;

ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> errored = intent.getStringArrayListExtra(
ConnectivityManager.EXTRA_ERRORED_TETHER);
                updateState((String[]) available.toArray(), (String[]) active.toArray(), (String[]) errored.toArray());
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
updateState();
updateState(available, tethered, errored);
}

    private void updateState(String[] available, String[] tethered,
            String[] errored) {
ConnectivityManager cm =
(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
boolean usbTethered = false;
boolean usbErrored = false;
boolean massStorageActive =
Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
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
        for (String s: errored) {
for (String regex : mUsbRegexs) {
if (s.matches(regex)) usbErrored = true;
}

//<End of snippet n. 0>








