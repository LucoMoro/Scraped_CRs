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
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
//Synthetic comment -- @@ -3347,16 +3348,8 @@

private Object
responseSignalStrength(Parcel p) {
        int numInts = 12;
        int response[];

        /* TODO: Add SignalStrength class to match RIL_SignalStrength */
        response = new int[numInts];
        for (int i = 0 ; i < numInts ; i++) {
            response[i] = p.readInt();
        }

        return response;
}

private ArrayList<CdmaInformationRecords>








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
old mode 100644
new mode 100755
//Synthetic comment -- index e4cfb23..479b075

//Synthetic comment -- @@ -454,6 +454,38 @@
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

    /*package*/ void
    notifySignalStrength() {
        mNotifier.notifySignalStrength(this);
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

import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.EventLogTags;
//Synthetic comment -- @@ -127,13 +128,6 @@
}

@Override
    protected void setSignalStrengthDefaultValues() {
        // TODO Make a constructor only has boolean gsm as parameter
        mSignalStrength = new SignalStrength(99, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, SignalStrength.INVALID_SNR, -1, false);
    }

    @Override
protected void pollState() {
pollingContext = new int[1];
pollingContext[0] = 0;
//Synthetic comment -- @@ -441,53 +435,11 @@
}

@Override
    protected void onSignalStrengthResult(AsyncResult ar) {
        SignalStrength oldSignalStrength = mSignalStrength;

        if (ar.exception != null) {
            // Most likely radio is resetting/disconnected change to default
            // values.
            setSignalStrengthDefaultValues();
        } else {
            int[] ints = (int[])ar.result;

            int lteRssi = -1;
            int lteRsrp = -1;
            int lteRsrq = -1;
            int lteRssnr = SignalStrength.INVALID_SNR;
            int lteCqi = -1;

            int offset = 2;
            int cdmaDbm = (ints[offset] > 0) ? -ints[offset] : -120;
            int cdmaEcio = (ints[offset + 1] > 0) ? -ints[offset + 1] : -160;
            int evdoRssi = (ints[offset + 2] > 0) ? -ints[offset + 2] : -120;
            int evdoEcio = (ints[offset + 3] > 0) ? -ints[offset + 3] : -1;
            int evdoSnr = ((ints[offset + 4] > 0) && (ints[offset + 4] <= 8)) ? ints[offset + 4]
                    : -1;

            if (mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) {
                lteRssi = ints[offset+5];
                lteRsrp = ints[offset+6];
                lteRsrq = ints[offset+7];
                lteRssnr = ints[offset+8];
                lteCqi = ints[offset+9];
            }

            if (mRilRadioTechnology != ServiceState.RIL_RADIO_TECHNOLOGY_LTE) {
                mSignalStrength = new SignalStrength(99, -1, cdmaDbm, cdmaEcio, evdoRssi, evdoEcio,
                        evdoSnr, false);
            } else {
                mSignalStrength = new SignalStrength(99, -1, cdmaDbm, cdmaEcio, evdoRssi, evdoEcio,
                        evdoSnr, lteRssi, lteRsrp, lteRsrq, lteRssnr, lteCqi, true);
            }
}

        try {
            phone.notifySignalStrength();
        } catch (NullPointerException ex) {
            loge("onSignalStrengthResult() Phone already destroyed: " + ex
                    + "SignalStrength not notified");
        }
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 5a4af7a..67e499b 100755

//Synthetic comment -- @@ -338,7 +338,7 @@
return;
}
ar = (AsyncResult) msg.obj;
            onSignalStrengthResult(ar);
queueNextSignalStrengthPoll();

break;
//Synthetic comment -- @@ -448,7 +448,7 @@
// so we don't have to ask it.
dontPollSignalStrength = true;

            onSignalStrengthResult(ar);
break;

case EVENT_RUIM_RECORDS_LOADED:
//Synthetic comment -- @@ -815,7 +815,7 @@
}

protected void setSignalStrengthDefaultValues() {
        mSignalStrength = new SignalStrength(99, -1, -1, -1, -1, -1, -1, false);
}

