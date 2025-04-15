/*Fix parent node for toast detector

This makes @SuppressLint work on a variable statement
creating a toast.

Change-Id:I98f06f2d015defd5f133f6ae0dee10ce6f957146*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/data/src/test/pkg/ToastTest.java.txt b/lint/cli/src/test/java/com/android/tools/lint/checks/data/src/test/pkg/ToastTest.java.txt
//Synthetic comment -- index ce8af3a..44ccaaa 100644

//Synthetic comment -- @@ -37,5 +37,15 @@
public ToastTest(Context context) {
Toast.makeText(context, "foo", Toast.LENGTH_LONG);
}
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index e245fe8..9bc7af5 100644

//Synthetic comment -- @@ -109,7 +109,7 @@
ShowFinder finder = new ShowFinder(node);
method.accept(finder);
if (!finder.isShowCalled()) {
            context.report(ISSUE, method, context.getLocation(node),
"Toast created but not shown: did you forget to call show() ?", null);
}
}







