/*Fix deletion of least-recently-used external databases.

The present external database LRU garbage collector mistakenly includes
SQLite temporary files, including those associated with the internal
database, as distinct external databases.  This leads to a number of
problems, including partial deletion of databases and/or their temporary
files and early deletion of databases before the MAX_EXTERNAL_DATABASES
limit is reached.  In short, the present code works only, and
coincidentally, in a single circumstance: when there is an internal
database and _exactly one_ external database.

This fixes the above problem by removing SQLite temporary files from the
list of candidate databases, and later ensures that all files associated
with a deleted database are deleted as well.

Note, it's quite possible that the actual bug here is in the
android.app.ContextImpl::databaseList and ContextImpl::deleteDatabase
methods, which return SQLite temporary files as part of a context's
"database list" instead of hiding them and handling their deletion
internally.  However, since these methods are part of the Android public
API and used by third-party apps, the present fix is implemented in
MediaProvider only.  Should the Context database API be altered, this fix
will be made redundant but remain unharmful.*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c3114d3..40a4846 100644

//Synthetic comment -- @@ -85,6 +85,7 @@
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.Stack;

/**
//Synthetic comment -- @@ -275,6 +276,9 @@
* on demand, create and upgrade the schema, etc.
*/
static final class DatabaseHelper extends SQLiteOpenHelper {
        // Matches SQLite database temporary files.
        private static final Pattern DB_TMPFILE_PAT = Pattern.compile("\\.db-\\w+\\z");

final Context mContext;
final String mName;
final boolean mInternal;  // True if this is the internal database
//Synthetic comment -- @@ -369,12 +373,20 @@

// delete least recently used databases if we are over the limit
String[] databases = mContext.databaseList();
            String[] dbFiles   = databases.clone();
int count = databases.length;
int limit = MAX_EXTERNAL_DATABASES;

// delete external databases that have not been used in the past two months
long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
for (int i = 0; i < databases.length; i++) {
                // Remove SQLite temporary files as they don't count as distinct databases.
                if (DB_TMPFILE_PAT.matcher(databases[i]).find()) {
                    databases[i] = null;
                    count--;
                    continue;
                }

File other = mContext.getDatabasePath(databases[i]);
if (INTERNAL_DATABASE_NAME.equals(databases[i]) || file.equals(other)) {
databases[i] = null;
//Synthetic comment -- @@ -388,7 +400,15 @@
long time = other.lastModified();
if (time < twoMonthsAgo) {
if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[i]);

                        // Delete database and all temporary files associated with it.
                        String name = databases[i];
                        for (String f : dbFiles) {
                            if (f.startsWith(name)) {
                                mContext.deleteDatabase(f);
                            }
                        }

databases[i] = null;
count--;
}
//Synthetic comment -- @@ -414,7 +434,15 @@
// delete least recently used database
if (lruIndex != -1) {
if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[lruIndex]);

                    // Delete database and all temporary files associated with it.
                    String name = databases[lruIndex];
                    for (String f : dbFiles) {
                        if (f.startsWith(name)) {
                            mContext.deleteDatabase(f);
                        }
                    }

databases[lruIndex] = null;
count--;
}







