//<Beginning of snippet n. 0>

public void testDefaultCategories() throws Exception {
    executeTest("monkey -v -p " + PKGS[0] + " 5000", "Failed to retrieve output for default categories.", out -> {
        validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity", "cmp=com.android.cts.monkey/.BaboonActivity");
    });
}

public void testSingleCategory() throws Exception {
    executeTest("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000", "Failed to retrieve output for single category.", out -> {
        validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
        assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
    });
    
    executeTest("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000", "Failed to retrieve output for single category MONKEY test.", out -> {
        assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        validateOutput(out, "cmp=com.android.cts.monkey/.BaboonActivity");
    });
}

public void testMultipleCategories() throws Exception {
    executeTest("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000", "Failed to retrieve output for multiple categories.", out -> {
        validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity", "cmp=com.android.cts.monkey/.BaboonActivity");
    });
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public void testSinglePackage() throws Exception {
    executeTest("monkey -v -p " + PKGS[0] + " 5000", "Failed to retrieve output for single package.", out -> {
        validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity");
        assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
    });

    executeTest("monkey -v -p " + PKGS[1] + " 5000", "Failed to retrieve output for second single package test.", out -> {
        assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        validateOutput(out, "cmp=com.android.cts.monkey2/.ChimpActivity");
    });
}

public void testMultiplePackages() throws Exception {
    executeTest("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000", "Failed to retrieve output for multiple packages.", out -> {
        validateOutput(out, "cmp=com.android.cts.monkey/.MonkeyActivity", "cmp=com.android.cts.monkey2/.ChimpActivity");
    });
}

private void executeTest(String command, String errorMsg, Consumer<String> assertions) throws Exception {
    int retries = 3;
    while (retries > 0) {
        try {
            String out = mDevice.executeShellCommand(command);
            validateCommandOutput(out, errorMsg);
            assertions.accept(out);
            return;
        } catch (Exception e) {
            retries--;
            if (retries == 0) {
                throw new Exception("Error executing command after retries: " + e.getMessage(), e);
            }
        }
    }
}

private void validateCommandOutput(String output, String errorMsg) throws Exception {
    if (output.contains("error") || output.trim().isEmpty()) {
        throw new Exception(errorMsg);
    }
}

private void validateOutput(String output, String... expectedComponents) {
    for (String component : expectedComponents) {
        assertTrue(output.contains(component));
    }
}

//<End of snippet n. 1>