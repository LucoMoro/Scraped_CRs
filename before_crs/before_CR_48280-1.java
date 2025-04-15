/*Settings: Check Null Pointer Activity in InstalledAppDetails.initiateClearUserData().

If Fragment doesn't attach to one Activity
getActivity() will return null. So we need to check it.

Change-Id:I22e2300dba962b878faa0b2d16feb839554883faAuthor: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63725*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
//Synthetic comment -- index 7241fdc..43bb6391 100644

//Synthetic comment -- @@ -1007,15 +1007,18 @@
if (mClearDataObserver == null) {
mClearDataObserver = new ClearUserDataObserver();
}
        ActivityManager am = (ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        boolean res = am.clearApplicationUserData(packageName, mClearDataObserver);
        if (!res) {
            // Clearing data failed for some obscure reason. Just log error for now
            Log.i(TAG, "Couldnt clear application user data for package:"+packageName);
            showDialogInner(DLG_CANNOT_CLEAR_DATA, 0);
        } else {
            mClearDataButton.setText(R.string.recompute_size);
}
}








