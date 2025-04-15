/*Improved logging when there's an error creating the directories in getFilesDir()

Improved logging when there's an error creating the
directories in getFilesDir();

Due to mkdirs() only returns false on errors,
it's better to output the directory path so the dev
knows the path that can not be created
See also Issue 8886http://code.google.com/p/android/issues/detail?id=8886Change-Id:I44d6adc8508ef9ca57f000b5d7bceeb0cfa3ed13*/




//Synthetic comment -- diff --git a/core/java/android/app/ContextImpl.java b/core/java/android/app/ContextImpl.java
//Synthetic comment -- index f471f57..54e3919 100644

//Synthetic comment -- @@ -53,7 +53,6 @@
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
//Synthetic comment -- @@ -85,11 +84,9 @@
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Vibrator;
import android.os.FileUtils.FileStatus;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.AndroidRuntimeException;
//Synthetic comment -- @@ -208,7 +205,7 @@
private File mCacheDir;
private File mExternalFilesDir;
private File mExternalCacheDir;

private static long sInstanceCount = 0;

private static final String[] EMPTY_FILE_LIST = {};
//Synthetic comment -- @@ -260,18 +257,18 @@
public Looper getMainLooper() {
return mMainThread.getLooper();
}

@Override
public Context getApplicationContext() {
return (mPackageInfo != null) ?
mPackageInfo.getApplication() : mMainThread.getApplication();
}

@Override
public void setTheme(int resid) {
mThemeResource = resid;
}

@Override
public Resources.Theme getTheme() {
if (mTheme == null) {
//Synthetic comment -- @@ -321,7 +318,7 @@
}
throw new RuntimeException("Not supported in system context");
}

private static File makeBackupFile(File prefsFile) {
return new File(prefsFile.getPath() + ".bak");
}
//Synthetic comment -- @@ -341,7 +338,7 @@
return sp;
}
}

FileInputStream str = null;
File backup = makeBackupFile(f);
if (backup.exists()) {
//Synthetic comment -- @@ -353,7 +350,7 @@
if (f.exists() && !f.canRead()) {
Log.w(TAG, "Attempt to read preferences file " + f + " without permission");
}

Map map = null;
if (f.exists() && f.canRead()) {
try {
//Synthetic comment -- @@ -437,7 +434,7 @@
}
if (!mFilesDir.exists()) {
if(!mFilesDir.mkdirs()) {
                    Log.w(TAG, "Unable to create files directory " + mFilesDir.getPath());
return null;
}
FileUtils.setPermissions(
//Synthetic comment -- @@ -448,7 +445,7 @@
return mFilesDir;
}
}

@Override
public File getExternalFilesDir(String type) {
synchronized (mSync) {
//Synthetic comment -- @@ -480,7 +477,7 @@
return dir;
}
}

@Override
public File getCacheDir() {
synchronized (mSync) {
//Synthetic comment -- @@ -500,7 +497,7 @@
}
return mCacheDir;
}

@Override
public File getExternalCacheDir() {
synchronized (mSync) {
//Synthetic comment -- @@ -522,7 +519,7 @@
return mExternalCacheDir;
}
}

@Override
public File getFileStreamPath(String name) {
return makeFilename(getFilesDir(), name);
//Synthetic comment -- @@ -563,7 +560,7 @@
return (list != null) ? list : EMPTY_FILE_LIST;
}


private File getDatabasesDir() {
synchronized (mSync) {
if (mDatabasesDir == null) {
//Synthetic comment -- @@ -575,7 +572,7 @@
return mDatabasesDir;
}
}

@Override
public Drawable getWallpaper() {
return getWallpaperManager().getDrawable();
//Synthetic comment -- @@ -643,7 +640,7 @@
} catch (RemoteException e) {
}
}

@Override
public void sendBroadcast(Intent intent) {
String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
//Synthetic comment -- @@ -1541,15 +1538,15 @@
final void setActivityToken(IBinder token) {
mActivityToken = token;
}

final void setOuterContext(Context context) {
mOuterContext = context;
}

final Context getOuterContext() {
return mOuterContext;
}

final IBinder getActivityToken() {
return mActivityToken;
}
//Synthetic comment -- @@ -1626,7 +1623,7 @@
{
return mMainThread.releaseProvider(provider);
}

private final ActivityThread mMainThread;
}

//Synthetic comment -- @@ -1659,7 +1656,7 @@
throw new RuntimeException("Package manager has died", e);
}
}

