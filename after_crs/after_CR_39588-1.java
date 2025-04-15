/*Add a test for Method.getParameterAnnotations.

Note that this wouldn't tickle the bug because dx always points
to an empty list rather than using a 0 offset.

Bug:http://code.google.com/p/android/issues/detail?id=35304Change-Id:Ia60be0d2f7f799425dd5918f475bf538c42dc89d*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/annotation/tests/java/lang/annotation/AnnotationTest.java b/luni/src/test/java/org/apache/harmony/annotation/tests/java/lang/annotation/AnnotationTest.java
//Synthetic comment -- index 33ce8fb..8395c00 100644

//Synthetic comment -- @@ -126,6 +126,17 @@
m2.getDeclaredAnnotations()[0].hashCode());

}

    public static void test35304() throws Exception {
        Class c = AnnotationTest.class;
        Class[] parameterTypes = new Class[] { String.class, String.class };
        Annotation[][] annotations = c.getDeclaredMethod("test35304_method", parameterTypes).getParameterAnnotations();
        assertEquals(2, annotations.length); // Two parameters.
        assertEquals(0, annotations[0].length); // No annotations on the first.
        assertEquals(1, annotations[1].length); // One annotation on the second.
    }

    private static String test35304_method(String s1, @Deprecated String s2) { return null; }
}

class AnnotatedClass2 {







