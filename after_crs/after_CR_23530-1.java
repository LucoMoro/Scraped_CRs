/*Extended AaptExecLoopTask to support custom arguments

Developers now can specify arguments that are not natively
supported by AaptExecLoopTask but are supported by aapt tool.

 Here is an example:

      <aapt executable="${aapt}"
               command="package"
               versioncode="${version.code}"
               ...>
               <res path="${resource.absolute.dir}" />
               <arg value="--version-name" />
               <arg value="${version.name}" />
       </aapt>

Where "--version-name" is used to set version name of .apk
without modifying AndroidManifest.xml file.

Change-Id:Ie0acff971ada155bd046fedb466ae9a7e0eeeb21Signed-off-by: Ievgenii Nazaruk <ievgenii.nazaruk@gmail.com>*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..11aaf48 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.util.ArrayList;
//Synthetic comment -- @@ -87,6 +88,7 @@
private String mResourceFilter;
private String mRFolder;
private final ArrayList<NoCompress> mNoCompressList = new ArrayList<NoCompress>();
    private Commandline mCommandline = new Commandline();

/**
* Sets the value of the "executable" attribute.
//Synthetic comment -- @@ -241,6 +243,13 @@
return path;
}

    /**
     * Returns an object representing a nested <var>arg</var> element.
     */
    public Commandline.Argument createArg() {
        return mCommandline.createArgument();
    }

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -429,6 +438,10 @@
task.createArg().setValue(mRFolder);
}

        for(String arg : mCommandline.getArguments()){
             task.createArg().setValue(arg);
        }

// final setup of the task
task.setProject(taskProject);
task.setOwningTarget(getOwningTarget());







