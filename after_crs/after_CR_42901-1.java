/*Keydispatching timeout while finish Activity

If there is input to be handled during finish activity we can get a
keydispatching timeout ANR. The reason is that finish activity is some
times not possible, and the activity is instead put on a finish queue.
The activity will then be finished sometime in the future. When we add
the activity to the finish queue, key dispatching is paused, and there
is an ANR timer waiting for it to be resumed again. Since it can take a
long time before the activity is actually finished, we need to resume
the key dispatching to avoid the ANR.

Change-Id:Icea4ab3b5ad05c8bfbadf8f5cece1a59ec621469*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index b9e63b7..3a2e6d6 100755

//Synthetic comment -- @@ -3800,6 +3800,7 @@
// activity into the stopped state and then finish it.
if (localLOGV) Slog.v(TAG, "Enqueueing pending finish: " + r);
mFinishingActivities.add(r);
            r.resumeKeyDispatchingLocked();
resumeTopActivityLocked(null);
}
return r;







