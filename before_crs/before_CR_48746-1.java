/*UsbDeviceManager: notice Audio Accessory plug after USB configured

updateAudioSourceFunction(Send ACTION_USB_AUDIO_ACCESSORY_PLUG intent) only after USB configured
instead of just after USB connected

Change-Id:I88288e14271d38070277805675e028da4919006eAuthor: Li Gang <gang.g.li@intel.com>
Signed-off-by: Li Gang <gang.g.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57549*/
//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbDeviceManager.java b/services/java/com/android/server/usb/UsbDeviceManager.java
//Synthetic comment -- index f34a52d..ce25c4e 100644

//Synthetic comment -- @@ -607,7 +607,9 @@
}
if (mBootCompleted) {
updateUsbState();
                        updateAudioSourceFunction();
}
break;
case MSG_ENABLE_ADB:







