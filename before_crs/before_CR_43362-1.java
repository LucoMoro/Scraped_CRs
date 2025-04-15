/*Fix import statement

Change-Id:I09062e0d9982d4a42580693dc8cf0941d65597b5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java
//Synthetic comment -- index d786c04..710d257 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -174,7 +174,7 @@
Object args = mArgConstructor.newInstance();
mArgOutName.set(args, osOutFilePath);
mArgFileNames.set(args, osFilenames.toArray(new String[osFilenames.size()]));
            mArgJarOutput.set(args, osOutFilePath.endsWith(AdtConstants.DOT_JAR));
mArgVerbose.set(args, verbose);

// call the run method







