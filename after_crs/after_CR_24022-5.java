/*Add support to Ant for dependency checking

This change adds dependency checking to the Ant generation of
R.java. If no resources have been changed or added since the
last generation of R.java then that call to aapt will be skipped.

Change-Id:I262b94111316496fac54da8030a6d474dc30b584*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index 725f6b6..ed0277a 100644

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
//Synthetic comment -- @@ -259,8 +258,10 @@
// more R classes need to be created for libraries.
if (mRFolder != null && new File(mRFolder).isDirectory()) {
libPkgProp = taskProject.getProperty(AntConstants.PROP_PROJECT_LIBS_PKG);
            if (libPkgProp != null) {
                // Replace ";" with ":" since that's what aapt expects
                libPkgProp = libPkgProp.replace(';', ':');
            }
}
// Call aapt. If there are libraries, we'll pass a non-null string of libs.
callAapt(libPkgProp);
//Synthetic comment -- @@ -278,7 +279,31 @@

final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();

        // Get whether we have libraries
        Object libResRef = taskProject.getReference(AntConstants.PROP_PROJECT_LIBS_RES_REF);

if (generateRClass) {
            // If the only reason we're here is to generate R.java and that doesn't need updating
            // we can skip what comes next. First we grab the dependency file.
            // Then query to see if an update is needed.
            ArrayList<File> watchPaths = new ArrayList<File>();
            // We need to watch for changes in the main project res folder
            for (Path pathList : mResources) {
                for (String path : pathList.list()) {
                    watchPaths.add(new File(path));
                }
            }
            // and if libraries exist, in their res folders
            if (libResRef instanceof Path) {
                for (String path : ((Path)libResRef).list()) {
                    watchPaths.add(new File(path));
                }
            }
            if (initDependencies(mRFolder + File.separator + "R.d", watchPaths)
                              && dependenciesHaveChanged() == false) {
                System.out.println("No changed resources. R.java and Manifest.java untouched.");
                return;
            }
} else if (mResourceFilter == null) {
System.out.println("Creating full resource package...");
} else {
//Synthetic comment -- @@ -347,7 +372,6 @@
}

// if the project contains libraries, force auto-add-overlay
if (libResRef != null) {
task.createArg().setValue("--auto-add-overlay");
}
//Synthetic comment -- @@ -417,6 +441,8 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
            // Use dependency generation
            task.createArg().setValue("--generate-dependencies");
}

// final setup of the task








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/BaseTask.java b/anttasks/src/com/android/ant/BaseTask.java
new file mode 100644
//Synthetic comment -- index 0000000..2126d3f

//Synthetic comment -- @@ -0,0 +1,70 @@
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
import java.util.ArrayList;

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
     * @param watchPaths a list of folders to watch for new files
     * @return true if the dependency graph was sucessfully initialized
     */
    protected boolean initDependencies(String dependencyFile, ArrayList<File> watchPaths) {
        File depFile = new File(dependencyFile);
        if (depFile.exists()) {
            mDependencies = new DependencyGraph(dependencyFile, watchPaths);
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
//Synthetic comment -- index 0000000..1cb7d17

//Synthetic comment -- @@ -0,0 +1,254 @@
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *  This class takes care of dependency tracking for all targets and prerequisites listed in
 *  a single dependency file. A dependency graph always has a dependency file associated with it
 *  for the duration of its lifetime
 */
public class DependencyGraph {

    // Files that we know about from the dependency file
    private Set<File> mTargets = Collections.emptySet();
    private Set<File> mPrereqs = mTargets;
    private ArrayList<File> mWatchPaths;

    public DependencyGraph(String dependencyFilePath, ArrayList<File> watchPaths) {
        mWatchPaths = watchPaths;
        parseDependencyFile(dependencyFilePath);
    }

    /**
     * Check all the dependencies to see if anything has changed.
     * @return true if new prerequisites have appeared, target files are missing or if
     *         prerequisite files have been modified since the last target generation.
     */
    public boolean dependenciesHaveChanged() {
        boolean noFile = (mTargets.size() == 0);
        boolean missingPrereq = missingPrereqFile();
        boolean newPrereq = newPrereqFile();
        boolean missingTarget = missingTargetFile();
        boolean modPrereq = modifiedPrereq();

        if (noFile) {
            System.out.println("No Dependency File Found");
        }
        if (missingPrereq) {
            System.out.println("Found Deleted Prereq File");
        }
        if (newPrereq) {
            System.out.println("Found New Prereq File");
        }
        if (missingTarget) {
            System.out.println("Found Deleted Target File");
        }
        if (modPrereq) {
            System.out.println("Found //$NON-NLS-1$ Modified Prereq File");
        }
        // If no dependency file has been set up, then we'll just return true
        // if we have a dependency file, we'll check to see what's been changed
        return noFile || missingPrereq || newPrereq || missingTarget || modPrereq;
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
        String[] lines = content.toString().split("\n");
        StringBuilder sb = new StringBuilder(content.length());
        for (String line : lines) {
            line = line.trim();
            if (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1);
            }
            sb.append(line);
        }

        // split the left and right part
        String[] files = sb.toString().split(":");

        // get the target files:
        String[] targets = files[0].trim().split(" ");

        String[] prereqs = {};
        // Check to make sure our dependency file is okay
        if (files.length < 1) {
            System.err.println(
                    "Warning! Dependency file does not list any prerequisites after ':' ");
        } else {
            // and the prerequisite files:
            prereqs = files[1].trim().split(" ");
        }

        mTargets = new HashSet<File>(targets.length);
        for (String path : targets) {
            mTargets.add(new File(path));
        }
        mPrereqs = new HashSet<File>(prereqs.length);
        for (String path : prereqs) {
            mPrereqs.add(new File(path));
        }
    }

    /**
     * Check all the folders we know about to see if there have been new
     * files added to them.
     * @return true if a new file is encountered in the dependency folders
     */
    private boolean newPrereqFile() {
        for (File dir : mWatchPaths) {
            if (newFileInTree(dir)) {
                return true;
            }
        }
        // If we make it all the way through our directories we're good.
        return false;
    }

    /**
     * Check all the files in the tree under root and check to see if the files are
     * listed under the dependencies. Recurses into subdirs.
     * @param root the root of the file tree to search through
     * @return true if a file is encountered in the tree that is not in our list of prereqs
     */
    private boolean newFileInTree(File root) {
        File[] files = root.listFiles();
        if (files == null) {
            System.err.println("ERROR " + root.toString() + " is not a dir or can't be read");
            return false;
        }
        // Loop through files in this folder
        for (File file : files) {
            // If this is a directory, recurse into it
            if (file.isDirectory()) {
                if (newFileInTree(file)) {
                    return true;
                }
            } else if (file.isFile() && mPrereqs.contains(file) == false) {
                return true;
            }
        }
        // If we got to here then we didn't find anything interesting
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







