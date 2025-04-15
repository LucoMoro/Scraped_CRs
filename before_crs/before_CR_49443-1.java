/*[ScreentShot]: change the implementation of get image file size

Because File can't get permission to get file size of screenshot image.
So use InputStream to get file size correctly of screenshot image.

Change-Id:I1c3df40ddd526d3431bc899642d00f4021d7eeb3Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: shi yang <yang.a.shi@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30940*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java b/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java
//Synthetic comment -- index f25ac0d..11a5683 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
import android.widget.ImageView;

import com.android.systemui.R;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
//Synthetic comment -- @@ -204,8 +204,11 @@

// update file size in the database
values.clear();
            values.put(MediaStore.Images.ImageColumns.SIZE, new File(mImageFilePath).length());
resolver.update(uri, values, null, null);

params[0].imageUri = uri;
params[0].result = 0;







