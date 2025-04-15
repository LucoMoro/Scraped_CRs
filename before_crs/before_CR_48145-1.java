/*Fail if the new index is out of range.

We were silently truncating, which made an obvious problem
into a non-obvious one.

Bug:http://code.google.com/p/android/issues/detail?id=40409Change-Id:I15576357c72ac0e98cf85c0a1d289fd5009468f9*/
//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index fc4d145..b7677cf 100644

//Synthetic comment -- @@ -406,6 +406,9 @@
}

@Override void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
indexMap.typeIds[oldIndex] = (short) newIndex;
}

//Synthetic comment -- @@ -446,6 +449,9 @@
}

@Override void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
indexMap.protoIds[oldIndex] = (short) newIndex;
}

//Synthetic comment -- @@ -466,6 +472,9 @@
}

@Override void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
indexMap.fieldIds[oldIndex] = (short) newIndex;
}

//Synthetic comment -- @@ -486,6 +495,9 @@
}

@Override void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
indexMap.methodIds[oldIndex] = (short) newIndex;
}








