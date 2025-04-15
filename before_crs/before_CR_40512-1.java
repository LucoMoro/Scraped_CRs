/*Telephony: Signal Strength cleanup & LTE support

Make signal strength parsing common for all modes

Add Support to calculate LTE
level for UI signal bar display

Update the lowest of Evdo/ CDMA dbm value
in phone status instead of using only CDMA dbm

Change-Id:I41bce658c536dc30558224c8ca76d6d70afb78ee*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SignalStrength.java b/telephony/java/android/telephony/SignalStrength.java
old mode 100644
new mode 100755
//Synthetic comment -- index 1049669..184e389

//Synthetic comment -- @@ -48,7 +48,8 @@
};

/** @hide */
    public static final int INVALID_SNR = 0x7FFFFFFF;

private int mGsmSignalStrength; // Valid values are (0-31, 99) as defined in TS 27.007 8.5
private int mGsmBitErrorRate;   // bit error rate (0-7, 99) as defined in TS 27.007 8.5
//Synthetic comment -- @@ -64,7 +65,6 @@
private int mLteCqi;

private boolean isGsm; // This value is set by the ServiceStateTracker onSignalStrengthResult

/**
* Create a new SignalStrength from a intent notifier Bundle
*
//Synthetic comment -- @@ -96,15 +96,39 @@
mEvdoDbm = -1;
mEvdoEcio = -1;
mEvdoSnr = -1;
        mLteSignalStrength = -1;
        mLteRsrp = -1;
        mLteRsrq = -1;
        mLteRssnr = INVALID_SNR;
        mLteCqi = -1;
isGsm = true;
}

/**
* Constructor
*
* @hide
//Synthetic comment -- @@ -138,9 +162,8 @@
int cdmaDbm, int cdmaEcio,
int evdoDbm, int evdoEcio, int evdoSnr,
boolean gsm) {
        this(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio,
                evdoDbm, evdoEcio, evdoSnr, -1, -1,
                -1, INVALID_SNR, -1, gsm);
}

/**
//Synthetic comment -- @@ -179,19 +202,21 @@
* @hide
*/
public SignalStrength(Parcel in) {
        mGsmSignalStrength = in.readInt();
        mGsmBitErrorRate = in.readInt();
        mCdmaDbm = in.readInt();
        mCdmaEcio = in.readInt();
        mEvdoDbm = in.readInt();
        mEvdoEcio = in.readInt();
        mEvdoSnr = in.readInt();
        mLteSignalStrength = in.readInt();
        mLteRsrp = in.readInt();
        mLteRsrq = in.readInt();
        mLteRssnr = in.readInt();
        mLteCqi = in.readInt();
        isGsm = (in.readInt() != 0);
}

/**
//Synthetic comment -- @@ -236,7 +261,54 @@
};

/**
     * Get the GSM Signal Strength, valid values are (0-31, 99) as defined in TS 27.007 8.5
*/
public int getGsmSignalStrength() {
return this.mGsmSignalStrength;
//Synthetic comment -- @@ -293,25 +365,19 @@
int level;

if (isGsm) {
            // TODO Need solve the discrepancy of invalid values between
            // RIL_LTE_SignalStrength and here.
            if ((mLteSignalStrength == -1)
                    && (mLteRsrp == -1)
                    && (mLteRsrq == -1)
                    && (mLteCqi == -1)) {
level = getGsmLevel();
            } else {
                level = getLteLevel();
}
} else {
int cdmaLevel = getCdmaLevel();
int evdoLevel = getEvdoLevel();
if (evdoLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
/* We don't know evdo, use cdma */
                level = getCdmaLevel();
} else if (cdmaLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
/* We don't know cdma, use evdo */
                level = getEvdoLevel();
} else {
/* We know both, use the lowest level */
level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
//Synthetic comment -- @@ -329,10 +395,7 @@
public int getAsuLevel() {
int asuLevel;
if (isGsm) {
            if ((mLteSignalStrength == -1)
                    && (mLteRsrp == -1)
                    && (mLteRsrq == -1)
                    && (mLteCqi == -1)) {
asuLevel = getGsmAsuLevel();
} else {
asuLevel = getLteAsuLevel();
//Synthetic comment -- @@ -364,16 +427,17 @@
int dBm;

if(isGsm()) {
            if ((mLteSignalStrength == -1)
                    && (mLteRsrp == -1)
                    && (mLteRsrq == -1)
                    && (mLteCqi == -1)) {
dBm = getGsmDbm();
} else {
dBm = getLteDbm();
}
} else {
            dBm = getCdmaDbm();
}
if (DBG) log("getDbm=" + dBm);
return dBm;
//Synthetic comment -- @@ -568,34 +632,63 @@
* @hide
*/
public int getLteLevel() {
        int levelLteRsrp = 0;
        int levelLteRssnr = 0;

        if (mLteRsrp == -1) levelLteRsrp = 0;
        else if (mLteRsrp >= -95) levelLteRsrp = SIGNAL_STRENGTH_GREAT;
        else if (mLteRsrp >= -105) levelLteRsrp = SIGNAL_STRENGTH_GOOD;
        else if (mLteRsrp >= -115) levelLteRsrp = SIGNAL_STRENGTH_MODERATE;
        else levelLteRsrp = SIGNAL_STRENGTH_POOR;

        if (mLteRssnr == INVALID_SNR) levelLteRssnr = 0;
        else if (mLteRssnr >= 45) levelLteRssnr = SIGNAL_STRENGTH_GREAT;
        else if (mLteRssnr >= 10) levelLteRssnr = SIGNAL_STRENGTH_GOOD;
        else if (mLteRssnr >= -30) levelLteRssnr = SIGNAL_STRENGTH_MODERATE;
        else levelLteRssnr = SIGNAL_STRENGTH_POOR;

        int level;
        if (mLteRsrp == -1)
            level = levelLteRssnr;
        else if (mLteRssnr == INVALID_SNR)
            level = levelLteRsrp;
        else
            level = (levelLteRssnr < levelLteRsrp) ? levelLteRssnr : levelLteRsrp;

        if (DBG) log("Lte rsrp level: "+levelLteRsrp
                + " snr level: " + levelLteRssnr + " level: " + level);
        return level;
}

/**
* Get the LTE signal level as an asu value between 0..97, 99 is unknown
* Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
//Synthetic comment -- @@ -605,8 +698,20 @@
public int getLteAsuLevel() {
int lteAsuLevel = 99;
int lteDbm = getLteDbm();
        if (lteDbm <= -140) lteAsuLevel = 0;
        else if (lteDbm >= -43) lteAsuLevel = 97;
else lteAsuLevel = lteDbm + 140;
if (DBG) log("Lte Asu level: "+lteAsuLevel);
return lteAsuLevel;







