/*Add account type check for invisible sync adapters.

The invisible sync adapters array is modified to
store the sync adapters instead of their authorities,
so that "Sync now" function will only synchronize
those with the same account type as the current
account.

Change-Id:If9b0b32db9828d8671f57a0d7a2f64712a7ebabf*/
//Synthetic comment -- diff --git a/src/com/android/settings/accounts/AccountSyncSettings.java b/src/com/android/settings/accounts/AccountSyncSettings.java
//Synthetic comment -- index 82f9844..6847607 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
private Account[] mAccounts;
private ArrayList<SyncStateCheckBoxPreference> mCheckBoxes =
new ArrayList<SyncStateCheckBoxPreference>();
    private ArrayList<String> mInvisibleAdapters = Lists.newArrayList();

@Override
public Dialog onCreateDialog(final int id) {
//Synthetic comment -- @@ -321,8 +321,11 @@
}
// plus whatever the system needs to sync, e.g., invisible sync adapters
if (mAccount != null) {
            for (String authority : mInvisibleAdapters) {
                requestOrCancelSync(mAccount, authority, startSync);
}
}
}
//Synthetic comment -- @@ -449,7 +452,7 @@
} else {
// keep track of invisible sync adapters, so sync now forces
// them to sync as well.
                mInvisibleAdapters.add(sa.authority);
}
}








