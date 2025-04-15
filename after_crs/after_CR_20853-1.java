/*Support video/mp4 content type

Change-Id:I388e6066b05872993c3f15167d2250d7732c6d8fSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/model/VideoModel.java b/src/com/android/mms/model/VideoModel.java
//Synthetic comment -- index 958c3bb..ec77b9e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.util.Config;
import android.util.Log;

import com.google.android.mms.ContentType;
import java.io.IOException;

public class VideoModel extends RegionMediaModel {
//Synthetic comment -- @@ -76,6 +77,25 @@
throw new MmsException("Type of media is unknown.");
}

                    if (mContentType.equals(ContentType.VIDEO_MP4) && !(TextUtils.isEmpty(mSrc))) {
                        int index = mSrc.lastIndexOf(".");
                        if (index != -1) {
                            try {
                                String extension = mSrc.substring(index + 1);
                                if (!(TextUtils.isEmpty(extension)) &&
                                        (extension.equalsIgnoreCase("3gp") ||
                                        extension.equalsIgnoreCase("3gpp") ||
                                        extension.equalsIgnoreCase("3g2"))) {
                                    mContentType = ContentType.VIDEO_3GPP;
                                }
                            } catch(IndexOutOfBoundsException ex) {
                                if (LOCAL_LOGV) {
                                    Log.v(TAG, "Media extension is unknown.");
                                }
                            }
                        }
                    }

if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
Log.v(TAG, "New VideoModel created:"
+ " mSrc=" + mSrc







