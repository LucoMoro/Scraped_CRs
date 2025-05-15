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

String libPkgProp = null;
Path libRFileProp = null;

// Method to validate package names against the app package
private void validatePackageNames(String appPackage, List<String> libraryPackages) throws BuildException {
    for (String libPackage : libraryPackages) {
        if (appPackage.equals(libPackage)) {
            throw new BuildException(String.format("Package name conflict detected: '%s' matches library package '%s'. Aborting.", appPackage, libPackage));
        }
    }
}

// Check for R class generation requirements
if (shouldGenerateRClass) {
    validatePackageNames(mManifestPackage, mLibraryPackages);

    task.createArg().setValue("-M");
    task.createArg().setValue(mManifest);
}

// Rename manifest package
if (mManifestPackage != null) {
    task.createArg().setValue("--rename-manifest-package");
    task.setProject(taskProject);
    task.setOwningTarget(getOwningTarget());
}

// Execute it
task.execute();

// Now if the project has libraries, R needs to be created for each library
try {
    for (int i = 0; i < packages.length; i++) {
        File libRFile = new File(rFiles[i]);
        if (libRFile.isFile()) {
            SymbolLoader symbols = new SymbolLoader(libRFile);
            symbols.load();

            SymbolWriter writer = new SymbolWriter(mRFolder, packages[i], symbols, symbolValues);
            writer.write();
        }
    }
} catch (IOException e) {
    throw new BuildException("Error occurred while writing R files: " + e.getMessage(), e);
}

//<End of snippet n. 0>