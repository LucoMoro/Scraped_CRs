/*telephony: Display Supplementary Service notifications.

Display SS notifictions to user as per 3GPP TS 27.007, 7.17.

Change-Id:I35bdd38c65a1b0abb635e1235b707f5a22c2376e*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallManager.java b/telephony/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index 3dd57ee..5bfdfa8 100644

//Synthetic comment -- @@ -78,6 +78,7 @@
private static final int EVENT_SUPP_SERVICE_FAILED = 117;
private static final int EVENT_SERVICE_STATE_CHANGED = 118;
private static final int EVENT_POST_DIAL_CHARACTER = 119;

// Singleton instance
private static final CallManager INSTANCE = new CallManager();
//Synthetic comment -- @@ -158,6 +159,9 @@
protected final RegistrantList mSuppServiceFailedRegistrants
= new RegistrantList();

protected final RegistrantList mServiceStateChangedRegistrants
= new RegistrantList();

//Synthetic comment -- @@ -422,6 +426,9 @@
phone.registerForMmiComplete(mHandler, EVENT_MMI_COMPLETE, null);
phone.registerForSuppServiceFailed(mHandler, EVENT_SUPP_SERVICE_FAILED, null);
phone.registerForServiceStateChanged(mHandler, EVENT_SERVICE_STATE_CHANGED, null);

// for events supported only by GSM and CDMA phone
if (phone.getPhoneType() == Phone.PHONE_TYPE_GSM ||
//Synthetic comment -- @@ -454,6 +461,7 @@
phone.unregisterForMmiInitiate(mHandler);
phone.unregisterForMmiComplete(mHandler);
phone.unregisterForSuppServiceFailed(mHandler);
phone.unregisterForServiceStateChanged(mHandler);

// for events supported only by GSM and CDMA phone
//Synthetic comment -- @@ -1271,6 +1279,27 @@
}

/**
* Register for notifications when a sInCall VoicePrivacy is enabled
*
* @param h Handler that receives the notification message.
//Synthetic comment -- @@ -1779,6 +1808,10 @@
if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SUPP_SERVICE_FAILED)");
mSuppServiceFailedRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_SERVICE_STATE_CHANGED:
if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SERVICE_STATE_CHANGED)");
mServiceStateChangedRegistrants.notifyRegistrants((AsyncResult) msg.obj);







