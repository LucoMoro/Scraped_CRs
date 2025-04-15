/*Fix incorrect assertion

Change-Id:Id110d90b173d052b952f90622bcbff64f5bedbe5*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index 4b5bc84..251a65e 100644

//Synthetic comment -- @@ -265,7 +265,7 @@
if (!label.equals(OK_LABEL)
&& isEnglishResource(context)
&& context.isEnabled(CASE)) {
                                        assert text.equalsIgnoreCase(OK_LABEL);
context.report(CASE, child, context.getLocation(child),
String.format(
"The standard Android way to capitalize %1$s " +







