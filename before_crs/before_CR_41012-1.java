/*Fix the issue for IO Exception

In some specific case, we are facing a crash in the SQliteDB when
deleting an information stored in the SQLiteDB.
To secured the bluetooth management, I have added a catch of this
IOException to avoid an application crash and treat the Exception.

Change-Id:I0283f25bafa6212a6562b33298f25b6d575e706bAuthor: Sebastien Cayetanot <sebastien.cayetanot@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 9199 17106*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppProvider.java b/src/com/android/bluetooth/opp/BluetoothOppProvider.java
//Synthetic comment -- index 864d6a2..06fffce 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;
import android.util.Log;
//Synthetic comment -- @@ -433,7 +434,7 @@
@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
int match = sURIMatcher.match(uri);
switch (match) {
case SHARES:
//Synthetic comment -- @@ -453,8 +454,11 @@
long rowId = Long.parseLong(segment);
myWhere += " ( " + BluetoothShare._ID + " = " + rowId + " ) ";
}

                count = db.delete(DB_TABLE, myWhere, selectionArgs);
break;
}
default: {







