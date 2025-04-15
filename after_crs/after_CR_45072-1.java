/*Telephony: STK CC feature implementation.

Support to display the current request is changed by STKCC.
Support to display alpha messgae from STKCC.
Support to display informatiom from new SS message.

Change-Id:I22ca16dab3d578a264a04770cc8d7d006390bbda*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 1b54656..6478cdb 100644

//Synthetic comment -- @@ -89,6 +89,8 @@
protected Registrant mRingRegistrant;
protected Registrant mRestrictedStateRegistrant;
protected Registrant mGsmBroadcastSmsRegistrant;
    protected Registrant mCatCcAlphaRegistrant;
    protected Registrant mSSRegistrant;

// Preferred network type received from PhoneFactory.
// This is used when establishing a connection to the
//Synthetic comment -- @@ -384,6 +386,22 @@
mRingRegistrant.clear();
}

    public void setOnSS(Handler h, int what, Object obj) {
        mSSRegistrant = new Registrant (h, what, obj);
    }

    public void unSetOnSS(Handler h) {
        mSSRegistrant.clear();
    }

    public void setOnCatCcAlphaNotify(Handler h, int what, Object obj) {
        mCatCcAlphaRegistrant = new Registrant (h, what, obj);
    }

    public void unSetOnCatCcAlphaNotify(Handler h) {
        mCatCcAlphaRegistrant.clear();
    }

public void registerForInCallVoicePrivacyOn(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mVoicePrivacyOnRegistrants.add(r);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandException.java b/src/java/com/android/internal/telephony/CommandException.java
//Synthetic comment -- index 94c544e..b6bf1a3 100644

//Synthetic comment -- @@ -42,6 +42,15 @@
MODE_NOT_SUPPORTED,
FDN_CHECK_FAILURE,
ILLEGAL_SIM_OR_ME,
        DIAL_MODIFIED_TO_USSD,
        DIAL_MODIFIED_TO_SS,
        DIAL_MODIFIED_TO_DIAL,
        USSD_MODIFIED_TO_DIAL,
        USSD_MODIFIED_TO_SS,
        USSD_MODIFIED_TO_USSD,
        SS_MODIFIED_TO_DIAL,
        SS_MODIFIED_TO_USSD,
        SS_MODIFIED_TO_SS
}

public CommandException(Error e) {
//Synthetic comment -- @@ -83,6 +92,24 @@
return new CommandException(Error.FDN_CHECK_FAILURE);
case RILConstants.ILLEGAL_SIM_OR_ME:
return new CommandException(Error.ILLEGAL_SIM_OR_ME);
            case RILConstants.DIAL_MODIFIED_TO_USSD:
                return new CommandException(Error.DIAL_MODIFIED_TO_USSD);
            case RILConstants.DIAL_MODIFIED_TO_SS:
                return new CommandException(Error.DIAL_MODIFIED_TO_SS);
            case RILConstants.DIAL_MODIFIED_TO_DIAL:
                return new CommandException(Error.DIAL_MODIFIED_TO_DIAL);
            case RILConstants.USSD_MODIFIED_TO_DIAL:
                return new CommandException(Error.USSD_MODIFIED_TO_DIAL);
            case RILConstants.USSD_MODIFIED_TO_SS:
                return new CommandException(Error.USSD_MODIFIED_TO_SS);
            case RILConstants.USSD_MODIFIED_TO_USSD:
                return new CommandException(Error.USSD_MODIFIED_TO_USSD);
            case RILConstants.SS_MODIFIED_TO_DIAL:
                return new CommandException(Error.SS_MODIFIED_TO_DIAL);
            case RILConstants.SS_MODIFIED_TO_USSD:
                return new CommandException(Error.SS_MODIFIED_TO_USSD);
            case RILConstants.SS_MODIFIED_TO_SS:
                return new CommandException(Error.SS_MODIFIED_TO_SS);
default:
Log.e("GSM", "Unrecognized RIL errno " + ril_errno);
return new CommandException(Error.INVALID_RESPONSE);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..d90a3b8 100644

//Synthetic comment -- @@ -384,6 +384,28 @@
//void unSetSuppServiceNotifications(Handler h);

/**
     * Sets the handler for Alpha Notification during STK Call Control.
     * Unlike the register* methods, there's only one notification handler
     *
     * @param h Handler for notification message.
     * @param what User-defined message code.
     * @param obj User object.
     */
    void setOnCatCcAlphaNotify(Handler h, int what, Object obj);
    void unSetOnCatCcAlphaNotify(Handler h);

    /**
     * Sets the handler for notifying SS Data during STK Call Control.
     * Unlike the register* methods, there's only one notification handler
     *
     * @param h Handler for notification message.
     * @param what User-defined message code.
     * @param obj User object.
     */
    void setOnSS(Handler h, int what, Object obj);
    void unSetOnSS(Handler h);

    /**
* Sets the handler for Event Notifications for CDMA Display Info.
* Unlike the register* methods, there's only one notification handler
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Connection.java b/src/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 554d974..dab84e4 100644

//Synthetic comment -- @@ -56,6 +56,9 @@
CS_RESTRICTED_NORMAL,           /* call was blocked by restricted normal voice access */
CS_RESTRICTED_EMERGENCY,        /* call was blocked by restricted emergency voice access */
UNOBTAINABLE_NUMBER,            /* Unassigned number (3GPP TS 24.008 table 10.5.123) */
        DIAL_MODIFIED_TO_USSD,          /* Stk Call Control modified DIAL request to USSD request */
        DIAL_MODIFIED_TO_SS,            /* Stk Call Control modified DIAL request to SS request */
        DIAL_MODIFIED_TO_DIAL,          /* Stk Call Control modified DIAL request to DIAL with modified data */
