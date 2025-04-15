/*Fix window leak problems in settings.

There were window leak in the settings application.
These leak happens when an AlertDialog displays, rotate the phone,
then it would cause window leak.

Change-Id:I914897bf657933efea72eeea66076dc288098420*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index e097854..f2fc8b7 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
private EditTextPreference mMmsPort;
private ListPreference mAuthType;
private EditTextPreference mApnType;
    private AlertDialog mErrorDialog = null;

private String mCurMnc;
private String mCurMcc;
//Synthetic comment -- @@ -415,11 +416,26 @@
}

private void showErrorMessage(String message) {
        if (mErrorDialog != null) {
            mErrorDialog.setMessage(message);
            mErrorDialog.show();
        } else {
            mErrorDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.error_title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
}

private void deleteApn() {








//Synthetic comment -- diff --git a/src/com/android/settings/LanguageSettings.java b/src/com/android/settings/LanguageSettings.java
//Synthetic comment -- index 1b9f0c1..06241e1 100644

//Synthetic comment -- @@ -53,6 +53,8 @@

private String mLastInputMethodId;
private String mLastTickedInputMethodId;

    private AlertDialog mDialog = null;

static public String getInputMethodIdFromKey(String key) {
return key;
//Synthetic comment -- @@ -229,29 +231,35 @@
if (selImi == null) {
return super.onPreferenceTreeClick(preferenceScreen, preference);
}
                if (mDialog != null) {
                    mDialog.setMessage(getString(R.string.ime_security_warning,
                            selImi.getServiceInfo().applicationInfo.loadLabel(
                                    getPackageManager())));
                } else {
                    mDialog = (new AlertDialog.Builder(this))
                            .setTitle(android.R.string.dialog_alert_title)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage(getString(R.string.ime_security_warning,
                                    selImi.getServiceInfo().applicationInfo.loadLabel(
                                            getPackageManager())))
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            chkPref.setChecked(true);
                                            mLastTickedInputMethodId = id;
                                        }

                            })
                            .setNegativeButton(android.R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                            })
                            .create();
                }
                mDialog.show();
} else if (id.equals(mLastTickedInputMethodId)) {
mLastTickedInputMethodId = null;
}
//Synthetic comment -- @@ -271,4 +279,13 @@
return super.onPreferenceTreeClick(preferenceScreen, preference);
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}








//Synthetic comment -- diff --git a/src/com/android/settings/MasterClear.java b/src/com/android/settings/MasterClear.java
//Synthetic comment -- index fd4a411..f550530 100644

//Synthetic comment -- @@ -53,6 +53,8 @@
private View mFinalView;
private Button mFinalButton;

    private AlertDialog mDialog = null;

/** 
* The user has gone through the multiple confirmation, so now we go ahead
* and invoke the Checkin Service to reset the device to its factory-default
//Synthetic comment -- @@ -85,10 +87,14 @@
* the implementation of masterClear() may have returned instead
* of resetting the device.
*/
                if (mDialog != null) {
                    mDialog.show();
                } else {
                    mDialog = new AlertDialog.Builder(MasterClear.this)
                            .setMessage(getText(R.string.master_clear_failed))
                            .setPositiveButton(getText(android.R.string.ok), null)
                            .show();
                }
}
};

//Synthetic comment -- @@ -189,6 +195,16 @@
establishInitialState();
}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

