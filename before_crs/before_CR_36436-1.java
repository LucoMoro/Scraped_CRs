/*Find constructors in source file from class files

This changeset fixes the API detector such that references to a
constructor (e.g. where the bytecode name of the method being called
is "<init>") are properly located in the source code. This is done by
altering the search pattern to look for the owner field instead.

This changeset also pulls out the generic "find location for a class
declaration" from the recent HandlerDetector and into a generic
utility position in the ClassContext, and makes the case handling
anonymous inner classes generic rather than being hardcoded for
subclasses of Handler.

Change-Id:I25f60fda77924edc02bb3029b301e484b0b24931*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index f4f4efa..44d5392 100644

//Synthetic comment -- @@ -344,7 +344,7 @@
* @param node the instruction node to get a line number for
* @return the closest line number, or -1 if not known
*/
    public static int findLineNumber(AbstractInsnNode node) {
AbstractInsnNode curr = node;

// First search backwards
//Synthetic comment -- @@ -373,7 +373,7 @@
* @param node the method node to get a line number for
* @return the closest line number, or -1 if not known
*/
    public static int findLineNumber(MethodNode node) {
if (node.instructions != null && node.instructions.size() > 0) {
return findLineNumber(node.instructions.get(0));
}
//Synthetic comment -- @@ -382,6 +382,79 @@
}

/**
* Computes a user-readable type signature from the given class owner, name
* and description. For example, for owner="foo/bar/Foo$Baz", name="foo",
* description="(I)V", it returns "void foo.bar.Foo.Bar#foo(int)".
//Synthetic comment -- @@ -443,7 +516,8 @@
* @param fqcn the fully qualified class name
* @return the internal class name
*/
    public static String getInternalName(String fqcn) {
String[] parts = fqcn.split("\\."); //$NON-NLS-1$
StringBuilder sb = new StringBuilder();
String prev = null;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 43ea4ef..a19b4c4 100644

//Synthetic comment -- @@ -295,7 +295,7 @@
"Class requires API level %1$d (current min is %2$d): %3$s",
api, minSdk, fqcn);
AbstractInsnNode first = nodes.size() > 0 ? nodes.get(0) : null;
                        report(context, message, first, method, null, null);
}
}
}
//Synthetic comment -- @@ -458,6 +458,15 @@
private void report(final ClassContext context, String message, AbstractInsnNode node,
MethodNode method, String patternStart, String patternEnd) {
int lineNumber = node != null ? ClassContext.findLineNumber(node) : -1;
Location location = context.getLocationForLine(lineNumber, patternStart, patternEnd);
context.report(UNSUPPORTED, method, location, message, null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 3c188c5..03a611f 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
//Synthetic comment -- @@ -33,7 +32,6 @@
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.EnumSet;
//Synthetic comment -- @@ -97,26 +95,7 @@
return;
}

                // Attempt to find a proper location for this class. This is tricky
                // since classes do not have line number entries in the class file; we need
                // to find a method, look up the corresponding line number then search
                // around it for a suitable tag, such as the class name.
                String pattern;
                if (isAnonymousClass(classNode.name)) {
                    pattern = "Handler"; //$NON-NLS-1$
                } else {
                    pattern = classNode.name.substring(classNode.name.lastIndexOf('$') + 1);
                }

                int firstLineNo = -1;
                if (classNode.methods != null && !classNode.methods.isEmpty()) {
                    MethodNode firstMethod = getFirstRealMethod(classNode);
                    if (firstMethod != null) {
                        firstLineNo = ClassContext.findLineNumber(firstMethod);
                    }
                }

                Location location = context.getLocationForLine(firstLineNo, pattern, null);
context.report(ISSUE, location, String.format(
"This Handler class should be static or leaks might occur (%1$s)",
ClassContext.createSignature(classNode.name, null, null)),
//Synthetic comment -- @@ -127,28 +106,6 @@
}
}

    @Nullable
    private MethodNode getFirstRealMethod(@NonNull ClassNode classNode) {
        // Return the first method in the class for line number purposes. Skip <init>,
        // since it's typically not located near the real source of the method.
        if (classNode.methods != null) {
            @SuppressWarnings("rawtypes") // ASM API
            List methods = classNode.methods;
            for (Object m : methods) {
                MethodNode method = (MethodNode) m;
                if (method.name.charAt(0) != '<') {
                    return method;
                }
            }

            if (classNode.methods.size() > 0) {
                return (MethodNode) classNode.methods.get(0);
            }
        }

        return null;
    }

private boolean isStaticInnerClass(@NonNull ClassNode classNode) {
@SuppressWarnings("rawtypes") // ASM API
List fieldList = classNode.fields;
//Synthetic comment -- @@ -161,14 +118,4 @@

return true;
}

    private boolean isAnonymousClass(@NonNull String fqcn) {
        int lastIndex = fqcn.lastIndexOf('$');
        if (lastIndex != -1 && lastIndex < fqcn.length() - 1) {
            if (Character.isDigit(fqcn.charAt(lastIndex + 1))) {
                return true;
            }
        }
        return false;
    }
}







