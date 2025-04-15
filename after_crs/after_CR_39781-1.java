/*GlobalActions: move silent mode to the bottom

The silent mode toggle is supposed to be the last, but when
Multi-user is enabled, the user list is shown below it.

Change-Id:I889ec3e1dbeb533eeb031ed86e95606b62fba812*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index fc187ce..612ab8c 100644

//Synthetic comment -- @@ -227,11 +227,7 @@
// next: airplane mode
mItems.add(mAirplaneModeOn);

        // next: users
List<UserInfo> users = mContext.getPackageManager().getUsers();
if (users.size() > 1) {
UserInfo currentUser;
//Synthetic comment -- @@ -268,6 +264,11 @@
}
}

        // last: silent mode
        if (SHOW_SILENT_TOGGLE) {
            mItems.add(mSilentModeAction);
        }

mAdapter = new MyAdapter();

final AlertDialog.Builder ab = new AlertDialog.Builder(mContext);







