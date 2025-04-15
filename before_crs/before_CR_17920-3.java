/*Telephony: Process PIN2 blocked and PIN2 permanently blocked states

Change-Id:I0652f38d874d55f01de2bbaad597c284f82778d9*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index e270ce9..2efd561 100644

//Synthetic comment -- @@ -28,6 +28,7 @@

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;

/**
* {@hide}
//Synthetic comment -- @@ -49,7 +50,6 @@
private boolean mIccFdnEnabled = false; // Default to disabled.
// Will be updated when SIM_READY.


/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
static public final String INTENT_KEY_ICC_STATE = "ss";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
//Synthetic comment -- @@ -621,24 +621,7 @@
currentRadioState == RadioState.RUIM_LOCKED_OR_ABSENT ||
currentRadioState == RadioState.RUIM_READY) {

            int index;

            // check for CDMA radio technology
            if (currentRadioState == RadioState.RUIM_LOCKED_OR_ABSENT ||
                currentRadioState == RadioState.RUIM_READY) {
                index = mIccCardStatus.getCdmaSubscriptionAppIndex();
            }
            else {
                index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
            }

            IccCardApplication app;
            if (index >= 0 && index < IccCardStatus.CARD_MAX_APPS) {
                app = mIccCardStatus.getApplication(index);
            } else {
                Log.e(mLogTag, "[IccCard] Invalid Subscription Application index:" + index);
                return IccCard.State.ABSENT;
            }

if (app == null) {
Log.e(mLogTag, "[IccCard] Subscription Application in not present");
//Synthetic comment -- @@ -692,6 +675,44 @@
}
}

private void log(String msg) {
Log.d(mLogTag, "[IccCard] " + msg);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardApplication.java b/telephony/java/com/android/internal/telephony/IccCardApplication.java
//Synthetic comment -- index 9f60a6c..0a3e60a 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;


/**
* See also RIL_AppStatus in include/telephony/ril.h
//Synthetic comment -- @@ -103,8 +104,8 @@
public String         app_label;
// applicable to USIM and CSIM
public int            pin1_replaced;
    public int            pin1;
    public int            pin2;

AppType AppTypeFromRILInt(int type) {
AppType newType;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardStatus.java b/telephony/java/com/android/internal/telephony/IccCardStatus.java
//Synthetic comment -- index 0e7bad7..2ea834f 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
}
};

public enum PinState {
PINSTATE_UNKNOWN,
PINSTATE_ENABLED_NOT_VERIFIED,








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index e059555..aae9b72 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;

//Synthetic comment -- @@ -2891,8 +2892,8 @@
ca.aid            = p.readString();
ca.app_label      = p.readString();
ca.pin1_replaced  = p.readInt();
            ca.pin1           = p.readInt();
            ca.pin2           = p.readInt();
status.addApplication(ca);
}
return status;







