/*35588: @SuppressLint does not work on constructors in Java files

The AST-based check for whether an issue is suppressed did not take
constructors into account. This was because the AST node for
constructors (ConstructorDeclaration) does not extend the AST node for
method declarations (MethodDeclaration). This mistake was made in a
couple of other detectors as well.

(Also renamed test class which did not have the correct name (detector
class + "Test") so jump to test did not work.)

Change-Id:I0ecf99ab7d0357a03e300b7197ae84079f0ddbd1*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 69a44f3..270ffa4 100644

//Synthetic comment -- @@ -91,6 +91,7 @@
import lombok.ast.AnnotationValue;
import lombok.ast.ArrayInitializer;
import lombok.ast.ClassDeclaration;
import lombok.ast.Expression;
import lombok.ast.MethodDeclaration;
import lombok.ast.Modifiers;
//Synthetic comment -- @@ -1780,6 +1781,13 @@
if (isSuppressed(issue, declaration.astModifiers())) {
return true;
}
} else if (type == ClassDeclaration.class) {
// Class
ClassDeclaration declaration = (ClassDeclaration) scope;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 41086ca..48fdbeb 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
//Synthetic comment -- @@ -75,13 +76,13 @@
return Collections.singletonList("edit"); //$NON-NLS-1$
}

    private MethodDeclaration findSurroundingMethod(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
// The Lombok AST uses a flat hierarchy of node type implementation classes
// so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class) {
                return (MethodDeclaration) scope;
}

scope = scope.getParent();
//Synthetic comment -- @@ -112,7 +113,7 @@
return;
}

        MethodDeclaration method = findSurroundingMethod(node.getParent());
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index 620cc5f..7437a8a 100644

//Synthetic comment -- @@ -62,6 +62,7 @@

import lombok.ast.AstVisitor;
import lombok.ast.CharLiteral;
import lombok.ast.Expression;
import lombok.ast.FloatingPointLiteral;
import lombok.ast.ForwardingAstVisitor;
//Synthetic comment -- @@ -790,31 +791,30 @@
// Found a String.format call
// Look inside to see if we can find an R string
// Find surrounding method
                    lombok.ast.Node current = node.getParent();
                    while (current != null && !(current instanceof MethodDeclaration)) {
                        current = current.getParent();
                    }
                    if (current instanceof MethodDeclaration) {
                        checkStringFormatCall(context, (MethodDeclaration) current, node);
                    }
}
}
} else {
// getResources().getString(R.string.foo, arg1, arg2, ...)
// Check that the arguments in R.string.foo match arg1, arg2, ...
if (node.astArguments().size() > 1 && node.astOperand() != null ) {
                // Multiple arguments: formatted form of the String.format list
                lombok.ast.Node current = node.getParent();
                while (current != null && !(current instanceof MethodDeclaration)) {
                    current = current.getParent();
                }
                if (current instanceof MethodDeclaration) {
                    checkStringFormatCall(context, (MethodDeclaration) current, node);
                }
}
}
}

/**
* Check the given String.format call (with the given arguments) to see if
* the string format is being used correctly
//Synthetic comment -- @@ -825,7 +825,7 @@
*/
private void checkStringFormatCall(
JavaContext context,
            MethodDeclaration method,
MethodInvocation call) {

StrictListAccessor<Expression, MethodInvocation> args = call.astArguments();
//Synthetic comment -- @@ -974,7 +974,7 @@
*/
private static class StringTracker extends ForwardingAstVisitor {
/** Method we're searching within */
        private final MethodDeclaration mTop;
/** Map from variable name to corresponding string resource name */
private final Map<String, String> mMap = new HashMap<String, String>();
/** Map from variable name to corresponding type */
//Synthetic comment -- @@ -988,7 +988,7 @@
*/
private String mName;

        public StringTracker(MethodDeclaration top, MethodInvocation targetNode) {
mTop = top;
mTargetNode = targetNode;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index 23ff794..b15d149 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.IntegralLiteral;
//Synthetic comment -- @@ -74,13 +75,13 @@
return Collections.singletonList("makeText"); //$NON-NLS-1$
}

    private MethodDeclaration findSurroundingMethod(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
// The Lombok AST uses a flat hierarchy of node type implementation classes
// so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class) {
                return (MethodDeclaration) scope;
}

scope = scope.getParent();
//Synthetic comment -- @@ -117,7 +118,7 @@
}
}

        MethodDeclaration method = findSurroundingMethod(node.getParent());
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SetJavaScriptEnabledTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetectorTest.java
similarity index 94%
rename from lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SetJavaScriptEnabledTest.java
rename to lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetectorTest.java
//Synthetic comment -- index 41d67dc..f5d2b60 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class SetJavaScriptEnabledTest extends AbstractCheckTest {
@Override
protected Detector getDetector() {
return new SetJavaScriptEnabledDetector();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index 167478d..574b23a 100644

//Synthetic comment -- @@ -27,8 +27,8 @@

public void test() throws Exception {
assertEquals(
            "SharedPrefsTest.java:54: Warning: SharedPreferences.edit() without a " +
            "corresponding commit() or apply() call",

lintProject("src/test/pkg/SharedPrefsTest.java.txt=>" +
"src/test/pkg/SharedPrefsTest.java"));








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/StringFormatDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/StringFormatDetectorTest.java
//Synthetic comment -- index 4f497b3..a53440e 100644

//Synthetic comment -- @@ -41,6 +41,8 @@
"=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
"pkg/StringFormatActivity.java:26: Error: Wrong argument count, format string hello2 requires 3 but format call supplies 2\n" +
"=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
"values-es/formatstrings.xml:3: Error: Inconsistent formatting types for argument #1 in format string hello ('%1$d'): Found both 's' and 'd' (in values/formatstrings.xml)\n" +
"=> values/formatstrings.xml:3: Conflicting argument type here\n" +
"values-es/formatstrings.xml:4: Warning: Inconsistent number of arguments in formatting string hello2; found both 2 and 3\n" +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ToastDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ToastDetectorTest.java
//Synthetic comment -- index d0981e9..0b8c984 100644

//Synthetic comment -- @@ -29,7 +29,8 @@
assertEquals(
"ToastTest.java:31: Warning: Toast created but not shown: did you forget to call show() ?\n" +
"ToastTest.java:32: Warning: Expected duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG, a custom duration value is not supported\n" +
            "ToastTest.java:32: Warning: Toast created but not shown: did you forget to call show() ?",

lintProject("src/test/pkg/ToastTest.java.txt=>src/test/pkg/ToastTest.java"));
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SetJavaScriptEnabled.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SetJavaScriptEnabled.java.txt
//Synthetic comment -- index b3af229..ad8383a 100644

//Synthetic comment -- @@ -15,4 +15,14 @@
webView.getSettings().setJavaScriptEnabled(false); // good
webView.loadUrl("file:///android_asset/www/index.html");
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt
//Synthetic comment -- index 557fc51..5261e34 100644

//Synthetic comment -- @@ -55,5 +55,12 @@
editor.putString("foo", "bar");
editor.putInt("bar", 42);
}
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/StringFormatActivity.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/StringFormatActivity.java.txt
//Synthetic comment -- index 88ed84d..c22828f 100644

//Synthetic comment -- @@ -25,4 +25,11 @@
getResources().getString(hello2, target, "How are you");
getResources().getString(R.string.hello2, target, "How are you");
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/ToastTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/ToastTest.java.txt
//Synthetic comment -- index 9d7eafd..ce8af3a 100644

//Synthetic comment -- @@ -7,30 +7,35 @@

public class ToastTest {
private Toast createToast(Context context) {
	// Don't warn here
	return Toast.makeText(context, "foo", Toast.LENGTH_LONG);
}

private void showToast(Context context) {
	// Don't warn here
	Toast toast = Toast.makeText(context, "foo", Toast.LENGTH_LONG);
	System.out.println("Other intermediate code here");
	int temp = 5 + 2;
	toast.show();
}

private void showToast2(Context context) {
	// Don't warn here
	int duration = Toast.LENGTH_LONG;
	Toast.makeText(context, "foo", Toast.LENGTH_LONG).show();
	Toast.makeText(context, R.string.app_name, duration).show();
}

private void broken(Context context) {
	// Errors
	Toast.makeText(context, "foo", Toast.LENGTH_LONG);
	Toast toast = Toast.makeText(context, R.string.app_name, 5000);
	toast.getDuration();
}
}








