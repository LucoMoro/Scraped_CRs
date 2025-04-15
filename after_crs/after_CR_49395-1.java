/*Fix the creation of empty file when screenshot is failed.

The doInBackground function does not check the return value from
compress method in Bitmap class. So an empty file is created when
compress fails.

The original code does not clean up file and DB when it fails to
create a screenshot.

This fix adds check the return value and cleans up file and DB
when compress method fails.

Change-Id:I80182a1cba8dde74a11a84e718a8a93770b1597b*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java b/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java
//Synthetic comment -- index f25ac0d..7a734ab 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
import com.android.systemui.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//Synthetic comment -- @@ -198,10 +199,17 @@
PendingIntent.FLAG_CANCEL_CURRENT));

OutputStream out = resolver.openOutputStream(uri);
            boolean success = image.compress(Bitmap.CompressFormat.PNG, 100, out);
out.flush();
out.close();

            if (!success) {
                resolver.delete(uri, null, null);
                File file = new File(mImageFilePath);
                file.delete();
                throw new IOException();
            }

// update file size in the database
values.clear();
values.put(MediaStore.Images.ImageColumns.SIZE, new File(mImageFilePath).length());







