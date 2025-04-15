/*Fixed device tests to use new device api

Change-Id:I8d37398581023808eac19ad21ce864eaba69ae3b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceParserTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceParserTest.java
//Synthetic comment -- index 01274c7..134a5a1 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
assertFalse(meta.hasFrame());

// Test Hardware information
        Hardware hw = device.getDefaultHardware();
Screen screen = hw.getScreen();
assertEquals(screen.getSize(), ScreenSize.NORMAL);
assertEquals(4.65, screen.getDiagonalLength());
//Synthetic comment -- @@ -171,7 +171,7 @@
try {
List<Device> devices = DeviceParser.parse(stream);
assertEquals(1, devices.size());
            assertEquals(0, devices.get(0).getDefaultHardware().getNetworking().size());
fail();
} catch (SAXParseException e) {
assertTrue(e.getMessage().startsWith("cvc-enumeration-valid: Value 'NFD'"));







