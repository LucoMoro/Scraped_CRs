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

    private static final boolean DEBUG = System.getenv("SDKMAN_DEBUG") != null;        //$NON-NLS-1$

public final static String PROP_VERSION_SDK = "ro.build.version.sdk";              //$NON-NLS-1$
public final static String PROP_VERSION_CODENAME = "ro.build.version.codename";    //$NON-NLS-1$
public final static String PROP_VERSION_RELEASE = "ro.build.version.release";      //$NON-NLS-1$
//Synthetic comment -- @@ -177,6 +179,9 @@
File[] platforms  = platformFolder.listFiles();
if (platforms != null) {
for (File platform : platforms) {
                    if (!platform.isDirectory()) {
                        break;
                    }
visited.add(platform);
DirInfo dirInfo = mTargetDirs.get(platform);
if (dirInfo == null) {
//Synthetic comment -- @@ -186,6 +191,10 @@
changed = dirInfo.hasChanged();
}
if (changed) {
                        if (DEBUG) {
                            System.out.println("SDK changed due to " +              //$NON-NLS-1$
                                (dirInfo != null ? dirInfo.toString() : platform.getPath()));
                        }
break;
}
}
//Synthetic comment -- @@ -198,6 +207,9 @@
File[] addons  = addonFolder.listFiles();
if (addons != null) {
for (File addon : addons) {
                    if (!addon.isDirectory()) {
                        break;
                    }
visited.add(addon);
DirInfo dirInfo = mTargetDirs.get(addon);
if (dirInfo == null) {
//Synthetic comment -- @@ -207,6 +219,10 @@
changed = dirInfo.hasChanged();
}
if (changed) {
                        if (DEBUG) {
                            System.out.println("SDK changed due to " +              //$NON-NLS-1$
                                (dirInfo != null ? dirInfo.toString() : addon.getPath()));
                        }
break;
}
}
//Synthetic comment -- @@ -219,6 +235,10 @@
if (!visited.contains(previousDir)) {
// This directory is no longer present.
changed = true;
                    if (DEBUG) {
                        System.out.println("SDK changed: " +                        //$NON-NLS-1$
                                previousDir.getPath() + " removed");                //$NON-NLS-1$
                    }
break;
}
}
//Synthetic comment -- @@ -445,12 +465,12 @@
if (target != null) {
targets.add(target);
}
                    // Remember we visited this file/directory,
                    // even if we failed to load anything from it.
                    dirInfos.put(platform, new DirInfo(platform, target));
} else {
log.warning("Ignoring platform '%1$s', not a folder.", platform.getName());
}
}

return;
//Synthetic comment -- @@ -789,10 +809,10 @@
if (target != null) {
targets.add(target);
}
                        // Remember we visited this file/directory,
                        // even if we failed to load anything from it.
                        dirInfos.put(addon, new DirInfo(addon, target));
}
}
}

//Synthetic comment -- @@ -1332,5 +1352,15 @@
return 0;
}

        /** Returns a visual representation of this object for debugging. */
        @Override
        public String toString() {
            String s = String.format("<DirInfo %1$s TS=%2$d", mDir, mDirModifiedTS);  //$NON-NLS-1$
            if (mPropsModifedTS != 0) {
                s += String.format(" | Props TS=%1$d, Chksum=%2$s",                   //$NON-NLS-1$
                        mPropsModifedTS, mPropsChecksum);
            }
            return s + ">";                                                           //$NON-NLS-1$
        }
}
}







