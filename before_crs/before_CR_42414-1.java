/*Display Unicode name for China Operators

The operator name is displayed in the phone using latin
characters by default (the SPN field). The SIM can be
configured to display a unicode name instead using the
PLMN field. However, many Chinese operators requires
this to be a customizable setting as well.

A property, ro.operator.use-plmn, can be set to override
default behaviour and display operator name as unicode
is introduced.

Change-Id:I998dc17a60a1e24af32b9f72f5f422d116fd3e6d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
old mode 100755
new mode 100644
//Synthetic comment -- index 42443fe..76e9422

//Synthetic comment -- @@ -497,10 +497,19 @@
if (rule != curSpnRule
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
            boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
            boolean showPlmn = !TextUtils.isEmpty(plmn) &&
                (rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

if (DBG) {
log(String.format("updateSpnDisplay: changed sending intent" + " rule=" + rule +







