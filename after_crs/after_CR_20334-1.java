/*Huawei fix for receive message tests failing on Mobile Internet Devices, Personal Media Players and other non-phone devices

Change-Id:I7640dc1cf95db24fa155bf5bd8e374fc4c391bfc*/




//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..d324b4c 100755

//Synthetic comment -- @@ -43,6 +43,13 @@

private static final String LOG_TAG = "NoReceiveSmsPermissionTest";

    private TelephonyManager mTelephonyManager;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE); 
    }
/**
* Verify that SmsManager.sendTextMessage requires permissions.
* <p>Tests Permission:
//Synthetic comment -- @@ -51,6 +58,12 @@
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
        
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            // if device is not a phone device, for example:MIDs,PMPs, do nothing
            return;
        }
        
// register our test receiver to receive SMSs. This won't throw a SecurityException,
// so test needs to wait to determine if it actual receives an SMS
// admittedly, this is a weak verification
//Synthetic comment -- @@ -82,10 +95,11 @@
PendingIntent deliveryIntent = PendingIntent.getBroadcast(getContext(), 0,
new Intent(MESSAGE_STATUS_RECEIVED_ACTION), PendingIntent.FLAG_ONE_SHOT);

        /*TelephonyManager telephony = (TelephonyManager)
getContext().getSystemService(Context.TELEPHONY_SERVICE);
// get current phone number
        String currentNumber = telephony.getLine1Number();*/
        String currentNumber = mTelephonyManager.getLine1Number();
Log.i(LOG_TAG, String.format("Sending SMS to self: %s", currentNumber));
sendSms(currentNumber, "test message", sentIntent, deliveryIntent);
}







