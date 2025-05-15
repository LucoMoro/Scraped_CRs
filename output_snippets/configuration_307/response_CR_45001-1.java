//<Beginning of snippet n. 0>


}

/**
* Indicates whether SIM is in provisioned state or not.
* Overridden only if SIM can be dynamically provisioned via OTA.
*

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private String mImei;
private String mImeiSv;
private String mVmNumber;
private String mCallForwardingNumber; // Added to store CF number


// Constructors

Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
    resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE, isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
} else {
    ar = (AsyncResult) msg.obj;
    IccRecords r = mIccRecords.get();
    if (ar.exception == null && r != null) {
        r.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
        if (msg.arg1 == 1) { // Updating CF number when enabled
            mCallForwardingNumber = obtainCallForwardingNumber(); // Method to obtain CF number
            mFh.updateEFLinearFixed(EF_CFIS, 1, r.getVoiceCallForwardingFlag(), mCallForwardingNumber); // Update EF_CFIS with CF number
        }
    }
    onComplete = (Message) ar.userObj;
    if (onComplete != null) {
    } else {
        for (int i = 0, s = infos.length; i < s; i++) {
            if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                r.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
                // should only have the one
                break;
            }
        }
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


if ((ar.exception == null) && (msg.arg1 == 1)) {
    boolean cffEnabled = (msg.arg2 == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
        if (cffEnabled) { // Updating CF number when enabled
            mCallForwardingNumber = obtainCallForwardingNumber(); // Method to obtain CF number
            mFh.updateEFLinearFixed(EF_CFIS, 1, mIccRecords.getVoiceCallForwardingFlag(), mCallForwardingNumber); // Update EF_CFIS with CF number
        }
    }
}

if (== CommandsInterface.SERVICE_CLASS_VOICE) {
    boolean cffEnabled = (info.status == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
        if (cffEnabled) { // Updating CF number when enabled
            mCallForwardingNumber = obtainCallForwardingNumber(); // Method to obtain CF number
            mFh.updateEFLinearFixed(EF_CFIS, 1, mIccRecords.getVoiceCallForwardingFlag(), mCallForwardingNumber); // Update EF_CFIS with CF number
        }
    }
}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
private static final int CPHS_SST_MBN_MASK = 0x30;
private static final int CPHS_SST_MBN_ENABLED = 0x30;

// ***** Event Constants

private static final int EVENT_APP_READY = 1;
*/
@Override
public void setVoiceCallForwardingFlag(int line, boolean enable) {
    
    if (line != 1) return; // only line 1 is supported

    mEfCfis[1] &= 0xfe;

    // TODO: Should really update other fields in EF_CFIS, eg,
    // dialing number.  We don't read or use it right now.

    mFh.updateEFLinearFixed(
        EF_CFIS, 1, mEfCfis, mCallForwardingNumber // Include CF number
    );

//<End of snippet n. 3>