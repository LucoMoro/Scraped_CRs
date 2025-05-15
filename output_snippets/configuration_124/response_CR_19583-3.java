//<Beginning of snippet n. 0>
boolean changed = false;

// Look for services and their primary processes that no longer exist...
for (int i = 0; i < mServiceProcessesByName.size(); i++) {
    HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
    List<ProcessItem> toRemove = new ArrayList<>();
    Iterator<ProcessItem> pit = procs.values().iterator();
    
    while (pit.hasNext()) {
        ProcessItem pi = pit.next();
        if (pi == null || pi.mPackageInfo == null) {
            continue;
        }
        
        if (procs.size() == 0) {
            toRemove.add(pi);
        }
        
        if (pi.mPid != 0) {
            mServiceProcessesByPid.remove(pi.mPid);
        }
    }
    
    for (ProcessItem item : toRemove) {
        procs.remove(item);
    }

    if (procs.size() == 0) {
        mServiceProcessesByName.remove(mServiceProcessesByName.keyAt(i));
    }
}

if (changed) {
    // First determine an order for the services.
//<End of snippet n. 0>