/*Update Eclipse Add Target API annotations for new InlinedApi check. DO NOT MERGE

Change-Id:I95eb67c2e9d5ee57de470aeef91b37cbf13e2bc4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index 59dbe82..d7a0758 100644

//Synthetic comment -- @@ -332,7 +332,8 @@
}

int api = -1;
        if (id.equals(ApiDetector.UNSUPPORTED.getId()) ||
                id.equals(ApiDetector.INLINED.getId())) {
String message = marker.getAttribute(IMarker.MESSAGE, null);
if (message != null) {
Pattern pattern = Pattern.compile("\\s(\\d+)\\s"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java
//Synthetic comment -- index fdb0383..0d751cb 100644

//Synthetic comment -- @@ -177,7 +177,8 @@
fixes.add(new AddSuppressAttribute(editor, id, marker, element, desc, null));

int api = -1;
        if (id.equals(ApiDetector.UNSUPPORTED.getId())
                || id.equals(ApiDetector.INLINED.getId())) {
String message = marker.getAttribute(IMarker.MESSAGE, null);
if (message != null) {
Pattern pattern = Pattern.compile("\\s(\\d+)\\s"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index de7706e..936f882 100644

//Synthetic comment -- @@ -315,6 +315,7 @@
return;
}
}
        assert name != null; // Eclipse can't infer this
int api = mApiDatabase.getFieldVersion(owner, name);
int minSdk = getMinSdk(context);
if (api > minSdk && api > context.getFolderVersion()







