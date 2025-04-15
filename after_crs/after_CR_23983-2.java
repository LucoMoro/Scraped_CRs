/*Add Dependency/Multi R.java generation to Ant/ADT

This change changes ant and adt's calls to aapt to take advantage
of its ability to create multiple R.java files in one pass, thus
saving time when compiling projects with libraries. It also introduces
dependency handling for these R.java files speeding up builds when
no resources have been modified.

Change-Id:Ib0ef9ad0d600c5569db8714931a59c0e545614e3*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..bf85301 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Path;

//Synthetic comment -- @@ -51,7 +50,7 @@
* <tr><td></td><td></td><td></td></tr>
* </table>
*/
public final class AaptExecLoopTask extends BaseTask {

/**
* Class representing a &lt;nocompress&gt; node in the main task XML.
//Synthetic comment -- @@ -252,43 +251,39 @@
@Override
public void execute() throws BuildException {
Project taskProject = getProject();
        String libPkgProp = null;

// if the parameters indicate generation of the R class, check if
// more R classes need to be created for libraries.
if (mRFolder != null && new File(mRFolder).isDirectory()) {
            libPkgProp = taskProject.getProperty(AntConstants.PROP_PROJECT_LIBS_PKG);
            // Replace ";" with ":" since that's what aapt expects
            libPkgProp = libPkgProp.replace(';', ':');
}
        // Call aapt. If there are libraries, we'll pass a non-null string of libs.
        callAapt(libPkgProp);
}

/**
* Calls aapt with the given parameters.
* @param resourceFilter the resource configuration filter to pass to aapt (if configName is
* non null)
     * @param extraPackages an optional custom package.
*/
    private void callAapt(String extraPackages) {
Project taskProject = getProject();

final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();

if (generateRClass) {
            // If the only reason we're here is to generate R.java and that doesn't need updating
            // we can skip what comes next. First we grab the dependency file.
            // Then query to see if an update is needed.
            if (initDependencies(mRFolder + File.separator + "R.d")
                              && dependenciesHaveChanged() == false) {
                System.out.println("No changed resources. R.java and Manifest.java untouched.");
                return;
            }
} else if (mResourceFilter == null) {
System.out.println("Creating full resource package...");
} else {
//Synthetic comment -- @@ -351,9 +346,9 @@
}
}

        if (extraPackages != null) {
            task.createArg().setValue("--extra-packages");
            task.createArg().setValue(extraPackages);
}

// if the project contains libraries, force auto-add-overlay
//Synthetic comment -- @@ -427,6 +422,9 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);

            // Use dependency generation
            task.createArg().setValue("--generate-dependencies");
}

// final setup of the task








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/BaseTask.java b/anttasks/src/com/android/ant/BaseTask.java
new file mode 100644
//Synthetic comment -- index 0000000..ce9a8f2

//Synthetic comment -- @@ -0,0 +1,68 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * A base class for the ant task that contains logic for handling dependency files
 */
public class BaseTask extends Task {

    private DependencyGraph mDependencies;

