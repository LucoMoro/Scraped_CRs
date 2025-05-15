//<Beginning of snippet n. 0>
break;

case TelephonyManager.PHONE_TYPE_CDMA:
    if (isInLteMode()) {
        return getImei();
    } else {
        assertCdmaDeviceId(deviceId);
    }
    break;

case TelephonyManager.PHONE_TYPE_NONE:
    // Handle PHONE_TYPE_NONE case
    return "No valid device ID found";
//<End of snippet n. 0>