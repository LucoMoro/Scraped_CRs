//<Beginning of snippet n. 0>
case TelephonyManager.PHONE_TYPE_CDMA:
    if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
        deviceId = telephonyManager.getDeviceId(); // Fetch IMEI in LTE mode
    } else {
        deviceId = telephonyManager.getDeviceId(1); // MEID or fallback
    }
    if (deviceId == null) {
        deviceId = "UNKNOWN"; // Fallback handling
    }
    assertCdmaDeviceId(deviceId);
    break;

case TelephonyManager.PHONE_TYPE_NONE:
//<End of snippet n. 0>