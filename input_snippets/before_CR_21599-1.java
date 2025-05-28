
//<Beginning of snippet n. 0>


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
* Helper method to retrieve a valid application package name to use for tests.
*/
private String getValidPackageName() {
        List<PackageInfo> packages = mContextWrapper.getPackageManager().getInstalledPackages(
                PackageManager.GET_ACTIVITIES);
        assertTrue(packages.size() >= 1);
        return packages.get(0).packageName;
}

@TestTargetNew(

//<End of snippet n. 0>








