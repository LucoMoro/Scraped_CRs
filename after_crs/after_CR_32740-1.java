/*Wipe the user data out in any case.

When the external storage is not mounted, the android system won't
wipe out the user data (i.e. "/data") if wipeData() is called with
the flag WIPE_EXTERNAL_STORAGE.

We think that the user data should be wiped out in any options and
also wipeData(int) method currently supports also for a external
storage. So we will also change the API reference comment.

If we should care about backward compatibility of this method behavior
with the option WIPE_EXTERNAL_STORAGE, then we would add an another
bitmask something like a ALWAYS_RESET.

Change-Id:Id7bf673c722bacc0480d32e46553b9a348513879*/




//Synthetic comment -- diff --git a/core/java/android/app/admin/DevicePolicyManager.java b/core/java/android/app/admin/DevicePolicyManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index c89396b..807bde5

//Synthetic comment -- @@ -1001,13 +1001,15 @@
/**
* Ask the user date be wiped.  This will cause the device to reboot,
* erasing all user data while next booting up.  External storage such
     * as SD cards will be also erased if the flag {@link #WIPE_EXTERNAL_STORAGE}
     * is set.
*
* <p>The calling device admin must have requested
* {@link DeviceAdminInfo#USES_POLICY_WIPE_DATA} to be able to call
* this method; if it has not, a security exception will be thrown.
*
     * @param flags Bit mask of additional options: currently 0 and
     *              {@link #WIPE_EXTERNAL_STORAGE} are supported.
*/
public void wipeData(int flags) {
if (mService != null) {








//Synthetic comment -- diff --git a/services/java/com/android/server/DevicePolicyManagerService.java b/services/java/com/android/server/DevicePolicyManagerService.java
//Synthetic comment -- index 47644de..03f73b7 100644

//Synthetic comment -- @@ -1670,6 +1670,7 @@
// Note: we can only do the wipe via ExternalStorageFormatter if the volume is not emulated.
if ((forceExtWipe || wipeExtRequested) && !Environment.isExternalStorageEmulated()) {
Intent intent = new Intent(ExternalStorageFormatter.FORMAT_AND_FACTORY_RESET);
            intent.putExtra(ExternalStorageFormatter.EXTRA_ALWAYS_RESET, true);
intent.setComponent(ExternalStorageFormatter.COMPONENT_NAME);
mWakeLock.acquire(10000);
mContext.startService(intent);