CDMA_LOCKED_UNTIL_POWER_CYCLE,  /* MS is locked until next power cycle */
CDMA_DROP,
CDMA_INTERCEPT,                 /* INTERCEPT order received, MS state idle entered */








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1f69f7a..c3dbe17 100755

//Synthetic comment -- @@ -114,6 +114,7 @@
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;
protected static final int EVENT_ICC_CHANGED                    = 31;
    protected static final int EVENT_SS                             = 34;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 6131792..e7fb18b 100755

//Synthetic comment -- @@ -50,6 +50,7 @@
import android.util.Log;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccCardApplicationStatus;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
//Synthetic comment -- @@ -2478,6 +2479,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;
            case RIL_UNSOL_ON_SS: ret =  responseSSData(p); break;
            case RIL_UNSOL_STK_CC_ALPHA_NOTIFY: ret =  responseString(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2807,6 +2810,24 @@
}
break;

            case RIL_UNSOL_ON_SS:
                if (RILJ_LOGD) unsljLogRet(response, ret);

                if (mSSRegistrant != null) {
                    mSSRegistrant.notifyRegistrant(
                                        new AsyncResult (null, ret, null));
                }
                break;

            case RIL_UNSOL_STK_CC_ALPHA_NOTIFY:
                if (RILJ_LOGD) unsljLogRet(response, ret);

                if (mCatCcAlphaRegistrant != null) {
                    mCatCcAlphaRegistrant.notifyRegistrant(
                                        new AsyncResult (null, ret, null));
                }
                break;

case RIL_UNSOl_CDMA_PRL_CHANGED:
if (RILJ_LOGD) unsljLogRet(response, ret);

//Synthetic comment -- @@ -3619,6 +3640,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            case RIL_UNSOL_ON_SS: return "UNSOL_ON_SS";
            case RIL_UNSOL_STK_CC_ALPHA_NOTIFY: return "UNSOL_STK_CC_ALPHA_NOTIFY";
