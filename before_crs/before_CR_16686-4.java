/*Display Unicode name for China Operators.

The operator name is displayed in the phone using latin
characters by default (the SPN field). The SIM can be
configured to display a unicode name instead using the
PLMN field. However, many Chinese operators requires
this to be a customizable setting as well.

A property, ro.operator.use-plmn, can be set to override
default behaviour and display operator name as unicode
is introduced.

Change-Id:I0b2183cc40fd76bee88ffbe98b99a1d6fa9e2f5b*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6e2b262d..4030daf5 100644

//Synthetic comment -- @@ -497,11 +497,19 @@
if (rule != curSpnRule
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
            boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
            boolean showPlmn = !TextUtils.isEmpty(plmn) &&
                (rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

if (DBG) {
log(String.format("updateSpnDisplay: changed sending intent" + " rule=" + rule +
" showPlmn='%b' plmn='%s' showSpn='%b' spn='%s'",







