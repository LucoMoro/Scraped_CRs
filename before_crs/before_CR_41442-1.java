/*Add a few more calls to BlockGuardOs

While replacing frameworks/base calls with direct calls to
Libcore.os.stat, I noticed that it started failing tests that check that
the StrictMode violations are triggered.

Adding the missing file-based calls to BlockGuardOs restores the tests
to working and will help developers catch StrictMode violations in the
future.

Change-Id:Iefaac96591481f15ce9eaa9eb1ef27edc732d8fe*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/BlockGuardOs.java b/luni/src/main/java/libcore/io/BlockGuardOs.java
//Synthetic comment -- index 61c9765..ece29d8 100644

//Synthetic comment -- @@ -195,4 +195,88 @@
BlockGuard.getThreadPolicy().onWriteToDisk();
return os.writev(fd, buffers, offsets, byteCounts);
}
}







