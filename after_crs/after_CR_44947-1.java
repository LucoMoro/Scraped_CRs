/*Telephony: Enable network operators option

Enable Network Operator option in case of No SIM.

Change-Id:Ieec2d9646e9beb94ae9a2a02daed002696ef55ad*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..b03d362 100644

//Synthetic comment -- @@ -1523,7 +1523,7 @@

public boolean isCspPlmnEnabled() {
IccRecords r = mIccRecords.get();
        return (r != null) ? r.isCspPlmnEnabled() : true;
}

private void registerForSimRecordEvents() {







