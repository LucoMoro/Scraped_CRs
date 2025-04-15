/*Make dex merging stateless.

This is required to run dx in Eclipse which doesn't unload the dx
library after each run.

(cherry-pick of 7aa5ce7e990dc3766eba97cd0932b62e4de21503.)

Change-Id:I7a69a3015d448ddd5558c307cd01346156cbc739*/




//Synthetic comment -- diff --git a/dx/src/com/android/dx/command/dexer/Main.java b/dx/src/com/android/dx/command/dexer/Main.java
//Synthetic comment -- index d127550..80ddbd0 100644

//Synthetic comment -- @@ -186,6 +186,9 @@
// Reset the error/warning count to start fresh.
warnings = 0;
errors = 0;
        // empty the list, so that  tools that load dx and keep it around
        // for multiple runs don't reuse older buffers.
        libraryDexBuffers.clear();

args = arguments;
args.makeOptionsObjects();
//Synthetic comment -- @@ -297,6 +300,7 @@
DexBuffer ab = new DexMerger(a, b, CollisionPolicy.FAIL).merge();
outArray = ab.getBytes();
}

return outArray;
}








