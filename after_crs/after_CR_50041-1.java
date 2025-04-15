/*Fix for battery temperature display

Centi temperature under zero degree is displayed with '-' sign.
Fix this using absolute value in centi degree display.

Change-Id:If4958d9c75c03d1f3ecfd14436fc80dc2fd57860*/




//Synthetic comment -- diff --git a/src/com/android/settings/BatteryInfo.java b/src/com/android/settings/BatteryInfo.java
//Synthetic comment -- index ccad236..b1046a8 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
*/
private final String tenthsToFixedString(int x) {
int tens = x / 10;
        return Integer.toString(tens) + "." + Math.abs(x - 10 * tens);
}

/**







