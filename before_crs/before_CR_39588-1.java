/*Add a test for Method.getParameterAnnotations.

Note that this wouldn't tickle the bug because dx always points
to an empty list rather than using a 0 offset.

Bug:http://code.google.com/p/android/issues/detail?id=35304Change-Id:Ia60be0d2f7f799425dd5918f475bf538c42dc89d*/
//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/annotation/tests/java/lang/annotation/AnnotationTest.java b/luni/src/test/java/org/apache/harmony/annotation/tests/java/lang/annotation/AnnotationTest.java
//Synthetic comment -- index 33ce8fb..8395c00 100644

//Synthetic comment -- @@ -126,6 +126,17 @@
m2.getDeclaredAnnotations()[0].hashCode());

}
}

class AnnotatedClass2 {







