/*Mms: process stops after sharing a video through Messaging

testcase: 1> In the messaging application, create a new message
          2> Attach a recorded video
          3> View the video once
          4> View the video twice
          5> Share it through Messaging when playing it

Some logic should be added to deal with different MMS content uri.

Change-Id:I19b3fabd12f1afbda582bc2c9c9a99405f311f1aAuthor: b533 <b533@borqs.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27059*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/VideoModel.java b/src/com/android/mms/model/VideoModel.java
//Synthetic comment -- index 67f4be3..cc98de2 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
//Synthetic comment -- @@ -109,8 +110,13 @@
path = uri.toString();
}
mSrc = path.substring(path.lastIndexOf('/') + 1);
                    mContentType = c.getString(c.getColumnIndexOrThrow(
                            Images.Media.MIME_TYPE));
if (TextUtils.isEmpty(mContentType)) {
throw new MmsException("Type of media is unknown.");
}







