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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index c391b1c..5a10ee4 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -163,28 +164,26 @@
boolean runProguard = false;
List<File> proguardConfigFiles = null;
if (proguardConfig != null && proguardConfig.length() > 0) {
// Be tolerant with respect to file and path separators just like
// Ant is. Allow "/" in the property file to mean whatever the file
// separator character is:
if (File.separatorChar != '/' && proguardConfig.indexOf('/') != -1) {
proguardConfig = proguardConfig.replace('/', File.separatorChar);
}
                // Also split path: no need to convert to File.pathSeparator because we'll
                // be splitting the path ourselves right here, so just ensure that both
                // ':' and ';' work:
                if (proguardConfig.indexOf(';') != -1) {
                    proguardConfig = proguardConfig.replace(';', ':');
                }
                String[] paths = proguardConfig.split(":"); //$NON-NLS-1$

for (String path : paths) {
                    if (path.startsWith(SDK_PROPERTY_REF)) {
                        path = AdtPrefs.getPrefs().getOsSdkFolder() +
                                path.substring(SDK_PROPERTY_REF.length());
                    } else if (path.startsWith(HOME_PROPERTY_REF)) {
                        path = System.getProperty(HOME_PROPERTY) +
                                path.substring(HOME_PROPERTY_REF.length());
                    }
File proguardConfigFile = new File(path);
if (proguardConfigFile.isAbsolute() == false) {
proguardConfigFile = new File(project.getLocation().toFile(), path);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 12ac36f..938234a 100644

//Synthetic comment -- @@ -15,6 +15,13 @@
*/
package com.android.ide.eclipse.adt;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -142,4 +149,48 @@
AdtUtils.stripSuffix("LinearLayout_LayoutParams", "Params"));
assertEquals("Foo", AdtUtils.stripSuffix("Foo", "Bar"));
}
}







