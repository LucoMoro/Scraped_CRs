/*39134: forgot to call commit() after editing a SharedPreference

This updates the lint check to handle some additional scenarios around
missing commit() calls for SharedPreference.Edit; in particular when
using edit() on field references and in chained method calls.

Change-Id:I1b249ed81ca9d0b0e1c85466ccfa1ecdd6c53fad*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 5db301b..d38b05e 100644

//Synthetic comment -- @@ -33,13 +33,16 @@

import lombok.ast.AstVisitor;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Return;
import lombok.ast.VariableDefinition;
import lombok.ast.VariableDefinitionEntry;

/**
* Detector looking for SharedPreferences.edit() calls without a corresponding
//Synthetic comment -- @@ -77,7 +80,8 @@
return Collections.singletonList("edit"); //$NON-NLS-1$
}

    private Node findSurroundingMethod(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
// The Lombok AST uses a flat hierarchy of node type implementation classes
//Synthetic comment -- @@ -92,11 +96,29 @@
return null;
}

@Override
public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
@NonNull MethodInvocation node) {
assert node.astName().astValue().equals("edit");
        if (node.astOperand() == null) {
return;
}

//Synthetic comment -- @@ -104,26 +126,42 @@
// to a local variable; this means we won't recognize some other usages
// of the API (e.g. assigning it to a previously declared variable) but
// is needed until we have type attribution in the AST itself.
        if (!(node.getParent() instanceof VariableDefinitionEntry &&
                node.getParent().getParent() instanceof VariableDefinition)) {
            return;
        }
        VariableDefinition definition = (VariableDefinition) node.getParent().getParent();
        String type = definition.astTypeReference().toString();
        if (!type.endsWith("SharedPreferences.Editor")) {                   //$NON-NLS-1$
            if (!type.equals("Editor") ||                                   //$NON-NLS-1$
                    !LintUtils.isImported(context.compilationUnit,
                            "android.content.SharedPreferences.Editor")) {  //$NON-NLS-1$
return;
}
}

        Node method = findSurroundingMethod(node.getParent());
if (method == null) {
return;
}

        CommitFinder finder = new CommitFinder(node);
method.accept(finder);
if (!finder.isCommitCalled()) {
context.report(ISSUE, method, context.getLocation(node),
//Synthetic comment -- @@ -132,6 +170,39 @@
}
}

private class CommitFinder extends ForwardingAstVisitor {
/** Whether we've found one of the commit/cancel methods */
private boolean mFound;
//Synthetic comment -- @@ -139,16 +210,18 @@
private MethodInvocation mTarget;
/** Whether we've seen the target edit node yet */
private boolean mSeenTarget;

        private CommitFinder(MethodInvocation target) {
mTarget = target;
}

@Override
public boolean visitMethodInvocation(MethodInvocation node) {
if (node == mTarget) {
mSeenTarget = true;
            } else if (mSeenTarget || node.astOperand() == mTarget) {
String name = node.astName().astValue();
if ("commit".equals(name) || "apply".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
// TODO: Do more flow analysis to see whether we're really calling commit/apply
//Synthetic comment -- @@ -157,7 +230,7 @@
}
}

            return true;
}

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index 964232c..dbfc838 100644

//Synthetic comment -- @@ -75,4 +75,35 @@
lintProject("src/test/pkg/SharedPrefsTest4.java.txt=>" +
"src/test/pkg/SharedPrefsTest4.java"));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest5.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest5.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..f005162

//Synthetic comment -- @@ -0,0 +1,54 @@







