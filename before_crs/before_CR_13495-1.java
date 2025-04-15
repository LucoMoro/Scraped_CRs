/*Fixs the incorrect message for SecurityException

when injecting a Key, Pointer and Trackball events into the UI across
applications, the corresponding methods throw SecurityException with
incorrect permission message.
INJECT EVENT permission should be INJECT_EVENTS*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 327cd72..b87ad32 100644

//Synthetic comment -- @@ -5285,7 +5285,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENT permission");
case INJECT_SUCCEEDED:
return true;
}
//Synthetic comment -- @@ -5313,7 +5313,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENT permission");
case INJECT_SUCCEEDED:
return true;
}
//Synthetic comment -- @@ -5341,7 +5341,7 @@
switch (result) {
case INJECT_NO_PERMISSION:
throw new SecurityException(
                        "Injecting to another application requires INJECT_EVENT permission");
case INJECT_SUCCEEDED:
return true;
}







