/*Make ZipFileTest#testHugeZipFile faster, and reduce ZipFile memory usage.

This test was timing out because of unbuffered I/O.

ZipFile was creating an unnecessary byte[] when writing out the central
directory. In the case of these tests, that was an extra 3MiB of heap!

Bug: 6481730
Change-Id:I75acac35c556ad5fdc8b06bc616c11244d6b6c55*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipOutputStream.java b/luni/src/main/java/java/util/zip/ZipOutputStream.java
//Synthetic comment -- index 9a56fa2..47db8f6 100644

//Synthetic comment -- @@ -248,8 +248,8 @@
} else {
writeShort(cDir, 0);
}
        // Write the central dir
        out.write(cDir.toByteArray());
cDir = null;
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java b/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java
//Synthetic comment -- index b7a6050..7e8286e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package libcore.java.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//Synthetic comment -- @@ -114,9 +115,9 @@
File result = File.createTempFile("ZipFileTest", "zip");
result.deleteOnExit();

          ZipOutputStream out = new ZipOutputStream(new FileOutputStream(result));
for (int i = 0; i < count; ++i) {
              ZipEntry entry = new ZipEntry(Integer.toString(i));
out.putNextEntry(entry);
out.closeEntry();
}







