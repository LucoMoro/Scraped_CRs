/*Telephony: Add uicc debug info to DebugService

Change-Id:I03be52f97ea84808d608c48b4e6ffa2454e2a67c*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DebugService.java b/src/java/com/android/internal/telephony/DebugService.java
//Synthetic comment -- index 82543ae..deba3eb 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import android.telephony.Rlog;

import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.IccCardProxy;

import java.io.FileDescriptor;
import java.io.PrintWriter;

//Synthetic comment -- @@ -99,6 +102,20 @@
}
pw.flush();
pw.println("++++++++++++++++++++++++++++++++");
        try {
            UiccController.getInstance().dump(fd, pw, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pw.flush();
        pw.println("++++++++++++++++++++++++++++++++");
        try {
            ((IccCardProxy)phoneProxy.getIccCard()).dump(fd, pw, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pw.flush();
        pw.println("++++++++++++++++++++++++++++++++");
log("dump: -");
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af81e04..c3256df 100644

//Synthetic comment -- @@ -3872,7 +3872,7 @@
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("RIL: " + this);
pw.println(" mSocket=" + mSocket);
pw.println(" mSenderThread=" + mSenderThread);
pw.println(" mSender=" + mSender);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccCardProxy.java b/src/java/com/android/internal/telephony/uicc/IccCardProxy.java
//Synthetic comment -- index 72da49c..f1fae9d 100644

//Synthetic comment -- @@ -46,6 +46,9 @@
import com.android.internal.telephony.uicc.IccCardStatus.PinState;
import com.android.internal.telephony.uicc.UiccController;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_SIM_STATE;

/**
//Synthetic comment -- @@ -699,4 +702,34 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("IccCardProxy: " + this);
        pw.println(" mContext=" + mContext);
        pw.println(" mCi=" + mCi);
        pw.println(" mAbsentRegistrants: size=" + mAbsentRegistrants.size());
        for (int i = 0; i < mAbsentRegistrants.size(); i++) {
            pw.println("  mAbsentRegistrants[" + i + "]=" + ((Registrant)mAbsentRegistrants.get(i)).getHandler());
        }
        pw.println(" mPinLockedRegistrants: size=" + mPinLockedRegistrants.size());
        for (int i = 0; i < mPinLockedRegistrants.size(); i++) {
            pw.println("  mPinLockedRegistrants[" + i + "]=" + ((Registrant)mPinLockedRegistrants.get(i)).getHandler());
        }
        pw.println(" mNetworkLockedRegistrants: size=" + mNetworkLockedRegistrants.size());
        for (int i = 0; i < mNetworkLockedRegistrants.size(); i++) {
            pw.println("  mNetworkLockedRegistrants[" + i + "]=" + ((Registrant)mNetworkLockedRegistrants.get(i)).getHandler());
        }
        pw.println(" mCurrentAppType=" + mCurrentAppType);
        pw.println(" mUiccController=" + mUiccController);
        pw.println(" mUiccCard=" + mUiccCard);
        pw.println(" mUiccApplication=" + mUiccApplication);
        pw.println(" mIccRecords=" + mIccRecords);
        pw.println(" mCdmaSSM=" + mCdmaSSM);
        pw.println(" mRadioOn=" + mRadioOn);
        pw.println(" mQuietMode=" + mQuietMode);
        pw.println(" mInitialized=" + mInitialized);
        pw.println(" mExternalState=" + mExternalState);

        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccRecords.java b/src/java/com/android/internal/telephony/uicc/IccRecords.java
//Synthetic comment -- index a690e93..2f326aa 100644

//Synthetic comment -- @@ -25,6 +25,8 @@

import com.android.internal.telephony.CommandsInterface;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
//Synthetic comment -- @@ -442,4 +444,49 @@
public UsimServiceTable getUsimServiceTable() {
return null;
}

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("IccRecords: " + this);
        pw.println(" mDestroyed=" + mDestroyed);
        pw.println(" mCi=" + mCi);
        pw.println(" mFh=" + mFh);
        pw.println(" mParentApp=" + mParentApp);
        pw.println(" recordsLoadedRegistrants: size=" + recordsLoadedRegistrants.size());
        for (int i = 0; i < recordsLoadedRegistrants.size(); i++) {
            pw.println("  recordsLoadedRegistrants[" + i + "]=" + ((Registrant)recordsLoadedRegistrants.get(i)).getHandler());
        }
        pw.println(" mImsiReadyRegistrants: size=" + mImsiReadyRegistrants.size());
        for (int i = 0; i < mImsiReadyRegistrants.size(); i++) {
            pw.println("  mImsiReadyRegistrants[" + i + "]=" + ((Registrant)mImsiReadyRegistrants.get(i)).getHandler());
        }
        pw.println(" mRecordsEventsRegistrants: size=" + mRecordsEventsRegistrants.size());
        for (int i = 0; i < mRecordsEventsRegistrants.size(); i++) {
            pw.println("  mRecordsEventsRegistrants[" + i + "]=" + ((Registrant)mRecordsEventsRegistrants.get(i)).getHandler());
        }
        pw.println(" mNewSmsRegistrants: size=" + mNewSmsRegistrants.size());
        for (int i = 0; i < mNewSmsRegistrants.size(); i++) {
            pw.println("  mNewSmsRegistrants[" + i + "]=" + ((Registrant)mNewSmsRegistrants.get(i)).getHandler());
        }
        pw.println(" mNetworkSelectionModeAutomaticRegistrants: size=" + mNetworkSelectionModeAutomaticRegistrants.size());
        for (int i = 0; i < mNetworkSelectionModeAutomaticRegistrants.size(); i++) {
            pw.println("  mNetworkSelectionModeAutomaticRegistrants[" + i + "]=" + ((Registrant)mNetworkSelectionModeAutomaticRegistrants.get(i)).getHandler());
        }
        pw.println(" recordsRequested=" + recordsRequested);
        pw.println(" recordsToLoad=" + recordsToLoad);
        pw.println(" adnCache=" + adnCache);
        pw.println(" iccid=" + iccid);
        pw.println(" msisdn=" + msisdn);
        pw.println(" msisdnTag=" + msisdnTag);
        pw.println(" voiceMailNum=" + voiceMailNum);
        pw.println(" voiceMailTag=" + voiceMailTag);
        pw.println(" newVoiceMailNum=" + newVoiceMailNum);
        pw.println(" newVoiceMailTag=" + newVoiceMailTag);
        pw.println(" isVoiceMailFixed=" + isVoiceMailFixed);
        pw.println(" countVoiceMessages=" + countVoiceMessages);
        pw.println(" mImsi=" + mImsi);
        pw.println(" mncLength=" + mncLength);
        pw.println(" mailboxIndex=" + mailboxIndex);
        pw.println(" spn=" + spn);
        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IsimUiccRecords.java b/src/java/com/android/internal/telephony/uicc/IsimUiccRecords.java
//Synthetic comment -- index 212cb0c..2330aa8a 100644

//Synthetic comment -- @@ -26,8 +26,11 @@
import com.android.internal.telephony.gsm.SimTlv;
//import com.android.internal.telephony.gsm.VoiceMailConstants;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static com.android.internal.telephony.uicc.IccConstants.EF_DOMAIN;
import static com.android.internal.telephony.uicc.IccConstants.EF_IMPI;
//Synthetic comment -- @@ -263,4 +266,15 @@
protected void loge(String s) {
if (DBG) Rlog.e(LOG_TAG, "[ISIM] " + s);
}

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("IsimRecords: " + this);
        pw.println(" extends:");
        super.dump(fd, pw, args);
        pw.println(" mIsimImpi=" + mIsimImpi);
        pw.println(" mIsimDomain=" + mIsimDomain);
        pw.println(" mIsimImpu[]=" + Arrays.toString(mIsimImpu));
        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/RuimRecords.java b/src/java/com/android/internal/telephony/uicc/RuimRecords.java
//Synthetic comment -- index 4c8cf06..c7d1070 100755

//Synthetic comment -- @@ -23,7 +23,10 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_TEST_CSIM;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import android.content.Context;
import android.os.AsyncResult;
//Synthetic comment -- @@ -797,4 +800,23 @@
protected void loge(String s) {
Rlog.e(LOG_TAG, "[RuimRecords] " + s);
}

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("RuimRecords: " + this);
        pw.println(" extends:");
        super.dump(fd, pw, args);
        pw.println(" m_ota_commited=" + m_ota_commited);
        pw.println(" mMyMobileNumber=" + mMyMobileNumber);
        pw.println(" mMin2Min1=" + mMin2Min1);
        pw.println(" mPrlVersion=" + mPrlVersion);
        pw.println(" mEFpl[]=" + Arrays.toString(mEFpl));
        pw.println(" mEFli[]=" + Arrays.toString(mEFli));
        pw.println(" mCsimSpnDisplayCondition=" + mCsimSpnDisplayCondition);
        pw.println(" mMdn=" + mMdn);
        pw.println(" mMin=" + mMin);
        pw.println(" mHomeSystemId=" + mHomeSystemId);
        pw.println(" mHomeNetworkId=" + mHomeNetworkId);
        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/SIMRecords.java b/src/java/com/android/internal/telephony/uicc/SIMRecords.java
//Synthetic comment -- index 6f226b8..66eaf6a 100755

//Synthetic comment -- @@ -37,7 +37,10 @@
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.gsm.SmsMessage;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;


/**
//Synthetic comment -- @@ -1673,4 +1676,26 @@

log("[CSP] Value Added Service Group (0xC0), not found!");
}

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("SIMRecords: " + this);
        pw.println(" extends:");
        super.dump(fd, pw, args);
        pw.println(" mVmConfig=" + mVmConfig);
        pw.println(" mSpnOverride=" + mSpnOverride);
        pw.println(" callForwardingEnabled=" + callForwardingEnabled);
        pw.println(" spnState=" + spnState);
        pw.println(" mCphsInfo=" + mCphsInfo);
        pw.println(" mCspPlmnEnabled=" + mCspPlmnEnabled);
        pw.println(" efMWIS[]=" + Arrays.toString(efMWIS));
        pw.println(" efCPHS_MWI[]=" + Arrays.toString(efCPHS_MWI));
        pw.println(" mEfCff[]=" + Arrays.toString(mEfCff));
        pw.println(" mEfCfis[]=" + Arrays.toString(mEfCfis));
        pw.println(" spnDisplayCondition=" + spnDisplayCondition);
        pw.println(" spdiNetworks[]=" + spdiNetworks);
        pw.println(" pnnHomeName=" + pnnHomeName);
        pw.println(" mUsimServiceTable=" + mUsimServiceTable);
        pw.flush();
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccCard.java b/src/java/com/android/internal/telephony/uicc/UiccCard.java
//Synthetic comment -- index 8da07b8..bc211ed 100644

//Synthetic comment -- @@ -49,6 +49,9 @@

import com.android.internal.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
* {@hide}
*/
//Synthetic comment -- @@ -350,4 +353,49 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("UiccCard:");
        pw.println(" mCi=" + mCi);
        pw.println(" mDestroyed=" + mDestroyed);
        pw.println(" mLastRadioState=" + mLastRadioState);
        pw.println(" mCatService=" + mCatService);
        pw.println(" mAbsentRegistrants: size=" + mAbsentRegistrants.size());
        for (int i = 0; i < mAbsentRegistrants.size(); i++) {
            pw.println("  mAbsentRegistrants[" + i + "]=" + ((Registrant)mAbsentRegistrants.get(i)).getHandler());
        }
        pw.println(" mCardState=" + mCardState);
        pw.println(" mUniversalPinState=" + mUniversalPinState);
        pw.println(" mGsmUmtsSubscriptionAppIndex=" + mGsmUmtsSubscriptionAppIndex);
        pw.println(" mCdmaSubscriptionAppIndex=" + mCdmaSubscriptionAppIndex);
        pw.println(" mImsSubscriptionAppIndex=" + mImsSubscriptionAppIndex);
        pw.println(" mImsSubscriptionAppIndex=" + mImsSubscriptionAppIndex);
        pw.println(" mUiccApplications: length=" + mUiccApplications.length);
        for (int i = 0; i < mUiccApplications.length; i++) {
            if (mUiccApplications[i] == null) {
                pw.println("  mUiccApplications[" + i + "]=" + null);
            } else {
                pw.println("  mUiccApplications[" + i + "]=" + mUiccApplications[i].getType() + " " + mUiccApplications[i]);
            }
        }
        pw.println();
        // Print details of all applications
        for (UiccCardApplication app : mUiccApplications) {
            if (app != null) {
                app.dump(fd, pw, args);
                pw.println();
            }
        }
        // Print details of all IccRecords
        for (UiccCardApplication app : mUiccApplications) {
            if (app != null) {
                IccRecords ir = app.getIccRecords();
                if (ir != null) {
                    ir.dump(fd, pw, args);
                    pw.println();
                }
            }
        }
        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java b/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java
