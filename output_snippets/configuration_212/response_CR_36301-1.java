//<Beginning of snippet n. 0>

private static File createDeepStructure(File base) throws Exception {
    String longString = longString(128);
    File f = base;
    for (int i = 0; f.toString().length() < 159 - longString.length(); ++i) {
        f = new File(f, longString);
        assertTrue(f.mkdir());
    }
    return f;
}

public void test_longReadlink() throws Exception {
    File base = createTemporaryDirectory();
    File target = createDeepStructure(base);
    File source = new File(base, "source");
    assertFalse(source.exists());
    assertTrue(target.exists());
    assertTrue(target.getCanonicalPath().length() <= 159);
    
    File exceedingPath = new File(base, "a".repeat(160));
    assertFalse(exceedingPath.exists());
    
    ln_s(target, source);
    assertTrue(source.exists());
    assertEquals(target.getCanonicalPath(), source.getCanonicalPath());
    
    assertTrue(exceedingPath.length() > 159);
}

//<End of snippet n. 0>