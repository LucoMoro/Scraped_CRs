/*Ensure the library path variable is valid.

Remove all non-valid characters and attempt
to reduce collisions.

Change-Id:Ie36924b80f9ed02dd46bfc12f211036257451014*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index b7de225..4985ab4 100644

//Synthetic comment -- @@ -1241,7 +1241,19 @@
* @param name the name of the library project.
*/
private String getLibraryVariableName(String name) {
        return "_android_" + name.replaceAll("-", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

/**







