//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

}

private static File createDeepStructure(File base) throws Exception {
    String longString = longString(128);
    File f = base;
    for (int i = 0; f.toString().length() < 158; ++i) {
        f = new File(f, longString);
        if (!f.mkdir()) {
            break;
        }
    }
}

public void test_longReadlink() throws Exception {
    File base = createTemporaryDirectory();
    File target = createDeepStructure(base);
    File source = new File(base, "source");
    assertFalse(source.exists());
    assertTrue(target.exists());
    assertTrue(target.getCanonicalPath().length() < 159);
    ln_s(target, source);
    assertTrue(source.exists());
    assertEquals(target.getCanonicalPath(), source.getCanonicalPath());

    // Additional test for paths at the limit
    String longString = longString(158);
    File edgeCaseFile = new File(base, longString);
    assertTrue(edgeCaseFile.mkdir());
    assertTrue(edgeCaseFile.getCanonicalPath().length() < 159);
}

//<End of snippet n. 0>