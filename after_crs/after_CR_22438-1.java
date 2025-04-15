/*When possible use File.setExecutable instead of doing an exec of chmod.

Change-Id:I2b2f79bb07e277a1f2caa62b4100cbd8f7e8328d*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
//Synthetic comment -- index dfd2587..52a5f91 100755

//Synthetic comment -- @@ -22,6 +22,8 @@
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
//Synthetic comment -- @@ -30,6 +32,21 @@
abstract class OsHelper {

/**
     * Reflection method for File.setExecutable(boolean, boolean). Only present in Java 6.
     */
    private static Method sFileSetExecutable = null;
    /**
     * Whether File.setExecutable was queried through reflection. This is done to only
     * attempt reflection once.
     */
    private static boolean sFileReflectionDone = false;
    /**
     * Parameters to call File.setExecutable through reflection.
     */
    private final static Object[] sFileSetExecutableParams = new Object[] {
        Boolean.TRUE, Boolean.FALSE };

    /**
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
//Synthetic comment -- @@ -78,6 +95,8 @@
/**
* Sets the executable Unix permission (+x) on a file or folder.
* <p/>
     * This attempts to use {@link File#setExecutable(boolean, boolean)} through reflection if
     * it's available
* This invokes a chmod exec, so there is no guarantee of it being fast.
* Caller must make sure to not invoke this under Windows.
*
//Synthetic comment -- @@ -85,9 +104,37 @@
* @throws IOException If an I/O error occurs
*/
static void setExecutablePermission(File file) throws IOException {
        if (sFileReflectionDone == false) {
            if (sFileSetExecutable == null) {
                try {
                    sFileSetExecutable = File.class.getMethod("setExecutable", //$NON-NLS-1$
                            boolean.class, boolean.class);

                } catch (SecurityException e) {
                    // do nothing we'll use chdmod instead
                } catch (NoSuchMethodException e) {
                    // do nothing we'll use chdmod instead
                }
            }
            sFileReflectionDone = true;
        }

        if (sFileSetExecutable != null) {
            try {
                sFileSetExecutable.invoke(file, sFileSetExecutableParams);
                return;
            } catch (IllegalArgumentException e) {
                // we'll run chmod below
            } catch (IllegalAccessException e) {
                // we'll run chmod below
            } catch (InvocationTargetException e) {
                // we'll run chmod below
            }
        }

Runtime.getRuntime().exec(new String[] {
               "chmod", "+x", file.getAbsolutePath()  //$NON-NLS-1$ //$NON-NLS-2$
            });
}

/**







