/*Replaced deprecated Config.LOGD with Config.DEBUG, so theres only DEBUG output on a Debug Build

Change-Id:I51bfad536030c21f136b484e048d1421cf1db99a*/




//Synthetic comment -- diff --git a/services/java/com/android/server/DeviceStorageMonitorService.java b/services/java/com/android/server/DeviceStorageMonitorService.java
//Synthetic comment -- index 57af029..4f98183 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
class DeviceStorageMonitorService extends Binder {
private static final String TAG = "DeviceStorageMonitorService";
private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.DEBUG : Config.LOGV;
private static final int DEVICE_MEMORY_WHAT = 1;
private static final int MONITOR_INTERVAL = 1; //in minutes
private static final int LOW_MEMORY_NOTIFICATION_ID = 1;








//Synthetic comment -- diff --git a/services/java/com/android/server/MountListener.java b/services/java/com/android/server/MountListener.java
//Synthetic comment -- index 3e53585..fb86d0b 100644

//Synthetic comment -- @@ -104,7 +104,7 @@
* @param event  An event received from the vol service daemon
*/
private void handleEvent(String event) {
        if (Config.DEBUG) Log.d(TAG, "handleEvent " + event);

int colonIndex = event.indexOf(':');
String path = (colonIndex > 0 ? event.substring(colonIndex + 1) : null);








//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index cc78300..e54041f 100644

//Synthetic comment -- @@ -2216,7 +2216,7 @@
}
}

        if ((parseFlags&PackageParser.PARSE_CHATTY) != 0 && Config.DEBUG) Log.d(
TAG, "Scanning package " + pkgName);
if (mPackages.containsKey(pkgName) || mSharedLibraries.containsKey(pkgName)) {
Log.w(TAG, "*************************************************");
//Synthetic comment -- @@ -2302,7 +2302,7 @@
mLastScanError = PackageManager.INSTALL_FAILED_INSUFFICIENT_STORAGE;
return null;
}
                if ((parseFlags&PackageParser.PARSE_CHATTY) != 0 && Config.DEBUG) {
Log.d(TAG, "Shared UserID " + pkg.mSharedUserId + " (uid="
+ suid.userId + "): packages=" + suid.packages);
}
//Synthetic comment -- @@ -2577,7 +2577,7 @@
} else {
p.info.authority = p.info.authority + ";" + names[j];
}
                        if ((parseFlags&PackageParser.PARSE_CHATTY) != 0 && Config.DEBUG)
Log.d(TAG, "Registered content provider: " + names[j] +
", className = " + p.info.name +
", isSyncable = " + p.info.isSyncable);
//Synthetic comment -- @@ -2600,7 +2600,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Providers: " + r);
}

N = pkg.services.size();
//Synthetic comment -- @@ -2620,7 +2620,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Services: " + r);
}

N = pkg.receivers.size();
//Synthetic comment -- @@ -2640,7 +2640,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Receivers: " + r);
}

N = pkg.activities.size();
//Synthetic comment -- @@ -2660,7 +2660,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Activities: " + r);
}

N = pkg.permissionGroups.size();
//Synthetic comment -- @@ -2694,7 +2694,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Permission Groups: " + r);
}

N = pkg.permissions.size();
//Synthetic comment -- @@ -2755,7 +2755,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Permissions: " + r);
}

N = pkg.instrumentation.size();
//Synthetic comment -- @@ -2777,7 +2777,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Instrumentation: " + r);
}

if (pkg.protectedBroadcasts != null) {
//Synthetic comment -- @@ -2887,7 +2887,7 @@
if (! sharedLibraryFile.exists() ||
sharedLibraryFile.length() != entry.getSize() ||
sharedLibraryFile.lastModified() != entry.getTime()) {
                if (Config.DEBUG) {
Log.d(TAG, "Caching shared lib " + entry.getName());
}
if (mInstaller == null) {
//Synthetic comment -- @@ -2973,7 +2973,7 @@
}

void removePackageLI(PackageParser.Package pkg, boolean chatty) {
        if (chatty && Config.DEBUG) Log.d(
TAG, "Removing package " + pkg.applicationInfo.packageName );

synchronized (mPackages) {
//Synthetic comment -- @@ -3014,7 +3014,7 @@
for (int j = 0; j < names.length; j++) {
if (mProviders.get(names[j]) == p) {
mProviders.remove(names[j]);
                        if (chatty && Config.DEBUG) Log.d(
TAG, "Unregistered content provider: " + names[j] +
", className = " + p.info.name +
", isSyncable = " + p.info.isSyncable);
//Synthetic comment -- @@ -3030,7 +3030,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Providers: " + r);
}

N = pkg.services.size();
//Synthetic comment -- @@ -3048,7 +3048,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Services: " + r);
}

N = pkg.receivers.size();
//Synthetic comment -- @@ -3066,7 +3066,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Receivers: " + r);
}

N = pkg.activities.size();
//Synthetic comment -- @@ -3084,7 +3084,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Activities: " + r);
}

N = pkg.permissions.size();
//Synthetic comment -- @@ -3118,7 +3118,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Permissions: " + r);
}

N = pkg.instrumentation.size();
//Synthetic comment -- @@ -3136,7 +3136,7 @@
}
}
if (r != null) {
                if (Config.DEBUG) Log.d(TAG, "  Instrumentation: " + r);
}
}
}








//Synthetic comment -- diff --git a/services/java/com/android/server/SensorService.java b/services/java/com/android/server/SensorService.java
//Synthetic comment -- index 4dfeb9d..22db8dd 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
class SensorService extends ISensorService.Stub {
static final String TAG = SensorService.class.getSimpleName();
private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.DEBUG : Config.LOGV;
private static final int SENSOR_DISABLE = -1;

/**








//Synthetic comment -- diff --git a/services/java/com/android/server/WifiWatchdogService.java b/services/java/com/android/server/WifiWatchdogService.java
//Synthetic comment -- index 9443a95..865b0c8 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
public class WifiWatchdogService {
private static final String TAG = "WifiWatchdogService";
private static final boolean V = false || Config.LOGV;
    private static final boolean D = Config.DEBUG;

private Context mContext;
private ContentResolver mContentResolver;
//Synthetic comment -- @@ -1263,7 +1263,7 @@
return false;

} catch (Exception e) {
                if (V || Config.DEBUG) {
Log.d(TAG, "DnsPinger.isReachable got an unknown exception", e);
}
return false;







