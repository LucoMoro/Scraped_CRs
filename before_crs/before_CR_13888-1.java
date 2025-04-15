/*Added null check before objects from mSyncStatus are used.

mSyncStatus is a sparseArray but isn't treated as such,
this caused crashes during stability test.*/
//Synthetic comment -- diff --git a/core/java/android/content/SyncStorageEngine.java b/core/java/android/content/SyncStorageEngine.java
//Synthetic comment -- index be70909..b9a75bf 100644

//Synthetic comment -- @@ -952,10 +952,12 @@
while (i > 0) {
i--;
SyncStatusInfo stats = mSyncStatus.valueAt(i);
                AuthorityInfo authority = mAuthorities.get(stats.authorityId);
                if (authority != null && authority.enabled) {
                    if (oldest == 0 || stats.initialFailureTime < oldest) {
                        oldest = stats.initialFailureTime;
}
}
}
//Synthetic comment -- @@ -1285,7 +1287,7 @@
while (i > 0) {
i--;
st = mSyncStatus.valueAt(i);
                        if (st.authorityId == authority.ident) {
found = true;
break;
}







