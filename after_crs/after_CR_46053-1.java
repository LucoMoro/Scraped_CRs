/*Gut dalvik.system.TemporaryDirectory, which has nothing to do with setting java.io.tmpdir.

This has been dead since at least Froyo, but shows up if you're searching
trying to find out who's really responsible for setting java.io.tmpdir.

Change-Id:I539ad9aeac4ba4556a491cddeddfb6fbc6766b5c*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/TemporaryDirectory.java b/dalvik/src/main/java/dalvik/system/TemporaryDirectory.java
//Synthetic comment -- index ff0f759..f8fb0b1 100644

//Synthetic comment -- @@ -19,79 +19,20 @@
import java.io.File;

/**
 * Obsolete, for binary compatibility only.
*
* @hide
*/
public class TemporaryDirectory {
/**
     * This method exists for binary compatibility only.
*/
public static void setUpDirectory(String baseDir) {
}

/**
     * This method exists for binary compatibility only.
*/
public static synchronized void setUpDirectory(File baseDir) {
}
}







