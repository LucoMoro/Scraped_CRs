/*SDK Manager: fix sdk-has-changed.

The SdkManager.hasChanged method was supposed to
only look at direct folders in platforms and
add-ons, not regular files.

Change-Id:I87c3d51bfd7bd2578285f957a9838fadec703401*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 0651f5a..e1397ba 100644

//Synthetic comment -- @@ -63,6 +63,8 @@
*/
public class SdkManager {

public final static String PROP_VERSION_SDK = "ro.build.version.sdk";              //$NON-NLS-1$
public final static String PROP_VERSION_CODENAME = "ro.build.version.codename";    //$NON-NLS-1$
public final static String PROP_VERSION_RELEASE = "ro.build.version.release";      //$NON-NLS-1$
//Synthetic comment -- @@ -177,6 +179,9 @@
File[] platforms  = platformFolder.listFiles();
if (platforms != null) {
for (File platform : platforms) {
visited.add(platform);
DirInfo dirInfo = mTargetDirs.get(platform);
if (dirInfo == null) {
//Synthetic comment -- @@ -186,6 +191,10 @@
changed = dirInfo.hasChanged();
}
if (changed) {
break;
}
}
//Synthetic comment -- @@ -198,6 +207,9 @@
File[] addons  = addonFolder.listFiles();
if (addons != null) {
for (File addon : addons) {
visited.add(addon);
DirInfo dirInfo = mTargetDirs.get(addon);
if (dirInfo == null) {
//Synthetic comment -- @@ -207,6 +219,10 @@
changed = dirInfo.hasChanged();
}
if (changed) {
break;
}
}
//Synthetic comment -- @@ -219,6 +235,10 @@
if (!visited.contains(previousDir)) {
// This directory is no longer present.
changed = true;
break;
}
}
//Synthetic comment -- @@ -445,12 +465,12 @@
if (target != null) {
targets.add(target);
}
} else {
log.warning("Ignoring platform '%1$s', not a folder.", platform.getName());
}
                // Remember we visited this file/directory, even if we failed to load anything
                // from it.
                dirInfos.put(platform, new DirInfo(platform, target));
}

return;
//Synthetic comment -- @@ -789,10 +809,10 @@
if (target != null) {
targets.add(target);
}
}
                    // Remember we visited this file/directory, even if we failed to load anything
                    // from it.
                    dirInfos.put(addon, new DirInfo(addon, target));
}
}

//Synthetic comment -- @@ -1332,5 +1352,15 @@
return 0;
}

}
}







