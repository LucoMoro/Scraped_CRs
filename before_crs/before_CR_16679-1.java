/*Telephony : Display operator's name in status bar

If the operator's name is null, try to read it from EF_SPN_CPHS or
EF_SPN_SHORT_CPHS fields on the SIM card. Then add the result to the
operator's name string.

Change-Id:I3e152d5dc1a80e4aafc7030b38b9922845773f03*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 30f38bd..9d2fd89 100644

//Synthetic comment -- @@ -1367,13 +1367,22 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
spnDisplayCondition = 0xff & data[0];
                    spn = IccUtils.adnStringFieldToString(data, 1, data.length - 1);

                    if (DBG) log("Load EF_SPN: " + spn
                            + " spnDisplayCondition: " + spnDisplayCondition);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
} else {
phone.getIccFileHandler().loadEFTransparent( EF_SPN_CPHS,
obtainMessage(EVENT_GET_SPN_DONE));
//Synthetic comment -- @@ -1389,13 +1398,23 @@
case READ_SPN_CPHS:
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
                    spn = IccUtils.adnStringFieldToString(
data, 0, data.length - 1 );

                    if (DBG) log("Load EF_SPN_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
} else {
phone.getIccFileHandler().loadEFTransparent(
EF_SPN_SHORT_CPHS, obtainMessage(EVENT_GET_SPN_DONE));
//Synthetic comment -- @@ -1407,12 +1426,19 @@
case READ_SPN_SHORT_CPHS:
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
                    spn = IccUtils.adnStringFieldToString(
data, 0, data.length - 1);

                    if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);
                }else {
if (DBG) log("No SPN loaded in either CHPS or 3GPP");
}








