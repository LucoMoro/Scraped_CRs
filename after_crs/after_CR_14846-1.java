/*Disconnect active call for AT+CHUP command from bluetooth

As per 3GPP spec TS 27.007, active call should be disconnected for AT+CHUP
command. Instead, ringing call is disconnected in the current implementation
for bluetooth. This is corrected to disconnect the active call.

Change-Id:I8863465c62fdfd1cd27e66f54593c21f65527154*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 13a0bec..b58a7ac 100644

//Synthetic comment -- @@ -1543,10 +1543,10 @@
@Override
public AtCommandResult handleActionCommand() {
sendURC("OK");
                if (!mForegroundCall.isIdle()) {
PhoneUtils.hangupActiveCall(mPhone);
                } else if (!mRingingCall.isIdle()) {
                    PhoneUtils.hangupRingingCall(mPhone);
} else if (!mBackgroundCall.isIdle()) {
PhoneUtils.hangupHoldingCall(mPhone);
}







