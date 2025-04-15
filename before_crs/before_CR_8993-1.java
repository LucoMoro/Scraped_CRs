/*Change scope on SettingsProvider.mDatabaseHelper and DatabaseHelper
This change will allow the DatabaseHelper to be inheritted and extended
without the need to make futher changes to the existing implementation.*/
//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index e2ea85b..2182271 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
* Database helper class for {@link SettingsProvider}.
* Mostly just has a bit {@link #onCreate} to initialize the database.
*/
class DatabaseHelper extends SQLiteOpenHelper {
/**
* Path to file containing default bookmarks, relative to ANDROID_ROOT.
*/








//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java b/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
//Synthetic comment -- index 35bf6b0..0101ece 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
private static final String TAG = "SettingsProvider";
private static final boolean LOCAL_LOGV = false;

    private DatabaseHelper mOpenHelper;

/**
* Decode a content URL into the table, projection, and arguments