//Synthetic comment -- index 2a349f7..bde5925 100644

//Synthetic comment -- @@ -30,6 +30,9 @@
import com.android.internal.telephony.uicc.IccCardApplicationStatus.PersoSubState;
import com.android.internal.telephony.uicc.IccCardStatus.PinState;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
* {@hide}
*/
//Synthetic comment -- @@ -691,4 +694,38 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("UiccCardApplication: " + this);
        pw.println(" mUiccCard=" + mUiccCard);
        pw.println(" mAppState=" + mAppState);
        pw.println(" mAppType=" + mAppType);
        pw.println(" mPersoSubState=" + mPersoSubState);
        pw.println(" mAid=" + mAid);
        pw.println(" mAppLabel=" + mAppLabel);
        pw.println(" mPin1Replaced=" + mPin1Replaced);
        pw.println(" mPin1State=" + mPin1State);
        pw.println(" mPin2State=" + mPin2State);
        pw.println(" mIccFdnEnabled=" + mIccFdnEnabled);
        pw.println(" mDesiredFdnEnabled=" + mDesiredFdnEnabled);
        pw.println(" mIccLockEnabled=" + mIccLockEnabled);
        pw.println(" mDesiredPinLocked=" + mDesiredPinLocked);
        pw.println(" mCi=" + mCi);
        pw.println(" mIccRecords=" + mIccRecords);
        pw.println(" mIccFh=" + mIccFh);
        pw.println(" mDestroyed=" + mDestroyed);
        pw.println(" mReadyRegistrants: size=" + mReadyRegistrants.size());
        for (int i = 0; i < mReadyRegistrants.size(); i++) {
            pw.println("  mReadyRegistrants[" + i + "]=" + ((Registrant)mReadyRegistrants.get(i)).getHandler());
        }
        pw.println(" mPinLockedRegistrants: size=" + mPinLockedRegistrants.size());
        for (int i = 0; i < mPinLockedRegistrants.size(); i++) {
            pw.println("  mPinLockedRegistrants[" + i + "]=" + ((Registrant)mPinLockedRegistrants.get(i)).getHandler());
        }
        pw.println(" mNetworkLockedRegistrants: size=" + mNetworkLockedRegistrants.size());
        for (int i = 0; i < mNetworkLockedRegistrants.size(); i++) {
            pw.println("  mNetworkLockedRegistrants[" + i + "]=" + ((Registrant)mNetworkLockedRegistrants.get(i)).getHandler());
        }
        pw.flush();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index d486743..b46f180 100644

//Synthetic comment -- @@ -26,6 +26,9 @@

import com.android.internal.telephony.CommandsInterface;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
* This class is responsible for keeping all knowledge about
* Universal Integrated Circuit Card (UICC), also know as SIM's,
//Synthetic comment -- @@ -218,4 +221,21 @@
private void log(String string) {
Rlog.d(LOG_TAG, string);
}

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("UiccController: " + this);
        pw.println(" mContext=" + mContext);
        pw.println(" mInstance=" + mInstance);
        pw.println(" mCi=" + mCi);
        pw.println(" mUiccCard=" + mUiccCard);
        pw.println(" mIccChangedRegistrants: size=" + mIccChangedRegistrants.size());
        for (int i = 0; i < mIccChangedRegistrants.size(); i++) {
            pw.println("  mIccChangedRegistrants[" + i + "]=" + ((Registrant)mIccChangedRegistrants.get(i)).getHandler());
        }
        pw.println();
        pw.flush();
        if (mUiccCard != null) {
            mUiccCard.dump(fd, pw, args);
        }
    }
}







