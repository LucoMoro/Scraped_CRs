
//<Beginning of snippet n. 0>


break;

case TelephonyManager.PHONE_TYPE_CDMA:
                // LTE device is using IMEI as device id
                if (mTelephonyManager.getLteOnCdmaMode() == 1) {
                    assertGsmDeviceId(deviceId);
                } else {
                    assertCdmaDeviceId(deviceId);
                }
break;

case TelephonyManager.PHONE_TYPE_NONE:

//<End of snippet n. 0>








