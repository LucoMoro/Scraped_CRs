/*Fix for data and roaming icons

- Enable data connected flag based on the data state.

- Do not display data icon in data connected state
if radio type is unknown

- Display data icon independent of voice service state
i.e even if voice network is unavailable

- Show cdma roaming indicators if either voice
or data is roaming.

- Cleanup roaming indicator default values.
Set default CDMA Eri index to roaming off

- Show service if either voice/data is in
service if ro.config.combined_signal is true

- Consider eri only when in service

- Add in service check to isCdmaEri.
roaming related information
in registration state will not be valid
during out of service.

CRs-Fixed: 285986, 303275, 326204, 326634, 333008,
           339297
Change-Id:I9380d19b7dc763ed43c8b8442150674e48d9d595*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index bbb90c8..dcf31ec 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
private int mWimaxSignal = 0;
private int mWimaxState = 0;
private int mWimaxExtraState = 0;
    private int mDataServiceState = ServiceState.STATE_OUT_OF_SERVICE;
// data connectivity (regardless of state, can we access the internet?)
// state of inet connection - 0 not connected, 100 connected
private boolean mConnected = false;
//Synthetic comment -- @@ -428,9 +428,19 @@
@Override
public void onServiceStateChanged(ServiceState state) {
if (DEBUG) {
                Slog.d(TAG, "onServiceStateChanged state=" + state);
}
mServiceState = state;
            if (SystemProperties.getBoolean("ro.config.combined_signal", true)) {
                /*
                 * if combined_signal is set to true only then consider data
                 * service state for signal display
                 */
                mDataServiceState = mServiceState.getDataState();
                if (DEBUG) {
                    Slog.d(TAG, "Combining data service state" + mDataServiceState + "for signal");
                }
            }
updateTelephonySignalStrength();
updateDataNetType();
updateDataIcon();
//Synthetic comment -- @@ -522,14 +532,18 @@
}

private final void updateTelephonySignalStrength() {
        if (!hasService() &&
                (mDataServiceState != ServiceState.STATE_IN_SERVICE)) {
            if (DEBUG) Slog.d(TAG, " No service");
mPhoneSignalIconId = R.drawable.stat_sys_signal_null;
mQSPhoneSignalIconId = R.drawable.ic_qs_signal_no_signal;
mDataSignalIconId = R.drawable.stat_sys_signal_null;
} else {
            if ((mSignalStrength == null) || (mServiceState == null)) {
                if (DEBUG) {
                    Slog.d(TAG, " Null object, mSignalStrength= " + mSignalStrength
                            + " mServiceState " + mServiceState);
                }
mPhoneSignalIconId = R.drawable.stat_sys_signal_null;
mQSPhoneSignalIconId = R.drawable.ic_qs_signal_no_signal;
mDataSignalIconId = R.drawable.stat_sys_signal_null;
//Synthetic comment -- @@ -547,20 +561,13 @@
mLastSignalLevel = iconLevel = mSignalStrength.getLevel();
}

                // Though mPhone is a Manager, this call is not an IPC
                if ((isCdma() && isCdmaEri()) || mPhone.isNetworkRoaming()) {
                    iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH_ROAMING[mInetCondition];
} else {
                    iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH[mInetCondition];
}

mPhoneSignalIconId = iconList[iconLevel];
mQSPhoneSignalIconId =
TelephonyIcons.QS_TELEPHONY_SIGNAL_STRENGTH[mInetCondition][iconLevel];
//Synthetic comment -- @@ -582,6 +589,9 @@
} else {
switch (mDataNetType) {
case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    if (DEBUG) {
                        Slog.e(TAG, "updateDataNetType NETWORK_TYPE_UNKNOWN");
                    }
if (!mShowAtLeastThreeGees) {
mDataIconList = TelephonyIcons.DATA_G[mInetCondition];
mDataTypeIconId = 0;
//Synthetic comment -- @@ -668,7 +678,7 @@
mContentDescriptionDataType = mContext.getString(
R.string.accessibility_data_connection_4g);
break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
if (!mShowAtLeastThreeGees) {
mDataIconList = TelephonyIcons.DATA_G[mInetCondition];
mDataTypeIconId = R.drawable.stat_sys_data_connected_g;
//Synthetic comment -- @@ -683,6 +693,13 @@
R.string.accessibility_data_connection_3g);
}
break;
                default:
                    if (DEBUG) {
                        Slog.e(TAG, "updateDataNetType unknown radio:" + mDataNetType);
                    }
                    mDataNetType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
                    mDataTypeIconId = 0;
                    break;
}
}

//Synthetic comment -- @@ -698,7 +715,8 @@
}

