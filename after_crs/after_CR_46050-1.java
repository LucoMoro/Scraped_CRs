/*Contacts(DSDS): Add support for DSDS

Change-Id:I5522c5c1e7a8545516e6cc68d2f8f4c0959b211c*/




//Synthetic comment -- diff --git a/src/com/android/contacts/SpecialCharSequenceMgr.java b/src/com/android/contacts/SpecialCharSequenceMgr.java
//Synthetic comment -- index 4902fde..cf334ad 100644

//Synthetic comment -- @@ -1,5 +1,8 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2011-12, Code Aurora Forum. All rights reserved.
 * Not a Contribution, Apache license notifications and license are retained
 * for attribution purposes only
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,6 +19,7 @@

package com.android.contacts;

import com.android.internal.telephony.msim.ITelephonyMSim;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.TelephonyCapabilities;
import com.android.internal.telephony.TelephonyIntents;
//Synthetic comment -- @@ -33,6 +37,9 @@
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.MSimTelephonyManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
//Synthetic comment -- @@ -55,6 +62,8 @@
public class SpecialCharSequenceMgr {
private static final String TAG = "SpecialCharSequenceMgr";
private static final String MMI_IMEI_DISPLAY = "*#06#";
    private static final int SUB1 = 0;
    private static final int SUB2 = 1;

/**
* Remembers the previous {@link QueryHandler} and cancel the operation when needed, to
//Synthetic comment -- @@ -168,6 +177,9 @@
}

int len = input.length();
        int subscription = 0;
        Uri uri = null;

if ((len > 1) && (len < 5) && (input.endsWith("#"))) {
try {
// get the ordinal number of the sim contact
//Synthetic comment -- @@ -203,9 +215,22 @@

// display the progress dialog
sc.progressDialog.show();
                subscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();

                if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
                    if(subscription == SUB1) {
                        uri = Uri.parse("content://iccmsim/adn");
                    } else if (subscription == SUB2) {
                        uri = Uri.parse("content://iccmsim/adn_sub2");
                    } else {
                        Log.d(TAG, "handleAdnEntry:Invalid Subscription");
                    }
                } else {
                    uri = Uri.parse("content://icc/adn");
                }

// run the query.
                handler.startQuery(ADN_QUERY_TOKEN, sc, uri,
new String[]{ADN_PHONE_NUMBER_COLUMN_NAME}, null, null, null);

if (sPreviousAdnQueryHandler != null) {
//Synthetic comment -- @@ -222,10 +247,19 @@
}

static boolean handlePinEntry(Context context, String input) {
        int subscription = 0;
if ((input.startsWith("**04") || input.startsWith("**05")) && input.endsWith("#")) {
try {
                // Use Voice Subscription for both change PIN & unblock PIN using PUK.
                subscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();
                Log.d(TAG, "Sending MMI on subscription :" + subscription);
                if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
                    return ITelephonyMSim.Stub.asInterface(ServiceManager.getService("phone_msim"))
                            .handlePinMmi(input, subscription);
                } else {
                    return ITelephony.Stub.asInterface(ServiceManager.getService("phone"))
                            .handlePinMmi(input);
                }
} catch (RemoteException e) {
Log.e(TAG, "Failed to handlePinMmi due to remote exception");
return false;
//Synthetic comment -- @@ -235,15 +269,22 @@
}

static boolean handleIMEIDisplay(Context context, String input, boolean useSystemWindow) {
        if (input.equals(MMI_IMEI_DISPLAY)) {
            int subscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();
            int phoneType;
            if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
                phoneType = ((MSimTelephonyManager)context.getSystemService(
                        Context.MSIM_TELEPHONY_SERVICE)).getCurrentPhoneType(subscription);
            } else {
                phoneType = ((TelephonyManager)context.getSystemService(
                        Context.TELEPHONY_SERVICE)).getCurrentPhoneType();
            }

if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
                showIMEIPanel(context, useSystemWindow);
return true;
} else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                showMEIDPanel(context, useSystemWindow);
return true;
}
}
//Synthetic comment -- @@ -256,10 +297,16 @@
// version of SpecialCharSequenceMgr.java.  (This will require moving
// the phone app's TelephonyCapabilities.getDeviceIdLabel() method
// into the telephony framework, though.)
    static void showIMEIPanel(Context context, boolean useSystemWindow) {
        int subscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();
        String imeiStr;
        if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
            imeiStr = ((MSimTelephonyManager)context.
                    getSystemService(Context.MSIM_TELEPHONY_SERVICE)).getDeviceId(subscription);
        } else {
            imeiStr = ((TelephonyManager)context.
                    getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }

AlertDialog alert = new AlertDialog.Builder(context)
.setTitle(R.string.imei)
//Synthetic comment -- @@ -269,9 +316,16 @@
.show();
}

    static void showMEIDPanel(Context context, boolean useSystemWindow) {
        int subscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();
        String meidStr;
        if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
            meidStr = ((MSimTelephonyManager)context.
                    getSystemService(Context.MSIM_TELEPHONY_SERVICE)).getDeviceId(subscription);
        } else {
            meidStr = ((TelephonyManager)context.
                    getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }

AlertDialog alert = new AlertDialog.Builder(context)
.setTitle(R.string.meid)








//Synthetic comment -- diff --git a/src/com/android/contacts/activities/DialtactsActivity.java b/src/com/android/contacts/activities/DialtactsActivity.java
//Synthetic comment -- index b5b0a63..67048f5 100644

//Synthetic comment -- @@ -53,6 +53,7 @@
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.MSimTelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
//Synthetic comment -- @@ -88,6 +89,8 @@
private static final String PHONE_PACKAGE = "com.android.phone";
private static final String CALL_SETTINGS_CLASS_NAME =
"com.android.phone.CallFeaturesSetting";
   private static final String MSIM_CALL_SETTINGS_CLASS_NAME =
            "com.android.phone.MSimCallFeaturesSetting";

/**
* Copied from PhoneApp. See comments in Phone app for more detail.
//Synthetic comment -- @@ -1245,7 +1248,11 @@
/** Returns an Intent to launch Call Settings screen */
public static Intent getCallSettingsIntent() {
final Intent intent = new Intent(Intent.ACTION_MAIN);
        if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
            intent.setClassName(PHONE_PACKAGE, MSIM_CALL_SETTINGS_CLASS_NAME);
        } else {
            intent.setClassName(PHONE_PACKAGE, CALL_SETTINGS_CLASS_NAME);
        }
intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
return intent;
}








