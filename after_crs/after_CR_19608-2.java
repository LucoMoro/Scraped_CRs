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
import com.android.settings.wifi.WifiDialog.PasswordWPAFilter;

import android.app.AlertDialog;
import android.content.Context;
//Synthetic comment -- @@ -26,6 +27,7 @@
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
//Synthetic comment -- @@ -132,6 +134,7 @@

mSsid.addTextChangedListener(this);
mPassword.addTextChangedListener(this);
        mPassword.setFilters(new InputFilter[] {new PasswordWPAFilter(true)});
((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);
mSecurity.setOnItemSelectedListener(this);









//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiDialog.java b/src/com/android/settings/wifi/WifiDialog.java
//Synthetic comment -- index a8bf717..911c7a46 100644

//Synthetic comment -- @@ -31,7 +31,9 @@
import android.security.Credentials;
import android.security.KeyStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.View;
//Synthetic comment -- @@ -58,6 +60,47 @@
private int mSecurity;
private TextView mPassword;

    /**
     * This filter will constrain password length according to the WPA standard
     * when it is enabled.
     */
    public static class PasswordWPAFilter implements InputFilter {
        private static final int WPA_ASCII_MAX = 63;
        private static final int WPA_HEX_MAX = 64;
        private boolean mIsEnabled;

        public PasswordWPAFilter(boolean enabled) {
            mIsEnabled = enabled;
        }

        public void setEnabled(boolean enabled) {
            mIsEnabled = enabled;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                int dstart, int dend) {

            if (mIsEnabled) {
                boolean isHex = source.subSequence(start, end).toString().matches("[0-9A-Fa-f]*")
                        && dest.subSequence(0, dstart).toString().matches("[0-9A-Fa-f]*")
                        && dest.subSequence(dend, dest.length()).toString().matches("[0-9A-Fa-f]*");
                int limit = isHex ? WPA_HEX_MAX : WPA_ASCII_MAX;
                int keep = limit - (dest.length() - (dend - dstart));
                if (keep <= 0) {
                    return "";
                } else if (keep >= end - start) {
                    return null; // keep original
                } else {
                    return source.subSequence(start, start + keep);
                }
            }

            return null; // keep original
        }
    };

    private PasswordWPAFilter mPasswordWPAFilter = new PasswordWPAFilter(false);

private Spinner mEapMethod;
private Spinner mEapCaCert;
private Spinner mPhase2;
//Synthetic comment -- @@ -275,12 +318,23 @@
public void onItemSelected(AdapterView parent, View view, int position, long id) {
mSecurity = position;
showSecurityFields();
        setPasswordFilter();
validate();
}

public void onNothingSelected(AdapterView parent) {
}

    private void setPasswordFilter() {
        switch (mSecurity) {
            case AccessPoint.SECURITY_PSK:
                mPasswordWPAFilter.setEnabled(true);
                break;
            default:
                mPasswordWPAFilter.setEnabled(false);
        }
    }

private void showSecurityFields() {
if (mSecurity == AccessPoint.SECURITY_NONE) {
mView.findViewById(R.id.fields).setVisibility(View.GONE);
//Synthetic comment -- @@ -291,6 +345,9 @@
if (mPassword == null) {
mPassword = (TextView) mView.findViewById(R.id.password);
mPassword.addTextChangedListener(this);
            mPassword.setFilters(new InputFilter[] {mPasswordWPAFilter});
            setPasswordFilter();

((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);

if (mAccessPoint != null && mAccessPoint.networkId != -1) {








//Synthetic comment -- diff --git a/tests/src/com/android/settings/wifi/FWifiApDialogTooLongTests.java b/tests/src/com/android/settings/wifi/FWifiApDialogTooLongTests.java
new file mode 100644
//Synthetic comment -- index 0000000..0b9e983

//Synthetic comment -- @@ -0,0 +1,99 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.wifi;

import com.android.settings.R;

import android.app.Instrumentation;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.test.ActivityInstrumentationTestCase2;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

public class FWifiApDialogTooLongTests extends ActivityInstrumentationTestCase2<WifiApSettings> {

    public FWifiApDialogTooLongTests() {
        super("com.android.settings", WifiApSettings.class);
    }

    private Instrumentation mInstrumentation;
    private WifiApSettings mActivity;
    private WifiApDialog mWifiApDialog;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mActivity = (WifiApSettings) getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        if (mActivity != null) {
            mActivity.finish();
        }
        if (mWifiApDialog != null) {
            mWifiApDialog.dismiss();
        }
        super.tearDown();
    }

    /**
     * Tests that the WPA password filter is limiting the length.
     */
    public void testUiTooLongWPAPassword() {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
        mWifiApDialog = new WifiApDialog(mActivity, mActivity, wifiConfig);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mWifiApDialog.show();
            }
        });
        mInstrumentation.waitForIdleSync();

        // Password input
        final String pwdHex64 = "123456789012345678901234567890123456789012345678901234567890abcd";
        final String pwdHex65 = "123456789012345678901234567890123456789012345678901234567890abcde";
        final String pwdAscii63 = "123456789012345678901234567890123456789012345678901234567890xyz";
        final String pwdAscii64 =
                "123456789012345678901234567890123456789012345678901234567890xyzz";
        final EditText pwdTextView = (EditText)mWifiApDialog.findViewById(R.id.password);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                pwdTextView.setText(pwdHex64); // Acceptable
                pwdTextView.setText(pwdHex65); // Should be denied
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("Password is not 64 hex tokens", 64, pwdTextView.getText().length());

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                pwdTextView.setText(pwdAscii63); // Acceptable
                pwdTextView.setText(pwdAscii64); // Should be denied
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("Password is not 63 ascii tokens", 63, pwdTextView.getText().length());
    }
}







