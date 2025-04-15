/*Bluetooth: handling crash when cancelling connection

This issue is very rare and occurs when many attempts
at the same time are made to reconnect to a BluetoothHeadset.
Since concurrent access to the internal mService object
is not protected by synchronized methods for an Android
design choice, it may be null when invoking methods.
This patch just handles the NullPointerException
to avoid crash and reboot.

Change-Id:If691b1d2a83c85e840f8e57467f7446f79f09492Author: Raffaele Aquilone <raffaelex.aquilone@intel.com>
Signed-off-by: Raffaele Aquilone <raffaelex.aquilone@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 46130*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothHeadset.java b/core/java/android/bluetooth/BluetoothHeadset.java
//Synthetic comment -- index 2bbf008..e7e81ce 100644

//Synthetic comment -- @@ -573,6 +573,7 @@
try {
return mService.cancelConnectThread();
} catch (RemoteException e) {Log.e(TAG, e.toString());}
} else {
Log.w(TAG, "Proxy not attached to service");
if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));







