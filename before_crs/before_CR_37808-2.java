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

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -949,4 +951,39 @@

return sEclipse4;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index c391b1c..cf59d7b 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -169,14 +170,8 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 12ac36f..b89d56a 100644

//Synthetic comment -- @@ -15,6 +15,10 @@
*/
package com.android.ide.eclipse.adt;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -142,4 +146,29 @@
AdtUtils.stripSuffix("LinearLayout_LayoutParams", "Params"));
assertEquals("Foo", AdtUtils.stripSuffix("Foo", "Bar"));
}
}







