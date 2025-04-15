/*CB: Add bc type field to RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS

Change-Id:I3f29b54f2df3f6d3daadf30f53cb2a04af2962d4*/




//Synthetic comment -- diff --git a/tests/src/com/android/cellbroadcastreceiver/DialogSmsDisplayTests.java b/tests/src/com/android/cellbroadcastreceiver/DialogSmsDisplayTests.java
//Synthetic comment -- index b2150f1..74bb2ca 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
private static final int DCS_7BIT_ENGLISH = 0x01;
private static final int DCS_16BIT_UCS2 = 0x48;

    /* ETWS Secondary Gsm Test message including header */
private static final byte[] etwsMessageNormal = IccUtils.hexStringToBytes("000011001101" +
//   Line 1                  CRLFLine 2
"EA307DCA602557309707901F58310D0A5BAE57CE770C531790E85C716CBF3044573065B930675730" +
//Synthetic comment -- @@ -111,11 +111,11 @@

private static final SmsCbLocation sEmptyLocation = new SmsCbLocation();

    private static SmsCbMessage createFromPdu(byte[] pdu, int type) {
try {
byte[][] pdus = new byte[1][];
pdus[0] = pdu;
            return GsmSmsCbMessage.createSmsCbMessage(sEmptyLocation, pdus, type);
} catch (IllegalArgumentException e) {
return null;
}
//Synthetic comment -- @@ -124,34 +124,37 @@
public void testSendMessage7bit() throws Exception {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
byte[] pdu = encodeCellBroadcast(0, 0, DCS_7BIT_ENGLISH, "Hello in GSM 7 bit");
        intent.putExtra("message", createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
getActivity().sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public void testSendMessageUCS2() throws Exception {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
byte[] pdu = encodeCellBroadcast(0, 0, DCS_16BIT_UCS2, "Hello in UCS2");
        intent.putExtra("message", createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
getActivity().sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public void testSendEtwsMessageNormal() throws Exception {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageNormal,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
getActivity().sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}

public void testSendEtwsMessageCancel() throws Exception {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageCancel,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
getActivity().sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}

public void testSendEtwsMessageTest() throws Exception {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageTest,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
getActivity().sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}








//Synthetic comment -- diff --git a/tests/src/com/android/cellbroadcastreceiver/tests/SendTestMessages.java b/tests/src/com/android/cellbroadcastreceiver/tests/SendTestMessages.java
//Synthetic comment -- index e634b23..95d0d17 100644

//Synthetic comment -- @@ -385,19 +385,19 @@

private static final SmsCbLocation sEmptyLocation = new SmsCbLocation();

    private static SmsCbMessage createFromPdu(byte[] pdu, int type) {
try {
byte[][] pdus = new byte[1][];
pdus[0] = pdu;
            return GsmSmsCbMessage.createSmsCbMessage(sEmptyLocation, pdus, type);
} catch (IllegalArgumentException e) {
return null;
}
}

    private static SmsCbMessage createFromPdus(byte[][] pdus, int type) {
try {
            return GsmSmsCbMessage.createSmsCbMessage(sEmptyLocation, pdus, type);
} catch (IllegalArgumentException e) {
return null;
}
//Synthetic comment -- @@ -405,25 +405,29 @@

public static void testSendMessage7bit(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTest,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitNoPadding(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestNoPadding,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitNoPaddingUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestNoPaddingUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

//Synthetic comment -- @@ -432,81 +436,93 @@
byte[][] pdus = new byte[2][];
pdus[0] = gsm7BitTestMultipage1;
pdus[1] = gsm7BitTestMultipage2;
        intent.putExtra("message", createFromPdus(pdus, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitMultipageUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestMultipageUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitWithLanguage(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestWithLanguage,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitWithLanguageInBody(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestWithLanguageInBody,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessage7bitWithLanguageInBodyUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsm7BitTestWithLanguageInBodyUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessageUcs2(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsmUcs2Test,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessageUcs2Umts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsmUcs2TestUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessageUcs2MultipageUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsmUcs2TestMultipageUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessageUcs2WithLanguageInBody(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsmUcs2TestWithLanguageInBody,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendMessageUcs2WithLanguageUmts(Activity activity) {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(gsmUcs2TestWithLanguageInBodyUmts,
                GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS));
activity.sendOrderedBroadcast(intent, "android.permission.RECEIVE_SMS");
}

public static void testSendEtwsMessageNormal(Activity activity) {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageNormal,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
activity.sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}

public static void testSendEtwsMessageCancel(Activity activity) {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageCancel,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
activity.sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}

public static void testSendEtwsMessageTest(Activity activity) {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
        intent.putExtra("message", createFromPdu(etwsMessageTest,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM));
activity.sendOrderedBroadcast(intent,
"android.permission.RECEIVE_EMERGENCY_BROADCAST");
}







