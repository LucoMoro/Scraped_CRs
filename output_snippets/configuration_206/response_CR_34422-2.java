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
    int count = databases.length;
    int limit = MAX_EXTERNAL_DATABASES;

    // Filter out temporary files
    List<String> filteredDatabases = new ArrayList<>();
    for (String dbName : databases) {
        File dbFile = mContext.getDatabasePath(dbName);
        if (!dbName.endsWith("-journal") && !dbName.endsWith("-shm") && 
            !dbName.endsWith("-wal") && !INTERNAL_DATABASE_NAME.equals(dbName) && 
            dbFile.exists()) {
            filteredDatabases.add(dbName);
        }
    }

    databases = filteredDatabases.toArray(new String[0]);
    count = databases.length;

    // delete external databases that have not been used in the past two months
    long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
    for (int i = 0; i < databases.length; i++) {
        File other = mContext.getDatabasePath(databases[i]);
        long time = other.lastModified();
        
        if (time < twoMonthsAgo) {
            if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[i]);
            mContext.deleteDatabase(databases[i]);
            databases[i] = null;
            count--;
        }
    }

    // Check against database limit before deleting LRU
    if (count > limit) {
        int lruIndex = findLeastRecentlyUsedIndex(); // Assume this function exists
        if (lruIndex != -1) {
            if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[lruIndex]);
            mContext.deleteDatabase(databases[lruIndex]);
            databases[lruIndex] = null;
            count--;
        }
    }

//<End of snippet n. 0>