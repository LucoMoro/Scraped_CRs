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

// Constructors

Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
    resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
        isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
} else {
    ar = (AsyncResult)msg.obj;
    IccRecords r = mIccRecords.get();
    if (ar.exception == null && r != null && !(mCfNumber == null || mCfNumber.isEmpty())) {
        r.setVoiceCallForwardingFlag(1, msg.arg1 == 1, mCfNumber);
    }
}

onComplete = (Message) ar.userObj;
if (onComplete != null) {
} else {
    for (int i = 0, s = infos.length; i < s; i++) {
        if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
            r.setVoiceCallForwardingFlag(1, (infos[i].status == 1), mCfNumber);
            break;
        }
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

if ((ar.exception == null) && (msg.arg1 == 1) && !(mCfNumber == null || mCfNumber.isEmpty())) {
    boolean cffEnabled = (msg.arg2 == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled, mCfNumber);
    }
}

if (info.serviceClass == CommandsInterface.SERVICE_CLASS_VOICE) {
    boolean cffEnabled = (info.status == 1);
    if (mIccRecords != null) {
        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled, mCfNumber);
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
public void setVoiceCallForwardingFlag(int line, boolean enable, String cfNumber) {
    if (line != 1) return; // only line 1 is supported

    mEfCfis[1] &= 0xfe; 
    if (enable) {
        mEfCfis[1] |= 0x01; // Set status to enabled
        mEfCfis[2] = cfNumber.getBytes(); // Store CF number (assuming index 2 for the number)
    } else {
        mEfCfis[1] &= 0x7e; // Clear enabled status
        mEfCfis[2] = null; // Clear CF number
    }
    
    mFh.updateEFLinearFixed(EF_CFIS, 1, mEfCfis, null, null);
}

//<End of snippet n. 3>