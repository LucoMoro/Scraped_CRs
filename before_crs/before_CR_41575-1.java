/*fix system_server crash when start/stop BT

When headset is null, the com.android.phone application
will throw an exception.
This exception will be catched by binder.
Then binder will write an error message to a Parcel object
used by bluetooth service in system_server.
Once bluetooth service get the error message,
it will throw an exception to cause system_server crash.

Change-Id:I6432ea073d43ad915742e1df994d084cba08f761Author: Pan Zhenjie <zhenjie.pan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 42733*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index db64376..21c9edf 100755

//Synthetic comment -- @@ -857,11 +857,11 @@
setState(device, BluetoothProfile.STATE_DISCONNECTING);

HeadsetBase headset = remoteHeadset.mHeadset;
                    if (remoteHeadset.mHeadsetType == BluetoothHandsfree.TYPE_HANDSFREE) {
                        headset.sendURC("+CIEV: 7,3");
                    }

if (headset != null) {
headset.disconnect();
headset = null;
}







