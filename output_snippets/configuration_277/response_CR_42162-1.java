<<Beginning of snippet n. 0>>
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                if (isLteEnabled()) {
                    return getImei();
                } else {
                    assertCdmaDeviceId(deviceId);
                }
break;

case TelephonyManager.PHONE_TYPE_NONE:
<<End of snippet n. 0>>