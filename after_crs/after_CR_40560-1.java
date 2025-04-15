/*Telephony: Signal Strength cleanup

Make signal strength parsing common for all modes

Change-Id:I41bce658c536dc30558224c8ca76d6d70afb78ee*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
old mode 100644
new mode 100755
//Synthetic comment -- index b55240a..a69c0fd

//Synthetic comment -- @@ -563,6 +563,11 @@
mNotifier.notifyServiceState(this);
}

    /* package */void
    notifySignalStrength() {
        mNotifier.notifySignalStrength(this);
    }

// Inherited documentation suffices.
public SimulatedRadioControl getSimulatedRadioControl() {
return mSimulatedRadioControl;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
old mode 100644
new mode 100755
//Synthetic comment -- index b14f6c8..c359652

//Synthetic comment -- @@ -43,6 +43,7 @@
import android.os.PowerManager.WakeLock;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
//Synthetic comment -- @@ -3347,16 +3348,8 @@

private Object
responseSignalStrength(Parcel p) {
        SignalStrength signalStrength = new SignalStrength(p);
        return signalStrength;
}

private ArrayList<CdmaInformationRecords>








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
old mode 100644
new mode 100755
//Synthetic comment -- index e4cfb23..479b075

//Synthetic comment -- @@ -454,6 +454,38 @@
}

/**
     * send signal-strength-changed notification if changed Called both for
     * solicited and unsolicited signal strength updates
     */
    protected void onSignalStrengthResult(AsyncResult ar, PhoneBase phone, boolean isGsm) {
        SignalStrength oldSignalStrength = mSignalStrength;

        // This signal is used for both voice and data radio signal so parse
        // all fields

        if ((ar.exception == null) && (ar.result != null)) {
            mSignalStrength = (SignalStrength) ar.result;
            mSignalStrength.validateInput();
            mSignalStrength.setGsm(isGsm);
        } else {
            log("onSignalStrengthResult() Exception from RIL : " + ar.exception);
            mSignalStrength = new SignalStrength(isGsm);
        }

        if (!mSignalStrength.equals(oldSignalStrength)) {
            try {
                // This takes care of delayed EVENT_POLL_SIGNAL_STRENGTH
                // (scheduled after POLL_PERIOD_MILLIS) during Radio Technology
                // Change)
                phone.notifySignalStrength();
            } catch (NullPointerException ex) {
                log("onSignalStrengthResult() Phone already destroyed: " + ex
                        + "SignalStrength not notified");
            }
        }
    }

    /**
* Hang up all voice call and turn off radio. Implemented by derived class.
*/
protected abstract void hangupAndPowerOff();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 26ef9dc..77f3c50 100755

//Synthetic comment -- @@ -356,11 +356,6 @@
return ret;
}

public Connection
dial (String dialString) throws CallStateException {
// Need to make sure dialString gets parsed properly








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0c5c342..c2bf791

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony.cdma;

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.EventLogTags;
//Synthetic comment -- @@ -127,13 +128,6 @@
}

@Override
protected void pollState() {
pollingContext = new int[1];
pollingContext[0] = 0;
//Synthetic comment -- @@ -441,53 +435,11 @@
}

@Override
    protected void onSignalStrengthResult(AsyncResult ar, PhoneBase phone, boolean isGsm) {
        if (mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) {
            isGsm = true;
}
        super.onSignalStrengthResult(ar, phone, isGsm);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 5a4af7a..67e499b 100755

//Synthetic comment -- @@ -338,7 +338,7 @@
return;
}
ar = (AsyncResult) msg.obj;
            onSignalStrengthResult(ar, phone, false);
queueNextSignalStrengthPoll();

break;
//Synthetic comment -- @@ -448,7 +448,7 @@
// so we don't have to ask it.
dontPollSignalStrength = true;

            onSignalStrengthResult(ar, phone, false);
break;

case EVENT_RUIM_RECORDS_LOADED:
//Synthetic comment -- @@ -815,7 +815,7 @@
}

protected void setSignalStrengthDefaultValues() {
        mSignalStrength = new SignalStrength( false);
}

/**
//Synthetic comment -- @@ -1135,41 +1135,6 @@
sendMessageDelayed(msg, POLL_PERIOD_MILLIS);
}

protected int radioTechnologyToDataServiceState(int code) {
int retVal = ServiceState.STATE_OUT_OF_SERVICE;
switch(code) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 8c5368e..10ce24a 100644

//Synthetic comment -- @@ -394,11 +394,6 @@
mNotifier.notifyCellLocation(this);
}

public void
notifyCallForwardingIndicator() {
mNotifier.notifyCallForwardingChanged(this);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
old mode 100644
new mode 100755
//Synthetic comment -- index 808ec2b..2a77cba

//Synthetic comment -- @@ -327,7 +327,7 @@
return;
}
ar = (AsyncResult) msg.obj;
                onSignalStrengthResult(ar, phone, true);
queueNextSignalStrengthPoll();

break;
//Synthetic comment -- @@ -394,7 +394,7 @@
// we don't have to ask it
dontPollSignalStrength = true;

                onSignalStrengthResult(ar, phone, true);
break;

case EVENT_SIM_RECORDS_LOADED:
//Synthetic comment -- @@ -683,9 +683,7 @@
}

private void setSignalStrengthDefaultValues() {
        mSignalStrength = new SignalStrength(true);
}

/**
//Synthetic comment -- @@ -1083,54 +1081,6 @@
}

/**
* Set restricted state based on the OnRestrictedStateChanged notification
* If any voice or packet restricted state changes, trigger a UI
* notification and notify registrants when sim is ready.







