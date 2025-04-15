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
//Synthetic comment -- index d30bada..4a32f0f 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.util.ArrayList;
import java.util.List;

/**
* Used to make {@code String.split} fast (and to help {@code Pattern.split} too).
//Synthetic comment -- @@ -63,16 +64,32 @@
return new String[] { "" };
}

        // Collect text preceding each occurrence of the separator, while there's enough space.
        ArrayList<String> list = new ArrayList<String>();
        int maxSize = limit <= 0 ? Integer.MAX_VALUE : limit;
int begin = 0;
int end;
        while ((end = input.indexOf(ch, begin)) != -1 && list.size() + 1 < maxSize) {
list.add(input.substring(begin, end));
begin = end + 1;
}
        return finishSplit(list, input, begin, maxSize, limit);
}

public static String[] split(Pattern pattern, String re, String input, int limit) {
//Synthetic comment -- @@ -89,25 +106,23 @@

// Collect text preceding each occurrence of the separator, while there's enough space.
ArrayList<String> list = new ArrayList<String>();
        int maxSize = limit <= 0 ? Integer.MAX_VALUE : limit;
Matcher matcher = new Matcher(pattern, input);
int begin = 0;
        while (matcher.find() && list.size() + 1 < maxSize) {
list.add(input.substring(begin, matcher.start()));
begin = matcher.end();
}
        return finishSplit(list, input, begin, maxSize, limit);
}

    private static String[] finishSplit(List<String> list, String input, int begin, int maxSize, int limit) {
// Add trailing text.
if (begin < input.length()) {
list.add(input.substring(begin));
        } else if (limit != 0) { // No point adding the empty string if limit == 0, just to remove it below.
list.add("");
        }
        // Remove all trailing empty matches in the limit == 0 case.
        if (limit == 0) {
int i = list.size() - 1;
while (i >= 0 && list.get(i).isEmpty()) {
list.remove(i);








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 8338983..fd1e338 100644

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
//Synthetic comment -- @@ -273,13 +280,14 @@
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







