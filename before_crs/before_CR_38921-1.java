/*Add support for MMAC install checks.

MMAC install check mechanism applies an install-time check
of app permissions against a MAC policy configuration. The
policy configuration can be found at

git.selinuxproject.org/~seandroid/selinux/mac-policy

The install checks run by default in permissive mode (allow
all apps to install with denials going to logcat), and can be
set to enforcing by setting 'persist.mac_enforcing_mode' to 1.
In this case, be sure to grab an updated sepolicy from

git://git.selinuxproject.org/~seandroid/selinux/sepolicy

-
Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>

Change-Id:I61d34a9fd6975f23023f70f205a510e3357d843c*/
//Synthetic comment -- diff --git a/core/java/android/content/pm/ApplicationInfo.java b/core/java/android/content/pm/ApplicationInfo.java
//Synthetic comment -- index e1434b3..a67c236 100644

//Synthetic comment -- @@ -385,6 +385,15 @@
public String[] resourceDirs;

/**
* Paths to all shared libraries this application is linked against.  This
* field is only set if the {@link PackageManager#GET_SHARED_LIBRARY_FILES
* PackageManager.GET_SHARED_LIBRARY_FILES} flag was used when retrieving
//Synthetic comment -- @@ -464,6 +473,9 @@
if (resourceDirs != null) {
pw.println(prefix + "resourceDirs=" + resourceDirs);
}
pw.println(prefix + "dataDir=" + dataDir);
if (sharedLibraryFiles != null) {
pw.println(prefix + "sharedLibraryFiles=" + sharedLibraryFiles);
//Synthetic comment -- @@ -531,6 +543,7 @@
publicSourceDir = orig.publicSourceDir;
nativeLibraryDir = orig.nativeLibraryDir;
resourceDirs = orig.resourceDirs;
sharedLibraryFiles = orig.sharedLibraryFiles;
dataDir = orig.dataDir;
uid = orig.uid;
//Synthetic comment -- @@ -569,6 +582,7 @@
dest.writeString(publicSourceDir);
dest.writeString(nativeLibraryDir);
dest.writeStringArray(resourceDirs);
dest.writeStringArray(sharedLibraryFiles);
dest.writeString(dataDir);
dest.writeInt(uid);
//Synthetic comment -- @@ -607,6 +621,7 @@
publicSourceDir = source.readString();
nativeLibraryDir = source.readString();
resourceDirs = source.readStringArray();
sharedLibraryFiles = source.readStringArray();
dataDir = source.readString();
uid = source.readInt();








//Synthetic comment -- diff --git a/core/java/android/content/pm/PackageManager.java b/core/java/android/content/pm/PackageManager.java
//Synthetic comment -- index 6de69b0..82a3cc8 100644

//Synthetic comment -- @@ -619,6 +619,14 @@
public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;

/**
* Flag parameter for {@link #deletePackage} to indicate that you don't want to delete the
* package's data directory.
*








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..51c39c6 100644

//Synthetic comment -- @@ -2053,7 +2053,7 @@
// the PID of the new process, or else throw a RuntimeException.
Process.ProcessStartResult startResult = Process.start("android.app.ActivityThread",
app.processName, uid, uid, gids, debugFlags,
                    app.info.targetSdkVersion, null, null);

BatteryStatsImpl bs = app.batteryStats.getBatteryStats();
synchronized (bs) {








//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 3501e47..6f494ef 100644

//Synthetic comment -- @@ -102,6 +102,7 @@
import android.os.UserId;
import android.provider.Settings.Secure;
import android.security.SystemKeyStore;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
//Synthetic comment -- @@ -139,6 +140,7 @@
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import libcore.io.ErrnoException;
import libcore.io.IoUtils;
//Synthetic comment -- @@ -162,6 +164,8 @@
private static final boolean DEBUG_PREFERRED = false;
static final boolean DEBUG_UPGRADE = false;
private static final boolean DEBUG_INSTALL = false;
private static final boolean DEBUG_REMOVE = false;
private static final boolean DEBUG_SHOW_INFO = false;
private static final boolean DEBUG_PACKAGE_INFO = false;
//Synthetic comment -- @@ -186,6 +190,8 @@
// package apks to install directory.
private static final String INSTALL_PACKAGE_SUFFIX = "-";

static final int SCAN_MONITOR = 1<<0;
static final int SCAN_NO_DEX = 1<<1;
static final int SCAN_FORCE_DEX = 1<<2;
//Synthetic comment -- @@ -321,6 +327,19 @@
// Temporary for building the final shared libraries for an .apk.
String[] mTmpSharedLibraries = null;

// These are the features this devices supports that were read from the
// etc/permissions.xml file.
final HashMap<String, FeatureInfo> mAvailableFeatures =
//Synthetic comment -- @@ -1042,6 +1061,13 @@
}
}

// Find base frameworks (resource packages without code).
mFrameworkInstallObserver = new AppDirObserver(
mFrameworkDir.getPath(), OBSERVER_EVENTS, true);
//Synthetic comment -- @@ -1317,6 +1343,368 @@
mSettings.removePackageLPw(ps.name);
}

void readPermissions() {
// Read permissions from .../etc/permission directory.
File libraryDir = new File(Environment.getRootDirectory(), "etc/permissions");
//Synthetic comment -- @@ -3439,6 +3827,64 @@
}
}

private PackageParser.Package scanPackageLI(PackageParser.Package pkg,
int parseFlags, int scanMode, long currentTime) {
File scanFile = new File(pkg.mScanPath);
//Synthetic comment -- @@ -3451,6 +3897,15 @@
}
mScanningPath = scanFile;

if ((parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
pkg.applicationInfo.flags |= ApplicationInfo.FLAG_SYSTEM;
}







