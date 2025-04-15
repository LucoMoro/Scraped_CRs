/*Made roaming checks more efficient and fixed a bug that could occur when
the SIM card does not report an alphanumeric identifier back.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 8140654..ef0c78f 100644

//Synthetic comment -- @@ -1215,26 +1215,20 @@
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
	return gsmRoaming && simNumeric.equals(operatorNumeric);
}

private static int twoDigitsAt(String s, int offset) {







