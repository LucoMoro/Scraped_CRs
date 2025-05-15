//<Beginning of snippet n. 0>
}

/**
* Indicates wether SIM is in provisioned state or not.
* Overridden only if SIM can be dynamically provisioned via OTA.
*/

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private String mImei;
private String mImeiSv;
private String mVmNumber;
private String mCfNumber; // New field for call forwarding number

// Constructors

Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
    resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
        isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
} else {
    ar = (AsyncResult)msg.obj;
    IccRecords r = mIccRecords.get();
    if (ar.exception == null && r != null) {
        r.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
        
        // Update the CF number if call forwarding is enabled
        if (msg.arg1 == 1 && mCfNumber != null) {
            r.setCallForwardingNumber(mCfNumber); // Assuming there's a method to set the CF number
        }
    }
    onComplete = (Message) ar.userObj;
    if (onComplete != null) {
        // Handle completion
    } else {
        for (int i = 0, s = infos.length; i < s; i++) {
            if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                r.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
                if (infos[i].status == 1) {
                    mCfNumber = infos[i].number; // Save the CF number from info
                }
                break;
            }
        }
    }
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
if ((ar.exception == null) && (msg.arg1 == 1)) {
    boolean cffEnabled = (msg.arg2 == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
        // Include CF Number in EF update
        if (cffEnabled && mCfNumber != null) {
            mIccRecords.setCallForwardingNumber(mCfNumber); // Update CF number
        }
    }
}

if (commandInterfaceCFReason == CommandsInterface.SERVICE_CLASS_VOICE) {
    boolean cffEnabled = (info.status == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
        // Include CF Number in EF update
        if (cffEnabled && mCfNumber != null) {
            mIccRecords.setCallForwardingNumber(mCfNumber); // Update CF number
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
@Override
public void setVoiceCallForwardingFlag(int line, boolean enable) {

    if (line != 1) return; // only line 1 is supported

    mEfCfis[1] &= 0xfe;
    
    // Validate CF number before updating
    if (enable && (mCfNumber == null || !isValidCfNumber(mCfNumber))) {
        Log.e("CallForwarding", "Invalid CF number");
        return; // handle invalid CF number 
    }

    // TODO: Should really update other fields in EF_CFIS, eg,
    // dialing number.  We don't read or use it right now.
    if (enable) {
        mEfCfis[1] |= 0x01; // update enable flag
    }

    mFh.updateEFLinearFixed(EF_CFIS, 1, mEfCfis, null /* onComplete */);
}

// Add method to validate CF number
private boolean isValidCfNumber(String cfNumber) {
    // A simple validation for demonstration (adapt as necessary)
    return cfNumber != null && cfNumber.matches("\\+?[0-9]*");
}
//<End of snippet n. 3>