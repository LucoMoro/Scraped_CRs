/*Unit test fix

Change-Id:Iaaede955589ce962b33eb4e326fc012a4fa7b591*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java
//Synthetic comment -- index 2f43cf0..8b6ddbb 100644

//Synthetic comment -- @@ -787,7 +787,6 @@
}

if (issue == SecurityDetector.EXPORTED_SERVICE
                                || issue == SecurityDetector.EXPORTED_ACTIVITY
|| issue == SecurityDetector.EXPORTED_PROVIDER
|| issue == SecurityDetector.EXPORTED_RECEIVER) {
// Don't complain about missing permissions when exporting: the







