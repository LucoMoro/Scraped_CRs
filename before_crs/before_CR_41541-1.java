/*UsbDeviceManager: turn on Screen to notify user USB connection event

In some devices, due to no USB charging, so screen will not be on
when we plug usb to PC. This patch adds this support, by holding a
wakelock for a while.

Change-Id:I9e6b953c9f1abef980ae6c71e7d55771cd8d4bdbAuthor: Wu, Hao <hao.wu@intel.com>
Signed-off-by: Wu, Hao <hao.wu@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 33896*/
//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbDeviceManager.java b/services/java/com/android/server/usb/UsbDeviceManager.java
//Synthetic comment -- index a115345c..9984c00 100644

//Synthetic comment -- @@ -40,6 +40,8 @@
import android.os.Message;
import android.os.Parcelable;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
//Synthetic comment -- @@ -101,12 +103,15 @@

private static final String BOOT_MODE_PROPERTY = "ro.bootmode";

private UsbHandler mHandler;
private boolean mBootCompleted;

private final Context mContext;
private final ContentResolver mContentResolver;
private final UsbSettingsManager mSettingsManager;
private NotificationManager mNotificationManager;
private final boolean mHasUsbAccessory;
private boolean mUseUsbNotification;
//Synthetic comment -- @@ -152,10 +157,15 @@
mSettingsManager = settingsManager;
PackageManager pm = mContext.getPackageManager();
mHasUsbAccessory = pm.hasSystemFeature(PackageManager.FEATURE_USB_ACCESSORY);
initRndisAddress();

readOemUsbOverrideConfig();

// create a thread for our Handler
HandlerThread thread = new HandlerThread("UsbDeviceManager",
Process.THREAD_PRIORITY_BACKGROUND);
//Synthetic comment -- @@ -581,6 +591,11 @@
updateUsbState();
updateAudioSourceFunction();
}
break;
case MSG_ENABLE_ADB:
setAdbEnabled(msg.arg1 == 1);
//Synthetic comment -- @@ -802,6 +817,13 @@
return usbFunctions;
}

public void dump(FileDescriptor fd, PrintWriter pw) {
if (mHandler != null) {
mHandler.dump(fd, pw);







