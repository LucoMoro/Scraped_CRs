/*Tolerate cache files being removed from underneath DiskLruCache

Bug: 6777079
Change-Id:I2f950ab6d847dd63061aeb449fc5d46ab9e6c50a*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 4699766..e3bc0f5 100644

//Synthetic comment -- @@ -460,7 +460,13 @@
for (int i = 0; i < valueCount; i++) {
if (!entry.getDirtyFile(i).exists()) {
editor.abort();
                    // We report if this happens because usually it
                    // indicates the caller is using the API
                    // incorrectly. We log instead of throwing an
                    // exception because this can also happen if the
                    // cache directory is removed out from beneath us.
                    System.logW("Newly created entry didn't create value for index " + i);
                    return;
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpResponseCache.java b/luni/src/main/java/libcore/net/http/HttpResponseCache.java
//Synthetic comment -- index 910461e7..2130fd1 100644

//Synthetic comment -- @@ -426,7 +426,7 @@
}

public void writeTo(DiskLruCache.Editor editor) throws IOException {
            OutputStream out = editor.newOutputStream(ENTRY_METADATA);
Writer writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));

writer.write(uri + '\n');








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/DiskLruCacheTest.java b/luni/src/test/java/libcore/io/DiskLruCacheTest.java
//Synthetic comment -- index 03a6932..0613d6b 100644

//Synthetic comment -- @@ -331,11 +331,7 @@
public void testCreateNewEntryWithTooFewValuesFails() throws Exception {
DiskLruCache.Editor creator = cache.edit("k1");
creator.set(1, "A");
        creator.commit();

assertFalse(getCleanFile("k1", 0).exists());
assertFalse(getCleanFile("k1", 1).exists());
//Synthetic comment -- @@ -805,4 +801,4 @@
assertTrue(getCleanFile(key, 1).exists());
snapshot.close();
}
\ No newline at end of file
}







