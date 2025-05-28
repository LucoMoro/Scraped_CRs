//<Beginning of snippet n. 0>
for (int i = 0; i < mServiceProcessesByName.size(); i++) {
    HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
    if (procs.isEmpty()) {
        mServiceProcessesByName.remove(mServiceProcessesByName.keyAt(i));
        i--; // Adjust index after removal
        continue;
    }
    Iterator<ProcessItem> pit = procs.values().iterator();
    while (pit.hasNext()) {
        ProcessItem pi = pit.next();
        if (pi == null || pi.mPackageInfo == null || pi.mPid == 0) {
            if (pi != null && pi.mPid != 0) {
                mServiceProcessesByPid.remove(pi.mPid);
            }
            pit.remove();
        }
    }
    if (procs.isEmpty()) {
        mServiceProcessesByName.remove(mServiceProcessesByName.keyAt(i));
        i--; // Adjust index after removal
    }
}
// Additional logic if changed is true can be handled here
//<End of snippet n. 0>