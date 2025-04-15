/*Tolerate cache folder deletion on writes.

If the cache is stored somewhere where the entire folder is subject
to possible deletion this will attempt to restore the folder tree
once on a `FileNotFoundException` during writes.

We choose not to perform the same operation on reads since no useful
state will be gained (i.e., it's a cache miss either way).

A test is included for the current ICS MR API. There is also a second
test (currently commented out) for the forthcoming public editing
API which Jesse Wilson was kind enough to make me privy to.

Change-Id:Ied6a3d8a7cc54eec7f923ef46b4d39a0599e221a*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index ecc1302..1935f6f 100644

//Synthetic comment -- @@ -670,7 +670,15 @@
if (entry.currentEditor != this) {
throw new IllegalStateException();
}
                File outFile = entry.getDirtyFile(index);
                FileOutputStream out;
                try {
                    out = new FileOutputStream(outFile);
                } catch (FileNotFoundException e) {
                    directory.mkdirs();
                    out = new FileOutputStream(outFile);
                }
                return new FaultHidingOutputStream(out);
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/DiskLruCacheTest.java b/luni/src/test/java/libcore/io/DiskLruCacheTest.java
//Synthetic comment -- index 808918d..9a40865 100644

//Synthetic comment -- @@ -602,6 +602,20 @@
assertAbsent("A");
}

    public void testAggressiveClearingHandledOnWrite() throws Exception {
        IoUtils.deleteContents(cacheDir);
        set("A", "a", "a");
        assertValue("A", "a", "a");
    }

    /*public void testAggressiveClearingHandledOnEdit() throws Exception {
        set("A", "a", "a");
        DiskLruCache.Editor a = cache.get("A").edit();
        IoUtils.deleteContents(cacheDir);
        a.set(1, "a2");
        a.commit();
    }*/

private void assertJournalEquals(String... expectedBodyLines) throws Exception {
List<String> expectedLines = new ArrayList<String>();
expectedLines.add(MAGIC);







