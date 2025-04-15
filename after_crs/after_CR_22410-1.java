/*Make sure 'gen' folder is never derived.

That would delete it when cleaning the project which is not
good since it takes a while to refresh and to recreate it
through the ResourceManagerBuilder.

Change-Id:I4c050bdc37b8aa78abf7463baf8d19115b032fb2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 945cd93..943dcfe 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.build.AidlProcessor;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -526,6 +526,10 @@

// remove all the derived resources from the 'gen' source folder.
if (mGenFolder != null) {
            // gen folder should not be derived, but previous version could set it to derived
            // so we make sure this isn't the case (or it'll get deleted by the clean)
            mGenFolder.setDerived(false);

removeDerivedResources(mGenFolder, monitor);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java
//Synthetic comment -- index 8c4127e..950200a 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -191,7 +191,6 @@
"Creating 'gen' source folder for generated Java files");
genFolder.create(true /* force */, true /* local */,
new SubProgressMonitor(monitor, 10));
}

// add it to the source folder list, if needed only (or it will throw)







