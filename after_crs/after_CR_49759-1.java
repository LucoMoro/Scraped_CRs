/*Add CellInfo support to RIL.

Change-Id:Ief739f83b903105610b99df4a898ea6aa7b1f303*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 49d3c76..0d744e1 100644

//Synthetic comment -- @@ -71,6 +71,7 @@
protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
protected RegistrantList mIccRefreshRegistrants = new RegistrantList();
    protected RegistrantList mCellInfoListRegistrants = new RegistrantList();

protected Registrant mGsmSmsRegistrant;
protected Registrant mCdmaSmsRegistrant;
//Synthetic comment -- @@ -569,6 +570,17 @@
mRilConnectedRegistrants.remove(h);
}

    @Override
    public void registerForRilCellInfoList(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCellInfoListRegistrants.add(r);
    }

    @Override
    public void unregisterForRilCellInfoList(Handler h) {
        mCellInfoListRegistrants.remove(h);
    }

/**
* {@inheritDoc}
*/








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d9c3dc7..444f0aa 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccCardStatus;
import android.telephony.CellInfo;

import android.os.Message;
import android.os.Handler;
//Synthetic comment -- @@ -554,6 +555,20 @@
void unregisterForRilConnected(Handler h);

/**
     * Registers the handler for RIL_UNSOL_RIL_CONNECT events.
     *
     * When ril connects or disconnects a message is sent to the registrant
     * which contains an AsyncResult, ar, in msg.obj. The ar.result is an
     * Integer which is the version of the ril or -1 if the ril disconnected.
     *
     * @param h Handler for notification message.
     * @param what User-defined message code.
     * @param obj User object.
     */
    void registerForRilCellInfoList(Handler h, int what, Object obj);
    void unregisterForRilCellInfoList(Handler h);

    /**
* Supply the ICC PIN to the ICC card
*
*  returned message
//Synthetic comment -- @@ -1554,6 +1569,25 @@
public void getIccCardStatus(Message result);

/**
     * Request the current list of CellInfo
     *
     * @param result
     *          Callback message containing an array of {@link CellInfo}.
     */
    public void getCellInfoList(Message result);

    /**
     * Set the minimum time between RIL_UNSOL_CELL_INFO_LIST in milliseconds.
     * A value of 0 means return as quickly as possible, a value of MAX_INT (0x7fffffff)
     * means no message shall be sent.
     *
     * @param minTimeMs time in milliseconds between RIL_UNSOL_CELL_INFO_LIST messages
     * @param result
     *          Callback message containing an array of {@link CellInfo}.
     */
    public void setUnsolCellInfoListRate(int minTimeMs, Message result);

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
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
//Synthetic comment -- @@ -2036,6 +2046,33 @@
send(rr);
}

    /**
     * {@inheritDoc}
     */
    @Override
    public void getCellInfoList(Message response) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_GET_CELL_INFO_LIST, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUnsolCellInfoListRate(int minTimeMs, Message response) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE, response);

        rr.mp.writeInt(1);
        rr.mp.writeInt(minTimeMs);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

//***** Private Methods

private void sendScreenState(boolean on) {
//Synthetic comment -- @@ -2335,6 +2372,8 @@
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: ret = responseVoid(p); break;
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: ret = responseICC_IO(p); break;
case RIL_REQUEST_VOICE_RADIO_TECH: ret = responseInts(p); break;
            case RIL_REQUEST_GET_CELL_INFO_LIST: ret = responseCellInfoList(p);
            case RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE: ret = responseVoid(p);
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2516,6 +2555,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;
            case RIL_UNSOL_CELL_INFO_LIST: ret = responseCellInfoList(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2873,6 +2913,16 @@
notifyRegistrantsRilConnectionChanged(((int[])ret)[0]);
break;
}

            case RIL_UNSOL_CELL_INFO_LIST: {
                if (RILJ_LOGD) unsljLogRet(response, ret);

                if (mCellInfoListRegistrants != null) {
                    mCellInfoListRegistrants.notifyRegistrants(
                            new AsyncResult (null, ret, null));
                }
                break;
            }
}
}