boolean isCdmaEri() {
        if ((mServiceState != null)
                && (hasService() || (mDataServiceState == ServiceState.STATE_IN_SERVICE))) {
final int iconIndex = mServiceState.getCdmaEriIconIndex();
if (iconIndex != EriInfo.ROAMING_INDICATOR_OFF) {
final int iconMode = mServiceState.getCdmaEriIconMode();
//Synthetic comment -- @@ -712,14 +730,16 @@
}

private final void updateDataIcon() {
        int iconId = 0;
boolean visible = true;
        if (mDataNetType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            // If data network type is unknown do not display data icon
            visible = false;
        } else if (!isCdma()) {
// GSM case, we have to check also the sim state
if (mSimState == IccCardConstants.State.READY ||
mSimState == IccCardConstants.State.UNKNOWN) {
                if (mDataState == TelephonyManager.DATA_CONNECTED) {
switch (mDataActivity) {
case TelephonyManager.DATA_ACTIVITY_IN:
iconId = mDataIconList[1];
//Synthetic comment -- @@ -745,7 +765,7 @@
}
} else {
// CDMA case, mDataActivity can be also DATA_ACTIVITY_DORMANT
            if (mDataState == TelephonyManager.DATA_CONNECTED) {
switch (mDataActivity) {
case TelephonyManager.DATA_ACTIVITY_IN:
iconId = mDataIconList[1];








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index a9a5e90..80baffd 100644

//Synthetic comment -- @@ -123,6 +123,7 @@
public static final int REGISTRATION_STATE_ROAMING = 5;

private int mState = STATE_OUT_OF_SERVICE;
    private int mDataState = STATE_OUT_OF_SERVICE;
private boolean mRoaming;
private String mOperatorAlphaLong;
private String mOperatorAlphaShort;
//Synthetic comment -- @@ -138,7 +139,7 @@
private int mSystemId;
private int mCdmaRoamingIndicator;
private int mCdmaDefaultRoamingIndicator;
    private int mCdmaEriIconIndex = 1; //EriInfo.ROAMING_INDICATOR_OFF;
private int mCdmaEriIconMode;

/**
//Synthetic comment -- @@ -189,6 +190,7 @@
mCdmaEriIconIndex = s.mCdmaEriIconIndex;
mCdmaEriIconMode = s.mCdmaEriIconMode;
mIsEmergencyOnly = s.mIsEmergencyOnly;
        mDataState = s.mDataState;
}

/**
//Synthetic comment -- @@ -210,6 +212,7 @@
mCdmaEriIconIndex = in.readInt();
mCdmaEriIconMode = in.readInt();
mIsEmergencyOnly = in.readInt() != 0;
        mDataState = in.readInt();
}

public void writeToParcel(Parcel out, int flags) {
//Synthetic comment -- @@ -228,6 +231,7 @@
out.writeInt(mCdmaEriIconIndex);
out.writeInt(mCdmaEriIconMode);
out.writeInt(mIsEmergencyOnly ? 1 : 0);
        out.writeInt(mDataState);
}

public int describeContents() {
//Synthetic comment -- @@ -258,6 +262,19 @@
}

/**
     * Get current data service state of phone
     *
     * @see #STATE_IN_SERVICE
     * @see #STATE_OUT_OF_SERVICE
     * @see #STATE_EMERGENCY_ONLY
     * @see #STATE_POWER_OFF
     * @hide
     */
    public int getDataState() {
        return mDataState;
    }

    /**
* Get current roaming indicator of phone
* (note: not just decoding from TS 27.007 7.2)
*
//Synthetic comment -- @@ -392,7 +409,8 @@
&& equalsHandlesNulls(mCdmaRoamingIndicator, s.mCdmaRoamingIndicator)
&& equalsHandlesNulls(mCdmaDefaultRoamingIndicator,
s.mCdmaDefaultRoamingIndicator)
                && mIsEmergencyOnly == s.mIsEmergencyOnly
                && mDataState== s.mDataState);
}

/**
//Synthetic comment -- @@ -479,6 +497,7 @@
+ " " + (mCssIndicator ? "CSS supported" : "CSS not supported")
+ " " + mNetworkId
+ " " + mSystemId
                + " mDataState=" + mDataState
+ " RoamInd=" + mCdmaRoamingIndicator
+ " DefRoamInd=" + mCdmaDefaultRoamingIndicator
+ " EmergOnly=" + mIsEmergencyOnly);
//Synthetic comment -- @@ -486,6 +505,7 @@

private void setNullState(int state) {
mState = state;
        mDataState = state;
mRoaming = false;
mOperatorAlphaLong = null;
mOperatorAlphaShort = null;
//Synthetic comment -- @@ -514,6 +534,11 @@
mState = state;
}

    /** @hide */
    public void setDataState(int dataState) {
        mDataState = dataState;
    }

public void setRoaming(boolean roaming) {
mRoaming = roaming;
}







