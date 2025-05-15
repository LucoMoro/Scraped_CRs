//<Beginning of snippet n. 0>
private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-zA-Z_-]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
    // Add test cases
    assert DEVICE_PATTERN.matcher("valid-device").matches();
    assert DEVICE_PATTERN.matcher("VaLiD-Device").matches();
    assert !DEVICE_PATTERN.matcher("invalid device").matches();
    assert !DEVICE_PATTERN.matcher("invalid_device!").matches();
}
//<End of snippet n. 0>