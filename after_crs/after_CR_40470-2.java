/*Telephony: Add 'ServiceCategory' to SMS finger print

In case of certain broadcast messages sent back to back, say if,
service category 0x0002 Administrative and service category
0x0003 Maintenance are sent in sequence,
one after the other, service category 0x0003 fails to be displayed
on the UI although modem sends it to telephony
layer. This is because telephony rejects/discards the new
incoming message - 0x0003 as its finger-print matches exactly
with the previously sent message 0x0002.

Change-Id:Id48169924605f423ee97a0866e754462f8c4c30d*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 6254a50..966ef47 100644

//Synthetic comment -- @@ -979,6 +979,7 @@
/* package */ byte[] getIncomingSmsFingerprint() {
ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mEnvelope.serviceCategory);
output.write(mEnvelope.teleService);
output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);







