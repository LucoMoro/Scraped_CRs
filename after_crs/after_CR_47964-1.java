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
        int firstSpace = line.indexOf(' ');
        if (firstSpace < 0) {
throw new IOException("unexpected journal line: " + line);
}

        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(' ', keyBegin);
        final String key;
        if (secondSpace < 0) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                lruEntries.remove(key);
                return;
            }
        } else {
            key = line.substring(keyBegin, secondSpace);
}

Entry entry = lruEntries.get(key);
//Synthetic comment -- @@ -273,19 +280,39 @@
lruEntries.put(key, entry);
}

        String[] parts;
        if (firstSpace == CLEAN.length() && line.startsWith(CLEAN) && secondSpace >= 0
                && (parts = fastSplitTail(line, secondSpace + 1, ' ', valueCount)) != null) {
entry.readable = true;
entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (firstSpace == DIRTY.length() && line.startsWith(DIRTY) && secondSpace < 0) {
entry.currentEditor = new Editor(entry);
        } else if (firstSpace == READ.length() && line.startsWith(READ) && secondSpace < 0) {
// this work was already done by calling lruEntries.get()
} else {
throw new IOException("unexpected journal line: " + line);
}
}

    private static String[] fastSplitTail(String s, int start, char delim, int parts) {
        String[] result = new String[parts];
        for (int i = 0; i != parts - 1; ++i) {
            int spacePos = s.indexOf(delim, start);
            if (spacePos < 0) {
                return null;
            }
            result[i] = s.substring(start, spacePos);
            start = spacePos + 1;
        }
        int spacePos = s.indexOf(delim, start);
        if (spacePos >= 0) {
            return null;
        }
        result[parts - 1] = s.substring(start);
        return result;
    }

/**
* Computes the initial size and collects garbage as a part of opening the
* cache. Dirty entries are assumed to be inconsistent and will be deleted.







