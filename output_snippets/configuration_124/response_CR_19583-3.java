//<Beginning of snippet n. 0>
for (int i = 0; i < mServiceProcessesByName.size(); i++) {
    HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
    if (procs != null && !procs.isEmpty()) {
        List<String> keysToRemove = new ArrayList<>();
        List<ProcessItem> itemsToRemove = new ArrayList<>();
        Iterator<ProcessItem> pit = procs.values().iterator();
        while (pit.hasNext()) {
            ProcessItem pi = pit.next();
            if (pi != null) {
                if (pi.mPid != 0) {
                    mServiceProcessesByPid.remove(pi.mPid);
                }
                itemsToRemove.add(pi);
            }
        }
        for (ProcessItem pi : itemsToRemove) {
            pit.remove();
        }
        if (procs.isEmpty()) {
            keysToRemove.add(mServiceProcessesByName.keyAt(i));
        }
        for (String key : keysToRemove) {
            mServiceProcessesByName.remove(key);
        }
    }
}
if (changed) {
// First determine an order for the services.
//<End of snippet n. 0>