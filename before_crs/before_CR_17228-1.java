/*Fix var path name generation.

The hashcode can be a negative int.
Convert it to an always positive long and convert
to hexadecimal.

Change-Id:Ia655dd92ef1986834680cb942ac004eb7374916f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 4985ab4..61d35c2 100644

//Synthetic comment -- @@ -1253,7 +1253,10 @@
// - removing all unsupported characters.
// - append the hashcode of the original name. This should help reduce collisions.
String validName = name.replaceAll("[^0-9a-zA-Z]+", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        return "_android_" + validName + "_" + Integer.toString(name.hashCode()) ;  //$NON-NLS-1$ //$NON-NLS-2$
}

/**







