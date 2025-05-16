//<Beginning of snippet n. 0>


import java.io.File;

static final class DatabaseHelper extends SQLiteOpenHelper {
    final Context mContext;
    final String mName;
    final boolean mInternal;

    // delete least recently used databases if we are over the limit
    String[] databases = mContext.databaseList();
    int count = databases.length;
    int limit = MAX_EXTERNAL_DATABASES;

    // Filter out temporary files
    List<String> validDatabases = new ArrayList<>();
    for (String dbName : databases) {
        File dbFile = mContext.getDatabasePath(dbName);
        if (!INTERNAL_DATABASE_NAME.equals(dbName) && dbFile.exists() && !dbName.endsWith(".tmp")) {
            validDatabases.add(dbName);
        }
    }

    long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
    Iterator<String> iterator = validDatabases.iterator();
    while (iterator.hasNext()) {
        String dbName = iterator.next();
        File dbFile = mContext.getDatabasePath(dbName);
        long time = dbFile.lastModified();
        if (time < twoMonthsAgo) {
            if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + dbName);
            mContext.deleteDatabase(dbName);
            iterator.remove();
            count--;
        }
    }

    // Ensure lruIndex is valid
    if (validDatabases.size() > 0) {
        int lruIndex = getLruIndex(validDatabases); // Assume this method exists
        if (lruIndex >= 0 && lruIndex < validDatabases.size()) {
            String dbToDelete = validDatabases.get(lruIndex);
            if (LOCAL_LOGV) Log.v(TAG, "Deleting least recently used database " + dbToDelete);
            mContext.deleteDatabase(dbToDelete);
            validDatabases.remove(lruIndex);
            count--;
        }
    }
//<End of snippet n. 0>