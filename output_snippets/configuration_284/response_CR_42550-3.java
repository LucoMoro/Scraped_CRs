//<Beginning of snippet n. 0>
public void testDefaultCategories() throws Exception {
    if (!mDevice.hasPermission("android.permission.START_ACTIVITIES")) {
        fail("Missing permission to start activities");
    }
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testSingleCategory() throws Exception {
    if (!mDevice.hasPermission("android.permission.START_ACTIVITIES")) {
        fail("Missing permission to start activities");
    }
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testMultipleCategories() throws Exception {
    if (!mDevice.hasPermission("android.permission.START_ACTIVITIES")) {
        fail("Missing permission to start activities");
    }
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testSinglePackage() throws Exception {
    if (!mDevice.hasPermission("android.permission.START_ACTIVITIES")) {
        fail("Missing permission to start activities");
    }
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}

public void testMultiplePackages() throws Exception {
    if (!mDevice.hasPermission("android.permission.START_ACTIVITIES")) {
        fail("Missing permission to start activities");
    }
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000");
    } catch (Exception e) {
        fail("Shell command execution failed: " + e.getMessage());
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}
//<End of snippet n. 1>