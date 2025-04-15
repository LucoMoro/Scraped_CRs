/*ReadWriteLock in PackageManagerService

Replace all synchronization on mPackages
with a ReadWriteLock to allow for
multiple readers at once.

Change-Id:I87e3eb1c5e924c80d5f2a2c9751979365ab9363b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 938d93a..03b30c1 100644

//Synthetic comment -- @@ -130,6 +130,8 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
//Synthetic comment -- @@ -276,6 +278,8 @@
// the prefix "LI".
final Object mInstallLock = new Object();

    private final ReadWriteLock mLock = new ReentrantReadWriteLock();

// These are the directories in the 3rd party applications installed dir
// that we have currently loaded packages from.  Keys are the application's
// installed zip file (absolute codePath), and values are Package.
//Synthetic comment -- @@ -608,7 +612,8 @@
int size = 0;
int uids[];
Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    try {
                        writeLock();
if (mPendingBroadcasts == null) {
return;
}
//Synthetic comment -- @@ -633,6 +638,8 @@
}
size = i;
mPendingBroadcasts.clear();
                    } finally {
                        writeUnlock();
}
// Send broadcasts
for (int i = 0; i < size; i++) {
//Synthetic comment -- @@ -644,10 +651,13 @@
case START_CLEANING_PACKAGE: {
String packageName = (String)msg.obj;
Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    try {
                        writeLock();
if (!mSettings.mPackagesToBeCleaned.contains(packageName)) {
mSettings.mPackagesToBeCleaned.add(packageName);
}
                    } finally {
                        writeUnlock();
}
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
startCleaningPackages();
//Synthetic comment -- @@ -732,18 +742,24 @@
} break;
case WRITE_SETTINGS: {
Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    try {
                        writeLock();
removeMessages(WRITE_SETTINGS);
removeMessages(WRITE_STOPPED_PACKAGES);
mSettings.writeLPr();
                    } finally {
                        writeUnlock();
}
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
} break;
case WRITE_STOPPED_PACKAGES: {
Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    try {
                        writeLock();
removeMessages(WRITE_STOPPED_PACKAGES);
mSettings.writeStoppedLPr();
                    } finally {
                        writeUnlock();
}
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
} break;
//Synthetic comment -- @@ -911,7 +927,8 @@

synchronized (mInstallLock) {
// writer
        try {
            writeLock();
mHandlerThread.start();
mHandler = new PackageHandler(mHandlerThread.getLooper());

//Synthetic comment -- @@ -1153,7 +1170,9 @@
Runtime.getRuntime().gc();

mRequiredVerifierPackage = getRequiredVerifierLPr();
        } finally {
            writeUnlock();
        }
} // synchronized (mInstallLock)
}

//Synthetic comment -- @@ -1498,7 +1517,8 @@

public PackageInfo getPackageInfo(String packageName, int flags) {
// reader
        try {
            readLock();
PackageParser.Package p = mPackages.get(packageName);
if (DEBUG_PACKAGE_INFO)
Log.v(TAG, "getPackageInfo " + packageName + ": " + p);
//Synthetic comment -- @@ -1508,6 +1528,8 @@
if((flags & PackageManager.GET_UNINSTALLED_PACKAGES) != 0) {
return generatePackageInfoFromSettingsLPw(packageName, flags);
}
        } finally {
            readUnlock();
}
return null;
}
//Synthetic comment -- @@ -1515,11 +1537,14 @@
public String[] currentToCanonicalPackageNames(String[] names) {
String[] out = new String[names.length];
// reader
        try {
            readLock();
for (int i=names.length-1; i>=0; i--) {
PackageSetting ps = mSettings.mPackages.get(names[i]);
out[i] = ps != null && ps.realName != null ? ps.realName : names[i];
}
        } finally {
            readUnlock();
}
return out;
}
//Synthetic comment -- @@ -1527,18 +1552,22 @@
public String[] canonicalToCurrentPackageNames(String[] names) {
String[] out = new String[names.length];
// reader
        try {
            readLock();
for (int i=names.length-1; i>=0; i--) {
String cur = mSettings.mRenamedPackages.get(names[i]);
out[i] = cur != null ? cur : names[i];
}
        } finally {
            readUnlock();
}
return out;
}

public int getPackageUid(String packageName) {
// reader
        try {
            readLock();
PackageParser.Package p = mPackages.get(packageName);
if(p != null) {
return p.applicationInfo.uid;
//Synthetic comment -- @@ -1549,12 +1578,15 @@
}
p = ps.pkg;
return p != null ? p.applicationInfo.uid : -1;
        } finally {
            readUnlock();
}
}

public int[] getPackageGids(String packageName) {
// reader
        try {
            readLock();
PackageParser.Package p = mPackages.get(packageName);
if (DEBUG_PACKAGE_INFO)
Log.v(TAG, "getPackageGids" + packageName + ": " + p);
//Synthetic comment -- @@ -1563,6 +1595,8 @@
final SharedUserSetting suid = ps.sharedUser;
return suid != null ? suid.gids : ps.gids;
}
        } finally {
            readUnlock();
}
// stupid thing to indicate an error.
return new int[0];
//Synthetic comment -- @@ -1583,18 +1617,22 @@

public PermissionInfo getPermissionInfo(String name, int flags) {
// reader
        try {
            readLock();
final BasePermission p = mSettings.mPermissions.get(name);
if (p != null) {
return generatePermissionInfo(p, flags);
}
return null;
        } finally {
            readUnlock();
}
}

public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) {
// reader
        try {
            readLock();
ArrayList<PermissionInfo> out = new ArrayList<PermissionInfo>(10);
for (BasePermission p : mSettings.mPermissions.values()) {
if (group == null) {
//Synthetic comment -- @@ -1612,20 +1650,26 @@
return out;
}
return mPermissionGroups.containsKey(group) ? out : null;
        } finally {
            readUnlock();
}
}

public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) {
// reader
        try {
            readLock();
return PackageParser.generatePermissionGroupInfo(
mPermissionGroups.get(name), flags);
        } finally {
            readUnlock();
}
}

public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
// reader
        try {
            readLock();
final int N = mPermissionGroups.size();
ArrayList<PermissionGroupInfo> out
= new ArrayList<PermissionGroupInfo>(N);
//Synthetic comment -- @@ -1633,6 +1677,8 @@
out.add(PackageParser.generatePermissionGroupInfo(pg, flags));
}
return out;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -1673,7 +1719,8 @@

public ApplicationInfo getApplicationInfo(String packageName, int flags) {
// writer
        try {
            writeLock();
PackageParser.Package p = mPackages.get(packageName);
if (DEBUG_PACKAGE_INFO) Log.v(
TAG, "getApplicationInfo " + packageName
//Synthetic comment -- @@ -1688,6 +1735,8 @@
if((flags & PackageManager.GET_UNINSTALLED_PACKAGES) != 0) {
return generateApplicationInfoFromSettingsLPw(packageName, flags);
}
        } finally {
            writeUnlock();
}
return null;
}
//Synthetic comment -- @@ -1743,7 +1792,8 @@
}

public ActivityInfo getActivityInfo(ComponentName component, int flags) {
        try {
            writeLock();
PackageParser.Activity a = mActivities.mActivities.get(component);

if (DEBUG_PACKAGE_INFO) Log.v(TAG, "getActivityInfo " + component + ": " + a);
//Synthetic comment -- @@ -1753,49 +1803,61 @@
if (mResolveComponentName.equals(component)) {
return mResolveActivity;
}
        } finally {
            writeUnlock();
}
return null;
}

public ActivityInfo getReceiverInfo(ComponentName component, int flags) {
        try {
            writeLock();
PackageParser.Activity a = mReceivers.mActivities.get(component);
if (DEBUG_PACKAGE_INFO) Log.v(
TAG, "getReceiverInfo " + component + ": " + a);
if (a != null && mSettings.isEnabledLPr(a.info, flags)) {
return PackageParser.generateActivityInfo(a, flags);
}
        } finally {
            writeUnlock();
}
return null;
}

public ServiceInfo getServiceInfo(ComponentName component, int flags) {
        try {
            writeLock();
PackageParser.Service s = mServices.mServices.get(component);
if (DEBUG_PACKAGE_INFO) Log.v(
TAG, "getServiceInfo " + component + ": " + s);
if (s != null && mSettings.isEnabledLPr(s.info, flags)) {
return PackageParser.generateServiceInfo(s, flags);
}
        } finally {
            writeUnlock();
}
return null;
}

public ProviderInfo getProviderInfo(ComponentName component, int flags) {
        try {
            writeLock();
PackageParser.Provider p = mProvidersByComponent.get(component);
if (DEBUG_PACKAGE_INFO) Log.v(
TAG, "getProviderInfo " + component + ": " + p);
if (p != null && mSettings.isEnabledLPr(p.info, flags)) {
return PackageParser.generateProviderInfo(p, flags);
}
        } finally {
            writeUnlock();
}
return null;
}

public String[] getSystemSharedLibraryNames() {
Set<String> libSet;
        try {
            readLock();
libSet = mSharedLibraries.keySet();
int size = libSet.size();
if (size > 0) {
//Synthetic comment -- @@ -1803,13 +1865,16 @@
libSet.toArray(libs);
return libs;
}
        } finally {
            readUnlock();
}
return null;
}

public FeatureInfo[] getSystemAvailableFeatures() {
Collection<FeatureInfo> featSet;
        try {
            readLock();
featSet = mAvailableFeatures.values();
int size = featSet.size();
if (size > 0) {
//Synthetic comment -- @@ -1821,18 +1886,24 @@
features[size] = fi;
return features;
}
        } finally {
            readUnlock();
}
return null;
}

public boolean hasSystemFeature(String name) {
        try {
            readLock();
return mAvailableFeatures.containsKey(name);
        } finally {
            readUnlock();
}
}

public int checkPermission(String permName, String pkgName) {
        try {
            readLock();
PackageParser.Package p = mPackages.get(pkgName);
if (p != null && p.mExtras != null) {
PackageSetting ps = (PackageSetting)p.mExtras;
//Synthetic comment -- @@ -1844,12 +1915,15 @@
return PackageManager.PERMISSION_GRANTED;
}
}
        } finally {
            readUnlock();
}
return PackageManager.PERMISSION_DENIED;
}

public int checkUidPermission(String permName, int uid) {
        try {
            readLock();
Object obj = mSettings.getUserIdLPr(uid);
if (obj != null) {
GrantedPermissions gp = (GrantedPermissions)obj;
//Synthetic comment -- @@ -1862,6 +1936,8 @@
return PackageManager.PERMISSION_GRANTED;
}
}
        } finally {
            readUnlock();
}
return PackageManager.PERMISSION_DENIED;
}
//Synthetic comment -- @@ -1964,19 +2040,26 @@
}

public boolean addPermission(PermissionInfo info) {
        try {
            writeLock();
return addPermissionLocked(info, false);
        } finally {
            writeUnlock();
}
}

public boolean addPermissionAsync(PermissionInfo info) {
        try {
            writeLock();
return addPermissionLocked(info, true);
        } finally {
            writeUnlock();
}
}

public void removePermission(String name) {
        try {
            writeLock();
checkPermissionTreeLP(name);
BasePermission bp = mSettings.mPermissions.get(name);
if (bp != null) {
//Synthetic comment -- @@ -1988,17 +2071,23 @@
mSettings.mPermissions.remove(name);
mSettings.writeLPr();
}
        } finally {
            writeUnlock();
}
}

public boolean isProtectedBroadcast(String actionName) {
        try {
            readLock();
return mProtectedBroadcasts.contains(actionName);
        } finally {
            readUnlock();
}
}

public int checkSignatures(String pkg1, String pkg2) {
        try {
            readLock();
final PackageParser.Package p1 = mPackages.get(pkg1);
final PackageParser.Package p2 = mPackages.get(pkg2);
if (p1 == null || p1.mExtras == null
//Synthetic comment -- @@ -2006,12 +2095,15 @@
return PackageManager.SIGNATURE_UNKNOWN_PACKAGE;
}
return compareSignatures(p1.mSignatures, p2.mSignatures);
        } finally {
            readUnlock();
}
}

public int checkUidSignatures(int uid1, int uid2) {
// reader
        try {
            readLock();
Signature[] s1;
Signature[] s2;
Object obj = mSettings.getUserIdLPr(uid1);
//Synthetic comment -- @@ -2039,6 +2131,8 @@
return PackageManager.SIGNATURE_UNKNOWN_PACKAGE;
}
return compareSignatures(s1, s2);
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2068,7 +2162,8 @@

public String[] getPackagesForUid(int uid) {
// reader
        try {
            readLock();
Object obj = mSettings.getUserIdLPr(uid);
if (obj instanceof SharedUserSetting) {
final SharedUserSetting sus = (SharedUserSetting) obj;
//Synthetic comment -- @@ -2084,13 +2179,16 @@
final PackageSetting ps = (PackageSetting) obj;
return new String[] { ps.name };
}
        } finally {
            readUnlock();
}
return null;
}

public String getNameForUid(int uid) {
// reader
        try {
            readLock();
Object obj = mSettings.getUserIdLPr(uid);
if (obj instanceof SharedUserSetting) {
final SharedUserSetting sus = (SharedUserSetting) obj;
//Synthetic comment -- @@ -2099,6 +2197,8 @@
final PackageSetting ps = (PackageSetting) obj;
return ps.name;
}
        } finally {
            readUnlock();
}
return null;
}
//Synthetic comment -- @@ -2108,12 +2208,15 @@
return -1;
}
// reader
        try {
            readLock();
final SharedUserSetting suid = mSettings.getSharedUserLPw(sharedUserName, 0, false);
if(suid == null) {
return -1;
}
return suid.userId;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2161,7 +2264,8 @@
ResolveInfo findPreferredActivity(Intent intent, String resolvedType,
int flags, List<ResolveInfo> query, int priority) {
// writer
        try {
            writeLock();
if (intent.getSelector() != null) {
intent = intent.getSelector(); 
}
//Synthetic comment -- @@ -2239,6 +2343,8 @@
}
}
}
        } finally {
            writeUnlock();
}
return null;
}
//Synthetic comment -- @@ -2264,7 +2370,8 @@
}

// reader
        try {
            readLock();
final String pkgName = intent.getPackage();
if (pkgName == null) {
return mActivities.queryIntent(intent, resolvedType, flags);
//Synthetic comment -- @@ -2275,6 +2382,8 @@
pkg.activities);
}
return new ArrayList<ResolveInfo>();
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2467,7 +2576,8 @@
}

// reader
        try {
            readLock();
String pkgName = intent.getPackage();
if (pkgName == null) {
return mReceivers.queryIntent(intent, resolvedType, flags);
//Synthetic comment -- @@ -2477,6 +2587,8 @@
return mReceivers.queryIntentForPackage(intent, resolvedType, flags, pkg.receivers);
}
return null;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2512,7 +2624,8 @@
}

// reader
        try {
            readLock();
String pkgName = intent.getPackage();
if (pkgName == null) {
return mServices.queryIntent(intent, resolvedType, flags);
//Synthetic comment -- @@ -2522,6 +2635,8 @@
return mServices.queryIntentForPackage(intent, resolvedType, flags, pkg.services);
}
return null;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2546,7 +2661,8 @@
final String[] keys;

// writer
        try {
            writeLock();
if (listUninstalled) {
keys = mSettings.mPackages.keySet().toArray(new String[mSettings.mPackages.size()]);
} else {
//Synthetic comment -- @@ -2581,6 +2697,8 @@
if (i == N) {
list.setLastSlice(true);
}
        } finally {
            writeUnlock();
}

return list;
//Synthetic comment -- @@ -2593,7 +2711,8 @@
final String[] keys;

// writer
        try {
            writeLock();
if (listUninstalled) {
keys = mSettings.mPackages.keySet().toArray(new String[mSettings.mPackages.size()]);
} else {
//Synthetic comment -- @@ -2628,6 +2747,8 @@
if (i == N) {
list.setLastSlice(true);
}
        } finally {
            writeUnlock();
}

return list;
//Synthetic comment -- @@ -2637,7 +2758,8 @@
final ArrayList<ApplicationInfo> finalList = new ArrayList<ApplicationInfo>();

// reader
        try {
            readLock();
final Iterator<PackageParser.Package> i = mPackages.values().iterator();
while (i.hasNext()) {
final PackageParser.Package p = i.next();
//Synthetic comment -- @@ -2647,6 +2769,8 @@
finalList.add(PackageParser.generateApplicationInfo(p, flags));
}
}
        } finally {
            readUnlock();
}

return finalList;
//Synthetic comment -- @@ -2654,7 +2778,8 @@

public ProviderInfo resolveContentProvider(String name, int flags) {
// reader
        try {
            readLock();
final PackageParser.Provider provider = mProviders.get(name);
return provider != null
&& mSettings.isEnabledLPr(provider.info, flags)
//Synthetic comment -- @@ -2662,6 +2787,8 @@
&ApplicationInfo.FLAG_SYSTEM) != 0)
? PackageParser.generateProviderInfo(provider, flags)
: null;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2671,7 +2798,8 @@
@Deprecated
public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) {
// reader
        try {
            readLock();
final Iterator<Map.Entry<String, PackageParser.Provider>> i = mProviders.entrySet()
.iterator();

//Synthetic comment -- @@ -2686,6 +2814,8 @@
outInfo.add(PackageParser.generateProviderInfo(p, 0));
}
}
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2694,7 +2824,8 @@
ArrayList<ProviderInfo> finalList = null;

// reader
        try {
            readLock();
final Iterator<PackageParser.Provider> i = mProvidersByComponent.values().iterator();
while (i.hasNext()) {
final PackageParser.Provider p = i.next();
//Synthetic comment -- @@ -2711,6 +2842,8 @@
finalList.add(PackageParser.generateProviderInfo(p, flags));
}
}
        } finally {
            readUnlock();
}

if (finalList != null) {
//Synthetic comment -- @@ -2723,9 +2856,12 @@
public InstrumentationInfo getInstrumentationInfo(ComponentName name,
int flags) {
// reader
        try {
            readLock();
final PackageParser.Instrumentation i = mInstrumentation.get(name);
return PackageParser.generateInstrumentationInfo(i, flags);
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -2735,7 +2871,8 @@
new ArrayList<InstrumentationInfo>();

// reader
        try {
            readLock();
final Iterator<PackageParser.Instrumentation> i = mInstrumentation.values().iterator();
while (i.hasNext()) {
final PackageParser.Instrumentation p = i.next();
//Synthetic comment -- @@ -2745,6 +2882,8 @@
flags));
}
}
        } finally {
            readUnlock();
}

return finalList;
//Synthetic comment -- @@ -2853,7 +2992,8 @@
PackageSetting ps = null;
PackageSetting updatedPkg;
// reader
        try {
            readLock();
// Look to see if we already know about this package.
String oldName = mSettings.mRenamedPackages.get(pkg.packageName);
if (pkg.mOriginalPackages != null && pkg.mOriginalPackages.contains(oldName)) {
//Synthetic comment -- @@ -2870,6 +3010,8 @@
// package name depending on our state.
updatedPkg = mSettings.mDisabledSysPackages.get(
ps != null ? ps.name : pkg.packageName);
        } finally {
            readUnlock();
}
// First check if this is a system package that may involve an update
if (updatedPkg != null && (parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
//Synthetic comment -- @@ -2893,9 +3035,12 @@
// apps in system partition will go through. If not there won't be a working
// version of the app
// writer
                    try {
                        writeLock();
// Just remove the loaded entries from package lists.
mPackages.remove(ps.name);
                    } finally {
                        writeUnlock();
}
Slog.w(TAG, "Package " + ps.name + " at " + scanFile
+ "reverting from " + ps.codePathString
//Synthetic comment -- @@ -3000,11 +3145,14 @@

public void performBootDexOpt() {
ArrayList<PackageParser.Package> pkgs = null;
        try {
            writeLock();
if (mDeferredDexOpt.size() > 0) {
pkgs = new ArrayList<PackageParser.Package>(mDeferredDexOpt);
mDeferredDexOpt.clear();
}
        } finally {
            writeUnlock();
}
if (pkgs != null) {
for (int i=0; i<pkgs.size(); i++) {
//Synthetic comment -- @@ -3035,11 +3183,14 @@
}

PackageParser.Package p;
        try {
            readLock();
p = mPackages.get(packageName);
if (p == null || p.mDidDexOpt) {
return false;
}
        } finally {
            readUnlock();
}
synchronized (mInstallLock) {
return performDexOptLI(p, false, false) == DEX_OPT_PERFORMED;
//Synthetic comment -- @@ -3141,7 +3292,8 @@
}

if (pkg.packageName.equals("android")) {
            try {
            writeLock();
if (mAndroidApplication != null) {
Slog.w(TAG, "*************************************************");
Slog.w(TAG, "Core android package being redefined.  Skipping.");
//Synthetic comment -- @@ -3171,6 +3323,8 @@
mResolveInfo.match = 0;
mResolveComponentName = new ComponentName(
mAndroidApplication.packageName, mResolveActivity.name);
            } finally {
                writeUnlock();
}
}

//Synthetic comment -- @@ -3202,7 +3356,8 @@
}

// writer
        try {
            writeLock();
// Check all shared libraries and map to their actual file path.
if (pkg.usesLibraries != null || pkg.usesOptionalLibraries != null) {
if (mTmpSharedLibraries == null ||
//Synthetic comment -- @@ -3423,6 +3578,8 @@
}
}
}
        } finally {
            writeUnlock();
}

final String pkgName = pkg.packageName;
//Synthetic comment -- @@ -3497,13 +3654,16 @@
+ mOutPermissions[1] + " on disk, "
+ pkg.applicationInfo.uid + " in settings";
// writer
                        try {
                            writeLock();
mSettings.mReadMessages.append(msg);
mSettings.mReadMessages.append('\n');
uidError = true;
if (!pkgSetting.uidError) {
reportSettingsProblem(Log.ERROR, msg);
}
                        } finally {
                            writeUnlock();
}
}
}
//Synthetic comment -- @@ -3639,7 +3799,8 @@
}

// writer
        try {
            writeLock();
// We don't expect installation to fail beyond this point,
if ((scanMode&SCAN_MONITOR) != 0) {
mAppDirs.put(pkg.mPath, pkg);
//Synthetic comment -- @@ -3920,6 +4081,8 @@
}

pkgSetting.setTimeStamp(scanFileTime);
        } finally {
            writeUnlock();
}

return pkg;
//Synthetic comment -- @@ -3945,7 +4108,8 @@
}

// writer
        try {
            writeLock();
clearPackagePreferredActivitiesLPw(pkg.packageName);

mPackages.remove(pkg.applicationInfo.packageName);
//Synthetic comment -- @@ -4088,6 +4252,8 @@
if (r != null) {
if (DEBUG_REMOVE) Log.d(TAG, "  Instrumentation: " + r);
}
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -4760,7 +4926,8 @@

public String nextPackageToClean(String lastPackage) {
// writer
        try {
            writeLock();
if (!isExternalMediaAvailable()) {
// If the external storage is no longer mounted at this point,
// the caller may not have been able to delete all of this
//Synthetic comment -- @@ -4772,6 +4939,8 @@
}
return mSettings.mPackagesToBeCleaned.size() > 0
? mSettings.mPackagesToBeCleaned.get(0) : null;
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -4781,13 +4950,16 @@

void startCleaningPackages() {
// reader
        try {
            readLock();
if (!isExternalMediaAvailable()) {
return;
}
if (mSettings.mPackagesToBeCleaned.size() <= 0) {
return;
}
        } finally {
            readUnlock();
}
Intent intent = new Intent(PackageManager.ACTION_CLEAN_EXTERNAL_STORAGE);
intent.setComponent(DEFAULT_CONTAINER_COMPONENT);
//Synthetic comment -- @@ -4838,8 +5010,11 @@
}
PackageParser.Package p = null;
// reader
                try {
                    readLock();
p = mAppDirs.get(fullPathStr);
                } finally {
                    readUnlock();
}
if ((event&REMOVE_EVENTS) != 0) {
if (p != null) {
//Synthetic comment -- @@ -4865,9 +5040,12 @@
* lock.
*/
// writer
                            try {
                                writeLock();
updatePermissionsLPw(p.packageName, p,
p.permissions.size() > 0, false, false);
                            } finally {
                                writeUnlock();
}
addedPackage = p.applicationInfo.packageName;
addedUid = p.applicationInfo.uid;
//Synthetic comment -- @@ -4876,8 +5054,11 @@
}

