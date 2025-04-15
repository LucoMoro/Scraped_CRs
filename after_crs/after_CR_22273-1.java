/*Removing spagetti debug code.

Yes, I tried to extract it, refactor and control the execution
by DEBUG_... constant. But in fact, that code simply does not have
sense.

Change-Id:I506549981c4a5a02ecb784f40f433efe31c8a4b7*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0e38e10..342a854 100755

//Synthetic comment -- @@ -8118,37 +8118,6 @@

private final void killServicesLocked(ProcessRecord app,
boolean allowRestart) {

// Clean up any connections this application has to other services.
if (app.connections.size() > 0) {







