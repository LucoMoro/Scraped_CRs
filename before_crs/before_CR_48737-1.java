/*UsbDeviceManager: turn on Screen to notify user USB connection event

In some devices, due to no USB charging, so screen will not be on
when we plug usb to PC. This patch adds this support, by holding a
wakelock for a while.

Change-Id:I770148153cab65d7015adb465d994acd90d4d9e9Author: Hao Wu <hao.wu@intel.com>
Signed-off-by: Hao Wu <hao.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 33896*/
//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbDeviceManager.java b/services/java/com/android/server/usb/UsbDeviceManager.java
//Synthetic comment -- index f34a52d..a72381f 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
//Synthetic comment -- @@ -98,6 +100,7 @@

private static final String BOOT_MODE_PROPERTY = "ro.bootmode";

private UsbHandler mHandler;
private boolean mBootCompleted;

//Synthetic comment -- @@ -107,6 +110,8 @@
private final ContentResolver mContentResolver;
// @GuardedBy("mLock")
private UsbSettingsManager mCurrentSettings;
private NotificationManager mNotificationManager;
private final boolean mHasUsbAccessory;
private boolean mUseUsbNotification;
//Synthetic comment -- @@ -152,10 +157,15 @@
mContentResolver = context.getContentResolver();
PackageManager pm = mContext.getPackageManager();
mHasUsbAccessory = pm.hasSystemFeature(PackageManager.FEATURE_USB_ACCESSORY);
initRndisAddress();

readOemUsbOverrideConfig();

// create a thread for our Handler
HandlerThread thread = new HandlerThread("UsbDeviceManager",
Process.THREAD_PRIORITY_BACKGROUND);
//Synthetic comment -- @@ -609,6 +619,11 @@
updateUsbState();
updateAudioSourceFunction();
}
break;
case MSG_ENABLE_ADB:
setAdbEnabled(msg.arg1 == 1);
//Synthetic comment -- @@ -860,6 +875,13 @@
}
}

public void dump(FileDescriptor fd, PrintWriter pw) {
if (mHandler != null) {
mHandler.dump(fd, pw);







