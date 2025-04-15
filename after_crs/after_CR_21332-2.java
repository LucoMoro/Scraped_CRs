/*Fixing improper boxing to Integer instead of Long.

This is tricky: entry.getMessageBox() returns int, so it is
by default boxed to Integer. Map.get() however accepts Objects,
so it ignores the difference, but nothing is find during lookup,
just because Long and Integer objects are compared using equals(),
which has to lead to 'false' result. So, explicit boxning to Long
is unnecessary here.

Change-Id:I967a7829a8926410a00274c6b5e312e1af567299*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/util/PduCache.java b/core/java/com/google/android/mms/util/PduCache.java
//Synthetic comment -- index 7c3fad7..866ca1e 100644

//Synthetic comment -- @@ -235,7 +235,7 @@
}

private void removeFromMessageBoxes(Uri key, PduCacheEntry entry) {
        HashSet<Uri> msgBox = mThreads.get(Long.valueOf(entry.getMessageBox()));
if (msgBox != null) {
msgBox.remove(key);
}







