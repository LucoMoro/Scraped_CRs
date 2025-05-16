```
<Beginning of snippet n. 0>
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

public class RClassGenerator {

    private String libPkgProp = null;
    private Path libRFileProp = null;

    public void executeAaptTask(ExecTask task, String mManifest, String mManifestPackage, Set<String> packages, String[] rFiles, String mRFolder) throws BuildException {
        if (task == null || packages == null || rFiles == null) {
            throw new BuildException("Task, packages, or rFiles cannot be null.");
        }

        task.createArg().setValue("-M");
        task.createArg().setValue(mManifest);
        
        if (mManifestPackage != null) {
            task.createArg().setValue("--rename-manifest-package");
        }
        
        task.execute();

        if (packages.size() != rFiles.length) {
            throw new BuildException(String.format("%1$s and %2$s must contain the same number of items.", "Library Packages", "Library R File"));
        }

        for (int i = 0; i < packages.size(); i++) {
            File libRFile = new File(rFiles[i]);
            if (libRFile.isFile()) {
                SymbolLoader symbols = new SymbolLoader(libRFile);
                symbols.load();
                
                SymbolWriter writer = new SymbolWriter(mRFolder, packages.toArray(new String[0])[i], symbols);
                writer.write();
            } else {
                throw new BuildException("Library R file does not exist: " + libRFile.getAbsolutePath());
            }
        }
    }
}
<End of snippet n. 0>