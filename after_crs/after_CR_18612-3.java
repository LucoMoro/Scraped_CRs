/*Clear reference to the IIntentReceiver in order to avoid memory leak

When using sendOrderedBroadcast(..) with a BroadcastReceiver the
BroadcastReceiver instance was not released. The reason for this was that
the resultTo field in the BroadcastRecord kept a reference until it was pushed
out of the mBroadcastHistory. This reference in turn kept a reference to the
process side IIntentReceiver (implemented in ReceiverDispatcher$InnerReceiver).
This in turn had a strong reference (through mStrongRef) to the Context.

In order to keep the debug output the resultTo is also kept as a String in the
new resultToString variable.

Change-Id:I4382a22a541c27b3694fb2b78a04ee820b235f8f*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 16cd62b..da715db 100644

//Synthetic comment -- @@ -11035,6 +11035,9 @@
performReceiveLocked(r.callerApp, r.resultTo,
new Intent(r.intent), r.resultCode,
r.resultData, r.resultExtras, false, false);
                            // Set this to null so that the reference
                            // (local and remote) isnt kept in the mBroadcastHistory.
                            r.resultTo = null;
} catch (RemoteException e) {
Slog.w(TAG, "Failure sending broadcast result of " + r.intent, e);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BroadcastRecord.java b/services/java/com/android/server/am/BroadcastRecord.java
//Synthetic comment -- index b268efa..c95053e 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
final boolean initialSticky; // initial broadcast from register to sticky?
final String requiredPermission; // a permission the caller has required
final List receivers;   // contains BroadcastFilter and ResolveInfo
    IIntentReceiver resultTo; // who receives final result if non-null
long dispatchTime;      // when dispatch started on this set of receivers
long receiverTime;      // when current receiver started for timeouts.
long finishTime;        // when we finished the broadcast.