default: return "<unknown reponse>";
}
}
//Synthetic comment -- @@ -3647,6 +3670,45 @@
riljLogv("[UNSL]< " + responseToString(response) + " " + retToString(response, ret));
}

    private Object
    responseSSData(Parcel p) {
        int num;
        SsData ssData = new SsData();

        ssData.serviceType = ssData.ServiceTypeFromRILInt(p.readInt());
        ssData.requestType = ssData.RequestTypeFromRILInt(p.readInt());
        ssData.teleserviceType = ssData.TeleserviceTypeFromRILInt(p.readInt());
        ssData.serviceClass = p.readInt(); // This is service class sent in the SS request.
        ssData.result = p.readInt(); // This is the result of the SS request.
        num = p.readInt();

        if (ssData.serviceType.isTypeCF() &&
            ssData.requestType.isTypeInterrogation()) {
            ssData.cfInfo = new CallForwardInfo[num];

            for (int i = 0; i < num; i++) {
                ssData.cfInfo[i] = new CallForwardInfo();

                ssData.cfInfo[i].status = p.readInt();
                ssData.cfInfo[i].reason = p.readInt();
                ssData.cfInfo[i].serviceClass = p.readInt();
                ssData.cfInfo[i].toa = p.readInt();
                ssData.cfInfo[i].number = p.readString();
                ssData.cfInfo[i].timeSeconds = p.readInt();

                riljLog("[SS Data] CF Info " + i + " : " +  ssData.cfInfo[i]);
            }
        } else {
            ssData.ssInfo = new int[num];
            for (int i = 0; i < num; i++) {
                ssData.ssInfo[i] = p.readInt();
                riljLog("[SS Data] SS Info " + i + " : " +  ssData.ssInfo[i]);
            }
        }

        return ssData;
    }


// ***** Methods for CDMA support
public void








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 299e140..a1edd20 100644

//Synthetic comment -- @@ -25,12 +25,17 @@

/*
* Intent's actions which are broadcasted by the Telephony once a new CAT
     * proactive command, session end, ALPHA during STK CC arrive.
*/
public static final String CAT_CMD_ACTION =
"android.intent.action.stk.command";
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";
    public static final String CAT_ALPHA_NOTIFY_ACTION =
                                    "android.intent.action.stk.alpha_notify";

    //This is used to send ALPHA string from card to STK App.
    public static final String ALPHA_STRING = "alpha_string";

