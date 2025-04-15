/*Initiate SCO setup immediately after callsetup=2, before callsetup=3

According to HFP spec  v1.5, chapter 4.18, we should initiate SCO
setup immediately after sending +CIEV (callsetup = 2), and before
sending +CIEV (callsetup = 3).
This bug has caused incompabilities with existing older headsets.

Change-Id:I5a8d7cb2085b104eb74f82a5cd26b8b900f059fc*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5845397..b21c5e1 100755

//Synthetic comment -- @@ -1043,6 +1043,9 @@
break;
case DIALING:
callsetup = 2;
                // Open the SCO channel for the outgoing call.
                mCallStartTime = System.currentTimeMillis();
                audioOn();
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
//Synthetic comment -- @@ -1056,9 +1059,6 @@
break;
case ALERTING:
callsetup = 3;
mAudioPossible = true;
break;
case DISCONNECTING:







