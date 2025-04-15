/*Telephony: Display Supplementary Service notifications.

Display SS notifictions to user as per 3GPP TS 27.007, 7.17.

Change-Id:I4d313c440ef6a30d56ffbbc10cde6ab6c5966521*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b87ea50..6eabfa7 100644

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

//Synthetic comment -- @@ -441,6 +445,9 @@
phone.registerForMmiComplete(mHandler, EVENT_MMI_COMPLETE, null);
phone.registerForSuppServiceFailed(mHandler, EVENT_SUPP_SERVICE_FAILED, null);
phone.registerForServiceStateChanged(mHandler, EVENT_SERVICE_STATE_CHANGED, null);
        if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_GSM) {
            phone.registerForSuppServiceNotification(mHandler, EVENT_SUPP_SERVICE_NOTIFY, null);
        }

// for events supported only by GSM and CDMA phone
if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_GSM ||
//Synthetic comment -- @@ -473,6 +480,7 @@
phone.unregisterForMmiInitiate(mHandler);
phone.unregisterForMmiComplete(mHandler);
phone.unregisterForSuppServiceFailed(mHandler);
        phone.unregisterForSuppServiceNotification(mHandler);
phone.unregisterForServiceStateChanged(mHandler);

// for events supported only by GSM and CDMA phone
//Synthetic comment -- @@ -1290,6 +1298,27 @@
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
//Synthetic comment -- @@ -1798,6 +1827,10 @@
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







