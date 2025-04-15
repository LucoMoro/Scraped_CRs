/*Fix AttrParser tests on Windows.

Change-Id:I7c73b484db07aa06c6c4812672f88242b34c1e3e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java
//Synthetic comment -- index 6ff02ea..b4420e4 100644

//Synthetic comment -- @@ -16,6 +16,8 @@
package com.android.ide.eclipse.tests;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
//Synthetic comment -- @@ -36,6 +38,10 @@
private static AdtTestData sInstance = null;
private static final Logger sLogger = Logger.getLogger(AdtTestData.class.getName());

    /** The prefered directory separator to use. */
    private static final String DIR_SEP_STR  = "/";
    private static final char   DIR_SEP_CHAR = '/';

/** The absolute file path to the plugin's contents. */
private String mOsRootDataPath;

//Synthetic comment -- @@ -54,6 +60,20 @@
sLogger.info("Running as an Eclipse Plug-in JUnit test, using FileLocator");
try {
mOsRootDataPath = FileLocator.resolve(url).getFile();
                    if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS) {
                        // Fix the path returned by the URL resolver
                        // so that it actually works on Windows.

                        // First, Windows paths don't start with a / especially
                        // if they contain a drive spec such as C:/...
                        int pos = mOsRootDataPath.indexOf(':');
                        if (pos > 0 && mOsRootDataPath.charAt(0) == '/') {
                            mOsRootDataPath = mOsRootDataPath.substring(1);
                        }

                        // Looking for "." probably inserted a /./, so clean it up
                        mOsRootDataPath = mOsRootDataPath.replace("/./", "/");
                    }
} catch (IOException e) {
sLogger.warning("IOException while using FileLocator, reverting to url");
mOsRootDataPath = url.getFile();
//Synthetic comment -- @@ -68,9 +88,14 @@
sLogger.warning("Resource data not found using class loader!, Defaulting to no path");
}

        if (File.separatorChar == '\\') {
            // On Windows, uniformize all separators to use the / convention
            mOsRootDataPath.replace('\\', DIR_SEP_CHAR);
        }

        if (!mOsRootDataPath.endsWith(File.separator) && !mOsRootDataPath.endsWith(DIR_SEP_STR)) {
sLogger.info("Fixing test_data env variable (does not end with path separator)");
            mOsRootDataPath += DIR_SEP_STR;
}
}

//Synthetic comment -- @@ -91,6 +116,20 @@
* @return absolute OS path to test file
*/
public String getTestFilePath(String osRelativePath) {
        File path = new File(mOsRootDataPath, osRelativePath);

        if (!path.exists()) {
            // On Windows at least this ends up using the wrong plugin path.
            String pkgAdt   = AdtPlugin  .class.getPackage().getName();
            String pkgTests = AdtTestData.class.getPackage().getName();

            if (mOsRootDataPath.contains(pkgAdt)) {
                path = new File(mOsRootDataPath.replace(pkgAdt, pkgTests), osRelativePath);
            }

            assert path.exists();
        }

        return path.getAbsolutePath();
}
}







