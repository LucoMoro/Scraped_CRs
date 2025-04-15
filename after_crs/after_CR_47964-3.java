/*Performance improvement in DiskLruCache.readJournalLine().

Speed up DiskLruCache.readJournalLine() by avoiding memory
allocations from String.split(). For non-CLEAN lines, we
avoid using String.split() altogether and find separators
explicitly, for CLEAN lines we defer to String.split() and
optimize the most common case when there are only 2 parts
or less.

On a test journal with 7347 entries (1099 CLEAN) this saves
about 45-50% from ~250ms. On a test journal with 272 entries
(86 CLEAN) this saves about 35-40% from ~10ms. Measured
loadJournal on GN in a tight loop (file contents cached).

If used without the other DiskLruCache.readJournalLine()
improvements, the String.split() optimization alone would
provide about half of the savings by improving non-CLEAN
lines' processing. It should also speed up other code
outside the DiskLruCache.

Change-Id:I1d6c6b13d54d8fcba3081f2bb9df701b58f5e143*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Splitter.java b/luni/src/main/java/java/util/regex/Splitter.java
//Synthetic comment -- index d30bada..71b7597 100644

//Synthetic comment -- @@ -63,16 +63,32 @@
return new String[] { "" };
}

        // Optimization: Avoid excessive allocations when there's only one or two parts.
int begin = 0;
int end;
        if (limit == 1 || (end = input.indexOf(ch, begin)) == -1) {
            return new String[] { input };
        }
        String firstPart = input.substring(begin, end);
        begin = end + 1;
        if (limit == 2 || (end = input.indexOf(ch, begin)) == -1) {
            if (limit != 0 || begin != input.length()) {
                return new String[] { firstPart, input.substring(begin) };
            }
            // There was an empty tail for limit == 0. Check if the first part is also empty.
            return firstPart.isEmpty() ? new String[0] : new String[] { firstPart };
        }

        // Collect text preceding each occurrence of the separator, while there's enough space.
        ArrayList<String> list = new ArrayList<String>();
        list.add(firstPart);
        list.add(input.substring(begin, end));
        begin = end + 1;
        while (list.size() + 1 != limit && (end = input.indexOf(ch, begin)) != -1) {
list.add(input.substring(begin, end));
begin = end + 1;
}
        return finishSplit(list, input, begin, limit);
}

public static String[] split(Pattern pattern, String re, String input, int limit) {
//Synthetic comment -- @@ -89,17 +105,16 @@

// Collect text preceding each occurrence of the separator, while there's enough space.
ArrayList<String> list = new ArrayList<String>();
Matcher matcher = new Matcher(pattern, input);
int begin = 0;
        while (list.size() + 1 != limit && matcher.find()) {
list.add(input.substring(begin, matcher.start()));
begin = matcher.end();
}
        return finishSplit(list, input, begin, limit);
}

    private static String[] finishSplit(List<String> list, String input, int begin, int limit) {
// Add trailing text.
if (begin < input.length()) {
list.add(input.substring(begin));








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 8338983..c7a1f6a 100644

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
//Synthetic comment -- @@ -273,13 +280,14 @@
lruEntries.put(key, entry);
}

        if (firstSpace == CLEAN.length() && line.startsWith(CLEAN) && secondSpace >= 0) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
entry.readable = true;
entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (firstSpace == DIRTY.length() && line.startsWith(DIRTY) && secondSpace < 0) {
entry.currentEditor = new Editor(entry);
        } else if (firstSpace == READ.length() && line.startsWith(READ) && secondSpace < 0) {
// this work was already done by calling lruEntries.get()
} else {
throw new IOException("unexpected journal line: " + line);







