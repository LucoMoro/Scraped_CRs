/*Make System.loadLibrary use open(2) rather than stat(2).

This will let us remove read permission from directories containing .so files.

Bug: 6485312
Change-Id:I72daa265ce54747fc91cdb9d915a05a2464041bb*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 1253223..048cc83 100644

//Synthetic comment -- @@ -25,6 +25,11 @@
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
* A pair of lists of entries, associated with a {@code ClassLoader}.
//Synthetic comment -- @@ -154,36 +159,20 @@
* Helper for {@link #splitPaths}, which does the actual splitting
* and filtering and adding to a result.
*/
    private static void splitAndAdd(String path, boolean wantDirectories,
ArrayList<File> resultList) {
        if (path == null) {
return;
}

        String[] strings = path.split(Pattern.quote(File.pathSeparator));

        for (String s : strings) {
            File file = new File(s);

            if (! (file.exists() && file.canRead())) {
                continue;
            }

            /*
             * Note: There are other entities in filesystems than
             * regular files and directories.
             */
            if (wantDirectories) {
                if (!file.isDirectory()) {
                    continue;
}
            } else {
                if (!file.isFile()) {
                    continue;
                }
}

            resultList.add(file);
}
}

//Synthetic comment -- @@ -370,14 +359,12 @@
*/
public String findLibrary(String libraryName) {
String fileName = System.mapLibraryName(libraryName);

for (File directory : nativeLibraryDirectories) {
            File file = new File(directory, fileName);
            if (file.exists() && file.isFile() && file.canRead()) {
                return file.getPath();
}
}

return null;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index c4c12db..58142d3 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import libcore.io.Libcore;
import static libcore.io.OsConstants._SC_NPROCESSORS_CONF;

//Synthetic comment -- @@ -372,7 +373,8 @@
for (String directory : mLibPaths) {
String candidate = directory + filename;
candidates.add(candidate);
            if (new File(candidate).exists()) {
String error = nativeLoad(candidate, loader);
if (error == null) {
return; // We successfully loaded the library. Job done.








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/IoUtils.java b/luni/src/main/java/libcore/io/IoUtils.java
//Synthetic comment -- index 7177f3f..fb11c97 100644

//Synthetic comment -- @@ -147,6 +147,17 @@
}
}

public static void throwInterruptedIoException() throws InterruptedIOException {
// This is typically thrown in response to an
// InterruptedException which does not leave the thread in an







