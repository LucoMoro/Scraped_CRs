/*Fix for battery temperature display

Centi temperature under zero degree is displayed with '-' sign.
Fix this using absolute value in centi degree display.

Change-Id:If4958d9c75c03d1f3ecfd14436fc80dc2fd57860*/




//Synthetic comment -- diff --git a/src/com/android/settings/BatteryInfo.java b/src/com/android/settings/BatteryInfo.java
//Synthetic comment -- index ccad236..3f88277 100644

//Synthetic comment -- @@ -63,11 +63,12 @@

/**
* Format a number of tenths-units as a decimal string without using a
     * conversion to float.  E.g. 347 -> "34.7", -99 -> "-9.9"
*/
private final String tenthsToFixedString(int x) {
int tens = x / 10;
        // use Math.abs to avoid "-9.-9" about -99
        return Integer.toString(tens) + "." + Math.abs(x - 10 * tens);
}

/**







