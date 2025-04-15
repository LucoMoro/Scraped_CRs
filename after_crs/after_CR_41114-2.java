/*Telephony: Fix LTE CB Sms for dual-mode devices

Ignore 3gpp location information when it is not available.
This is required to support dual-mode devices such as CDMA/LTE devices
that require support for both 3GPP and 3GPP2 format messages.

Change-Id:Ie5d6372ef7e8da6893800e05a83ba840b7fd31f6*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index d6c2a20..9295773 100644

//Synthetic comment -- @@ -393,9 +393,17 @@

SmsCbHeader header = new SmsCbHeader(receivedPdu);
String plmn = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
            int lac = -1;
            int cid = -1;
            android.telephony.CellLocation cl = mPhone.getCellLocation();
            // Check if cell location is GsmCellLocation.  This is required to support
            // dual-mode devices such as CDMA/LTE devices that require support for
            // both 3GPP and 3GPP2 format messages
            if (cl instanceof GsmCellLocation) {
                GsmCellLocation cellLocation = (GsmCellLocation)cl;
                lac = cellLocation.getLac();
                cid = cellLocation.getCid();
            }

SmsCbLocation location;
switch (header.getGeographicalScope()) {







