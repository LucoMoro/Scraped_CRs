/*Rewrite File.mkdirs, improve the documentation, and add tests.

Every time anyone stumbles across the File.mkdirs implementation,
usually because of some other problem, it takes us a few minutes
of head-scratching to convince ourselves that the recusive case
is correct. So let's rewrite it to be a little less unclear.

Also improve the documentation and add some tests. Turns out the
only mkdirs tests were implicit (where it's used for test setup)
or for special cases (like the empty path). Duh.

Change-Id:I78376fb1aaa72223c63c581d18c6682a50c84e23*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/File.java b/luni/src/main/java/java/io/File.java
//Synthetic comment -- index 968f021..ac67e7c 100644

//Synthetic comment -- @@ -855,57 +855,65 @@
}

/**
     * Creates the directory named by this file, assuming its parents exist.
     * Use {@link #mkdirs} if you also want to create missing parents.
*
* <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value. Note also that this method returns
     * false if the directory already existed. If you want to know whether the
     * directory exists on return, either use {@code (f.mkdir() || f.isDirectory())}
     * or simply ignore the return value from this method and simply call {@link #isDirectory}.
*
     * @return {@code true} if the directory was created,
     *         {@code false} on failure or if the directory already existed.
*/
public boolean mkdir() {
try {
            mkdirErrno();
return true;
} catch (ErrnoException errnoException) {
return false;
}
}

    private void mkdirErrno() throws ErrnoException {
        // On Android, we don't want default permissions to allow global access.
        Libcore.os.mkdir(path, S_IRWXU);
    }

/**
     * Creates the directory named by this file, creating missing parent
     * directories if necessary.
     * Use {@link #mkdir} if you don't want to create missing parents.
*
* <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value. Note also that this method returns
     * false if the directory already existed. If you want to know whether the
     * directory exists on return, either use {@code (f.mkdirs() || f.isDirectory())}
     * or simply ignore the return value from this method and simply call {@link #isDirectory}.
*
     * @return {@code true} if the directory was created,
     *         {@code false} on failure or if the directory already existed.
*/
public boolean mkdirs() {
        return mkdirs(false);
    }

    private boolean mkdirs(boolean resultIfExists) {
        try {
            // Try to create the directory directly.
            mkdirErrno();
return true;
        } catch (ErrnoException errnoException) {
            if (errnoException.errno == ENOENT) {
                // If the parent was missing, try to create it and then try again.
                File parent = getParentFile();
                return parent != null && parent.mkdirs(true) && mkdir();
            } else if (errnoException.errno == EEXIST) {
                return resultIfExists;
            }
return false;
}
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/FileTest.java b/luni/src/test/java/libcore/java/io/FileTest.java
//Synthetic comment -- index 3cf621e..b2391ac 100644

//Synthetic comment -- @@ -225,4 +225,45 @@
assertTrue(new File("/").getTotalSpace() >= 0);
assertTrue(new File("/").getUsableSpace() >= 0);
}

    public void test_mkdirs() throws Exception {
        // Set up a directory to test in.
        File base = createTemporaryDirectory();

        // mkdirs returns true only if it _creates_ a directory.
        // So we get false for a directory that already exists...
        assertTrue(base.exists());
        assertFalse(base.mkdirs());
        // But true if we had to create something.
        File a = new File(base, "a");
        assertFalse(a.exists());
        assertTrue(a.mkdirs());
        assertTrue(a.exists());

        // Test the recursive case where we need to create multiple parents.
        File b = new File(a, "b");
        File c = new File(b, "c");
        File d = new File(c, "d");
        assertTrue(a.exists());
        assertFalse(b.exists());
        assertFalse(c.exists());
        assertFalse(d.exists());
        assertTrue(d.mkdirs());
        assertTrue(a.exists());
        assertTrue(b.exists());
        assertTrue(c.exists());
        assertTrue(d.exists());

        // Test the case where the 'directory' exists as a file.
        File existsAsFile = new File(base, "existsAsFile");
        existsAsFile.createNewFile();
        assertTrue(existsAsFile.exists());
        assertFalse(existsAsFile.mkdirs());

        // Test the case where the parent exists as a file.
        File badParent = new File(existsAsFile, "sub");
        assertTrue(existsAsFile.exists());
        assertFalse(badParent.exists());
        assertFalse(badParent.mkdirs());
    }
}







