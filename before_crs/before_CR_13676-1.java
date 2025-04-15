/*The ADB dump command sometimes give a null pointer exception

Using the ADB DUMP command to get the string content from the current screen
state occationally fails with an exception in ViewDebug.

The tell tale sign of this issue is:

java.lang.NullPointerException at
android.view.ViewDebug.dumpViewWithProperties(ViewDebug.java:1004) at
android.view.ViewDebug.dumpViewHierarchyWithProperties(ViewDebug.java:991) at
android.view.ViewDebug.dumpViewHierarchyWithProperties(ViewDebug.java:989) at
android.view.ViewDebug.dumpViewHierarchyWithProperties(ViewDebug.java:989) at
...

Fixed by adding a null check.*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewDebug.java b/core/java/android/view/ViewDebug.java
//Synthetic comment -- index 4baf612..aa986c0 100644

//Synthetic comment -- @@ -972,21 +972,30 @@
private static boolean dumpViewWithProperties(Context context, View view,
BufferedWriter out, int level) {

        try {
            for (int i = 0; i < level; i++) {
out.write(' ');
}
            out.write(view.getClass().getName());
            out.write('@');
            out.write(Integer.toHexString(view.hashCode()));
            out.write(' ');
            dumpViewProperties(context, view, out);
            out.newLine();
        } catch (IOException e) {
            Log.w("View", "Error while dumping hierarchy tree");
            return false;
}
        return true;
}

private static Field[] getExportedPropertyFields(Class<?> klass) {







