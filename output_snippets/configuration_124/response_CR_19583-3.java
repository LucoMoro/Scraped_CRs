//<Beginning of snippet n. 0>
}

// Look for services and their primary processes that no longer exist...
List<String> keysToRemove = new ArrayList<>();
for (int i = 0; i < mServiceProcessesByName.size(); i++) {
    HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
    Iterator<ProcessItem> pit = procs.values().iterator();
    boolean hasValidProcess = false;

    while (pit.hasNext()) {
        ProcessItem pi = pit.next();
        if (pi != null && pi.mPackageInfo != null) {
            hasValidProcess = true;
            if (pi.mPid != 0) {
                mServiceProcessesByPid.remove(pi.mPid);
            }
        } else {
            pit.remove();
        }
    }

    if (procs.size() == 0) {
        keysToRemove.add(mServiceProcessesByName.keyAt(i));
    } else if (!hasValidProcess) {
        keysToRemove.add(mServiceProcessesByName.keyAt(i));
    }
}

for (String key : keysToRemove) {
    mServiceProcessesByName.remove(key);
}

if (changed) {
    // First determine an order for the services.
//<End of snippet n. 0>