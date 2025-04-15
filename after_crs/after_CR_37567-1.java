/*Telephony: Add 'ServiceCategory' to SMS finger print

In case of certain CMAS messages sent back to back, say if,
CMAS message id's 4097 and 4099  are sent in sequence,
one after the other, msg id 4099 fails to be displayed
on the UI although modem sends it to telephony
layer. This is because telephony rejects/discards the new
incoming CMAS message - 4099 as its finger-print matches exactly
with the previously sent CMAS msg id 4097

Change-Id:I2a03eb46ef3ab921a533a946f6be2ca7f1d082f5*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 1409cab..0625dda 100644

//Synthetic comment -- @@ -953,6 +953,7 @@
/* package */ byte[] getIncomingSmsFingerprint() {
ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mEnvelope.serviceCategory);
output.write(mEnvelope.teleService);
output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);







