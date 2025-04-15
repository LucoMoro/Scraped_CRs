/*Add account type check for invisible sync adapters.

The invisible sync adapters array is modified to
store the sync adapters instead of their authorities,
so that "Sync now" function will only synchronize
those with the same account type as the current
account.

Change-Id:If9b0b32db9828d8671f57a0d7a2f64712a7ebabf*/




//Synthetic comment -- diff --git a/src/com/android/settings/AccountSyncSettings.java b/src/com/android/settings/AccountSyncSettings.java
//Synthetic comment -- index d6c9920..fd3c9fa 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
private Button mRemoveAccountButton;
private ArrayList<SyncStateCheckBoxPreference> mCheckBoxes =
new ArrayList<SyncStateCheckBoxPreference>();
    private ArrayList<SyncAdapterType> mInvisibleAdapters = Lists.newArrayList();

public void onClick(View v) {
if (v == mRemoveAccountButton) {
//Synthetic comment -- @@ -282,8 +282,11 @@
}
// plus whatever the system needs to sync, e.g., invisible sync adapters
if (mAccount != null) {
            for (SyncAdapterType syncAdapter : mInvisibleAdapters) {
                // invisible sync adapters' account type should be same as current account type
                if (syncAdapter.accountType.equals(mAccount.type)) {
                    requestOrCancelSync(mAccount, syncAdapter.authority, startSync);
                }
}
}
}
//Synthetic comment -- @@ -397,7 +400,7 @@
} else {
// keep track of invisible sync adapters, so sync now forces
// them to sync as well.
                mInvisibleAdapters.add(sa);
}
}








