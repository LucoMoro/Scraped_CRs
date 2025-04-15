/*Possible NPE in ActivityManagerService

Change-Id:Ib3effa9b2dc371b0678b9cf645d4c927684c01d1*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5d74cf3..352f3ef 100644

//Synthetic comment -- @@ -11872,7 +11872,7 @@
// r.record is null if findServiceLocked() failed the caller permission check
if (r.record == null) {
throw new SecurityException(
                            "Permission Denial: Accessing service"
+ " from pid=" + Binder.getCallingPid()
+ ", uid=" + Binder.getCallingUid()
+ " requires " + r.permission);







