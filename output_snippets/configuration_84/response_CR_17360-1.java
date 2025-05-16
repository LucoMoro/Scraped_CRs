//<Beginning of snippet n. 0>
}

private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-zA-Z_-]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    // Test cases for valid device names
    assertTrue(DEVICE_PATTERN.matcher("device_name").matches());
    assertTrue(DEVICE_PATTERN.matcher("Device-Name").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE_NAME_123").matches());
    assertTrue(DEVICE_PATTERN.matcher("device-name-123").matches());
    assertTrue(DEVICE_PATTERN.matcher("123-device_name").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE123").matches());
    assertTrue(DEVICE_PATTERN.matcher("device-name").matches());
    
    // Test cases for invalid device names
    assertFalse(DEVICE_PATTERN.matcher("device@name").matches());
    assertFalse(DEVICE_PATTERN.matcher("device name").matches());
    assertFalse(DEVICE_PATTERN.matcher("device/name").matches());
    assertFalse(DEVICE_PATTERN.matcher("device#name").matches());
}

//<End of snippet n. 0>