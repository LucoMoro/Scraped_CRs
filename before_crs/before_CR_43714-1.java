/*Telephony: Dispose service state tracker

Change-Id:I296d3f292a72d2f8181f866bd1de7637c8333c63*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 090e38d..5b13792 100755

//Synthetic comment -- @@ -182,6 +182,10 @@
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
}

public boolean getDesiredPowerState() {
return mDesiredPowerState;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 03a71d8..7099dd4 100755

//Synthetic comment -- @@ -216,6 +216,7 @@
cr.unregisterContentObserver(mAutoTimeZoneObserver);
mCdmaSSM.dispose(this);
cm.unregisterForCdmaPrlChanged(this);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..3533126 100755

//Synthetic comment -- @@ -251,6 +251,7 @@
cr.unregisterContentObserver(this.mAutoTimeObserver);
cr.unregisterContentObserver(this.mAutoTimeZoneObserver);
phone.getContext().unregisterReceiver(mIntentReceiver);
}

protected void finalize() {







