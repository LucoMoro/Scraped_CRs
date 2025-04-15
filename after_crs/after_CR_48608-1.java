/*orkut: fix "orkut has stopped" error message when upload a image

An exception java.lang.NullpointerException is thrown because "intent" is
null pointer. here we avold using null intent pointer, the image can be uploaded
into orkut.

Change-Id:I83ed8ca6d0c7c4c501c879a2a81b4b7a60bdab05Author: Dongxiang Zhang <dongxing.zhang@intel.com>
Signed-off-by: Dongxiang Zhang <dongxing.zhang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39297*/




//Synthetic comment -- diff --git a/core/java/android/app/PendingIntent.java b/core/java/android/app/PendingIntent.java
//Synthetic comment -- index d36d99d..61559d4 100644

//Synthetic comment -- @@ -274,7 +274,8 @@
String resolvedType = intent != null ? intent.resolveTypeIfNeeded(
context.getContentResolver()) : null;
try {
            if(intent != null)
                intent.setAllowFds(false);
IIntentSender target =
ActivityManagerNative.getDefault().getIntentSender(
ActivityManager.INTENT_SENDER_ACTIVITY, packageName,







