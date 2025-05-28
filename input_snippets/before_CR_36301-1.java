
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

}

private static File createDeepStructure(File base) throws Exception {
        // ext has a limit of around 256 characters for each path entry.
        // 128 characters should be safe for everything but FAT.
        String longString = longString(128);
        // Keep creating subdirectories until the path length is greater than 1KiB.
// Ubuntu 8.04's kernel is happy up to about 4KiB.
File f = base;
        for (int i = 0; f.toString().length() <= 1024; ++i) {
f = new File(f, longString);
assertTrue(f.mkdir());
}
}

// readlink(2) is a special case,.
public void test_longReadlink() throws Exception {
File base = createTemporaryDirectory();
File target = createDeepStructure(base);
File source = new File(base, "source");
assertFalse(source.exists());
assertTrue(target.exists());
        assertTrue(target.getCanonicalPath().length() > 1024);
ln_s(target, source);
assertTrue(source.exists());
assertEquals(target.getCanonicalPath(), source.getCanonicalPath());

//<End of snippet n. 0>








