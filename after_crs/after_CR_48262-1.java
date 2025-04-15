/*Memory.java: unmount SD card only when it is mounted or not unmountable

Disable the Dialog box during unmounting SD card

Change-Id:Iaae1a7b970a12b4a592161b6a96cfe467615cdb8Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51872*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 825a7be..280d306 100644

//Synthetic comment -- @@ -66,6 +66,8 @@
private static final int DLG_CONFIRM_UNMOUNT = 1;
private static final int DLG_ERROR_UNMOUNT = 2;

    private static boolean doingUnmount = false;

// The mountToggle Preference that has last been clicked.
// Assumes no two successive unmount event on 2 different volumes are performed before the first
// one's preference is disabled
//Synthetic comment -- @@ -76,6 +78,7 @@
private IMountService mMountService;
private StorageManager mStorageManager;
private UsbManager mUsbManager;
    private boolean mUsbConnected = false;

private ArrayList<StorageVolumePreferenceCategory> mCategories = Lists.newArrayList();

//Synthetic comment -- @@ -248,6 +251,7 @@
String action = intent.getAction();
if (action.equals(UsbManager.ACTION_USB_STATE)) {
boolean isUsbConnected = intent.getBooleanExtra(UsbManager.USB_CONNECTED, false);
               mUsbConnected = isUsbConnected;
String usbFunction = mUsbManager.getDefaultFunction();
for (StorageVolumePreferenceCategory category : mCategories) {
category.onUsbStateChanged(isUsbConnected, usbFunction);
//Synthetic comment -- @@ -284,14 +288,25 @@
}

private void doUnmount() {
IMountService mountService = getMountService();
try {
            String state = mStorageManager.getVolumeState(sClickedMountPoint);
            Log.e(TAG, " doUnmount mountpoint is "+sClickedMountPoint+" state is "+state);
            if (!Environment.MEDIA_BAD_REMOVAL.equals(state) &&
                  !Environment.MEDIA_UNMOUNTED.equals(state) &&
                  !Environment.MEDIA_REMOVED.equals(state) &&
                  !Environment.MEDIA_SHARED.equals(state) &&
                  !Environment.MEDIA_UNMOUNTABLE.equals(state) &&
                  !mUsbConnected) {
                // Present a toast here
                doingUnmount = true;
                Toast.makeText(getActivity(), R.string.unmount_inform_text, Toast.LENGTH_SHORT).show();
                sLastClickedMountToggle.setEnabled(false);
                sLastClickedMountToggle.setTitle(getString(R.string.sd_ejecting_title));
                sLastClickedMountToggle.setSummary(getString(R.string.sd_ejecting_summary));
                mountService.unmountVolume(sClickedMountPoint, true, false);
                doingUnmount = false;
            }
} catch (RemoteException e) {
// Informative dialog to user that unmount failed.
showDialogInner(DLG_ERROR_UNMOUNT);
//Synthetic comment -- @@ -305,6 +320,7 @@

private boolean hasAppsAccessingStorage() throws RemoteException {
IMountService mountService = getMountService();
        String state = mStorageManager.getVolumeState(sClickedMountPoint);
int stUsers[] = mountService.getStorageUsers(sClickedMountPoint);
if (stUsers != null && stUsers.length > 0) {
return true;
//Synthetic comment -- @@ -328,7 +344,9 @@
try {
if (hasAppsAccessingStorage()) {
// Present dialog to user
               String state = mStorageManager.getVolumeState(sClickedMountPoint);
               if (!doingUnmount && Environment.MEDIA_MOUNTED.equals(state))
                   showDialogInner(DLG_CONFIRM_UNMOUNT);
} else {
doUnmount();
}







