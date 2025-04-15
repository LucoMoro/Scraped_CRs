/*Do as the comment says and allow "up" or "unknown" to bring up device.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PppLink.java b/telephony/java/com/android/internal/telephony/gsm/PppLink.java
//Synthetic comment -- index 43d4f1f..2004518a 100644

//Synthetic comment -- @@ -150,10 +150,10 @@

// Unfortunately, we're currently seeing operstate
// "unknown" where one might otherwise expect "up"
            if ((ArrayUtils.equals(mCheckPPPBuffer, UP_ASCII_STRING, UP_ASCII_STRING.length)
|| ArrayUtils.equals(mCheckPPPBuffer, UNKNOWN_ASCII_STRING,
                            UNKNOWN_ASCII_STRING.length)) 
                            && (dataConnection.state == State.INITING || connecting)) {

Log.i(LOG_TAG, 
"found ppp interface. Notifying GPRS connected");







