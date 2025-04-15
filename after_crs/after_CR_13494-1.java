/*Fixs the incorrect message for SecurityException

when injecting a Key, Pointer and Trackball events into the UI across
applications, the corresponding methods throw SecurityException with
incorrect permission message.
INJECT EVENT permission should be INJECT_EVENTS*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index d4c27b7..6290c49 100644

//Synthetic comment -- @@ -4291,7 +4291,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENTS permission");
case INJECT_SUCCEEDED:
return true;
}
//Synthetic comment -- @@ -4319,7 +4319,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENTS permission");
case INJECT_SUCCEEDED:
return true;
}
//Synthetic comment -- @@ -4347,7 +4347,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENTS permission");
case INJECT_SUCCEEDED:
return true;
}







