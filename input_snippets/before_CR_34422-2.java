
//<Beginning of snippet n. 0>


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

// delete external databases that have not been used in the past two months
long twoMonthsAgo = now - OBSOLETE_DATABASE_DB;
for (int i = 0; i < databases.length; i++) {
File other = mContext.getDatabasePath(databases[i]);
if (INTERNAL_DATABASE_NAME.equals(databases[i]) || file.equals(other)) {
databases[i] = null;
long time = other.lastModified();
if (time < twoMonthsAgo) {
if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[i]);
                        mContext.deleteDatabase(databases[i]);
databases[i] = null;
count--;
}
// delete least recently used database
if (lruIndex != -1) {
if (LOCAL_LOGV) Log.v(TAG, "Deleting old database " + databases[lruIndex]);
                    mContext.deleteDatabase(databases[lruIndex]);
databases[lruIndex] = null;
count--;
}

//<End of snippet n. 0>








