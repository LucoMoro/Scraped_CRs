/*Ensure FolderWrapper only lists files and directories.

This matches the description of the original File.list better
and avoid us seeing non-file objects that we can't deal with
(e.g. pipes, etc.)

Change-Id:Ie47c9926c2db4cd2605d277fe847c6a60e180d09*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java
//Synthetic comment -- index a4601d3..33e31c1 100644

//Synthetic comment -- @@ -91,7 +91,7 @@
File f = files[i];
if (f.isFile()) {
afiles[i] = new FileWrapper(f);
                } else if (f.isDirectory()) {
afiles[i] = new FolderWrapper(f);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFolder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFolder.java
//Synthetic comment -- index bfbc86d..17c0dbd 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.io;

import java.io.File;

/**
*  A folder.
*/
//Synthetic comment -- @@ -44,23 +46,32 @@
boolean hasFile(String name);

/**
     * Returns an {@link IAbstractFile} representing a child of the current folder with the
* given name. The file may not actually exist.
* @param name the name of the file.
*/
IAbstractFile getFile(String name);

/**
     * Returns an {@link IAbstractFolder} representing a child of the current folder with the
* given name. The folder may not actually exist.
* @param name the name of the folder.
*/
IAbstractFolder getFolder(String name);

/**
     * Returns a list of all existing file and directory members in this folder.
     * The returned array can be empty but is never null.
*/
IAbstractResource[] listMembers();

    /**
     * Returns a list of all existing file and directory members in this folder
     * that satisfy the specified filter.
     *
     * @param filter A filename filter instance. Must not be null.
     * @return An array of file names (generated using {@link File#getName()}).
     *         The array can be empty but is never null.
     */
String[] list(FilenameFilter filter);
}







