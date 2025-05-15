
//<Beginning of snippet n. 0>


}

// Look for services and their primary processes that no longer exist...
        ArrayList<Integer> uidToDelete = new ArrayList<Integer>();
for (int i=0; i<mServiceProcessesByName.size(); i++) {
HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
Iterator<ProcessItem> pit = procs.values().iterator();
changed = true;
pit.remove();
if (procs.size() == 0) {
                        uidToDelete.add(mServiceProcessesByName.keyAt(i));
}
if (pi.mPid != 0) {
mServiceProcessesByPid.remove(pi.mPid);
}
}
}
        for (Iterator<Integer> iterator = uidToDelete.iterator(); iterator.hasNext();) {
            int uid = iterator.next();
            mServiceProcessesByName.remove(uid);
        }

if (changed) {
// First determine an order for the services.

//<End of snippet n. 0>








