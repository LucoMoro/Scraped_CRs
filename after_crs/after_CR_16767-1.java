/*phone : clean up radio technology constants

Change-Id:I73379a7cdd2fdca594a544db1a4fc135459d068c*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5078c69..76fb5e1 100644

//Synthetic comment -- @@ -478,8 +478,7 @@
cdmaIconLevel = (levelDbm < levelEcio) ? levelDbm : levelEcio;

if (mServiceState != null &&
                  (mServiceState.getRadioTechnology().isEvdo())) {
int evdoEcio = signalStrength.getEvdoEcio();
int evdoSnr = signalStrength.getEvdoSnr();
int levelEvdoEcio = 0;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..6f8a734 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.os.Message;
import android.os.ServiceManager;
import android.telephony.NeighboringCellInfo;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
//Synthetic comment -- @@ -692,30 +693,30 @@
* Returns the network type
*/
public int getNetworkType() {
        RadioTechnology radiotech = mPhone.getServiceState().getRadioTechnology();
switch(radiotech) {
            case RADIO_TECH_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
            case RADIO_TECH_EDGE:
return TelephonyManager.NETWORK_TYPE_EDGE;
            case RADIO_TECH_UMTS:
return TelephonyManager.NETWORK_TYPE_UMTS;
            case RADIO_TECH_HSDPA:
return TelephonyManager.NETWORK_TYPE_HSDPA;
            case RADIO_TECH_HSUPA:
return TelephonyManager.NETWORK_TYPE_HSUPA;
            case RADIO_TECH_HSPA:
return TelephonyManager.NETWORK_TYPE_HSPA;
            case RADIO_TECH_IS95A:
            case RADIO_TECH_IS95B:
return TelephonyManager.NETWORK_TYPE_CDMA;
            case RADIO_TECH_1xRTT:
return TelephonyManager.NETWORK_TYPE_1xRTT;
            case RADIO_TECH_EVDO_0:
return TelephonyManager.NETWORK_TYPE_EVDO_0;
            case RADIO_TECH_EVDO_A:
return TelephonyManager.NETWORK_TYPE_EVDO_A;
            case RADIO_TECH_EVDO_B:
return TelephonyManager.NETWORK_TYPE_EVDO_B;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;