/*
* Callback function from app to telephony to pass a result code and user's








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..82525af 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
static final int MSG_ID_REFRESH                  = 5;
static final int MSG_ID_RESPONSE                 = 6;
static final int MSG_ID_SIM_READY                = 7;
    static final int MSG_ID_ALPHA_NOTIFY             = 8;

static final int MSG_ID_RIL_MSG_DECODED          = 10;

//Synthetic comment -- @@ -122,7 +123,7 @@
mCmdIf.setOnCatEvent(this, MSG_ID_EVENT_NOTIFY, null);
mCmdIf.setOnCatCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);
        mCmdIf.setOnCatCcAlphaNotify(this, MSG_ID_ALPHA_NOTIFY, null);
mIccRecords = ir;
mUiccApplication = ca;

//Synthetic comment -- @@ -142,6 +143,7 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);
        mCmdIf.unSetOnCatCcAlphaNotify(this);

this.removeCallbacksAndMessages(null);
}
//Synthetic comment -- @@ -654,6 +656,24 @@
CatLog.d(this, "SIM ready. Reporting STK service running now...");
mCmdIf.reportStkServiceIsRunning(null);
break;
        case MSG_ID_ALPHA_NOTIFY:
            CatLog.d(this, "Received STK CC Alpha message from card");
            if (msg.obj != null) {
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar != null && ar.result != null) {
                    String alphaString = (String)ar.result;
                    CatLog.d(this, "Broadcasting STK Alpha message from card: " + alphaString);
                    Intent intent = new Intent(AppInterface.CAT_ALPHA_NOTIFY_ACTION);
                    intent.putExtra(AppInterface.ALPHA_STRING, alphaString);
                    //intent.putExtra("SLOT_ID", mSlotId); TODO Add for DSDS suresh
                    mContext.sendBroadcast(intent);
                } else {
                    CatLog.d(this, "STK Alpha message: ar.result is null");
                }
            } else {
                CatLog.d(this, "STK Alpha message: msg.obj is null");
            }
            break;
default:
throw new AssertionError("Unrecognized CAT command: " + msg.what);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CallFailCause.java b/src/java/com/android/internal/telephony/cdma/CallFailCause.java
//Synthetic comment -- index ad6c23c..03b1820 100644

//Synthetic comment -- @@ -40,6 +40,11 @@
static final int CALL_BARRED                    = 240;
static final int FDN_BLOCKED                    = 241;

    // Stk Call Control
    static final int DIAL_MODIFIED_TO_USSD = 244;
    static final int DIAL_MODIFIED_TO_SS = 245;
    static final int DIAL_MODIFIED_TO_DIAL = 246;

static final int CDMA_LOCKED_UNTIL_POWER_CYCLE  = 1000;
static final int CDMA_DROP                      = 1001;
static final int CDMA_INTERCEPT                 = 1002;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 17eecbd..cf4b6dc 100755

//Synthetic comment -- @@ -393,6 +393,12 @@
return DisconnectCause.CALL_BARRED;
case CallFailCause.FDN_BLOCKED:
return DisconnectCause.FDN_BLOCKED;
            case CallFailCause.DIAL_MODIFIED_TO_USSD:
                return DisconnectCause.DIAL_MODIFIED_TO_USSD;
            case CallFailCause.DIAL_MODIFIED_TO_SS:
                return DisconnectCause.DIAL_MODIFIED_TO_SS;
            case CallFailCause.DIAL_MODIFIED_TO_DIAL:
                return DisconnectCause.DIAL_MODIFIED_TO_DIAL;
case CallFailCause.CDMA_LOCKED_UNTIL_POWER_CYCLE:
return DisconnectCause.CDMA_LOCKED_UNTIL_POWER_CYCLE;
case CallFailCause.CDMA_DROP:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/CallFailCause.java b/src/java/com/android/internal/telephony/gsm/CallFailCause.java
//Synthetic comment -- index af2ad48..994dfa0 100644

//Synthetic comment -- @@ -49,5 +49,11 @@
static final int ACM_LIMIT_EXCEEDED = 68;
static final int CALL_BARRED        = 240;
static final int FDN_BLOCKED        = 241;

    // Stk Call Control
    static final int DIAL_MODIFIED_TO_USSD = 244;
    static final int DIAL_MODIFIED_TO_SS = 245;
    static final int DIAL_MODIFIED_TO_DIAL = 246;

static final int ERROR_UNSPECIFIED = 0xffff;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..adfae45 100644

//Synthetic comment -- @@ -160,6 +160,7 @@
mCM.setOnUSSD(this, EVENT_USSD, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);
        mCM.setOnSS(this, EVENT_SS, null);

if (false) {
try {
//Synthetic comment -- @@ -213,6 +214,7 @@
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnUSSD(this);
mCM.unSetOnSuppServiceNotification(this);
            mCM.unSetOnSS(this);

mPendingMMIs.clear();

//Synthetic comment -- @@ -1087,7 +1089,7 @@
* The exception is cancellation of an incoming USSD-REQUEST, which is
* not on the list.
*/
        if (mPendingMMIs.remove(mmi) || mmi.isUssdRequest() || mmi.isSsInfo()) {
mMmiCompleteRegistrants.notifyRegistrants(
new AsyncResult(null, mmi, null));
}
//Synthetic comment -- @@ -1332,6 +1334,16 @@
}
break;

            case EVENT_SS:
                ar = (AsyncResult)msg.obj;
                Log.d(LOG_TAG, "Event EVENT_SS received");
                // SS data is already being handled through MMI codes.
                // So, this result if processed as MMI response would help
                // in re-using the existing functionality.
                GsmMmiCode mmi = new GsmMmiCode(this, mUiccApplication.get());
                mmi.processSsData(ar);
                break;

