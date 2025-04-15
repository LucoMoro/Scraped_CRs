/*Check ProcessesReady before broadcast

Most of the time testIsSystemReady is used to know if we are allowed to broadcast intents.
But in broadcast intent method we check mProcessesReady flag (not mSystemReady).
If we broadcast after systemReady flag but before ProcessesReady flag, system will crash

Adding condition to check if system & processes are ready fix that race condition

Change-Id:I17e42bff957164b139f85ee92ace1c5867e7265aSigned-off-by: Fabien DUVOUX <fabien.duvoux@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index db64a9a..5a56f05 100644

//Synthetic comment -- @@ -7619,7 +7619,7 @@

public boolean testIsSystemReady() {
// no need to synchronize(this) just to read & return the value
        return mSystemReady;
}

private static File getCalledPreBootReceiversFile() {