//Synthetic comment -- @@ -3315,6 +3365,90 @@
return response;
}

    private CellInfo getCellInfo(Parcel p) {
        CellInfo ci;
        int cellInfoType = p.readInt();
        int registered = p.readInt();
        long time = p.readLong();
        switch(cellInfoType) {
            case CellInfo.TYPE_GSM: {
                CellInfoGsm ciGsm = new CellInfoGsm();
                ciGsm.setRegisterd(registered != 0);
                ciGsm.setTimeStamp(time);
                ciGsm.setCellIdentity(new CellIdentityGsm(
                        p.readInt(),    // mcc
                        p.readInt(),    // mnc
                        p.readInt(),    // lac
                        p.readInt(),    // cid
                        p.readInt()));  // psc
                ciGsm.setCellSignalStrength(new CellSignalStrengthGsm(
                        p.readInt(),    // ss
                        p.readInt()));  // ber
                ci = ciGsm;
                break;
            }
            case CellInfo.TYPE_CDMA: {
                CellInfoCdma ciCdma = new CellInfoCdma();
                ciCdma.setRegisterd(registered != 0);
                ciCdma.setTimeStamp(time);
                ciCdma.setCellIdentity(new CellIdentityCdma(
                        p.readInt(),    // nid
                        p.readInt(),    // sid
                        p.readInt(),    // bid
                        p.readInt(),    // lat
                        p.readInt()));  // lon
                ciCdma.setCellSignalStrength(new CellSignalStrengthCdma(
                        p.readInt(),    // cdma dbm
                        p.readInt(),    // cdma ecio
                        p.readInt(),    // evdo dbm
                        p.readInt(),    // evdo ecio
                        p.readInt()));  // evdo signalNoiseRadio
                ci = ciCdma;
                break;
            }
            case CellInfo.TYPE_LTE: {
                CellInfoLte ciLte = new CellInfoLte();
                ciLte.setRegisterd(registered != 0);
                ciLte.setTimeStamp(time);
                ciLte.setCellIdentity(new CellIdentityLte(
                        p.readInt(),    // mcc
                        p.readInt(),    // mnc
                        p.readInt(),    // ci
                        p.readInt(),    // pci
                        p.readInt()));  // tac
                ciLte.setCellSignalStrength(new CellSignalStrengthLte(
                        p.readInt(),    // signalStrengh
                        p.readInt(),    // rsrp
                        p.readInt(),    // rsrq
                        p.readInt(),    // rssnr
                        p.readInt(),    // cqi
                        p.readInt()));  // timingAdvance
                ci = ciLte;
                break;
            }
            default:
                riljLog("Unexpected CellInfoType=" + cellInfoType);
                ci = null;
                break;
        }
        return ci;
    }

    private Object
    responseCellInfoList(Parcel p) {
        ArrayList<CellInfo> response;

        int num = p.readInt();
        riljLog("responseCellInfoList num=" + num);

        response = new ArrayList<CellInfo>(num);
        for (int i = 0; i < num; i++) {
            response.add(getCellInfo(p));
        }

        return response;
    }

private Object responseGetPreferredNetworkType(Parcel p) {
int [] response = (int[]) responseInts(p);

//Synthetic comment -- @@ -3608,6 +3742,8 @@
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: return "RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: return "RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS";
case RIL_REQUEST_VOICE_RADIO_TECH: return "RIL_REQUEST_VOICE_RADIO_TECH";
            case RIL_REQUEST_GET_CELL_INFO_LIST: return "RIL_REQUEST_GET_CELL_INFO_LIST";
            case RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE: return "RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3657,6 +3793,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            case RIL_UNSOL_CELL_INFO_LIST: return "UNSOL_GET_CELL_INFO_LIST";
default: return "<unknown reponse>";
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipCommandInterface.java b/src/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index 99f4e0f..a2eed6d 100644

//Synthetic comment -- @@ -416,9 +416,19 @@
Message response) {
}

    @Override
public void requestIsimAuthentication(String nonce, Message response) {
}

    @Override
public void getVoiceRadioTechnology(Message result) {
}

    @Override
    public void getCellInfoList(Message result) {
    }

    @Override
    public void setUnsolCellInfoListRate(int minTimeMs, Message result) {
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 1672044..6520783 100644

//Synthetic comment -- @@ -1522,4 +1522,13 @@
public void getVoiceRadioTechnology(Message response) {
unimplemented(response);
}
    @Override
    public void getCellInfoList(Message response) {
        unimplemented(response);
    }

    @Override
    public void setUnsolCellInfoListRate(int minTimeMs, Message response) {
        unimplemented(response);
    }
}







