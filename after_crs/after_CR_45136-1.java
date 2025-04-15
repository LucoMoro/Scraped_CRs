/*Fix incorrect assertion

(cherry picked from commit 62af68eaa1453456b05109e70a760ead67a82546)

Change-Id:Id56dcfbd738748ab0243380fd6152d696e1ca7dc*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index 4b5bc84..251a65e 100644

//Synthetic comment -- @@ -265,7 +265,7 @@
if (!label.equals(OK_LABEL)
&& isEnglishResource(context)
&& context.isEnabled(CASE)) {
                                        assert text.trim().equalsIgnoreCase(OK_LABEL);
context.report(CASE, child, context.getLocation(child),
String.format(
"The standard Android way to capitalize %1$s " +







