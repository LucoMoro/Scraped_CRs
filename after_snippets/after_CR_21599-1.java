
//<Beginning of snippet n. 0>


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
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

//<End of snippet n. 0>








