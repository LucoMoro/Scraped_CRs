/*Fixing issue with updating AndroidManifest.xml when refactoring activity

This change fixeshttp://code.google.com/p/android/issues/detail?id=14729The problem happens when an activity is in subpackage of application
package.

Change-Id:If7ce1a2bcdaf6a51280477ec44c19c1dbea1c20f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidDocumentChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidDocumentChange.java
//Synthetic comment -- index f6677f8..fe3c014 100755

//Synthetic comment -- @@ -277,7 +277,7 @@
if (name != null) {
String newValue;
if (combinePackage) {
                newValue = RefactoringUtil.getNewValue(getAppPackage(), name, newName);
} else {
newValue = newName;
}







