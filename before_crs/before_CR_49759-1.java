/*Add CellInfo support to RIL.

Change-Id:Ief739f83b903105610b99df4a898ea6aa7b1f303*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 49d3c76..0d744e1 100644

//Synthetic comment -- @@ -71,6 +71,7 @@
protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
protected RegistrantList mIccRefreshRegistrants = new RegistrantList();

protected Registrant mGsmSmsRegistrant;
protected Registrant mCdmaSmsRegistrant;
//Synthetic comment -- @@ -569,6 +570,17 @@
mRilConnectedRegistrants.remove(h);
}

/**
* {@inheritDoc}
*/








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d9c3dc7..444f0aa 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccCardStatus;

import android.os.Message;
import android.os.Handler;
//Synthetic comment -- @@ -554,6 +555,20 @@
void unregisterForRilConnected(Handler h);

/**
* Supply the ICC PIN to the ICC card
*
*  returned message
//Synthetic comment -- @@ -1554,6 +1569,25 @@
public void getIccCardStatus(Message result);

/**
* Return if the current radio is LTE on CDMA. This
* is a tri-state return value as for a period of time
* the mode may be unknown.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af81e04..2d250bb 100644

//Synthetic comment -- @@ -41,6 +41,16 @@
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.PowerManager.WakeLock;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
//Synthetic comment -- @@ -2036,6 +2046,33 @@
send(rr);
}

//***** Private Methods

private void sendScreenState(boolean on) {
//Synthetic comment -- @@ -2335,6 +2372,8 @@
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: ret = responseVoid(p); break;
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: ret = responseICC_IO(p); break;
case RIL_REQUEST_VOICE_RADIO_TECH: ret = responseInts(p); break;
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2516,6 +2555,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2873,6 +2913,16 @@
notifyRegistrantsRilConnectionChanged(((int[])ret)[0]);
break;
}
}
}

//Synthetic comment -- @@ -3315,6 +3365,90 @@
return response;
}

private Object responseGetPreferredNetworkType(Parcel p) {
int [] response = (int[]) responseInts(p);

//Synthetic comment -- @@ -3608,6 +3742,8 @@
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: return "RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: return "RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS";
case RIL_REQUEST_VOICE_RADIO_TECH: return "RIL_REQUEST_VOICE_RADIO_TECH";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3657,6 +3793,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
default: return "<unknown reponse>";
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipCommandInterface.java b/src/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index 99f4e0f..a2eed6d 100644

//Synthetic comment -- @@ -416,9 +416,19 @@
Message response) {
}

public void requestIsimAuthentication(String nonce, Message response) {
}

public void getVoiceRadioTechnology(Message result) {
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 1672044..6520783 100644

//Synthetic comment -- @@ -1522,4 +1522,13 @@
public void getVoiceRadioTechnology(Message response) {
unimplemented(response);
}
}







