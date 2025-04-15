/*orkut: fix "orkut has stopped" error message when upload a image

An exception java.lang.NullpointerException is thrown because "intent" is
null pointer. here we avold using null intent pointer, the image can be uploaded
into orkut.

Change-Id:I840527f1e00f8c519e086b2a85f6b7054d5a51bfAuthor: Dongxing Zhang <dongxing.zhang@intel.com>
Signed-off-by: Dongxing Zhang <dongxing.zhang@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39297*/




//Synthetic comment -- diff --git a/core/java/android/app/PendingIntent.java b/core/java/android/app/PendingIntent.java
//Synthetic comment -- index 8adc8a2..09900e5 100644

//Synthetic comment -- @@ -221,7 +221,9 @@
String resolvedType = intent != null ? intent.resolveTypeIfNeeded(
context.getContentResolver()) : null;
try {
            if(intent != null) {
                intent.setAllowFds(false);
            }
IIntentSender target =
ActivityManagerNative.getDefault().getIntentSender(
ActivityManager.INTENT_SENDER_ACTIVITY, packageName,







