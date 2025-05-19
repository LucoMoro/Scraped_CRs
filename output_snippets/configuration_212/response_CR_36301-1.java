//<Beginning of snippet n. 0>
private static final int MAX_PATH_LENGTH = 159;

private static File createDeepStructure(File base) throws Exception {
    String longString = longString(MAX_PATH_LENGTH);
    File f = base;
    for (int i = 0; f.toString().length() < MAX_PATH_LENGTH; ++i) {
        f = new File(f, longString);
        assertTrue(f.mkdir());
    }
}

public void test_longReadlink() throws Exception {
    File base = createTemporaryDirectory();
    File target = createDeepStructure(base);
    File source = new File(base, "source");
    assertFalse(source.exists());
    assertTrue(target.exists());
    assertTrue(target.getCanonicalPath().length() < MAX_PATH_LENGTH);
    ln_s(target, source);
    assertTrue(source.exists());
    assertEquals(target.getCanonicalPath(), source.getCanonicalPath());
    assertEquals(MAX_PATH_LENGTH - 1, target.getCanonicalPath().length());
}
//<End of snippet n. 0>