default:
super.handleMessage(msg);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 59aa12a..5fd8deb 100644

//Synthetic comment -- @@ -368,6 +368,15 @@
case CallFailCause.UNOBTAINABLE_NUMBER:
return DisconnectCause.UNOBTAINABLE_NUMBER;

            case CallFailCause.DIAL_MODIFIED_TO_USSD:
                return DisconnectCause.DIAL_MODIFIED_TO_USSD;

            case CallFailCause.DIAL_MODIFIED_TO_SS:
                return DisconnectCause.DIAL_MODIFIED_TO_SS;

            case CallFailCause.DIAL_MODIFIED_TO_DIAL:
                return DisconnectCause.DIAL_MODIFIED_TO_DIAL;

case CallFailCause.ERROR_UNSPECIFIED:
case CallFailCause.NORMAL_CLEARING:
default:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..fd08224 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.util.Log;

import static com.android.internal.telephony.CommandsInterface.*;
import com.android.internal.telephony.gsm.SsData;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -126,6 +127,9 @@

private boolean isUssdRequest;

    private boolean isSsInfo = false;


State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -246,6 +250,136 @@
return ret;
}

    /** Process SS Data */
    void
    processSsData(AsyncResult data) {
        Log.d(LOG_TAG, "In processSsData");

        isSsInfo = true;
        try {
            SsData ssData = (SsData)data.result;
            parseSsData(ssData);
        } catch (ClassCastException ex) {
            Log.e(LOG_TAG, "Exception in parsing SS Data : " + ex);
        } catch (NullPointerException ex) {
            Log.e(LOG_TAG, "Exception in parsing SS Data : " + ex);
        }
    }

    void parseSsData(SsData ssData) {
        CommandException ex;

        ex = CommandException.fromRilErrno(ssData.result);
        sc = getScStringFromScType(ssData.serviceType);
        action = getActionStringFromReqType(ssData.requestType);
        Log.d(LOG_TAG, "parseSsData sc = " + sc + ", action = " + action + ", ex = " + ex);

        switch (ssData.requestType) {
            case SS_ACTIVATION:
            case SS_DEACTIVATION:
            case SS_REGISTRATION:
            case SS_ERASURE:
                if ((ssData.result == RILConstants.SUCCESS) &&
                      ssData.serviceType.isTypeUnConditional()) {
                    /*
                     * When ServiceType is SS_CFU/SS_CF_ALL and RequestType is activate/register
                     * and ServiceClass is Voice/None, set IccRecords.setVoiceCallForwardingFlag.
                     * Only CF status can be set here since number is not available.
                     */
                    boolean cffEnabled = ((ssData.requestType == SsData.RequestType.SS_ACTIVATION ||
                            ssData.requestType == SsData.RequestType.SS_REGISTRATION) &&
                            isServiceClassVoiceorNone(ssData.serviceClass));

                    Log.d(LOG_TAG, "setVoiceCallForwardingFlag cffEnabled: " + cffEnabled);
                    if (phone.mIccRecords != null) {
                        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
                        Log.d(LOG_TAG, "setVoiceCallForwardingFlag done from SS Info.");
                    } else {
                        Log.d(LOG_TAG, "setVoiceCallForwardingFlag aborted. sim records is null.");
                    }
                }
                onSetComplete(new AsyncResult(null, ssData.cfInfo, ex));
                break;
            case SS_INTERROGATION:
                if (ssData.serviceType.isTypeClir()) {
                    Log.d(LOG_TAG, "CLIR INTERROGATION");
                    onGetClirComplete(new AsyncResult(null, ssData.ssInfo, ex));
                } else if (ssData.serviceType.isTypeCF()) {
                    Log.d(LOG_TAG, "CALL FORWARD INTERROGATION");
                    onQueryCfComplete(new AsyncResult(null, ssData.cfInfo, ex));
                } else {
                    onQueryComplete(new AsyncResult(null, ssData.ssInfo, ex));
                }
                break;
            default:
                Log.e(LOG_TAG, "Invaid requestType in SSData : " + ssData.requestType);
                break;
        }
    }

    private String getScStringFromScType(SsData.ServiceType sType) {
        switch (sType) {
            case SS_CFU:
                return SC_CFU;
            case SS_CF_BUSY:
                return SC_CFB;
            case SS_CF_NO_REPLY:
                return SC_CFNRy;
            case SS_CF_NOT_REACHABLE:
                return SC_CFNR;
            case SS_CF_ALL:
                return SC_CF_All;
            case SS_CF_ALL_CONDITIONAL:
                return SC_CF_All_Conditional;
            case SS_CLIP:
                return SC_CLIP;
            case SS_CLIR:
                return SC_CLIR;
            case SS_WAIT:
                return SC_WAIT;
            case SS_BAOC:
                return SC_BAOC;
            case SS_BAOIC:
                return SC_BAOIC;
            case SS_BAOIC_EXC_HOME:
                return SC_BAOICxH;
            case SS_BAIC:
                return SC_BAIC;
            case SS_BAIC_ROAMING:
                return SC_BAICr;
            case SS_ALL_BARRING:
                return SC_BA_ALL;
            case SS_OUTGOING_BARRING:
                return SC_BA_MO;
            case SS_INCOMING_BARRING:
                return SC_BA_MT;
        }

        return "";
    }

    private String getActionStringFromReqType(SsData.RequestType rType) {
        switch (rType) {
            case SS_ACTIVATION:
                return ACTION_ACTIVATE;
            case SS_DEACTIVATION:
                return ACTION_DEACTIVATE;
            case SS_INTERROGATION:
                return ACTION_INTERROGATE;
            case SS_REGISTRATION:
                return ACTION_REGISTER;
            case SS_ERASURE:
                return ACTION_ERASURE;
        }

        return "";
    }

    private boolean isServiceClassVoiceorNone(int serviceClass) {
        return (((serviceClass & CommandsInterface.SERVICE_CLASS_VOICE) != 0) ||
                (serviceClass == CommandsInterface.SERVICE_CLASS_NONE));
    }

