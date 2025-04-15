/*Improve shutdown time without SDcard plugged

Change-Id:If6c30843a20b768afdea218633b17815d597ef52Signed-off-by: christian bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 4d934b6..b69019f 100644

//Synthetic comment -- @@ -1140,7 +1140,12 @@
}
}

        if (state.equals(Environment.MEDIA_MOUNTED)) {
// Post a unmount message.
ShutdownCallBack ucb = new ShutdownCallBack(path, observer);
mHandler.sendMessage(mHandler.obtainMessage(H_UNMOUNT_PM_UPDATE, ucb));







