/*frameworks/base: Implement new CDMA RIL messages

Rename RIL_REQUEST_CDMA_SET_SUBSCRIPTION to RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE
Implement:
- RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
- RIL_UNSOL_CDMA_PRL_CHANGED
- RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE

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
//Synthetic comment -- index 5de0426..1981b43 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import android.os.Message;
import android.os.Handler;
import android.util.Log;


/**
//Synthetic comment -- @@ -154,6 +155,8 @@

RadioState getRadioState();

    void getCdmaSubscriptionSource(Message result);

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
//Synthetic comment -- index af483db..647abe6 100644

//Synthetic comment -- @@ -639,6 +639,15 @@

//***** CommandsInterface implementation

    public void getCdmaSubscriptionSource(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

@Override public void
setOnNITZTime(Handler h, int what, Object obj) {
super.setOnNITZTime(h, what, obj);
//Synthetic comment -- @@ -2223,6 +2232,7 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret = responseInts(p); break;
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2364,6 +2374,8 @@
case RIL_UNSOL_OEM_HOOK_RAW: ret = responseRaw(p); break;
case RIL_UNSOL_RINGBACK_TONE: ret = responseInts(p); break;
case RIL_UNSOL_RESEND_INCALL_MUTE: ret = responseVoid(p); break;
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: ret =  responseInts(p); break;
            case RIL_UNSOL_CDMA_PRL_CHANGED: ret =  responseInts(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2381,7 +2393,22 @@
if (RILJ_LOGD) unsljLogMore(response, newState.toString());

switchToRadioState(newState);
                break;
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                int [] ssource = (int[])ret;
                mCdmaSubscriptionSourceChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, ssource, null));
                break;
            case RIL_UNSOL_CDMA_PRL_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                int [] prlv = (int [])ret;

                mCdmaPrlChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, prlv, null));
                break;
case RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED:
if (RILJ_LOGD) unsljLog(response);

//Synthetic comment -- @@ -3276,6 +3303,7 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3320,7 +3348,9 @@
case RIL_UNSOL_OEM_HOOK_RAW: return "UNSOL_OEM_HOOK_RAW";
case RIL_UNSOL_RINGBACK_TONE: return "UNSOL_RINGBACK_TONG";
case RIL_UNSOL_RESEND_INCALL_MUTE: return "UNSOL_RESEND_INCALL_MUTE";
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: return "UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED";
            case RIL_UNSOL_CDMA_PRL_CHANGED: return "UNSOL_CDMA_PRL_CHANGED";
            default: return "<unknown response>";
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 888f721..cc71b48 100644

//Synthetic comment -- @@ -237,6 +237,7 @@
int RIL_REQUEST_SET_SMSC_ADDRESS = 101;
int RIL_REQUEST_REPORT_SMS_MEMORY_STATUS = 102;
int RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING = 103;
    int RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE = 104;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -269,4 +270,6 @@
int RIL_UNSOL_OEM_HOOK_RAW = 1028;
int RIL_UNSOL_RINGBACK_TONE = 1029;
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
    int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
    int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..d63896c 100644

//Synthetic comment -- @@ -120,6 +120,8 @@
protected static final int EVENT_ERI_FILE_LOADED                   = 36;
protected static final int EVENT_OTA_PROVISION_STATUS_CHANGE       = 37;
protected static final int EVENT_SET_RADIO_POWER_OFF               = 38;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 39;
    protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index d2a4bd8..281b99a 100755

//Synthetic comment -- @@ -177,6 +177,7 @@
cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED_CDMA, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
        cm.registerForCdmaPrlChanged(this, EVENT_CDMA_PRL_VERSION_CHANGED, null);

cm.registerForRUIMReady(this, EVENT_RUIM_READY, null);

//Synthetic comment -- @@ -209,6 +210,7 @@
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
        cm.unregisterForCdmaPrlChanged(this);
}

@Override
//Synthetic comment -- @@ -514,6 +516,21 @@
}
break;

        case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
            ar = (AsyncResult)msg.obj;
            if (ar.exception == null) {
                ints = (int[]) ar.result;
            }
            break;

        case EVENT_CDMA_PRL_VERSION_CHANGED:
            ar = (AsyncResult)msg.obj;
            if (ar.exception == null) {
                ints = (int[]) ar.result;
                mPrlVersion = Integer.toString(ints[0]);
            }
            break;

case EVENT_SET_RADIO_POWER_OFF:
synchronized(this) {
if (mPendingRadioPowerOffAfterDataOff) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java b/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index ed578c8..6d4a723 100644

//Synthetic comment -- @@ -367,6 +367,9 @@
public void setCdmaBroadcastActivation(boolean activate, Message response) {
}

    public void getCdmaSubscriptionSource(Message result) {
    }

public void exitEmergencyCallbackMode(Message response) {
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 9c72e5a..806674e 100644

//Synthetic comment -- @@ -1473,4 +1473,8 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}

    public void getCdmaSubscriptionSource(Message response) {
        unimplemented(response);
    }
}







