/*ExternalStorageFormatter: phone call can be picked up during erasing

During the erasing, dialog is on the top of the screen so that the
setting->storage cannot be entered again until the erasing is done.

During erasing, if there is a phone call, still allow this phone call
to be picked up

Change-Id:Ifa5b6bf320ff313cd1df15276a97e50952493d10Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57131*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java b/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java
//Synthetic comment -- index fb7f215..f8d41bd 100644

//Synthetic comment -- @@ -91,6 +91,8 @@
mProgressDialog.setIndeterminate(true);
mProgressDialog.setCancelable(true);
mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
if (!mAlwaysReset) {
mProgressDialog.setOnCancelListener(this);
}
//Synthetic comment -- @@ -224,6 +226,8 @@
mProgressDialog.setIndeterminate(true);
mProgressDialog.setCancelable(false);
mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
mProgressDialog.show();
}








