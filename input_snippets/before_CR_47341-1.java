
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755


package com.android.ant;

import com.android.SdkConstants;
import com.android.sdklib.internal.build.SymbolLoader;
import com.android.sdklib.internal.build.SymbolWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
* Task to execute aapt.
*

String libPkgProp = null;
Path libRFileProp = null;

// if the parameters indicate generation of the R class, check if
// more R classes need to be created for libraries, only if this project itself
task.createArg().setValue("-M");
task.createArg().setValue(mManifest);
}

// Rename manifest package
if (mManifestPackage != null) {
task.createArg().setValue("--rename-manifest-package");
task.setProject(taskProject);
task.setOwningTarget(getOwningTarget());

        // execute it.
        task.execute();

// now if the project has libraries, R needs to be created for each libraries
// but only if the project is not a library.
try {
"%1$s and %2$s must contain the same number of items.",
mLibraryPackagesRefid, mLibraryRFileRefid));
}

for (int i = 0 ; i < packages.length ; i++) {
File libRFile = new File(rFiles[i]);
if (libRFile.isFile()) {
SymbolLoader symbols = new SymbolLoader(libRFile);
symbols.load();

SymbolWriter writer = new SymbolWriter(mRFolder, packages[i],
symbols, symbolValues);
writer.write();
throw new BuildException(e);
}

}
}

//<End of snippet n. 0>








