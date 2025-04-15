/*Use the ResourceMerger to handle overlay.

This creates a merged res folder that is then fed to aapt.

Right now the merged folder does not have its image
crunched, nor is it incremental.

This means the build will be slower (previously crunching was
incremental). Next step is to change aapt to be able to crunch
single images and then make the merging itself incremental.

Change-Id:Ide96f733be835d94518b831baee7ee77b9a61dec*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index 1ceccdc..f184d17 100644

//Synthetic comment -- @@ -35,6 +35,9 @@
import com.android.builder.packaging.DuplicateFileException;
import com.android.builder.packaging.PackagerException;
import com.android.builder.packaging.SealedPackageException;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -44,6 +47,7 @@
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -210,38 +214,30 @@
return;
}

        // launch aapt: create the command line
        ArrayList<String> command = Lists.newArrayList();

        @SuppressWarnings("deprecation")
        String aaptPath = mTarget.getPath(IAndroidTarget.AAPT);

        command.add(aaptPath);
        command.add("crunch");

        if (mVerboseExec) {
            command.add("-v");
        }

boolean runCommand = false;
        for (File input : inputs) {
            if (input.isDirectory()) {
                command.add("-S");
                command.add(input.getAbsolutePath());
                runCommand = true;
}
}

        if (!runCommand) {
            return;
}

        command.add("-C");
        command.add(resOutputDir);

        mLogger.info("processImages command: %s", command.toString());

        mCmdLineRunner.runCmdLine(command);
}

/**
//Synthetic comment -- @@ -551,7 +547,9 @@
}

command.add("-f");
        command.add("--no-crunch");

// inputs
command.add("-I");







