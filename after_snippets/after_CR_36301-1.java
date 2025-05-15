
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

}

private static File createDeepStructure(File base) throws Exception {
	// 64 character string length should be safe for everything.
	int strlen = 64;
	String longString = longString(strlen);
	// Keep creating subdirectories until the path length is max of 159 bytes.
// Ubuntu 8.04's kernel is happy up to about 4KiB.
File f = base;
        for (int i = 0;( f.toString().length() + strlen) <= 159; ++i) {
f = new File(f, longString);
assertTrue(f.mkdir());
}
}

// readlink(2) is a special case,.
    //Modify test_longReadlink to only test up to a maximum of 159 characters for yaffs2.
public void test_longReadlink() throws Exception {
File base = createTemporaryDirectory();
File target = createDeepStructure(base);
File source = new File(base, "source");
assertFalse(source.exists());
assertTrue(target.exists());
        assertTrue(target.getCanonicalPath().length() <= 159);
ln_s(target, source);
assertTrue(source.exists());
assertEquals(target.getCanonicalPath(), source.getCanonicalPath());

//<End of snippet n. 0>








