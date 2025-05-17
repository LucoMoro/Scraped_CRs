//<Beginning of snippet n. 0>
public void testDefaultCategories() throws Exception {
    if (!isDeviceCompatible()) {
        return;
    }

    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    validateOutput(out);

    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testSingleCategory() throws Exception {
    if (!isDeviceCompatible()) {
        return;
    }

    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000");
    validateOutput(out);
    
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

    out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000");
    validateOutput(out);
    
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testMultipleCategories() throws Exception {
    if (!isDeviceCompatible()) {
        return;
    }

    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000");
    validateOutput(out);
    
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testSinglePackage() throws Exception {
    if (!isDeviceCompatible()) {
        return;
    }

    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    validateOutput(out);
    
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

    out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
    validateOutput(out);
    
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}

public void testMultiplePackages() throws Exception {
    if (!isDeviceCompatible()) {
        return;
    }

    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000");
    validateOutput(out);
    
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}

private boolean isDeviceCompatible() {
    // Implement device compatibility checks here
    return true; // Placeholder
}

private void validateOutput(String out) {
    if (out == null || out.isEmpty() || !isValidJson(out)) {
        fail("Command execution failed or returned no output or invalid format.");
    }
}

private boolean isValidJson(String out) {
    // Implement logic to check if output is in valid JSON format
    return true; // Placeholder
}
//<End of snippet n. 1>