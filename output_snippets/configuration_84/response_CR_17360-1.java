//<Beginning of snippet n. 0>
private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^[0-9a-zA-Z_-]+$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    // Add test cases to verify the updated DEVICE_PATTERN
    assertTrue(DEVICE_PATTERN.matcher("device-name").matches()); // lowercase and hyphen
    assertTrue(DEVICE_PATTERN.matcher("DEVICE_NAME").matches()); // uppercase
    assertTrue(DEVICE_PATTERN.matcher("device123").matches()); // lowercase and numbers
    assertTrue(DEVICE_PATTERN.matcher("DEVICE-123").matches()); // uppercase and hyphen
    assertFalse(DEVICE_PATTERN.matcher("invalid!name").matches()); // invalid character
    assertFalse(DEVICE_PATTERN.matcher("").matches()); // empty string
    assertFalse(DEVICE_PATTERN.matcher("device name").matches()); // space
    assertFalse(DEVICE_PATTERN.matcher("device#name").matches()); // special character
}
//<End of snippet n. 0>