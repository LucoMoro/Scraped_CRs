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
import com.android.builder.resources.DuplicateResourceException;
import com.android.builder.resources.ResourceMerger;
import com.android.builder.resources.ResourceSet;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -44,6 +47,7 @@
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.sun.xml.internal.rngom.ast.builder.BuildException;

import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -210,38 +214,30 @@
return;
}

        ResourceMerger merger = new ResourceMerger();

boolean runCommand = false;
        try {
            // The list of inputs is ordered as the most important first, but the merger starts
            // from the lowest important (and merges new one on top), so we loop in reverse.
            for (int n = inputs.size() - 1; n >= 0; n--) {
                File input = inputs.get(n);
                if (input.isDirectory()) {
                    ResourceSet set = new ResourceSet();
                    set.addSource(input);

                    merger.addResourceSet(set);
                    runCommand = true;
                }
}
        } catch (DuplicateResourceException e) {
            throw new BuildException(e);
}

        if (runCommand) {
            ResourceSet mergedSet = merger.getMergedSet();
            mergedSet.writeTo(new File(resOutputDir));
}
}

/**
//Synthetic comment -- @@ -551,7 +547,9 @@
}

command.add("-f");

        //TODO: reenable when we can crunch per-file in the ResourceMerger
        // command.add("--no-crunch");

// inputs
command.add("-I");







