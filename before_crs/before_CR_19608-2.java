/*Limiting WPA password box to the standard.

In case of ascii the max length is 63 and in case of hex it is 64.

This fixes the below scenario:

If defining"Portable Wi-Fi hotspot" with WPA2 key containing 64
characters (or more) the hotspot will be deactivated and "Error" will
be shown. Then if you activate hotspot again and check settings, the
hotspot will be up and running and settings will say it is secured with
WPA2 eventhough it is activated as an "open" hotspot and others can
connect witout security check.

The setting will be updated first after stepping backwards in the menu
and then in again to see that the Hotspot is actually defined and active
as "open".

This could be really problematic for users that might be mislead to think
they have secured their Hotspot.

Change-Id:Idc0a1bf80f3e8b6db312e2cd9c3ba380c5fb15cf*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApDialog.java b/src/com/android/settings/wifi/WifiApDialog.java
//Synthetic comment -- index 43289d2..a70a349 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings.wifi;

import com.android.settings.R;

import android.app.AlertDialog;
import android.content.Context;
//Synthetic comment -- @@ -26,6 +27,7 @@
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
//Synthetic comment -- @@ -132,6 +134,7 @@

mSsid.addTextChangedListener(this);
mPassword.addTextChangedListener(this);
((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);
mSecurity.setOnItemSelectedListener(this);









//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiDialog.java b/src/com/android/settings/wifi/WifiDialog.java
//Synthetic comment -- index a8bf717..911c7a46 100644

//Synthetic comment -- @@ -31,7 +31,9 @@
import android.security.Credentials;
import android.security.KeyStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.View;
//Synthetic comment -- @@ -58,6 +60,47 @@
private int mSecurity;
private TextView mPassword;

private Spinner mEapMethod;
private Spinner mEapCaCert;
private Spinner mPhase2;
//Synthetic comment -- @@ -275,12 +318,23 @@
public void onItemSelected(AdapterView parent, View view, int position, long id) {
mSecurity = position;
showSecurityFields();
validate();
}

public void onNothingSelected(AdapterView parent) {
}

private void showSecurityFields() {
if (mSecurity == AccessPoint.SECURITY_NONE) {
mView.findViewById(R.id.fields).setVisibility(View.GONE);
//Synthetic comment -- @@ -291,6 +345,9 @@
if (mPassword == null) {
mPassword = (TextView) mView.findViewById(R.id.password);
mPassword.addTextChangedListener(this);
((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);

if (mAccessPoint != null && mAccessPoint.networkId != -1) {








//Synthetic comment -- diff --git a/tests/src/com/android/settings/wifi/FWifiApDialogTooLongTests.java b/tests/src/com/android/settings/wifi/FWifiApDialogTooLongTests.java
new file mode 100644
//Synthetic comment -- index 0000000..0b9e983

//Synthetic comment -- @@ -0,0 +1,99 @@







