/*Fix Ant tasks to only generate R class when needed.

If a project or library doesn't have resources, then
the R.txt file will not be there and we need to support
this.

Change-Id:I97d971473fc3cbbe9e5e1d16d440084ac4e90406*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecTask.java b/anttasks/src/com/android/ant/AaptExecTask.java
//Synthetic comment -- index c82f0d6..76a6339 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ant;

import com.android.sdklib.internal.build.SymbolLoader;
import com.android.sdklib.internal.build.SymbolWriter;

//Synthetic comment -- @@ -678,32 +679,38 @@
// but only if the project is not a library.
try {
if (!mNonConstantId && libPkgProp != null && !libPkgProp.isEmpty()) {
                SymbolLoader symbolValues = new SymbolLoader(new File(mBinFolder, "R.txt"));
                symbolValues.load();

                // we have two props which contains list of items. Both items reprensent 2 data of
                // a single property.
                // Don't want to use guava's splitter because it doesn't provide a list of the
                // result. but we know the list starts with a ; so strip it.
                if (libPkgProp.startsWith(";")) {
                    libPkgProp = libPkgProp.substring(1).trim();
                }
                String[] packages = libPkgProp.split(";");
                String[] rFiles = libRFileProp.list();

                if (packages.length != rFiles.length) {
                    throw new BuildException(String.format(
                            "%1$s and %2$s must contain the same number of items.",
                            mLibraryPackagesRefid, mLibraryRFileRefid));
                }

                for (int i = 0 ; i < packages.length ; i++) {
                    SymbolLoader symbols = new SymbolLoader(new File(rFiles[i]));
                    symbols.load();

                    SymbolWriter writer = new SymbolWriter(mRFolder, packages[i],
                            symbols, symbolValues);
                    writer.write();
}
}
} catch (IOException e) {







