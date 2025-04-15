/*Some changes added to compile and run with Java 6 and Java 7.

  - correction of errors with Javac 6:
    - fields accessibility of generic class.

Change-Id:Ice8a707450cd07f1c562c826af644153a30e8cfe*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/util/LinkedNode.java b/src/com/android/gallery3d/util/LinkedNode.java
//Synthetic comment -- index 4cfc3cd..1aeec03 100644

//Synthetic comment -- @@ -56,13 +56,12 @@
}

public T nextOf(T node) {
            return (T) (node.mNext == mHead ? null : node.mNext);
}

public T previousOf(T node) {
            return (T) (node.mPrev == mHead ? null : node.mPrev);
}

}

public static <T extends LinkedNode> List<T> newList() {







