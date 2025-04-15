/*Fixed a crash when switching languages

When the user switches a languages on a CDMA phone the GsmServiceStateTracker
crashes. The fix is to unregister the intent receiver listening for the
locale changed intent in dispose.

Change-Id:Id9e21c4f89d2efb41ebe10afc337724d4cd78507*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6ddb312..b2b72c1 100644

//Synthetic comment -- @@ -240,6 +240,7 @@
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
        phone.getContext().unregisterReceiver(mIntentReceiver);
}

protected void finalize() {