//***** Private Class methods

/** make empty strings be null.
//Synthetic comment -- @@ -622,6 +756,10 @@
return isUssdRequest;
}

    public boolean isSsInfo() {
        return isSsInfo;
    }

/** Process a MMI code or short code...anything that isn't a dialing number */
void
processCode () {
//Synthetic comment -- @@ -949,6 +1087,24 @@
if (err == CommandException.Error.FDN_CHECK_FAILURE) {
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
return context.getText(com.android.internal.R.string.mmiFdnError);
            } else if (err == CommandException.Error.USSD_MODIFIED_TO_DIAL) {
                Log.i(LOG_TAG, "USSD_MODIFIED_TO_DIAL");
                return context.getText(com.android.internal.R.string.stkCcUssdToDial);
            } else if (err == CommandException.Error.USSD_MODIFIED_TO_SS) {
                Log.i(LOG_TAG, "USSD_MODIFIED_TO_SS");
                return context.getText(com.android.internal.R.string.stkCcUssdToSs);
            } else if (err == CommandException.Error.USSD_MODIFIED_TO_USSD) {
                Log.i(LOG_TAG, "USSD_MODIFIED_TO_USSD");
                return context.getText(com.android.internal.R.string.stkCcUssdToUssd);
            } else if (err == CommandException.Error.SS_MODIFIED_TO_DIAL) {
                Log.i(LOG_TAG, "SS_MODIFIED_TO_DIAL");
                return context.getText(com.android.internal.R.string.stkCcSsToDial);
            } else if (err == CommandException.Error.SS_MODIFIED_TO_USSD) {
                Log.i(LOG_TAG, "SS_MODIFIED_TO_USSD");
                return context.getText(com.android.internal.R.string.stkCcSsToUssd);
            } else if (err == CommandException.Error.SS_MODIFIED_TO_SS) {
                Log.i(LOG_TAG, "SS_MODIFIED_TO_SS");
                return context.getText(com.android.internal.R.string.stkCcSsToSs);
}
}

