/*Change field getter threshold from API 8 to API 9

The Dalvik optimization to automatically inline getters
was introduced in Gingerbread, not Froyo.

Change-Id:If14288aa398206d4eebfa57ccc7ddfabc6dde83d*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index 6cddfd5..f126074 100644

//Synthetic comment -- @@ -62,9 +62,9 @@
"nothing other than return the field, you might want to just reference the " +
"local field directly instead.\n" +
"\n" +
            "NOTE: As of Android 2.3 (Gingerbread), this optimization is performed " +
            "automatically by Dalvik, so there is no need to change your code; this is " +
            "only relevant if you are targeting older versions of Android.",

Category.PERFORMANCE,
4,
//Synthetic comment -- @@ -94,8 +94,8 @@
@SuppressWarnings("rawtypes")
@Override
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        // As of Gingerbread/API 9, Dalvik performs this optimization automatically
        if (context.getProject().getMinSdk() >= 9) {
return;
}








