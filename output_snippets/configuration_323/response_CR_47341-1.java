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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Task to execute aapt.
*/

String libPkgProp = null;
Path libRFileProp = null;
Logger logger = Logger.getLogger(getClass().getName());

public void execute() throws BuildException {
    // Check for package name conflicts
    if (mApplicationPackage.equals(mLibraryPackage)) {
        logger.log(Level.SEVERE, "Error: The application and library package names must not be the same.");
        throw new BuildException("Package name conflict detected.");
    }

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
        for (String packageName : mLibraryPackages) {
            File libRFile = new File(rFiles[i]);
            if (libRFile.isFile()) {
                SymbolLoader symbols = new SymbolLoader(libRFile);
                symbols.load();

                SymbolWriter writer = new SymbolWriter(mRFolder, packageName, symbols, symbolValues);
                writer.write();
            } else {
                logger.log(Level.WARNING, "Warning: Library R file not found: " + libRFile.getPath());
            }
        }
    } catch (IOException e) {
        logger.log(Level.SEVERE, "R.java generation failed due to an IOException.", e);
        throw new BuildException(e);
    }
}

//<End of snippet n. 0>