//Synthetic comment -- @@ -1011,8 +1167,7 @@
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
} else {
                    sb.append(getErrorMessage(ar));
}
} else {
sb.append(context.getText(








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SsData.java b/src/java/com/android/internal/telephony/gsm/SsData.java
new file mode 100644
//Synthetic comment -- index 0000000..28ab408

//Synthetic comment -- @@ -0,0 +1,190 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2011-12, Code Aurora Forum. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony.gsm;

import com.android.internal.telephony.CallForwardInfo;

import java.util.ArrayList;

/**
 * See also RIL_StkCcUnsolSsResponse in include/telephony/ril.h
 *
 * {@hide}
 */
public class SsData {
    public enum ServiceType {
        SS_CFU,
        SS_CF_BUSY,
        SS_CF_NO_REPLY,
        SS_CF_NOT_REACHABLE,
        SS_CF_ALL,
        SS_CF_ALL_CONDITIONAL,
        SS_CLIP,
        SS_CLIR,
        SS_COLP,
        SS_COLR,
        SS_WAIT,
        SS_BAOC,
        SS_BAOIC,
        SS_BAOIC_EXC_HOME,
        SS_BAIC,
        SS_BAIC_ROAMING,
        SS_ALL_BARRING,
        SS_OUTGOING_BARRING,
        SS_INCOMING_BARRING;

        public boolean isTypeCF() {
            return (this == SS_CFU || this == SS_CF_BUSY || this == SS_CF_NO_REPLY ||
                  this == SS_CF_NOT_REACHABLE || this == SS_CF_ALL || this == SS_CF_ALL_CONDITIONAL);
        }

        public boolean isTypeUnConditional() {
            return (this == SS_CFU || this == SS_CF_ALL);
        }

        public boolean isTypeCW() {
            return (this == SS_WAIT);
        }

        public boolean isTypeClip() {
            return (this == SS_CLIP);
        }

        public boolean isTypeClir() {
            return (this == SS_CLIR);
        }

        public boolean isTypeBarring() {
            return (this == SS_BAOC || this == SS_BAOIC || this == SS_BAOIC_EXC_HOME ||
                  this == SS_BAIC || this == SS_BAIC_ROAMING || this == SS_ALL_BARRING ||
                  this == SS_OUTGOING_BARRING || this == SS_INCOMING_BARRING);
        }
    };

    public enum RequestType {
        SS_ACTIVATION,
        SS_DEACTIVATION,
        SS_INTERROGATION,
        SS_REGISTRATION,
        SS_ERASURE;

        public boolean isTypeInterrogation() {
            return (this == SS_INTERROGATION);
        }
    };

    public enum TeleserviceType {
        SS_ALL_TELE_AND_BEARER_SERVICES,
        SS_ALL_TELESEVICES,
        SS_TELEPHONY,
        SS_ALL_DATA_TELESERVICES,
        SS_SMS_SERVICES,
        SS_ALL_TELESERVICES_EXCEPT_SMS;
    };

    public ServiceType serviceType;
    public RequestType requestType;
    public TeleserviceType teleserviceType;
    public int serviceClass;
    public int result;

    public int[] ssInfo; /* This is the response data for most of the SS GET/SET
                            RIL requests. E.g. RIL_REQUSET_GET_CLIR returns
                            two ints, so first two values of ssInfo[] will be
                            used for respone if serviceType is SS_CLIR and
                            requestType is SS_INTERROGATION */

    public CallForwardInfo[] cfInfo; /* This is the response data for SS request
                                        to query call forward status. see
                                        RIL_REQUEST_QUERY_CALL_FORWARD_STATUS */

    public ServiceType ServiceTypeFromRILInt(int type) {
        ServiceType newType;
        /* RIL_SsServiceType ril.h */
        switch(type) {
            case 0: newType = ServiceType.SS_CFU; break;
            case 1: newType = ServiceType.SS_CF_BUSY; break;
            case 2: newType = ServiceType.SS_CF_NO_REPLY; break;
            case 3: newType = ServiceType.SS_CF_NOT_REACHABLE; break;
            case 4: newType = ServiceType.SS_CF_ALL; break;
            case 5: newType = ServiceType.SS_CF_ALL_CONDITIONAL; break;
            case 6: newType = ServiceType.SS_CLIP; break;
            case 7: newType = ServiceType.SS_CLIR; break;
            case 8: newType = ServiceType.SS_COLP; break;
            case 9: newType = ServiceType.SS_COLR; break;
            case 10: newType = ServiceType.SS_WAIT; break;
            case 11: newType = ServiceType.SS_BAOC; break;
            case 12: newType = ServiceType.SS_BAOIC; break;
            case 13: newType = ServiceType.SS_BAOIC_EXC_HOME; break;
            case 14: newType = ServiceType.SS_BAIC; break;
            case 15: newType = ServiceType.SS_BAIC_ROAMING; break;
            case 16: newType = ServiceType.SS_ALL_BARRING; break;
            case 17: newType = ServiceType.SS_OUTGOING_BARRING; break;
            case 18: newType = ServiceType.SS_INCOMING_BARRING; break;
            default:
                throw new RuntimeException(
                            "Unrecognized SS ServiceType " + type);
        }

        return newType;
    }

    public RequestType RequestTypeFromRILInt(int type) {
        RequestType newType;
        /* RIL_SsRequestType ril.h */
        switch(type) {
            case 0: newType = RequestType.SS_ACTIVATION; break;
            case 1: newType = RequestType.SS_DEACTIVATION; break;
            case 2: newType = RequestType.SS_INTERROGATION; break;
            case 3: newType = RequestType.SS_REGISTRATION; break;
            case 4: newType = RequestType.SS_ERASURE; break;
            default:
                throw new RuntimeException(
                            "Unrecognized SS RequestType " + type);
        }

        return newType;
    }

    public TeleserviceType TeleserviceTypeFromRILInt(int type) {
        TeleserviceType newType;
        /* RIL_SsTeleserviceType ril.h */
        switch(type) {
            case 0: newType = TeleserviceType.SS_ALL_TELE_AND_BEARER_SERVICES; break;
            case 1: newType = TeleserviceType.SS_ALL_TELESEVICES; break;
            case 2: newType = TeleserviceType.SS_TELEPHONY; break;
            case 3: newType = TeleserviceType.SS_ALL_DATA_TELESERVICES; break;
            case 4: newType = TeleserviceType.SS_SMS_SERVICES; break;
            case 5: newType = TeleserviceType.SS_ALL_TELESERVICES_EXCEPT_SMS; break;
            default:
                throw new RuntimeException(
                            "Unrecognized SS TeleserviceType " + type);
        }

        return newType;
    }

    public String toString() {
        return "[SsData] " + "ServiceType: " + serviceType
            + " RequestType: " + requestType
            + " TeleserviceType: " + teleserviceType
            + " ServiceClass: " + serviceClass
            + " Result: " + result
            + " Is Service Type CF: " + serviceType.isTypeCF();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 4f61509..3c4fb00 100644

//Synthetic comment -- @@ -1169,6 +1169,20 @@
}

/**
     * Simulates an Stk Call Control Alpha message
     * @param alphaString Alpha string to send.
     */
    public void triggerIncomingStkCcAlpha(String alphaString) {
        if (mCatCcAlphaRegistrant != null) {
            mCatCcAlphaRegistrant.notifyResult(alphaString);
        }
    }

    public void sendStkCcAplha(String alphaString) {
        triggerIncomingStkCcAlpha(alphaString);
    }

    /**
* Simulates an incoming USSD message
* @param statusCode  Status code string. See <code>setOnUSSD</code>
* in CommandsInterface.java







