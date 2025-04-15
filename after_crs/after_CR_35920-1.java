/*Connect audio SCO upon dialing call status

The audio connection shall be established in DIALING mode.
This is necessary for the busy tone to be played in the HFP device (no ALERTING
for busy call).

Signed-off-by: Patrick Combes <p-combes@ti.com>
Signed-off-by: Hugo Dupras <h-dupras@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 0c01301..05ce6da 100755

//Synthetic comment -- @@ -1041,6 +1041,7 @@
break;
case DIALING:
callsetup = 2;
                audioOn();
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was







