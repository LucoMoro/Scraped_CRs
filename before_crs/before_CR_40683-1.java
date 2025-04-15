/*36175: Lint crashes with java.nio.BufferUnderflowException

This CL adds some code to more gracefully handle
problems with the Api Checker's binary cache.

Change-Id:I02c86ab16b857b88df8945c84cb92c7217a6144a*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index b53e337..5cf2603 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -130,6 +131,17 @@
}
}

/**
* Returns an instance of the API database
*
//Synthetic comment -- @@ -146,19 +158,12 @@
return null;
}

        String name = xmlFile.getName();
        if (LintUtils.endsWith(name, DOT_XML)) {
            name = name.substring(0, name.length() - DOT_XML.length());
        }
File cacheDir = client.getCacheDir(true/*create*/);
if (cacheDir == null) {
cacheDir = xmlFile.getParentFile();
}

        File binaryData = new File(cacheDir, name
                // Incorporate version number in the filename to avoid upgrade filename
                // conflicts on Windows (such as issue #26663)
                + "-" + BINARY_FORMAT_VERSION + ".bin"); //$NON-NLS-1$ //$NON-NLS-2$

if (DEBUG_FORCE_REGENERATE_BINARY) {
System.err.println("\nTemporarily regenerating binary data unconditionally \nfrom "
//Synthetic comment -- @@ -166,7 +171,8 @@
if (!createCache(client, xmlFile, binaryData)) {
return null;
}
        } else if (!binaryData.exists() || binaryData.lastModified() < xmlFile.lastModified()) {
if (!createCache(client, xmlFile, binaryData)) {
return null;
}
//Synthetic comment -- @@ -308,7 +314,10 @@
// the offset array separately.
// TODO: Investigate (profile) accessing the byte buffer directly instead of
// accessing a byte array.
        } catch (IOException e) {
mClient.log(e, null);
}
if (WRITE_STATS) {
//Synthetic comment -- @@ -523,6 +532,9 @@
byte[] b = new byte[size];
buffer.rewind();
buffer.get(b);
FileOutputStream output = Files.newOutputStreamSupplier(file).getOutput();
output.write(b);
output.close();
//Synthetic comment -- @@ -803,4 +815,10 @@

return -1;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 96fb214..2e55533 100644

//Synthetic comment -- @@ -17,6 +17,12 @@
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class ApiLookupTest extends AbstractCheckTest {
//Synthetic comment -- @@ -86,4 +92,107 @@
fail("This is not used in the ApiDatabase test");
return null;
}
}







