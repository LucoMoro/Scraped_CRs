/*Performance improvement in DiskLruCache.readJournalLine().

Speed up DiskLruCache.readJournalLine() by avoiding memory
allocations from String.split(). For non-CLEAN lines, we
avoid using String.split() altogether and find separators
explicitly, for CLEAN lines we defer to String.split() and
we optimize the underlying Splitter.fastSplit() overload to
avoid unnecessary allocations itself.

On a test journal with 7347 entries (1099 CLEAN) this saves
about 45-50% from ~250ms. On a test journal with 272 entries
(86 CLEAN) this saves about 35-40% from ~10ms. Measured
loadJournal on GN in a tight loop (file contents cached).

If used without the other DiskLruCache.readJournalLine()
improvements, the Splitter.fastSplit() optimization alone
would provide about 60% of the savings. It should also
speed up other code outside the DiskLruCache.

Change-Id:I1d6c6b13d54d8fcba3081f2bb9df701b58f5e143*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Splitter.java b/luni/src/main/java/java/util/regex/Splitter.java
//Synthetic comment -- index d30bada..40d6984 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.util.ArrayList;
import java.util.List;
import libcore.util.EmptyArray;

/**
* Used to make {@code String.split} fast (and to help {@code Pattern.split} too).
//Synthetic comment -- @@ -63,16 +64,41 @@
return new String[] { "" };
}

        // Count separators
        int separatorCount = 0;
int begin = 0;
int end;
        while (separatorCount + 1 != limit && (end = input.indexOf(ch, begin)) != -1) {
            ++separatorCount;
begin = end + 1;
}
        int lastPartEnd = input.length();
        if (limit == 0 && begin == lastPartEnd) {
            // Last part is empty for limit == 0, remove all trailing empty matches.
            if (separatorCount == lastPartEnd) {
                // Input contains only separators.
                return new String[0];
            }
            // Find the beginning of trailing separators.
            do {
                --begin;
            } while (input.charAt(begin - 1) == ch);
            // Reduce separatorCount and fix lastPartEnd.
            separatorCount -= input.length() - begin;
            lastPartEnd = begin;
        }

        // Collect the result parts.
        String[] result = new String[separatorCount + 1];
        begin = 0;
        for (int i = 0; i != separatorCount; ++i) {
            end = input.indexOf(ch, begin);
            result[i] = input.substring(begin, end);
            begin = end + 1;
        }
        // Add last part.
        result[separatorCount] = input.substring(begin, lastPartEnd);
        return result;
}

public static String[] split(Pattern pattern, String re, String input, int limit) {
//Synthetic comment -- @@ -89,25 +115,23 @@

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
        } else if (limit != 0) {
list.add("");
        } else {
            // Remove all trailing empty matches in the limit == 0 case.
int i = list.size() - 1;
while (i >= 0 && list.get(i).isEmpty()) {
list.remove(i);








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 8338983..fd1e338 100644

//Synthetic comment -- @@ -256,15 +256,22 @@
}

private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(' ');
        if (firstSpace == -1) {
throw new IOException("unexpected journal line: " + line);
}

        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(' ', keyBegin);
        final String key;
        if (secondSpace == -1) {
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

        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
entry.readable = true;
entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
entry.currentEditor = new Editor(entry);
        } else if (secondSpace == -1 && firstSpace == READ.length() && line.startsWith(READ)) {
// this work was already done by calling lruEntries.get()
} else {
throw new IOException("unexpected journal line: " + line);







