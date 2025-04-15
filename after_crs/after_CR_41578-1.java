/*Calling: BT headset/Car kit wont receive audio in all cases

Audio is not routed to BT headset before call is connected.
Therefore audio is not routed to the headset in case of calling to incorrect phone number
Fix: turn on bt audio already in dialing state

Change-Id:I92705685e79cb299ad195cd7922df2d71a682cd7Author: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 4819*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5845397..9c75d92 100755

//Synthetic comment -- @@ -1043,6 +1043,8 @@
break;
case DIALING:
callsetup = 2;
                // Open the SCO channel for the outgoing call.
                audioOn();
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
//Synthetic comment -- @@ -1056,9 +1058,7 @@
break;
case ALERTING:
callsetup = 3;
mCallStartTime = System.currentTimeMillis();
mAudioPossible = true;
break;
case DISCONNECTING:







