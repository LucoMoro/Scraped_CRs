/*Track redundant ops when opening the cache.

Seehttps://github.com/JakeWharton/DiskLruCache/issues/28Change-Id:I4815aed3b40f0d2599ff12a42853a4947feffbca*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index fd1e338..b9cc0a1 100644

//Synthetic comment -- @@ -243,13 +243,16 @@
+ magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
}

            int lineCount = 0;
while (true) {
try {
readJournalLine(reader.readLine());
                    lineCount++;
} catch (EOFException endOfJournal) {
break;
}
}
            redundantOpCount = lineCount - lruEntries.size();
} finally {
IoUtils.closeQuietly(reader);
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/DiskLruCacheTest.java b/luni/src/test/java/libcore/io/DiskLruCacheTest.java
//Synthetic comment -- index 2796b65..7a5bfa5 100644

//Synthetic comment -- @@ -579,6 +579,44 @@
assertValue("B", "b", "b");
}

    /** @see <a href="https://github.com/JakeWharton/DiskLruCache/issues/28">Issue #28</a> */
    public void testRebuildJournalOnRepeatedReadsWithOpenAndClose() throws Exception {
        set("a", "a", "a");
        set("b", "b", "b");
        long lastJournalLength = 0;
        while (true) {
            long journalLength = journalFile.length();
            assertValue("a", "a", "a");
            assertValue("b", "b", "b");
            cache.close();
            cache = DiskLruCache.open(cacheDir, appVersion, 2, Integer.MAX_VALUE);
            if (journalLength < lastJournalLength) {
                System.out.printf("Journal compacted from %s bytes to %s bytes\n",
                        lastJournalLength, journalLength);
                break; // test passed!
            }
            lastJournalLength = journalLength;
        }
    }

    /** @see <a href="https://github.com/JakeWharton/DiskLruCache/issues/28">Issue #28</a> */
    public void testRebuildJournalOnRepeatedEditsWithOpenAndClose() throws Exception {
        long lastJournalLength = 0;
        while (true) {
            long journalLength = journalFile.length();
            set("a", "a", "a");
            set("b", "b", "b");
            cache.close();
            cache = DiskLruCache.open(cacheDir, appVersion, 2, Integer.MAX_VALUE);
            if (journalLength < lastJournalLength) {
                System.out.printf("Journal compacted from %s bytes to %s bytes\n",
                        lastJournalLength, journalLength);
                break;
            }
            lastJournalLength = journalLength;
        }
    }

public void testOpenCreatesDirectoryIfNecessary() throws Exception {
cache.close();
File dir = new File(javaTmpDir, "testOpenCreatesDirectoryIfNecessary");







