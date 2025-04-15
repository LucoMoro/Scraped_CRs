/*Make sure the background data checkbox is updated.

Add a receiver to get background data status from
connectivity manager to make sure the background
data checkbox status is right.

Change-Id:Ia00b075a3739fc0dd32137b1b7f48a99dd161ccf*/




//Synthetic comment -- diff --git a/src/com/android/settings/ManageAccountsSettings.java b/src/com/android/settings/ManageAccountsSettings.java
//Synthetic comment -- index c9aec63..3cbcdaf 100644

//Synthetic comment -- @@ -44,6 +44,9 @@
import java.util.ArrayList;
import java.util.HashSet;

import android.content.IntentFilter;
import android.content.BroadcastReceiver;

public class ManageAccountsSettings extends AccountPreferenceBase implements View.OnClickListener {
private static final String AUTO_SYNC_CHECKBOX_KEY = "syncAutomaticallyCheckBox";
private static final String MANAGE_ACCOUNTS_CATEGORY_KEY = "manageAccountsCategory";
//Synthetic comment -- @@ -57,6 +60,16 @@
private Button mAddAccountButton;
private CheckBoxPreference mAutoSyncCheckbox;

    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED)) {
                onSyncStateUpdated();
            }
        }
    };

@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -80,11 +93,16 @@

AccountManager.get(this).addOnAccountsUpdatedListener(this, null, true);
updateAuthDescriptions();

        IntentFilter bgDataSettingChanged = new IntentFilter(
                 ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
        registerReceiver(mConnectivityReceiver, bgDataSettingChanged);
}

@Override
public void onDestroy() {
AccountManager.get(this).removeOnAccountsUpdatedListener(this);
        unregisterReceiver(mConnectivityReceiver);
super.onDestroy();
}








