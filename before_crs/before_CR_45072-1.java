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

// Preferred network type received from PhoneFactory.
// This is used when establishing a connection to the
//Synthetic comment -- @@ -384,6 +386,22 @@
mRingRegistrant.clear();
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
}

public CommandException(Error e) {
//Synthetic comment -- @@ -83,6 +92,24 @@
return new CommandException(Error.FDN_CHECK_FAILURE);
case RILConstants.ILLEGAL_SIM_OR_ME:
return new CommandException(Error.ILLEGAL_SIM_OR_ME);
default:
Log.e("GSM", "Unrecognized RIL errno " + ril_errno);
return new CommandException(Error.INVALID_RESPONSE);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..d90a3b8 100644

//Synthetic comment -- @@ -384,6 +384,28 @@
//void unSetSuppServiceNotifications(Handler h);

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
CDMA_LOCKED_UNTIL_POWER_CYCLE,  /* MS is locked until next power cycle */
CDMA_DROP,
CDMA_INTERCEPT,                 /* INTERCEPT order received, MS state idle entered */








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1f69f7a..c3dbe17 100755

//Synthetic comment -- @@ -114,6 +114,7 @@
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;
protected static final int EVENT_ICC_CHANGED                    = 31;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 6131792..e7fb18b 100755

//Synthetic comment -- @@ -50,6 +50,7 @@
import android.util.Log;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccCardApplicationStatus;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
//Synthetic comment -- @@ -2478,6 +2479,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2807,6 +2810,24 @@
}
break;

case RIL_UNSOl_CDMA_PRL_CHANGED:
if (RILJ_LOGD) unsljLogRet(response, ret);

//Synthetic comment -- @@ -3619,6 +3640,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
default: return "<unknown reponse>";
}
}
//Synthetic comment -- @@ -3647,6 +3670,45 @@
riljLogv("[UNSL]< " + responseToString(response) + " " + retToString(response, ret));
}


// ***** Methods for CDMA support
public void








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 299e140..a1edd20 100644

//Synthetic comment -- @@ -25,12 +25,17 @@

/*
* Intent's actions which are broadcasted by the Telephony once a new CAT
     * proactive command, session end arrive.
*/
public static final String CAT_CMD_ACTION =
"android.intent.action.stk.command";
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

/*
* Callback function from app to telephony to pass a result code and user's








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..82525af 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
static final int MSG_ID_REFRESH                  = 5;
static final int MSG_ID_RESPONSE                 = 6;
static final int MSG_ID_SIM_READY                = 7;

static final int MSG_ID_RIL_MSG_DECODED          = 10;

//Synthetic comment -- @@ -122,7 +123,7 @@
mCmdIf.setOnCatEvent(this, MSG_ID_EVENT_NOTIFY, null);
mCmdIf.setOnCatCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

mIccRecords = ir;
mUiccApplication = ca;

//Synthetic comment -- @@ -142,6 +143,7 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);

this.removeCallbacksAndMessages(null);
}
//Synthetic comment -- @@ -654,6 +656,24 @@
CatLog.d(this, "SIM ready. Reporting STK service running now...");
mCmdIf.reportStkServiceIsRunning(null);
break;
default:
throw new AssertionError("Unrecognized CAT command: " + msg.what);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CallFailCause.java b/src/java/com/android/internal/telephony/cdma/CallFailCause.java
//Synthetic comment -- index ad6c23c..03b1820 100644

//Synthetic comment -- @@ -40,6 +40,11 @@
static final int CALL_BARRED                    = 240;
static final int FDN_BLOCKED                    = 241;

static final int CDMA_LOCKED_UNTIL_POWER_CYCLE  = 1000;
static final int CDMA_DROP                      = 1001;
static final int CDMA_INTERCEPT                 = 1002;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 17eecbd..cf4b6dc 100755

//Synthetic comment -- @@ -393,6 +393,12 @@
return DisconnectCause.CALL_BARRED;
case CallFailCause.FDN_BLOCKED:
return DisconnectCause.FDN_BLOCKED;
case CallFailCause.CDMA_LOCKED_UNTIL_POWER_CYCLE:
return DisconnectCause.CDMA_LOCKED_UNTIL_POWER_CYCLE;
case CallFailCause.CDMA_DROP:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/CallFailCause.java b/src/java/com/android/internal/telephony/gsm/CallFailCause.java
//Synthetic comment -- index af2ad48..994dfa0 100644

//Synthetic comment -- @@ -49,5 +49,11 @@
static final int ACM_LIMIT_EXCEEDED = 68;
static final int CALL_BARRED        = 240;
static final int FDN_BLOCKED        = 241;
static final int ERROR_UNSPECIFIED = 0xffff;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..adfae45 100644

//Synthetic comment -- @@ -160,6 +160,7 @@
mCM.setOnUSSD(this, EVENT_USSD, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);

if (false) {
try {
//Synthetic comment -- @@ -213,6 +214,7 @@
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnUSSD(this);
mCM.unSetOnSuppServiceNotification(this);

mPendingMMIs.clear();

//Synthetic comment -- @@ -1087,7 +1089,7 @@
* The exception is cancellation of an incoming USSD-REQUEST, which is
* not on the list.
*/
        if (mPendingMMIs.remove(mmi) || mmi.isUssdRequest()) {
mMmiCompleteRegistrants.notifyRegistrants(
new AsyncResult(null, mmi, null));
}
//Synthetic comment -- @@ -1332,6 +1334,16 @@
}
break;

default:
super.handleMessage(msg);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 59aa12a..5fd8deb 100644

//Synthetic comment -- @@ -368,6 +368,15 @@
case CallFailCause.UNOBTAINABLE_NUMBER:
return DisconnectCause.UNOBTAINABLE_NUMBER;

case CallFailCause.ERROR_UNSPECIFIED:
case CallFailCause.NORMAL_CLEARING:
default:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..fd08224 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.util.Log;

import static com.android.internal.telephony.CommandsInterface.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -126,6 +127,9 @@

private boolean isUssdRequest;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -246,6 +250,136 @@
return ret;
}

//***** Private Class methods

/** make empty strings be null.
//Synthetic comment -- @@ -622,6 +756,10 @@
return isUssdRequest;
}

/** Process a MMI code or short code...anything that isn't a dialing number */
void
processCode () {
//Synthetic comment -- @@ -949,6 +1087,24 @@
if (err == CommandException.Error.FDN_CHECK_FAILURE) {
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
return context.getText(com.android.internal.R.string.mmiFdnError);
}
}

//Synthetic comment -- @@ -1011,8 +1167,7 @@
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
} else {
                    sb.append(context.getText(
                            com.android.internal.R.string.mmiError));
}
} else {
sb.append(context.getText(








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SsData.java b/src/java/com/android/internal/telephony/gsm/SsData.java
new file mode 100644
//Synthetic comment -- index 0000000..28ab408

//Synthetic comment -- @@ -0,0 +1,190 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 4f61509..3c4fb00 100644

//Synthetic comment -- @@ -1169,6 +1169,20 @@
}

/**
* Simulates an incoming USSD message
* @param statusCode  Status code string. See <code>setOnUSSD</code>
* in CommandsInterface.java







