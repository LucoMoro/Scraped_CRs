/*Use env var com.android.ddms.bindir for ddms.

The env var is used if the system property is not defined,
which is convenient to start ddms in debug mode from Eclipse.

Change-Id:I2a33c12399c703cf3194e2e67f39562ab0d59bd9*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/Main.java b/ddms/app/src/com/android/ddms/Main.java
//Synthetic comment -- index 0c55078..c8bab66 100644

//Synthetic comment -- @@ -84,6 +84,12 @@
// get the ddms parent folder location
String ddmsParentLocation = System.getProperty("com.android.ddms.bindir"); //$NON-NLS-1$

        if (ddmsParentLocation == null) {
            // Tip: for debugging DDMS in eclipse, set this env var to the SDK/tools
            // directory path.
            ddmsParentLocation = System.getenv("com.android.ddms.bindir"); //$NON-NLS-1$
        }

// we're past the point where ddms can be called just to send a ping, so we can
// ping for ddms itself.
ping(ddmsParentLocation);







