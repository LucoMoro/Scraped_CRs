//<Beginning of snippet n. 0>

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
*/

// Unique package handling validation
String libPkgProp = null;
Path libRFileProp = null;

// Check if the parameters indicate generation of the R class and valid library names
if (task.getOwningTarget() != null) {
    task.createArg().setValue("-M");
    task.createArg().setValue(mManifest);
}

// Rename manifest package
if (mManifestPackage != null) {
    task.createArg().setValue("--rename-manifest-package");
}

// Execute task
task.setProject(taskProject);
task.setOwningTarget(getOwningTarget());
try {
    task.execute();
} catch (BuildException e) {
    throw new BuildException("Error executing the task: " + e.getMessage(), e);
}

// Validate libraries for R class creation
if (!packagesAreValid()) {
    throw new BuildException("Library packages must not share the same name.");
}
for (int i = 0; i < packages.length; i++) {
    File libRFile = new File(rFiles[i]);
    if (libRFile.isFile()) {
        SymbolLoader symbols = new SymbolLoader(libRFile);
        symbols.load();

        SymbolWriter writer = new SymbolWriter(mRFolder, packages[i], symbols, symbolValues);
        writer.write();
    } else {
        throw new BuildException("Library R file does not exist: " + libRFile.getAbsolutePath());
    }
}

// Method to validate package names
private boolean packagesAreValid() {
    // Logic to check for unique package names
    // (Implementation required here)
    return true;  // Placeholder return for compilation
}

//<End of snippet n. 0>