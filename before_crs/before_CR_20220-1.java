/*Referencing of "r.record.name" is occured, when "r.record" is null.

Change-Id:I625e45923a7fd9f4ac8eb434ed07d452f8e1e0b6*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index da715db..e653c3f

//Synthetic comment -- @@ -9266,7 +9266,7 @@
// r.record is null if findServiceLocked() failed the caller permission check
if (r.record == null) {
throw new SecurityException(
                            "Permission Denial: Accessing service " + r.record.name
+ " from pid=" + Binder.getCallingPid()
+ ", uid=" + Binder.getCallingUid()
+ " requires " + r.permission);







