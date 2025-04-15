/*Mms: Catch UnsupportContentTypeException

The original code has not catched "Unsupported Content-Type" exception
resulting in force close if an mms containing an unsupported content
type is opened.

Change-Id:I9b578e2fd61ba5df9dbfbe9e6e28fb276ef89666Author: Haixiong Zheng <haixiong.zheng@borqs.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 5339*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/SlideshowModel.java b/src/com/android/mms/model/SlideshowModel.java
//Synthetic comment -- index 722ee9d..3e79e3e 100755

//Synthetic comment -- @@ -60,6 +60,7 @@
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;

public class SlideshowModel extends Model
implements List<SlideModel>, IModelChangedObserver {
//Synthetic comment -- @@ -214,6 +215,8 @@
Log.e(TAG, e.getMessage(), e);
} catch (IllegalArgumentException e) {
Log.e(TAG, e.getMessage(), e);
}
}








