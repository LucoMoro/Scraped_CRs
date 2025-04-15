/*Permit MAC Addresses without Colons

Bug 2694172

Change-Id:I01cb8b19026186784a1fa5bb3e653f9ead415175*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9ecbf20..52b12f3 100644

//Synthetic comment -- @@ -377,9 +377,12 @@

private void assertMacAddressReported() {
String macAddress = getMacAddress();
        String withColons = "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}";
        String noColons = "([0-9a-fA-F]{2}){6}";
        String message = "MAC Address " + macAddress + " does not match pattern "
                + withColons + " or " + noColons;
        assertTrue(message, Pattern.matches(withColons, macAddress)
                || Pattern.matches(noColons, macAddress));
}

/** @return mac address which requires the WiFi system to be enabled */







