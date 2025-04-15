/*frameworks/base: Fix to release reference in am

BackupRecord has a reference to ProcessRecord which isn't
cleared when the BackupAgent is unbound, fix is to release
the corresponding reference.

Fix to release references of ActivityRecord in Pending Activity list,
Connection Records list of a service.

Change-Id:I0007766edb1da0a57ba3d46d48ef34531e0e1f6c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 2322ee1..96b79d5 100644

//Synthetic comment -- @@ -2070,6 +2070,8 @@
mMainStack.startActivityUncheckedLocked(pal.r, pal.sourceRecord,
pal.grantedUriPermissions, pal.grantedMode, pal.onlyIfNeeded,
doResume && i == (N-1));
            pal.r = null;
            pal.sourceRecord = null;
}
mPendingActivityLaunches.clear();
}
//Synthetic comment -- @@ -2569,6 +2571,7 @@
mMainStack.mHistory.remove(i);

r.inHistory = false;
                    r.resultTo = null;
mWindowManager.removeAppToken(r);
if (VALIDATE_TOKENS) {
mWindowManager.validateAppTokens(mMainStack.mHistory);
//Synthetic comment -- @@ -9121,6 +9124,10 @@
TAG, "Removed service that is not running: " + r);
}

        if (r.connections.size() > 0) {
            r.connections.clear();
        }

if (r.bindings.size() > 0) {
r.bindings.clear();
}
//Synthetic comment -- @@ -9948,6 +9955,7 @@
}

ProcessRecord proc = mBackupTarget.app;
            mBackupTarget.app = null;
mBackupTarget = null;
mBackupAppName = null;









//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 463493b..618060f 100644

//Synthetic comment -- @@ -3098,6 +3098,7 @@
if (r.state != ActivityState.DESTROYED) {
mHistory.remove(r);
r.inHistory = false;
            r.resultTo = null;
r.state = ActivityState.DESTROYED;
mService.mWindowManager.removeAppToken(r);
if (VALIDATE_TOKENS) {







