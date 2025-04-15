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
//Synthetic comment -- index 6ddb312..65dee1a 100644

//Synthetic comment -- @@ -595,11 +595,18 @@
if (rule != curSpnRule
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
            boolean showSpn = !mEmergencyOnly
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
            boolean showPlmn =
                (rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

Intent intent = new Intent(Intents.SPN_STRINGS_UPDATED_ACTION);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
intent.putExtra(Intents.EXTRA_SHOW_SPN, showSpn);







