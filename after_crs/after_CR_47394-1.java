/*fix operation option missing in movie studio when create new project

Root cause:
the time series between two threads can't work well, update the menus
after finishing the export operation immediately can fix this issue.

Change-Id:I36262483f9512d8f2b32c08e839e8d5d7dcc6a40Author: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39133*/




//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index ca0b770..438fda5 100755

//Synthetic comment -- @@ -1454,6 +1454,7 @@
mExportProgressDialog.dismiss();
mExportProgressDialog = null;
}
        invalidateOptionsMenu();
}

@Override







