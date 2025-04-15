/*Enable HDMI Audio (Stereo) in frameworks/base

Enabled HDMI Audio Intent receiving and invoking device connection
handler in AudioSystem.

Change-Id:If72f1e68ee226316c07cba3842ed4014d45eb5bbAuthor: Kumaran S <kumaran.s@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 48114*/
//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 2e153dd..14049aa 100644

//Synthetic comment -- @@ -464,6 +464,7 @@
new IntentFilter(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
intentFilter.addAction(Intent.ACTION_DOCK_EVENT);
intentFilter.addAction(Intent.ACTION_USB_AUDIO_ACCESSORY_PLUG);
intentFilter.addAction(Intent.ACTION_USB_AUDIO_DEVICE_PLUG);
intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
//Synthetic comment -- @@ -3450,6 +3451,9 @@
}
}
}
} else if (action.equals(Intent.ACTION_USB_AUDIO_ACCESSORY_PLUG) ||
action.equals(Intent.ACTION_USB_AUDIO_DEVICE_PLUG)) {
state = intent.getIntExtra("state", 0);