// reader
                try {
                    writeLock();
mSettings.writeLPr();
                } finally {
                    writeUnlock();
}
}

//Synthetic comment -- @@ -5007,7 +5188,8 @@
}

private int getUidForVerifier(VerifierInfo verifierInfo) {
        try {
            readLock();
final PackageParser.Package pkg = mPackages.get(verifierInfo.packageName);
if (pkg == null) {
return -1;
//Synthetic comment -- @@ -5041,6 +5223,8 @@
}

return pkg.applicationInfo.uid;
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -5080,7 +5264,8 @@
public void setInstallerPackageName(String targetPackage, String installerPackageName) {
final int uid = Binder.getCallingUid();
// writer
        try {
            writeLock();
PackageSetting targetPackageSetting = mSettings.mPackages.get(targetPackage);
if (targetPackageSetting == null) {
throw new IllegalArgumentException("Unknown target package: " + targetPackage);
//Synthetic comment -- @@ -5144,6 +5329,8 @@
// Okay!
targetPackageSetting.installerPackageName = installerPackageName;
scheduleWriteSettingsLocked();
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -5361,7 +5548,8 @@
int installLocation = pkgLite.installLocation;
boolean onSd = (flags & PackageManager.INSTALL_EXTERNAL) != 0;
// reader
            try {
            readLock();
PackageParser.Package pkg = mPackages.get(packageName);
if (pkg != null) {
if ((flags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
//Synthetic comment -- @@ -5396,6 +5584,8 @@
return PackageHelper.RECOMMEND_FAILED_ALREADY_EXISTS;
}
}
            } finally {
                readUnlock();
}
// All the special cases have been taken care of.
// Return result based on recommended install location.
//Synthetic comment -- @@ -6289,7 +6479,8 @@

boolean dataDirExists = getDataPathForPackage(pkg.packageName, 0).exists();
res.name = pkgName;
        try {
            readLock();
if (mSettings.mRenamedPackages.containsKey(pkgName)) {
// A package with the same name is already installed, though
// it has been renamed to an older name.  The package we
//Synthetic comment -- @@ -6308,6 +6499,8 @@
res.returnCode = PackageManager.INSTALL_FAILED_ALREADY_EXISTS;
return;
}
        } finally {
            readUnlock();
}
mLastScanError = PackageManager.INSTALL_SUCCEEDED;
PackageParser.Package newPackage = scanPackageLI(pkg, parseFlags, scanMode,
//Synthetic comment -- @@ -6344,13 +6537,16 @@
PackageParser.Package oldPackage;
String pkgName = pkg.packageName;
// First find the old package info and check signatures
        try {
            readLock();
oldPackage = mPackages.get(pkgName);
if (compareSignatures(oldPackage.mSignatures, pkg.mSignatures)
!= PackageManager.SIGNATURE_MATCH) {
res.returnCode = PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
return;
}
        } finally {
            readUnlock();
}
boolean sysPkg = (isSystemApp(oldPackage));
if (sysPkg) {
//Synthetic comment -- @@ -6433,11 +6629,14 @@
}
// Restore of old package succeeded. Update permissions.
// writer
                try {
                    writeLock();
updatePermissionsLPw(deletedPackage.packageName, deletedPackage,
true, false, false);
// can downgrade to reader
mSettings.writeLPr();
                } finally {
                    writeUnlock();
}
Slog.i(TAG, "Successfully restored package : " + pkgName + " after failed upgrade");
}
//Synthetic comment -- @@ -6461,7 +6660,8 @@
PackageParser.Package oldPkg;
PackageSetting oldPkgSetting;
// reader
        try {
            readLock();
oldPkg = mPackages.get(packageName);
oldPkgSetting = mSettings.mPackages.get(packageName);
if((oldPkg == null) || (oldPkg.applicationInfo == null) ||
//Synthetic comment -- @@ -6469,6 +6669,8 @@
Slog.w(TAG, "Couldn't find package:"+packageName+" information");
return;
}
        } finally {
            readUnlock();
}

killApplication(packageName, oldPkg.applicationInfo.uid);
//Synthetic comment -- @@ -6478,7 +6680,8 @@
// Remove existing system package
removePackageLI(oldPkg, true);
// writer
        try {
            writeLock();
if (!mSettings.disableSystemPackageLPw(packageName) && deletedPackage != null) {
// We didn't need to disable the .apk as a current system package,
// which means we are replacing another update that is already
//Synthetic comment -- @@ -6491,6 +6694,8 @@
} else {
res.removedInfo.args = null;
}
        } finally {
            writeUnlock();
}

// Successfully disabled the old package. Now proceed with re-installation
//Synthetic comment -- @@ -6521,13 +6726,16 @@
// Add back the old system package
scanPackageLI(oldPkg, parseFlags, SCAN_MONITOR | SCAN_UPDATE_SIGNATURE, 0);
// Restore the old system information in Settings
            try {
                writeLock();
if (updatedSettings) {
mSettings.enableSystemPackageLPw(packageName);
mSettings.setInstallerPackageName(packageName,
oldPkgSetting.installerPackageName);
}
mSettings.writeLPr();
            } finally {
                writeUnlock();
}
}
}
//Synthetic comment -- @@ -6557,12 +6765,15 @@
private void updateSettingsLI(PackageParser.Package newPackage,
String installerPackageName, PackageInstalledInfo res) {
String pkgName = newPackage.packageName;
        try {
            writeLock();
//write settings. the installStatus will be incomplete at this stage.
//note that the new package setting would have already been
//added to mPackages. It hasn't been persisted yet.
mSettings.setInstallStatus(pkgName, PackageSettingBase.PKG_INSTALL_INCOMPLETE);
mSettings.writeLPr();
        } finally {
            writeUnlock();
}

if ((res.returnCode = moveDexFilesLI(newPackage))
//Synthetic comment -- @@ -6577,7 +6788,8 @@
} else {
Log.d(TAG, "New package installed in " + newPackage.mPath);
}
        try {
            writeLock();
updatePermissionsLPw(newPackage.packageName, newPackage,
newPackage.permissions.size() > 0, true, false);
res.name = pkgName;
//Synthetic comment -- @@ -6588,6 +6800,8 @@
res.returnCode = PackageManager.INSTALL_SUCCEEDED;
//to update install status
mSettings.writeLPr();
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -6652,7 +6866,8 @@
pp = null;
String oldCodePath = null;
boolean systemApp = false;
        try {
            readLock();
// Check if installing already existing package
if ((pFlags&PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
String oldName = mSettings.mRenamedPackages.get(pkgName);
//Synthetic comment -- @@ -6680,6 +6895,8 @@
ApplicationInfo.FLAG_SYSTEM) != 0;
}
}
        } finally {
            readUnlock();
}

if (systemApp && onSd) {
//Synthetic comment -- @@ -6984,8 +7201,11 @@
// Retrieve object to delete permissions for shared user later on
final PackageSetting deletedPs;
// reader
        try {
            readLock();
deletedPs = mSettings.mPackages.get(packageName);
        } finally {
            readUnlock();
}
if ((flags&PackageManager.DONT_DELETE_DATA) == 0) {
int retCode = mInstaller.remove(packageName, 0);
//Synthetic comment -- @@ -7000,7 +7220,8 @@
schedulePackageCleaning(packageName);
}
// writer
        try {
            writeLock();
if (deletedPs != null) {
if ((flags&PackageManager.DONT_DELETE_DATA) == 0) {
if (outInfo != null) {
//Synthetic comment -- @@ -7030,6 +7251,8 @@
// Save settings now
mSettings.writeLPr();
}
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -7049,8 +7272,11 @@
// An updated system app can be deleted. This will also have to restore
// the system pkg from system partition
// reader
        try {
            readLock();
ps = mSettings.getDisabledSystemPkgLPr(p.packageName);
        } finally {
            readUnlock();
}
if (ps == null) {
Slog.w(TAG, "Attempt to delete unknown system package "+ p.packageName);
//Synthetic comment -- @@ -7073,11 +7299,14 @@
return false;
}
// writer
        try {
            writeLock();
// Reinstate the old system package
mSettings.enableSystemPackageLPw(p.packageName);
// Remove any native libraries from the upgraded package.
NativeLibraryHelper.removeNativeBinariesLI(p.applicationInfo.nativeLibraryDir);
        } finally {
            writeUnlock();
}
// Install the system package
PackageParser.Package newPkg = scanPackageLI(ps.codePath,
//Synthetic comment -- @@ -7089,12 +7318,15 @@
return false;
}
// writer
        try {
            writeLock();
updatePermissionsLPw(newPkg.packageName, newPkg, true, true, false);
// can downgrade to reader here
if (writeSettings) {
mSettings.writeLPr();
}
        } finally {
            writeUnlock();
}
return true;
}
//Synthetic comment -- @@ -7137,7 +7369,8 @@
}
PackageParser.Package p;
boolean dataOnly = false;
        try {
            readLock();
p = mPackages.get(packageName);
if (p == null) {
//this retrieves partially installed apps
//Synthetic comment -- @@ -7149,6 +7382,8 @@
}
p = ps.pkg;
}
        } finally {
            readUnlock();
}
if (p == null) {
Slog.w(TAG, "Package named '" + packageName +"' doesn't exist.");
//Synthetic comment -- @@ -7219,7 +7454,8 @@
}
PackageParser.Package p;
boolean dataOnly = false;
        try {
            writeLock();
p = mPackages.get(packageName);
if(p == null) {
dataOnly = true;
//Synthetic comment -- @@ -7230,6 +7466,8 @@
}
p = ps.pkg;
}
        } finally {
            writeUnlock();
}

if (!dataOnly) {
//Synthetic comment -- @@ -7282,8 +7520,11 @@
return false;
}
PackageParser.Package p;
        try {
            readLock();
p = mPackages.get(packageName);
        } finally {
            readUnlock();
}
if (p == null) {
Slog.w(TAG, "Package named '" + packageName +"' doesn't exist.");
//Synthetic comment -- @@ -7333,7 +7574,8 @@
PackageParser.Package p;
boolean dataOnly = false;
String asecPath = null;
        try {
            readLock();
p = mPackages.get(packageName);
if(p == null) {
dataOnly = true;
//Synthetic comment -- @@ -7350,6 +7592,8 @@
asecPath = PackageHelper.getSdFilesystem(secureContainerId);
}
}
        } finally {
            readUnlock();
}
String publicSrcDir = null;
if(!dataOnly) {
//Synthetic comment -- @@ -7409,7 +7653,8 @@
public void addPreferredActivity(IntentFilter filter, int match,
ComponentName[] set, ComponentName activity) {
// writer
        try {
            writeLock();
if (mContext.checkCallingOrSelfPermission(
android.Manifest.permission.SET_PREFERRED_APPLICATIONS)
!= PackageManager.PERMISSION_GRANTED) {
//Synthetic comment -- @@ -7428,6 +7673,8 @@
mSettings.mPreferredActivities.addFilter(
new PreferredActivity(filter, match, set, activity));
scheduleWriteSettingsLocked();            
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -7449,7 +7696,8 @@
"replacePreferredActivity expects filter to have no data authorities, " +
"paths, schemes or types.");
}
        try {
            writeLock();
if (mContext.checkCallingOrSelfPermission(
android.Manifest.permission.SET_PREFERRED_APPLICATIONS)
!= PackageManager.PERMISSION_GRANTED) {
//Synthetic comment -- @@ -7475,13 +7723,16 @@
}
}
addPreferredActivity(filter, match, set, activity);
        } finally {
            writeUnlock();
}
}

public void clearPackagePreferredActivities(String packageName) {
final int uid = Binder.getCallingUid();
// writer
        try {
            writeLock();
PackageParser.Package pkg = mPackages.get(packageName);
if (pkg == null || pkg.applicationInfo.uid != uid) {
if (mContext.checkCallingOrSelfPermission(
//Synthetic comment -- @@ -7501,6 +7752,8 @@
if (clearPackagePreferredActivitiesLPw(packageName)) {
scheduleWriteSettingsLocked();            
}
        } finally {
            writeUnlock();
}
}

//Synthetic comment -- @@ -7522,7 +7775,8 @@

int num = 0;
// reader
        try {
            readLock();
final Iterator<PreferredActivity> it = mSettings.mPreferredActivities.filterIterator();
while (it.hasNext()) {
final PreferredActivity pa = it.next();
//Synthetic comment -- @@ -7536,6 +7790,8 @@
}
}
}
        } finally {
            readUnlock();
}

return num;
//Synthetic comment -- @@ -7573,7 +7829,8 @@
ArrayList<String> components;

// writer
        try {
            writeLock();
pkgSetting = mSettings.mPackages.get(packageName);
if (pkgSetting == null) {
if (className == null) {
//Synthetic comment -- @@ -7645,6 +7902,8 @@
mHandler.sendEmptyMessageDelayed(SEND_PENDING_BROADCAST, BROADCAST_DELAY);
}
}
        } finally {
            writeUnlock();
}

long callingId = Binder.clearCallingIdentity();
//Synthetic comment -- @@ -7679,32 +7938,44 @@
android.Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE);
final boolean allowedByPermission = (permission == PackageManager.PERMISSION_GRANTED);
// writer
        try {
            writeLock();
if (mSettings.setPackageStoppedStateLPw(packageName, stopped, allowedByPermission,
uid)) {
scheduleWriteStoppedPackagesLocked();
}
        } finally {
            writeUnlock();
}
}

public String getInstallerPackageName(String packageName) {
// reader
        try {
            readLock();
return mSettings.getInstallerPackageNameLPr(packageName);
        } finally {
            readUnlock();
}
}

public int getApplicationEnabledSetting(String packageName) {
// reader
        try {
            readLock();
return mSettings.getApplicationEnabledSettingLPr(packageName);
        } finally {
            readUnlock();
}
}

public int getComponentEnabledSetting(ComponentName componentName) {
// reader
        try {
            readLock();
return mSettings.getComponentEnabledSettingLPr(componentName);
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -7900,7 +8171,8 @@
}

// reader
        try {
            readLock();
if (dumpState.isDumping(DumpState.DUMP_VERIFIERS) && packageName == null) {
if (dumpState.onTitlePrinted())
pw.println(" ");
//Synthetic comment -- @@ -8032,6 +8304,8 @@
}
}
}
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -8100,7 +8374,8 @@
}
// reader; this apparently protects mMediaMounted, but should probably
// be a different lock in that case.
        try {
            writeLock();
Log.i(TAG, "Updating external media status from "
+ (mMediaMounted ? "mounted" : "unmounted") + " to "
+ (mediaStatus ? "mounted" : "unmounted"));
//Synthetic comment -- @@ -8114,6 +8389,8 @@
return;
}
mMediaMounted = mediaStatus;
        } finally {
            writeUnlock();
}
// Queue up an async operation since the package installation may take a
// little while.
//Synthetic comment -- @@ -8149,7 +8426,8 @@
int uidList[] = new int[list.length];
int num = 0;
// reader
            try {
            readLock();
for (String cid : list) {
SdInstallArgs args = new SdInstallArgs(cid);
if (DEBUG_SD_INSTALL)
//Synthetic comment -- @@ -8184,6 +8462,8 @@
removeCids.add(cid);
}
}
            } finally {
                readUnlock();
}

if (num > 0) {
//Synthetic comment -- @@ -8273,11 +8553,14 @@
* to be straightened out.
*/
// writer
                        try {
                            writeLock();
retCode = PackageManager.INSTALL_SUCCEEDED;
pkgList.add(pkg.packageName);
// Post process args
args.doPostInstall(PackageManager.INSTALL_SUCCEEDED);
                        } finally {
                            writeUnlock();
}
} else {
Slog.i(TAG, "Failed to install pkg from  " + codePath + " from sdcard");
//Synthetic comment -- @@ -8293,7 +8576,8 @@
}
}
// writer
        try {
            writeLock();
// If the platform SDK has changed since the last time we booted,
// we need to re-grant app permission to catch any new ones that
// appear. This is really a hack, and means that apps can in some
//Synthetic comment -- @@ -8312,6 +8596,8 @@
// can downgrade to reader
// Persist settings
mSettings.writeLPr();
        } finally {
            writeUnlock();
}
// Send a broadcast to let everyone know we are done processing
if (pkgList.size() > 0) {
//Synthetic comment -- @@ -8380,11 +8666,13 @@
}
}

        try {
            writeLock();
// We didn't update the settings after removing each package;
// write them now for all packages.
mSettings.writeLPr();
        } finally {
            writeUnlock();
}

