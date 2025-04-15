/*Phone: Send AT+RING when phone is ringing and headset connected

When Phone is in ringing state and headset is connected at that time,
ring or beep is not heard on the headset. The added change fixes the
issue.

Change-Id:Id737564062c86dd1b39ac01ee9459ea323c08574*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 2d51eef..e80557d 100644

//Synthetic comment -- @@ -258,6 +258,9 @@

if (isIncallAudio()) {
audioOn();
        } else if ( mCM.getFirstActiveRingingCall().isRinging()) {
            // need to update HS with RING when single ringing call exist
            mBluetoothPhoneState.ring();
}
}

//Synthetic comment -- @@ -1745,6 +1748,10 @@
sendURC("OK");  // send immediately, then initiate audio
if (isIncallAudio()) {
audioOn();
                            } else if (mCM.getFirstActiveRingingCall().isRinging()) {
                                // need to update HS with RING cmd when single
                                // ringing call exist
                                mBluetoothPhoneState.ring();
}
// only send OK once
return new AtCommandResult(AtCommandResult.UNSOLICITED);
//Synthetic comment -- @@ -1983,6 +1990,9 @@
sendURC("OK");  // send reply first, then connect audio
if (isIncallAudio()) {
audioOn();
                } else if (mCM.getFirstActiveRingingCall().isRinging()) {
                    // need to update HS with RING when single ringing call exist
                    mBluetoothPhoneState.ring();
}
// already replied
return new AtCommandResult(AtCommandResult.UNSOLICITED);







