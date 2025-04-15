/*More char replacement from project name to link var.

. and space in the Eclipse project name are now
replaced with _ to make up the linked path variable
used by the library linked source folder.

Change-Id:Ie36924b80f9ed02dd46bfc12f211036257451014*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index b7de225..eab0563 100644

//Synthetic comment -- @@ -1241,7 +1241,7 @@
* @param name the name of the library project.
*/
private String getLibraryVariableName(String name) {
        return "_android_" + name.replaceAll("-", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

/**







