/*Tolerate cache files being removed from underneath DiskLruCache

Bug: 6777079
Change-Id:I2f950ab6d847dd63061aeb449fc5d46ab9e6c50a*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index 4699766..b7d246d 100644

//Synthetic comment -- @@ -458,9 +458,14 @@
// if this edit is creating the entry for the first time, every index must have a value
if (success && !entry.readable) {
for (int i = 0; i < valueCount; i++) {
if (!entry.getDirtyFile(i).exists()) {
editor.abort();
                    throw new IllegalStateException("edit didn't create file " + i);
}
}
}
//Synthetic comment -- @@ -659,10 +664,12 @@
*/
public final class Editor {
private final Entry entry;
private boolean hasErrors;

private Editor(Entry entry) {
this.entry = entry;
}

/**
//Synthetic comment -- @@ -702,6 +709,9 @@
if (entry.currentEditor != this) {
throw new IllegalStateException();
}
return new FaultHidingOutputStream(new FileOutputStream(entry.getDirtyFile(index)));
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpResponseCache.java b/luni/src/main/java/libcore/net/http/HttpResponseCache.java
//Synthetic comment -- index 910461e7..2130fd1 100644

//Synthetic comment -- @@ -426,7 +426,7 @@
}

public void writeTo(DiskLruCache.Editor editor) throws IOException {
            OutputStream out = editor.newOutputStream(0);
Writer writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));

writer.write(uri + '\n');








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/DiskLruCacheTest.java b/luni/src/test/java/libcore/io/DiskLruCacheTest.java
//Synthetic comment -- index 03a6932..2796b65 100644

//Synthetic comment -- @@ -349,6 +349,28 @@
creator2.commit();
}

public void testRevertWithTooFewValues() throws Exception {
DiskLruCache.Editor creator = cache.edit("k1");
creator.set(1, "A");
//Synthetic comment -- @@ -805,4 +827,4 @@
assertTrue(getCleanFile(key, 1).exists());
snapshot.close();
}
}
\ No newline at end of file







