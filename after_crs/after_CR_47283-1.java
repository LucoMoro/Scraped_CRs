/*Telephony: Add new apn.sim.operator_numeric property

Use new property to filter apns. We need this new property
because icc.operator_numeric property is also used in cdma
and ApnSetting UI might display wrong set of apns if the
peroperty value used was set by RuimRecords

Change-Id:I9b5097fb0ccd2946ac8bfd12aac36d164e6c8f0aCRs-Fixed: 362846*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index e866757..34e065e 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_APN_SIM_OPERATOR_NUMERIC;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -1281,6 +1282,7 @@
log("SIMRecords: onAllRecordsLoaded set 'gsm.sim.operator.numeric' to operator='" +
operator + "'");
SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, operator);
        SystemProperties.set(PROPERTY_APN_SIM_OPERATOR_NUMERIC, operator);

if (mImsi != null) {
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,







