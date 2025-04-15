/*Add lint check for cut & paste errors in findViewById calls*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java
//Synthetic comment -- index ae86568..d91f423 100644

//Synthetic comment -- @@ -23,6 +23,8 @@

import java.io.File;

import lombok.ast.Node;

/**
//Synthetic comment -- @@ -105,4 +107,21 @@
}
super.report(issue, location, message, data);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index cd654ae..52c89d4 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 130;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -183,6 +183,7 @@
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);
issues.add(SharedPrefsDetector.ISSUE);
issues.add(NonInternationalizedSmsDetector.ISSUE);
issues.add(PrivateKeyDetector.ISSUE);
issues.add(AnnotationDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/CutPasteDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/CutPasteDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..193ab8f

//Synthetic comment -- @@ -0,0 +1,240 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index d38b05e..1f432f7 100644

//Synthetic comment -- @@ -81,22 +81,6 @@
}

@Nullable
    private static Node findSurroundingMethod(Node scope) {
        while (scope != null) {
            Class<? extends Node> type = scope.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class || type == ConstructorDeclaration.class) {
                return scope;
            }

            scope = scope.getParent();
        }

        return null;
    }

    @Nullable
private static NormalTypeBody findSurroundingTypeBody(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
//Synthetic comment -- @@ -156,7 +140,7 @@
allowCommitBeforeTarget = false;
}

        Node method = findSurroundingMethod(parent);
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index 52ae8ed..e8f8d6e 100644

//Synthetic comment -- @@ -31,11 +31,9 @@
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.IntegralLiteral;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Return;
//Synthetic comment -- @@ -75,21 +73,6 @@
return Collections.singletonList("makeText"); //$NON-NLS-1$
}

    private Node findSurroundingMethod(Node scope) {
        while (scope != null) {
            Class<? extends Node> type = scope.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class || type == ConstructorDeclaration.class) {
                return scope;
            }

            scope = scope.getParent();
        }

        return null;
    }

@Override
public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
@NonNull MethodInvocation node) {
//Synthetic comment -- @@ -118,7 +101,7 @@
}
}

        Node method = findSurroundingMethod(node.getParent());
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CutPasteDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CutPasteDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..df02a41

//Synthetic comment -- @@ -0,0 +1,55 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index dbfc838..7035c21 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class SharedPrefsDetectorTest  extends AbstractCheckTest {
@Override
protected Detector getDetector() {
return new SharedPrefsDetector();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/PasteError.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/PasteError.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..b12b56b

//Synthetic comment -- @@ -0,0 +1,109 @@







