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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Task to execute aapt.
*/
public class AaptTask extends ExecTask {
    private String mManifest;
    private String mManifestPackage;
    private Path mLibraryPackagesRefid;
    private Path mLibraryRFileRefid;
    private File mRFolder;

    public void execute() throws BuildException {
        if (mManifestPackage == null) {
            throw new BuildException("Manifest package name must not be null.");
        }

        // Enhanced unique package name check for app and libraries
        Set<String> libraryPackages = new HashSet<>(mLibraryPackagesRefid.getReferences());
        if (libraryPackages.contains(mManifestPackage)) {
            throw new BuildException("Library and app package names must be unique: " + mManifestPackage);
        }

        // if the parameters indicate generation of the R class, check if
        // more R classes need to be created for libraries, only if this project itself is not a library
        createRFile();

        // Rename manifest package
        if (mManifestPackage != null) {
            createArg().setValue("--rename-manifest-package");
            createArg().setValue(mManifestPackage);
            setProject(getProject());
            setOwningTarget(getOwningTarget());
        }

        // execute it.
        super.execute();

        // now if the project has libraries, R needs to be created for each library
        // but only if the project is not a library.
        generateRFilesForLibraries();
    }

    private void createRFile() {
        createArg().setValue("-M");
        createArg().setValue(mManifest);
    }

    private void generateRFilesForLibraries() throws BuildException {
        String[] packages = mLibraryPackagesRefid.getReferences().toArray(new String[0]);
        String[] rFiles = mLibraryRFileRefid.getPaths().toArray(new String[0]);

        if (packages.length != rFiles.length) {
            throw new BuildException(String.format("%1$s and %2$s must contain the same number of items.",
                    mLibraryPackagesRefid, mLibraryRFileRefid));
        }

        for (int i = 0; i < packages.length; i++) {
            File libRFile = new File(rFiles[i]);
            if (libRFile.isFile()) {
                try {
                    SymbolLoader symbols = new SymbolLoader(libRFile);
                    symbols.load();

                    SymbolWriter writer = new SymbolWriter(mRFolder, packages[i], symbols, null);
                    writer.write();
                } catch (IOException e) {
                    throw new BuildException("I/O Error generating R file for library: " + packages[i], e);
                } catch (BuildException e) {
                    throw new BuildException("Build Error generating R file for library: " + packages[i], e);
                }
            }
        }
    }
}

//<End of snippet n. 0>