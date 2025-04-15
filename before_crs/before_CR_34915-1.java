/*28321: Lint didn't warn about "new Long(long)"

Change-Id:I69d053e4242e0af2080fdd9ed233fd10c8963d1b*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index 95fc107..e99005c 100644

//Synthetic comment -- @@ -124,6 +124,9 @@
private static final String BOOL = "boolean";                           //$NON-NLS-1$
private static final String BOOLEAN = "Boolean";                        //$NON-NLS-1$
private static final String LONG = "Long";                              //$NON-NLS-1$
private static final String HASH_MAP = "HashMap";                       //$NON-NLS-1$
private static final String CANVAS = "Canvas";                          //$NON-NLS-1$
private static final String ON_DRAW = "onDraw";                         //$NON-NLS-1$
//Synthetic comment -- @@ -204,11 +207,12 @@
TypeReference reference = node.astTypeReference();
typeName = reference.astParts().last().astIdentifier().astValue();
}
                if ((typeName.equals("Integer")             //$NON-NLS-1$
                        || typeName.equals("Boolean")       //$NON-NLS-1$
                        || typeName.equals("Float")         //$NON-NLS-1$
                        || typeName.equals("Character")     //$NON-NLS-1$
                        || typeName.equals("Double"))       //$NON-NLS-1$
&& node.astTypeReference().astParts().size() == 1
&& node.astArguments().size() == 1) {
String argument = node.astArguments().first().toString();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java
//Synthetic comment -- index cfcda6a..100c3f7 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
"JavaPerformanceTest.java:116: Warning: Avoid object allocations during draw operations: Use Canvas.getClipBounds(Rect) instead of Canvas.getClipBounds() which allocates a temporary Rect\n" +
"JavaPerformanceTest.java:140: Warning: Avoid object allocations during draw/layout operations (preallocate and reuse instead)\n" +
"JavaPerformanceTest.java:145: Warning: Use Integer.valueOf(42) instead\n" +
"JavaPerformanceTest.java:147: Warning: Use Boolean.valueOf(true) instead\n" +
"JavaPerformanceTest.java:148: Warning: Use Character.valueOf('c') instead\n" +
"JavaPerformanceTest.java:149: Warning: Use Float.valueOf(1.0f) instead\n" +







