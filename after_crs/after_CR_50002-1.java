/*data: enhanced Telephony Carrier DB transaction time

APN reset to default time is faster than old.

Change-Id:Iddf2b36d358d210bdb55893246f06389c3f89d3e*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 7ec2aed..040a3d7 100755

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
//Synthetic comment -- @@ -320,6 +321,7 @@
private void loadApns(SQLiteDatabase db, XmlPullParser parser) {
if (parser != null) {
try {
                    db.beginTransaction();
while (true) {
XmlUtils.nextElement(parser);
ContentValues row = getRow(parser);
//Synthetic comment -- @@ -329,10 +331,15 @@
break;  // do we really want to skip the rest of the file?
}
}
                    db.setTransactionSuccessful();
} catch (XmlPullParserException e)  {
Log.e(TAG, "Got execption while getting perferred time zone.", e);
} catch (IOException e) {
Log.e(TAG, "Got execption while getting perferred time zone.", e);
                } catch (SQLException e){
                    Log.e(TAG, "Got SQLException", e);
                } finally {
                    db.endTransaction();
}
}
}







