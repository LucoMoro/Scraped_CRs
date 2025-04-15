/*Telephony: Fix LTE CB Sms

Ignore 3gpp location information when it is not available.

Change-Id:Ie5d6372ef7e8da6893800e05a83ba840b7fd31f6*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index d6c2a20..c49e5ad 100644

//Synthetic comment -- @@ -393,9 +393,14 @@

SmsCbHeader header = new SmsCbHeader(receivedPdu);
String plmn = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
            GsmCellLocation cellLocation = (GsmCellLocation) mPhone.getCellLocation();
            int lac = cellLocation.getLac();
            int cid = cellLocation.getCid();

SmsCbLocation location;
switch (header.getGeographicalScope()) {







