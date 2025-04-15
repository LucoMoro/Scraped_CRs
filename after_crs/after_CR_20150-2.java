/*Fixed a crash when switching languages

When the user switches a languages on a CDMA phone the GsmServiceStateTracker
crashes. The fix is to unregister the intent receiver listening for the
locale changed intent in dispose.

Change-Id:Id9e21c4f89d2efb41ebe10afc337724d4cd78507*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6e2b262d..2732218 100644

//Synthetic comment -- @@ -246,6 +246,7 @@
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
cr.unregisterContentObserver(this.mAutoTimeZoneObserver);
        phone.getContext().unregisterReceiver(mIntentReceiver);
}

protected void finalize() {