@Override
public String[] canonicalToCurrentPackageNames(String[] names) {
try {
//Synthetic comment -- @@ -1668,7 +1665,7 @@
throw new RuntimeException("Package manager has died", e);
}
}

@Override
public Intent getLaunchIntentForPackage(String packageName) {
// First see if the package has an INFO activity; the existence of
//Synthetic comment -- @@ -1842,7 +1839,7 @@
throw new RuntimeException("Package manager has died", e);
}
}

@Override
public boolean hasSystemFeature(String name) {
try {
//Synthetic comment -- @@ -1851,7 +1848,7 @@
throw new RuntimeException("Package manager has died", e);
}
}

@Override
public int checkPermission(String permName, String pkgName) {
try {
//Synthetic comment -- @@ -1923,9 +1920,9 @@
throw new RuntimeException("Package manager has died", e);
}
}

@Override
        public int getUidForSharedUser(String sharedUserName)
throws NameNotFoundException {
try {
int uid = mPM.getUidForSharedUser(sharedUserName);
//Synthetic comment -- @@ -2296,7 +2293,7 @@
}
}
}

private static final class ResourceName {
final String packageName;
final int iconId;
//Synthetic comment -- @@ -2468,7 +2465,7 @@
}
}
@Override
        public void clearApplicationUserData(String packageName,
IPackageDataObserver observer) {
try {
mPM.clearApplicationUserData(packageName, observer);
//Synthetic comment -- @@ -2477,7 +2474,7 @@
}
}
@Override
        public void deleteApplicationCacheFiles(String packageName,
IPackageDataObserver observer) {
try {
mPM.deleteApplicationCacheFiles(packageName, observer);
//Synthetic comment -- @@ -2502,9 +2499,9 @@
// Should never happen!
}
}

@Override
        public void getPackageSizeInfo(String packageName,
IPackageStatsObserver observer) {
try {
mPM.getPackageSizeInfo(packageName, observer);
//Synthetic comment -- @@ -2549,7 +2546,7 @@
// Should never happen!
}
}

@Override
public void replacePreferredActivity(IntentFilter filter,
int match, ComponentName[] set, ComponentName activity) {
//Synthetic comment -- @@ -2568,7 +2565,7 @@
// Should never happen!
}
}

@Override
public int getPreferredActivities(List<IntentFilter> outFilters,
List<ComponentName> outActivities, String packageName) {
//Synthetic comment -- @@ -2579,7 +2576,7 @@
}
return 0;
}

@Override
public void setComponentEnabledSetting(ComponentName componentName,
int newState, int flags) {
//Synthetic comment -- @@ -2609,7 +2606,7 @@
// Should never happen!
}
}

@Override
public int getApplicationEnabledSetting(String packageName) {
try {
//Synthetic comment -- @@ -2666,7 +2663,7 @@
return mTimestamp != mFileStatus.mtime;
}
}

public void replace(Map newContents) {
if (newContents != null) {
synchronized (this) {
//Synthetic comment -- @@ -2674,7 +2671,7 @@
}
}
}

public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
synchronized(this) {
mListeners.put(listener, mContent);
//Synthetic comment -- @@ -2840,7 +2837,7 @@
public Editor edit() {
return new EditorImpl();
}

private FileOutputStream createFileOutputStream(File file) {
FileOutputStream str = null;
try {
//Synthetic comment -- @@ -2877,7 +2874,7 @@
mFile.delete();
}
}

// Attempt to write the file, delete the backup and return true as atomically as
// possible.  If any exception occurs, delete the new file; next time we will restore
// from the backup.
//Synthetic comment -- @@ -2892,7 +2889,7 @@
if (FileUtils.getFileStatus(mFile.getPath(), mFileStatus)) {
mTimestamp = mFileStatus.mtime;
}

// Writing was successful, delete the backup file if there is one.
mBackupFile.delete();
return true;







