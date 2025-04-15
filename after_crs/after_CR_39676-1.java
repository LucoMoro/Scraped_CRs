/*Fix String#format checks

Change-Id:I83023903a7744b1372a832dfdc56b595c21f4902*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index 015842f..620cc5f 100644

//Synthetic comment -- @@ -46,6 +46,7 @@

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
//Synthetic comment -- @@ -71,17 +72,19 @@
import lombok.ast.Select;
import lombok.ast.StrictListAccessor;
import lombok.ast.StringLiteral;
import lombok.ast.VariableDefinitionEntry;
import lombok.ast.VariableReference;

/**
* Check which looks for problems with formatting strings such as inconsistencies between
* translations or between string declaration and string usage in Java.
 * <p>
 * TODO: Handle Resources.getQuantityString as well
*/
public class StringFormatDetector extends ResourceXmlDetector implements Detector.JavaScanner {
/** The name of the String.format method */
private static final String FORMAT_METHOD = "format"; //$NON-NLS-1$
    private static final String GET_STRING_METHOD = "getString"; //$NON-NLS-1$

/** Whether formatting strings are invalid */
public static final Issue INVALID = Issue.create(
//Synthetic comment -- @@ -767,7 +770,7 @@

@Override
public List<String> getApplicableMethodNames() {
        return Arrays.asList(FORMAT_METHOD, GET_STRING_METHOD);
}

@Override
//Synthetic comment -- @@ -778,13 +781,29 @@
}

String methodName = node.astName().getDescription();
        if (methodName.equals(FORMAT_METHOD)) {
            // String.format(getResources().getString(R.string.foo), arg1, arg2, ...)
            // Check that the arguments in R.string.foo match arg1, arg2, ...
            if (node.astOperand() instanceof VariableReference) {
                VariableReference ref = (VariableReference) node.astOperand();
                if ("String".equals(ref.astIdentifier().astValue())) { //$NON-NLS-1$
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
//Synthetic comment -- @@ -1036,6 +1055,11 @@
String reference = ((Select) current).astIdentifier().astValue();

while (current != mTop && !(current instanceof VariableDefinitionEntry)) {
                    if (current == mTargetNode) {
                        mName = reference;
                        mDone = true;
                        return false;
                    }
current = current.getParent();
}
if (current instanceof VariableDefinitionEntry) {
//Synthetic comment -- @@ -1097,7 +1121,7 @@
} else if (expression instanceof MethodInvocation) {
MethodInvocation method = (MethodInvocation) expression;
String methodName = method.astName().astValue();
                if (methodName.equals(GET_STRING_METHOD)) {
return String.class;
}
} else if (expression instanceof StringLiteral) {
//Synthetic comment -- @@ -1114,11 +1138,5 @@

return null;
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/StringFormatDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/StringFormatDetectorTest.java
//Synthetic comment -- index 7dda069..4f497b3 100644

//Synthetic comment -- @@ -35,6 +35,12 @@
"=> values-es/formatstrings.xml:3: Conflicting argument declaration here\n" +
"pkg/StringFormatActivity.java:15: Error: Wrong argument count, format string hello2 requires 3 but format call supplies 2\n" +
"=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
            "pkg/StringFormatActivity.java:24: Error: Wrong argument count, format string hello2 requires 3 but format call supplies 2\n" +
            "=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
            "pkg/StringFormatActivity.java:25: Error: Wrong argument count, format string hello2 requires 3 but format call supplies 2\n" +
            "=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
            "pkg/StringFormatActivity.java:26: Error: Wrong argument count, format string hello2 requires 3 but format call supplies 2\n" +
            "=> values-es/formatstrings.xml:4: This definition requires 3 arguments\n" +
"values-es/formatstrings.xml:3: Error: Inconsistent formatting types for argument #1 in format string hello ('%1$d'): Found both 's' and 'd' (in values/formatstrings.xml)\n" +
"=> values/formatstrings.xml:3: Conflicting argument type here\n" +
"values-es/formatstrings.xml:4: Warning: Inconsistent number of arguments in formatting string hello2; found both 2 and 3\n" +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/StringFormatActivity.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/StringFormatActivity.java.txt
//Synthetic comment -- index e1593fd..88ed84d 100644

//Synthetic comment -- @@ -21,5 +21,8 @@
String output4 = String.format(score, true);  // wrong
String output4 = String.format(score, won);   // wrong
String output5 = String.format(score, 75);
        String.format(getResources().getString(R.string.hello2), target, "How are you");
        getResources().getString(hello2, target, "How are you");
        getResources().getString(R.string.hello2, target, "How are you");
}
}







