/*Telephony: Look for CSS indicator for voice+data

For non-LTE, use the CSS field of VOICE_REGISTRATION_STATE to determine
if the modem supports simultaneous voice+data.

Change-Id:I546c939f325a6e3b09d563e32d45412910401422*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 0c5c342..269d7af 100644

//Synthetic comment -- @@ -492,10 +492,12 @@

@Override
public boolean isConcurrentVoiceAndDataAllowed() {
        // Note: it needs to be confirmed which CDMA network types
        // can support voice and data calls concurrently.
        // For the time-being, the return value will be false.
        return (mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE);
}

/**







