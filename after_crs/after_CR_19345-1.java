/*The phone did not reject unsupported vCalendar item

vCalendar is not supported in the phone but it was not
rejected. A blacklist was present in the code but not
used. Hence, this fix enables the blacklist functionality.

Change-Id:I4fefe0e819eb025d37c972cf7e74eabd4f6e6585*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index 278d29e..429c090 100644

//Synthetic comment -- @@ -230,10 +230,13 @@
}

// Reject policy: anything outside the "white list" plus unspecified
            // MIME Types. Also reject everything in the "black list".
if (!pre_reject
                    && (mimeType == null
                            || !Constants.mimeTypeMatches(mimeType,
                                    Constants.ACCEPTABLE_SHARE_INBOUND_TYPES)
                            || Constants.mimeTypeMatches(mimeType,
                                    Constants.UNACCEPTABLE_SHARE_INBOUND_TYPES))) {
if (D) Log.w(TAG, "mimeType is null or in unacceptable list, reject the transfer");
pre_reject = true;
obexResponse = ResponseCodes.OBEX_HTTP_UNSUPPORTED_TYPE;







