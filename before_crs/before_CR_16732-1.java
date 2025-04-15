/*Fix ActivityThread will return old source path after apk updated.

If the apk is pre-installed in /system/app, after updated the apk
from Market that installed in /data/app, ActivityThread will still
return the apk in /system/app instead of /data/app.
It will cause "Problem loading widget" issue because the new resources
are not existing in old apk.

Change-Id:Idf9db35b7f2b6411c4a8b54e775dd1dbe1c6b088*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 773c344..a528079 100644

//Synthetic comment -- @@ -2272,6 +2272,13 @@
boolean mGcIdlerScheduled = false;

public final PackageInfo getPackageInfo(String packageName, int flags) {
synchronized (mPackages) {
WeakReference<PackageInfo> ref;
if ((flags&Context.CONTEXT_INCLUDE_CODE) != 0) {
//Synthetic comment -- @@ -2283,6 +2290,14 @@
//Slog.i(TAG, "getPackageInfo " + packageName + ": " + packageInfo);
//if (packageInfo != null) Slog.i(TAG, "isUptoDate " + packageInfo.mResDir
//        + ": " + packageInfo.mResources.getAssets().isUpToDate());
if (packageInfo != null && (packageInfo.mResources == null
|| packageInfo.mResources.getAssets().isUpToDate())) {
if (packageInfo.isSecurityViolation()
//Synthetic comment -- @@ -2297,13 +2312,6 @@
}
}

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_SHARED_LIBRARY_FILES);
        } catch (RemoteException e) {
        }

if (ai != null) {
return getPackageInfo(ai, flags);
}







