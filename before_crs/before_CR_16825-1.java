/*Telephony: Time count shouldn't restart after call gets disconnected

Avoid showing 00:01 when the call gets disconnected by the
other party. Check call state, if it disconnecting, then
just return without updating the time count.

Change-Id:Id30abaa8876ae8dbd7e865b35483dfde6b9eb202*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5078c69..3af1618 100644

//Synthetic comment -- @@ -702,6 +702,10 @@
audioOn();
mAudioPossible = true;
break;
default:
mAudioPossible = false;
}







