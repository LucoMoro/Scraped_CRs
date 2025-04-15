/*telephony: Display Supplementary Service notifications.

Display SS notifictions to user as per 3GPP TS 27.007, 7.17.

Change-Id:I35bdd38c65a1b0abb635e1235b707f5a22c2376e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallManager.java b/telephony/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b89058f..6bab988 100644

//Synthetic comment -- @@ -78,6 +78,7 @@
private static final int EVENT_SUPP_SERVICE_FAILED = 117;
private static final int EVENT_SERVICE_STATE_CHANGED = 118;
private static final int EVENT_POST_DIAL_CHARACTER = 119;
    private static final int EVENT_SUPP_SERVICE_NOTIFY = 120;

// Singleton instance
private static final CallManager INSTANCE = new CallManager();
//Synthetic comment -- @@ -158,6 +159,9 @@
protected final RegistrantList mSuppServiceFailedRegistrants
= new RegistrantList();

    protected final RegistrantList mSuppServiceNotificationRegistrants
    = new RegistrantList();

protected final RegistrantList mServiceStateChangedRegistrants
= new RegistrantList();

//Synthetic comment -- @@ -421,6 +425,9 @@
phone.registerForMmiComplete(mHandler, EVENT_MMI_COMPLETE, null);
phone.registerForSuppServiceFailed(mHandler, EVENT_SUPP_SERVICE_FAILED, null);
phone.registerForServiceStateChanged(mHandler, EVENT_SERVICE_STATE_CHANGED, null);
        if (phone.getPhoneType() == Phone.PHONE_TYPE_GSM) {
            phone.registerForSuppServiceNotification(mHandler, EVENT_SUPP_SERVICE_NOTIFY, null);
        }

// for events supported only by GSM and CDMA phone
if (phone.getPhoneType() == Phone.PHONE_TYPE_GSM ||
//Synthetic comment -- @@ -453,6 +460,7 @@
phone.unregisterForMmiInitiate(mHandler);
phone.unregisterForMmiComplete(mHandler);
phone.unregisterForSuppServiceFailed(mHandler);
        phone.unregisterForSuppServiceNotification(mHandler);
phone.unregisterForServiceStateChanged(mHandler);

// for events supported only by GSM and CDMA phone
//Synthetic comment -- @@ -1260,6 +1268,27 @@
}

/**
     * Register for supplementary service notifications.
     * Message.obj will contain an AsyncResult.
     *
     * @param h Handler that receives the notification message.
     * @param what User-defined message code.
     * @param obj User object.
     */
    public void registerForSuppServiceNotification(Handler h, int what, Object obj){
        mSuppServiceNotificationRegistrants.addUnique(h, what, obj);
    }

    /**
     * Unregister for supplementary service notifications.
     *
     * @param h Handler to be removed from the registrant list.
     */
    public void unregisterForSuppServiceNotification(Handler h){
        mSuppServiceNotificationRegistrants.remove(h);
    }

    /**
* Register for notifications when a sInCall VoicePrivacy is enabled
*
* @param h Handler that receives the notification message.
//Synthetic comment -- @@ -1768,6 +1797,10 @@
if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SUPP_SERVICE_FAILED)");
mSuppServiceFailedRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
                case EVENT_SUPP_SERVICE_NOTIFY:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SUPP_SERVICE_NOTIFICATION)");
                    mSuppServiceNotificationRegistrants.notifyRegistrants((AsyncResult) msg.obj);
                    break;
case EVENT_SERVICE_STATE_CHANGED:
if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SERVICE_STATE_CHANGED)");
mServiceStateChangedRegistrants.notifyRegistrants((AsyncResult) msg.obj);







