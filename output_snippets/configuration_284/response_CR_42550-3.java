//<Beginning of snippet n. 0>
public void testDefaultCategories() throws Exception {
    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testDefaultCategories.");
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity") || !out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testSingleCategory() throws Exception {
    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testSingleCategory (LAUNCHER).");
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity") || !out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

    out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.MONKEY 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testSingleCategory (MONKEY).");
        return;
    }
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity") || !out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testMultipleCategories() throws Exception {
    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testMultipleCategories.");
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity") || !out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testSinglePackage() throws Exception {
    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testSinglePackage (PKGS[0]).");
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity") || !out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

    out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testSinglePackage (PKGS[1]).");
        return;
    }
    assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity") || !out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}

public void testMultiplePackages() throws Exception {
    String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " -p " + PKGS[1] + " 5000");
    if (out == null || out.isEmpty()) {
        log("Package/component information unavailable for testMultiplePackages.");
        return;
    }
    assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity") || !out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
    assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity") || !out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}
//<End of snippet n. 1>