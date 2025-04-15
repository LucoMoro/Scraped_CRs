/*Skip gsm format test if phoneType is PHONE_TYPE_NONE

Change-Id:Ibc8043386e35c9dcbf29ccff9a6b3e50c2b80489*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsMessageTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsMessageTest.java
//Synthetic comment -- index 515f8b5..0daf008 100644

//Synthetic comment -- @@ -171,7 +171,8 @@
)
})
public void testCreateFromPdu() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -254,7 +255,8 @@
)
})
public void testCPHSVoiceMail() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -302,7 +304,8 @@
)
})
public void testGetUserData() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -352,7 +355,8 @@
// expected
}

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, OCTET encoding for EMS not properly supported
return;
}
//Synthetic comment -- @@ -403,7 +407,8 @@
)
})
public void testEmailGateway() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/gsm/cts/SmsMessageTest.java b/tests/tests/telephony/src/android/telephony/gsm/cts/SmsMessageTest.java
//Synthetic comment -- index 8b26880..bfacba7 100644

//Synthetic comment -- @@ -169,7 +169,8 @@
)
})
public void testCreateFromPdu() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -251,7 +252,8 @@
)
})
public void testCPHSVoiceMail() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -299,7 +301,8 @@
)
})
public void testGetUserData() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}
//Synthetic comment -- @@ -349,7 +352,8 @@
// expected
}

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, OCTET encoding for EMS not properly supported
return;
}
//Synthetic comment -- @@ -400,7 +404,8 @@
)
})
public void testEmailGateway() throws Exception {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA || 
                mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
// TODO: temp workaround, need to adjust test to use CDMA pdus
return;
}







