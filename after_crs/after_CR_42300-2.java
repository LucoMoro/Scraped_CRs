/*Fix SdkManager.hasChanged

Also add a simple unit test.

Change-Id:I8ed3dfbea07578528036f1dabd75c18f3161819c*/




//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 10d7576..96cb003 100644

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.SdkConstants;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -26,6 +27,8 @@
import com.android.utils.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
//Synthetic comment -- @@ -161,6 +164,50 @@
getLog().toString());
}

    public void testSdkManagerHasChanged() throws IOException {
        Main main = new Main();
        main.setLogger(getLog());
        SdkManager sdkman = getSdkManager();
        main.setSdkManager(sdkman);
        getLog().clear();

        assertFalse(sdkman.hasChanged());

        File addonsDir = new File(sdkman.getLocation(), SdkConstants.FD_ADDONS);
        assertTrue(addonsDir.isDirectory());

        FileWriter readme = new FileWriter(new File(addonsDir, "android.txt"));
        readme.write("test\n");
        readme.close();

        // Adding a file doesn't alter sdk.hasChanged
        assertFalse(sdkman.hasChanged());
        sdkman.reloadSdk(getLog());
        assertFalse(sdkman.hasChanged());

        File fakeAddon = new File(addonsDir, "google-addon");
        fakeAddon.mkdirs();
        File sourceProps = new File(fakeAddon, SdkConstants.FN_SOURCE_PROP);
        FileWriter propsWriter = new FileWriter(sourceProps);
        propsWriter.write("revision=7\n");
        propsWriter.close();

        // Adding a directory does alter sdk.hasChanged even if not a real add-on
        assertTrue(sdkman.hasChanged());
        // Once reloaded, sdk.hasChanged will be reset
        sdkman.reloadSdk(getLog());
        assertFalse(sdkman.hasChanged());

        // Changing the source.properties file alters sdk.hasChanged
        propsWriter = new FileWriter(sourceProps);
        propsWriter.write("revision=8\n");
        propsWriter.close();
        assertTrue(sdkman.hasChanged());
        // Once reloaded, sdk.hasChanged will be reset
        sdkman.reloadSdk(getLog());
        assertFalse(sdkman.hasChanged());
    }

public void testCheckFilterValues() {
// These are the values we expect checkFilterValues() to match.
String[] expectedValues = {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index e1397ba..5986387 100644

//Synthetic comment -- @@ -180,7 +180,7 @@
if (platforms != null) {
for (File platform : platforms) {
if (!platform.isDirectory()) {
                        continue;
}
visited.add(platform);
DirInfo dirInfo = mTargetDirs.get(platform);
//Synthetic comment -- @@ -195,7 +195,6 @@
System.out.println("SDK changed due to " +              //$NON-NLS-1$
(dirInfo != null ? dirInfo.toString() : platform.getPath()));
}
}
}
}
//Synthetic comment -- @@ -208,7 +207,7 @@
if (addons != null) {
for (File addon : addons) {
if (!addon.isDirectory()) {
                        continue;
}
visited.add(addon);
DirInfo dirInfo = mTargetDirs.get(addon);
//Synthetic comment -- @@ -223,7 +222,6 @@
System.out.println("SDK changed due to " +              //$NON-NLS-1$
(dirInfo != null ? dirInfo.toString() : addon.getPath()));
}
}
}
}
//Synthetic comment -- @@ -467,7 +465,7 @@
}
// Remember we visited this file/directory,
// even if we failed to load anything from it.
                    dirInfos.put(platform, new DirInfo(platform));
} else {
log.warning("Ignoring platform '%1$s', not a folder.", platform.getName());
}
//Synthetic comment -- @@ -811,7 +809,7 @@
}
// Remember we visited this file/directory,
// even if we failed to load anything from it.
                        dirInfos.put(addon, new DirInfo(addon));
}
}
}
//Synthetic comment -- @@ -1262,24 +1260,20 @@
/**
* Creates a new immutable {@link DirInfo}.
*
         * @param dir The platform/addon directory of the target. It should be a directory.
*/
        public DirInfo(@NonNull File dir) {
mDir = dir;
mDirModifiedTS = dir.lastModified();

            // Capture some info about the source.properties file if it exists.
            // We use propsModifedTS == 0 to mean there is no props file.
long propsChecksum = 0;
long propsModifedTS = 0;
            File props = new File(dir, SdkConstants.FN_SOURCE_PROP);
            if (props.isFile()) {
                propsModifedTS = props.lastModified();
                propsChecksum = getFileChecksum(props);
}
mPropsModifedTS = propsModifedTS;
mPropsChecksum = propsChecksum;







