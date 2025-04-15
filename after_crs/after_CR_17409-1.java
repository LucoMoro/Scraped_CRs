/*frameworks/base: Implement new CDMA RIL messages

Rename RIL_REQUEST_CDMA_SET_SUBSCRIPTION to RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE
Implement:
- RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
- RIL_UNSOL_CDMA_PRL_CHANGED
- RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE
- RIL_REQUEST_CDMA_PRL_VERSION

Change-Id:I81e04828fb37eef74a3eff93a716b85c70ba16f8*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/BaseCommands.java b/telephony/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 815fbfb4..474e8b4 100644

//Synthetic comment -- @@ -50,6 +50,8 @@
protected RegistrantList mNetworkStateRegistrants = new RegistrantList();
protected RegistrantList mDataConnectionRegistrants = new RegistrantList();
protected RegistrantList mRadioTechnologyChangedRegistrants = new RegistrantList();
    protected RegistrantList mCdmaSubscriptionSourceChangedRegistrants = new RegistrantList();
    protected RegistrantList mCdmaPrlChangedRegistrants = new RegistrantList();
protected RegistrantList mIccStatusChangedRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOnRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOffRegistrants = new RegistrantList();
//Synthetic comment -- @@ -118,6 +120,24 @@
}
}

    public void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCdmaSubscriptionSourceChangedRegistrants.add(r);
    }

    public void unregisterForCdmaSubscriptionSourceChanged(Handler h) {
        mCdmaSubscriptionSourceChangedRegistrants.remove(h);
    }

    public void registerForCdmaPrlChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCdmaPrlChangedRegistrants.add(r);
    }

    public void unregisterForCdmaPrlChanged(Handler h) {
        mCdmaPrlChangedRegistrants.remove(h);
    }

public void registerForOn(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..430473f 100644

//Synthetic comment -- @@ -154,6 +154,9 @@

RadioState getRadioState();

    void getCdmaSubscriptionSource(Message result);
    void getCdmaPrlVersion(Message result);

/**
* Fires on any RadioState transition
* Always fires immediately as well
//Synthetic comment -- @@ -165,6 +168,12 @@
void registerForRadioStateChanged(Handler h, int what, Object obj);
void unregisterForRadioStateChanged(Handler h);

    void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj);
    void unregisterForCdmaSubscriptionSourceChanged(Handler h);

    void registerForCdmaPrlChanged(Handler h, int what, Object obj);
    void unregisterForCdmaPrlChanged(Handler h);

/**
* Fires on any transition into RadioState.isOn()
* Fires immediately if currently in that state








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 569ac5c..c318756 100644

//Synthetic comment -- @@ -633,6 +633,24 @@

//***** CommandsInterface implementation

    public void getCdmaSubscriptionSource(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    public void getCdmaPrlVersion(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_PRL_VERSION, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

@Override public void
setOnNITZTime(Handler h, int what, Object obj) {
super.setOnNITZTime(h, what, obj);
//Synthetic comment -- @@ -1350,7 +1368,7 @@
send(rrPnt);

RILRequest rrCs = RILRequest.obtain(
                                   RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE, null);
rrCs.mp.writeInt(1);
rrCs.mp.writeInt(mCdmaSubscription);
if (RILJ_LOGD) riljLog(rrCs.serialString() + "> "
//Synthetic comment -- @@ -2180,7 +2198,7 @@
case RIL_REQUEST_GET_PREFERRED_NETWORK_TYPE: ret =  responseInts(p); break;
case RIL_REQUEST_GET_NEIGHBORING_CELL_IDS: ret = responseCellList(p); break;
case RIL_REQUEST_SET_LOCATION_UPDATES: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE: ret =  responseVoid(p); break;
case RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE: ret =  responseVoid(p); break;
case RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE: ret =  responseInts(p); break;
case RIL_REQUEST_SET_TTY_MODE: ret =  responseVoid(p); break;
//Synthetic comment -- @@ -2365,6 +2383,18 @@

if (RILJ_LOGD) unsljLogMore(response, mState.toString());
break;
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                mCdmaSubscriptionSourceChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, null, null));
                break;
            case RIL_UNSOL_CDMA_PRL_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                mCdmaPrlChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, null, null));
                break;
case RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED:
if (RILJ_LOGD) unsljLog(response);

//Synthetic comment -- @@ -3232,7 +3262,7 @@
case RIL_REQUEST_GET_PREFERRED_NETWORK_TYPE: return "REQUEST_GET_PREFERRED_NETWORK_TYPE";
case RIL_REQUEST_GET_NEIGHBORING_CELL_IDS: return "REQUEST_GET_NEIGHBORING_CELL_IDS";
case RIL_REQUEST_SET_LOCATION_UPDATES: return "REQUEST_SET_LOCATION_UPDATES";
            case RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE";
case RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE: return "RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE";
case RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE: return "RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE";
case RIL_REQUEST_SET_TTY_MODE: return "RIL_REQUEST_SET_TTY_MODE";
//Synthetic comment -- @@ -3259,6 +3289,8 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
            case RIL_REQUEST_CDMA_PRL_VERSION: return "RIL_REQUEST_CDMA_PRL_VERSION";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3303,7 +3335,9 @@
case RIL_UNSOL_OEM_HOOK_RAW: return "UNSOL_OEM_HOOK_RAW";
case RIL_UNSOL_RINGBACK_TONE: return "UNSOL_RINGBACK_TONG";
case RIL_UNSOL_RESEND_INCALL_MUTE: return "UNSOL_RESEND_INCALL_MUTE";
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: return "UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED";
            case RIL_UNSOL_CDMA_PRL_CHANGED: return "UNSOL_CDMA_PRL_CHANGED";
            default: return "<unknown response>";
}
}

//Synthetic comment -- @@ -3388,7 +3422,7 @@
*/
public void setCdmaSubscription(int cdmaSubscription , Message response) {
RILRequest rr = RILRequest.obtain(
                RILConstants.RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE, response);

rr.mp.writeInt(1);
rr.mp.writeInt(cdmaSubscription);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 71a80e0..1a847e5 100644

//Synthetic comment -- @@ -209,7 +209,7 @@
int RIL_REQUEST_GET_PREFERRED_NETWORK_TYPE = 74;
int RIL_REQUEST_GET_NEIGHBORING_CELL_IDS = 75;
int RIL_REQUEST_SET_LOCATION_UPDATES = 76;
    int RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE = 77;
int RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE = 78;
int RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE = 79;
int RIL_REQUEST_SET_TTY_MODE = 80;
//Synthetic comment -- @@ -236,6 +236,8 @@
int RIL_REQUEST_SET_SMSC_ADDRESS = 101;
int RIL_REQUEST_REPORT_SMS_MEMORY_STATUS = 102;
int RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING = 103;
    int RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE = 104;
    int RIL_REQUEST_CDMA_PRL_VERSION = 105;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -268,4 +270,6 @@
int RIL_UNSOL_OEM_HOOK_RAW = 1028;
int RIL_UNSOL_RINGBACK_TONE = 1029;
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
    int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
    int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index a120f52..88662a8 100644

//Synthetic comment -- @@ -1474,4 +1474,13 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}

    public void getCdmaSubscriptionSource(Message response) {
        unimplemented(response);
    }

    public void getCdmaPrlVersion(Message response) {
        unimplemented(response);
    }

}







