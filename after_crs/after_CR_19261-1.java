/*Change response when AT+COPS? is received when no network

The response to the AT+COPS command is not according to
specification when no operator is selected. The response
'+COPS: 0,0,"UNKNOWN",0' or similar is incorrect and will look
bad on the display of a car kit. The response should be on the
format +COPS: <mode> if no network is found.

Change-Id:Ic4635b114e917adc571ac01723308822573a6ec1*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5078c69..34b6570 100644

//Synthetic comment -- @@ -1955,7 +1955,7 @@
"+COPS: 0,0,\"" + operatorName + "\"");
} else {
return new AtCommandResult(
                            "+COPS: 0");
}
}
@Override







