/*Use proper check for spn for operator name display

Change null check to null or empty string check.

Change-Id:Iff6d94107dde30f15a668e0990201e9f952c1529*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..d10d02f 100755

//Synthetic comment -- @@ -1392,7 +1392,7 @@
@Override
public int getDisplayRule(String plmn) {
int rule;
        if (TextUtils.isEmpty(spn) || spnDisplayCondition == -1) {
// EF_SPN was not found on the SIM, or not yet loaded.  Just show ONS.
rule = SPN_RULE_SHOW_PLMN;
} else if (isOnMatchingPlmn(plmn)) {







