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
//Synthetic comment -- index 968f021..997175f 100644

//Synthetic comment -- @@ -855,15 +855,17 @@
}

/**
     * Creates the directory named by the trailing filename of this file. Does
     * not create the complete path required to create this directory.
*
* <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
*
     * @return {@code true} if the directory has been created, {@code false}
     *         otherwise.
     * @see #mkdirs
*/
public boolean mkdir() {
try {
//Synthetic comment -- @@ -876,36 +878,42 @@
}

/**
     * Creates the directory named by the trailing filename of this file,
     * including the complete directory path required to create this directory.
*
* <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
*
     * @return {@code true} if the necessary directories have been created,
     *         {@code false} if the target directory already exists or one of
     *         the directories can not be created.
     * @see #mkdir
*/
public boolean mkdirs() {
        /* If the terminal directory already exists, answer false */
if (exists()) {
return false;
}

        /* If the receiver can be created, answer true */
if (mkdir()) {
return true;
}

        String parentDir = getParent();
        /* If there is no parent and we were not created, answer false */
        if (parentDir == null) {
            return false;
        }

        /* Otherwise, try to create a parent directory and then this directory */
        return (new File(parentDir).mkdirs() && mkdir());
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/FileTest.java b/luni/src/test/java/libcore/java/io/FileTest.java
//Synthetic comment -- index 3cf621e..143765e 100644

//Synthetic comment -- @@ -225,4 +225,33 @@
assertTrue(new File("/").getTotalSpace() >= 0);
assertTrue(new File("/").getUsableSpace() >= 0);
}
}







