/*Mms: process stops after sharing a video through Messaging

Some logic should be added to deal with different MMS content uri.

testcase: 1> In the messaging application, create a new message
          2> Attach a recorded video
          3> View the video once
          4> View the video twice
          5> Share it through Messaging when playing it

Some logic should be added to deal with different MMS content uri.

Change-Id:I2d7f2ec034823a17ecc517f0861a00958bbb1b38Author: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27059*/




//Synthetic comment -- diff --git a/src/com/android/mms/model/VideoModel.java b/src/com/android/mms/model/VideoModel.java
old mode 100644
new mode 100755
//Synthetic comment -- index a71e455..52b8b3d

//Synthetic comment -- @@ -38,6 +38,7 @@
import com.android.mms.util.ItemLoadedCallback;
import com.android.mms.util.ItemLoadedFuture;
import com.android.mms.util.ThumbnailManager;
import android.provider.Telephony.Mms.Part;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;

//Synthetic comment -- @@ -110,8 +111,13 @@
path = uri.toString();
}
mSrc = path.substring(path.lastIndexOf('/') + 1);
                    if (VideoModel.isMmsUri(uri)) {
                        mContentType = c.getString(c.getColumnIndexOrThrow(
                                Part.CONTENT_TYPE));
                    } else {
                        mContentType = c.getString(c.getColumnIndexOrThrow(
                                Images.Media.MIME_TYPE));
                    }
if (TextUtils.isEmpty(mContentType)) {
throw new MmsException("Type of media is unknown.");
}







