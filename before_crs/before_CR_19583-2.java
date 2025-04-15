/*Fix NPE due to missing ProcessItem.mPackageInfo

The reason was that a item was removed from the SparseArray while
iterating it causing one ProcessItem to be skipped in the loop which
makes sure that mPackageInfo is not null. This happens when all
processes for one uid is stopped and a new process is created.
This problem was found by running monkey.

Change-Id:I5e9a76e8007819d5e6d9ba15af0c2362da193526*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/RunningState.java b/src/com/android/settings/applications/RunningState.java
//Synthetic comment -- index dbe4a64..18db521 100644

//Synthetic comment -- @@ -756,6 +756,7 @@
}

// Look for services and their primary processes that no longer exist...
for (int i=0; i<mServiceProcessesByName.size(); i++) {
HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
Iterator<ProcessItem> pit = procs.values().iterator();
//Synthetic comment -- @@ -772,7 +773,7 @@
changed = true;
pit.remove();
if (procs.size() == 0) {
                        mServiceProcessesByName.remove(mServiceProcessesByName.keyAt(i));
}
if (pi.mPid != 0) {
mServiceProcessesByPid.remove(pi.mPid);
//Synthetic comment -- @@ -789,6 +790,10 @@
}
}
}

if (changed) {
// First determine an order for the services.







