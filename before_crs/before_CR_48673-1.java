/*Am: acquire lock only when needed in removeContentProvider

Fix to make sure lock is acquired only after the validations
are done in removeContentProvider.

Change-Id:If4942162f58f2f9af0710e3e9e4d9fa87b237098Author: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 58594*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index db64a9a..0aa640e 100644

//Synthetic comment -- @@ -6665,19 +6665,19 @@
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







