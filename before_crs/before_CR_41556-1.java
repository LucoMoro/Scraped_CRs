/*BlueTooth: hold wakelock for 20 sec after AT command

spammy headset/handsfree units keep polling and waking
up the system constantly, its more power efficient to
prevent suspend and remain on and idle, than to keep
constantly waking up. Experimentally the wakeup periodicity
seemed to be a max of 5 seconds, but this may vary
depending on the device logic.

For this reason, hold a timed wakelock for 20 seconds
on any incoming AT command.

Change-Id:I71d2e22f2fa1de303690235b16b5fdd08a4d7ce5Author: Axel Haslam <axelx.haslam@intel.com>
Signed-off-by: Axel Haslam <axelx.haslam@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34138*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
//Synthetic comment -- index 9ef2eb5..deed5b9 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
public final class HeadsetBase {
private static final String TAG = "Bluetooth HeadsetBase";
private static final boolean DBG = false;

public static final int RFCOMM_DISCONNECTED = 1;

//Synthetic comment -- @@ -112,7 +113,7 @@
/* Process an incoming AT command line
*/
protected void handleInput(String input) {
        acquireWakeLock();
long timestamp;

synchronized(HeadsetBase.class) {
//Synthetic comment -- @@ -133,8 +134,6 @@
}

sendURC(result.toString());

        releaseWakeLock();
}

/**







