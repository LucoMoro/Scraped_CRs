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

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj == this) {
            result = true;
        }
        else if (obj instanceof IntentFilter) {

            IntentFilter compareObj = (IntentFilter)obj;

            result = (mPriority == compareObj.mPriority) &&
                     (mHasPartialTypes == compareObj.mHasPartialTypes) &&
                     (mActions != null ? mActions.equals(compareObj.mActions) : compareObj.mActions == null) &&
                     (mCategories != null ? mCategories.equals(compareObj.mCategories) : compareObj.mCategories == null) &&
                     (mDataSchemes != null ? mDataSchemes.equals(compareObj.mDataSchemes) : compareObj.mDataSchemes == null) &&
                     (mDataTypes != null ? mDataTypes.equals(compareObj.mDataTypes) : compareObj.mDataTypes == null) &&
                     (mDataAuthorities != null ? mDataAuthorities.equals(compareObj.mDataAuthorities) : compareObj.mDataAuthorities == null) &&
                     (mDataPaths != null ? mDataPaths.equals(compareObj.mDataPaths) : compareObj.mDataPaths == null);

        }

        return result;
    }

// These functions are the start of more optimized code for managing
// the string sets...  not yet implemented.

//Synthetic comment -- @@ -646,6 +670,25 @@
mPort = src.readInt();
}

        public boolean equals(Object obj) {
            boolean result = false;

            if (obj == this) {
                result = true;
            }
            else if (obj instanceof AuthorityEntry) {

                AuthorityEntry compareObj = (AuthorityEntry)obj;

                result = (mOrigHost != null ? mOrigHost.equals(compareObj.mOrigHost) : compareObj.mOrigHost == null) &&
                         (mHost != null ? mHost.equals(compareObj.mHost) : compareObj.mHost == null) &&
                         (mWild == compareObj.mWild) &&
                         (mPort == compareObj.mPort);
            }

            return result;
        }

void writeToParcel(Parcel dest) {
dest.writeString(mOrigHost);
dest.writeString(mHost);








//Synthetic comment -- diff --git a/core/java/android/os/PatternMatcher.java b/core/java/android/os/PatternMatcher.java
//Synthetic comment -- index 56dc837..2924481 100644

//Synthetic comment -- @@ -53,6 +53,23 @@
mType = type;
}

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj == this) {
            result = true;
        }
        else if (obj instanceof PatternMatcher) {

            PatternMatcher compareObj = (PatternMatcher) obj;

            result = (mType == compareObj.mType) &&
                     (mPattern != null ? mPattern.equals(compareObj.mPattern) : compareObj.mPattern == null);
        }

        return result;
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

            /*
             * Check for duplicate intent filters in the ReceiverList. Malicious
             * applications can spam the framework with an attempt to register
             * a receiver and its corresponding intent filter multiple times
             * and trigger an android framework reboot caused by OutOfMemory.
             *    Hence, to avoid excess unnecessary memory usage, do not add
             * duplicate broadcast filter to the receiver list.
             */
            if (!rl.contains(bf)) {

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
}









//Synthetic comment -- diff --git a/services/java/com/android/server/am/BroadcastFilter.java b/services/java/com/android/server/am/BroadcastFilter.java
//Synthetic comment -- index b49bc22..318768e 100644

//Synthetic comment -- @@ -49,6 +49,25 @@
super.dump(pr, prefix);
dumpBroadcastFilterState(pw, prefix);
}

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj == this) {
            result = true;
        }
        else if (obj instanceof BroadcastFilter) {

            BroadcastFilter compareObj = (BroadcastFilter)obj;

            result = (packageName != null ? packageName.equals(compareObj.packageName) : compareObj.packageName == null) &&
                     (requiredPermission != null ? requiredPermission.equals(compareObj.requiredPermission) : compareObj.requiredPermission == null) &&
                     (receiverList != null ? receiverList.equals(compareObj.receiverList) : compareObj.receiverList == null) &&
                     super.equals(obj);
        }

        return result;
    }

void dumpBroadcastFilterState(PrintWriter pw, String prefix) {
if (requiredPermission != null) {