//Synthetic comment -- diff --git a/src/com/android/contacts/dialpad/DialpadFragment.java b/src/com/android/contacts/dialpad/DialpadFragment.java
//Synthetic comment -- index a853711..e7a5092 100644

//Synthetic comment -- @@ -1,5 +1,8 @@
/*
* Copyright (C) 2011 The Android Open Source Project
 * Copyright (c) 2011-12, Code Aurora Forum. All rights reserved.
 * Not a Contribution, Apache license notifications and license are retained
 * for attribution purposes only
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -40,6 +43,7 @@
import android.provider.Contacts.Phones;
import android.provider.Contacts.PhonesColumns;
import android.provider.Settings;
import android.telephony.MSimTelephonyManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -91,6 +95,7 @@

private static final boolean DEBUG = DialtactsActivity.DEBUG;

    private static final String SUBSCRIPTION_KEY = "subscription";
private static final String EMPTY_NUMBER = "";

/** The length of DTMF tones in milliseconds */
//Synthetic comment -- @@ -135,6 +140,7 @@
*/
private String mProhibitedPhoneNumberRegexp;

    private int mSubscription = 0;

// Last number dialed, retrieved asynchronously from the call DB
// in onCreate. This number is displayed when the user hits the
//Synthetic comment -- @@ -1546,18 +1552,47 @@
*
* @return true if voicemail is enabled and accessibly. Note that this can be false
* "temporarily" after the app boot.
     * @see MSimTelephonyManager#getVoiceMailNumber()
*/
private boolean isVoicemailAvailable() {
        boolean promptEnabled = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.MULTI_SIM_VOICE_PROMPT, 0) == 1;
        Log.d(TAG, "prompt enabled :  "+ promptEnabled);
        if (promptEnabled) {
            return hasVMNumber();
        } else {
            try {
                mSubscription = MSimTelephonyManager.getDefault().getPreferredVoiceSubscription();
                if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
                    return (MSimTelephonyManager.getDefault().
                            getVoiceMailNumber(mSubscription) != null);
                } else {
                    return (TelephonyManager.getDefault().getVoiceMailNumber() != null);
                }
            } catch (SecurityException se) {
                // Possibly no READ_PHONE_STATE privilege.
                Log.w(TAG, "SecurityException is thrown. Maybe privilege isn't sufficient.");
            }
}
return false;
}

    private boolean hasVMNumber() {
        boolean hasVMNum = false;
        int phoneCount = MSimTelephonyManager.getDefault().getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            try {
                hasVMNum = MSimTelephonyManager.getDefault().getVoiceMailNumber(i) != null;
            } catch (SecurityException se) {
                // Possibly no READ_PHONE_STATE privilege.
            }
            if (hasVMNum) {
                break;
            }
        }
        return hasVMNum;
    }

/**
* This function return true if Wait menu item can be shown
* otherwise returns false. Assumes the passed string is non-empty
//Synthetic comment -- @@ -1618,6 +1653,7 @@
private Intent newFlashIntent() {
final Intent intent = ContactsUtils.getCallIntent(EMPTY_NUMBER);
intent.putExtra(EXTRA_SEND_EMPTY_FLASH, true);
        intent.putExtra(SUBSCRIPTION_KEY, mSubscription);
return intent;
}
}








//Synthetic comment -- diff --git a/src/com/android/contacts/interactions/ImportExportDialogFragment.java b/src/com/android/contacts/interactions/ImportExportDialogFragment.java
//Synthetic comment -- index 521d2f0..66c3bef 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.telephony.MSimTelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
//Synthetic comment -- @@ -92,7 +92,15 @@
}
};

        boolean hasIccCard = false;
        for (int i = 0; i < MSimTelephonyManager.getDefault().getPhoneCount(); i++) {
            hasIccCard = MSimTelephonyManager.getDefault().hasIccCard(i);
            if (hasIccCard) {
               break;
            }
        }

        if (hasIccCard
&& res.getBoolean(R.bool.config_allow_sim_import)) {
adapter.add(R.string.import_from_sim);
}








//Synthetic comment -- diff --git a/src/com/android/contacts/util/AccountSelectionUtil.java b/src/com/android/contacts/util/AccountSelectionUtil.java
//Synthetic comment -- index 7317d21..165725a 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.MSimTelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -170,7 +171,11 @@
importIntent.putExtra("account_type", account.type);
importIntent.putExtra("data_set", account.dataSet);
}
        if (MSimTelephonyManager.getDefault().isMultiSimEnabled()) {
            importIntent.setClassName("com.android.phone", "com.android.phone.MSimContacts");
        } else {
            importIntent.setClassName("com.android.phone", "com.android.phone.SimContacts");
        }
context.startActivity(importIntent);
}








