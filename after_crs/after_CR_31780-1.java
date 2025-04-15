/*telephony: fix adding of neighboring cells

GsmServiceStateTracker pollStateDone() sets the system property
PROPERTY_DATA_NETWORK_TYPE to the result from
ServiceState.radioTechnologyToString().

Since Change-IdI832872c876b290a3e83a79068059a21894472a1d,
ServiceState.radioTechnologyToString() returns
the new string:integer format.

RIL responseCellList() gets the PROPERTY_DATA_NETWORK_TYPE and
performs a string compare. However, it still expects the old
string format. So radioType becomes NETWORK_TYPE_UNKNOWN,
which will not add the neighboring cells.

Change-Id:I42de4e28ce76d01feabf9505a2939cdb91b7839eSigned-off-by: Tom Heyligen <t.heyligen@option.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f2e7f45..8816e9e 100644

//Synthetic comment -- @@ -3167,17 +3167,17 @@
String radioString = SystemProperties.get(
TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE, "unknown");
int radioType;
       if (radioString.startsWith("GPRS")) {
radioType = NETWORK_TYPE_GPRS;
       } else if (radioString.startsWith("EDGE")) {
radioType = NETWORK_TYPE_EDGE;
       } else if (radioString.startsWith("UMTS")) {
radioType = NETWORK_TYPE_UMTS;
       } else if (radioString.startsWith("HSDPA")) {
radioType = NETWORK_TYPE_HSDPA;
       } else if (radioString.startsWith("HSUPA")) {
radioType = NETWORK_TYPE_HSUPA;
       } else if (radioString.startsWith("HSPA")) {
radioType = NETWORK_TYPE_HSPA;
} else {
radioType = NETWORK_TYPE_UNKNOWN;







