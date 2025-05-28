<<Beginning of snippet n. 0>>
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                if (isLteMode()) {
                    deviceId = getImei();
                } else {
                    deviceId = getMeid();
                }
                assertCdmaDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:
//<End of snippet n. 0>>