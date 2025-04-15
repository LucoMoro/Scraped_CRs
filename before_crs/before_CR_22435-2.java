/*When unarchiving install, files that need +x don't need +w too.

Change-Id:I860c28fa979a1d8673abd9690014b8b28c6e8d7e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
//Synthetic comment -- index 02cebe1..dfd2587 100755

//Synthetic comment -- @@ -76,7 +76,7 @@
}

/**
     * Sets the executable Unix permission (0777) on a file or folder.
* <p/>
* This invokes a chmod exec, so there is no guarantee of it being fast.
* Caller must make sure to not invoke this under Windows.
//Synthetic comment -- @@ -86,7 +86,7 @@
*/
static void setExecutablePermission(File file) throws IOException {
Runtime.getRuntime().exec(new String[] {
           "chmod", "777", file.getAbsolutePath()
});
}








