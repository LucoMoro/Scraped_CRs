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

    // delete external databases that have not been used in the past two months
    long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
    for (String database : databases) {
        File other = mContext.getDatabasePath(database);
        if (!INTERNAL_DATABASE_NAME.equals(database) && other != null && !database.endsWith("-journal") && !database.endsWith("-shm") && !database.endsWith("-wal")) {
            long time = other.lastModified();
            if (time < twoMonthsAgo) {
                if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + database);
                try {
                    mContext.deleteDatabase(database);
                    other.delete(); // Delete associated files
                    count++;
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting database " + database, e);
                }
            }
        }
    }

    // Ensure we handle the LRU logic correctly
    if (count > limit) {
        int lruIndex = getLRUIndex(databases);
        if (lruIndex != -1 && databases[lruIndex] != null) {
            if (LOCAL_LOGV) Log.v(TAG, "Deleting least recently used database " + databases[lruIndex]);
            try {
                mContext.deleteDatabase(databases[lruIndex]);
                File lruFile = mContext.getDatabasePath(databases[lruIndex]);
                if (lruFile.exists()) {
                    lruFile.delete(); // Delete associated files
                }
                databases[lruIndex] = null;
                count--;
            } catch (Exception e) {
                Log.e(TAG, "Error deleting least recently used database " + databases[lruIndex], e);
            }
        }
    }

    private int getLRUIndex(String[] databases) {
        int lruIndex = -1;
        long oldestTime = Long.MAX_VALUE;
        for (int i = 0; i < databases.length; i++) {
            if (databases[i] != null) {
                File dbFile = mContext.getDatabasePath(databases[i]);
                if (!dbFile.getName().endsWith("-journal") && !dbFile.getName().endsWith("-shm") && !dbFile.getName().endsWith("-wal")) {
                    long time = dbFile.lastModified();
                    if (time < oldestTime) {
                        oldestTime = time;
                        lruIndex = i;
                    }
                }
            }
        }
        return lruIndex;
    }
}

//<End of snippet n. 0>