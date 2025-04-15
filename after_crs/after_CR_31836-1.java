/*Tune sleep period in bad finalizer test

Change-Id:Iad79a12e86728ebd79488f1421a25052fed9c8d5Signed-off-by: Chris Dearman <chris@mips.com>*/




//Synthetic comment -- diff --git a/tests/030-bad-finalizer/src/Main.java b/tests/030-bad-finalizer/src/Main.java
//Synthetic comment -- index c063476..bad593f 100644

//Synthetic comment -- @@ -14,7 +14,9 @@
System.gc();

for (int i = 0; i < 8; i++) {
            // The finalizer timeout is 10s. Sleep midway between 3.333s and 5s
            // for the best chance of getting exactly 2 GC messages out
            BadFinalizer.snooze(4166);
System.out.println("Requesting another GC.");
System.gc();
}







