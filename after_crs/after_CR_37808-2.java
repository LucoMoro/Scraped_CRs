/*28860: Default proguard.config invalid on Windows

Attempt to work with ambiguous ProGuard path definitions
(e.g. where : is used both as a path separator and in
paths like D:\foo\bar)

Change-Id:I63c3488b59ebfeb44e92ab84ec302932947ab909*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index d25b27c..18d488d 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -949,4 +951,39 @@

return sEclipse4;
}

    /**
     * Splits the given path into its individual parts, attempting to be
     * tolerant about path separators (: or ;). It can handle possibly ambiguous
     * paths, such as {@code c:\foo\bar:\other}, though of course these are to
     * be avoided if possible.
     *
     * @param path the path variable to split, which can use both : and ; as
     *            path separators.
     * @return the individual path components as an iterable of strings
     */
    public static Iterable<String> splitPath(String path) {
        if (path.indexOf(';') != -1) {
            return Splitter.on(';').omitEmptyStrings().trimResults().split(path);
        }

        List<String> combined = new ArrayList<String>();
        Iterables.addAll(combined, Splitter.on(':').omitEmptyStrings().trimResults().split(path));
        for (int i = 0, n = combined.size(); i < n; i++) {
            String p = combined.get(i);
            if (p.length() == 1 && i < n - 1 && Character.isLetter(p.charAt(0))
                    // Technically, Windows paths do not have to have a \ after the :,
                    // which means it would be using the current directory on that drive,
                    // but that's unlikely to be the case in a path since it would have
                    // unpredictable results
                    && !combined.get(i+1).isEmpty() && combined.get(i+1).charAt(0) == '\\') {
                combined.set(i, p + ':' + combined.get(i+1));
                combined.remove(i+1);
                n--;
                continue;
            }
        }

        return combined;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index c391b1c..cf59d7b 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -169,14 +170,8 @@
if (File.separatorChar != '/' && proguardConfig.indexOf('/') != -1) {
proguardConfig = proguardConfig.replace('/', File.separatorChar);
}

                Iterable<String> paths = AdtUtils.splitPath(proguardConfig);
for (String path : paths) {
if (path.startsWith(SDK_PROPERTY_REF)) {
path = AdtPrefs.getPrefs().getOsSdkFolder() +








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 12ac36f..b89d56a 100644

//Synthetic comment -- @@ -15,6 +15,10 @@
*/
package com.android.ide.eclipse.adt;

import com.google.common.collect.Iterables;

import java.util.Arrays;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -142,4 +146,29 @@
AdtUtils.stripSuffix("LinearLayout_LayoutParams", "Params"));
assertEquals("Foo", AdtUtils.stripSuffix("Foo", "Bar"));
}

    public void testSplitPath() throws Exception {
        assertTrue(Arrays.equals(new String[] { "/foo", "/bar", "/baz" },
                Iterables.toArray(AdtUtils.splitPath("/foo:/bar:/baz"), String.class)));

        assertTrue(Arrays.equals(new String[] { "/foo", "/bar" },
                Iterables.toArray(AdtUtils.splitPath("/foo;/bar"), String.class)));

        assertTrue(Arrays.equals(new String[] { "/foo", "/bar:baz" },
                Iterables.toArray(AdtUtils.splitPath("/foo;/bar:baz"), String.class)));

        assertTrue(Arrays.equals(new String[] { "\\foo\\bar", "\\bar\\foo" },
                Iterables.toArray(AdtUtils.splitPath("\\foo\\bar;\\bar\\foo"), String.class)));

        assertTrue(Arrays.equals(new String[] { "${sdk.dir}\\foo\\bar", "\\bar\\foo" },
                Iterables.toArray(AdtUtils.splitPath("${sdk.dir}\\foo\\bar;\\bar\\foo"),
                        String.class)));

        assertTrue(Arrays.equals(new String[] { "${sdk.dir}/foo/bar", "/bar/foo" },
                Iterables.toArray(AdtUtils.splitPath("${sdk.dir}/foo/bar:/bar/foo"),
                        String.class)));

        assertTrue(Arrays.equals(new String[] { "C:\\foo", "/bar" },
                Iterables.toArray(AdtUtils.splitPath("C:\\foo:/bar"), String.class)));
    }
}







