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
    //Use int max, as -1 is a valid value in signal strength
    public static final int INVALID = 0x7FFFFFFF;

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
        mLteSignalStrength = 99;
        mLteRsrp = INVALID;
        mLteRsrq = INVALID;
        mLteRssnr = INVALID;
        mLteCqi = INVALID;
isGsm = true;
}

/**
     * This constructor is used to create SignalStrength with default
     * values and set the isGsmFlag with the value passed in the input
     *
     * @param gsmFlag true if Gsm Phone,false if Cdma phone
     * @return newly created SignalStrength
     * @hide
     */
    public SignalStrength(boolean gsmFlag) {
        mGsmSignalStrength = 99;
        mGsmBitErrorRate = -1;
        mCdmaDbm = -1;
        mCdmaEcio = -1;
        mEvdoDbm = -1;
        mEvdoEcio = -1;
        mEvdoSnr = -1;
        mLteSignalStrength = 99;
        mLteRsrp = INVALID;
        mLteRsrq = INVALID;
        mLteRssnr = INVALID;
        mLteCqi = INVALID;
        isGsm = gsmFlag;
    }

    /**
* Constructor
*
* @hide
//Synthetic comment -- @@ -138,9 +162,8 @@
int cdmaDbm, int cdmaEcio,
int evdoDbm, int evdoEcio, int evdoSnr,
boolean gsm) {
         this(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99,
                INVALID, INVALID, INVALID, INVALID, gsm);
}

/**
//Synthetic comment -- @@ -179,19 +202,21 @@
* @hide
*/
public SignalStrength(Parcel in) {
        if (DBG) log("Size of signalstrength parcel:" + in.dataSize());

        if (in.dataAvail() > 0) mGsmSignalStrength = in.readInt();
        if (in.dataAvail() > 0) mGsmBitErrorRate = in.readInt();
        if (in.dataAvail() > 0) mCdmaDbm = in.readInt();
        if (in.dataAvail() > 0) mCdmaEcio = in.readInt();
        if (in.dataAvail() > 0) mEvdoDbm = in.readInt();
        if (in.dataAvail() > 0) mEvdoEcio = in.readInt();
        if (in.dataAvail() > 0) mEvdoSnr = in.readInt();
        if (in.dataAvail() > 0) mLteSignalStrength = in.readInt();
        if (in.dataAvail() > 0) mLteRsrp = in.readInt();
        if (in.dataAvail() > 0) mLteRsrq = in.readInt();
        if (in.dataAvail() > 0) mLteRssnr = in.readInt();
        if (in.dataAvail() > 0) mLteCqi = in.readInt();
        if (in.dataAvail() > 0) isGsm = (in.readInt() != 0);
}

/**
//Synthetic comment -- @@ -236,7 +261,54 @@
};

/**
     * Validate the individual signal strength fields as per the range
     * specified in ril.h
     * Set to invalid any field that is not in the valid range
     * Cdma, evdo, lte rsrp & rsrq values are sign converted
     * when received from ril interface
     *
     * @return
     *      Valid values for all signalstrength fields
     * @hide
     */
    public void validateInput() {
        if (DBG) log("Signal before validate=" + this);
        // TS 27.007 8.5
        mGsmSignalStrength = mGsmSignalStrength >= 0 ? mGsmSignalStrength : 99;
        // BER no change;

        mCdmaDbm = mCdmaDbm > 0 ? -mCdmaDbm : -120;
        mCdmaEcio = (mCdmaEcio > 0) ? -mCdmaEcio : -160;

        mEvdoDbm = (mEvdoDbm > 0) ? -mEvdoDbm : -120;
        mEvdoEcio = (mEvdoEcio > 0) ? -mEvdoEcio : -1;
        mEvdoSnr = ((mEvdoSnr > 0) && (mEvdoSnr <= 8)) ? mEvdoSnr : -1;

        // TS 36.214 Physical Layer Section 5.1.3, TS 36.331 RRC
        mLteSignalStrength = (mLteSignalStrength >= 0) ? mLteSignalStrength : 99;
        mLteRsrp = ((mLteRsrp >= 44) && (mLteRsrp <= 140)) ? -mLteRsrp : SignalStrength.INVALID;
        mLteRsrq = ((mLteRsrq >= 3) && (mLteRsrq <= 20)) ? -mLteRsrq : SignalStrength.INVALID;
        mLteRssnr = ((mLteRssnr >= -200) && (mLteRssnr <= 300)) ? mLteRssnr
                : SignalStrength.INVALID;
        // Cqi no change
        if (DBG) log("Signal after validate=" + this);
    }

    /**
     * @param true - Gsm, Lte phones
     *        false - Cdma phones
     *
     * Used by voice phone to set the isGsm
     *        flag
     * @hide
     */
    public void setGsm(boolean gsmFlag) {
        isGsm = gsmFlag;
    }

    /**
     * Get the GSM Signal Strength, valid values are (0-31, 99) as defined in TS
     * 27.007 8.5
*/
public int getGsmSignalStrength() {
return this.mGsmSignalStrength;
//Synthetic comment -- @@ -293,25 +365,19 @@
int level;

if (isGsm) {
            level = getLteLevel();
            if (level == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
level = getGsmLevel();
}
} else {
int cdmaLevel = getCdmaLevel();
int evdoLevel = getEvdoLevel();
if (evdoLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
/* We don't know evdo, use cdma */
                level = cdmaLevel;
} else if (cdmaLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
/* We don't know cdma, use evdo */
                level = evdoLevel;
} else {
/* We know both, use the lowest level */
level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
//Synthetic comment -- @@ -329,10 +395,7 @@
public int getAsuLevel() {
int asuLevel;
if (isGsm) {
            if (getLteLevel() == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
asuLevel = getGsmAsuLevel();
} else {
asuLevel = getLteAsuLevel();
//Synthetic comment -- @@ -364,16 +427,17 @@
int dBm;

if(isGsm()) {
            if (getLteLevel() == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
dBm = getGsmDbm();
} else {
dBm = getLteDbm();
}
} else {
            int cdmaDbm = getCdmaDbm();
            int evdoDbm = getEvdoDbm();

            return (evdoDbm == -120) ? cdmaDbm : ((cdmaDbm == -120) ? evdoDbm
                    : (cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm));
}
if (DBG) log("getDbm=" + dBm);
return dBm;
//Synthetic comment -- @@ -568,34 +632,63 @@
* @hide
*/
public int getLteLevel() {
        /*
         * TS 36.214 Physical Layer Section 5.1.3 TS 36.331 RRC RSSI = received
         * signal + noise RSRP = reference signal dBm RSRQ = quality of signal
         * dB= Number of Resource blocksxRSRP/RSSI SNR = gain=signal/noise ratio
         * = -10log P1/P2 dB
         */
        int rssiIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN, rsrpIconLevel = -1, snrIconLevel = -1;

        if (mLteRsrp > -44) rsrpIconLevel = -1;
        else if (mLteRsrp >= -85) rsrpIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteRsrp >= -95) rsrpIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteRsrp >= -105) rsrpIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteRsrp >= -115) rsrpIconLevel = SIGNAL_STRENGTH_POOR;
        else if (mLteRsrp >= -140) rsrpIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        /*
         * Values are -200 dB to +300 (SNR*10dB) RS_SNR >= 13.0 dB =>4 bars 4.5
         * dB <= RS_SNR < 13.0 dB => 3 bars 1.0 dB <= RS_SNR < 4.5 dB => 2 bars
         * -3.0 dB <= RS_SNR < 1.0 dB 1 bar RS_SNR < -3.0 dB/No Service Antenna
         * Icon Only
         */
        if (mLteRssnr > 300) snrIconLevel = -1;
        else if (mLteRssnr >= 130) snrIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteRssnr >= 45) snrIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteRssnr >= 10) snrIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteRssnr >= -30) snrIconLevel = SIGNAL_STRENGTH_POOR;
        else if (mLteRssnr >= -200)
            snrIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        if (DBG) log("getLTELevel - rsrp:" + mLteRsrp + " snr:" + mLteRssnr + " rsrpIconLevel:"
                + rsrpIconLevel + " snrIconLevel:" + snrIconLevel);

        /* Choose a measurement type to use for notification */
        if (snrIconLevel != -1 && rsrpIconLevel != -1) {
            /*
             * The number of bars displayed shall be the smaller of the bars
             * associated with LTE RSRP and the bars associated with the LTE
             * RS_SNR
             */
            return (rsrpIconLevel < snrIconLevel ? rsrpIconLevel : snrIconLevel);
        }

        if (snrIconLevel != -1) return snrIconLevel;

        if (rsrpIconLevel != -1) return rsrpIconLevel;

        /* Valid values are (0-63, 99) as defined in TS 36.331 */
        if (mLteSignalStrength > 63) rssiIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else if (mLteSignalStrength >= 12) rssiIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteSignalStrength >= 8) rssiIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteSignalStrength >= 5) rssiIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteSignalStrength >= 0) rssiIconLevel = SIGNAL_STRENGTH_POOR;
        if (DBG) log("getLTELevel - rssi:" + mLteSignalStrength + " rssiIconLevel:"
                + rssiIconLevel);
        return rssiIconLevel;

}
/**
* Get the LTE signal level as an asu value between 0..97, 99 is unknown
* Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
//Synthetic comment -- @@ -605,8 +698,20 @@
public int getLteAsuLevel() {
int lteAsuLevel = 99;
int lteDbm = getLteDbm();
        /*
         * 3GPP 27.007 (Ver 10.3.0) Sec 8.69
         * 0   -140 dBm or less
         * 1   -139 dBm
         * 2...96  -138... -44 dBm
         * 97  -43 dBm or greater
         * 255 not known or not detectable
         */
        /*
         * validateInput will always give a valid range between -140 t0 -44 as
         * per ril.h. so RSRP >= -43 & <-140 will fall under asu level 255
         * and not 97 or 0
         */
        if (lteDbm == SignalStrength.INVALID) lteAsuLevel = 255;
else lteAsuLevel = lteDbm + 140;
if (DBG) log("Lte Asu level: "+lteAsuLevel);
return lteAsuLevel;