/** Abandon all progress through the confirmation sequence by returning
* to the initial view any time the activity is interrupted (e.g. by
* idle timeout).








//Synthetic comment -- diff --git a/src/com/android/settings/ProxySelector.java b/src/com/android/settings/ProxySelector.java
//Synthetic comment -- index 80fe3c9..78cf8bd 100644

//Synthetic comment -- @@ -65,6 +65,7 @@
EditText    mHostnameField;
EditText    mPortField;
Button      mOKButton;
    private AlertDialog mErrorDialog = null;

// Matches blank input, ips, and domain names
private static final String HOSTNAME_REGEXP = "^$|^[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*$";
//Synthetic comment -- @@ -86,11 +87,26 @@

protected void showError(int error) {

        if (mErrorDialog != null) {
            mErrorDialog.setMessage(getResources().getString(error));
            mErrorDialog.show();
        } else {
            mErrorDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.proxy_error)
                    .setMessage(error)
                    .setPositiveButton(R.string.proxy_error_dismiss, null)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
}

void initView() {








//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index ce236fd..2f9510f 100644

//Synthetic comment -- @@ -130,6 +130,7 @@
private Button updateSmscButton;
private Button refreshSmscButton;
private Spinner preferredNetworkType;
    private AlertDialog mDialog = null;

private TelephonyManager mTelephonyManager;
private Phone phone = null;
//Synthetic comment -- @@ -987,10 +988,25 @@
disconnects.setText(sb.toString());
}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

private void displayQxdmEnableResult() {
String status = mQxdmLogEnabled ? "Start QXDM Log" : "Stop QXDM Log";

        if (mDialog != null) {
            mDialog.setMessage(status);
            mDialog.show();
        } else {
            mDialog = new AlertDialog.Builder(this).setMessage(status).show();
        }

mHandler.postDelayed(
new Runnable() {








//Synthetic comment -- diff --git a/src/com/android/settings/SettingsSafetyLegalActivity.java b/src/com/android/settings/SettingsSafetyLegalActivity.java
//Synthetic comment -- index 619dc94..59f1a68 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private WebView mWebView;

    private AlertDialog mErrorDialog = null;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -81,14 +83,30 @@
}

private void showErrorAndFinish(String url) {
        if (mErrorDialog != null) {
            mErrorDialog.setMessage(getResources()
                    .getString(R.string.settings_safetylegal_activity_unreachable, url));
            mErrorDialog.show();
        } else {
            mErrorDialog = new AlertDialog.Builder(this)
                    .setMessage(getResources()
                    .getString(R.string.settings_safetylegal_activity_unreachable, url))
                    .setTitle(R.string.settings_safetylegal_activity_title)
                    .setPositiveButton(android.R.string.ok, this)
                    .setOnCancelListener(this)
                    .setCancelable(true)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
}

@Override








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/CachedBluetoothDevice.java b/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
//Synthetic comment -- index 57bffa9..db46822 100644

//Synthetic comment -- @@ -71,6 +71,8 @@

private final LocalBluetoothManager mLocalManager;

    private AlertDialog mDialog = null;

private List<Callback> mCallbacks = new ArrayList<Callback>();

/**
//Synthetic comment -- @@ -367,12 +369,28 @@
}
};

        if (mDialog != null) {
            mDialog.setTitle(getName());
            mDialog.setMessage(message);
            mDialog.show();
        } else {
            mDialog = new AlertDialog.Builder(context)
                    .setTitle(getName())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, disconnectListener)
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
}

public void connect() {








//Synthetic comment -- diff --git a/src/com/android/settings/vpn/VpnEditor.java b/src/com/android/settings/vpn/VpnEditor.java
//Synthetic comment -- index 497f4bf..a70e910 100644

//Synthetic comment -- @@ -51,6 +51,8 @@
private boolean mAddingProfile;
private byte[] mOriginalProfileData;

    private AlertDialog mDialog = null;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -172,20 +174,37 @@
}

private void showCancellationConfirmDialog() {
        if (mDialog != null) {
            mDialog.setMessage(mAddingProfile
                    ? getString(R.string.vpn_confirm_add_profile_cancellation)
                    : getString(R.string.vpn_confirm_edit_profile_cancellation));
            mDialog.show();
        } else {
            mDialog = new AlertDialog.Builder(this)
                    .setTitle(android.R.string.dialog_alert_title)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(mAddingProfile
                            ? R.string.vpn_confirm_add_profile_cancellation
                            : R.string.vpn_confirm_edit_profile_cancellation)
                    .setPositiveButton(R.string.vpn_yes_button,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int w) {
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.vpn_mistake_button, null)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
}

private VpnProfile getProfile() {








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/AccessPointDialog.java b/src/com/android/settings/wifi/AccessPointDialog.java
//Synthetic comment -- index 4804d78..5411084 100644

//Synthetic comment -- @@ -102,6 +102,7 @@
private TextView mPasswordText;
private EditText mPasswordEdit;
private CheckBox mShowPasswordCheckBox;
    private AlertDialog mErrorDialog = null;

// Enterprise fields
private TextView mEapText;
//Synthetic comment -- @@ -542,12 +543,16 @@
(mState.security != null) &&
!mState.security.equals(AccessPointState.OPEN) &&
!mState.isEnterprise()) {
          if (mErrorDialog != null) {
              mErrorDialog.show();
          } else {
              mErrorDialog = new AlertDialog.Builder(getContext())
                      .setTitle(R.string.error_title)
                      .setIcon(android.R.drawable.ic_dialog_alert)
                      .setMessage(R.string.wifi_password_incorrect_error)
                      .setPositiveButton(android.R.string.ok, null)
                      .show();
          }
return;
}

//Synthetic comment -- @@ -556,6 +561,16 @@
}
}

    @Override
    public void dismiss() {
        super.dismiss();

        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
    }

private void handleSave() {
replaceStateWithWifiLayerInstance();








