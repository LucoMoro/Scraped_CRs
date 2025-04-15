/*Telephony: Dispose service state tracker

Change-Id:I296d3f292a72d2f8181f866bd1de7637c8333c63*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 988dc6e..d4710cb 100644

//Synthetic comment -- @@ -200,6 +200,7 @@

public void dispose() {
cm.unSetOnSignalStrengthUpdate(this);
        mUiccController.unregisterForIccChanged(this);
}

public boolean getDesiredPowerState() {







