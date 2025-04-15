/*unused private members removed from AccountSyncSettings
unused local variable removed from AddAccountSettings
override warnings fixed for SyncActivityTooManyDeletes

Change-Id:I7dce4e15f8a9fe39d4866647faf5a948dcd92c5e*/
//Synthetic comment -- diff --git a/src/com/android/settings/AccountSyncSettings.java b/src/com/android/settings/AccountSyncSettings.java
//Synthetic comment -- index 3200a13..d6c9920 100644

//Synthetic comment -- @@ -59,7 +59,6 @@
public class AccountSyncSettings extends AccountPreferenceBase implements OnClickListener {
private static final String ACCOUNT_KEY = "account";
private static final String TAG = "AccountSettings";
    private static final String CHANGE_PASSWORD_KEY = "changePassword";
private static final int MENU_SYNC_NOW_ID = Menu.FIRST;
private static final int MENU_SYNC_CANCEL_ID = Menu.FIRST + 1;
private static final int REALLY_REMOVE_DIALOG = 100;
//Synthetic comment -- @@ -73,7 +72,6 @@
protected View mRemoveAccountArea;
private java.text.DateFormat mDateFormat;
private java.text.DateFormat mTimeFormat;
    private Preference mAuthenticatorPreferences;
private Account mAccount;
// List of all accounts, updated when accounts are added/removed
// We need to re-scan the accounts on sync events, in case sync state changes.








//Synthetic comment -- diff --git a/src/com/android/settings/AddAccountSettings.java b/src/com/android/settings/AddAccountSettings.java
//Synthetic comment -- index 3481172..50c95a9 100644

//Synthetic comment -- @@ -107,11 +107,9 @@

private AccountManagerCallback<Bundle> mCallback = new AccountManagerCallback<Bundle>() {
public void run(AccountManagerFuture<Bundle> future) {
            boolean accountAdded = false;
try {
Bundle bundle = future.getResult();
bundle.keySet();
                accountAdded = true;
if (LDEBUG) Log.d(TAG, "account added: " + bundle);
} catch (OperationCanceledException e) {
if (LDEBUG) Log.d(TAG, "addAccount was canceled");








//Synthetic comment -- diff --git a/src/com/android/settings/SyncActivityTooManyDeletes.java b/src/com/android/settings/SyncActivityTooManyDeletes.java
//Synthetic comment -- index d122640..1ffaa0a 100644

//Synthetic comment -- @@ -107,8 +107,8 @@
setContentView(ll);
}

    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // the contants for position correspond to the items options array in onCreate()
if (position == 0) startSyncReallyDelete();
else if (position == 1) startSyncUndoDeletes();
finish();







