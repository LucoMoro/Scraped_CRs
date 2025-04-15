/*Guard against badly configured projects in aidl processor.

The project could have source folders that don't exist.

Change-Id:Ie8f9e35580918d9fade5074ca90d295c49e6cae4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index e05f8b7..09dd571 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.builders.BaseBuilder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -105,7 +105,9 @@
IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
for (IPath p : sourceFolders) {
IFolder f = wsRoot.getFolder(p);
            if (f.exists()) { // if the resource doesn't exist, getLocation will return null.
                command[index++] = quote("-I" + f.getLocation().toOSString()); //$NON-NLS-1$
            }
}

boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;







