/*Enable HDMI Audio (Stereo) in frameworks/base

Enabled HDMI Audio Intent receiving and invoking device connection
handler in AudioSystem.

Change-Id:Ie69452b549fd3bb1b7e8f570904a47f923a294f4Author: Arulselvan M <arulselvan.m@intel.com>
Signed-off-by: Arulselvan M <arulselvan.m@intel.com>
Signed-off-by: Kumaran S <kumaran.s@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 48114*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 22f699f..5b4033e 100644

//Synthetic comment -- @@ -502,6 +502,7 @@
new IntentFilter(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
intentFilter.addAction(Intent.ACTION_DOCK_EVENT);
        intentFilter.addAction(Intent.ACTION_HDMI_AUDIO_PLUG);
intentFilter.addAction(Intent.ACTION_USB_AUDIO_ACCESSORY_PLUG);
intentFilter.addAction(Intent.ACTION_USB_AUDIO_DEVICE_PLUG);
intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
//Synthetic comment -- @@ -3817,6 +3818,9 @@
}
}
}
            } else if (action.equals(Intent.ACTION_HDMI_AUDIO_PLUG)) {
                state = intent.getIntExtra("state", 0);
                handleDeviceConnection((state == 1), AudioSystem.DEVICE_OUT_AUX_DIGITAL, "");
} else if (action.equals(Intent.ACTION_USB_AUDIO_ACCESSORY_PLUG) ||
action.equals(Intent.ACTION_USB_AUDIO_DEVICE_PLUG)) {
state = intent.getIntExtra("state", 0);







