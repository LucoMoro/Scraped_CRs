/*Make the DownloadProvider work in the simulator*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index 6b3124a..0ce6b22 100644

//Synthetic comment -- @@ -385,7 +385,8 @@
}
}

        if (Binder.getCallingPid() != Process.myPid() && Binder.getCallingUid() != 0) {
if (!emptyWhere) {
qb.appendWhere(" AND ");
}







