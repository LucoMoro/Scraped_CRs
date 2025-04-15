/*Movie studio: fix operation option missing in movie studio when create new project

Root cause: the time series between two threads can't work well, update the menus
after finishing the export operation immediately can fix this issue.

Change-Id:Ifda968c2e79c80f59d726073b735ecb9cfbb4070Author: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39133*/
//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index 193bae3..49ab345 100755

//Synthetic comment -- @@ -1453,6 +1453,7 @@
mExportProgressDialog.dismiss();
mExportProgressDialog = null;
}
}

@Override







