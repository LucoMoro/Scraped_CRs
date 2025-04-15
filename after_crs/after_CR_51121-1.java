/*Make color usage detector catch additional scenarios

It now also flags
  textView.setTextColor(foo > 0 ? R.color.green : R.color.blue)
as incorrect (and in general, expressions using R.color constants
as part of a set color call, where the expression isn't passed
to resources.getColor().

Change-Id:I5cee955c2b6da0cdd6d5ab39df40941078098593*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ColorUsageDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ColorUsageDetectorTest.java
//Synthetic comment -- index d1a04e1..3a48c5d 100644

//Synthetic comment -- @@ -36,8 +36,13 @@
"src/test/pkg/WrongColor.java:12: Error: Should pass resolved color instead of resource id here: getResources().getColor(android.R.color.red) [ResourceAsColor]\n" +
"        textView.setTextColor(android.R.color.red);\n" +
"                              ~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongColor.java:13: Error: Should pass resolved color instead of resource id here: getResources().getColor(R.color.blue) [ResourceAsColor]\n" +
            "        textView.setTextColor(foo > 0 ? R.color.green : R.color.blue);\n" +
            "                                                        ~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongColor.java:13: Error: Should pass resolved color instead of resource id here: getResources().getColor(R.color.green) [ResourceAsColor]\n" +
            "        textView.setTextColor(foo > 0 ? R.color.green : R.color.blue);\n" +
            "                                        ~~~~~~~~~~~~~\n" +
            "5 errors, 0 warnings\n",

lintProject("src/test/pkg/WrongColor.java.txt=>src/test/pkg/WrongColor.java"));
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/data/src/test/pkg/WrongColor.java.txt b/lint/cli/src/test/java/com/android/tools/lint/checks/data/src/test/pkg/WrongColor.java.txt
//Synthetic comment -- index cacd834..79e7904 100644

//Synthetic comment -- @@ -10,6 +10,7 @@
// Wrong
textView.setTextColor(R.color.red);
textView.setTextColor(android.R.color.red);
        textView.setTextColor(foo > 0 ? R.color.green : R.color.blue);
// OK
textView.setTextColor(getResources().getColor(R.color.red));
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index 85be257..2bebd3c 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import java.io.File;

import lombok.ast.AstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Select;
//Synthetic comment -- @@ -90,18 +91,24 @@
select = select.getParent();
}

            Node current = select.getParent();
            while (current != null) {
                if (current.getClass() == MethodInvocation.class) {
                    MethodInvocation call = (MethodInvocation) current;
                    String methodName = call.astName().astValue();
                    if (methodName.endsWith("Color")              //$NON-NLS-1$
                            && methodName.startsWith("set")) {    //$NON-NLS-1$
                        context.report(
                                ISSUE, select, context.getLocation(select), String.format(
                                    "Should pass resolved color instead of resource id here: " +
                                    "getResources().getColor(%1$s)", select.toString()),
                                null);
                    }
                    break;
                } else if (current.getClass() == MethodDeclaration.class) {
                    break;
}
                current = current.getParent();
}
}
}







