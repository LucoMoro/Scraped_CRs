//<Beginning of snippet n. 0>
}

private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-zA-Z_-]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    // Example test cases to validate the new pattern
    assertTrue(DEVICE_PATTERN.matcher("DEVICE-123").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE_NAME").matches());
    assertTrue(DEVICE_PATTERN.matcher("Device-Name").matches());
    assertFalse(DEVICE_PATTERN.matcher("DEVICE@123").matches());
    assertFalse(DEVICE_PATTERN.matcher("DEVICE NAME").matches());
}

//<End of snippet n. 0>