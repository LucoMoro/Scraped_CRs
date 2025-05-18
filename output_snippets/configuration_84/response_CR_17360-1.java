//<Beginning of snippet n. 0>
}

private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9A-Za-z_-]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    // Additional test cases for uppercase letters, underscores, and hyphens
    assertTrue(DEVICE_PATTERN.matcher("DEVICE-123").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE_123").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE-456").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE-XYZ").matches());
    assertTrue(DEVICE_PATTERN.matcher("Device-123").matches()); // Mixed-case test
    assertFalse(DEVICE_PATTERN.matcher("DEVICE 789").matches()); // Invalid due to space
    assertFalse(DEVICE_PATTERN.matcher("DEVICE!@#").matches()); // Invalid due to special characters
    assertFalse(DEVICE_PATTERN.matcher("-DEVICE-123").matches()); // Invalid due to leading hyphen
    assertFalse(DEVICE_PATTERN.matcher("DEVICE-123-").matches()); // Invalid due to trailing hyphen
    assertFalse(DEVICE_PATTERN.matcher("DEVICE--123").matches()); // Invalid due to consecutive hyphens
}
//<End of snippet n. 0>