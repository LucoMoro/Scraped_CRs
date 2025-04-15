/*Synchronize access to *Locked() functions

Several places were calling *Locked() functions without properly
synchronizing.

Change-Id:Ie39b6592da8bb5f4a5a1e738c45f228256116ec4*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 1359f66..9e9552a 100644

//Synthetic comment -- @@ -4931,9 +4931,11 @@
Intent intent = new Intent(Intent.ACTION_PACKAGE_DATA_CLEARED,
Uri.fromParts("package", packageName, null));
intent.putExtra(Intent.EXTRA_UID, pkgUid);
                broadcastIntentLocked(null, null, intent,
                        null, null, 0, null, null, null,
                        false, false, MY_PID, Process.SYSTEM_UID);
} catch (RemoteException e) {
}
} finally {
//Synthetic comment -- @@ -5668,7 +5670,9 @@
ArrayList<ProcessRecord> procs =
new ArrayList<ProcessRecord>(mProcessesOnHold);
for (int ip=0; ip<NP; ip++) {
                this.startProcessLocked(procs.get(ip), "on-hold", null);
}
}
if (mFactoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
//Synthetic comment -- @@ -7879,9 +7883,14 @@
}

public static final void installSystemProviders() {
        ProcessRecord app = mSelf.mProcessNames.get("system", Process.SYSTEM_UID);
        List providers = mSelf.generateApplicationProvidersLocked(app);
        mSystemThread.installSystemProviders(providers);
}

// =========================================================
//Synthetic comment -- @@ -8156,11 +8165,15 @@
}

public void registerActivityWatcher(IActivityWatcher watcher) {
        mWatchers.register(watcher);
}

public void unregisterActivityWatcher(IActivityWatcher watcher) {
        mWatchers.unregister(watcher);
}

public final void enterSafeMode() {







