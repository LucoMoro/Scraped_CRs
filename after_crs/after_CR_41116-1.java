/*Telephony: Unregister broadcast receiver

Telephony code registers a BroadcastReceiver but does not
unregister it. This is causing Broadcast Register Receivers list
to grow large enough to cause situation with excessive JNI refs.

Change-Id:I8d2531849fd2b1282aaf5c20516b043b3bf0a2fa*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d4a1f60..78e0055 100644

//Synthetic comment -- @@ -246,6 +246,7 @@
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
cr.unregisterContentObserver(this.mAutoTimeZoneObserver);
        phone.getContext().unregisterReceiver(mIntentReceiver);
}

protected void finalize() {







