```
<Beginning of snippet n. 0>
import java.io.File;
import java.util.ArrayList;
import java.util.List;

static final class DatabaseHelper extends SQLiteOpenHelper {
    final Context mContext;
    final String mName;
    final boolean mInternal;  // True if this is the internal database

    void cleanupDatabases() {
        String[] databases = mContext.databaseList();
        List<String> externalDatabases = new ArrayList<>();
        long now = System.currentTimeMillis();
        long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;

        for (String db : databases) {
            File other = mContext.getDatabasePath(db);
            if (!INTERNAL_DATABASE_NAME.equals(db) && other != null && isExternalDatabase(db)) {
                externalDatabases.add(db);
            }
        }

        int count = externalDatabases.size();
        int limit = MAX_EXTERNAL_DATABASES;

        while (count > limit) {
            long oldestTime = Long.MAX_VALUE;
            int lruIndex = -1;

            for (int i = 0; i < externalDatabases.size(); i++) {
                File other = mContext.getDatabasePath(externalDatabases.get(i));
                long time = other.lastModified();

                if (time < twoMonthsAgo) {
                    if (LOCAL_LOGV) Log.v(TAG, "Deleting obsolete database " + externalDatabases.get(i));
                    boolean deleted = deleteDatabaseWithAssociatedFiles(other);
                    if (deleted) {
                        externalDatabases.remove(i);
                        count--;
                        i--;
                    }
                } else if (time < oldestTime) {
                    oldestTime = time;
                    lruIndex = i;
                }
            }

            if (lruIndex != -1) {
                if (LOCAL_LOGV) Log.v(TAG, "Deleting least recently used database " + externalDatabases.get(lruIndex));
                File dbFile = mContext.getDatabasePath(externalDatabases.get(lruIndex));
                boolean deleted = deleteDatabaseWithAssociatedFiles(dbFile);
                if (deleted) {
                    externalDatabases.remove(lruIndex);
                    count--;
                }
            }
        }
    }

    private boolean deleteDatabaseWithAssociatedFiles(File dbFile) {
        boolean deleted = dbFile.delete();
        if (deleted) {
            File shmFile = new File(dbFile.getPath() + "-shm");
            if (shmFile.exists()) shmFile.delete();
            File walFile = new File(dbFile.getPath() + "-wal");
            if (walFile.exists()) walFile.delete();
            if (LOCAL_LOGV) Log.v(TAG, "Deleted database and associated temporary files: " + dbFile.getName());
        } else {
            Log.e(TAG, "Failed to delete database: " + dbFile.getName());
        }
        return deleted;
    }

    private boolean isExternalDatabase(String dbName) {
        return !dbName.endsWith("-journal") && !dbName.endsWith("-shm") && !dbName.endsWith("-wal");
    }
}
<End of snippet n. 0>