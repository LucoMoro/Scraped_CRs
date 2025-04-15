/*Fix AttrParser tests on Windows.

Change-Id:I7c73b484db07aa06c6c4812672f88242b34c1e3e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java
//Synthetic comment -- index 6ff02ea..b4420e4 100644

//Synthetic comment -- @@ -16,6 +16,8 @@
package com.android.ide.eclipse.tests;

import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
//Synthetic comment -- @@ -36,6 +38,10 @@
private static AdtTestData sInstance = null;
private static final Logger sLogger = Logger.getLogger(AdtTestData.class.getName());

/** The absolute file path to the plugin's contents. */
private String mOsRootDataPath;

//Synthetic comment -- @@ -54,6 +60,20 @@
sLogger.info("Running as an Eclipse Plug-in JUnit test, using FileLocator");
try {
mOsRootDataPath = FileLocator.resolve(url).getFile();
} catch (IOException e) {
sLogger.warning("IOException while using FileLocator, reverting to url");
mOsRootDataPath = url.getFile();
//Synthetic comment -- @@ -68,9 +88,14 @@
sLogger.warning("Resource data not found using class loader!, Defaulting to no path");
}

        if (!mOsRootDataPath.endsWith(File.separator)) {
sLogger.info("Fixing test_data env variable (does not end with path separator)");
            mOsRootDataPath = mOsRootDataPath.concat(File.separator);
}
}

//Synthetic comment -- @@ -91,6 +116,20 @@
* @return absolute OS path to test file
*/
public String getTestFilePath(String osRelativePath) {
        return mOsRootDataPath + osRelativePath;
}
}







