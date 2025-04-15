/*Added validation to ActivityManagerService to detect duplicate
registration requests of broadcast receivers. Currently, malicious
applications can post spam registration requests and can cause a
framework reboot caused by OutOfMemory exception.

Change-Id:Ia221065dad3639e4b295891bc6e9b63a68a1d7a9*/
//Synthetic comment -- diff --git a/core/java/android/content/IntentFilter.java b/core/java/android/content/IntentFilter.java
//Synthetic comment -- index 3b0d846..1464ce7 100644

//Synthetic comment -- @@ -240,6 +240,30 @@
private ArrayList<String> mDataTypes = null;
private boolean mHasPartialTypes = false;

// These functions are the start of more optimized code for managing
// the string sets...  not yet implemented.

//Synthetic comment -- @@ -646,6 +670,25 @@
mPort = src.readInt();
}

void writeToParcel(Parcel dest) {
dest.writeString(mOrigHost);
dest.writeString(mHost);








//Synthetic comment -- diff --git a/core/java/android/os/PatternMatcher.java b/core/java/android/os/PatternMatcher.java
//Synthetic comment -- index 56dc837..2924481 100644

//Synthetic comment -- @@ -53,6 +53,23 @@
mType = type;
}

public final String getPath() {
return mPattern;
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..3a5ce67 100644

//Synthetic comment -- @@ -12725,27 +12725,39 @@
mRegisteredReceivers.put(receiver.asBinder(), rl);
}
BroadcastFilter bf = new BroadcastFilter(filter, rl, callerPackage, permission);
            rl.add(bf);
            if (!bf.debugCheck()) {
                Slog.w(TAG, "==> For Dynamic broadast");
            }
            mReceiverResolver.addFilter(bf);

            // Enqueue broadcasts for all existing stickies that match
            // this filter.
            if (allSticky != null) {
                ArrayList receivers = new ArrayList();
                receivers.add(bf);

                int N = allSticky.size();
                for (int i=0; i<N; i++) {
                    Intent intent = (Intent)allSticky.get(i);
                    BroadcastQueue queue = broadcastQueueForIntent(intent);
                    BroadcastRecord r = new BroadcastRecord(queue, intent, null,
                            null, -1, -1, null, receivers, null, 0, null, null,
                            false, true, true);
                    queue.enqueueParallelBroadcastLocked(r);
                    queue.scheduleBroadcastsLocked();
}
}









//Synthetic comment -- diff --git a/services/java/com/android/server/am/BroadcastFilter.java b/services/java/com/android/server/am/BroadcastFilter.java
//Synthetic comment -- index b49bc22..318768e 100644

//Synthetic comment -- @@ -49,6 +49,25 @@
super.dump(pr, prefix);
dumpBroadcastFilterState(pw, prefix);
}

void dumpBroadcastFilterState(PrintWriter pw, String prefix) {
if (requiredPermission != null) {







