//<Beginning of snippet n. 0>
}

/**
* Indicates whether SIM is in provisioned state or not.
* Overridden only if SIM can be dynamically provisioned via OTA.
*/

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private String mImei;
private String mImeiSv;
private String mVmNumber;

// Constructors

Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
    resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
        isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
} else {
    ar = (AsyncResult)msg.obj;
    IccRecords r = mIccRecords.get();
    if (ar.exception == null && r != null) {
        boolean enable = msg.arg1 == 1;
        r.setVoiceCallForwardingFlag(1, enable);
        
        if (enable) {
            String cfNumber = getCfNumberFromMessage(msg);
            if (cfNumber != null && !cfNumber.isEmpty()) {
                r.setVoiceCallForwardingNumber(cfNumber);
            }
        }
    }
    onComplete = (Message) ar.userObj;
    if (onComplete != null) {
        onComplete.sendToTarget();
    } 
    else {
        for (int i = 0, s = infos.length; i < s; i++) {
            if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                r.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
                if (infos[i].status == 1) {
                    String cfNumber = getCfNumberFromInfo(infos[i]);
                    if (cfNumber != null && !cfNumber.isEmpty()) {
                        r.setVoiceCallForwardingNumber(cfNumber);
                    }
                }
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
        
        if (cffEnabled) {
            String cfNumber = getCfNumberFromMessage(msg);
            if (cfNumber != null && !cfNumber.isEmpty()) {
                mIccRecords.setVoiceCallForwardingNumber(cfNumber);
            }
        }
    }
}

if (info != null && (info.serviceClass == CommandsInterface.SERVICE_CLASS_VOICE)) {
    boolean cffEnabled = (info.status == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
        
        if (cffEnabled) {
            String cfNumber = getCfNumberFromInfo(info);
            if (cfNumber != null && !cfNumber.isEmpty()) {
                mIccRecords.setVoiceCallForwardingNumber(cfNumber);
            }
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

    if (enable) {
        String cfNumber = getCfNumber(); 
        if (cfNumber != null && !cfNumber.isEmpty()) {
            mEfCfis[2] = cfNumber.getBytes(); 
        }
    }

    mFh.updateEFLinearFixed(
        EF_CFIS, 1, mEfCfis, null, null);
}
//<End of snippet n. 3>