// We have to absolutely send UPDATED_MEDIA_STATUS only
//Synthetic comment -- @@ -8414,7 +8702,8 @@
int currFlags = 0;
int newFlags = 0;
// reader
        try {
            readLock();
PackageParser.Package pkg = mPackages.get(packageName);
if (pkg == null) {
returnCode = PackageManager.MOVE_FAILED_DOESNT_EXIST;
//Synthetic comment -- @@ -8467,6 +8756,8 @@
msg.obj = mp;
mHandler.sendMessage(msg);
}
        } finally {
            readUnlock();
}
}

//Synthetic comment -- @@ -8481,7 +8772,8 @@
if (currentStatus == PackageManager.MOVE_SUCCEEDED) {
int uidArr[] = null;
ArrayList<String> pkgList = null;
                    try {
                        readLock();
PackageParser.Package pkg = mPackages.get(mp.packageName);
if (pkg == null) {
Slog.w(TAG, " Package " + mp.packageName
//Synthetic comment -- @@ -8500,13 +8792,16 @@
pkgList = new ArrayList<String>();
pkgList.add(mp.packageName);
}
                    } finally {
                        readUnlock();
}
if (returnCode == PackageManager.MOVE_SUCCEEDED) {
// Send resources unavailable broadcast
sendResourcesChangedBroadcast(false, pkgList, uidArr, null);
// Update package code and resource paths
synchronized (mInstallLock) {
                            try {
                                writeLock();
PackageParser.Package pkg = mPackages.get(mp.packageName);
// Recheck for package again.
if (pkg == null) {
//Synthetic comment -- @@ -8579,6 +8874,8 @@
mSettings.writeLPr();
}
}
                            } finally {
                                writeUnlock();
}
}
// Send resources available broadcast
//Synthetic comment -- @@ -8602,12 +8899,15 @@
// Allow more operations on this file if we didn't fail because
// an operation was already pending for this package.
if (returnCode != PackageManager.MOVE_FAILED_OPERATION_PENDING) {
                    try {
                        writeLock();
PackageParser.Package pkg = mPackages.get(mp.packageName);
if (pkg != null) {
pkg.mOperationPending = false;
}
                    } finally {
                        writeUnlock();
                    }
}

IPackageMoveObserver observer = mp.observer;
//Synthetic comment -- @@ -8669,8 +8969,27 @@
android.Manifest.permission.PACKAGE_VERIFICATION_AGENT,
"Only package verification agents can read the verifier device identity");

        try {
            readLock();
return mSettings.getVerifierDeviceIdentityLPw();
        } finally {
            readUnlock();
}
}

    private void writeUnlock() {
        mLock.writeLock().unlock();
    }

    private void writeLock() {
        mLock.writeLock().lock();
    }

    private void readUnlock() {
        mLock.readLock().unlock();
    }

    private void readLock() {
        mLock.readLock().lock();
    }
}







