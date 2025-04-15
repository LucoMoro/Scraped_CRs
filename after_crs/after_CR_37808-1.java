/*28860: Default proguard.config invalid on Windows

Attempt to work with ambiguous ProGuard path definitions
(e.g. where : is used both as a path separator and in
paths like D:\foo\bar)

Change-Id:I63c3488b59ebfeb44e92ab84ec302932947ab909*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index d25b27c..7183231 100644

//Synthetic comment -- @@ -949,4 +949,55 @@

return sEclipse4;
}

    /**
     * Splits the given path into its individual parts. This method tries to be smart
     * about path separators (; and :) such that it can handle possibly ambiguous
     * paths, such as {@code c:\foo\bar:\other}.
     *
     * @param path the path variable to split, which can use both : and ; as path separators.
     * @return the individual path components
     */
    public static String[] splitPath(String path) {
        // Also split path: no need to convert to File.pathSeparator because we'll
        // be splitting the path ourselves right here, so just ensure that both
        // ':' and ';' work:
        if (path.indexOf(';') != -1) {
            path = path.replace(';', ':');
        }
        String[] paths = path.split(":"); //$NON-NLS-1$

        List<String> combined = new ArrayList<String>(paths.length);
        loop:
        for (int i = 0, n = paths.length; i < n; i++) {
            String p = paths[i];
            if (p.length() == 0) {
                continue;
            } else if (p.length() == 1 && i < n - 1) {
                // If a path component is a single letter, it is likely a Windows drive
                // letter, e.g. "C:\Foo\Bar" => "C", "\Foo\Bar" and these should be recombined.
                combined.add(p + ':' + paths[i + 1]);
                i++;
                continue;
            }
            File file = new File(p);
            if (file.isFile()) {
                combined.add(file.getPath());
            } else {
                // Try to combine files forwards
                File f = file;
                for (int j = i + 1; j < paths.length; j++) {
                    f = new File(f.getPath() + ':' + paths[j]);
                    if (f.isFile()) {
                        combined.add(f.getPath());
                        i = j;
                        continue loop;
                    }
                }
                combined.add(file.getPath());
            }
        }

        return combined.toArray(new String[combined.size()]);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index c391b1c..5a10ee4 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -163,28 +164,26 @@
boolean runProguard = false;
List<File> proguardConfigFiles = null;
if (proguardConfig != null && proguardConfig.length() > 0) {
                // Replace the path prefixes for ${sdk.dir} etc such that
                // proguardConfig holds a real, expanded path
                if (proguardConfig.contains(SDK_PROPERTY_REF)) {
                    proguardConfig = proguardConfig.replace(SDK_PROPERTY_REF,
                            AdtPrefs.getPrefs().getOsSdkFolder());
                }
                if (proguardConfig.contains(HOME_PROPERTY_REF)) {
                    proguardConfig = proguardConfig.replace(HOME_PROPERTY_REF,
                            System.getProperty(HOME_PROPERTY));
                }

// Be tolerant with respect to file and path separators just like
// Ant is. Allow "/" in the property file to mean whatever the file
// separator character is:
if (File.separatorChar != '/' && proguardConfig.indexOf('/') != -1) {
proguardConfig = proguardConfig.replace('/', File.separatorChar);
}
                String[] paths = AdtUtils.splitPath(proguardConfig);

for (String path : paths) {
File proguardConfigFile = new File(path);
if (proguardConfigFile.isAbsolute() == false) {
proguardConfigFile = new File(project.getLocation().toFile(), path);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 12ac36f..938234a 100644

//Synthetic comment -- @@ -15,6 +15,13 @@
*/
package com.android.ide.eclipse.adt;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -142,4 +149,48 @@
AdtUtils.stripSuffix("LinearLayout_LayoutParams", "Params"));
assertEquals("Foo", AdtUtils.stripSuffix("Foo", "Bar"));
}

    public void testSplitPath() throws Exception {
        assertTrue(Arrays.equals(new String[] { "foo", "bar" },
                AdtUtils.splitPath("foo:bar")));

        assertTrue(Arrays.equals(new String[] { "/foo", "/bar" },
                AdtUtils.splitPath("/foo;/bar")));

        assertTrue(Arrays.equals(new String[] { "C:\foo", "/bar" },
                AdtUtils.splitPath("C:\foo:/bar")));

        File tempDir = Files.createTempDir();
        try {
            File path1 = new File(tempDir, "foo");
            File path2 = new File(tempDir, "f/foo:bar:baz");
            File path3 = new File(tempDir, "test/myfile");
            path1.getParentFile().mkdirs();
            path2.getParentFile().mkdirs();
            path3.getParentFile().mkdirs();
            Files.write("test", path1, Charsets.US_ASCII);
            Files.write("test", path2, Charsets.US_ASCII);
            Files.write("test", path3, Charsets.US_ASCII);
            String path = path1.getPath() + ':' + path2.getPath() + ':' + path3.getPath();

            String[] split = AdtUtils.splitPath(path);
            assertEquals(Arrays.toString(split), 3, split.length);
            assertEquals(path1.getPath(), split[0]);
            assertEquals(path2.getPath(), split[1]);
            assertEquals(path3.getPath(), split[2]);

            path = path1.getPath() + ';' + path2.getPath() + ';' + path3.getPath();
            split = AdtUtils.splitPath(path);
            assertEquals(Arrays.toString(split), 3, split.length);
            assertEquals(path1.getPath(), split[0]);
            assertEquals(path2.getPath(), split[1]);
            assertEquals(path3.getPath(), split[2]);
        } finally {
            try {
                Files.deleteRecursively(tempDir);
            } catch (IOException ioe) {
                // pass
            }
        }
    }
}







