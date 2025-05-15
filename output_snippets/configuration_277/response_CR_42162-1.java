//<Beginning of snippet n. 0>

break;

case TelephonyManager.PHONE_TYPE_CDMA:
                if (isLteEnabled()) {
                    deviceId = getImei(); // Return IMEI if in LTE mode
                } else {
                    deviceId = getMeid(); // Fallback to MEID if not
                }
                assertCdmaDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:

//<End of snippet n. 0>