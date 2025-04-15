/*Telephony: Use DATA_REGISTRATION_STATE for data

Use reg state from DATA_REGISTRATION_STATE when in LTE/EHRPD
and also for CDMA. Without this fix, the radiotech in voice
reg. state would get processed for CDMA and data calls get
attempted prematurely.

Change-Id:I4588b42677745b74ddd761760f170f158c906ff0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 07bc6ea..4f9159a 100644

//Synthetic comment -- @@ -259,22 +259,11 @@

@Override
protected void pollStateDone() {
        mNewRilRadioTechnology = mLteSS.getRilRadioTechnology();
        mNewDataConnectionState = mLteSS.getState();
        newSS.setRadioTechnology(mNewRilRadioTechnology);
        log("pollStateDone CdmaLTEServiceState STATE_IN_SERVICE newNetworkType = "
                + mNewDataConnectionState);

// TODO: Add proper support for LTE Only, we should be looking at
//       the preferred network mode, to know when newSS state should







