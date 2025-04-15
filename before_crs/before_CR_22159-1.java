/*To prevent the reference to null pointer

The installProvider method returns null when fail to get instance of holder.provider.
In this case, prov is null and referencing of prov.asBinder() causes system crash.

Change-Id:I39434c15e74e9fb9ca7294c8a3d7816626462e76*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
old mode 100644
new mode 100755
//Synthetic comment -- index 162d9eb..4566520

//Synthetic comment -- @@ -3356,7 +3356,7 @@
IContentProvider prov = installProvider(context, holder.provider,
holder.info, true);
//Slog.i(TAG, "noReleaseNeeded=" + holder.noReleaseNeeded);
        if (holder.noReleaseNeeded || holder.provider == null) {
// We are not going to release the provider if it is an external
// provider that doesn't care about being released, or if it is
// a local provider running in this process.







