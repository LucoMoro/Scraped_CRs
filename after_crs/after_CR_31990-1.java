/*getAvailableBlocks() may return 0

Signed-off-by: Chris Dearman <chris@mips.com>*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/StatFsTest.java b/tests/tests/os/src/android/os/cts/StatFsTest.java
//Synthetic comment -- index 5f0d781..419abea 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
assertTrue(blockSize > 0);
assertTrue(totalBlocks > 0);
assertTrue(freeBlocks >= availableBlocks);
        assertTrue(availableBlocks >= 0);

path = Environment.getRootDirectory();
stat.restat(path.getPath());







