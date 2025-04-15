/*Track redundant ops when opening the cache.

Seehttps://github.com/JakeWharton/DiskLruCache/issues/28Change-Id:I4815aed3b40f0d2599ff12a42853a4947feffbca*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index fd1e338..b9cc0a1 100644

//Synthetic comment -- @@ -243,13 +243,16 @@
+ magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
}

while (true) {
try {
readJournalLine(reader.readLine());
} catch (EOFException endOfJournal) {
break;
}
}
} finally {
IoUtils.closeQuietly(reader);
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/DiskLruCacheTest.java b/luni/src/test/java/libcore/io/DiskLruCacheTest.java
//Synthetic comment -- index 2796b65..7a5bfa5 100644

//Synthetic comment -- @@ -579,6 +579,44 @@
assertValue("B", "b", "b");
}

public void testOpenCreatesDirectoryIfNecessary() throws Exception {
cache.close();
File dir = new File(javaTmpDir, "testOpenCreatesDirectoryIfNecessary");







