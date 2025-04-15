/*Refactor to remove multiple in-loop Object[]->String[] castings

(This replacesIaa0f3b25eaadb094a4c3fb4cecbd09e0322aae33)

This change removes the need to convert Object[]->String[] for each iteration
of each of available,tethered,errored states in updateStatus()
by converting these as soon as possible.

This fix (and the code before) is strange because each of available,tethered,
errored are defined as type ArrayList<String> but
intent.getStringArrayListExtra() returns type Object[] dispite its name.
Because other calls to updateState() use String[] it seems best to do the
conversion outside this method in the Object[] case and thus avoid the
per-iteration casting in the Object[] case and the downright wasteful
per-iteration String[]->Object[]->String[] casting in the String[] input cases.

I've also removed some unused imports and an unused variable.

Change-Id:I805ef19aa9ceff7e4fc491623cdcb413ce170798*/




//Synthetic comment -- diff --git a/src/com/android/settings/TetherSettings.java b/src/com/android/settings/TetherSettings.java
//Synthetic comment -- index 9eee4e0..05e7a3b 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -33,8 +32,6 @@
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.webkit.WebView;

import java.io.InputStream;
//Synthetic comment -- @@ -67,7 +64,6 @@
private BroadcastReceiver mTetherChangeReceiver;

private String[] mUsbRegexs;

private String[] mWifiRegexs;

//Synthetic comment -- @@ -161,7 +157,8 @@
ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> errored = intent.getStringArrayListExtra(
ConnectivityManager.EXTRA_ERRORED_TETHER);
                updateState((String[]) available.toArray(), (String[]) active.toArray(),
                		(String[]) errored.toArray());
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
updateState();
//Synthetic comment -- @@ -205,8 +202,8 @@
updateState(available, tethered, errored);
}

    private void updateState(String[] available, String[] tethered,
            String[] errored) {
ConnectivityManager cm =
(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
boolean usbTethered = false;
//Synthetic comment -- @@ -215,8 +212,7 @@
boolean usbErrored = false;
boolean massStorageActive =
Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
        for (String s : available) {
for (String regex : mUsbRegexs) {
if (s.matches(regex)) {
usbAvailable = true;
//Synthetic comment -- @@ -226,14 +222,12 @@
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







