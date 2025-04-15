/*frameworks/base: acquire lock on am only when needed

appendDropBoxProcessHeaders acquires a lock on am while accessing
ProcessRecord (even if it is null). Watchdog thread ends up invoking
this function (with a null ProcessRecord) to add the stack file to
dropbox. This function would block if and when the watchdog thread is
invoked due to unavailability of lock on am resulting in a deadlock.
This would prevent watchdog from killing system_server.

Change-Id:Ieb34b767779cb587e0c5f536b9b7ba44fb9a28d9*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 2322ee1..aff14bd 100644

//Synthetic comment -- @@ -6730,18 +6730,19 @@
* to append various headers to the dropbox log text.
*/
private void appendDropBoxProcessHeaders(ProcessRecord process, StringBuilder sb) {
// Note: ProcessRecord 'process' is guarded by the service
// instance.  (notably process.pkgList, which could otherwise change
// concurrently during execution of this method)
synchronized (this) {
            if (process == null || process.pid == MY_PID) {
sb.append("Process: system_server\n");
} else {
sb.append("Process: ").append(process.processName).append("\n");
}
            if (process == null) {
                return;
            }
int flags = process.info.flags;
IPackageManager pm = AppGlobals.getPackageManager();
sb.append("Flags: 0x").append(Integer.toString(flags, 16)).append("\n");







