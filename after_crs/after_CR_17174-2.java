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
        /*
         * From the javadoc of IPathVariableManager:
         * A path variable is a pair of non-null elements (name,value) where name is a
         * case-sensitive string (containing only letters, digits and the underscore character,
         * and not starting with a digit), and value is an absolute IPath object.
         */

        // the variable name is made by:
        // - prepending _android_ (this ensure there's no digit at the start)
        // - removing all unsupported characters.
        // - append the hashcode of the original name. This should help reduce collisions.
        String validName = name.replaceAll("[^0-9a-zA-Z]+", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        return "_android_" + validName + "_" + Integer.toString(name.hashCode()) ;  //$NON-NLS-1$ //$NON-NLS-2$
}

/**







