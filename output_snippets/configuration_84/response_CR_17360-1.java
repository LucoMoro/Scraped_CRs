//<Beginning of snippet n. 0>
private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^(?!-)(?!.*--)(?!.*-$)[0-9a-zA-Z]([-0-9a-zA-Z]*[0-9a-zA-Z])?$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    assertTrue(DEVICE_PATTERN.matcher("valid-device-name").matches());
    assertTrue(DEVICE_PATTERN.matcher("DEVICE123").matches());
    assertTrue(DEVICE_PATTERN.matcher("a-b").matches());
    assertTrue(DEVICE_PATTERN.matcher("valid-device").matches());
    assertTrue(DEVICE_PATTERN.matcher("VALID-DEVICE").matches());
    assertTrue(DEVICE_PATTERN.matcher("valid-device-name-123").matches());
    assertFalse(DEVICE_PATTERN.matcher("-invalid-prefix").matches());
    assertFalse(DEVICE_PATTERN.matcher("invalid-suffix-").matches());
    assertFalse(DEVICE_PATTERN.matcher("invalid--device").matches());
    assertFalse(DEVICE_PATTERN.matcher("invalid__device").matches());
    assertFalse(DEVICE_PATTERN.matcher("").matches());
    assertFalse(DEVICE_PATTERN.matcher("----").matches());
    assertFalse(DEVICE_PATTERN.matcher("a--b").matches());
    assertFalse(DEVICE_PATTERN.matcher("invalid-").matches());
    assertFalse(DEVICE_PATTERN.matcher("-invalid").matches());
    assertFalse(DEVICE_PATTERN.matcher("invalid").matches());
}
//<End of snippet n. 0>