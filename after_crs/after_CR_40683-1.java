/*36175: Lint crashes with java.nio.BufferUnderflowException

This CL adds some code to more gracefully handle
problems with the Api Checker's binary cache.

Change-Id:I02c86ab16b857b88df8945c84cb92c7217a6144a*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index b53e337..5cf2603 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -130,6 +131,17 @@
}
}

    @VisibleForTesting
    static String getCacheFileName(String xmlFileName) {
        if (LintUtils.endsWith(xmlFileName, DOT_XML)) {
            xmlFileName = xmlFileName.substring(0, xmlFileName.length() - DOT_XML.length());
        }

        // Incorporate version number in the filename to avoid upgrade filename
        // conflicts on Windows (such as issue #26663)
        return xmlFileName + '-' + BINARY_FORMAT_VERSION + ".bin"; //$NON-NLS-1$
    }

/**
* Returns an instance of the API database
*
//Synthetic comment -- @@ -146,19 +158,12 @@
return null;
}

File cacheDir = client.getCacheDir(true/*create*/);
if (cacheDir == null) {
cacheDir = xmlFile.getParentFile();
}

        File binaryData = new File(cacheDir, getCacheFileName(xmlFile.getName()));

if (DEBUG_FORCE_REGENERATE_BINARY) {
System.err.println("\nTemporarily regenerating binary data unconditionally \nfrom "
//Synthetic comment -- @@ -166,7 +171,8 @@
if (!createCache(client, xmlFile, binaryData)) {
return null;
}
        } else if (!binaryData.exists() || binaryData.lastModified() < xmlFile.lastModified()
               || binaryData.length() == 0) {
if (!createCache(client, xmlFile, binaryData)) {
return null;
}
//Synthetic comment -- @@ -308,7 +314,10 @@
// the offset array separately.
// TODO: Investigate (profile) accessing the byte buffer directly instead of
// accessing a byte array.
        } catch (Throwable e) {
            mClient.log(null, "Failure reading binary cache file %1$s", mBinaryFile.getPath());
            mClient.log(null, "Please delete the file and restart the IDE/lint: %1$s",
                    mBinaryFile.getPath());
mClient.log(e, null);
}
if (WRITE_STATS) {
//Synthetic comment -- @@ -523,6 +532,9 @@
byte[] b = new byte[size];
buffer.rewind();
buffer.get(b);
        if (file.exists()) {
            file.delete();
        }
FileOutputStream output = Files.newOutputStreamSupplier(file).getOutput();
output.write(b);
output.close();
//Synthetic comment -- @@ -803,4 +815,10 @@

return -1;
}

    /** Clears out any existing lookup instances */
    @VisibleForTesting
    static void dispose() {
        sInstance.clear();
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 96fb214..2e55533 100644

//Synthetic comment -- @@ -17,6 +17,12 @@
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Severity;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;

@SuppressWarnings("javadoc")
public class ApiLookupTest extends AbstractCheckTest {
//Synthetic comment -- @@ -86,4 +92,107 @@
fail("This is not used in the ApiDatabase test");
return null;
}

    private File mCacheDir;
    private StringBuilder mLogBuffer = new StringBuilder();

    public void testCorruptedCacheHandling() throws Exception {
        ApiLookup lookup;

        // Real cache:
        mCacheDir = new TestLintClient().getCacheDir(true);
        mLogBuffer.setLength(0);
        lookup = ApiLookup.get(new LookupTestClient());
        assertEquals(11, lookup.getFieldVersion("android/R$attr", "actionMenuTextAppearance"));
        assertNotNull(lookup);
        assertEquals("", mLogBuffer.toString()); // No warnings
        ApiLookup.dispose();

        // Custom cache dir: should also work
        mCacheDir = new File(getTempDir(), "test-cache");
        mCacheDir.mkdirs();
        mLogBuffer.setLength(0);
        lookup = ApiLookup.get(new LookupTestClient());
        assertEquals(11, lookup.getFieldVersion("android/R$attr", "actionMenuTextAppearance"));
        assertNotNull(lookup);
        assertEquals("", mLogBuffer.toString()); // No warnings
        ApiLookup.dispose();

        // Now truncate cache file
        File cacheFile = new File(mCacheDir,
                ApiLookup.getCacheFileName("api-versions.xml")); //$NON-NLS-1$
        mLogBuffer.setLength(0);
        assertTrue(cacheFile.exists());
        RandomAccessFile raf = new RandomAccessFile(cacheFile, "rw");
        // Truncate file in half
        raf.setLength(100);  // Broken header
        raf.close();
        lookup = ApiLookup.get(new LookupTestClient());
        String message = mLogBuffer.toString();
        assertTrue(message.contains("Please delete the file and restart the IDE/lint:"));
        assertTrue(message.contains(mCacheDir.getPath()));
        ApiLookup.dispose();

        mLogBuffer.setLength(0);
        assertTrue(cacheFile.exists());
        raf = new RandomAccessFile(cacheFile, "rw");
        // Truncate file in half in the data portion
        raf.setLength(raf.length() / 2);
        raf.close();
        lookup = ApiLookup.get(new LookupTestClient());
        // This data is now truncated: lookup returns the wrong size.
        try {
            assertNotNull(lookup);
            lookup.getFieldVersion("android/R$attr", "actionMenuTextAppearance");
            fail("Can't look up bogus data");
        } catch (Throwable t) {
            // Expected this: the database is corrupted.
        }
        assertTrue(message.contains("Please delete the file and restart the IDE/lint:"));
        assertTrue(message.contains(mCacheDir.getPath()));
        ApiLookup.dispose();

        mLogBuffer.setLength(0);
        assertTrue(cacheFile.exists());
        raf = new RandomAccessFile(cacheFile, "rw");
        // Truncate file to 0 bytes
        raf.setLength(0);
        raf.close();
        lookup = ApiLookup.get(new LookupTestClient());
        assertEquals(11, lookup.getFieldVersion("android/R$attr", "actionMenuTextAppearance"));
        assertNotNull(lookup);
        assertEquals("", mLogBuffer.toString()); // No warnings
        ApiLookup.dispose();
    }

    private final class LookupTestClient extends TestLintClient {
        @Override
        public File getCacheDir(boolean create) {
            assertNotNull(mCacheDir);
            if (create && !mCacheDir.exists()) {
                mCacheDir.mkdirs();
            }
            return mCacheDir;
        }

        @Override
        public void log(Severity severity, Throwable exception, String format,
                Object... args) {
            if (format != null) {
                mLogBuffer.append(String.format(format, args));
                mLogBuffer.append('\n');
            }
            if (exception != null) {
                StringWriter writer = new StringWriter();
                exception.printStackTrace(new PrintWriter(writer));
                mLogBuffer.append(writer.toString());
                mLogBuffer.append('\n');
            }
        }

        @Override
        public void log(Throwable exception, String format, Object... args) {
            log(Severity.WARNING, exception, format, args);
        }
    }
}