    /*
     * (non-Javadoc)
     *
     * Executes the loop. Based on the values inside default.properties, this will
     * create alternate temporary ap_ files.
     *
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {

    }

    /**
     * Set up the dependency graph by passing it the location of the ".d" file
     * @param dependencyFile path to the dependency file to use
     * @return true if the dependency graph was sucessfully initialized
     */
    protected boolean initDependencies(String dependencyFile) {
        File depFile = new File(dependencyFile);
        if (depFile.exists()) {
            mDependencies = new DependencyGraph(dependencyFile);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Wrapper check to see if we need to execute this task at all
     * @return true if the DependencyGraph reports that our prereqs or targets
     *         have changed since the last run
     */
    protected boolean dependenciesHaveChanged() {
        assert mDependencies != null : "Dependencies have not been initialized";
        return mDependencies.dependenciesHaveChanged();
    }
}








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
new file mode 100644
//Synthetic comment -- index 0000000..1deb9da

//Synthetic comment -- @@ -0,0 +1,224 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *  This class takes care of dependency tracking for all targets and prerequisites listed in
 *  a single dependency file. A dependency graph always has a dependency file associated with it
 *  for the duration of its lifetime
 */
public class DependencyGraph {

    // Files that we know about from the dependency file
    private Set<File> mTargets = Collections.emptySet();
    private Set<File> mPrereqs = mTargets;

    // Directories that we need to watch (parent dirs from the dependency file)
    private Set<File> mDirectories;

    public DependencyGraph(String dependencyFilePath) {
        mDirectories = new TreeSet<File>();
        parseDependencyFile(dependencyFilePath);
    }

    /**
     * Check all the dependencies to see if anything has changed.
     * @return true if new prerequisites have appeared, target files are missing or if
     *         prerequisite files have been modified since the last target generation.
     */
    public boolean dependenciesHaveChanged() {
        // If no dependency file has been set up, then we'll just return true
        return mDirectories.size() == 0 || missingPrereqFile() ||
                        newPrereqFile() || missingTargetFile() || modifiedPrereq();
    }

    /**
     * Parses the given dependency file and stores the file paths
     *
     * @param dependencyFilePath the dependency file
     */
    private void parseDependencyFile(String dependencyFilePath) {
        // Read in our dependency file
        String content = readFile(dependencyFilePath);
        if (content == null) {
            System.err.println("ERROR: Couldn't read " + dependencyFilePath);
            return;
        }

        // The format is something like:
        // output1 output2 [...]: dep1 dep2 [...]
        // expect it's likely split on several lines. So let's move it back on a single line
        // first
        String[] lines = content.toString().split("\n"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder(content.length());
        for (String line : lines) {
            line = line.trim();
            if (line.endsWith("\\")) { //$NON-NLS-1$
                line = line.substring(0, line.length() - 1);
            }
            sb.append(line);
        }

        // split the left and right part
        String[] files = sb.toString().split(":"); //$NON-NLS-1$

        // get the target files:
        String[] targets = files[0].trim().split(" "); //$NON-NLS-1$

        String[] prereqs = {};
        // Check to make sure our dependency file is okay
        if (files.length < 1) {
            System.err.println("Warning! Dependency file does not list any prerequisites after ':' ");
        } else {
            // and the prerequisite files:
            prereqs = files[1].trim().split(" "); //$NON-NLS-1$
        }

        mTargets = new HashSet<File>(targets.length);
        for (String path : targets) {
            mTargets.add(new File(path));
        }
        mPrereqs = new HashSet<File>(prereqs.length);
        for (String path : prereqs) {
            File preFile = new File(path);
            mPrereqs.add(preFile);
            // Add the parent directory to be watched if it's a resource
            // directory. The set will remove duplicate entries.
            // Make sure we ignore the root directory of the project
            if (preFile.getName().equals("AndroidManifest.xml") == false) {
                mDirectories.add(preFile.getParentFile());
            }
        }
    }

    /**
     * Check all the folders we know about to see if there have been new
     * files added to them.
     * @return true if a new file is encountered in the dependency folders
     */
    private boolean newPrereqFile() {
        // Loop over our directories to watch
        for (File directory : mDirectories) {
            // Loop over all files in that directory
            File[] files = directory.listFiles();
            if (files == null) {
                System.err.println("ERROR " + directory + " is not a dir or can't be read");
                continue;
            }
            for (File file : files) {
                // If we find a file we don't know about, it's obviously new.
                if (file.isFile() && mPrereqs.contains(file) == false) {
                    return true;
                }
            }
        }
        // If we got to here, then we didn't find anything interesting.
        return false;
    }

    /**
     * Check all the prereq files we know about to make sure they're still there
     * @return true if any of the prereq files are missing.
     */
    private boolean missingPrereqFile() {
        // Loop through our prereq files and make sure they still exist
        for (File prereq : mPrereqs) {
            if (prereq.exists() == false) {
                return true;
            }
        }
        // If we get this far, then all our targets are okay
        return false;
    }

    /**
     * Check all the target files we know about to make sure they're still there
     * @return true if any of the target files are missing.
     */
    private boolean missingTargetFile() {
        // Loop through our target files and make sure they still exist
        for (File target : mTargets) {
            if (target.exists() == false) {
                return true;
            }
        }
        // If we get this far, then all our targets are okay
        return false;
    }

    /**
     * Check to see if any of the prerequisite files have been modified since
     * the targets were last updated.
     * @return true if the latest prerequisite modification is after the oldest
     *         target modification.
     */
    private boolean modifiedPrereq() {
        // Find the oldest target
        long oldestTarget = Long.MAX_VALUE;
        for (File target : mTargets) {
            if (target.lastModified() < oldestTarget) {
                oldestTarget = target.lastModified();
            }
        }

        // Find the newest prerequisite
        long newestPrereq = 0;
        for (File prereq : mPrereqs) {
            if (prereq.lastModified() > newestPrereq) {
                newestPrereq = prereq.lastModified();
            }
        }

        // And return the comparison
        return newestPrereq > oldestTarget;
    }

    /**
     * Reads and returns the content of a text file.
     * @param filepath the file path to the text file
     * @return null if the file could not be read
     */
    private static String readFile(String filepath) {
        try {
            FileInputStream fStream = new FileInputStream(filepath);
            if (fStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fStream));

                String line;
                StringBuilder total = new StringBuilder(reader.readLine());
                while ((line = reader.readLine()) != null) {
                    total.append('\n');
                    total.append(line);
                }
                return total.toString();
            }
        } catch (IOException e) {
            // we'll just return null
        }
        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..5a7f0f6 100644

//Synthetic comment -- @@ -612,7 +612,7 @@
// handle libraries
ArrayList<IFolder> libResFolders = new ArrayList<IFolder>();
ArrayList<IFolder> libOutputFolders = new ArrayList<IFolder>();
            StringBuilder libJavaPackages = null;
if (libProjects != null) {
for (IProject lib : libProjects) {
IFolder libResFolder = lib.getFolder(SdkConstants.FD_RES);
//Synthetic comment -- @@ -623,26 +623,19 @@
try {
String libJavaPackage = AndroidManifest.getPackage(new IFolderWrapper(lib));
if (libJavaPackage.equals(javaPackage) == false) {
                            if (libJavaPackages == null) {
                                libJavaPackages = new StringBuilder(libJavaPackage);
                            } else {
                                libJavaPackages.append(":");
                                libJavaPackages.append(libJavaPackage);
                            }
}
} catch (Exception e) {
}
}
}
execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, libJavaPackages.toString());
}
}

//Synthetic comment -- @@ -686,7 +679,7 @@
}

if (customJavaPackage != null) {
            array.add("--extra-packages"); //$NON-NLS-1$
array.add(customJavaPackage);
}








