/*Fix the issue Memory.java UI stays on "Unmounting" when click OK button to unmount SD card

If SD card status is bad removal, it will not trigger unmount operation.

Change-Id:I334246ba7ffa2f7dc6f361a3127abafbdea3fac9Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 22998*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index cabc6e9..3c14781 100644

//Synthetic comment -- @@ -268,14 +268,18 @@
}

private void doUnmount() {
IMountService mountService = getMountService();
try {
            String state = mStorageManager.getVolumeState(mClickedMountPoint);
            Log.e(TAG, " doUnmount mountpoint is "+mClickedMountPoint+" state is "+state);
            if ( !Environment.MEDIA_BAD_REMOVAL.equals(state) ){
                // Present a toast here
                Toast.makeText(getActivity(), R.string.unmount_inform_text, Toast.LENGTH_SHORT).show();
                mLastClickedMountToggle.setEnabled(false);
                mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
                mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
                mountService.unmountVolume(mClickedMountPoint, true, false);
            }
} catch (RemoteException e) {
// Informative dialog to user that unmount failed.
showDialogInner(DLG_ERROR_UNMOUNT);







