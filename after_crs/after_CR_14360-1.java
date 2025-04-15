/*Add support for EVDO Rev B

Change-Id:I511fe8ff9a8d83742243ca450d6b308ec6791201*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index cdbaa8b..b684df5 100644

//Synthetic comment -- @@ -715,6 +715,8 @@
return TelephonyManager.NETWORK_TYPE_EVDO_0;
case ServiceState.RADIO_TECHNOLOGY_EVDO_A:
return TelephonyManager.NETWORK_TYPE_EVDO_A;
            case ServiceState.RADIO_TECHNOLOGY_EVDO_B:
                return TelephonyManager.NETWORK_TYPE_EVDO_B;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;
}







