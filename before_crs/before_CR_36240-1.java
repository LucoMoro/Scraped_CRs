/*Libcore : Limit change for directory path length

Modify test_longReadlink to only test up to a maximum of 159
characters for yaffs2.

Change-Id:Iab9481e23c916792826f124783cf5b1a3045219c*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/FileTest.java b/luni/src/test/java/libcore/java/io/FileTest.java
//Synthetic comment -- index d59111d..deb648c 100644

//Synthetic comment -- @@ -42,13 +42,13 @@
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
//Synthetic comment -- @@ -63,13 +63,14 @@
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







