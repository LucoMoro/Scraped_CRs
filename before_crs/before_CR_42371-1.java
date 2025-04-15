/*Suggest using SparseIntArray instead of SparseArray<Integer>

Change-Id:I95bf3bc87e4378af6ef866df0a1ffb69f8fd7a41*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index fe3b713..34a697c 100644

//Synthetic comment -- @@ -129,6 +129,7 @@
private static final String DOUBLE = "Double";                          //$NON-NLS-1$
private static final String FLOAT = "Float";                            //$NON-NLS-1$
private static final String HASH_MAP = "HashMap";                       //$NON-NLS-1$
private static final String CANVAS = "Canvas";                          //$NON-NLS-1$
private static final String ON_DRAW = "onDraw";                         //$NON-NLS-1$
private static final String ON_LAYOUT = "onLayout";                     //$NON-NLS-1$
//Synthetic comment -- @@ -199,6 +200,8 @@
// e.g. via Guava? This is a bit trickier since we need to infer the type
// arguments from the calling context.
if (typeName.equals(HASH_MAP)) {
checkSparseArray(node, reference);
}
}
//Synthetic comment -- @@ -481,7 +484,7 @@
* to a HashMap constructor call that is eligible for replacement by a
* SparseArray call instead
*/
        private void checkSparseArray(ConstructorInvocation node, TypeReference reference) {
// reference.hasTypeArguments returns false where it should not
StrictListAccessor<TypeReference, TypeReference> types = reference.getTypeArguments();
if (types != null && types.size() == 2) {
//Synthetic comment -- @@ -510,6 +513,28 @@
}
}
}
}

/** Visitor which records variable names assigned into */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java
//Synthetic comment -- index 1d2974c..9705dff 100644

//Synthetic comment -- @@ -63,6 +63,12 @@
"src/test/pkg/JavaPerformanceTest.java:74: Warning: Use new SparseIntArray(...) instead for better performance [UseSparseArrays]\n" +
"        Map<Integer, Integer> myIntMap = new java.util.HashMap<Integer, Integer>();\n" +
"                                         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/test/pkg/JavaPerformanceTest.java:33: Warning: Use Integer.valueOf(5) instead [UseValueOf]\n" +
"        Integer i = new Integer(5);\n" +
"                    ~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -84,8 +90,7 @@
"src/test/pkg/JavaPerformanceTest.java:150: Warning: Use Double.valueOf(1.0) instead [UseValueOf]\n" +
"        Double d1 = new Double(1.0);\n" +
"                    ~~~~~~~~~~~~~~~\n" +
            "0 errors, 19 warnings\n" +
            "",

lintProject("src/test/pkg/JavaPerformanceTest.java.txt=>" +
"src/test/pkg/JavaPerformanceTest.java"));








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/JavaPerformanceTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/JavaPerformanceTest.java.txt
//Synthetic comment -- index 55834ec..a8caccb 100644

//Synthetic comment -- @@ -185,4 +185,11 @@
if ((shader == null) || (lastWidth != getWidth()) || (lastHeight != getHeight())) {
}
}
}







