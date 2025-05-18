//<Beginning of snippet n. 0>
boolean isLteMode = (phoneType == TelephonyManager.PHONE_TYPE_LTE);
boolean isCdmaMode = (phoneType == TelephonyManager.PHONE_TYPE_CDMA);
boolean isDualMode = (isLteMode && isCdmaMode);

if (isDualMode) {
    assertLteDeviceId(deviceId);
} else if (isLteMode) {
    assertLteDeviceId(deviceId);
} else {
    switch (phoneType) {
        case TelephonyManager.PHONE_TYPE_CDMA:
            assertCdmaDeviceId(deviceId);
            break;
        case TelephonyManager.PHONE_TYPE_NONE:
            // Handle none case if applicable
            break;
    }
}
//<End of snippet n. 0>