/*Add seinfo parsing to PackageManagerService.

This patch set allows the PMS to parse the
mac_permissions.xml file which contains the
seinfo values. Each package that is installed
on the device will be assigned an seinfo value
based on policy. This value will help label the
app process and data directory.

You will need to checkout the project:

git.selinuxproject.org/~seandroid/selinux/mac-policy

There is also a dependency on:Ief91d6a717741c91c5ba8745452bb247dc8048ec(includes the mac_permissions.xml with each build)

andI0b4950a4f9e23b2f9f8c848acf0e81e44d580cca(ensures that ApplicationInfo contains seinfo variable)

Change-Id:I61ad1ea12fb6a9a6d0b108ec163bc4bf4c954b58Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 3501e47..747b1a3 100644

//Synthetic comment -- @@ -321,6 +321,15 @@
// Temporary for building the final shared libraries for an .apk.
String[] mTmpSharedLibraries = null;

// These are the features this devices supports that were read from the
// etc/permissions.xml file.
final HashMap<String, FeatureInfo> mAvailableFeatures =
//Synthetic comment -- @@ -1042,6 +1051,13 @@
}
}

// Find base frameworks (resource packages without code).
mFrameworkInstallObserver = new AppDirObserver(
mFrameworkDir.getPath(), OBSERVER_EVENTS, true);
//Synthetic comment -- @@ -1317,6 +1333,128 @@
mSettings.removePackageLPw(ps.name);
}

void readPermissions() {
// Read permissions from .../etc/permission directory.
File libraryDir = new File(Environment.getRootDirectory(), "etc/permissions");
//Synthetic comment -- @@ -3439,6 +3577,31 @@
}
}

private PackageParser.Package scanPackageLI(PackageParser.Package pkg,
int parseFlags, int scanMode, long currentTime) {
File scanFile = new File(pkg.mScanPath);
//Synthetic comment -- @@ -3451,6 +3614,10 @@
}
mScanningPath = scanFile;

if ((parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
pkg.applicationInfo.flags |= ApplicationInfo.FLAG_SYSTEM;
}







