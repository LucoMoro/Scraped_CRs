/*Fix SdkManager.hasChanged

Also add a simple unit test.

Change-Id:I8ed3dfbea07578528036f1dabd75c18f3161819c*/
//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 10d7576..96cb003 100644

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.SdkConstants;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -26,6 +27,8 @@
import com.android.utils.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
//Synthetic comment -- @@ -161,6 +164,50 @@
getLog().toString());
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
                        break;
}
visited.add(platform);
DirInfo dirInfo = mTargetDirs.get(platform);
//Synthetic comment -- @@ -195,7 +195,6 @@
System.out.println("SDK changed due to " +              //$NON-NLS-1$
(dirInfo != null ? dirInfo.toString() : platform.getPath()));
}
                        break;
}
}
}
//Synthetic comment -- @@ -208,7 +207,7 @@
if (addons != null) {
for (File addon : addons) {
if (!addon.isDirectory()) {
                        break;
}
visited.add(addon);
DirInfo dirInfo = mTargetDirs.get(addon);
//Synthetic comment -- @@ -223,7 +222,6 @@
System.out.println("SDK changed due to " +              //$NON-NLS-1$
(dirInfo != null ? dirInfo.toString() : addon.getPath()));
}
                        break;
}
}
}
//Synthetic comment -- @@ -467,7 +465,7 @@
}
// Remember we visited this file/directory,
// even if we failed to load anything from it.
                    dirInfos.put(platform, new DirInfo(platform, target));
} else {
log.warning("Ignoring platform '%1$s', not a folder.", platform.getName());
}
//Synthetic comment -- @@ -811,7 +809,7 @@
}
// Remember we visited this file/directory,
// even if we failed to load anything from it.
                        dirInfos.put(addon, new DirInfo(addon, target));
}
}
}
//Synthetic comment -- @@ -1262,24 +1260,20 @@
/**
* Creates a new immutable {@link DirInfo}.
*
         * @param dir The platform/addon directory of the target. It may not be a directory.
         * @param target The target associated with the directory or null if we failed to load it.
*/
        public DirInfo(@NonNull File dir, @Nullable IAndroidTarget target) {
mDir = dir;
mDirModifiedTS = dir.lastModified();

            // If we have a target for it, we expect the directory to have
            // a source props. If we have a target but no source props file,
            // mPropsModifedTS will be zero.
long propsChecksum = 0;
long propsModifedTS = 0;
            if (target != null) {
                File props = new File(dir, SdkConstants.FN_SOURCE_PROP);
                if (props.isFile()) {
                    propsModifedTS = props.lastModified();
                    propsChecksum = getFileChecksum(props);
                }
}
mPropsModifedTS = propsModifedTS;
mPropsChecksum = propsChecksum;







