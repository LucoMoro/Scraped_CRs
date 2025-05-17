//<Beginning of snippet n. 0>
case TelephonyManager.PHONE_TYPE_CDMA:
                if (isInLteMode()) {
                    deviceId = telephonyManager.getDeviceId(SubscriptionManager.getDefaultSubscriptionId());
                    // Additional check can be made here to ensure deviceId is valid
                } else {
                    assertCdmaDeviceId(deviceId);
                }
                break;

case TelephonyManager.PHONE_TYPE_NONE:
//<End of snippet n. 0>