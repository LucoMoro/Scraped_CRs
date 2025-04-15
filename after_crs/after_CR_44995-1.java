/*Allow @SuppressLint on fields for class-based detectors

Change-Id:I254f3fb5a6132d6cbe39bd425ffe6d67ed7b84ed*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java
//Synthetic comment -- index 7543def..9982f18 100644

//Synthetic comment -- @@ -40,12 +40,16 @@
import lombok.ast.AnnotationValue;
import lombok.ast.ArrayInitializer;
import lombok.ast.AstVisitor;
import lombok.ast.Block;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.Modifiers;
import lombok.ast.Node;
import lombok.ast.StrictListAccessor;
import lombok.ast.StringLiteral;
import lombok.ast.TypeBody;
import lombok.ast.VariableDefinition;

/**
//Synthetic comment -- @@ -153,11 +157,27 @@
IssueRegistry registry = mContext.getDriver().getRegistry();
Issue issue = registry.getIssue(id);
if (issue != null && !issue.getScope().contains(Scope.JAVA_FILE)) {
                // Ensure that this isn't a field
                Node parent = node.getParent();
                while (parent != null) {
                    if (parent instanceof MethodDeclaration
                            || parent instanceof ConstructorDeclaration
                            || parent instanceof Block) {
                        break;
                    } else if (parent instanceof TypeBody) { // It's a field
                        return true;
                    }
                    parent = parent.getParent();
                    if (parent == null) {
                        return true;
                    }
                }

// This issue doesn't have AST access: annotations are not
// available for local variables or parameters
mContext.report(ISSUE,mContext.getLocation(node), String.format(
"The @SuppresLint annotation cannot be used on a local" +
                    " variable with the lint check '%1$s': move out to the " +
"surrounding method", id),
null);
return false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java
//Synthetic comment -- index 43e9ea0..f22d662 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
6,
Severity.WARNING,
LocaleDetector.class,
            Scope.CLASS_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/reference/java/text/SimpleDateFormat.html"); //$NON-NLS-1$

private static final String DATE_FORMAT_OWNER = "java/text/SimpleDateFormat"; //$NON-NLS-1$
//Synthetic comment -- @@ -149,6 +149,7 @@
"Locale locale) with for example Locale.US for ASCII dates.", name);
context.report(DATE_FORMAT, method, location, message, null);
}
            return;
} else if (!owner.equals(STRING_OWNER)) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java
//Synthetic comment -- index 4c9d34d..becca80 100644

//Synthetic comment -- @@ -25,20 +25,22 @@
public class AnnotationDetectorTest extends AbstractCheckTest {
public void test() throws Exception {
assertEquals(
            "src/test/pkg/WrongAnnotation.java:9: Error: The @SuppresLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"    public static void foobar(View view, @SuppressLint(\"NewApi\") int foo) { // Invalid: class-file check\n" +
"                                         ~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:10: Error: The @SuppresLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @SuppressLint(\"NewApi\") // Invalid\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:12: Error: The @SuppresLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @SuppressLint({\"SdCardPath\", \"NewApi\"}) // Invalid: class-file based check on local variable\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:14: Error: The @SuppresLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @android.annotation.SuppressLint({\"SdCardPath\", \"NewApi\"}) // Invalid (FQN)\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:28: Error: The @SuppresLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
            "        @SuppressLint(\"NewApi\")\n" +
            "        ~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "5 errors, 0 warnings\n",

lintProject(
"src/test/pkg/WrongAnnotation.java.txt=>src/test/pkg/WrongAnnotation.java"








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt
//Synthetic comment -- index 9256055..6fef833 100644

//Synthetic comment -- @@ -1,6 +1,7 @@
package test.pkg;

import android.annotation.SuppressLint;
import android.view.View;

public class WrongAnnotation {
@Override
//Synthetic comment -- @@ -15,5 +16,16 @@
@SuppressLint("SdCardPath") // Valid: AST-based check
boolean d;
}

    @SuppressLint("NewApi")
    private int field1;

    @SuppressLint("NewApi")
    private int field2 = 5;

    static {
        // Local variable outside method: invalid
        @SuppressLint("NewApi")
        int localvar = 5;
    }
}







