/*Added catch of SQLiteException when updating screenshot

Error found when running Monkey test. User probably tries
to add a bookmark and directly after deleting it. The method
updateScreenshot is running an AsyncTask and gets an Exception
from the database when the bookmark does not exist. The deletion
of the bookmark runs in another thread.

Change-Id:I63bb954419f60fb5106c1a6597692b6bac925714*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 0a3fec9..d2898f3 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
//Synthetic comment -- @@ -2422,6 +2423,10 @@
}
} catch (IllegalStateException e) {
// Ignore
                } catch (SQLiteException s) {
                    // Added for possible error when user tries to remove the same bookmark
                    // that is being updated with a screen shot
                    Log.w(LOGTAG, "Error when running updateScreenshot ", s);
} finally {
if (c != null) c.close();
}







