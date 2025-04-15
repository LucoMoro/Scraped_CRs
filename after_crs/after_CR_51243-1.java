/*Telephony: Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE

Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE as a way to exit from
Emergency Callback Mode

Change-Id:Ia572ac5a4d5ab38166453dcdf26fb0a667bba74e*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1465a17..3994d42 100755

//Synthetic comment -- @@ -180,6 +180,8 @@
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);
        mCM.registerForExitEmergencyCallbackMode(this, EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE,
                null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//Synthetic comment -- @@ -234,6 +236,7 @@
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);
            mCM.unregisterForExitEmergencyCallbackMode(this);
removeCallbacks(mExitEcmRunnable);

mPendingMmis.clear();







