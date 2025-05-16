//<Beginning of snippet n. 0>
boolean changed = false;
List<Integer> keysToRemove = new ArrayList<>();

for (int i = 0; i < mServiceProcessesByName.size(); i++) {
    HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
    Iterator<ProcessItem> pit = procs.values().iterator();
    
    while (pit.hasNext()) {
        ProcessItem pi = pit.next();
        if (pi.mPackageInfo != null) {
            if (pi.mPid != 0) {
                mServiceProcessesByPid.remove(pi.mPid);
            }
            // Implementation of conditions that determine if ProcessItem should be removed
            // Example: if (someCondition) { pit.remove(); }
        } else {
            pit.remove(); // Remove if mPackageInfo is null to prevent NPE
        }
    }

    if (procs.size() == 0) {
        keysToRemove.add(mServiceProcessesByName.keyAt(i));
    }
}

for (Integer key : keysToRemove) {
    mServiceProcessesByName.remove(key);
}

if (changed) {
    // First determine an order for the services.
//<End of snippet n. 0>