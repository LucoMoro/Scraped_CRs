
//<Beginning of snippet n. 0>


import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.Stack;

/**
* on demand, create and upgrade the schema, etc.
*/
static final class DatabaseHelper extends SQLiteOpenHelper {
        // Matches SQLite database temporary files.
        private static final Pattern DB_TMPFILE_PAT = Pattern.compile("\\.db-\\w+\\z");

final Context mContext;
final String mName;
final boolean mInternal;  // True if this is the internal database

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

//<End of snippet n. 0>








