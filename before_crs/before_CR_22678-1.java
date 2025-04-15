/*ActivityManager hangs if process binding takes longer than 10s

On a system with heavy load, when a broadcast receiver timeouts
after the receiving process is started, but before it had time to
bind to ActivityManager, the BroadcastRecord's state is not
reinitialized to IDLE (since finishReceiverLocked() early-returns
as r.receiver is null), so the next call to processNextBroadcast()
will not process the next receiver but instead get stuck on the
current one for which an ANR was already reported.

That state is never updated afterwards: when the process finally
binds via attachApplicationLocked(), mPendingBroadcast is null, so
processCurBroadcastLocked() is not called (and the intent never
sent).

The "hung broadcast" mechanism eventually detects the problem, but
until this is done, all the records in mOrderedBroadcasts are stuck.
For a typical broadcast with 10 receivers for example, this is up to
3 minutes during which ActivityManager is blocking, waiting for
something that will never come. Moreover, the following receivers
of the same broadcast do not get called since the BroadcastRecord
is simply skipped.

This fix makes sure BroadcastRecord.state is always set to IDLE
when a timeout occurs, so that the next receiver in the list will
be processed on the next call to processNextBroadcast(). Since there
were other places where the same pattern was achieved (resetting
members of a BroadcastRecord before scheduleBroadcastsLocked()),
helper methods were added in BroadcastRecord, and some former calls
to finishReceiverLocked() are now using directly the new
BroadcastRecord.finishReceiver().

Change-Id:If4a3f6b608e3bd5205c5e05e686005cfd03647ba*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index d24ce7e..1e9faa4 100644

//Synthetic comment -- @@ -3417,7 +3417,7 @@
}
if (mPendingBroadcast != null && mPendingBroadcast.curApp.pid == pid) {
Slog.w(TAG, "Unattached app died before broadcast acknowledged, skipping");
                mPendingBroadcast.state = BroadcastRecord.IDLE;
mPendingBroadcast.nextReceiver = mPendingBroadcastRecvIndex;
mPendingBroadcast = null;
scheduleBroadcastsLocked();
//Synthetic comment -- @@ -3614,11 +3614,8 @@
+ br.curComponent.flattenToShortString(), e);
badApp = true;
logBroadcastReceiverDiscardLocked(br);
                finishReceiverLocked(br.receiver, br.resultCode, br.resultData,
                        br.resultExtras, br.resultAbort, true);
scheduleBroadcastsLocked();
                // We need to reset the state if we fails to start the receiver.
                br.state = BroadcastRecord.IDLE;
}
}

//Synthetic comment -- @@ -10590,30 +10587,14 @@
return false;
}
int state = r.state;
        r.state = r.IDLE;
if (state == r.IDLE) {
if (explicit) {
Slog.w(TAG, "finishReceiver called but state is IDLE");
}
}
        r.receiver = null;
        r.intent.setComponent(null);
        if (r.curApp != null) {
            r.curApp.curReceiver = null;
        }
        if (r.curFilter != null) {
            r.curFilter.receiverList.curBroadcast = null;
        }
        r.curFilter = null;
        r.curApp = null;
        r.curComponent = null;
        r.curReceiver = null;
        mPendingBroadcast = null;

        r.resultCode = resultCode;
        r.resultData = resultData;
        r.resultExtras = resultExtras;
        r.resultAbort = resultAbort;

// We will process the next receiver right now if this is finishing
// an app receiver (which is always asynchronous) or after we have
//Synthetic comment -- @@ -10770,8 +10751,7 @@
}

// Move on to the next receiver.
        finishReceiverLocked(r.receiver, r.resultCode, r.resultData,
                r.resultExtras, r.resultAbort, true);
scheduleBroadcastsLocked();

if (anrMessage != null) {
//Synthetic comment -- @@ -11103,7 +11083,7 @@
// process the next one.
if (DEBUG_BROADCAST) Slog.v(TAG, "Quick finishing: ordered="
+ r.ordered + " receiver=" + r.receiver);
                    r.state = BroadcastRecord.IDLE;
scheduleBroadcastsLocked();
}
return;
//Synthetic comment -- @@ -11160,9 +11140,7 @@
if (skip) {
if (DEBUG_BROADCAST)  Slog.v(TAG,
"Skipping delivery of ordered " + r + " for whatever reason");
                r.receiver = null;
                r.curFilter = null;
                r.state = BroadcastRecord.IDLE;
scheduleBroadcastsLocked();
return;
}
//Synthetic comment -- @@ -11206,10 +11184,8 @@
+ info.activityInfo.applicationInfo.uid + " for broadcast "
+ r.intent + ": process is bad");
logBroadcastReceiverDiscardLocked(r);
                finishReceiverLocked(r.receiver, r.resultCode, r.resultData,
                        r.resultExtras, r.resultAbort, true);
scheduleBroadcastsLocked();
                r.state = BroadcastRecord.IDLE;
return;
}









//Synthetic comment -- diff --git a/services/java/com/android/server/am/BroadcastRecord.java b/services/java/com/android/server/am/BroadcastRecord.java
//Synthetic comment -- index c95053e..79d3620 100644

//Synthetic comment -- @@ -73,6 +73,30 @@
ComponentName curComponent; // the receiver class that is currently running.
ActivityInfo curReceiver;   // info about the receiver that is currently running.

void dump(PrintWriter pw, String prefix) {
final long now = SystemClock.uptimeMillis();