/**
//Synthetic comment -- @@ -1135,41 +1135,6 @@
sendMessageDelayed(msg, POLL_PERIOD_MILLIS);
}

    /**
     *  send signal-strength-changed notification if changed
     *  Called both for solicited and unsolicited signal strength updates
     */
    protected void
    onSignalStrengthResult(AsyncResult ar) {
        SignalStrength oldSignalStrength = mSignalStrength;

        if (ar.exception != null) {
            // Most likely radio is resetting/disconnected change to default values.
            setSignalStrengthDefaultValues();
        } else {
            int[] ints = (int[])ar.result;
            int offset = 2;
            int cdmaDbm = (ints[offset] > 0) ? -ints[offset] : -120;
            int cdmaEcio = (ints[offset+1] > 0) ? -ints[offset+1] : -160;
            int evdoRssi = (ints[offset+2] > 0) ? -ints[offset+2] : -120;
            int evdoEcio = (ints[offset+3] > 0) ? -ints[offset+3] : -1;
            int evdoSnr  = ((ints[offset+4] > 0) && (ints[offset+4] <= 8)) ? ints[offset+4] : -1;

            //log(String.format("onSignalStrengthResult cdmaDbm=%d cdmaEcio=%d evdoRssi=%d evdoEcio=%d evdoSnr=%d",
            //        cdmaDbm, cdmaEcio, evdoRssi, evdoEcio, evdoSnr));
            mSignalStrength = new SignalStrength(99, -1, cdmaDbm, cdmaEcio,
                    evdoRssi, evdoEcio, evdoSnr, false);
        }

        try {
            phone.notifySignalStrength();
        } catch (NullPointerException ex) {
            loge("onSignalStrengthResult() Phone already destroyed: " + ex
                    + "SignalStrength not notified");
        }
    }


protected int radioTechnologyToDataServiceState(int code) {
int retVal = ServiceState.STATE_OUT_OF_SERVICE;
switch(code) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 8c5368e..10ce24a 100644

//Synthetic comment -- @@ -394,11 +394,6 @@
mNotifier.notifyCellLocation(this);
}

    /*package*/ void
    notifySignalStrength() {
        mNotifier.notifySignalStrength(this);
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
                onSignalStrengthResult(ar);
queueNextSignalStrengthPoll();

break;
//Synthetic comment -- @@ -394,7 +394,7 @@
// we don't have to ask it
dontPollSignalStrength = true;

                onSignalStrengthResult(ar);
break;

case EVENT_SIM_RECORDS_LOADED:
//Synthetic comment -- @@ -683,9 +683,7 @@
}

private void setSignalStrengthDefaultValues() {
        // TODO Make a constructor only has boolean gsm as parameter
        mSignalStrength = new SignalStrength(99, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, SignalStrength.INVALID_SNR, -1, true);
}

/**
//Synthetic comment -- @@ -1083,54 +1081,6 @@
}

/**
     *  Send signal-strength-changed notification if changed.
     *  Called both for solicited and unsolicited signal strength updates.
     */
    private void onSignalStrengthResult(AsyncResult ar) {
        SignalStrength oldSignalStrength = mSignalStrength;
        int rssi = 99;
        int lteSignalStrength = -1;
        int lteRsrp = -1;
        int lteRsrq = -1;
        int lteRssnr = SignalStrength.INVALID_SNR;
        int lteCqi = -1;

        if (ar.exception != null) {
            // -1 = unknown
            // most likely radio is resetting/disconnected
            setSignalStrengthDefaultValues();
        } else {
            int[] ints = (int[])ar.result;

            // bug 658816 seems to be a case where the result is 0-length
            if (ints.length != 0) {
                rssi = ints[0];
                lteSignalStrength = ints[7];
                lteRsrp = ints[8];
                lteRsrq = ints[9];
                lteRssnr = ints[10];
                lteCqi = ints[11];
            } else {
                loge("Bogus signal strength response");
                rssi = 99;
            }
        }

        mSignalStrength = new SignalStrength(rssi, -1, -1, -1,
                -1, -1, -1, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, true);

        if (!mSignalStrength.equals(oldSignalStrength)) {
            try { // This takes care of delayed EVENT_POLL_SIGNAL_STRENGTH (scheduled after
                  // POLL_PERIOD_MILLIS) during Radio Technology Change)
                phone.notifySignalStrength();
           } catch (NullPointerException ex) {
                log("onSignalStrengthResult() Phone already destroyed: " + ex
                        + "SignalStrength not notified");
           }
        }
    }

    /**
* Set restricted state based on the OnRestrictedStateChanged notification
* If any voice or packet restricted state changes, trigger a UI
* notification and notify registrants when sim is ready.







