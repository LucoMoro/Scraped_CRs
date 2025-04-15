/*Telephony: Set the operator numeric property correctly.

In RuimRecords, changes to set the PROPERTY_ICC_OPERATOR_NUMERIC
only if the operator is not null.  This is to ensure that the
property is set from ro.cdma.home.operator.numeric even if the
GET_IMSI failed in case of CDMA RUIM mode.

In SimRecords, changes to make sure that the PROPERTY_ICC_OPERATOR_NUMERIC
is not reset, to allow the property to be updated from
the ro.cdma.home.operator.numeric in case of CDMA RUIM mode with
more than one applications in the card.

Change-Id:I8fc81b0224fcb3205e08c0cad2988158ee5c0d9a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e8cd8f3..e86d509 100755

//Synthetic comment -- @@ -617,7 +617,9 @@
String operator = getRUIMOperatorNumeric();
log("RuimRecords: onAllRecordsLoaded set 'gsm.sim.operator.numeric' to operator='" +
operator + "'");
        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, operator);

if (mImsi != null) {
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..9f259c2 100755

//Synthetic comment -- @@ -228,7 +228,6 @@
adnCache.reset();

log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, null);
SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, null);
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY, null);








