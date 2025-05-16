//<Beginning of snippet n. 0>
public void testDefaultCategories() throws Exception {
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
        assertTrue("Output does not contain expected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue("Output does not contain expected BaboonActivity", out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
    } catch (Exception e) {
        fail("Failed to execute shell command: " + e.getMessage());
    }
}

public void testSingleCategory() throws Exception {
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000");
        assertTrue("Output does not contain expected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse("Output contains unexpected BaboonActivity", out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000");
        assertFalse("Output contains unexpected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue("Output does not contain expected BaboonActivity", out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
    } catch (Exception e) {
        fail("Failed to execute shell command: " + e.getMessage());
    }
}

public void testMultipleCategories() throws Exception {
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000");
        assertTrue("Output does not contain expected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue("Output does not contain expected BaboonActivity", out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
    } catch (Exception e) {
        fail("Failed to execute shell command: " + e.getMessage());
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testSinglePackage() throws Exception {
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
        assertTrue("Output does not contain expected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse("Output contains unexpected ChimpActivity", out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
        assertFalse("Output contains unexpected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue("Output does not contain expected ChimpActivity", out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
    } catch (Exception e) {
        fail("Failed to execute shell command: " + e.getMessage());
    }     
}

public void testMultiplePackages() throws Exception {
    String out;
    try {
        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000");
        assertTrue("Output does not contain expected MonkeyActivity", out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue("Output does not contain expected ChimpActivity", out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
    } catch (Exception e) {
        fail("Failed to execute shell command: " + e.getMessage());
    }
}
//<End of snippet n. 1>