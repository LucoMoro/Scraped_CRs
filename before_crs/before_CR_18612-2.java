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
} catch (RemoteException e) {
Slog.w(TAG, "Failure sending broadcast result of " + r.intent, e);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BroadcastRecord.java b/services/java/com/android/server/am/BroadcastRecord.java
//Synthetic comment -- index b268efa..6851d2c 100644

//Synthetic comment -- @@ -45,7 +45,8 @@
final boolean initialSticky; // initial broadcast from register to sticky?
final String requiredPermission; // a permission the caller has required
final List receivers;   // contains BroadcastFilter and ResolveInfo
    final IIntentReceiver resultTo; // who receives final result if non-null
long dispatchTime;      // when dispatch started on this set of receivers
long receiverTime;      // when current receiver started for timeouts.
long finishTime;        // when we finished the broadcast.
//Synthetic comment -- @@ -102,8 +103,8 @@
if (anrCount != 0) {
pw.print(prefix); pw.print("anrCount="); pw.println(anrCount);
}
        if (resultTo != null || resultCode != -1 || resultData != null) {
            pw.print(prefix); pw.print("resultTo="); pw.print(resultTo);
pw.print(" resultCode="); pw.print(resultCode);
pw.print(" resultData="); pw.println(resultData);
}
//Synthetic comment -- @@ -170,6 +171,7 @@
requiredPermission = _requiredPermission;
receivers = _receivers;
resultTo = _resultTo;
resultCode = _resultCode;
resultData = _resultData;
resultExtras = _resultExtras;







