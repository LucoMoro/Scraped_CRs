/*Deactive the logging code used for issue 15003.

This doesn't quite remove the logging code,
just deactivate it for right now.

Change-Id:I9a471926f6575bce117bf6079200f7939457fb57*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 4fe889d..581c49c 100644

//Synthetic comment -- @@ -117,8 +117,11 @@
/**
* Temporary logging code to help track down
* http://code.google.com/p/android/issues/detail?id=15003
     *
     * Deactivated right now.
     * TODO remove this and associated logging code once we're done with issue 15003.
*/
    public static final boolean DEBUG_XML_FILE_INIT = false;

/** The plug-in ID */
public static final String PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$







