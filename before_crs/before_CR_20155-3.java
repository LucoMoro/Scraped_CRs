/*phone app:Fix for wrong call status report in CDMA

When there 2 Mobile teriminated calls, that are not conference
( 1 held call and 1 accepted call ), in case of CDMA network
the call status return in AT+CLCC is multiparty. Added fix to
determine if it is a real multy party call.

Change-Id:Id9fa37dd0b4a7b41318342310db58f2f05544879*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index e80557d..47c5086 100644

//Synthetic comment -- @@ -1499,7 +1499,18 @@

int mpty = 0;
if (currCdmaCallState == CdmaPhoneCallState.PhoneCallState.CONF_CALL) {
            mpty = 1;
} else {
mpty = 0;
}







