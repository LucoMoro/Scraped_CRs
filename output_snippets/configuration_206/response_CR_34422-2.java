//<Beginning of snippet n. 0>


import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
* on demand, create and upgrade the schema, etc.
*/
static final class DatabaseHelper extends SQLiteOpenHelper {
    final Context mContext;
    final String mName;
    final boolean mInternal;  // True if this is the internal database

    // delete least recently used databases if we are over the limit
    String[] databases = mContext.databaseList();
    int count = 0;
    int limit = MAX_EXTERNAL_DATABASES;

    // Exclude temporary files and count valid databases
    for (String db : databases) {
        if (!db.endsWith("-journal") && !db.endsWith("-shm") && !db.endsWith("-wal")) {
            File other = mContext.getDatabasePath(db);
            if (!INTERNAL_DATABASE_NAME.equals(db) && !file.equals(other)) {
                long time = other.lastModified();
                if (time >= twoMonthsAgo) {
                    count++;
                }
            }
        }
    }

    // delete external databases that have not been used in the past two months
    long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
    for (String db : databases) {
        if (!db.endsWith("-journal") && !db.endsWith("-shm") && !db.endsWith("-wal")) {
            File other = mContext.getDatabasePath(db);
            if (INTERNAL_DATABASE_NAME.equals(db) || file.equals(other)) {
                databases[i] = null;
                continue;
            }
            long time = other.lastModified();
            if (time < twoMonthsAgo) {
                if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + db);
                mContext.deleteDatabase(db);
                count--;
            }
        }
    }

    // delete least recently used database if over limit
    if (count > limit) {
        // logic to determine lruIndex
        if (lruIndex != -1) {
            if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[lruIndex]);
            mContext.deleteDatabase(databases[lruIndex]);
            databases[lruIndex] = null;
            count--;
        }
    }

//<End of snippet n. 0>