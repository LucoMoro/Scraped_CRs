/*Performance improvement in DiskLruCache.readJournalLine().

Speed up DiskLruCache.readJournalLine() by avoiding the
expensive String.split() and explicitly parsing the line.
Some time is saved by avoiding unnecessary checks but most
savings seem to come from reduced object allocations.

On a test journal with 7347 entries (1099 CLEAN) this saves
about 45-50% from ~250ms. On a test journal with 272 entries
(86 CLEAN) this saves about 35-40% from ~10ms. Measured
loadJournal on GN in a tight loop (file contents cached).

Change-Id:I1d6c6b13d54d8fcba3081f2bb9df701b58f5e143*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 8338983..ddbb1ae 100644

//Synthetic comment -- @@ -256,15 +256,22 @@
}

private void readJournalLine(String line) throws IOException {
        String[] parts = line.split(" ");
        if (parts.length < 2) {
throw new IOException("unexpected journal line: " + line);
}

        String key = parts[1];
        if (parts[0].equals(REMOVE) && parts.length == 2) {
            lruEntries.remove(key);
            return;
}

Entry entry = lruEntries.get(key);
//Synthetic comment -- @@ -273,19 +280,39 @@
lruEntries.put(key, entry);
}

        if (parts[0].equals(CLEAN) && parts.length == 2 + valueCount) {
entry.readable = true;
entry.currentEditor = null;
            entry.setLengths(Arrays.copyOfRange(parts, 2, parts.length));
        } else if (parts[0].equals(DIRTY) && parts.length == 2) {
entry.currentEditor = new Editor(entry);
        } else if (parts[0].equals(READ) && parts.length == 2) {
// this work was already done by calling lruEntries.get()
} else {
throw new IOException("unexpected journal line: " + line);
}
}

/**
* Computes the initial size and collects garbage as a part of opening the
* cache. Dirty entries are assumed to be inconsistent and will be deleted.







