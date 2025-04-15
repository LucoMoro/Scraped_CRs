/*Mms: SIM Sms not refreshed after sim Hot swap

This patch adds a broadcast receiver for SIM ABSENT and clears
the internal variables resulting refresh of SIM SMS data settings
item.

The New SIM SMS will be loaded when the user re-enters the view.

Change-Id:Iba1dd8d6aea98cb450587e2d0ae986d678b05d0bAuthor: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61711*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index beadb54..d48cbbd 100644

//Synthetic comment -- @@ -21,8 +21,11 @@
import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.ContentObserver;
//Synthetic comment -- @@ -47,6 +50,9 @@
import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;

/**
* Displays a list of the SMS messages stored on the ICC.
*/
//Synthetic comment -- @@ -74,6 +80,18 @@

public static final int SIM_FULL_NOTIFICATION_ID = 234;

private final ContentObserver simChangeObserver =
new ContentObserver(new Handler()) {
@Override
//Synthetic comment -- @@ -224,11 +242,15 @@
public void onPause() {
super.onPause();
mContentResolver.unregisterContentObserver(simChangeObserver);
}

private void registerSimChangeObserver() {
mContentResolver.registerContentObserver(
ICC_URI, true, simChangeObserver);
}

private void copyToPhoneMemory(Cursor cursor) {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessagingPreferenceActivity.java b/src/com/android/mms/ui/MessagingPreferenceActivity.java
//Synthetic comment -- index 1d498cb..662d95e 100755

//Synthetic comment -- @@ -19,10 +19,12 @@

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
//Synthetic comment -- @@ -44,6 +46,9 @@
import com.android.mms.transaction.TransactionService;
import com.android.mms.util.Recycler;

/**
* With this activity, users can set preferences for MMS and SMS and
* can access and manipulate SMS messages stored on the SIM.
//Synthetic comment -- @@ -85,6 +90,23 @@
private CharSequence[] mVibrateEntries;
private CharSequence[] mVibrateValues;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -105,6 +127,12 @@
registerListeners();
}

private void loadPrefs() {
addPreferencesFromResource(R.xml.preferences);

//Synthetic comment -- @@ -346,6 +374,10 @@

private void registerListeners() {
mVibrateWhenPref.setOnPreferenceChangeListener(this);
}

public boolean onPreferenceChange(Preference preference, Object newValue) {







