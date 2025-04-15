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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/RefactoringUtil.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/RefactoringUtil.java
//Synthetic comment -- index 409cf72..adc4d5a 100644

//Synthetic comment -- @@ -41,49 +41,6 @@
private static boolean sRefactorAppPackage = false;

/**
     * Returns the new class name combined with a package name
     * the oldName and newName are class names as found in the manifest
     * (for instance with a leading dot or with a single element,
     * that needs to be recombined with a package name)
     *
     * @param javaPackage the package name
     * @param oldName the old name
     * @param newName the new name
     *
     * @return the new name
     */
    public static String getNewValue(String javaPackage, String oldName, String newName) {
        if (oldName == null || oldName.length() == 0) {
            return null;
        }
        if (javaPackage == null || javaPackage.length() == 0) {
            return null;
        }
        if (newName == null || newName.length() == 0) {
            return null;
        }
        if (!newName.startsWith(javaPackage + ".")) { //$NON-NLS-1$
            return newName;
        } else if (newName.length() > (javaPackage.length() + 1)) {
            String value = newName.substring(javaPackage.length() + 1);
            return value;
        }
        boolean startWithDot = (oldName.charAt(0) == '.');
        boolean hasDot = (oldName.indexOf('.') != -1);
        if (startWithDot || !hasDot) {

            if (startWithDot) {
                return "." + newName;
            } else {
                int lastPeriod = newName.lastIndexOf(".");
                return newName.substring(lastPeriod + 1);
            }
        } else {
            return newName;
        }
    }

    /**
* Releases SSE read model; saves SSE model if exists edit model
* Called in dispose method of refactoring change classes
*







