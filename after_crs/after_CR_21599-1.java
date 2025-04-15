/*fix failure due to too many pre-installed apps

PackageManager.getInstalledPackages(..) returns zero size when there
are large number of pre-installed applications, because of binder
failure.
binder : 1000: binder_alloc_buf size 1172160 failed, no address space
binder : 120:120 transaction failed 29101, size 1172160-0
binder : send failed reply for transaction 32541 to 1000:1007
getValidPackageName() is intended to just return a valuid package
naem for testing purposes, home is always expected to be present
on all devices and hence a good candidate.

Change-Id:I8133268f3b9c9f6ad0ef7af51f83114540426888*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 6ad14e0..605ab39 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
//Synthetic comment -- @@ -557,10 +558,11 @@
* Helper method to retrieve a valid application package name to use for tests.
*/
private String getValidPackageName() {
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> packages = mContextWrapper.getPackageManager().queryIntentActivities(intent, 0);
	assertTrue(packages.size() >= 1);
	return packages.get(0).activityInfo.packageName;
}

@TestTargetNew(







