/*Added catch of SQLiteException when updating screenshot

Error found when running Monkey test. User probably tries
to add a bookmark and directly after deleting it. The method
updateScreenshot is running an AsyncTask and gets an Exception
from the database when the bookmark does not exist. The deletion
of the bookmark runs in another thread.

Change-Id:I63bb954419f60fb5106c1a6597692b6bac925714*/




//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index a53e344..0e40df0 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
//Synthetic comment -- @@ -2051,6 +2052,10 @@
}
} catch (IllegalStateException e) {
// Ignore
                } catch (SQLiteException s) {
                    // Added for possible error when user tries to remove the same bookmark
                    // that is being updated with a screen shot
                    Log.w(LOGTAG, "Error when running updateScreenshot ", s);
} finally {
if (cursor != null) cursor.close();
}







