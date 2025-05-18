//<Beginning of snippet n. 0>
public void testDefaultCategories() throws Exception {
    if (!mDevice.supportsCommand("monkey")) {
        fail("Device does not support monkey command");
    }
    String out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " 5000");
    validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
    validateOutput(out, "cmp=com.android.cts.monkey/.BaboonActivity");
}

public void testSingleCategory() throws Exception {
    if (!mDevice.supportsCommand("monkey")) {
        fail("Device does not support monkey command");
    }
    String out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000");
    validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
    assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

    out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000");
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    validateOutput(out, "cmp=com.android.cts.monkey/.BaboonActivity");
}

public void testMultipleCategories() throws Exception {
    if (!mDevice.supportsCommand("monkey")) {
        fail("Device does not support monkey command");
    }
    String out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000");
    validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
    validateOutput(out, "cmp=com.android.cts.monkey/.BaboonActivity");
}

private String executeShellCommandWithErrorHandling(String command) throws Exception {
    try {
        String result = mDevice.executeShellCommand(command);
        validateShellOutput(result);
        return result;
    } catch (Exception e) {
        logError("Error executing command: " + command, e);
        throw new RuntimeException("Command execution failed: " + e.getMessage(), e);
    }
}

private void validateShellOutput(String output) {
    assertNotNull(output);
    assertTrue(output.length() > 0);
    // Additional format checks can be implemented here for completeness if necessary
    assertTrue(output.matches(".*cmp=.*"));
}

private void validateOutput(String output, String expected) {
    validateShellOutput(output);
    assertTrue(output.contains(expected));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testSinglePackage() throws Exception {
    if (!mDevice.supportsCommand("monkey")) {
        fail("Device does not support monkey command");
    }
    String out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " 5000");
    validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
    assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

    out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[1] + " 5000");
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    validateOutput(out, "cmp=com.android.cts.monkey2/.ChimpActivity");
}

public void testMultiplePackages() throws Exception {
    if (!mDevice.supportsCommand("monkey")) {
        fail("Device does not support monkey command");
    }
    String out = executeShellCommandWithErrorHandling("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000");
    validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
    validateOutput(out, "cmp=com.android.cts.monkey2/.ChimpActivity");
}
//<End of snippet n. 1>