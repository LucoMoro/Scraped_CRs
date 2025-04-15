/*Remove unused imports in SecuritySettings.java

Change-Id:Ibce3dbdd8ee8a6cd1727313f3a2502717ee7f30a*/
//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index 1348d48..b29db2d 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
//Synthetic comment -- @@ -32,20 +31,16 @@
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.security.Credentials;
import android.security.KeyStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;








//Synthetic comment -- diff --git a/src/com/android/settings/SoundSettings.java b/src/com/android/settings/SoundSettings.java
//Synthetic comment -- index bfb5566..a735268 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.settings;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;







