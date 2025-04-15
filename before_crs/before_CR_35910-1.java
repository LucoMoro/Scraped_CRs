/*Add delay before closing audio for HFP BT device for busy tone

The audio connection with HFP device shall be kept opened after the call
disconnect to let the busy tones be played on the BT device.

Change-Id:I6c939371b6e39e1e79a58d5e9b1551eeac56577cSigned-off-by: Hugo Dupras <h-dupras@ti.com>
Signed-off-by: Pierre Moos <p-moos@ti.com>
Signed-off-by: Patrick Combes <p-combes@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 0c01301..a97e594 100755

//Synthetic comment -- @@ -1027,6 +1027,14 @@
switch (mPhoneState) {
case IDLE:
mUserWantsAudio = true;  // out of call - reset state
audioOff();
break;
default:







