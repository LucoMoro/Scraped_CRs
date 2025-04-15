/*Frameworks: TD-SCDMA Signal Strength changes

Changes for TD-SCDMA Signal Strength display.

Change-Id:I7dc4d3e01f31afb5a526c892bc4104e215403777*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index a9a5e90..ae06379 100644

//Synthetic comment -- @@ -105,6 +105,11 @@
* @hide
*/
public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;

/**
* Available registration states for GSM, UMTS and CDMA.
//Synthetic comment -- @@ -458,6 +463,9 @@
case RIL_RADIO_TECHNOLOGY_GSM:
rtString = "GSM";
break;
default:
rtString = "Unexpected";
Log.w(LOG_TAG, "Unexpected radioTechnology=" + rt);
//Synthetic comment -- @@ -687,6 +695,8 @@
return TelephonyManager.NETWORK_TYPE_LTE;
case ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP:
return TelephonyManager.NETWORK_TYPE_HSPAP;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;
}
//Synthetic comment -- @@ -717,7 +727,8 @@
|| radioTechnology == RIL_RADIO_TECHNOLOGY_HSPA
|| radioTechnology == RIL_RADIO_TECHNOLOGY_LTE
|| radioTechnology == RIL_RADIO_TECHNOLOGY_HSPAP
                || radioTechnology == RIL_RADIO_TECHNOLOGY_GSM;
}

/** @hide */








//Synthetic comment -- diff --git a/telephony/java/android/telephony/SignalStrength.java b/telephony/java/android/telephony/SignalStrength.java
//Synthetic comment -- index 92b889b..e80bea0 100755

//Synthetic comment -- @@ -1,6 +1,10 @@
/*
* Copyright (C) 2009 Qualcomm Innovation Center, Inc.  All Rights Reserved.
* Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -63,6 +67,7 @@
private int mLteRsrq;
private int mLteRssnr;
private int mLteCqi;

private boolean isGsm; // This value is set by the ServiceStateTracker onSignalStrengthResult
/**
//Synthetic comment -- @@ -101,6 +106,7 @@
mLteRsrq = INVALID;
mLteRssnr = INVALID;
mLteCqi = INVALID;
isGsm = true;
}

//Synthetic comment -- @@ -161,9 +167,24 @@
public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate,
int cdmaDbm, int cdmaEcio,
int evdoDbm, int evdoEcio, int evdoSnr,
boolean gsm) {
this(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99,
                INVALID, INVALID, INVALID, INVALID, gsm);
}

/**
//Synthetic comment -- @@ -193,6 +214,7 @@
mLteRsrq = s.mLteRsrq;
mLteRssnr = s.mLteRssnr;
mLteCqi = s.mLteCqi;
isGsm = s.isGsm;
}

//Synthetic comment -- @@ -214,6 +236,7 @@
mLteRsrq = in.readInt();
mLteRssnr = in.readInt();
mLteCqi = in.readInt();
isGsm = (in.readInt() != 0);
}

//Synthetic comment -- @@ -233,6 +256,7 @@
out.writeInt(mLteRsrq);
out.writeInt(mLteRssnr);
out.writeInt(mLteCqi);
out.writeInt(isGsm ? 1 : 0);
}

//Synthetic comment -- @@ -288,6 +312,10 @@
mLteRsrq = ((mLteRsrq >= 3) && (mLteRsrq <= 20)) ? -mLteRsrq : SignalStrength.INVALID;
mLteRssnr = ((mLteRssnr >= -200) && (mLteRssnr <= 300)) ? mLteRssnr
: SignalStrength.INVALID;
// Cqi no change
if (DBG) log("Signal after validate=" + this);
}
//Synthetic comment -- @@ -360,12 +388,15 @@
* @hide
*/
public int getLevel() {
        int level;

if (isGsm) {
level = getLteLevel();
if (level == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                level = getGsmLevel();
}
} else {
int cdmaLevel = getCdmaLevel();
//Synthetic comment -- @@ -391,10 +422,14 @@
* @hide
*/
public int getAsuLevel() {
        int asuLevel;
if (isGsm) {
if (getLteLevel() == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                asuLevel = getGsmAsuLevel();
} else {
asuLevel = getLteAsuLevel();
}
//Synthetic comment -- @@ -422,11 +457,15 @@
* @hide
*/
public int getDbm() {
        int dBm;

if(isGsm()) {
if (getLteLevel() == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                dBm = getGsmDbm();
} else {
dBm = getLteDbm();
}
//Synthetic comment -- @@ -723,6 +762,54 @@
}

/**
* @return hash code
*/
@Override
//Synthetic comment -- @@ -734,7 +821,7 @@
+ (mEvdoDbm * primeNum) + (mEvdoEcio * primeNum) + (mEvdoSnr * primeNum)
+ (mLteSignalStrength * primeNum) + (mLteRsrp * primeNum)
+ (mLteRsrq * primeNum) + (mLteRssnr * primeNum) + (mLteCqi * primeNum)
                + (isGsm ? 1 : 0));
}

/**
//Synthetic comment -- @@ -766,6 +853,7 @@
&& mLteRsrq == s.mLteRsrq
&& mLteRssnr == s.mLteRssnr
&& mLteCqi == s.mLteCqi
&& isGsm == s.isGsm);
}

//Synthetic comment -- @@ -787,6 +875,7 @@
+ " " + mLteRsrq
+ " " + mLteRssnr
+ " " + mLteCqi
+ " " + (isGsm ? "gsm|lte" : "cdma"));
}

//Synthetic comment -- @@ -809,6 +898,7 @@
mLteRsrq = m.getInt("LteRsrq");
mLteRssnr = m.getInt("LteRssnr");
mLteCqi = m.getInt("LteCqi");
isGsm = m.getBoolean("isGsm");
}

//Synthetic comment -- @@ -831,6 +921,7 @@
m.putInt("LteRsrq", mLteRsrq);
m.putInt("LteRssnr", mLteRssnr);
m.putInt("LteCqi", mLteCqi);
m.putBoolean("isGsm", Boolean.valueOf(isGsm));
}









//Synthetic comment -- diff --git a/telephony/java/android/telephony/TelephonyManager.java b/telephony/java/android/telephony/TelephonyManager.java
//Synthetic comment -- index fa4b7cd..0ca0f41 100755

//Synthetic comment -- @@ -554,6 +554,8 @@
public static final int NETWORK_TYPE_EHRPD = 14;
/** Current network is HSPA+ */
public static final int NETWORK_TYPE_HSPAP = 15;

/**
* Returns a constant indicating the radio technology (network type)
//Synthetic comment -- @@ -576,6 +578,7 @@
* @see #NETWORK_TYPE_LTE
* @see #NETWORK_TYPE_EHRPD
* @see #NETWORK_TYPE_HSPAP
*/
public int getNetworkType() {
try{
//Synthetic comment -- @@ -627,6 +630,7 @@
case NETWORK_TYPE_EVDO_B:
case NETWORK_TYPE_EHRPD:
case NETWORK_TYPE_HSPAP:
return NETWORK_CLASS_3_G;
case NETWORK_TYPE_LTE:
return NETWORK_CLASS_4_G;
//Synthetic comment -- @@ -679,6 +683,8 @@
return "iDEN";
case NETWORK_TYPE_HSPAP:
return "HSPA+";
default:
return "UNKNOWN";
}







