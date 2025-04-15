/*Minor tweaks to match Google's internal code.

This better aligns the two variants of the code to try to avoid
future merge conflicts and other surprises.

Change-Id:I703b8b99f10a9bac4128ea8ac36d0da7fd0ddc9f*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 4712ab2..991a2f5 100755

//Synthetic comment -- @@ -1175,7 +1175,7 @@
public static final int TONE_OUT_OF_SERVICE = 10;
public static final int TONE_REDIAL = 11;
public static final int TONE_OTA_CALL_END = 12;
        public static final int TONE_UNOBTAINABLE_NUMBER = 13;

// The tone volume relative to other sounds in the stream
private static final int TONE_RELATIVE_VOLUME_HIPRI = 80;








//Synthetic comment -- diff --git a/src/com/android/phone/CallWaitingCheckBoxPreference.java b/src/com/android/phone/CallWaitingCheckBoxPreference.java
//Synthetic comment -- index 0f15b4e..97fe748 100644

//Synthetic comment -- @@ -83,10 +83,13 @@
}

if (ar.exception != null) {
                if (DBG)
Log.d(LOG_TAG, "handleGetCallWaitingResponse: ar.exception=" + ar.exception);
                tcpListener.onException(CallWaitingCheckBoxPreference.this,
                        (CommandException)ar.exception);
} else if (ar.userObj instanceof Throwable) {
tcpListener.onError(CallWaitingCheckBoxPreference.this, RESPONSE_ERROR);
} else {







