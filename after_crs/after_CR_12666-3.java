/*Made roaming checks more efficient and fixed a bug that could occur when
the SIM card does not report an alphanumeric identifier back.

Change-Id:I4e51849b057e1d4794ffaeaeb5dd0fe69546ae76*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bc7b7fa..6ec525a 100644

//Synthetic comment -- @@ -1281,26 +1281,20 @@
* @return true for roaming state set
*/
private boolean isRoamingBetweenOperators(boolean gsmRoaming, ServiceState s) {
	String simNumeric = SystemProperties.get(PROPERTY_ICC_OPERATOR_NUMERIC, "");
String  operatorNumeric = s.getOperatorNumeric();
	
	/* Okay, this should really make things more straight-forward
         * and more efficient...
         * The numeric values have always to be present from my understanding,
         * whilst the alphanumeric value is not present at least on my
         * SIM card.
         * Also, the country check can safely be omitted, the numerics match
         * so the country matches too. -speijnik
         */

	// TODO: what about mobile virtual network operators? -speijnik
	return gsmRoaming && !simNumeric.equals(operatorNumeric);
}

private static int twoDigitsAt(String s, int offset) {







