/*phone : clean up radio technology constants

Change-Id:I73379a7cdd2fdca594a544db1a4fc135459d068c*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5078c69..71f09cc 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;
//Synthetic comment -- @@ -478,8 +479,7 @@
cdmaIconLevel = (levelDbm < levelEcio) ? levelDbm : levelEcio;

if (mServiceState != null &&
                  RadioTechnology.isEvdo(mServiceState.getRadioTechnology())) {
int evdoEcio = signalStrength.getEvdoEcio();
int evdoSnr = signalStrength.getEvdoSnr();
int levelEvdoEcio = 0;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..9e150c0 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.os.Message;
import android.os.ServiceManager;
import android.telephony.NeighboringCellInfo;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
//Synthetic comment -- @@ -694,28 +695,28 @@
public int getNetworkType() {
int radiotech = mPhone.getServiceState().getRadioTechnology();
switch(radiotech) {
            case RadioTechnology.RADIO_TECH_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
            case RadioTechnology.RADIO_TECH_EDGE:
return TelephonyManager.NETWORK_TYPE_EDGE;
            case RadioTechnology.RADIO_TECH_UMTS:
return TelephonyManager.NETWORK_TYPE_UMTS;
            case RadioTechnology.RADIO_TECH_HSDPA:
return TelephonyManager.NETWORK_TYPE_HSDPA;
            case RadioTechnology.RADIO_TECH_HSUPA:
return TelephonyManager.NETWORK_TYPE_HSUPA;
            case RadioTechnology.RADIO_TECH_HSPA:
return TelephonyManager.NETWORK_TYPE_HSPA;
            case RadioTechnology.RADIO_TECH_IS95A:
            case RadioTechnology.RADIO_TECH_IS95B:
return TelephonyManager.NETWORK_TYPE_CDMA;
            case RadioTechnology.RADIO_TECH_1xRTT:
return TelephonyManager.NETWORK_TYPE_1xRTT;
            case RadioTechnology.RADIO_TECH_EVDO_0:
return TelephonyManager.NETWORK_TYPE_EVDO_0;
            case RadioTechnology.RADIO_TECH_EVDO_A:
return TelephonyManager.NETWORK_TYPE_EVDO_A;
            case RadioTechnology.RADIO_TECH_EVDO_B:
return TelephonyManager.NETWORK_TYPE_EVDO_B;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;







