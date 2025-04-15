/*phone app:Fix for wrong call status report in CDMA

When there 2 MT calls 1 held+ 1 accepted, the call status return
for Phone is conf call. This is a wrong behavior.Verifying the previous
state will signify it is a real multy party call in CDMA.
Added the change in CLCC building function.

Change-Id:Id9fa37dd0b4a7b41318342310db58f2f05544879*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 92caffe..6866746 100644

//Synthetic comment -- @@ -1483,7 +1483,16 @@

int mpty = 0;
if (currCdmaCallState == CdmaPhoneCallState.PhoneCallState.CONF_CALL) {
            if (prevCdmaCallState == CdmaPhoneCallState.PhoneCallState.THRWAY_ACTIVE) {
                // If the current state is reached after merging two calls
                // we set the multiparty call true.
                mpty = 1;
            } else {
                // CALL_CONF state is not from merging two calls, but from
                // accepting the second call. In this case first will be on
                // hold.
                mpty = 0;
            }
} else {
mpty = 0;
}







