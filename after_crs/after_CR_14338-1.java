/*Fix printing order of phone objects creation*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index cd72752e..803b736 100644

//Synthetic comment -- @@ -109,13 +109,13 @@

int phoneType = getPhoneType(networkMode);
if (phoneType == Phone.PHONE_TYPE_GSM) {
                    Log.i(LOG_TAG, "Creating GSMPhone");
sProxyPhone = new PhoneProxy(new GSMPhone(context,
sCommandsInterface, sPhoneNotifier));
} else if (phoneType == Phone.PHONE_TYPE_CDMA) {
                    Log.i(LOG_TAG, "Creating CDMAPhone");
sProxyPhone = new PhoneProxy(new CDMAPhone(context,
sCommandsInterface, sPhoneNotifier));
}

sMadeDefaults = true;







