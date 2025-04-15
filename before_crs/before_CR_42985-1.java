/*Am: acquire lock only when needed in removeContentProvider

Fix to make sure lock is acquired only after the validations
are done in removeContentProvider.

Change-Id:Ied10a8ed26173ac8706f7c66e55a2f58eff72b4e*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..8aeab64 100644

//Synthetic comment -- @@ -6471,19 +6471,19 @@
*/
public void removeContentProvider(IBinder connection, boolean stable) {
enforceNotIsolatedCaller("removeContentProvider");
synchronized (this) {
            ContentProviderConnection conn;
            try {
                conn = (ContentProviderConnection)connection;
            } catch (ClassCastException e) {
                String msg ="removeContentProvider: " + connection
                        + " not a ContentProviderConnection";
                Slog.w(TAG, msg);
                throw new IllegalArgumentException(msg);
            }
            if (conn == null) {
                throw new NullPointerException("connection is null");
            }
if (decProviderCountLocked(conn, null, null, stable)) {
updateOomAdjLocked();
}







