/*Remove useless code

This line has no effect, since mState has been changed to
NfcAdapter.STATE_TURNING_OFF, but applyRouting() only works
if mState == NfcAdapter.STATE_ON.

Change-Id:I501f79e30b6b9dd224af3cbed3e5479b209980bc*/




//Synthetic comment -- diff --git a/src/com/android/nfc/NfcService.java b/src/com/android/nfc/NfcService.java
//Synthetic comment -- index 602b25d..73c14d4 100755

//Synthetic comment -- @@ -566,7 +566,6 @@
// A convenient way to stop the watchdog properly consists of
// disconnecting the tag. The polling loop shall be stopped before
// to avoid the tag being discovered again.
maybeDisconnectTarget();

mNfcDispatcher.setForegroundDispatch(null, null, null);







