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
    String[] databases;
    int count = 0;
    int limit = MAX_EXTERNAL_DATABASES;

    public DatabaseHelper(Context context, String name, boolean internal) {
        super(context, name, null, DATABASE_VERSION);
        this.mContext = context;
        this.mName = name;
        this.mInternal = internal;
        initializeDatabases();
    }

    private void initializeDatabases() {
        databases = mContext.databaseList();
        long twoMonthsAgo = System.currentTimeMillis() - OBSOLETE_DATABASE_DB;
        for (String database : databases) {
            File other = mContext.getDatabasePath(database);
            if (!INTERNAL_DATABASE_NAME.equals(database) && other != null && !isTemporaryFile(database)) {
                long time = other.lastModified();
                if (time < twoMonthsAgo) {
                    if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + database);
                    try {
                        mContext.deleteDatabase(database);
                        other.delete();
                        count++;
                    } catch (Exception e) {
                        Log.e(TAG, "Error deleting database " + database, e);
                    }
                }
            }
        }

        manageLRU();
    }

    private void manageLRU() {
        if (count > limit) {
            int lruIndex = getLRUIndex(databases);
            if (lruIndex != -1 && databases[lruIndex] != null) {
                if (LOCAL_LOGV) Log.v(TAG, "Deleting least recently used database " + databases[lruIndex]);
                try {
                    mContext.deleteDatabase(databases[lruIndex]);
                    File lruFile = mContext.getDatabasePath(databases[lruIndex]);
                    if (lruFile.exists()) {
                        lruFile.delete();
                    }
                    databases[lruIndex] = null;
                    count--;
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting least recently used database " + databases[lruIndex], e);
                }
            }
        }
    }

    private int getLRUIndex(String[] databases) {
        int lruIndex = -1;
        long oldestTime = Long.MAX_VALUE;
        for (int i = 0; i < databases.length; i++) {
            if (databases[i] != null) {
                File dbFile = mContext.getDatabasePath(databases[i]);
                if (!isTemporaryFile(dbFile.getName())) {
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

    private boolean isTemporaryFile(String fileName) {
        return fileName.endsWith("-journal") || fileName.endsWith("-shm") || fileName.endsWith("-wal");
    }
}

//<End of snippet n. 0>