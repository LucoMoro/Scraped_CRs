//<Beginning of snippet n. 0>


import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.io.File;
import java.util.ArrayList;

/**
 * on demand, create and upgrade the schema, etc.
 */
static final class DatabaseHelper extends SQLiteOpenHelper {
    final Context mContext;
    final String mName;
    final boolean mInternal;  // True if this is the internal database

    // delete least recently used databases if we are over the limit
    String[] databases = mContext.databaseList();
    List<String> databasesToDelete = new ArrayList<>();
    int count = 0;
    int limit = MAX_EXTERNAL_DATABASES;

    // delete external databases that have not been used in the past two months
    long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
    for (String database : databases) {
        File other = mContext.getDatabasePath(database);
        if (INTERNAL_DATABASE_NAME.equals(database) || other == null || !other.exists() || database.startsWith("temp_")) {
            continue;
        }

        long time = other.lastModified();
        if (time < twoMonthsAgo) {
            if (LOCAL_LOGV) Log.v(TAG, "Queueing old database for deletion: " + database);
            databasesToDelete.add(database);
        }
    }

    List<String> validDatabasesToDelete = new ArrayList<>();
    for (String database : databasesToDelete) {
        if (database != null && !database.isEmpty()) {
            validDatabasesToDelete.add(database);
        }
    }

    for (String database : validDatabasesToDelete) {
        if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + database);
        if (mContext.deleteDatabase(database)) {
            count--;
        }
    }

    // delete least recently used database if needed
    if (lruIndex != -1 && count > limit) {
        if (LOCAL_LOGV) Log.v(TAG, "Deleting least recently used database " + databases[lruIndex]);
        if (mContext.deleteDatabase(databases[lruIndex])) {
            count--;
        }
    }

//<End of snippet n. 0>